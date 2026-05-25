package model;

public abstract class Persona {

    protected String nombre;
    protected String documento;
    protected String fotoRuta; // ruta de la foto de perfil (opcional)

    public Persona(String nombre, String documento) {
        this.nombre = nombre;
        this.documento = documento;
        this.fotoRuta = null;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getFotoRuta() { return fotoRuta; }
    public void setFotoRuta(String fotoRuta) { this.fotoRuta = fotoRuta; }

    @Override
    public String toString() {
        return nombre + " (Doc: " + documento + ")";
    }
}