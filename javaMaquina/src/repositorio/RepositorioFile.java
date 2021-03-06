package repositorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import vending.hardware.subsistemas.Dispensador;
import vending.hardware.subsistemas.economico.ControladorDePagos;
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
        vending.hardware.subsistemas.economico.RepositorioFile.load(ctrl, cajaFileName );
    }

    @Override
    public void guardarCaja(ControladorDePagos ctrl) {
        vending.hardware.subsistemas.economico.RepositorioFile.save(ctrl, cajaFileName );
    }
}
