package vending.producto;


// CLASE ABSTARCT
public abstract class Producto {
    public String nombre;
    public double precio;

    public Producto(String nombre, double precio) throws Exception {
        this.nombre = nombre;
        this.precio = precio;
        if (nombre == "")
            throw new IllegalArgumentException("nombre inválido");
        if (precio <= 0)
            throw new IllegalArgumentException("precio inválido");

    }

    public boolean esCeroCal() {
        return false;
    }

}
