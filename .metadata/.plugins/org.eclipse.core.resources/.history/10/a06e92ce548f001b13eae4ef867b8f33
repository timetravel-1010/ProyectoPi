/*
 * Programacion Interactiva
 * Autor: Julian Andres Orejuela Erazo - 1541304 
 * Autor: Daniel Felipe Velez Cuaical - 1924306
 * Mini proyecto 2: Juego de batalla naval.
 */
package batallaNaval;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

// TODO: Auto-generated Javadoc
/**
 * Clase encargada de representar el juego y de realizar las operaciones de E/S por medio de un JFrame.
 */
public class BatallaNavalVista extends JFrame {

    private BatallaNavalGame batallaNaval;
    private MapaBatallaNaval mapaJugador, mapaAI;
    private JPanel panelEstado;
    private Escucha listener;
    private JTextPane instrucciones;
    private boolean loading;
    private JLabel title;
    private Font Russo;

    // # ubicar los barcos
    private Map<JButton, TipoBarco> barcosParaUbicar;
    private JButton rotar, confirmar, mostrarAI, play, botonReiniciar;

    // # juego
    private final int TIEMPO_DE_ESPERA = 1;
    private Timer turno;
    private int contador;

    /**
     * Instantiates a new batalla naval vista.
     */
    public BatallaNavalVista() {
        batallaNaval = new BatallaNavalGame(10);
        contador = TIEMPO_DE_ESPERA;
        loading = true;

        initGUI();

        this.setSize(1066, 680);
        this.setTitle("Battleship");
        this.setIconImage(new ImageIcon(getClass().getResource("/images/ship.png")).getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    /**
     * Muestra una imagen de fondo al inicio del juego.
     *
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (loading)
            g.drawImage(getImage("battleship.jpg").getImage(), 0, 30, getWidth(), getHeight() - 30, this);
    }

    /**
     * Inits the content pane.
     */
    private void initContentPane() {
        JPanel contentPane = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(getImage("background.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBorder(new EmptyBorder(14, 0, 5, 0));
        this.setContentPane(contentPane);
    }

    /**
     * Inits the GUI.
     */
    private void initGUI() {
        this.setLayout(new FlowLayout());
        listener = new Escucha();
        turno = new Timer(3000, listener);
        turno.start();

        // #---------------------------------------------------------------------------
        // # Fuente

        try {
            Russo = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/RussoOne-Regular.ttf"));
        } catch (Exception e) {
        }

        // #---------------------------------------------------------------------------
        // # General

        mapaJugador = new MapaBatallaNaval(batallaNaval, this);
        mapaAI = new MapaBatallaNaval(batallaNaval, this);

        panelEstado = new JPanel();
        panelEstado.setForeground(Color.white);
        panelEstado.setLayout(new FlowLayout());
        panelEstado.setPreferredSize(new Dimension(385, 600));
        panelEstado.setOpaque(false);
        panelEstado.setBorder(new EmptyBorder(0, 15, 0, 0));

        title = new JLabel();
        title.setIcon(getImage("title.png"));
        title.setMinimumSize(new Dimension(350, 105));
        title.setOpaque(false);
        title.setBorder(new EmptyBorder(20, 13, 10, 0));

        // #---------------------------------------------------------------------------------------------
        // # Ubicacion barcos

        Dimension botonSize = new Dimension(220, 60);

        JButton barcoTipoPortavion = new JButton(Integer.toString(batallaNaval.getCantidadBarco(TipoBarco.Portavion)));
        barcoTipoPortavion.setPreferredSize(botonSize);
        barcoTipoPortavion.setIcon(getImage("portavion-jb.png"));
        barcoTipoPortavion.setBorderPainted(false);
        barcoTipoPortavion.setBorder(new EmptyBorder(0, 0, 0, 0));
        barcoTipoPortavion.setFont(Russo.deriveFont(16f));

        JButton barcoTipoSubmarino = new JButton(Integer.toString(batallaNaval.getCantidadBarco(TipoBarco.Submarino)));
        barcoTipoSubmarino.setPreferredSize(botonSize);
        barcoTipoSubmarino.setIcon(getImage("submarino-jb.png"));
        barcoTipoSubmarino.setBorderPainted(false);
        barcoTipoSubmarino.setBorder(new EmptyBorder(0, 0, 0, 0));
        barcoTipoSubmarino.setFont(Russo.deriveFont(16f));

        JButton barcoTipoDestructor = new JButton(
                Integer.toString(batallaNaval.getCantidadBarco(TipoBarco.Destructor)));
        barcoTipoDestructor.setPreferredSize(botonSize);
        barcoTipoDestructor.setIcon(getImage("destructor-jb.png"));
        barcoTipoDestructor.setBorderPainted(false);
        barcoTipoDestructor.setBorder(new EmptyBorder(0, 0, 0, 0));
        barcoTipoDestructor.setFont(Russo.deriveFont(16f));

        JButton barcoTipoFragata = new JButton(Integer.toString(batallaNaval.getCantidadBarco(TipoBarco.Fragata)));
        barcoTipoFragata.setPreferredSize(botonSize);
        barcoTipoFragata.setIcon(getImage("fragata-jb.png"));
        barcoTipoFragata.setBorderPainted(false);
        barcoTipoFragata.setBorder(new EmptyBorder(0, 0, 0, 0));
        barcoTipoFragata.setFont(Russo.deriveFont(16f));

        barcosParaUbicar = new HashMap<JButton, TipoBarco>();
        barcosParaUbicar.put(barcoTipoPortavion, TipoBarco.Portavion);
        barcosParaUbicar.put(barcoTipoSubmarino, TipoBarco.Submarino);
        barcosParaUbicar.put(barcoTipoDestructor, TipoBarco.Destructor);
        barcosParaUbicar.put(barcoTipoFragata, TipoBarco.Fragata);

        for (JButton barco : barcosParaUbicar.keySet())
            barco.addActionListener(listener);

        instrucciones = new JTextPane();

        // #---------------------------------------------------------------------------------------------
        // # Juego
        play = new JButton("Play");
        play.addActionListener(listener);
        play.setFont(Russo.deriveFont(16f));

        mostrarAI = new JButton("Mostrar AI");
        mostrarAI.addActionListener(listener);
        mostrarAI.setSize(new Dimension(20, 20));
        mostrarAI.setFont(Russo.deriveFont(16f));

        botonReiniciar = new JButton("Reiniciar Juego");
        botonReiniciar.addActionListener(listener);
        botonReiniciar.setFont(Russo.deriveFont(16f));

        /*loading = false;
        initContentPane();
        initGUI_ubicarBarcos();*/
    }

    // # -----------------------------------------
    /**
     * Inits the GUI ubicar barcos.
     */
    // # initGUI ubicacion barcos
    private void initGUI_ubicarBarcos() {
        add(mapaJugador);

        instrucciones.setPreferredSize(new Dimension(370, 180));
        instrucciones.setText("Despliega tu flota\n"
                + "\nEmpieza seleccionando un barco de tu flota\n y haz click en el mapa para ubicarlo. "
                + "\nEn cualquier momento puedes cambiar de barco o reubicarlo. "
                + "\nPodrás rotar el barco con click derecho, y una vez hayas determinado la ubicacion definitiva, fijalo en el mapa con doble click.");
        instrucciones.setFont(Russo.deriveFont(15f));

        // centrar el texto de las instrucciones
        StyledDocument doc = instrucciones.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        instrucciones.setBackground(new Color(228, 224, 213));
        instrucciones.setBorder(BorderFactory.createLineBorder(new Color(109, 102, 96), 5));
        instrucciones.setEditable(false);
        instrucciones.setHighlighter(null);

        panelEstado.add(title);
        for (JButton barco : barcosParaUbicar.keySet()) {
            panelEstado.add(barco);
            if (batallaNaval.getCantidadBarco(barcosParaUbicar.get(barco)) <= 0) {
                barco.setEnabled(false);
            } else
                barco.setEnabled(true);
        }
        panelEstado.add(instrucciones);

        add(panelEstado);

        repaint();
        revalidate();

        turno.setDelay(1000);
        turno.setInitialDelay(1000);
    }

    /**
     * Actualiza la cantidad de barcos disponibles de un tipo especificado.
     * @param tipo tipo del barco
     */
    public void actualizarBotones(TipoBarco tipo) {
        for (JButton barco : barcosParaUbicar.keySet()) {
            if (barcosParaUbicar.get(barco) == tipo) {
                if (batallaNaval.getCantidadBarco(barcosParaUbicar.get(barco)) <= 0)
                    barco.setEnabled(false);

                barco.setText(Integer.toString(batallaNaval.getCantidadBarco(barcosParaUbicar.get(barco))));
            }
        }
    }

    // #---------------------------------------------------------------------------
    // # INICIAR JUEGO
    // #---------------------------------------------------------------------------

    /**
     * Acomoda la interfaz grafica para proceder a iniciar el juego.
     */
    public void iniciarJuego() {
        remove(panelEstado);
        remove(mapaJugador);
        add(mapaAI);
        mapaAI.activarBotones(false);

        for (JButton barco : barcosParaUbicar.keySet())
            panelEstado.remove(barco);

        instrucciones.setText("Presiona Play para comenzar");
        instrucciones.setPreferredSize(new Dimension(350, 50));
        panelEstado.add(play);
        panelEstado.add(mostrarAI);

        panelEstado.add(Box.createVerticalStrut(39));

        mapaJugador.activarBotones(false);
        mapaJugador.quitarEfecto();
        mapaJugador.reducirMapa(true);
        mapaJugador.setImagenesMin(); //poner las imagenes de los barcos reducidas.
        panelEstado.add(mapaJugador);
        add(panelEstado);

        revalidate();
        repaint();
    }

    /**
     * Modifica la interfaz para iniciar el juego.
     */
    private void play() {
        batallaNaval.play();

        instrucciones.setText("Es tu turno");
        mapaAI.activarBotones(true);
        mapaJugador.activarBotones(false);

        panelEstado.remove(play);
        panelEstado.remove(mapaJugador);
        panelEstado.add(botonReiniciar);
        panelEstado.add(mapaJugador);
        revalidate();
        repaint();
    }

    // #---------------------------------------------------------------------------
    // # CAMBIO DE TURNO
    // #---------------------------------------------------------------------------

    // TURNO

    /**
     * Controla el cambio de turno entre los jugadores.
     */
    public void siguienteTurno() {
        if (determinarJuego()) // se termina la partida
            return;

        mapaAI.activarBotones(false);

        instrucciones.setText("El computador está pensando \nsu movimiento...");
        panelEstado.revalidate();
        panelEstado.repaint();

        contador = TIEMPO_DE_ESPERA;
        turno.start();
    }

    /**
     * Realiza el disparo del computador y lo muestra en el mapa del jugador.
     */
    private void dispararAI() {
        Map<Point, EstadoCasilla> resultado = batallaNaval.disparar(BatallaNavalGame.Jugador.human);
        mapaJugador.dibujarDisparo(resultado, true);

        if (determinarJuego()) // se termina la partida
            return;

        instrucciones.setText("Es tu turno");
        mapaAI.activarBotones(true);
        revalidate();
        repaint();
    }

    // #---------------------------------------------------------------------------
    // # REINICIAR Y DETERMINAR JUEGO
    // #---------------------------------------------------------------------------

    /**
     * Reinicia la vista, los mapas de los jugadores y el control.
     */
    private void reiniciar() {

        panelEstado.removeAll();

        batallaNaval.reiniciar();
        mapaAI.reiniciar();
        mapaJugador.reiniciar();

        initGUI_ubicarBarcos();

        this.remove(mapaAI);
        this.revalidate();
        this.repaint();
    }

    /**
     * Determina si el jugador gana o pierde y muestra el mensaje correspondiente.
     * @return true si hay un ganador.
     */
    private boolean determinarJuego() {
        BatallaNavalGame.Jugador ganador = batallaNaval.determinarJuego();
        if (ganador != null) {
            if (ganador == BatallaNavalGame.Jugador.human)
                instrucciones.setText("¡Ganaste!");
            else
                instrucciones.setText("Perdiste D:");

            mapaAI.activarBotones(false);
            mapaJugador.activarBotones(false);

            return true;
        }
        return false;
    }

    /**
     * Muestra u oculta la flota del computador.
     */
    private void mostrarAI() {
        if (mostrarAI.getText() == "Mostrar AI") {
            Set<Barco> barcos = new HashSet<Barco>(batallaNaval.getMap(BatallaNavalGame.Jugador.AI).values());
            mapaAI.mostrarBarco(barcos);
            mostrarAI.setText("Ocultar AI");
        } else {
            mapaAI.ocultarBarcos(new ArrayList(batallaNaval.getMap(BatallaNavalGame.Jugador.AI).keySet()));
            mostrarAI.setText("Mostrar AI");
        }
    }

    // #---------------------------------------------------------------------------
    // # FUNCIONES AUXILIARES
    // #---------------------------------------------------------------------------

    /**
     * Crea un ImageIcon cargando la imagen especificada. 
     * @param name el nombre de la imagen.
     * @return un ImageIcon
     */
    private ImageIcon getImage(String name) {
        return new ImageIcon(this.getClass().getResource("/images/" + name));
    }

    // #---------------------------------------------------------------------------
    // # LISTENER
    // #---------------------------------------------------------------------------

    /**
     * The Class Escucha. Clase interna encargada de manejar los eventos de la ventana.
     */
    private class Escucha implements ActionListener {

        /**
         * Action performed.
         *
         * @param event the event
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == turno) {

                if (loading) {
                    loading = false;
                    initContentPane();
                    initGUI_ubicarBarcos();
                    turno.stop();
                } else if (contador == 0) {
                    turno.stop();
                    dispararAI();
                } else
                    contador--;
            }

            if (!batallaNaval.haEmpezado()) {
                for (JButton barco : barcosParaUbicar.keySet())
                    if (event.getSource() == barco)
                        mapaJugador.setBarcoSeleccionado(barcosParaUbicar.get(barco));
            }

            if (event.getSource() == rotar)
                mapaJugador.rotarBarco();

            if (event.getSource() == confirmar)
                mapaJugador.confirmarBarco();

            if (event.getSource() == mostrarAI)
                mostrarAI();

            if (event.getSource() == play)
                play();

            if (event.getSource() == botonReiniciar && !turno.isRunning())
                reiniciar();
        }
    }
}