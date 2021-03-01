package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import vending.hardware.subsistemas.pagos.Efectivo;

public class EfectivoTests {
    
    @Test
    @DisplayName("Plantilla")
    void PlantillaTest() {
        //fail("Not yet implemented");
        // Preparación//Arrange//Given
    
        // Ejecución//Act//When
    
        // Aserción//Assert//Then
        assertEquals(1, 1);
    }

    @Test
    @DisplayName("new Efectivo -> valor=0, monedas=0")
    void efectivoV0M0() {
        Efectivo e = new Efectivo();

        assertAll("valor=0, monedas=0", 
            () -> assertEquals(0, e.numeroDeMonedas(), "Cero monedas"),
            () -> assertEquals(0, e.valor(), "Cero valor"));
    }

    @Test
    @DisplayName("Efectivo {1,1,1,1,1}, valor=3.80, monedas=5")
    void testEfectivo5_1() {
        int [] monedas = new int[]{1,1,1,1,1};
        Efectivo e = new Efectivo(monedas);

        assertAll("Solo se realiza una tranferencia", 
            () -> assertEquals(3.80, e.valor(), "3.80€"),
            () -> assertEquals(5, e.numeroDeMonedas(), "5 monedas"));
    }

    @Test
    @DisplayName("Efectivo {1,1,1,1,1,1}, valido= false")
    void testEfectivo6_1() {
        int [] monedas = new int[]{1,1,1,1,1,1};
        Efectivo e = new Efectivo(monedas);

        assertEquals(false, e.esValido());
    }

    @DisplayName("Efectivos INVALIDOS Extra")
    @ParameterizedTest(name = "ingreso #{index}: {0}")
    @ValueSource(strings = { "1,1,1,1,1,1","1,1,1,1","ERROR","1,1, A ,1,1", "1, 1 1 ,1,1,1"})
    void testEfeectivoStringInValidos(String monedas) {
        Efectivo e = new Efectivo(monedas);
        boolean result= e.esValido();
        assertEquals(false, result, monedas + ":"+result);
     }

    @DisplayName("Efectivo Válido Parameterized Test con CSV")
    @ParameterizedTest(name = "run #{index}: '{0} -> {1}€/{2} monedas")
    // "String de monedas, valor, monedas"
    @CsvSource({ 
        "'1, 1, 1,1 ,1', 3.80, 5",
        "' 2, 0,0, 0,0', 4.00, 2",
        "'0,2,0,   0,0', 2.00, 2",
        "'0,  0 ,2,0,0', 1.00, 2",
        "'  0,0, 0,2,0', 0.40, 2",
        "'0,0,0,0,2   ', 0.20, 2",
    })
    void testEfectivoStringCSV(String  input, double valor, int monedas) {
        Efectivo e = new Efectivo(input);
        assertAll(
            () -> assertEquals(valor, e.valor(), () -> input + "->" + valor + "€"),
            () -> assertEquals(monedas, e.numeroDeMonedas(), () -> input + "->" + monedas + " m.")
            );
    }

}

