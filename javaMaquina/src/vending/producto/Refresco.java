package vending.producto;

public class Refresco extends Producto {

    boolean zeroCal;

    // SOBRE CARGA DE CONSTRUCTORES
    public Refresco(String nombre, double precio) throws Exception {
        super(nombre, precio); // INVOCAR SUPER CONSTRUCTOR
    }

    public Refresco(String nombre, double precio, boolean zeroCal) throws Exception {
        this(nombre, precio); // INVOCACION CONSTRUCTOR EN LA MISMA CLASE
        this.zeroCal = zeroCal;
    }

    // SOBREESCRITURA DE METODO
    @Override
    public boolean esCeroCal() {
        return zeroCal;
    }
}
