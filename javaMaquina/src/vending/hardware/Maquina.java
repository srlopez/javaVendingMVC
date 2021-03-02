package vending.hardware;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

// XML
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//
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
     * 
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
     * 
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
        if (cacheCoordenadas.get(xy) != null)
            return true;
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

    // Trapaso de informacion 1 - Matriz de CSV
    public String[][] mostrarProductosString() {
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

    // Trapaso de informacion 2 - DocumentoXML
    public String mostrarProductosXML() throws ParserConfigurationException, TransformerException {
        // hacemos un DTO que es un string (DataTransferObject) con
        // El String es un Documento XML
        // nombre ; cero ; precio ; cantidad

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        // root element
        Element rootElement = doc.createElement("productos");
        doc.appendChild(rootElement);
        Attr attrFilas = doc.createAttribute("filas");
        attrFilas.setValue("" + dispensador.matriz.length);
        Attr attrCols = doc.createAttribute("columnas");
        attrCols.setValue("" + dispensador.matriz[0].length);
        rootElement.setAttributeNode(attrFilas);
        rootElement.setAttributeNode(attrCols);

        for (int f = 0; f < dispensador.matriz.length; f++)
            for (int c = 0; c < dispensador.matriz[0].length; c++) {
                Producto prod = dispensador.matriz[f][c];

                // elemento producto + atributos
                Element producto = doc.createElement("producto");
                producto.setAttribute("nombre", prod.nombre);
                producto.setAttribute("fila", "" + f);
                producto.setAttribute("columna", "" + c);

                // subelementos cero y precio
                Element cero = doc.createElement("cero");
                cero.appendChild(doc.createTextNode(""+prod.esCeroCal()));
                Element precio = doc.createElement("precio");
                precio.appendChild(doc.createTextNode(String.format("%.2f", prod.precio)));
                producto.appendChild(cero);
                producto.appendChild(precio);
                rootElement.appendChild(producto);
            }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString().replaceAll("\n|\r", "");
        //return writer.toString();
    }

    public String[][] mostrarProductosAdmin() {
        String[][] muestra = mostrarProductosString();
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

    public void apagar() {
        // Apagando máquina
        // Lo sobreescribiremos luego
    }

    // toString
    public String toString() {
        return empresa + " " + this.dispensador.matriz.length + "x" + this.dispensador.matriz[0].length;
    }

}
