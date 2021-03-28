/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package comunes;

/**
 * Enum utilizado para almacenar los estados del juego y el correspondiente mensaje 
 * para mostrar en pantalla.
 */
public enum GameStates {
    esperandoJugadores("Esperando a otros jugadores..."), apuestaInicial("Apuesta inicial de $10"),
    seRecibenApuestas("Se reciben sus apuestas"), repartoDealer("El Dealer hace el reparto inicial"),
    jugadorEnTurno(""), dealerJuega("Es el turno del Dealer"), setId(""),
    determinarJuego("Se va a determinar el juego"), elegirGanador(""), nuevaRonda("La nueva ronda inicia en "),
    prestamoAJugador("El casino te presta $50");

    public final String mensaje;

    private GameStates(String mensaje) {
        this.mensaje = mensaje;
    }
}