package model.persistencia;

import com.google.gson.*;
import model.Administrador;
import model.Empleado;
import model.Operador;
import model.Parque;
import model.Visitante;
import model.atraccion.Atraccion;
import model.atraccion.Zona;
import model.ticket.TicketFamiliar;
import model.ticket.TicketFastPass;
import model.ticket.TicketGeneral;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataLoader {

    private Parque parque;

    public DataLoader(Parque parque) {
        this.parque = parque;
    }

    // Carga todo el escenario desde un archivo JSON
    public void cargarEscenario(String rutaArchivo) {
        try {
            String contenido = Files.readString(Path.of(rutaArchivo));
            JsonObject root = JsonParser.parseString(contenido).getAsJsonObject();

            cargarZonas(root.getAsJsonArray("zonas"));
            cargarAtracciones(root.getAsJsonArray("atracciones"));
            cargarVisitantes(root.getAsJsonArray("visitantes"));
            cargarOperadores(root.getAsJsonArray("operadores"));
            cargarSenderos(root.getAsJsonArray("senderos"));

            System.out.println("✅ Escenario cargado correctamente desde: " + rutaArchivo);

        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo: " + e.getMessage());
        } catch (JsonParseException e) {
            System.err.println("❌ Error al parsear JSON: " + e.getMessage());
        }
    }

    // ── Zonas ─────────────────────────────────────────────────────
    private void cargarZonas(JsonArray jsonZonas) {
        for (JsonElement elem : jsonZonas) {
            JsonObject obj = elem.getAsJsonObject();
            String nombre = obj.get("nombre").getAsString();
            parque.agregarZona(new Zona(nombre));
        }
        System.out.println("   → Zonas cargadas: " + parque.getZonas().tamaño());
    }

    // ── Atracciones ───────────────────────────────────────────────
    private void cargarAtracciones(JsonArray jsonAtracciones) {
        for (JsonElement elem : jsonAtracciones) {
            JsonObject obj = elem.getAsJsonObject();
            Atraccion a = new Atraccion(
                    obj.get("id").getAsInt(),
                    obj.get("nombre").getAsString(),
                    obj.get("tipo").getAsString(),
                    obj.get("capacidad").getAsInt(),
                    obj.get("alturaMin").getAsDouble(),
                    obj.get("edadMin").getAsInt(),
                    obj.get("costoExtra").getAsDouble()
            );
            String zona = obj.get("zona").getAsString();
            parque.agregarAtraccionAZona(zona, a);
        }
        System.out.println("   → Atracciones cargadas: " + parque.getMapa().tamaño());
    }

    // ── Visitantes ────────────────────────────────────────────────
    private void cargarVisitantes(JsonArray jsonVisitantes) {
        for (JsonElement elem : jsonVisitantes) {
            JsonObject obj = elem.getAsJsonObject();
            Visitante v = new Visitante(
                    obj.get("nombre").getAsString(),
                    obj.get("documento").getAsString(),
                    obj.get("edad").getAsInt(),
                    obj.get("estatura").getAsDouble()
            );
            v.recargarSaldo(obj.get("saldo").getAsDouble());

            String tipo = obj.get("tipoTicket").getAsString();
            switch (tipo) {
                case "FASTPASS" -> v.setTicket(new TicketFastPass(25000));
                case "FAMILIAR" -> v.setTicket(new TicketFamiliar(18000, 4));
                default         -> v.setTicket(new TicketGeneral(15000));
            }
            parque.agregarVisitante(v);
        }
        System.out.println("   → Visitantes cargados: " + parque.getVisitantes().tamaño());
    }

    // ── Operadores ────────────────────────────────────────────────
    private void cargarOperadores(JsonArray jsonOperadores) {
        for (JsonElement elem : jsonOperadores) {
            JsonObject obj = elem.getAsJsonObject();
            String nombre   = obj.get("nombre").getAsString();
            String documento = obj.get("documento").getAsString();
            boolean esAdmin = obj.has("esAdmin") && obj.get("esAdmin").getAsBoolean();

            if (esAdmin) {
                Administrador admin = new Administrador(nombre, documento);
                parque.agregarEmpleado(admin);
            } else {
                Operador op = new Operador(nombre, documento);
                String nombreZona = obj.get("zona").getAsString();
                Zona zona = parque.buscarZona(nombreZona);
                if (zona != null) zona.agregarOperador(op);
                parque.agregarEmpleado(op);
            }
        }
        System.out.println("   → Operadores cargados: " + parque.getEmpleados().tamaño());
    }

    // ── Senderos (aristas del grafo) ──────────────────────────────
    private void cargarSenderos(JsonArray jsonSenderos) {
        int contador = 0;
        for (JsonElement elem : jsonSenderos) {
            JsonObject obj = elem.getAsJsonObject();
            int origen  = obj.get("origen").getAsInt();
            int destino = obj.get("destino").getAsInt();
            double peso = obj.get("peso").getAsDouble();
            parque.getMapa().agregarArista(origen, destino, peso);
            contador++;
        }
        System.out.println("   → Senderos cargados: " + contador);
    }

    // ── Guardar estado actual del parque en JSON ──────────────────
    public void guardarEstado(String rutaArchivo) {
        JsonObject root = new JsonObject();

        // Zonas
        JsonArray zonas = new JsonArray();
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            JsonObject z = new JsonObject();
            z.addProperty("nombre", parque.getZonas().obtener(i).getNombre());
            zonas.add(z);
        }
        root.add("zonas", zonas);

        // Atracciones
        JsonArray atracciones = new JsonArray();
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona zona = parque.getZonas().obtener(i);
            for (int j = 0; j < zona.getAtracciones().tamaño(); j++) {
                Atraccion a = zona.getAtracciones().obtener(j);
                JsonObject obj = new JsonObject();
                obj.addProperty("id",        a.getId());
                obj.addProperty("nombre",    a.getNombre());
                obj.addProperty("tipo",      a.getTipo());
                obj.addProperty("capacidad", a.getCapacidad());
                obj.addProperty("alturaMin", a.getAlturaMin());
                obj.addProperty("edadMin",   a.getEdadMin());
                obj.addProperty("costoExtra",a.getCostoExtra());
                obj.addProperty("zona",      zona.getNombre());
                atracciones.add(obj);
            }
        }
        root.add("atracciones", atracciones);

        // Visitantes
        JsonArray visitantes = new JsonArray();
        for (int i = 0; i < parque.getVisitantes().tamaño(); i++) {
            model.Visitante v = parque.getVisitantes().obtener(i);
            JsonObject obj = new JsonObject();
            obj.addProperty("nombre",     v.getNombre());
            obj.addProperty("documento",  v.getDocumento());
            obj.addProperty("edad",       v.getEdad());
            obj.addProperty("estatura",   v.getEstatura());
            obj.addProperty("saldo",      v.getSaldo());
            obj.addProperty("tipoTicket", v.getTicket() != null ?
                    v.getTicket().getTipo().toString() : "GENERAL");
            visitantes.add(obj);
        }
        root.add("visitantes", visitantes);

        // Operadores
        JsonArray operadores = new JsonArray();
        for (int i = 0; i < parque.getEmpleados().tamaño(); i++) {
            model.Empleado emp = parque.getEmpleados().obtener(i);
            if (emp instanceof model.Operador) {
                model.Operador op = (model.Operador) emp;
                JsonObject obj = new JsonObject();
                obj.addProperty("nombre",    op.getNombre());
                obj.addProperty("documento", op.getDocumento());
                // Buscar zona del operador
                String zonaOp = "Sin zona";
                for (int j = 0; j < parque.getZonas().tamaño(); j++) {
                    Zona zona = parque.getZonas().obtener(j);
                    for (int k = 0; k < zona.getOperadores().tamaño(); k++) {
                        if (zona.getOperadores().obtener(k)
                                .getDocumento().equals(op.getDocumento())) {
                            zonaOp = zona.getNombre();
                        }
                    }
                }
                obj.addProperty("zona", zonaOp);
                operadores.add(obj);
            }
        }
        root.add("operadores", operadores);

        // Senderos
        JsonArray senderos = new JsonArray();
        model.estructuras.ListaEnlazada<Atraccion> nodos =
                parque.getMapa().getAtracciones();
        for (int i = 0; i < nodos.tamaño(); i++) {
            Atraccion origen = nodos.obtener(i);
            model.estructuras.ListaEnlazada<Integer> vecinos =
                    parque.getMapa().getVecinos(origen.getId());
            for (int j = 0; j < vecinos.tamaño(); j++) {
                int idDest = vecinos.obtener(j);
                if (idDest <= origen.getId()) continue;
                JsonObject obj = new JsonObject();
                obj.addProperty("origen",  origen.getId());
                obj.addProperty("destino", idDest);
                obj.addProperty("peso",    parque.getMapa().getPeso(origen.getId(), idDest));
                senderos.add(obj);
            }
        }
        root.add("senderos", senderos);

        try (Writer writer = new FileWriter(rutaArchivo)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
            System.out.println("✅ Estado guardado en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar: " + e.getMessage());
        }
    }
}
