package vending.producto;

public class ProductoFactory {
    public static Producto getProducto(String prdType, String nombre, double precio, boolean zero) throws Exception {
        switch(prdType){
            case "Golosina":
                return new Golosina(nombre,precio);
            case "Refresco":
                return new Refresco(nombre,precio,zero);
            default:
                throw new RuntimeException("Produco no soportado");
        }
    }
}
