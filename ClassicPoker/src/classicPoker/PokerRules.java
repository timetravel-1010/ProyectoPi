/*
 * Programacion Interactiva
 * Mini proyecto 3: Juego de poker clasico.
 */
package classicPoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que determina una mano de poker.
 */
public class PokerRules {

    /**
     * Interface utilizada para las funciones que determinan la jugada de una mano.
     */
    private interface pokerRule {
        public List<Integer> comprobarMano(List<Carta> mano, Map<Integer, Integer> conteo);
    }

    private static pokerRule[] jugadas = {
        (mano, conteo) -> escaleraReal(mano),
        (mano, conteo) -> poker(mano, conteo),
        (mano, conteo) -> escaleraColor(mano),
        (mano, conteo) -> full(mano, conteo),
        (mano, conteo) -> color(mano),
        (mano, conteo) -> escalera(mano),
        (mano, conteo) -> trio(mano, conteo),
        (mano, conteo) -> doblePareja(mano, conteo),
        (mano, conteo) -> pareja(mano, conteo)
    };

    /**
     * Determina la mejor mano entre dos manos de jugadores especificados.
     * @param jugador1
     * @param jugador2
     * @return El jugador con la mejor mano, null en caso de empate.
     */
    public static Jugador determinarMano(Jugador jugador1, Jugador jugador2) {
        List <Carta> mano1 = jugador1.getMano();
        List <Carta> mano2 = jugador2.getMano();

        Pair <Integer, List<Integer>> resultado1 = determinarMano(mano1);
        Pair <Integer, List<Integer>> resultado2 = determinarMano(mano2);

        int valorJugada1 = resultado1.getFirst();
        int valorJugada2 = resultado2.getFirst();

        if (valorJugada1 == valorJugada2) { // Condicion para el posible desempate.
            // Se obtiene el valor de la carta mas alta que compone la jugada de cada jugador.
            int valorCartaMayor1 = cartaMasAlta(resultado1.getSecond());
            int valorCartaMayor2 = cartaMasAlta(resultado2.getSecond());

            if (valorCartaMayor1 == valorCartaMayor2)
                return null; // empate

            return valorCartaMayor1 > valorCartaMayor2 ? jugador1 : jugador2;
        }
        return valorJugada1 < valorJugada2 ?  jugador1 : jugador2; 
    }

