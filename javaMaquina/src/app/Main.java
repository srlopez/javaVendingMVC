package app;

import vending.hardware.*;
import vending.hardware.subsistemas.productos.Dispensador;
import vending.hardware.subsistemas.pagos.ControladorDePagos;
import vending.hardware.subsistemas.seguridad.*;
import vending.producto.*;
import app.ui.*;

public class Main {
	// CREA LOS OBJETOS, LOS COMPONE E INVOCA SUS USOS
	public static void main(String[] args) throws Exception {
		System.out.println("main runnig ...");

		// # 1
		// MAQUINA DE GOLOSINAS =================================
		System.out.println("\n#1  Máquina de Golosinas");
		Golosina[][] matrizGolosinas = {
			{ 	new Golosina("KitKat", 1.10), 
				new Golosina("Chicles de fresa", .80), 
				new Golosina("Lacasitos", 1.50),
				new Golosina("Palotes", .90f) },
			{ 	new Golosina("Kinder Bueno", 1.80), 
				new Golosina("Bolsa Haribo", 1.0), 
				new Golosina("Chetoos", 1.20),
				new Golosina("Twix", 1.0f) },
			{ 	new Golosina("Maiz", 0.7), 
				new Golosina("M&M’S", 1.30), 
				new Golosina("Papa Delta", 1.20),
				new Golosina("Chicles de menta", .80f) },
			{ 	new Golosina("Gusanitos", 1.50), 
				new Golosina("Crunch", 1.10), 
				new Golosina("Milkybar", 1.10),
				new Golosina("Patatas fritas", 1.10f) }, 
			};
		
		Dispensador dispensadorGolosinas = new Dispensador(matrizGolosinas);
		ISeguridad sistemaSeguridad = new SeguridadPin(new String[] { "1234", "2345" });
		ControladorDePagos sistemaPago = new ControladorDePagos();
		Maquina mGomez = new Maquina("Golosinas Gómez", 
				dispensadorGolosinas, 
				sistemaSeguridad, 
				sistemaPago
				);
		Terminal terminal = new Terminal(mGomez.getName());
		Controlador controller = new Controlador(terminal, mGomez);
		controller.run();

		// #2
		// MAQUINA DE REFESCOS ===============================================
		System.out.println("\n#2  Máquina de Refrescos");
		Refresco[][] matrizBebidas = { 
			{ 	new Refresco("Coca Zero", 2.10f, true), 
				new Refresco("Fanta Naranja", 1.80f) },
			{ 	new Refresco("Chus", 1.90), 
				new Refresco("Sprite Zero", 2.80f, true) }, 
			};
		Dispensador dispensadorRefrescos = new Dispensador(matrizBebidas);
		sistemaSeguridad = new SeguridadFormula();
		/*
		sistemaPago = new ControladorDePagos();
		cambiamos el Dispensador, cambiamos el sistema de seguridad, y utilizamos el
		mismo sistema de pago ¿?! Qué ocurre?
		*/
		Maquina mMtz = new Maquina("Refrescos Mtz.", dispensadorRefrescos, sistemaSeguridad, sistemaPago);
		new Controlador(new Terminal(mMtz.getName()), mMtz).run();

		// #3
		// HACER UNA MAQUINA CON ESTADO SQL QUE VENDA GOLOSINAS Y REFRESCOS
		System.out.println("\n#3  Máquina de Refrescos y Golosinas");
		Producto[][] matriz3 = { 
			{ new Golosina("KitKat", 1.10) }, 
			{ new Refresco("Sprite Z", 1.80, true) },
			{ new Refresco("San Miguel", 0.7) }, 
		};
		System.out.println("¿  ?");

		// =========================
		System.out.println("\nFin de la aplicación");
	}
}
