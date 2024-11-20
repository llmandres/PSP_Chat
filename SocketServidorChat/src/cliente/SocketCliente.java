package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketCliente {
	
	public static final int PUERTO = 4951;
	public static final String IP_SERVER = "localhost";
	
	public static void main(String[] args) {
		System.out.println("        CHAT CLIENTE         ");
		System.out.println("-----------------------------------");
		
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
		try (Scanner sc = new Scanner(System.in)){
			System.out.println("Introduzca el nombre");
			String nombre =sc.nextLine();
			
			System.out.println("CLIENTE: Conectando con el servidor ....");
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);
			System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + 
					" por el puerto " + PUERTO);

			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader entradaBuffer = new BufferedReader(entrada);
			
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			 salida.println(nombre);
			 
			String texto = "";
			boolean continuar = true;
			do {
				System.out.println("CLIENTE: Escribe mensaje (FIN para terminar): ");
				texto = sc.nextLine();//frase que vamos a mandar para contar				
				
				salida.println(texto);
				System.out.println("CLIENTE: Esperando respuesta ...... ");				
				String respuesta = entradaBuffer.readLine();
				
				
				
				if("OK".equalsIgnoreCase(respuesta)) {
					continuar = false;
				}else {
					System.out.println(nombre + ":" + respuesta);
				}				
			}while(continuar);
			//Cerramos la conexion
			socketAlServidor.close();
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la direcci�n" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
		
		System.out.println("CLIENTE: Fin del programa");
	

	}

}
