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

	@Override
	public void run() {
        while (activo) {
        	try {
   	         String serverMessage;
   	         InputStreamReader entrada = new InputStreamReader(socket.getInputStream());
   	         BufferedReader entradaBuffer = new BufferedReader(entrada);
             while (!Thread.currentThread().isInterrupted()) {
                 if ((serverMessage = entradaBuffer.readLine()) != null) {
                     System.out.println(serverMessage);
                 } else {
                     break; 
                 }
             }
             detener();
        	}catch(Exception e) {
        		
        	}
        }
        
		
	}
    public void detener() {
        activo = false;  
    }

}
