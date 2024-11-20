package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class SocketServidor {
	  public static final int PUERTO = 4951;
	public static void main(String[] args) {
		
		
		InetSocketAddress direccion = new InetSocketAddress(PUERTO);
		Socket socketAlCliente = null;
		
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(direccion);
            System.out.println("Esperando Peticiones");
            List<Socket> arraySocket = new ArrayList<Socket>();
            while(true) {

        
                socketAlCliente = serverSocket.accept();
                System.out.println("Jugador conectado, esperando al segundo...");
                arraySocket.add(socketAlCliente);


            new Thread(new Chat(arraySocket)).start();
            }
        } catch (IOException io) {
            System.err.println("Error en el servidor: " + io.getMessage());
        }
		}

}