package Model;

public abstract class Empleado extends Persona {
    protected String cargo;

    public Empleado(String nombre, String documento, String cargo) {
        super(nombre, documento);
        this.cargo = cargo;
    }

    public String getCargo() { return cargo; }
}
