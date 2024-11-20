package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class Chat implements Runnable {
	private String nombre;
	private static int contador;
	private List<Socket> arraySockets;
	
	public Chat(List<Socket> arraySocket) {
		this.arraySockets = arraySocket;
		Thread hilo = new Thread(this, nombre);
		hilo.start();
	}

	@Override
	public void run() {
		
		
		
		
	}
	public String recogerMensaje(String mensaje) {
		arraySockets.forEach(socket -> {

			  try  {
				  InputStreamReader entrada = new InputStreamReader(socket.getInputStream());
	                BufferedReader entradaBuffer = new BufferedReader(entrada);
	                entradaBuffer.read();
				  
				  
				  
				  
			  }catch(Exception e) {
					  
				  }
			  
		});
	}
	public void enviarMensajeATodos(String mensaje) {
		arraySockets.forEach(socket -> {

			  try  {
				  PrintStream salida = new PrintStream(socket.getOutputStream());
				  InputStreamReader entrada = new InputStreamReader(socket.getInputStream());
	                BufferedReader entradaBuffer = new BufferedReader(entrada);
	                
				  
				  
				  
				  
			  }catch(Exception e) {
					  
				  }
			  
		});
		
	}

}
