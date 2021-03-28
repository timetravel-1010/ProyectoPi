
/*
 * Programación Interactiva
 * Autor: Julián Andrés Orejuela Erazo - 1541304 
 * Autor: Daniel Felipe Vélez Cuaical - 1924306
 * Mini proyecto 1: Juego de entrenamiento de memoria
 */

package entrenamientoDeMemoria;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class ControlEntrenamientoDeMemoria. Maneja la logica del juego
 * entrenamiento de memoria, revuelve las cartas de manera aleatoria, determina
 * si se gana o pierde una ronda, etc.
 */
public class ControlEntrenamientoDeMemoria {

	private int complejidad;
	private int tiempoDeEspera;
	private int cartaGanadora;
	private ArrayList<Integer> seleccionadas;

	
	/**
	 * Instantiates a new control entrenamiento de memoria.  Se encarga de dar
	 * valores iniciales a los atributos del objeto.
	 */
	public ControlEntrenamientoDeMemoria() {
		complejidad = 4;
		tiempoDeEspera = 15;
	}

	/**
	 * Revuelve las cartas de manera aleatoria, determina la cartaGanadora y retorna un ArrayList con las cartas seleccionadas.
	 *
	 * @return the array list
	 */
	public ArrayList<Integer> revolverCartas() {
		// inicializar el ArrayList<Integer> con la cantidad de imagenes a devolver.
		ArrayList<Integer> cartas = new ArrayList<Integer>();
		seleccionadas = new ArrayList<Integer>();
		cartas.ensureCapacity(16);

		for (int j = 1; j <= 16; j++)
			cartas.add(j);

		// escoger las imagenes aleatoriamente.
		for (int i = 0; i < complejidad; i++) {
			int cartaSeleccionada = (int) (Math.random() * cartas.size());
			seleccionadas.add(cartas.get(cartaSeleccionada));
			cartas.remove(cartaSeleccionada);
		}

		cartaGanadora = (int) (Math.random() * complejidad);

		return seleccionadas;
	}

	/**
	 * Retorna el valor (int) del tiempo de espera (en milisegundos).
	 *
	 * @return the tiempo de espera
	 */
	public int getTiempoDeEspera() {
		return tiempoDeEspera;
	}

	/**
	 * Retorna true si la carta ingresada es la ganadora, aumenta la complejidad en
	 * 2 y disminuye el tiempo de espera, retorna false en caso contrario.
	 *
	 * @param carta the carta
	 * @return true, if successful
	 */
	public boolean determinarRonda(int carta) {
		if (carta == cartaGanadora) {
			if (complejidad < 12) {
				complejidad += 2;
				tiempoDeEspera -= 1;
				if (complejidad == 12)
					tiempoDeEspera -= 1;
			}
			return true;
		} else
			return false;
	}

	/**
	 * Retorna la carta ganadora.
	 *
	 * @return the carta ganadora
	 */
	public int getCartaGanadora() {
		return seleccionadas.get(cartaGanadora);
	}

}