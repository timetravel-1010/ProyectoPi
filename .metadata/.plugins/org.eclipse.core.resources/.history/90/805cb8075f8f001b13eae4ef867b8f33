/*
 * Programación Interactiva
 * Autor: Julián Andrés Orejuela Erazo - 1541304 
 * Autor: Daniel Felipe Vélez Cuaical - 1924306
 * Mini proyecto 1: Juego de entrenamiento de memoria
 */

package entrenamientoDeMemoria;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

// TODO: Auto-generated Javadoc
/**
 * The Class VistaEntrenamientoDeMemoria. Clase encargada de realizar las operaciones de E/S por medio una ventana (JFrame).
 */
public class VistaEntrenamientoDeMemoria extends JFrame {
    
    // atributos
    private Escucha listener;
    private ControlEntrenamientoDeMemoria control;
    private JLabel[] cartasLabel;
    private Timer timer;
    private ArrayList<Integer> cartas;
    private JPanel zonaJuego, zonaIndicaciones;
    private JLabel cartaGanadora, ganastePerdiste, contador, contadorCero, siguienteRondaText, indicaciones, separador;
    private JTextPane instrucciones;
    private Map<Integer, PanelProperties> panelPropertiesByCartasSize;
    private boolean siguienteRonda;
    private List<Color> bgcolorZonaJuego;
    private InputStream loadFont;
    private Font niagaraphobia;
    private final int TIEMPO_DE_ESPERA = 5;
    
    // metodos
    
