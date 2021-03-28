/*
 * Programación Interactiva
 * Mini proyecto 1: Juego de entrenamiento de memoria
 */

package entrenamientoDeMemoria;

import java.awt.EventQueue;

/**
 * The Class PrincipalEntrenamientoDeMemoria. Clase que contiene el metodo main
 * e inicia el programa.
 */
public class PrincipalEntrenamientoDeMemoria {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				VistaEntrenamientoDeMemoria myGame = new VistaEntrenamientoDeMemoria();
			}
		});

	}

}