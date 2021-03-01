package vending.hardware.subsistemas.productos;

import vending.producto.Producto;

public class Dispensador {
    // AGREGACION DE CLASE
    public Producto[][] matriz;
    public int[][] cantidad;
    static final int MAXCANTIDAD = 10;
    private int maxx;
    private int maxy;
  
    public Dispensador(Producto[][] matriz) {
        this.matriz = matriz;
        this.cantidad = new int[matriz.length][matriz[0].length];
        this.maxx = this.matriz.length - 1;
        this.maxy = this.matriz[0].length - 1;
    }

    // Coordenadas es un candidato perfecto para convertirse en Clase
    // Es perfecto para ser una clase interna
    // Y validarlo mediante try-catch
    public class Coordenadas {
        int x;
        int y;

        Coordenadas(int xy) {
            if (xy < 0 || xy > 99)
                throw new IllegalArgumentException("coordenada fuera de rango 0..99");
            x = xy / 10;
            y = xy % 10;
            // maxx = Dispensador.this.matriz.length - 1;
            if (x > maxx || y > maxy)
                throw new IllegalArgumentException(String.format("coordenada fuera de limites %dx%d", maxx, maxy));
        }

        public String toString() {
            return " (" + x + "," + y + ")";
        }
    }

    public Coordenadas obtenerCoordenada(int xy) {
        Coordenadas c = null;
        try {
            c = new Coordenadas(xy);
        } catch (Exception e) {
            // relanzamos la excepción
            throw e;
        }
        return c;
    }

    public double obtenerPrecio(Coordenadas c) {
        Producto p = matriz[c.x][c.y];
        return p.precio;
    }

    public String obtenerNombreProdcuto(Coordenadas c) {
        Producto p = matriz[c.x][c.y];
        return p.nombre;
    }

    public String efectuarRetirada(Coordenadas c) {
        cantidad[c.x][c.y] -= 1;
        return obtenerNombreProdcuto(c);
    }

    public boolean hayProducto(Coordenadas c) {
        return cantidad[c.x][c.y] > 0;
    }

    public void reponerProductos() {
        for (int f = 0; f < cantidad.length; f++)
            for (int c = 0; c < cantidad[0].length; c++)
                cantidad[f][c] = MAXCANTIDAD;
    }

    public String toString() {
        return "maquina " + this.matriz.length + "x" + this.matriz[0].length;
    }

    // public void guardarEstado(String id) {
    // repositorio.inicializar(id);
    // repositorio.guardarProductos(matriz);
    // }
}
