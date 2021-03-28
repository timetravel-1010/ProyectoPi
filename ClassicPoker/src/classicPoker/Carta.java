/*
 * Programacion Interactiva
 * Mini proyecto 3: Juego de poker clasico.
 */

package classicPoker;

/**
 * Clase que modela una carta de poker.
 */
public class Carta {

    public final int numero;
    public final Palos palo;

    /**
     * Instantiates a new Carta.
     * @param numero
     * @param palo
     */
    Carta(int numero, Palos palo) {
        this.numero = numero;
        this.palo = palo;
    }

    /**
     * enum que contiene los tipos de palo de las cartas.
     */
    public enum Palos {
        picas, corazones, diamantes, treboles
    }
    
}