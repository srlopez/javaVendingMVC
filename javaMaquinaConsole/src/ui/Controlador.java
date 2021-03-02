package ui;

import vending.hardware.Maquina;
import vending.hardware.subsistemas.pagos.Efectivo;
import vending.PagoStatus;
import vending.ModoTerminal;

public class Controlador {
    Maquina maquina;
    Terminal terminal;
    ModoTerminal modo;

    public Controlador(Terminal terminal, Maquina maquina) {
        this.maquina = maquina;
        this.terminal = terminal;
    }

    // CICLO DE MENU
    public void run() {

        char opcion;
        while (true) {
            modo = terminal.mostrarMenu();
            opcion = terminal.leerOpcionMenu();
            switch (opcion) {
                // MODO
                case 'M':
                    validarCambioDeModo();
                    break;
                // MOSTRAR PRODUCTOS
                case '1':
                case 'V':
                case 'A': {
                    UseCaseA();
                    terminal.leerEnter();
                    break;
                }
                // RETIRAR PRODUCTO
                case '2':
                case 'P':
                case 'B': {
                    UseCaseB();
                    terminal.leerEnter();
                    break;
                }
                // RELLENAR TOD
                case '3':
                case 'R':
                case 'C': {
                    UseCaseC();
                    terminal.leerEnter();
                    break;
                }
                // INFORME
                case '4':
                case 'I':
                case 'D': {
                    UseCaseD();
                    terminal.leerEnter();
                    break;
                }
                // APAGAR e INFORME?
                case '5':
                case 'F':
                case 'E': {
                    UseCaseF();
                    return;
                }
                // default:
            }
        }
    }

    // SUPERUSUARIO + PIN
    void validarCambioDeModo() {
        if (modo == ModoTerminal.NORMAL) {
            String pin = terminal.leerPin();
            if (maquina.esPinValido(pin))
                terminal.establecerModo(ModoTerminal.ADMIN);
        } else
            terminal.establecerModo(ModoTerminal.NORMAL);
    }

    // MOSTRAR PRODUCTOS
    void UseCaseA() {
        String[][] muestra;
        if (modo == ModoTerminal.ADMIN)
            muestra = maquina.mostrarProductosAdmin();
        else
            muestra = maquina.mostrarProductosString();
        terminal.mostrarMuestra(muestra);
    }

    // RETIRAR PRODUCTO
    int UseCaseB() {
        try {
            // DTO String
            String[][] muestra = maquina.mostrarProductosString();
            terminal.mostrarMuestra(muestra);
            // DTO XML
            // String muestra = maquina.mostrarProductosXML();
            // terminal.parseMuestraXML(muestra);
            int xy = terminal.leerPosicion();
            if (!maquina.esXYValido(xy)) {
                terminal.mostrarMsg("Coordenadas inválidas");
                return -1;
            }

            // CONFIRMAR DISPONIBILIDAD
            if (!maquina.hayProducto(xy)) {
                terminal.mostrarMsg("Articulo no disponible");
                return -1;
            }
            terminal.mostrarMsg("Ha seleccionado `%s`.\n", maquina.obtenerNombreProdcuto(xy));
            terminal.mostrarMsg("Introduzca %.2f€.\n", maquina.obtenerPrecio(xy));

            Efectivo pago = terminal.leerEfectivo();
            if (!pago.esValido()) {
                terminal.mostrarMsg("Incorrecto número de monedas");
                return -1;
            }

            terminal.mostrarMsg("Ha introducido %d monedas por importe de %.2f€.\n", pago.numeroDeMonedas(), pago.valor());

            // CONFIRMAR DISPONIBILIDAD
            if (!maquina.hayProducto(xy)) {
                terminal.mostrarMsg("Articulo no disponible");
                return -1;
            }
            // CONFIRMAR PAGO
            double precio = maquina.obtenerPrecio(xy);
            switch (maquina.esPagoValido(pago, precio)) {
                case PagoStatus.PAGO_INVALIDO:
                    terminal.mostrarMsg("ERROR: Número de monedas superior al admitido");
                    return -1;
                case PagoStatus.PAGO_SIN_CAMBIOS:
                    terminal.mostrarMsg("ERROR: No disponemos de cambios");
                    return -1;
                case PagoStatus.PAGO_INSUFICIENTE:
                    terminal.mostrarMsg("Importe inferior al precio de %.2f€\n", precio);
                    return -1;
                case PagoStatus.PAGO_VALIDO:
                    Efectivo cambio = maquina.procederAlPago(pago, precio);// ControlPAgos
                    String producto = maquina.efectuarRetirada(xy); // Dispensador
                    terminal.mostrarMsg("Su `%s` %.2f€. Gracias\n", producto, precio);
                    terminal.mostrarMsg("Cambio %.2f€ %s\n", pago.valor() - precio, cambio.toString());
                    return 0;
                default:
                    terminal.mostrarMsg("ERROR: ERROR NO TIPIFICADO");
                    return -1;
            }
        } catch (Exception e) {
            terminal.mostrarMsg("Error malicioso");
            return -1;
        }
    }

    // RELLENAR
    void UseCaseC() {
        double e = maquina.rellenar();
        terminal.mostrarMsg("Introducidos %.2f€\n", e);
    }

    // INFORME
    void UseCaseD() {
        terminal.mostarCabecera("INFORME");
        terminal.mostrarMsg(maquina.informe());
    }

    // APAGAR
    void UseCaseF() {
        boolean opcion = terminal.opcionInformeSN();
        if (opcion) UseCaseD();
        terminal.mostrarMsg("apagando %s ...\n",maquina.getName());
        maquina.apagar();
    }

}
