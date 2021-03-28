/*
 * Programacion Interactiva
 * Mini proyecto 2: Juego de batalla naval.
 */

package batallaNaval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Point;

/**
 * Clase que modela el juego de batalla naval.
 */
public class BatallaNavalGame {
    public enum Jugador {
        human, AI
    }

    private Map<Jugador, Map<Point, Barco>> mapa = new HashMap<>(); // mapa de los jugadores
    private Map<Jugador, Integer> numeroDeBarcos = new HashMap<>(); // numero de barcos a flote
    private Map<TipoBarco, Integer> navySize = new HashMap<>(); // numero de barcos de cada tipo
    private int mapSize;
    private boolean started;

    // #--------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Instantiates a new batalla naval game.
     * @param mapSize
     */
    public BatallaNavalGame(int mapSize) {
        mapa.put(Jugador.human, new HashMap<>());
        mapa.put(Jugador.AI, new HashMap<>());
        this.mapSize = mapSize;
        this.started = false;

        //  se asignan los tipos de barcos y la cantidad de cada uno.
        navySize.put(TipoBarco.Portavion, 4);
        navySize.put(TipoBarco.Submarino, 3);
        navySize.put(TipoBarco.Destructor, 2);
        navySize.put(TipoBarco.Fragata, 1);

        numeroDeBarcos.put(Jugador.human, 9);
        numeroDeBarcos.put(Jugador.AI, 9);

        inicializarBarcosAI();
    }

    /**
     * ubica un barco en el mapa del jugador
     * @param player
     * @param location punto de inicio del barco
     * @param orientacion
     * @param tipo tipo del barco
     */
    public void ubicarBarco(Jugador player, Point location, Orientation orientacion, TipoBarco tipo) {
        Barco barco = new Barco(tipo, location, orientacion);

        // ubicar el barco en el mapa
        List<Point> casillasBarco = Barco.ubicarBarco(tipo, location, orientacion);
        for (Point point : casillasBarco)
            mapa.get(player).put(point, barco);
    }

    /**
     * inicializar barcos AI
     */
    private void inicializarBarcosAI() {
        for (TipoBarco tipo : navySize.keySet())
            ubicarBarcoAleatoriamente(tipo, navySize.get(tipo));
    }

    /**
     * ubicar barcos aleatoriamente en el mapa de la AI
     * @param tipo tipo del barco
     * @param cantidad
     */
    private void ubicarBarcoAleatoriamente(TipoBarco tipo, int cantidad) {
        while (cantidad > 0) {
            Random aleatorio = new Random();
            Point casilla = new Point(aleatorio.nextInt(mapSize), aleatorio.nextInt(mapSize));
            Orientation orientacion = Orientation.values()[aleatorio.nextInt(4)];
            if (validarUbicacion(Barco.ubicarBarco(tipo, casilla, orientacion))) {
                ubicarBarco(BatallaNavalGame.Jugador.AI, casilla, orientacion, tipo);
                cantidad--;
            }
        }
    }

    /**
     * Verifica que los puntos esten libres en el mapa
     * @param puntos
     * @return true si es valida
     */
    private boolean validarUbicacion(List<Point> puntos) {
        // verificar que el barco no se salga del mapa.
        for (Point punto : puntos)
            if (!(punto.getX() < mapSize && punto.getX() >= 0 && punto.getY() < mapSize && punto.getY() >= 0))
                return false;

        // verificar que la posicion no este ocupada por otro barco.
        for (Point punto : puntos)
            if (mapa.get(Jugador.AI).get(punto) != null)
                return false;

        return true;
    }

    /**
     * Disparar
     * @param player el jugador al que le disparan
     * @param location la coordenada del disparo
     * @return un mapa de los puntos afectados y su estado
     */
    public Map<Point, EstadoCasilla> disparar(Jugador player, Point location) {
        Barco barco = mapa.get(player).get(location);
        if (barco != null) {
            if (barco.nosHudieron())
                return barco.getPartesDelBarco();

            Map<Point, EstadoCasilla> casillas = barco.nosDisparan(location);

            if (barco.nosHudieron()) // confirmar que el barco fue completamente hundido
                numeroDeBarcos.replace(player, numeroDeBarcos.get(player) - 1); // se le resta un barco al jugador.
            return casillas;
        } else
            return new HashMap<Point, EstadoCasilla>() {
                {
                    put(location, EstadoCasilla.Disparo);
                }
            };
    }

    /**
     * Disparar aleatoriamente
     * @param player el jugador al que le disparan
     * @return un mapa de los puntos afectados y su estado
     */
    public Map<Point, EstadoCasilla> disparar(Jugador player) {
        //! no se tiene en cuenta que en esa posicion no se haya disparado antes
        return disparar(player, new Point((int) (Math.random() * mapSize), (int) (Math.random() * mapSize)));
    }

    /**
     * Determina si el juego se ha terminado
     * @return el ganador
     */
    public Jugador determinarJuego() {
        if (numeroDeBarcos.get(Jugador.human) == 0)
            return Jugador.AI;
        if (numeroDeBarcos.get(Jugador.AI) == 0)
            return Jugador.human;
        return null;
    }

    /**
     * Reinicia las variables de BatallaNavalGame
     */
    public void reiniciar() {
        mapa.replace(Jugador.human, new HashMap<>());
        mapa.replace(Jugador.AI, new HashMap<>());
        this.started = false;

        navySize.replace(TipoBarco.Portavion, 4);
        navySize.replace(TipoBarco.Submarino, 3);
        navySize.replace(TipoBarco.Destructor, 2);
        navySize.replace(TipoBarco.Fragata, 1);

        numeroDeBarcos.replace(Jugador.human, 9);
        numeroDeBarcos.replace(Jugador.AI, 9);

        inicializarBarcosAI();
    }

    // #---------------------------------------------------------------------------
    // # AUXILIARES
    // #---------------------------------------------------------------------------

    /**
     * Maneja la variable started para representar si el juego ha empezado o no
     */
    public void play() {
        started = true;
    }

    /**
     * Devuelve si el juego ha empezado
     */
    public boolean haEmpezado() {
        return started;
    }

    /**
     * Devuelve el mapa del jugador player
     * @param player
     * @return elMapa
     */
    public Map<Point, Barco> getMap(Jugador player) {
        return mapa.get(player);
    }

    /**
     * Retorna el tamaño del mapa en unidades #filas
     */
    public int getMapSize() {
        return mapSize;
    }

    /**
     * Resta un barco del hashmap navySize para determinar
     * cuando el jugador ha ubicado todos sus barcos en el mapa
     * @param tipo tipo del barco
     */
    public void restarUnBarco(TipoBarco tipo) {
        navySize.replace(tipo, navySize.get(tipo) - 1);
    }

    /**
     * Devuelve cuantos barcos faltan por ubicar de ese tipo
     * @param tipo tipo del barco
     * @return la cantidad de barcos
     */
    public int getCantidadBarco(TipoBarco tipo) {
        return navySize.get(tipo);
    }

    /**
     * Devuelve el siguiente tipo de barco que todavia le quedan barcos por ubicar en el mapa
     * si no hay ninguno, devuelve null
     * @return tipo de barco
     */
    public TipoBarco cambiarTipoBarco() {
        for (TipoBarco tipo : navySize.keySet()) {
            if (navySize.get(tipo) > 0)
                return tipo;
        }
        return null;
    }

}