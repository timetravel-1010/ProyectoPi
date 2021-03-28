/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package clientebj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.OverlayLayout;

import comunes.BJGameState;
import comunes.ClientRequest;
import comunes.GameStates;
import comunes.estadoJugador;
import comunes.requests;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The Class BlackJackView. Clase que modela el cliente, controla la
 * comunicacion entre el cliente y el servidor y muestra la ventana de juego.
 */
public class BlackJackView extends JFrame implements Runnable, ActionListener {
	public static final int PUERTO = 7377;
	public static final String IP = "127.0.0.1";
	private Socket client;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private int id;
	private List<JManoPanel> jugadores;
	private JPanel overlay;
	private JDealerPanel dealer;
	private JTextField nombre;
	private JLabel notificaciones;
	private JButton pedirCarta, plantar;
	private boolean showSplash;
	private Image splash;

	private int s = 10;
	private String mensajeNuevaRonda;
	private Timer timer;
	private boolean ejecutar = true;

	/**
	 * Instantiates a new BlackJackView.
	 */
	public BlackJackView() {
		Resources.loadFonts(this);
		splash = Resources.getImage("splash.png").getImage();
		showSplash = true;

		this.setTitle("BlackJack");
		this.setIconImage(Resources.getImage("icon.png").getImage());
		this.setSize(1150 + 16, 660 + 39);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new closeWindowEvent());

