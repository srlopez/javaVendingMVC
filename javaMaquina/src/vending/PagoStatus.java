package vending;

/**
 * Errores de validación del Pago
 * Se implementa como int en lugar de enum ya que en CtrlPagos.esPagoValido
 * se utiliza tipo int como devolución
 */
public class PagoStatus {
    public static final int PAGO_VALIDO = 1;
    public static final int PAGO_INSUFICIENTE = 0;
    public static final int PAGO_SIN_CAMBIOS = -1;
    public static final int PAGO_INVALIDO = -2; // Numero de monedas no admitido

    private PagoStatus() {
    }
}
