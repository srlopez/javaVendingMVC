package vending.hardware.subsistemas.seguridad;

public class SeguridadFormula implements ISeguridad {
    static final int VALOR = 13;

    boolean calcula(String pin) {
        // Si la suma de los digitos es 13 y ... true
        int suma = 0;
        try {
            for (int i = 0; i < pin.length(); i++)
                suma += Integer.parseInt("" + pin.charAt(i));
            if (suma == VALOR)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean esValido(String pin) {
        // si longitud <> 4 return false
        return calcula(pin);
    };

}
