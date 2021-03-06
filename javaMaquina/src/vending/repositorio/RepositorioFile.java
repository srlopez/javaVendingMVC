package vending.repositorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import vending.hardware.subsistemas.pagos.ControladorDePagos;
import vending.hardware.subsistemas.productos.Dispensador;
import vending.producto.*;

public class RepositorioFile implements IRepositorio {

    String prodFileName;
    String cajaFileName;

    @Override
    public void inicializar(String id) {
        String dir = System.getProperty("user.dir");
        String name = id.trim().replaceAll(" ", "_");
        this.prodFileName = dir + "\\data\\" + name + ".prod.txt";
        this.cajaFileName = dir + "\\data\\" + name + ".caja.txt";
    }

    @Override
    public void cargarProductos(Dispensador dispensador) {
        try {
            File repo = new File(prodFileName);
            FileReader filereader = null;
            filereader = new FileReader(repo);
            BufferedReader bufferreader = new BufferedReader(filereader);

            String data;
            data = bufferreader.readLine();
            String[] dimension = data.split(":");
            int lx = Integer.parseInt(dimension[0]);
            int ly = Integer.parseInt(dimension[1]);
            dispensador.matriz = new Producto[lx][ly];
            dispensador.cantidad = new int[lx][ly];

            int f = 0;
            while ((data = bufferreader.readLine()) != null) {
                String[] sproducto = data.split(":");
                for (int c = 0; c < ly; c++) {
                    // Golosina;KitKat;false;1.10;9:
                    String[] item = sproducto[c].split(";");
                    dispensador.matriz[f][c] = ProductoFactory.getProducto(item[0],item[1],Double.parseDouble(item[3]),item[2] == "false" ? false : true);
                    dispensador.cantidad[f][c] = Integer.parseInt(item[4]);
                }
                f++;
            }
            bufferreader.close();
            filereader.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void guardarProductos(Dispensador dispensador) {
        try {
            File repo = new File(prodFileName);
            FileWriter filewriter = null;
            filewriter = new FileWriter(repo);

            // Dimensiones en la primera linea
            filewriter.write(dispensador.matriz.length + ":" + dispensador.matriz[0].length + "\n");
            // Resto de datos
            for (int f = 0; f < dispensador.matriz.length; f++) {
                String data = "";
                for (int c = 0; c < dispensador.matriz[0].length; c++) {
                    Producto prod = dispensador.matriz[f][c];
                    String tipo = prod.getClass().getSimpleName();
                    int cant = dispensador.cantidad[f][c];
                    data += tipo + ";" + prod.nombre + ";" + prod.esCeroCal() + ";"
                            + String.format("%.2f", prod.precio).replace(',', '.') + ";" + cant + ":";
                }
                data += "\n";
                filewriter.write(data);
            }
            filewriter.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void cargarCaja(ControladorDePagos ctrl) {
        load(ctrl, cajaFileName );
    }

    @Override
    public void guardarCaja(ControladorDePagos ctrl) {
        save(ctrl, cajaFileName );
    }

    private static void save(ControladorDePagos ctrl, String fileName) {
        try {
            File repo = new File(fileName);
            FileWriter filewriter = null;
            filewriter = new FileWriter(repo);

            // linea 1 caja
            String data = "";
            for (int i = 0; i < ctrl.caja.cantidad.length; i++)
                data += ctrl.caja.cantidad[i] + ":";
            data += "\n";
            filewriter.write(data);
            // linea 2 cajon
            data = String.format("%.2f", ctrl.cajon).replace(',', '.') + "\n";
            filewriter.write(data);

            filewriter.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    };

    private static void load(ControladorDePagos ctrl, String fileName) {
        try {
            File repo = new File(fileName);
            FileReader filereader = null;
            filereader = new FileReader(repo);
            BufferedReader bufferreader = new BufferedReader(filereader);

            String data;
            // Linea 1
            data = bufferreader.readLine();
            String[] monedas = data.split(":");
            for (int i = 0; i < ctrl.caja.cantidad.length; i++)
                ctrl.caja.cantidad[i] = Integer.parseInt(monedas[i]);
            // Linea 2
            data = bufferreader.readLine();
            ctrl.cajon = Double.parseDouble(data);

            bufferreader.close();
            filereader.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    };
}
