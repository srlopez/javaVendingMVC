package vending.hardware.subsistemas.pagos;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Efectivo {
    //static final int MAXTIPOSMONEDAS = cantidad.length;
    public int[] cantidad = { 0, 0, 0, 0, 0 };
    protected static final double[] valor = { 2, 1, .5, .2, .1 };
    private boolean valido = true;

    // Constructores

    /**
     * Crea un efectivo de valor 0
     */
    public Efectivo() {
    };

    /**
     * De un string con formato n,n,n,n,n nos crea un Efectivo
     * @param smonedas
     */
    public Efectivo(String smonedas) {
        String[] monedass = smonedas.split(",");
        int[] monedas = new int[monedass.length];
        try {
            for (int i = 0; i < cantidad.length; i++){
                // No Concatenamos un 0 para evaluar a 0 encaso de no ser un Integer
                // Eliminamos spaces
                String s = monedass[i].trim(); 
                monedas[i] = Integer.parseInt(s);
            }
        } catch (Exception e) {
            valido = false;
            return;
        }
        // Constructor call must be the first statement in a constructorJava
        establecerCantidad(monedas);
    }

    /**
     * Crea un Efectivo partiendo de un array de int's
     * @param monedas
     */
    public Efectivo(int[] monedas) {
        establecerCantidad(monedas);
    };

    // Métodos públicos

    /**
     * Devuelve el numero de monedas de que consta el Efectivo
     * @return
     */
    public int numeroDeMonedas() {
        // Por introducir los streams
        return IntStream.of(cantidad).sum();
    }

    /**
     * devuelve el valor del Efectivo
     * @return
     */
    public double valor() {
        // range + map + sum
        return IntStream.range(0, cantidad.length).mapToDouble(i -> cantidad[i] * valor[i]).sum();
    }

    /**
     * Preguntamos si la creación de Efectivo ha sido válida
     * @return
     */
    public boolean esValido() {
        // Por ver alternativas a
        // throw new IllegalArgumentException("numero de monedas no admitido");
        return valido;
    }

    /**
     * Devuelve el array de monedas
     * @return
     */
    public int[] cantidadesDeMonedas(){
        return cantidad;
    }    
    
    // Metodos privados
    private void establecerCantidad(int[] monedas) {
    // Se podría hacer una clase validadora de Efectivo.
        valido = false;
        if (monedas.length != cantidad.length)
            // si pasa un numero incorrecto de monedas -> salimos
            return;
        for (int i = 0; i < cantidad.length; i++)
            cantidad[i] = monedas[i];
        valido = true;
    }

    // Generico
    public String toString() {
        return Arrays.toString(cantidad);
    }
}