package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import vending.producto.Golosina;

class ProductsTests {

    @Test
    @DisplayName("Creación de Producto con nombre null")
    void productNombreNullTest() {

        Exception exception = assertThrows(Exception.class, () -> {
            new Golosina("", 0);
        });
        String expectedMessage = "nombre inválido";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Creación de Producto con importe <=0")
    void productImporteMenorIgualCeroTest() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Golosina("Chicle", 0);
        });
        String expectedMessage = "precio inválido";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
