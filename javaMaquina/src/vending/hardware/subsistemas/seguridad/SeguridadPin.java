package vending.hardware.subsistemas.seguridad;


public class SeguridadPin implements ISeguridad {
    String[] pin;

    public boolean esValido(String pin) {
        // si digitos <> 4 false
        for (String p: this.pin) {
            if(p.equals(pin)) return true;
        }
        return false;
    };

    public SeguridadPin( String[] pins){
        this.pin = pins;
    }
}
