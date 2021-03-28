/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package comunes;

import java.io.Serializable;

/**
 * Clase que modela una carta.
 */
public class Carta implements Serializable {

	private int valorBruto;
	private int valor;
	private Palos palo;
	private String name;

	/**
	 * Instantiates a new Carta.
	 * @param valor el valor de la carta.
	 * @param palo el palo de la carta.
	 */
	public Carta(int valor, Palos palo) {
		this.valor = valor;
		this.palo = palo;
		this.valorBruto = valor > 10 ? 10 : valor;

		name = palo.toString().substring(0, 1);
		name = "cartas/" + name + valor + ".png";
	}

	/**
	 * @return el valor de la carta teniendo en cuenta que las figuras valen 10.
	 */
	public int getValor() {
		return valorBruto;
	}

	/**
	 * Establece el valor de la carta.
	 * @param valor el valor asignado a la carta.
	 */
	public void setValor(int valor) {
		this.valor = valor;
	}

	/**
	 * @return el palo de la carta.
	 */
	public Palos getPalo() {
		return palo;
	}

	/**
	 * Establece el palo de la carta.
	 * @param palo el palo asignado a la carta.
	 */
	public void setPalo(Palos palo) {
		this.palo = palo;
	}

	/**
	 * @return
	 */
	public int getValorBruto() {
		return valorBruto;
	}

	/**
	 * @return una cadena con el valor y el palo de la carta.
	 */
	public String getCarta() {
		return Integer.toString(valor) + palo;
	}

	/**
	 * @return el string con la ruta de la imagen de la carta.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
     * enum que contiene los tipos de palo de las cartas.
     */
	public enum Palos {
		picas, corazones, diamantes, treboles
	}
}
