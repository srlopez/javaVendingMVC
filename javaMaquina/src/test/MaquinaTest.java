package test;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.*;

import vending.hardware.Maquina;
import vending.hardware.subsistemas.pagos.ControladorDePagos;
import vending.hardware.subsistemas.productos.Dispensador;
import vending.hardware.subsistemas.seguridad.ISeguridad;
import vending.hardware.subsistemas.seguridad.SeguridadPin;
import vending.producto.*;

public class MaquinaTest {
    static Golosina[][] matrizGolosinas;
    static Dispensador dispensadorGolosinas;
    static ISeguridad sistemaSeguridad;
    static ControladorDePagos sistemaPago;
    static Maquina mTest;

    @BeforeAll
    static void initAll() throws Exception {
        Golosina g00 = new Golosina("KitKat", 1.10);
        Golosina g01 = new Golosina("Chicles de fresa", .80);
        Golosina g02 = new Golosina("Lacasitos", 1.50);
        Golosina g03 = new Golosina("Palotes", .90f);
        Golosina g10 = new Golosina("Kinder Bueno", 1.80);
        Golosina g11 = new Golosina("Bolsa Haribo", 1.0);
        Golosina g12 = new Golosina("Chetoos", 1.20);
        Golosina g13 = new Golosina("Twix", 1.0f);
        Golosina g20 = new Golosina("Maiz", 0.7);
        Golosina g21 = new Golosina("M&M’S", 1.30);
        Golosina g22 = new Golosina("Papa Delta", 1.20);
        Golosina g23 = new Golosina("Chicles de menta", .80f);
        Golosina g30 = new Golosina("Gusanitos", 1.50);
        Golosina g31 = new Golosina("Crunch", 1.10);
        Golosina g32 = new Golosina("Milkybar", 1.10);
        Golosina g33 = new Golosina("Patatas fritas", 1.10f);
        matrizGolosinas = new Golosina[][] { { g00, g01, g02, g03 }, { g10, g11, g12, g13 }, { g20, g21, g22, g23 },
                { g30, g31, g32, g33 }, };

        dispensadorGolosinas = new Dispensador(matrizGolosinas);
        sistemaSeguridad = new SeguridadPin(new String[] { "1234", "2345" });
        sistemaPago = new ControladorDePagos();
        mTest = new Maquina("test", dispensadorGolosinas, sistemaSeguridad, sistemaPago);

    }

    @Test
    @DisplayName("Productos XML")
    void productosXMLTest() throws ParserConfigurationException, TransformerException {

        String xml = mTest.mostrarProductosXML();
        assertTrue(xml.contains("<productos columnas=\"4\" filas=\"4\">"));
        assertTrue(xml.contains("<producto columna=\"0\" fila=\"0\" nombre=\"KitKat\"><cero>false</cero><precio>1,10</precio></producto>"));
        //assertEquals(xml,"FalloSeguro");
    }
}
