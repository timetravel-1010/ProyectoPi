/*
 * Programacion Interactiva
 * Mini proyecto 4: Juego de Blackjack.
 */
package servidorbj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import comunes.ClientRequest;
import comunes.requests;

/**
 * The Class Jugador. Clase para gestionar la comunicacion con cada cliente que se conecte.
 */
public class Jugador implements Runnable {
    //varibles para gestionar la comunicacion con el cliente (Jugador) conectado.
    private Socket conexionCliente;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientRequest request;

    //variables de control.
    private BlackJackGame server;
    private int id;
    private String nombre;
    private boolean ejecutar = true;

    /**
     * Instantiates a new Jugador.
     */
    public Jugador(Socket conexionCliente, int indexJugador, BlackJackGame server) {
        this.conexionCliente = conexionCliente;
        this.id = indexJugador;
        this.nombre = null;
        this.server = server;

        try {
            out = new ObjectOutputStream(conexionCliente.getOutputStream());
            out.flush();
            in = new ObjectInputStream(conexionCliente.getInputStream());
            out.writeObject(id);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia el hilo para establecer la comunicacion entre el servidor y el cliente.
     */
    @Override
    public void run() {
        while (ejecutar) {
            try {
                request = (ClientRequest) in.readObject();
                if (request.request == requests.setNombre) {
                    nombre = request.nombre;
                    server.establecerNombre(nombre, id);
                } else if (request.request == requests.close)
                    cerrarConexion();
                else
                    synchronized (server) {
                        server.notify();
                    }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Despierta el hilo del servidor y retorna la solicitud del cliente.
     * @return la solicitud del cliente.
     * @throws InterruptedException
     */
    public ClientRequest getRequest() throws InterruptedException {
        synchronized (server) {
            server.wait();
        }
        return request;
    }

    /**
     * @return El String del nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Envia un objeto al cliente.
     * @param mensaje el objeto generico para enviar.
     */
    public void enviarMensajeCliente(Object mensaje) {
        try {
            out.reset();
            out.writeObject(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra los flujos de E/S y el socket.
     * @throws IOException
     */
    private void cerrarConexion() throws IOException {
        enviarMensajeCliente(null);
        out.close();
        in.close();
        conexionCliente.close();
        this.ejecutar = false;
        server.quitarJugador(id);
        System.out.println("¡El jugador " + this.nombre + " se ha desconectado!");
    }
}
