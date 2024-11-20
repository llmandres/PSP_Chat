package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chat implements Runnable {
    private String nombre;
    private static int contador = 0;
    private static final Map<Thread, Socket> arraySockets = new ConcurrentHashMap<>();  // ConcurrentMap para seguridad
    private Socket socket;

    public Chat(Map<Thread, Socket> arraySockets, Socket socketAlCliente) {
        this.socket = socketAlCliente;
        this.nombre = "Usuario " + (++contador); // Nombre por defecto
    }

    @Override
    public void run() {
        recogerUsuario();
        enviarMensajeATodos("** " + this.nombre + " se ha unido al chat **");
        mensajeRecibidoyEnviar();
    }

    public void recogerUsuario() {
        try {
            InputStreamReader entrada = new InputStreamReader(this.socket.getInputStream());
            BufferedReader entradaBuffer = new BufferedReader(entrada);
            String nombreCliente = entradaBuffer.readLine();
            if (nombreCliente != null && !nombreCliente.trim().isEmpty()) {
                this.nombre = nombreCliente;
            } else {
                this.nombre = "Usuario" + contador;  
            }
            // Añadir socket a la lista de clientes conectados
            arraySockets.put(Thread.currentThread(), this.socket);
        } catch (IOException e) {
            System.err.println("Error al recoger el nombre del usuario: " + e.getMessage());
        }
    }

    public void mensajeRecibidoyEnviar() {
        try {
            InputStreamReader entrada = new InputStreamReader(socket.getInputStream());
            BufferedReader entradaBuffer = new BufferedReader(entrada);
            String mensaje;

            while ((mensaje = entradaBuffer.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("salir")) {
                    enviarMensajeATodos(nombre + " ha salido");
                    cerrarConexion();
                    break;
                } else {
                    enviarMensajeATodos(nombre + ": " + mensaje);  // Enviar mensaje a todos los conectados
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el mensaje del usuario " + nombre + ": " + e.getMessage());
        }
    }

    public void enviarMensajeATodos(String mensaje) {
        // Enviar mensaje a todos los usuarios conectados
        arraySockets.forEach((thread, socket) -> {
            try {
                PrintStream salida = new PrintStream(socket.getOutputStream());
                salida.println(mensaje);
            } catch (IOException e) {
                System.err.println("Error al enviar mensaje a " + thread.getName() + ": " + e.getMessage());
            }
        });
    }

    private void cerrarConexion() {
        try {
            socket.close();
            arraySockets.remove(Thread.currentThread());  // Eliminar el socket del cliente desconectado
            System.out.println("Conexión cerrada para el usuario " + nombre);
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión para el usuario " + nombre + ": " + e.getMessage());
        }
    }
}