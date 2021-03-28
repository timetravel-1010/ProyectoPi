/*
 * Programacion Interactiva
 * Autor: Julian Andres Orejuela Erazo - 1541304 
 * Autor: Daniel Felipe Velez Cuaical - 1924306
 * Mini proyecto 2: Juego de batalla naval.
 */
package batallaNaval;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;

/**
 * Clase que representa y controla el mapa donde se ubican los barcos de cada jugador.
 */
public class MapaBatallaNaval extends JPanel {

    // # Relacion conoce un game y una vista
    private final int ASCII_CHAR_A = 65;
    private BatallaNavalGame batallaNaval;
    private BatallaNavalVista vista;
    private Escucha listener;
    private JButton[][] casillas;
    private JLabel coordenada;
    private List<JLabel> coordenadas;
    private TipoBarco tipo;
    private List<Orientation> orientacionesValidas;
    private Point casilla;

    private int mapSize;
    private int orientacion;
    private int casillaSize;
    private int casillaMinSize;
    private boolean lockCells;

    private Font Parikesit;
    private Font Russo;

    private BufferedImage bufferPortaviones = null;
    private BufferedImage bufferDestructor = null;
    private BufferedImage bufferFragata = null;
    private BufferedImage bufferSubmarino = null;
    private BufferedImage fire, dark, ximg;

    private BufferedImage portavionesMin = null;
    private BufferedImage destructorMin = null;
    private BufferedImage fragataMin = null;
    private BufferedImage submarinoMin = null;
    private BufferedImage fireMin, darkMin, ximgMin;

    private Map<TipoBarco, BufferedImage> imagenBarcos = new HashMap<>();
    private Map<TipoBarco, BufferedImage> imagenBarcosMin = new HashMap<>();

    /**
     * Instantiates a new mapa batalla naval.
     *
     * @param batallaNaval el objeto control de tipo BatallaNavalGame
     * @param vista el objeto vista de tipo BatallaNavalVista
     */
    public MapaBatallaNaval(BatallaNavalGame batallaNaval, BatallaNavalVista vista) {

        try {

            bufferPortaviones = ImageIO.read(getClass().getResource("/images/portavion.png"));
            bufferDestructor = ImageIO.read(getClass().getResource("/images/destructor.png"));
            bufferFragata = ImageIO.read(getClass().getResource("/images/fragata.png"));
            bufferSubmarino = ImageIO.read(getClass().getResource("/images/submarino.png"));
            fire = ImageIO.read(getClass().getResource("/images/fire.png"));
            dark = ImageIO.read(getClass().getResource("/images/dark.png"));
            ximg = ImageIO.read(getClass().getResource("/images/x.png"));

            // carga de imagenes reducidas
            portavionesMin = ImageIO.read(getClass().getResource("/images/portavion-min.png"));
            destructorMin = ImageIO.read(getClass().getResource("/images/destructor-min.png"));
            fragataMin = ImageIO.read(getClass().getResource("/images/fragata-min.png"));
            submarinoMin = ImageIO.read(getClass().getResource("/images/submarino-min.png"));
            fireMin = ImageIO.read(getClass().getResource("/images/fire-min.png"));
            darkMin = ImageIO.read(getClass().getResource("/images/dark-min.png"));
            ximgMin = ImageIO.read(getClass().getResource("/images/x-min.png"));

            imagenBarcos.put(TipoBarco.Portavion, bufferPortaviones);
            imagenBarcos.put(TipoBarco.Destructor, bufferDestructor);
            imagenBarcos.put(TipoBarco.Fragata, bufferFragata);
            imagenBarcos.put(TipoBarco.Submarino, bufferSubmarino);

            imagenBarcosMin.put(TipoBarco.Portavion, portavionesMin);
            imagenBarcosMin.put(TipoBarco.Destructor, destructorMin);
            imagenBarcosMin.put(TipoBarco.Fragata, fragataMin);
            imagenBarcosMin.put(TipoBarco.Submarino, submarinoMin);

            this.batallaNaval = batallaNaval;
            this.vista = vista;
            this.mapSize = batallaNaval.getMapSize();
            this.tipo = batallaNaval.cambiarTipoBarco();
            this.casilla = null;
            this.orientacion = 0;
            this.orientacionesValidas = new ArrayList<Orientation>();
            this.casillas = new JButton[mapSize][mapSize];
            this.casillaSize = 50;
            this.casillaMinSize = 31;
            this.lockCells = false;
            this.coordenadas = new ArrayList<JLabel>();

            Parikesit = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/fonts/Parikesit-0ZYR.ttf"));
            Russo = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/RussoOne-Regular.ttf"));

            initGUI();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se ha podido cargar la imagen");
        }
    }

