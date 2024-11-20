package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SocketServidor {
	  public static final int PUERTO = 4951;
	public static void main(String[] args) {
		
		
		InetSocketAddress direccion = new InetSocketAddress(PUERTO);
		Socket socketAlCliente = null;
		
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(direccion);
            System.out.println("Esperando Peticiones");
            Map<Thread, Socket> arraySockets = new HashMap<>();
            while(true) {

        
                socketAlCliente = serverSocket.accept();
                System.out.println("Conectado");
                arraySockets.put(Thread.currentThread() ,socketAlCliente);


            new Thread(new Chat(arraySockets, socketAlCliente)).start();
            }
        } catch (IOException io) {
            System.err.println("Error en el servidor: " + io.getMessage());
        }
		}

}