package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import vending.hardware.subsistemas.pagos.*;

/*
PAGOVALIDO = 1;
PAGOINSUFICIENTE = 0;
PAGOSINCAMBIOS = -1;
PAGOINVALIDO = -2; // Numero de monedas no admitido
*/

public class ControlPagosTests {

    @DisplayName("testCalcularCambio CSV")
    @ParameterizedTest(name = "run #{index}: '{0} -> {1}€/{2} monedas")
    // "caja inicial, cambio, resultado"
    @CsvSource (value = {
        "2,2,2,2,2: 1.20: 1,1,0,1,0", 
        "5,5,5,5,5: 2.60: 1,2,1,0,1",  
        "0,2,1,0,1: 2.60: 1,2,1,0,1",  
        "0,2,1,1,0: 2.60: -1,2,1,0,0", //-1=invalido NO HAY .10
        "0,2,0,2,3: 2.60: 1,2,0,2,2",  
        "0,2,0,2,1: 2.60: -1,2,0,2,1", // No llega
        "0,2,0,3,1: 2.60: 1,2,0,3,0", 
        "1,1,1,1,1: 0.80: 1,0,1,1,1", 
        }, delimiter = ':')
    void testCalcularCambio(String sCaja, double cambio, String sCambio)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        // fail("Not yet implemented");
        // Preparación
        Efectivo caja = new Efectivo(sCaja);
        ControladorDePagos ctrl = new ControladorDePagos(caja);
        
        // Ejecución
        Efectivo result =  ctrl.calcularCambio(cambio);
        Efectivo esperado = new Efectivo(sCambio);

        // Aserción
        assertArrayEquals(esperado.cantidadesDeMonedas(), result.cantidadesDeMonedas());
    }

    @DisplayName("testEsPagoValido CSV")
    @ParameterizedTest(name = "run #{index}: '{0} -> {1}€/{2} monedas")
    // "caja inicial, precio, pago, resultado"
    @CsvSource (value = {
        "1,1,1,1,1: 1.20: 2,0,0,0,0: -1", // 2.80
        "1,1,1,1,1: 1.20: 1,0,0,0,0: 1", // 0.80
        }, delimiter = ':')
    void testEsPagoValido(String sCaja, double precio, String sPago, int esperado) {
        // fail("Not yet implemented");
        // Preparación
        Efectivo caja = new Efectivo(sCaja);
        ControladorDePagos ctrl = new ControladorDePagos(caja);
        Efectivo pago = new Efectivo(sPago);
        
        // Ejecución
        int result = ctrl.esPagoValido(pago, precio);

        // Aserción
        assertEquals(esperado, result);
    }
}
