/*
 * Programacion Interactiva
 * Mini proyecto 2: Juego de batalla naval.
 */
package batallaNaval;

import java.awt.EventQueue;

/**
 * Clase que contiene el metodo main
 * e inicia el programa.
 */
public class PrincipalBatallaNaval {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				BatallaNavalVista myGame = new BatallaNavalVista();
			}
		});

	}
}
