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


    private static final Map<Thread, Socket> arraySockets = new ConcurrentHashMap<>();
    

    private static final Map<Thread, String> nombresUsuarios = new ConcurrentHashMap<>();

    private Socket socket;

    public Chat(Map<Thread, Socket> arraySockets, Socket socketAlCliente) {
        this.socket = socketAlCliente;
        this.nombre = "Usuario " + (++contador); 
    }

    @Override
    public void run() {
        recogerUsuario();
        imprimirUsuariosActivos();
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


            arraySockets.put(Thread.currentThread(), this.socket);
            nombresUsuarios.put(Thread.currentThread(), this.nombre);

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
                    enviarMensajeATodos("** " + nombre + " ha salido **");
                    cerrarConexion();
                    break;
                } else {
                    enviarMensajeATodos(nombre + ": " + mensaje); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el mensaje del usuario " + nombre + ": " + e.getMessage());
        }
    }

    public void enviarMensajeATodos(String mensaje) {
  
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
            arraySockets.remove(Thread.currentThread());
            nombresUsuarios.remove(Thread.currentThread()); 
            System.out.println("Conexión cerrada para el usuario " + nombre);
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión para el usuario " + nombre + ": " + e.getMessage());
        }
    }

    public static void imprimirUsuariosActivos() {
        System.out.println("Usuarios activos:");


        arraySockets.forEach((thread, socket) -> {
            try {
                PrintStream salida = new PrintStream(socket.getOutputStream());
                

                salida.println("Usuarios activos:");

                nombresUsuarios.forEach((otherThread, otherNombre) -> {
                    salida.println("- " + otherNombre);
                });

            } catch (IOException e) {
                System.err.println("Error al enviar la lista de usuarios activos a " + thread.getName() + ": " + e.getMessage());
            }
        });
    }
}