    /**
     * Muestra el marco de la esquina superior izquierda y el fondo del mapa
     *
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(new ImageIcon(getClass().getResource("/images/textura_agua.png")).getImage(), 0, 0, getWidth(),
                getHeight(), this);
        g.drawImage(new ImageIcon(getClass().getResource("/images/text-madera.png")).getImage(), 0, 0, getWidth(),
                getHeight(), this);

        setOpaque(false);
        super.paint(g);
    }

    /**
     * Inits the GUI.
     */
    private void initGUI() {
        this.setLayout(new GridLayout(mapSize + 1, mapSize + 1));

        listener = new Escucha();

        coordenada = new JLabel();
        coordenada.setPreferredSize(new Dimension(casillaSize, casillaSize));
        coordenada.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        add(coordenada);

        // agregar las letras a las casillas superiores
        for (int k = 0; k < mapSize; k++) {
            coordenada = new JLabel(String.valueOf((char) (k + ASCII_CHAR_A)), SwingConstants.CENTER);
            coordenada.setPreferredSize(new Dimension(casillaSize, casillaSize));
            coordenada.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
            coordenada.setFont(Parikesit.deriveFont(18f));
            coordenada.setForeground(Color.WHITE);
            coordenadas.add(coordenada);
            add(coordenada);
        }

        // agregar los numeros a las casillas laterales y los botones
        for (int fila = 0; fila < mapSize; fila++) {
            for (int columna = 0; columna < mapSize; columna++) {
                // agregar los numeros a las casillas laterales de la izquierda
                if (columna == 0) {
                    coordenada = new JLabel(String.valueOf(fila + 1), SwingConstants.CENTER);
                    coordenada.setPreferredSize(new Dimension(casillaSize, casillaSize));
                    coordenada.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                    coordenada.setFont(Russo.deriveFont(16f));
                    coordenada.setForeground(Color.WHITE);
                    coordenadas.add(coordenada);
                    add(coordenada);
                }
                // agregar los botones al mapa
                casillas[fila][columna] = new JButton();
                casillas[fila][columna].setBackground(Color.BLUE);
                casillas[fila][columna].setPreferredSize(new Dimension(casillaSize, casillaSize));
                casillas[fila][columna].setCursor(new Cursor(Cursor.HAND_CURSOR));
                casillas[fila][columna].addActionListener(listener);
                casillas[fila][columna].addMouseListener(listener);
                casillas[fila][columna].setContentAreaFilled(false);

                add(casillas[fila][columna]);
            }
        }
    }

