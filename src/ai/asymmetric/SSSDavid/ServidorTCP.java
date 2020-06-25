package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorTCP {
	public static void main(String[] args) throws Exception {
		ServerSocket servidor = new ServerSocket(12345);
        System.out.println("Porta 12345 aberta!");

        Socket cliente = servidor.accept();
        System.out.println("Nova conexão com o cliente " +     
            cliente.getInetAddress().getHostAddress()
        );
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        Scanner s = new Scanner(cliente.getInputStream());
        while (s.hasNext()) {
            System.out.println(s.next());
            
           // saida.println("ok");
        }

        s.close();
        servidor.close();
        cliente.close();
    
	 
	  }     
	}