    /**
     * Instantiates a new vista entrenamiento de memoria. Constructor de la clase. Inicializa los atributos de la clase (auxiliares y componentes graficos) y realiza la configuracion de la ventana principal.
     */
    VistaEntrenamientoDeMemoria() {
        // propiedades para cada panel de cartas, en funcion de las cartas a mostrar
        panelPropertiesByCartasSize = new HashMap<>();
        panelPropertiesByCartasSize.put(4, new PanelProperties(4, 2, null, 225));
        panelPropertiesByCartasSize.put(6, new PanelProperties(6, 3, null, 200));
        panelPropertiesByCartasSize.put(8, new PanelProperties(9, 3, Arrays.asList(new Integer[] { 4 }), 150));
        panelPropertiesByCartasSize.put(10, new PanelProperties(12, 4, Arrays.asList(new Integer[] { 5, 6 }), 150));
        panelPropertiesByCartasSize.put(12, new PanelProperties(12, 4, null, 150));

        // colores de fondo para la zona de juego
        bgcolorZonaJuego = new ArrayList<Color>();
        bgcolorZonaJuego.add(new Color(243, 105, 26));
        bgcolorZonaJuego.add(new Color(42, 69, 78));
        bgcolorZonaJuego.add(new Color(212, 31, 31));
        bgcolorZonaJuego.add(new Color(187, 212, 51));
        bgcolorZonaJuego.add(new Color(24, 219, 151));
        bgcolorZonaJuego.add(new Color(222, 69, 124));
        bgcolorZonaJuego.add(new Color(160, 82, 45));
        bgcolorZonaJuego.add(new Color(255, 140, 0));

        siguienteRonda = false;
        
        initGUI();

        // establecer la configuración de ventana predeterminada.
        this.setTitle("Delicious Memory");
        this.setSize(956, 560);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(getClass().getResource("/images/hamburguesa.png")).getImage());
        this.setVisible(true);
    }

    /**
     * Inits the GUI. Inicializa la interfaz grafica. Se configuran y crean el container, los objetos, y los componentes graficos.
     */
    private void initGUI() {
    	
    	// configurar el container de la ventana y su layout.
        this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        // se crean los objetos escucha, control y otros.
        control = new ControlEntrenamientoDeMemoria();
        listener = new Escucha();
        timer = new Timer(1000, listener);
        cartasLabel = new JLabel[12];
        
        for (int i = 0; i < 12; i++)
            cartasLabel[i] = new JLabel();

        // #---------------------------------------------------------------------------
        // # Fuente

        loadFont = getClass().getResourceAsStream("/fonts/Niagaraphobia-Bro3.ttf");
        try {
            niagaraphobia = Font.createFont(Font.TRUETYPE_FONT, loadFont);
        } catch (FontFormatException e) {
        } catch (IOException e) {
        }

        // #---------------------------------------------------------------------------
        // # Paneles

        zonaJuego = new JPanel();
        zonaJuego.setPreferredSize(new Dimension(730, 521));
        add(zonaJuego);

        zonaIndicaciones = new JPanel();
        zonaIndicaciones.setPreferredSize(new Dimension(210, 521));
        zonaIndicaciones.setBackground(new Color(255, 255, 255));
        add(zonaIndicaciones);

        // #---------------------------------------------------------------------------
        // # Zona Indicaciones

        cartaGanadora = new JLabel();
        cartaGanadora.setBorder(new EmptyBorder(25, 0, 5, 0));
        zonaIndicaciones.add(cartaGanadora);

        indicaciones = new JLabel("¡Mira estas delicias!");
        indicaciones.setFont(niagaraphobia.deriveFont(24f));
        zonaIndicaciones.add(indicaciones);

        separador = new JLabel("       _____________________________");
        separador.setFont(new Font("Arial", Font.BOLD, 35));
        separador.setForeground(new Color(255, 87, 51));
        zonaIndicaciones.add(separador);

        instrucciones = new JTextPane();
        instrucciones.setText(
                "Para ganar este juego\nmemorizar muy bien debes\nantes de que termine\nel tiempo\n\n¡Apresurate!\nTic Toc... Tic Toc...");
        instrucciones.setFont(niagaraphobia.deriveFont(19f));

        // centrar el texto de las instrucciones
        StyledDocument doc = instrucciones.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        instrucciones.setBorder(new EmptyBorder(15, 20, 16, 20));
        instrucciones.setEditable(false);
        instrucciones.setForeground(new Color(100, 100, 100));
        instrucciones.setHighlighter(null);
        zonaIndicaciones.add(instrucciones);

        contador = new JLabel();
        contador.setFont(niagaraphobia.deriveFont(30f));
        contador.setHorizontalAlignment(SwingConstants.CENTER);

        contadorCero = new JLabel("0");
        contadorCero.setFont(niagaraphobia.deriveFont(30f));

        // #---------------------------------------------------------------------------
        // # Fin de Ronda

        ganastePerdiste = new JLabel();
        ganastePerdiste.setFont(niagaraphobia.deriveFont(100f));
        ganastePerdiste.setBounds(30, 150, 730, 100);
        ganastePerdiste.setHorizontalAlignment(SwingConstants.CENTER);

        siguienteRondaText = new JLabel("La siguiente ronda comenzará en...");
        siguienteRondaText.setFont(niagaraphobia.deriveFont(20f));
        siguienteRondaText.setBounds(30, 230, 730, 100);
        siguienteRondaText.setHorizontalAlignment(SwingConstants.CENTER);

        // #---------------------------------------------------------------------------

        mostrarCartas();
    }

    /**
     * Mostrar cartas. Configura la zonaJuego y zonaIndicaciones al iniciar una ronda.
     */
    private void mostrarCartas() {
        cartas = control.revolverCartas();

        // # Zona Juego
        zonaJuego.setLayout(new GridBagLayout());
        zonaJuego.setBackground(bgcolorZonaJuego.get((int) (Math.random() * 8)));
        refreshZonaJuego();

        mostrarPanelDeCartas(panelPropertiesByCartasSize.get(cartas.size()));

        // # Zona Indicaciones
        cartaGanadora.setIcon(getImage("ojo", 150));
        indicaciones.setText("¡Mira estas delicias!");
        contador.setText(String.valueOf(control.getTiempoDeEspera()));
        zonaIndicaciones.remove(contadorCero);
        zonaIndicaciones.add(contador);

        timer.start();
    }

    /**
     * Mostrar panel de cartas. Agrega las cartas a la zonaJuego de acuerdo a las propiedades especificadas.
     *
     * @param props the props
     */
    private void mostrarPanelDeCartas(PanelProperties props) {
        GridBagConstraints constraints = new GridBagConstraints();

        for (int i = 0; i < props.numeroDeCeldas; i++) {
            if (props.restricciones.indexOf(i) != -1)
                continue;

            constraints.gridx = i % props.columnas;
            constraints.gridy = (int) Math.floor(i / props.columnas);
            constraints.gridwidth = 1;
            constraints.gridheight = 1;

            cartasLabel[i].removeMouseListener(listener);
            cartasLabel[i]
                    .setBorder(cartas.size() == 8 ? new EmptyBorder(10, 25, 10, 25) : new EmptyBorder(10, 10, 10, 10));
            cartasLabel[i].setIcon(getImage("C" + props.size + "." + cartas.get(getCartaIndex(i)), props.size));
            cartasLabel[i].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            zonaJuego.add(cartasLabel[i], constraints);
        }
    }

    /**
     * Ocultar cartas. Cambia las imagenes de las cartas por imagenes con numeros,  agrega la carta a buscar y las indicaciones a zonaIndicaciones.
     */
    private void ocultarCartas() {
        PanelProperties props = panelPropertiesByCartasSize.get(cartas.size());

        // # Zona Juego
        for (int i = 0; i < props.numeroDeCeldas; i++) {
            if (props.restricciones.indexOf(i) != -1)
                continue;

            cartasLabel[i].addMouseListener(listener);
            cartasLabel[i].setIcon(getImage("H" + props.size + "." + getCartaIndex(i + 1), props.size));
            cartasLabel[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // # Zona Indicaciones
        cartaGanadora.setIcon(getImage("C150." + control.getCartaGanadora(), 150));
        indicaciones.setText("¿En dónde estaba... ?");
    }

    /**
     * Determinar juego. Determina si se gana o pierde una ronda de acuerdo a la carta especificada.
     *
     * @param carta the carta
     */
    private void determinarJuego(int carta) {
        if (control.determinarRonda(carta)) {
            ganastePerdiste.setText("¡Ganaste!");
            ganastePerdiste.setForeground(new Color(180, 230, 47));
        } else {
            ganastePerdiste.setText("Perdiste... :(");
            ganastePerdiste.setForeground(new Color(215, 14, 14));
        }

        contador.setText(String.valueOf(TIEMPO_DE_ESPERA));
        contador.setBounds(30, 270, 730, 100);

        zonaJuego.setBackground(Color.WHITE);
        zonaJuego.setLayout(null);
        refreshZonaJuego();
        zonaJuego.add(ganastePerdiste);
        zonaJuego.add(siguienteRondaText);
        zonaJuego.add(contador);
        zonaIndicaciones.add(contadorCero);

        siguienteRonda = true;
        timer.start();
    }

    // #---------------------------------------------------------------------------
    // # FUNCIONES AUXILIARES
    // #---------------------------------------------------------------------------

    /**
     * Gets the carta index. Retorna el indice de una carta de acuerdo a un numeroDeCelda.
     *
     * @param numeroDeCelda the numero de celda
     * @return the carta index
     */
    private int getCartaIndex(int numeroDeCelda) {
        if (cartas.size() == 8)
            if (numeroDeCelda > 4)
                return numeroDeCelda - 1;
        if (cartas.size() == 10)
            if (numeroDeCelda > 6)
                return numeroDeCelda - 2;
        return numeroDeCelda;
    }

    /**
     * Gets the image. Retorna un ImageIcon con una imagen y dimension especificadas.
     *
     * @param name the name
     * @param size the size
     * @return the image
     */
    private ImageIcon getImage(String name, int size) {
        return new ImageIcon(new ImageIcon(this.getClass().getResource("/images/" + name + ".png")).getImage()
                .getScaledInstance(size, size, Image.SCALE_DEFAULT));
    }

    /**
     * Refresh zona juego. Actualiza la zona de juego quitando los componentes anteriores.
     */
    private void refreshZonaJuego() {
        zonaJuego.removeAll();
        zonaJuego.revalidate();
        zonaJuego.repaint();
    }

    // #---------------------------------------------------------------------------
    // # LISTENER
    // #---------------------------------------------------------------------------

    /**
     * The Class Escucha. Clase interna encargada de manejar los eventos de la ventana.
     */
    private class Escucha implements ActionListener, MouseListener {

        /**
         * Action performed.
         *
         * @param event the event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == timer)
                // si el contador está en 0
                if (Integer.parseInt(contador.getText()) == 0) {
                    timer.stop();
                    if (siguienteRonda) {
                        mostrarCartas();
                        siguienteRonda = false;
                    } else
                        ocultarCartas();
                } else
                    // reducir el contador en 1
                    contador.setText(String.valueOf(Integer.parseInt(contador.getText()) - 1));
        }

        /**
         * Mouse clicked.
         *
         * @param event the event
         */
        @Override
        public void mouseClicked(MouseEvent event) {
            for (int i = 0; i < 12; i++)
                if (event.getSource() == cartasLabel[i]) {
                    determinarJuego(getCartaIndex(i));
                    break;
                }
        }

        /**
         * Mouse pressed.
         *
         * @param e the e
         */
        @Override
        public void mousePressed(MouseEvent e) {

        }

        /**
         * Mouse released.
         *
         * @param e the e
         */
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        /**
         * Mouse entered.
         *
         * @param e the e
         */
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Mouse exited.
         *
         * @param e the e
         */
        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    // #---------------------------------------------------------------------------
    // # STRUCTURES
    // #---------------------------------------------------------------------------

    /**
     * Structure PanelProperties. Estructura que define las propiedades de un panel de cartas
     */
    private class PanelProperties {
        
        /** The numero de celdas. */
        public int numeroDeCeldas;
        
        /** The columnas. */
        public int columnas;
        
        /** The restricciones. */
        public List<Integer> restricciones;
        
        /** The size. */
        public int size;

        /**
         * Instantiates a new panel properties. Constructor de la clase. Se encarga de dar valores iniciales a los atributos.
         *
         * @param numeroDeCeldas the numero de celdas
         * @param columnas the columnas
         * @param restricciones the restricciones
         * @param size the size
         */
        PanelProperties(int numeroDeCeldas, int columnas, List<Integer> restricciones, int size) {
            this.numeroDeCeldas = numeroDeCeldas;
            this.columnas = columnas;
            this.restricciones = restricciones != null ? restricciones : Collections.emptyList();
            this.size = size;
        }
    }

}