package vending.hardware.subsistemas.pagos;

import vending.PagoStatus;

public class ControladorDePagos {
    // cantidad máxima de cada moneda en caja
    static final int MAX_MONEDAS = 5;
    // cantidad máxima de monedas admitas en el pago
    static final int MAX_PAGO_MONEDAS = 5;

    /**
     * caja de entrada de pagos y salida de cambios
     */
    Efectivo caja;
    /**
     * importe de excesos de monedas acumulables en caja
     */
    double cajon = 0;

    // operaciones realizadas
    private int nOperaciones = 0;

    /**
     * Controlador con caja vacía
     */
    public ControladorDePagos() {
        this.caja = new Efectivo();
    }

    /**
     * Controlador que establece una caja
     * @param e
     */
    public ControladorDePagos(Efectivo e) {
        this.caja = e;
    }

    /**
     * Verificamos si el pago es válido:
     * - con el número adecuado de monedas
     * - para un determinado precio
     * - y si podemos devolver cambio
     * @param pago
     * @param precio
     * @return
     */
    public int esPagoValido(Efectivo pago, double precio) {
        // Invalido?
        if (pago.numeroDeMonedas() > MAX_PAGO_MONEDAS)
            return PagoStatus.PAGO_INVALIDO;
        // Valido?
        double importe = pago.valor();
        if (importe < precio)
            return PagoStatus.PAGO_INSUFICIENTE;
        // Verificar que tenemos monedas para devolver
        Efectivo cambio = calcularCambio(importe - precio);
        // PAGO_SIN_CAMBIOS o PAGO_VALIDO en cambio.cantidad[0];
        return cambio.cantidad[0];
    }

    /**
     * Calculo del Efectivo para un determinado importe
     * @param cambio
     * @return
     */
    public Efectivo calcularCambio(double cambio) {
        // devolver[0] NO SE USA REQUISITO NO DEVOLVER 2€
        //
        // 1 - Si tenemos cambio PAGOVALIDO
        // -1 - Si no tenemos cambio PAGOSINCAMBIOS
        int[] devolver = new int[Efectivo.valor.length];
        devolver[0] = PagoStatus.PAGO_SIN_CAMBIOS;
        int[] monedero = caja.cantidad.clone();

        // Multiplicamos *100 para los calculos en integer
        // int iCambio = (int) (cambio * 100d);// 2,60*100=259!!!
        int iCambio = Integer.parseInt(String.format("%.0f", cambio * 100));

        // Saltamos los 2€ -> i=1
        for (int i = 1; i < Efectivo.valor.length; i++) {
            // Valor de la moneda
            int iValor = (int) (Efectivo.valor[i] * 100);
            while (iCambio >= iValor && monedero[i] > 0) {
                iCambio -= iValor;
                devolver[i]++;
                monedero[i]--;
            }
        }

        if (iCambio == 0) // Tenemos CAMBIO
            devolver[0] = PagoStatus.PAGO_VALIDO;
        return new Efectivo(devolver);
    }

    /**
     * Realiza el pago:
     * - Calcula el Cambio Efectivo
     * - Añade el Pago a la Caja
     * @param pago
     * @param precio
     * @return cambio
     */
    public Efectivo procederAlPago(Efectivo pago, double precio) {
        double importe = pago.valor();
        ptraza(caja.cantidad, "caja inicial");

        Efectivo cambio = calcularCambio(importe - precio);
        ptraza(cambio.cantidad, "cambio calculado");

        // Esta verificación puede estar de más, pero no molesta
        if (cambio.cantidad[0] == PagoStatus.PAGO_VALIDO) {
            // Contamos las operaciones realizadas
            nOperaciones++;

            // Quitar Cambio de la caja
            for (int i = 1; i < cambio.cantidad.length; i++)
                caja.cantidad[i] -= cambio.cantidad[i];

            ptraza(caja.cantidad, "caja - cambio");
            ptraza(pago.cantidad, "pago a sumar");

            // Añadir las monedas de Pago a la caja
            // y el extra suma a CAJON.
            for (int i = 0; i < pago.cantidad.length; i++) {
                int extra = 0;
                int entra = pago.cantidad[i];
                if (i == 0) { // Las de 2€ directas al cajon
                    extra = pago.cantidad[i];
                    entra = 0;
                } else if ((caja.cantidad[i] + pago.cantidad[i]) > MAX_MONEDAS) {
                    extra = caja.cantidad[i] + pago.cantidad[i] - MAX_MONEDAS;
                    entra = pago.cantidad[i] - extra;
                }
                caja.cantidad[i] += entra;
                cajon += extra * Efectivo.valor[i];
            }
        }
        cambio.cantidad[0] = 0; // Reestablecemos que NO devolvemos 2€
        ptraza(caja.cantidad, "caja finales");
        return cambio;
    }

    /**
     * Crea un informe (String)
     * @return
     */
    public String informe() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Operaciones %10.10s\n", nOperaciones));
        sb.append(String.format("      Cajón %10.2f€\n", cajon));
        sb.append("Caja de cambios\n |");
        for (double v : Efectivo.valor)
            sb.append(String.format(" %5.2f€ | ", v));
        sb.append("\n |");
        for (int n : caja.cantidad)
            sb.append(String.format("%5s   | ", n));
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Añade monedas a l caja
     * @return
     */
    public double rellenarCaja() {
        double sum = 0;
        // 2€ no se rellena, i=0
        for (int i = 1; i < caja.cantidad.length; i++) {
            sum += (MAX_MONEDAS - caja.cantidad[i]) * Efectivo.valor[i];
            caja.cantidad[i] = MAX_MONEDAS;
        }
        return sum;
    }

    /**
     * Método estrella. A todo el mundo le gusta recaudar
     */
    public double recaudar() {
        double aux = cajon;
        cajon = 0;
        return aux;
    }

    private void ptraza(int[] l, String s) {
        // descomentar si queremos trazas
        // System.out.printf("%s %s\n",Arrays.toString(l),s);
    }

}