		initGUI();
		iniciarCliente();
	}

	/**
	 * Muestra una imagen de fondo al inicio del juego.
	 * @param g 
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (showSplash)
			g.drawImage(splash, 8, 31, 1150, 660, this);
	}

	/**
	 * Inits the GUI.
	 */
	private void initGUI() {
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		gamePanel.setOpaque(false);

		overlay = new JPanel();
		overlay.setLayout(new OverlayLayout(overlay));
		overlay.setOpaque(false);

		timer = new Timer(1000, this);

		nombre = new JTextField(10);
		nombre.setFont(Resources.RobotoBold.deriveFont(14f));

		jugadores = new ArrayList<JManoPanel>();

		for (int i = 0; i < 3; i++)
			jugadores.add(new JManoPanel(i + 1));

		dealer = new JDealerPanel();

		JPanel northPanel = new JPanel();
		northPanel.setOpaque(false);
		northPanel.add(new JLabel(Resources.getImage("cartas.png")));
		northPanel.add(Box.createRigidArea(new Dimension(120, 0)));
		northPanel.add(dealer);
		northPanel.add(Box.createRigidArea(new Dimension(120, 0)));
		northPanel.add(new JLabel(Resources.getImage("fichas.png")));

		notificaciones = new JLabel("Esperado a los otros jugadores", SwingConstants.CENTER);
		notificaciones.setFont(Resources.HelveticaNeue.deriveFont(28f));
		notificaciones.setForeground(Color.WHITE);
		notificaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
		notificaciones.setOpaque(false);
		Resources.setJLabelSize(notificaciones, new Dimension(350, 70));

		pedirCarta = new JButton();
		pedirCarta.setContentAreaFilled(false);
		pedirCarta.setFocusPainted(false);
		pedirCarta.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
		pedirCarta.setIcon(Resources.getImage("pc.png"));
		pedirCarta.setRolloverIcon(Resources.getImage("pc-roll.png"));
		pedirCarta.setPressedIcon(Resources.getImage("pc.png"));
		pedirCarta.addActionListener(this);

		plantar = new JButton();
		plantar.setContentAreaFilled(false);
		plantar.setFocusPainted(false);
		plantar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
		plantar.setIcon(Resources.getImage("plantar.png"));
		plantar.setRolloverIcon(Resources.getImage("plantar-roll.png"));
		plantar.setPressedIcon(Resources.getImage("plantar.png"));
		plantar.addActionListener(this);

		activarBotones(false);

		gamePanel.add(northPanel);
		gamePanel.add(Box.createRigidArea(new Dimension(10, 10)));
		gamePanel.add(new GameTable(jugadores, notificaciones, pedirCarta, plantar), BorderLayout.CENTER);

		overlay.add(new JLoginPanel(nombre, this), BorderLayout.CENTER);
		overlay.add(gamePanel, BorderLayout.CENTER);
	}

	/**
	* Inits the content pane.
	*/
	private void initContentPane() {
		Image background = Resources.getImage("background-2.png").getImage();
		JPanel contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
			}
		};
		contentPane.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
		contentPane.setLayout(new BorderLayout());
		contentPane.add(overlay);
		this.setContentPane(contentPane);
	}

	// #---------------------------------------------------------------------------
	// # Funciones auxiliares
	// #---------------------------------------------------------------------------

	/**
	 * Ejecuta el hilo que manejará el socket del cliente
	 */
	public void iniciarCliente() {
		ExecutorService hiloCliente = Executors.newFixedThreadPool(1);
		hiloCliente.execute(this);
	}

	/**
	 * Envia una solicitud al servidor.
	 * @param mensaje solicitud del cliente.
	 */
	public void enviarMensajeServidor(ClientRequest mensaje) {
		try {
			out.writeObject(mensaje);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Busca y establece la comunicacion con el servidor.
	 * @throws InterruptedException
	 */
	public void buscarServidor() throws InterruptedException {
		System.out.println("Jugador buscando al servidor...");

		while (true) {
			try {
				client = new Socket(IP, PUERTO);
				out = new ObjectOutputStream(client.getOutputStream());
				out.flush();
				in = new ObjectInputStream(client.getInputStream());
				break;
			} catch (Exception e) {
				Thread.sleep(100);
			}
		}

		Thread.sleep(3000);
		showSplash = false;
		SwingUtilities.invokeLater(new initContentPaneEvent());

		System.out.println("Jugador conectado al servidor");

		// Procesar comunicacion con el Servidor.
		try {
			this.id = (int) in.readObject(); // recibe su id.
			BJGameState datos = (BJGameState) in.readObject();
			updateGame(datos); // adquirir el estado del juego en el momento de la conexion
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicia el hilo del cliente y recibe el objeto de tipo BJGameState para actualizar
	 * el estado del juego.
	 */
	@Override
	public void run() {
		try {
			buscarServidor();
		} catch (InterruptedException e) {
		}

		while (ejecutar) {
			try {
				BJGameState datosRecibidos = (BJGameState) in.readObject();
				if (datosRecibidos == null) {
					ejecutar = false;
					synchronized (this) {
						this.notifyAll();
					}
					continue;
				}
				System.out.println("Jugador hilo run ha recibido datos del servidor...");

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							updateGame(datosRecibidos);
							revalidate();
							repaint();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Actualiza el estado del juego.
	 * @param datos objeto de tipo BJGameStates que contiene la informacion necesaria
	 * para actualizar el estado del juego.
	 * @throws InterruptedException
	 */
	private void updateGame(BJGameState datos) throws InterruptedException {

		String mensaje = "";

		// Verificar si es el turno de este jugador.
		boolean flag = datos.turno == id && datos.estadoJuego == GameStates.jugadorEnTurno ? true : false;
		activarBotones(flag);

		// Establecer los nombres de los jugadores.
		if (datos.estadoJuego == GameStates.esperandoJugadores) {
			for (int i = 0; i < datos.nombres.length; i++)
				if (datos.nombres[i] != null) {
					jugadores.get(i).setNombre(datos.nombres[i]);
					jugadores.get(i).setDinero(Integer.toString(datos.dinero[i]));
				}
			mensaje = datos.estadoJuego.mensaje;
		} else if (datos.estadoJuego == GameStates.jugadorEnTurno && datos.turno >= 0)
			mensaje = flag ? "Es tu turno" : "Es el turno de " + datos.nombres[datos.turno];
		// Mostrar el estado de los jugadores una vez determinado el juego.
		else if (datos.estadoJuego == GameStates.elegirGanador) {
			// Mensaje en el panel de cada jugador.
			String msg;
			for (int i = 0; i < datos.nombres.length; i++) {
				if (datos.estadoJugadores[i] == estadoJugador.volo)
					continue;
				if (datos.estadoJugadores[i] == estadoJugador.ganador)
					msg = i == id ? "¡Ganaste!" : "¡Ha ganado!";
				else if (datos.estadoJugadores[i] == estadoJugador.perdedor)
					msg = i == id ? "¡Perdiste D:!" : "¡Ha Perdido!";
				else
					msg = i == id ? "¡Has empatado!" : "¡Ha empatado!";
				jugadores.get(i).mostrarMensaje(msg, datos.estadoJugadores[i]);
				jugadores.get(i).setDinero(Integer.toString(datos.dinero[i]));
			}
		}
		// Mostrar el mensaje de inicio de una nueva ronda. 
		else if (datos.estadoJuego == GameStates.nuevaRonda) {
			mensajeNuevaRonda = datos.estadoJuego.mensaje;
			timer.start();
		} else if (datos.estadoJuego == GameStates.prestamoAJugador)
			mensaje = datos.turno == id ? datos.estadoJuego.mensaje
					: "El casino le presta $50 a " + datos.nombres[datos.turno];
		else
			mensaje = datos.estadoJuego.mensaje;

		// Actualizar el mensaje del JLabel notificaciones.
		if (datos.estadoJuego != GameStates.nuevaRonda)
			notificaciones.setText(mensaje);

		// Mostrar las cartas y dinero.
		for (int i = 0; i < datos.manos.length; i++) {
			if (datos.nombres[i] != null) {
				jugadores.get(i).setDinero(Integer.toString(datos.dinero[i]));
				if (datos.estadoJugadores[i] != estadoJugador.volo)
					jugadores.get(i).mostrarCartas(datos.manos[i]);
			}
		}

		if (datos.nombres[id] != null)
			dealer.mostrarCartas(datos.manoDealer);

		// Actualizar la vista informando que un jugador ha volado.
		if (datos.turno >= 0 && datos.estadoJugadores[datos.turno] == estadoJugador.volo) {
			if (datos.turno == id) { // Es este jugador.
				activarBotones(false);
				jugadores.get(id).mostrarMensaje("¡Has volado!", datos.estadoJugadores[datos.turno]);
			} else
				jugadores.get(datos.turno).mostrarMensaje("¡Ha volado!", datos.estadoJugadores[datos.turno]);
		}
	}

	/**
	 * Cierra los flujos de E/S y el Socket.
	 */
	private void cerrarConexion() {
		try {
			enviarMensajeServidor(new ClientRequest(requests.close));
			synchronized (this) {
				this.wait();
			}

			in.close();
			out.close();
			client.close();
			System.out.println("I’m off!");
			System.exit(0);
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * Activa o desactiva los botones de acuerdo al booleano ingresado.
	 * @param flag true para activar los botones.
	 */
	private void activarBotones(boolean flag) {
		pedirCarta.setEnabled(flag);
		plantar.setEnabled(flag);
	}

	/**
	* Action performed.
	* @param event the event
	*/
	@Override
	public void actionPerformed(ActionEvent event) {

		if (!nombre.getText().equals("")) {
			overlay.remove(0);
			jugadores.get(id).setNombre(nombre.getText());

			ClientRequest envioNombre = new ClientRequest(requests.setNombre, nombre.getText());
			enviarMensajeServidor(envioNombre);
			System.out.println("Jugador ha enviado nombre " + nombre.getText());

			repaint();
			revalidate();
			nombre.setText("");
		}

		if (event.getSource() == pedirCarta) {
			ClientRequest request = new ClientRequest(requests.pedirCarta);
			enviarMensajeServidor(request);
		}

		if (event.getSource() == plantar) {
			ClientRequest request = new ClientRequest(requests.plantar);
			enviarMensajeServidor(request);
		}

		if (event.getSource() == timer) {
			notificaciones.setText(mensajeNuevaRonda + s);
			revalidate();
			repaint();
			s--;
			if (s == 0) {
				timer.stop();
				for (int j = 0; j < 3; j++)
					jugadores.get(j).reset();
				s = 10;
			}
		}
	}

	// Class closeWindowEvent. clase que maneja el evento windowClosing
	private class closeWindowEvent extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.out.println("Se va a cerrar la conexion.");
			cerrarConexion();
		}
	}

	// Class initContentPaneEvent. Permitirá el enlace entre el hilo y el hilo manejador de eventos
	private class initContentPaneEvent implements Runnable {
		@Override
		public void run() {
			initContentPane();
		}
	}
}
