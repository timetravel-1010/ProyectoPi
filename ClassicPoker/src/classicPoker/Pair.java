/*
 * Programacion Interactiva
 * Mini proyecto 3: Juego de poker clasico.
 */
package classicPoker;

/**
 * Clase que almacena un par de valores, que pueden ser de diferentes tipos (L y R).
 */
public class Pair<L, R> {
    private L first;
    private R second;

    /**
     * Instantiates a new Pair.
     * @param first valor de tipo L.
     * @param second valor de tipo R.
     */
    public Pair(L first, R second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return el primer elemento del pair.
     */
    public L getFirst() {
        return first;
    }

    /**
     * @return el segundo elemento del pair.
     */
    public R getSecond() {
        return second;
    }

    /**
     * Establece el primer elemento del pair.
     * @param first elemento de tipo L para almacenar en el pair.
     */
    public void setFirst(L first) {
        this.first = first;
    }

    /**
     * Establece el segundo elemento del pair.
     * @param second elemento de tipo R para almacenar en el pair.
     */
    public void setSecond(R second) {
        this.second = second;
    }
}
