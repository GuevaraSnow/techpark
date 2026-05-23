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
            Operador op = new Operador(
                    obj.get("nombre").getAsString(),
                    obj.get("documento").getAsString()
            );
            String nombreZona = obj.get("zona").getAsString();
            Zona zona = parque.buscarZona(nombreZona);
            if (zona != null) zona.agregarOperador(op);
            parque.agregarEmpleado(op);
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
        JsonArray zonas = new JsonArray();

        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            JsonObject z = new JsonObject();
            z.addProperty("nombre", parque.getZonas().obtener(i).getNombre());
            zonas.add(z);
        }
        root.add("zonas", zonas);

        try (Writer writer = new FileWriter(rutaArchivo)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
            System.out.println("✅ Estado guardado en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar: " + e.getMessage());
        }
    }
}