    /**
     * Determina la jugada correspondiente a la mano especificada.
     * @param mano lista de cartas.
     * @return un Pair que contiene el valor de la jugada y los valores de las cartas que componen la jugada.
     */
    public static Pair <Integer, List<Integer>> determinarMano(List<Carta> mano) {
        ordenarMano(mano);
        Map<Integer, Integer> conteo = conteoCartas(mano);

        List<Integer> valoresMano = new ArrayList<Integer>();
        Pair <Integer, List<Integer>> resultado = new Pair<Integer,List<Integer>>(null, null); 

        for (int i = 0; i<jugadas.length; i++) {
            valoresMano = jugadas[i].comprobarMano(mano, conteo);
            if (valoresMano != null) {
                resultado.setFirst(i+1);
                resultado.setSecond(valoresMano);
                return resultado;
            }
        }
        return new Pair <Integer, List<Integer>>(10, getValoresMano(mano)); //en caso de que la mano corresponda a la jugada cartaMasAlta.
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Escalera Real.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> escaleraReal(List<Carta> mano) {
        if (color(mano) == null)
            return null;
        for (int i = 0; i < 5; i++)
            if (mano.get(i).numero != i + 10)
                return null;
        return getValoresMano(mano);
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Poker.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> poker(List<Carta> mano, Map<Integer, Integer> conteo) {

        for (Integer n : conteo.keySet())
            if (conteo.get(n) == 4) {
                return new ArrayList<Integer>() {
                    {
                        add(n);
                    }
                };
            }
        return null;
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Escalera Color.
     * @param mano
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> escaleraColor(List<Carta> mano) {
        if (color(mano) == null)
            return null;
        return escalera(mano);
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Full.
     * @param mano
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> full(List<Carta> mano, Map<Integer, Integer> conteo) {
        List<Integer> subMano = new ArrayList<Integer>();
        boolean trio = false;
        boolean par = false;

        for (Integer n : conteo.keySet()) {
            if (conteo.get(n) == 3) {
                trio = true;
                subMano.add(n);
            }
            else if (conteo.get(n) == 2) 
                par = true;
            
            if (par && trio)
                return subMano;
        }
        return null;
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Color.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> color(List<Carta> mano) {
        for (int i = 1; i < mano.size(); i++)
            if (mano.get(i).palo != mano.get(0).palo)
                return null;
        return getValoresMano(mano);
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Escalera.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> escalera(List<Carta> mano) {
        boolean flag = true;

        for (int i = 0; i < 5; i++) {
            if (mano.get(i).numero != i + mano.get(0).numero) {
                flag = false;
                break;
            }
        }

        if (flag) // La mano es una escalera.
            return getValoresMano(mano);

        //# Condicion para el AS.

        //Hay que comprobar que antes del 2 haya un AS.
        for (int j = 0; j<5; j++) {
            if (j == 0) {
                if (mano.get(j).numero-13 != 1)
                    return null;
            }
            else {
                if (mano.get(j).numero != j+1) 
                    return null;
            }
        }
        return getValoresMano(mano);
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Trio.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> trio(List<Carta> mano, Map<Integer, Integer> conteo) {
        List<Integer> subMano = new ArrayList<Integer>();

        for (Integer n : conteo.keySet())
            if (conteo.get(n) == 3) {
                subMano.add(n);
                return subMano;
            }
        return null;
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Doble Pareja.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> doblePareja(List<Carta> mano, Map<Integer, Integer> conteo) {
        List<Integer> subMano = new ArrayList<Integer>();

        int parejas = 0;
        for (Integer n : conteo.keySet())
            if (conteo.get(n) == 2) {
                parejas++;
                subMano.add(n);
                if (parejas == 2)
                    return subMano;
            }
        return null;
    }

    /**
     * Verifica si la mano especificada corresponde a la jugada Pareja.
     * @param mano lista de cartas.
     * @return la lista de valores de las cartas si corresponde a la jugada, null en caso contrario.
     */
    private static List<Integer> pareja(List<Carta> mano, Map<Integer, Integer> conteo) {
        List<Integer> subMano = new ArrayList<Integer>();

        for (Integer n : conteo.keySet()) {
            if (conteo.get(n) == 2) {
                subMano.add(n);
                return subMano;
            }
        }
        return null;
    }

    /**
     * Retorna el maximo valor de una lista de enteros especificada.
     * @param mano lista de los valores de las cartas que componen una jugada o la mano del jugador.
     * @return el entero correspondiente a la carta mas alta.
     */
    private static int cartaMasAlta(List<Integer> valoresCartas) {
        return Collections.max(valoresCartas);
    }

    // #---------------------------------------------------------------------------
    // # FUNCIONES AUXILIARES
    // #---------------------------------------------------------------------------

    /**
     * Crea una lista con los valores de las cartas de una mano.
     * @param mano lista de cartas.
     * @return una lista de enteros que corresponden a los valores de cada carta.
     */
    private static List<Integer> getValoresMano(List<Carta> mano) {
        List<Integer> valoresMano = new ArrayList<Integer>();

        for (Carta carta : mano) 
            valoresMano.add(carta.numero);

        return valoresMano;
    }

    /**
     * Cuenta la cantidad de veces que se repite el valor de una carta.
     * @param mano lista de cartas.
     * @return un HashMap que contiene como clave el numero de carta y como valor la cantidad de cartas de ese numero.
     */
    private static Map<Integer, Integer> conteoCartas(List<Carta> mano) {
        // El primer Integer es el numero de la carta y el segundo es la cantidad de cartas de ese numero
        // no se tiene en cuenta el palo.
        Map<Integer, Integer> conteo = new HashMap<Integer, Integer>();
        
        for (Carta carta : mano) {
            if (conteo.get(carta.numero) == null)
                conteo.put(carta.numero, 1);
            else 
                conteo.replace(carta.numero, (conteo.get(carta.numero)+1));
        }
        return conteo;
    }

    /**
     * Ordena de manera ascendente la lista de cartas.
     * @param mano lista de cartas para ordenar.
     */
    public static void ordenarMano(List<Carta> mano) {
        Collections.sort(mano, new Comparator<Carta>() {
            @Override
            public int compare(Carta carta1, Carta carta2) {
                if (carta1.numero > carta2.numero) 
                    return 1;
                if (carta1.numero < carta2.numero) 
                    return -1;
                return 0;
            }
        });
    }
}
