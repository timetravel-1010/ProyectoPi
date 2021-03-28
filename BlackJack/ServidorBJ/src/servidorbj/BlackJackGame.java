/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package servidorbj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comunes.*;

/* Clase encargada de realizar la gestion del juego, esto es, el manejo de turnos y estado del juego.
 * Tambien gestiona al jugador Dealer.
 * El Dealer tiene una regla de funcionamiento definida:
 * Pide carta con 16 o menos y Planta con 17 o mas.
 */
public class BlackJackGame implements Runnable {
	public static final int PUERTO = 7377;
	public static final String IP = "127.0.0.1";
	public static final int LDJ = 3; // Cantidad de Jugadores

	private ServerSocket server;
	private Socket conexionJugador;
	private ExecutorService manejadorHilos;

	private Jugador[] jugadores;
	private String[] nombre;
	private int[] apuesta;
	private int[] dinero;
	private estadoJugador[] estadoJugadores;
	private List<Carta>[] mano;
	private Baraja mazo;
	private List<Carta> manoDealer;
	private BJGameState datosEnviar;
	private GameStates gameState;

	/**
	 * Instantiates a new BlackJackGame.
	 */
	public BlackJackGame() {
		inicializarJuego();
		inicializarHilos();

		// Crear el servidor
		try {
			System.out.println("Iniciando el servidor...");
			server = new ServerSocket(PUERTO, LDJ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicia el hilo del servidor y ejecuta los metodos en su interior.
	 */
	@Override
	public void run() {
		try {
			esperarJugadores();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa los atributos de la logica del juego.
	 */
	private void inicializarJuego() {
		nombre = new String[LDJ];
		apuesta = new int[LDJ];
		dinero = new int[LDJ];
		estadoJugadores = new estadoJugador[LDJ];
		mazo = new Baraja();
		mano = new ArrayList[LDJ];

		for (int i = 0; i < LDJ; i++)
			mano[i] = new ArrayList<Carta>();
		manoDealer = new ArrayList<Carta>();
	}

	/**
	 * Inicializa el manejador de hilos y el array de jugadores.
	 */
	private void inicializarHilos() {
		manejadorHilos = Executors.newFixedThreadPool(LDJ);
		jugadores = new Jugador[LDJ];
	}

	// #---------------------------------------------------------------------------
	// # Game
	// #---------------------------------------------------------------------------

	/**
	 * Establece la conexion entre los clientes y el servidor por medio del hilo de cada jugador.
	 * e inicia el juego.
	 * @throws InterruptedException
	 */
	private void esperarJugadores() throws InterruptedException {
		System.out.println("Esperando a los jugadores...");
		gameState = GameStates.esperandoJugadores;

		for (int i = 0; i < LDJ; i++) {
			try {
				System.out.println("Esperando al jugador " + i + "...");
				conexionJugador = server.accept();
				jugadores[i] = new Jugador(conexionJugador, i, this);
				manejadorHilos.execute(jugadores[i]);
				updateGameStatus();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Todos los jugadores se han conectado.");

		for (int i = 0; i < LDJ; i++)
			while (jugadores[i].getNombre() == null)
				Thread.sleep(100);

		while (true) {
			apuestaInicial();
			repartoInicial();
			iniciarRonda();

			gameState = GameStates.nuevaRonda;
			updateGameStatus();
			Thread.sleep(11000);
			// Preparar el inicio de una nueva ronda.
			for (int i = 0; i < LDJ; i++) {
				mazo.devolverCartas(mano[i]);
				mano[i].clear();
				estadoJugadores[i] = null;
			}
			mazo.devolverCartas(manoDealer);
			manoDealer.clear();
		}
	}

	/**
	 * Establece el nombre de un jugador, su apuesta inicial y actualiza el estado de juego.
	 * @param name el nombre del jugador en la posicion i.
	 * @param i entero que indica la posicion del jugador.
	 */
	public void establecerNombre(String name, int i) {
		nombre[i] = name;
		dinero[i] = 100;
		updateGameStatus();
	}

	/**
	 * Realiza la apuesta fija inicial de $10 de cada jugador.
	 * @throws InterruptedException
	 */
	private void apuestaInicial() throws InterruptedException {
		gameState = GameStates.apuestaInicial;
		updateGameStatus();
		Thread.sleep(2500);

		gameState = GameStates.seRecibenApuestas;
		updateGameStatus();
		Thread.sleep(1000);

		for (int i = 0; i < LDJ; i++) {
			gameState = GameStates.seRecibenApuestas;
			if (dinero[i] < 10) { // El casino le da dinero para que siga jugando.
				dinero[i] += 50;
				gameState = GameStates.prestamoAJugador;
				updateGameStatus(i);
				Thread.sleep(2000);
			}
			dinero[i] -= 10;
			apuesta[i] = 10;
			updateGameStatus();
			Thread.sleep(1000);
		}
	}

	/**
	 * Realiza el reparto inicial y actualiza el estado del juego.
	 * @throws InterruptedException
	 */
	private void repartoInicial() throws InterruptedException {
		gameState = GameStates.repartoDealer;
		updateGameStatus();
		Thread.sleep(1000);
		System.out.println("El Dealer realiza el reparto...");
		for (int j = 1; j <= 2; j++) {
			for (int k = 0; k < LDJ; k++) {
				mano[k].add(mazo.getCarta());
				updateGameStatus();
				Thread.sleep(1000);
			}
			if (j == 1) {
				manoDealer.add(mazo.getCarta());
				updateGameStatus();
				Thread.sleep(1000);
			}
		}
	}

	/**
	 * Inicia la ronda, controla la secuencia de turnos y actualiza el estado de la vista de cada jugador.
	 * @throws InterruptedException
	 */
	private void iniciarRonda() throws InterruptedException {
		ClientRequest request = null;
		gameState = GameStates.jugadorEnTurno;

		System.out.println("Inicia la ronda...");

		for (int i = 0; i < LDJ; i++) {
			updateGameStatus(i);
			do {
				if (jugadores[i] != null)
					request = jugadores[i].getRequest();

				if (request.request == requests.pedirCarta) {
					mano[i].add(mazo.getCarta());
					System.out.println("Se ha enviado carta al jugador " + nombre[i]);
				}
				updateGameStatus(i);
				Thread.sleep(1000);
			} while (calcularValorMano(mano[i]) <= 21 && request.request != requests.plantar);

			if (request.request != requests.plantar) { // Se le retiran las cartas al jugador y pierde su apuesta.
				mazo.devolverCartas(mano[i]);
				estadoJugadores[i] = estadoJugador.volo;
				apuesta[i] = 0;
				updateGameStatus(i);
				System.out.println("El jugador " + nombre[i] + " ha volado!");
			}
		}
		dealerGame();
		determinarJuego();
	}

	/**
	 * Simula la toma de decision del Dealer.
	 * @throws InterruptedException
	 */
	private void dealerGame() throws InterruptedException {
		gameState = GameStates.dealerJuega;
		updateGameStatus();
		Thread.sleep(1000);
		System.out.println("Inicia el Dealer...");
		do {
			manoDealer.add(mazo.getCarta());
			updateGameStatus();
			Thread.sleep(1000);
		} while (calcularValorMano(manoDealer) <= 16);
	}

	/**
	 * Determina si cada jugador ha ganado, perdido o empatado contra el Dealer y controla las apuestas.
	 * @throws InterruptedException
	 */
	private void determinarJuego() throws InterruptedException {
		int valorManoJugador;
		int valorManoDealer = calcularValorMano(manoDealer);

		System.out.println("Se va a determinar el juego.");
		gameState = GameStates.determinarJuego;
		updateGameStatus();
		Thread.sleep(3000);

		gameState = GameStates.elegirGanador;

		for (int i = 0; i < LDJ; i++) {
			if (estadoJugadores[i] == estadoJugador.volo)
				continue;

			valorManoJugador = calcularValorManoAs(mano[i]);

			System.out.println(nombre[i] + " tiene: " + valorManoJugador);
			System.out.println("El Dealer tiene " + valorManoDealer);

			if (valorManoDealer > 21) { // El jugador gana.
				estadoJugadores[i] = estadoJugador.ganador;
				// Condicion para el blackjack
				apuesta[i] += mano[i].size() == 2 && valorManoJugador == 21 ? (apuesta[i] * 3 / 2) : apuesta[i];
				System.out.println(nombre[i] + " ha ganado!");
			} else if (valorManoDealer == valorManoJugador) { // Empate.
				estadoJugadores[i] = estadoJugador.empate;
				System.out.println("Ha empatado con el Dealer.");
			} else {
				if (valorManoJugador > valorManoDealer) { // El jugador gana.
					estadoJugadores[i] = estadoJugador.ganador;
					// Condicion para el blackjack
					apuesta[i] += mano[i].size() == 2 && valorManoJugador == 21 ? (apuesta[i] * 3 / 2) : apuesta[i];
					System.out.println(nombre[i] + " ha ganado!");
				} else { // El jugador pierde.
					estadoJugadores[i] = estadoJugador.perdedor;
					apuesta[i] = 0;
					System.out.println(nombre[i] + " ha perdido.");
				}
			}
			dinero[i] += apuesta[i];
			apuesta[i] = 0;
		}
		updateGameStatus();
		Thread.sleep(2000);
	}

	// #---------------------------------------------------------------------------
	// # Funciones auxiliares
	// #---------------------------------------------------------------------------

	/**
	 * Actualiza el estado de juego en la vista de cada jugador 
	 * teniendo en cuenta el jugador que tiene le turno.
	 * @param turnoJugador la posicion del jugador en turno.
	 */
	private void updateGameStatus(int turnoJugador) {
		datosEnviar = new BJGameState(turnoJugador, nombre, manoDealer, dinero, gameState, mano, estadoJugadores);

		for (int i = 0; i < LDJ; i++)
			if (jugadores[i] != null)
				jugadores[i].enviarMensajeCliente(datosEnviar);
	}

	/**
	 * Actualiza el estado de juego en la vista de cada jugador.
	 */
	private void updateGameStatus() {
		updateGameStatus(-1);
	}

	/**
	 * Calcula el valor de la mano de un jugador teniendo en cuenta que el As
	 * puede valer 11 o 1 a conveniencia del jugador.
	 * @param mano lista de cartas para calcular el valor.
	 * @return el valor de la mano ingresada.
	 */
	private int calcularValorManoAs(List<Carta> mano) {
		int valorMano = 0;
		int tieneAs = 0;

		for (Carta carta : mano)
			if (carta.getValor() == 1)
				tieneAs++;
			else
				valorMano += carta.getValor();

		for (int i = 0; i < tieneAs; i++)
			valorMano += (valorMano + 11) > 21 ? 1 : 11;

		return valorMano;
	}

	/**
	 * Calcula el valor de la mano de un jugador contando siempre el As como 1.
	 * @param mano lista de cartas para calcular el valor.
	 * @return el valor de la mano ingresada.
	 */
	private int calcularValorMano(List<Carta> mano) {
		int valorMano = 0;

		for (Carta carta : mano)
			valorMano += carta.getValor();

		return valorMano;
	}

	/**
	 * Vuelve a null el jugador de la posicion i.
	 * @param i posicion del jugador a convertir a null.
	 */
	public void quitarJugador(int i) {
		jugadores[i] = null;
	}

	//# ------------------------ Main ----------------------------

	/**
	 * The main method.
	 * @param args the arguments.
	 */
	public static void main(String[] args) {

		BlackJackGame servidor = new BlackJackGame();
		Thread miHilo = new Thread(servidor);
		miHilo.start();
	}
}
