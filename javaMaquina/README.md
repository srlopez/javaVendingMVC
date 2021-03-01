# Ejemplo para creación de librería

Este proyecto es la creación de una librería que simula una máquina vending.
La interfaz para interactuar se crea en el proyecto javaVendingConsole para presentar la arquitectura MVC.

### Creacion de la librería jarfile
En el directorio del proyecto, ejecuta estos comandos PS
```bash
$out="javaMaquina.jar"
$base="src\vending"
rm bin -r -fo
rm $out 
mkdir bin
javac -d bin $base\*.java $base\hardware\subsistemas\pagos\*.java  $base\hardware\subsistemas\productos\*.java $base\hardware\subsistemas\seguridad\*.java $base\producto\*.java 
cd bin
jar cfe $out -C *
move $out .. -fo
cd ..
rm bin -r -fo
jar tvf $out
```

Se puede crear automaticamente desde los comandos del proyecto pero automaticamente añade todas las clases bajo `src` 

### Plantilla JAVA par JUnit5

Plantilla para MSCode.
```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/* PLANTILLAS DE TESTS
@Test
@DisplayName("")
void test() {
	fail("Not yet implemented");
	//assertEquals(0, 0, "Mensaje");
	//assertAll("Mensaje TODOS",
	// () -> assertEquals( 0,0, "Mensaje 1"),
	// () -> assertEquals( 0,0, "Mensaje 2")
	// );
}

@BeforeAll
static void initAll() {
}

@BeforeEach
void init() {
}

@Test
@Disabled("este tests no se ejecuta")
void skippedTest() {
	// not executed
}

@AfterEach
void tearDown() {
}

@AfterAll
static void tearDownAll() {
}

@Nested
class TestsAgrupados {
}
```