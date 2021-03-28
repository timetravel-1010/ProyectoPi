/*
 * Programacion Interactiva
 * Mini proyecto 2: Juego de batalla naval.
 */
package batallaNaval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;

/**
 * Clase que modela un barco.
 */
public class Barco {
    private Map<Point, EstadoCasilla> partesDelBarco;
    private TipoBarco tipo;
    private Point location;
    private Orientation orientacion;

    /**
     * Instantiates a new barco.
     * @param tipo tipo de barco
     * @param location punto de inicio del barco
     * @param orientacion
     */
    public Barco(TipoBarco tipo, Point location, Orientation orientacion) {
        this.partesDelBarco = new HashMap<Point, EstadoCasilla>();
        this.tipo = tipo;
        this.location = location;
        this.orientacion = orientacion;
        List<Point> puntos = ubicarBarco(tipo, location, orientacion);

        for (int i = 0; i < tipo.size; i++) {
            partesDelBarco.put(puntos.get(i), EstadoCasilla.aFlote);
        }
    }

    /**
     * Calcula los puntos que ocuparia un barco con los parametros de entrada
     * @param tipo tipo de barco
     * @param location punto de inicio del barco
     * @param orientacion
     * @return lista de puntos
     */
    public static List<Point> ubicarBarco(TipoBarco tipo, Point location, Orientation orientacion) {
        List<Point> casillasBarco = new ArrayList<Point>();
        for (int i = 0; i < tipo.size; i++)
            casillasBarco.add((Point) location.clone());

        if (orientacion == Orientation.ARB)
            for (int i = 0; i < tipo.size; i++)
                casillasBarco.get(i).translate(0, -i);

        if (orientacion == Orientation.DER)
            for (int i = 0; i < tipo.size; i++)
                casillasBarco.get(i).translate(i, 0);

        if (orientacion == Orientation.ABJ)
            for (int i = 0; i < tipo.size; i++)
                casillasBarco.get(i).translate(0, i);

        if (orientacion == Orientation.IZQ)
            for (int i = 0; i < tipo.size; i++)
                casillasBarco.get(i).translate(-i, 0);

        return casillasBarco;
    }

    /**
     * Determina si el barco esta hundido o no
     */
    public boolean nosHudieron() {
        for (EstadoCasilla estado : partesDelBarco.values())
            if (estado == EstadoCasilla.aFlote)
                return false;
        return true;
    }

    /**
     * Ejecuta la accion de disparar sobre el barco
     * @param location la coordenada del disparo
     * @return un mapa de las partes del barco y su estado
     */
    public Map<Point, EstadoCasilla> nosDisparan(Point location) {
        partesDelBarco.replace(location, EstadoCasilla.Tocado);

        if (nosHudieron())
            for (Point punto : partesDelBarco.keySet())
                partesDelBarco.replace(punto, EstadoCasilla.Hundido);

        return partesDelBarco;
    }

    /**
     * Gets the tipo.
     * @return the tipo
     */
    public TipoBarco getTipo() {
        return tipo;
    }

    /**
     * Gets the orientacion.
     * @return the orientacion
     */
    public Orientation getOrientacion() {
        return orientacion;
    }

    /**
     * Gets the points.
     * @return the points
     */
    public List<Point> getPoints() {
        return ubicarBarco(tipo, location, orientacion);
    }

    /**
     * Gets the partes del barco
     * @return the partes del barco
     */
    public Map<Point, EstadoCasilla> getPartesDelBarco() {
        return partesDelBarco;
    }
}
