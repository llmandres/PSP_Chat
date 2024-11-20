package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class recibirMensajes implements Runnable{
	
	private  boolean activo = true;
	Socket socket;
	
	public recibirMensajes(Socket socket) {
		this.socket = socket;
	}

    public void detener() {
        activo = false;  

    }

    @Override
    public void run() {
  
        try (InputStreamReader entrada = new InputStreamReader(socket.getInputStream());
             BufferedReader entradaBuffer = new BufferedReader(entrada)) {

            String serverMessage;

            while (!Thread.currentThread().isInterrupted()) {
                if ((serverMessage = entradaBuffer.readLine()) != null) {
                    System.out.println(serverMessage); 
                } else {
                    break; 
                }
            }
        } catch (IOException e) {
            if (!Thread.currentThread().isInterrupted()) {

                System.err.println("CLIENTE: Error al recibir mensaje: " + e.getMessage());
            }
        } 
    }
}