    /**
     * Controla lo que ocurre al hacer click en una casilla del mapa.
     * @param fila 
     * @param columna 
     */
    private void casillaClicked(int fila, int columna) {

        if (orientacionesValidas.size() > 0)
            descartarBarco(false);

        casilla = new Point(columna, fila);

        if (!batallaNaval.haEmpezado() && tipo != null) {
            // # proceso de ubicacion de barcos

            for (Orientation orientacion : Orientation.values())
                if (validarUbicacion(Barco.ubicarBarco(tipo, casilla, orientacion)))
                    orientacionesValidas.add(orientacion);

            if (orientacionesValidas.size() > 0) {
                mostrarBarco(Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(0)), Color.GRAY);
                asignarImagenes(Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(0)), imagenBarcos);
            } else {
                // no se puede ubicar el barco en esas casillas
            }

        } else if (!batallaNaval.haEmpezado() && tipo == null) {
            // se intenta ubicar un barco pero no se ha seleccionado un tipo
        } else {
            // # el juego ya ha empezado
            if (casillas[fila][columna].getBackground() == Color.BLUE) {
                Map<Point, EstadoCasilla> resultado = batallaNaval.disparar(BatallaNavalGame.Jugador.AI, casilla);
                dibujarDisparo(resultado, false);
                vista.siguienteTurno();
            }
        }
    }

    /**
     * Remueve cualquier icono que tengan las casillas de los barcos
     * @param puntos lista de puntos para quitarles el icono
     */
    public void ocultarBarcos(List<Point> puntos) {
        for (Point punto : puntos)
            casillas[(int) punto.getY()][(int) punto.getX()].setIcon(null);
    }

    /**
     * Muestra las imagenes de los barcos del conjunto especificado
     * @param barcos conjunto de barcos
     */
    public void mostrarBarco(Set<Barco> barcos) {
        for (Barco barco : barcos) {
            asignarImagenes(barco.getTipo(), barco.getOrientacion(), barco.getPoints(), imagenBarcos, casillaSize);
        }
    }

    /**
     * Asigna las imagenes correspondientes a la lista de puntos especificada
     * @param puntos lista de puntos del barco
     * @param mapa mapa que contiene la imagen de los barcos
     */
    public void asignarImagenes(List<Point> puntos, Map<TipoBarco, BufferedImage> mapa) {
        asignarImagenes(tipo, orientacionesValidas.get(orientacion), puntos, mapa, casillaSize);
    }

    /**
     * Asigna las imagenes correspondientes a la lista de puntos teninedo en cuenta los otros parametros ingresados.
     * @param tipo el tipo de barco
     * @param orientacion la orientacion del barco
     * @param puntos lista de puntos del barco
     * @param mapa mapa que contiene la imagen de los barcos
     * @param casilla dimension de la casilla 
     */
    public void asignarImagenes(TipoBarco tipo, Orientation orientacion, List<Point> puntos,
            Map<TipoBarco, BufferedImage> mapa, int casilla) {

        double anguloRotacion = (tipo != TipoBarco.Fragata) ? (double) orientacion.angle : 0;
        BufferedImage imgBarcoRotado = rotarImagen(mapa.get(tipo), anguloRotacion);

        // ! se debe cambiar pseudoCasillaSize por casillaSize
        int pseudoCasillaSize = casilla;

        // # proceso para obtener una subImagen de acuerdo a la orientacion del barco.
        for (int i = 0; i < tipo.size; i++) {
            int x = 0, y = 0;
            if (orientacion == Orientation.DER)
                x = i * pseudoCasillaSize;
            if (orientacion == Orientation.ABJ)
                y = i * pseudoCasillaSize;
            if (orientacion == Orientation.ARB)
                y = (tipo.size - i - 1) * pseudoCasillaSize;
            if (orientacion == Orientation.IZQ)
                x = (tipo.size - i - 1) * pseudoCasillaSize;

            // se agregan las imagenes a las casillas correspondientes
            BufferedImage subImagen = imgBarcoRotado.getSubimage(x, y, pseudoCasillaSize, pseudoCasillaSize);
            casillas[(int) puntos.get(i).getY()][(int) puntos.get(i).getX()].setIcon(new ImageIcon(subImagen));
        }
    }

    /**
     * Muestra el resultado del disparo a una casilla del mapa
     * @param resultado mapa con las ubicaciones y el estado de las casillas
     * @param disparoAJugador determina si se le esta o no disparando
    al jugador (human)
     */
    public void dibujarDisparo(Map<Point, EstadoCasilla> resultado, boolean disparoAJugador) {

        Map<EstadoCasilla, Color> colorEstadoCasilla = new HashMap<>();
        colorEstadoCasilla.put(EstadoCasilla.Disparo, new Color(24, 235, 168));
        colorEstadoCasilla.put(EstadoCasilla.Tocado, new Color(235, 222, 24));
        colorEstadoCasilla.put(EstadoCasilla.Hundido, new Color(235, 130, 24));
        colorEstadoCasilla.put(EstadoCasilla.aFlote, Color.BLACK);

        Barco elBarco;
        Point primerPunto = (Point) new ArrayList(resultado.keySet()).get(0);
        if (resultado.get(primerPunto) == EstadoCasilla.Hundido) {
            if (disparoAJugador) {
                elBarco = batallaNaval.getMap(BatallaNavalGame.Jugador.human).get(primerPunto);
                asignarImagenes(elBarco.getTipo(), elBarco.getOrientacion(), elBarco.getPoints(), imagenBarcosMin,
                        casillaMinSize);
            } else {
                elBarco = batallaNaval.getMap(BatallaNavalGame.Jugador.AI).get(primerPunto);
                asignarImagenes(elBarco.getTipo(), elBarco.getOrientacion(), elBarco.getPoints(), imagenBarcos,
                        casillaSize);
            }
        }

        for (Point punto : resultado.keySet())
            if (resultado.get(punto) != EstadoCasilla.aFlote) {

                casillas[(int) punto.getY()][(int) punto.getX()]
                        .setBackground(colorEstadoCasilla.get(resultado.get(punto)));

                BufferedImage fuego = disparoAJugador ? fireMin : fire;
                BufferedImage fuegoOscuro = disparoAJugador ? darkMin : dark;
                BufferedImage imagenX = disparoAJugador ? ximgMin : ximg;

                int pseudoCasillaSize = disparoAJugador ? 31 : 50;

                BufferedImage combined = new BufferedImage(pseudoCasillaSize, pseudoCasillaSize,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics g = combined.getGraphics();
                Icon casillaIcon = casillas[(int) punto.getY()][(int) punto.getX()].getIcon();

                if (casillaIcon != null)
                    g.drawImage(iconToImage(casillaIcon), 0, 0, null);
                if (resultado.get(punto) == EstadoCasilla.Tocado)
                    g.drawImage(fuego, 0, 0, null);
                if (resultado.get(punto) == EstadoCasilla.Hundido) {
                    g.drawImage(fuego, 0, 0, null);
                    g.drawImage(fuegoOscuro, 0, 0, null);
                }
                if (resultado.get(punto) == EstadoCasilla.Disparo)
                    g.drawImage(imagenX, 0, 0, null);

                g.dispose();
                casillas[(int) punto.getY()][(int) punto.getX()].setIcon(new ImageIcon(combined));
            }
    }

    /**
     * Transform an Icon to an image
     * @param icon 
     * @return the image
     */
    private Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    /**
     * Activa o desactiva los botones del mapa
     * @param b activar o desactivar
     */
    public void activarBotones(boolean b) {
        lockCells = !b;

        // efecto del cursor
        if (lockCells)
            for (int fila = 0; fila < mapSize; fila++)
                for (int columna = 0; columna < mapSize; columna++)
                    casillas[fila][columna].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        else
            for (int fila = 0; fila < mapSize; fila++)
                for (int columna = 0; columna < mapSize; columna++)
                    casillas[fila][columna].setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    /**
     * Deja sin el efecto de hover a los JButtons del mapa
     */
    public void quitarEfecto() {
        for (int fila = 0; fila < mapSize; fila++)
            for (int columna = 0; columna < mapSize; columna++)
                casillas[fila][columna].setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153), 1));
    }

    /**
     * Modifica la dimension del mapa de acuerdo al parametro ingresado
     * @param flag 
     */
    public void reducirMapa(boolean flag) {
        if (flag) {
        	setPreferredSize(new Dimension(341, 341));
        	for(JLabel coordenada: coordenadas) 
        		coordenada.setFont(Russo.deriveFont(12f));
        }
        else {
            setPreferredSize(new Dimension(550, 550));
            for(JLabel coordenada: coordenadas) 
        		coordenada.setFont(Russo.deriveFont(18f));
        }
    }

    /**
     * Reinicia las variables de MapaBatallaNaval
     */
    public void reiniciar() {
        this.tipo = batallaNaval.cambiarTipoBarco();
        this.casilla = null;
        this.orientacion = 0;
        this.orientacionesValidas = new ArrayList<Orientation>();
        this.casillas = new JButton[mapSize][mapSize];

        reducirMapa(false);
        lockCells = false;
        for (TipoBarco tipo : TipoBarco.values()) {
            vista.actualizarBotones(tipo);
        }

        this.removeAll();
        this.initGUI();
        this.revalidate();
        this.repaint();
    }

    /**
     * Asigna las imagenes reducidas correspondientes a los barcos del mapa del jugador (human)
     */
    public void setImagenesMin() {
        Set<Barco> barcos = new HashSet<Barco>(batallaNaval.getMap(BatallaNavalGame.Jugador.human).values());

        for (Barco barco : barcos) {
            asignarImagenes(barco.getTipo(), barco.getOrientacion(), barco.getPoints(), imagenBarcosMin,
                    casillaMinSize);
        }
    }

    // #---------------------------------------------------------------------------
    // # METODOS PARA UBICAR LOS BARCOS
    // #---------------------------------------------------------------------------

    /**
     * Muestra en el mapa un barco ubicado en las casillas de puntos con el color dado
     * @param puntos
     * @param unColor
     */
    public void mostrarBarco(List<Point> puntos, Color unColor) {
        for (Point punto : puntos)
            casillas[(int) punto.getY()][(int) punto.getX()].setBackground(unColor);
    }

    /**
     * Cambia el tipo de barco seleccionado y descarta cualquier proceso de ubicacion anterior
     * @param tipo tipo de barco
     */
    public void setBarcoSeleccionado(TipoBarco tipo) {
        descartarBarco();
        this.tipo = tipo;
    }

    /**
     * Rota el barco que esta en proceso de ubicacion en el sentido de las manecillas del reloj
     */
    public void rotarBarco() {
        if (tipo != null && casilla != null) {
            // # proceso de rotacion del barco
            for (Point punto : Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion))) {
                casillas[(int) punto.getY()][(int) punto.getX()].setIcon(null);
            }
            mostrarBarco(Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion)), Color.BLUE);

            orientacion = (orientacion < (orientacionesValidas.size() - 1)) ? orientacion + 1 : 0;

            mostrarBarco(Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion)), Color.GRAY);

            asignarImagenes(Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion)), imagenBarcos);
        }
    }

    /**
     * Fija un barco en el mapa del jugador (human)
     */
    // ! el misterio del tipo = null cuando doble click sobre un barco puesto
    public void confirmarBarco() {
        if (!(orientacionesValidas.size() > 0)) // si no hay un proceso de ubicar barco, no se puede fijar nada
            return;
        if (tipo != null && casilla != null) {
            for (Point punto : Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion)))
                casillas[(int) punto.getY()][(int) punto.getX()].setBackground(Color.BLACK);

            // se ubica el barco en el mapa
            batallaNaval.ubicarBarco(BatallaNavalGame.Jugador.human, casilla, orientacionesValidas.get(orientacion),
                    tipo);

            batallaNaval.restarUnBarco(tipo);
            vista.actualizarBotones(tipo);
            if (batallaNaval.getCantidadBarco(tipo) <= 0) {
                tipo = batallaNaval.cambiarTipoBarco();
                if (tipo == null)
                    vista.iniciarJuego();
            }

            resetProcesoUbicacion(false);
        }
    }

    /**
     * Verifica que los puntos esten libres en el mapa
     * @param puntos
     * @return true si es valida
     */
    private boolean validarUbicacion(List<Point> puntos) {
        for (Point punto : puntos) {
            // el barco no se salga del mapa
            if (!(punto.getX() < mapSize && punto.getX() >= 0 && punto.getY() < mapSize && punto.getY() >= 0))
                return false;

            // verificar que la posicion no este ya ocupada por otro barco
            if (!(casillas[(int) punto.getY()][(int) punto.getX()].getBackground() == Color.BLUE))
                return false;
        }
        return true;
    }

    /**
     * Descarta el proceso de ubicacion de un barco
     * @param totalClean determina si se hace una limpieza total de las variables o solo parcial
     */
    private void descartarBarco(boolean totalClean) {
        // si hay actualmente un barco en proceso de ubicacion
        if (orientacionesValidas.size() > 0) {
            for (Point punto : Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion))) {
                casillas[(int) punto.getY()][(int) punto.getX()].setIcon(null);
            }
            mostrarBarco(Barco.ubicarBarco(tipo, casilla, orientacionesValidas.get(orientacion)), Color.BLUE);
            resetProcesoUbicacion(totalClean);
        }
    }

    /**
     * Descarta el proceso de ubicacion de un barco, haciendo una limpieza total de las variables
     */
    private void descartarBarco() {
        descartarBarco(true);
    }

    /**
     * Resetea el proceso de ubicacion
     * @param totalClean determina si se hace una limpieza total de las variables o solo parcial
     */
    private void resetProcesoUbicacion(boolean totalClean) {
        orientacion = 0;
        orientacionesValidas.clear();
        casilla = null;
        if (totalClean)
            tipo = null;
    }

    /**
     * rotar una imagen de acuerdo a los parametros
     * @param img
     * @param angle angulo de rotacion, en grados
     * @return the buffered image
     */
    public BufferedImage rotarImagen(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
        g2d.dispose();

        return rotated;
    }

    // #---------------------------------------------------------------------------
    // # LISTENER
    // #---------------------------------------------------------------------------

    /**
     * The Class Escucha.
     */
    private class Escucha extends MouseAdapter implements ActionListener {

        /**
         * Action performed.
         *
         * @param event the event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if (!lockCells)
                for (int fila = 0; fila < mapSize; fila++)
                    for (int columna = 0; columna < mapSize; columna++)
                        if (event.getSource() == casillas[fila][columna])
                            // ! nos puede ocasionar problemas a la hora de disparar
                            if (casilla == null || !(casilla.getX() == columna && casilla.getY() == fila))
                                casillaClicked(fila, columna);

        }

        /**
         * Mouse clicked.
         *
         * @param evnt the evnt
         */
        @Override
        public void mouseClicked(MouseEvent evnt) {
            if (!lockCells)
                if (!batallaNaval.haEmpezado()) {
                    if (evnt.getButton() == MouseEvent.BUTTON1 && evnt.getClickCount() == 2) // click izquierdo
                        confirmarBarco();
                    if (evnt.getButton() == MouseEvent.BUTTON3) // click derecho
                        rotarBarco();
                }
        }

    }
}