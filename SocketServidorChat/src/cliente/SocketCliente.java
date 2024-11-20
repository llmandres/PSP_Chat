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
    public static final String IP_SERVER = "172.26.100.176";
    
    public static void main(String[] args) {
        System.out.println("        CHAT CLIENTE         ");
        System.out.println("-----------------------------------");
        
        InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("Introduzca el nombre:");
            String nombre = sc.nextLine();
            

            System.out.println("CLIENTE: Conectando con el servidor ....");
            Socket socketAlServidor = new Socket();
            socketAlServidor.connect(direccionServidor);
            System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + 
                    " por el puerto " + PUERTO);

            InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
            BufferedReader entradaBuffer = new BufferedReader(entrada);
            
            PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
            salida.println(nombre);  
            
            
            Thread recibirMensajes = new Thread(new recibirMensajes(socketAlServidor), "recibirMensajes");
    		recibirMensajes.start();


            String serverMessage = entradaBuffer.readLine();
            System.out.println("SERVER: " + serverMessage); 

   
            String texto = "";
            boolean continuar = true;
            do {
    
                System.out.println("CLIENTE: Escribe mensaje (salir para terminar): ");
                texto = sc.nextLine();
                salida.println(texto); 

                if ("salir".equalsIgnoreCase(texto)) {
                    continuar = false; 
                    recibirMensajes.interrupt();
                    break;
                } else {
                    String respuesta = entradaBuffer.readLine(); 
                    if (respuesta == null) {
                        System.err.println("El servidor ha cerrado la conexión.");
                        break;
                    } else {
                        System.out.println(respuesta); 
                    }
                }

            } while (continuar); 
            
            socketAlServidor.close();
            System.out.println("CLIENTE: Conexion cerrada.");
        } catch (UnknownHostException e) {
            System.err.println("CLIENTE: No encuentro el servidor en la dirección " + IP_SERVER);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("CLIENTE: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CLIENTE: Error -> " + e);
            e.printStackTrace();
        }
        
    }
}