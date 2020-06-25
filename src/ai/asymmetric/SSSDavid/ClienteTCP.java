package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eclipsesource.json.JsonObject;

public class ClienteTCP {
	
	 static Socket cliente;
		static int linha,coluna, camada;
		 static Scanner teclado;
        static PrintStream saida;
        static Scanner entrada;
		
		
       public ClienteTCP(int linha,int coluna, int camada) throws UnknownHostException, IOException {
        	this.linha =linha;
        	this.coluna= coluna;
        	this.camada = camada;
        	
        	
        }
        
       
       
       public String converte_input1(float input1[][][]) {
    	   String msg="";
    	   for(int i =0 ; i < camada ;i++ ) {
	        	msg+="#";
	        	for(int j=0;j<linha;j++) {
	        		
	        		for(int k = 0;k<coluna;k++) {
	        			msg+=input1[i][j][k];
	        			msg+=",";
	        		
	        		}
	        		
	        	}
	        	
	        }
	           
	        msg+="#";
	        
	        return msg;
    	   
       }
        
       
       public String converte_input2(HashMap<Integer, Integer> input2) {
    	   String msg="";
    	   for (Map.Entry me : input2.entrySet()) {
	        	
	        	msg+=me.getKey()+":"+me.getValue()+",";
	        }
    	   return msg;
       }
       
       public String converte_agrup(HashMap<Integer, Integer> agrup) {
    	   String msg="";
    	   for (Map.Entry me : agrup.entrySet()) {
	        	
	        	msg+=me.getKey()+":"+me.getValue()+",";
	        }
    	   return msg;
       }
       
		public HashMap<Integer, ArrayList<Float>> amostragem(float input1[][][], HashMap<Integer, Integer> input2) throws IOException {
			 cliente = new Socket("127.0.0.11", 12345);
		        teclado = new Scanner(System.in);
		        saida = new PrintStream(cliente.getOutputStream());
		        entrada = new Scanner(cliente.getInputStream());
			
			saida.println("amostragem");
			String msg="";
			
			msg+=converte_input1(input1);
	        
	        msg+="*";
	        msg+=converte_input2(input2);
	        msg+="*";
	        msg+="3";
	        msg+="*";
	        msg+="FIM";
	       
	        
			
	       
        	
            saida.println(msg);
          
           
	        
	        
           msg = entrada.next();
           
           
           HashMap<Integer, ArrayList<Float>> resposta = new HashMap<Integer, ArrayList<Float>>() ;
           
           String aux[] = msg.split(";");
           for( String ss : aux) {
           	String aux2[] = ss.split(":");
           	String aux3[] = aux2[1].split(",");
           	int id = Integer.parseInt( aux2[0]);
           	ArrayList<Float> amostragem = new ArrayList<>();
           	for(int i=0;i<8;i++) {
           		amostragem.add( Float.parseFloat(aux3[i]));
           	}
           	resposta.put(id, amostragem);
           		
           }
           
           
    
           	

       saida.close();
       teclado.close();
       cliente.close();
      
           
	        
			return resposta;
		}
	
		public void grava(float input1[][][], HashMap<Integer, Integer> input2, HashMap<Integer, Integer> agrup) throws IOException {
		    cliente = new Socket("127.0.0.11", 12345);
	        teclado = new Scanner(System.in);
	        saida = new PrintStream(cliente.getOutputStream());
	        entrada = new Scanner(cliente.getInputStream());
	      
			
	        saida.println("gravar");
			
			
			String msg="";
			
			msg+=converte_input1(input1);
	        
	        msg+="*";
	        msg+=converte_input2(input2);
	        msg+="*";
	        msg+="3";
	        msg+="*";
	        msg+=converte_agrup(agrup);
	        msg+="*";
	        msg+="FIM";
	    
        	
            saida.println(msg);
          
        
	        
	        
           msg = entrada.next();
           saida.close();
           teclado.close();
           cliente.close();
           
		}

		public void treina() throws IOException {
			
	        
	      
	        cliente = new Socket("127.0.0.11", 12345);
	        teclado = new Scanner(System.in);
	        saida = new PrintStream(cliente.getOutputStream());
	        entrada = new Scanner(cliente.getInputStream());
	        
			
	        saida.println("treinar");
        	
 
	        
	        
          entrada.next();
          saida.close();
          teclado.close();
          cliente.close();
           
		}
	
	
		public void carrega() throws IOException {
			
	        
		      
	        cliente = new Socket("127.0.0.11", 12345);
	        teclado = new Scanner(System.in);
	        saida = new PrintStream(cliente.getOutputStream());
	        entrada = new Scanner(cliente.getInputStream());
	        
			
	        saida.println("carregar");
	        
          entrada.next();
          
          
          saida.close();
          teclado.close();
          cliente.close();
           
		}
	
		
		public void proximo() throws IOException {
			
	        
		      
	        cliente = new Socket("127.0.0.11", 12345);
	        teclado = new Scanner(System.in);
	        saida = new PrintStream(cliente.getOutputStream());
	        entrada = new Scanner(cliente.getInputStream());
	        
			
	        saida.println("proximo");
	        
          entrada.next();
          
          
          saida.close();
          teclado.close();
          cliente.close();
           
		}
		
		
	
	public static void main(String[] args)throws UnknownHostException, IOException, JSONException{
		
		Random gerador = new Random();
		
        
        System.out.println("O cliente se conectou ao servidor!");
        
        
        linha = coluna = camada =8;
       coluna = 18;
        float estado[][][]= new float[camada][linha][coluna];
        
        
       
    //   amostragem(estado);
          
       
           
        
            
           
    
	}


	
}
