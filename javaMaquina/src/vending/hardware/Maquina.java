package vending.hardware;

import java.util.HashMap;
import java.util.Map;

import vending.hardware.subsistemas.productos.Dispensador.Coordenadas;
import vending.hardware.subsistemas.productos.Dispensador;
import vending.hardware.subsistemas.pagos.*;
import vending.hardware.subsistemas.seguridad.ISeguridad;
import vending.producto.Producto;

public class Maquina {
    String empresa;

    // COMPOSICION DE CLASES
    Dispensador dispensador;
    ISeguridad seguridad;
    ControladorDePagos ctrl;

    // Caché de Coordenadas
    // Introducción concepto de caché
    // e introducción de java Generics y de Mapas o Tablas Hash
    Map<Integer, Coordenadas> cacheCoordenadas = new HashMap<Integer, Coordenadas>();

    /**
     * Constructor de Empresa
     * @param empresa
     * @param dispensador
     * @param seguridad
     * @param ctrl
     */
    public Maquina(String empresa, Dispensador dispensador, ISeguridad seguridad, ControladorDePagos ctrl) {
        this.empresa = empresa;
        this.dispensador = dispensador;
        this.seguridad = seguridad;
        this.ctrl = ctrl;
    }

    /**
     * Obtencion del nombre de la maquina
     * @return
     */
    public String getName() {
        return empresa;
    }

    // Interfaz de Seguridad
    public boolean esPinValido(String pin) {
        return seguridad.esValido(pin);
    };

    public int esPagoValido(Efectivo pago, double precio) {
        return ctrl.esPagoValido(pago, precio);
    }

    // Interfaz de Controlador Pagos
    public Efectivo procederAlPago(Efectivo pago, double precio) {
        return ctrl.procederAlPago(pago, precio);
    }

    // Interfaz del Dispensador
    public boolean esXYValido(int xy) {
        // Si xy es valido lo guardamos en la caché
        // y debe ser el primer metodo utilizado
        // antes de proceder a más invocaciones con xy
        if (cacheCoordenadas.get(xy) != null) return true;
        try {
            cacheCoordenadas.put(xy, dispensador.obtenerCoordenada(xy));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    Coordenadas coordenada(int xy) {
        // Validador de coordenadas
        Coordenadas c = cacheCoordenadas.get(xy);
        if (c == null)
            throw new IllegalArgumentException("coordenada no registrada");
        return c;
    }

    public boolean hayProducto(int xy) {
        return dispensador.hayProducto(coordenada(xy));
    }

    public String efectuarRetirada(int xy) {
        return dispensador.efectuarRetirada(coordenada(xy));
    }

    public double obtenerPrecio(int xy) {
        return dispensador.obtenerPrecio(coordenada(xy));
    }

    public String obtenerNombreProdcuto(int xy) {
        return dispensador.obtenerNombreProdcuto(coordenada(xy));
    }

    // Trapaso de informacion
    public String[][] mostrarProductos() {
        // hacemos un DTO que es un string (DataTransferObject) con
        // nombre ; cero ; precio ; cantidad
        String[][] muestra = new String[dispensador.matriz.length][dispensador.matriz[0].length];
        for (int f = 0; f < muestra.length; f++)
            for (int c = 0; c < muestra[0].length; c++) {
                Producto prod = dispensador.matriz[f][c];
                muestra[f][c] = prod.nombre + ";" + prod.esCeroCal() + ";" + String.format("%.2f", prod.precio);
            }
        return muestra;
    }

    public String[][] mostrarProductosAdmin() {
        String[][] muestra = mostrarProductos();
        for (int f = 0; f < muestra.length; f++)
            for (int c = 0; c < muestra[0].length; c++) {
                muestra[f][c] = muestra[f][c] + ";" + dispensador.cantidad[f][c];
            }
        return muestra;
    }

    // Actuación del reponedor
    public double rellenar() {
        dispensador.reponerProductos();
        ctrl.recaudar();
        return ctrl.rellenarCaja();
    }

    public String informe() {
        return ctrl.informe();
    }

    public void apagar(){
        // Apagando máquina
        // Lo sobreescribiremos luego
    }

    // toString
    public String toString() {
        return empresa + " " + this.dispensador.matriz.length + "x" + this.dispensador.matriz[0].length;
    }

}
