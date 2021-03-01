package app.ui;

import java.util.Collections;
import java.util.Scanner;

import vending.ModoTerminal;
import vending.hardware.subsistemas.pagos.Efectivo;

public class Terminal {
    private Scanner input = new Scanner(System.in);
    private String nombre;
    private ModoTerminal modo;
    private String normalOpt = "M12AVBP";
    private String adminOpt = normalOpt + "345CRDIEF";

    public Terminal(String nombre) {
        this.nombre = nombre;
        this.modo = ModoTerminal.NORMAL;
    }

    // === PRESENTACION DE INFORMACION ===
    // CABECERA
    public void mostarCabecera(String cabecera) {
        // cls();
        pln();
        pf("   === %-15s ===\n", this.nombre);
        pf("   === %-15s %s\n", cabecera, modo.toString() == ModoTerminal.ADMIN.toString() ? "ADM" : "===");
        pln();
    }

    // MENU 
    public ModoTerminal mostrarMenu() {
        mostarCabecera("M)ENU");
        pln("   A.- V)er los productos");
        pln("   B.- Retirar P)roducto");
        if (modo == ModoTerminal.ADMIN) {
        pln("   C.- R)ellenar Productos y Monedas");
        pln("   D.- I)nforme");
        pln("   E.- Exit/F)IN");
        }
        pln();
        return modo;
    }

    public void establecerModo(ModoTerminal nuevoModo) {
            modo = nuevoModo;
    }

    // === MOSTRAR DATOS ===
    public void mostrarMuestra(String[][] muestra) {
        StringBuffer sb = new StringBuffer();
        int SIZE = 10;
        String line = "     ";
        for (int c = 0; c < muestra[0].length; c++)
            line = line + String.join("", String.format("%6d", c)) + "       ";
        line = line + "\n";
        sb.append(line);

        line = "   +";
        for (int c = 0; c < muestra[0].length; c++)
            line = line + String.join("", Collections.nCopies(SIZE + 2, "-")) + "+";
        line = line + "\n";
        sb.append(line);

        for (int f = 0; f < muestra.length; f++) {
            int n = muestra[f][0].split(";").length;
            for (int i = 0; i < n - 1; i++) {
                sb.append(i == 1 ? String.format("%2d", f) : "  ");
                for (int c = 0; c < muestra[0].length; c++) {
                    String[] data = muestra[f][c].split(";");
                    if (i == 0) {
                        sb.append((data[1].equals("false") ? " | " : " 🧴"));
                        sb.append(String.format("%-10.10s", data[0]));
                    }
                    if (i == 1)
                        sb.append(String.format(" | %9s€", data[2]));
                    if (i == 2)
                        sb.append(String.format(" | %-10s", data[3]));
                }
                sb.append(" |\n");
            }
            sb.append(line);
        }
        mostarCabecera("ARTICULOS");
        pln(sb.toString());
    }

    // === OBTENCION DE INPUT DE USUARIO ===
    public char leerOpcionMenu() {
        char opcion;
        while (true) {
            p("   Seleccione una opción: ");
            opcion = input.next().toUpperCase().charAt(0);
            if (modo == ModoTerminal.NORMAL && normalOpt.indexOf(opcion) > -1)
                break;
            if (modo == ModoTerminal.ADMIN && adminOpt.indexOf(opcion) > -1)
                break;
        }
        return opcion;
    }

    public String leerPin() {
        // return new String(System.console().readPassword("Introduzca PIN: "));
        // Puede dar Erro CP650001
        p("Introduzca PIN: ");
        return input.next();
    }

    public int leerPosicion() {
        p("Introduzca XY: ");
        return input.nextInt();
    }

    String leerImporte() {
        p("Indique el número de monedas de 2€,1€,0.5€,0.2€ y 0.1€ separadas por `,`: ");
        return input.next();
    }

    public Efectivo leerEfectivo() {
        return new Efectivo(leerImporte());
    }

    public boolean opcionInformeSN() {
        p("¿Desea el informe (N)?");
        char opcion = input.next().toUpperCase().charAt(0);
        return opcion=='S';
	}

    public void leerEnter() {
        p("Presione <Enter> para continuar ");
        input.nextLine();
        input.nextLine();
    }

    // == MENSAGES ==
    public void mostrarMsg(String format, Object... args) {
        mostrarMsg(String.format( format, args));
	}

	public void mostrarMsg(String string) {
        pln(string);
	}

    // === UTILIDADES ===
    private void pf(String format, Object... args) {
        System.out.printf(format, args);
    }

    private void pln(Object l) {
        System.out.println(l);
    }

    private void pln() {
        pln("");
    }

    private void p(Object l) {
        System.out.print(l);
    }

    private void cls() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


}
