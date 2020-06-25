package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.json.JSONException;

import jep.Interpreter;
import jep.JepException;
import jep.MainInterpreter;
import jep.SharedInterpreter;

public class RnaJep {

	
	 static Socket cliente;
		static int linha,coluna, camada;
		 static Scanner teclado;
     static PrintStream saida;
     static Scanner entrada;
     Interpreter interp;
     private Semaphore semaforo;
	
	
   public RnaJep(int camada,int linha,int coluna,int num_grupo,int epoca,int buffer, String id,String estrutura) throws UnknownHostException, IOException, JepException {
	   this.semaforo = new Semaphore(1);
	   this.linha =linha;
    	this.coluna= coluna;
    	this.camada = camada;        	
    	
    	interp = new SharedInterpreter();
    	interp.exec("from java.lang import System");
    	interp.exec("import numpy as np");
    	interp.exec("import imp");
    	interp.exec("print('dsd')");
    //	interp.exec("arquivo, caminho, descricao = imp.find_module('modelo', ['/project/6046773/dsaleixo/MicroRTS/model_python_jep'])");
    	interp.exec("arquivo, caminho, descricao = imp.find_module('modelo', ['/project/6046773/dsaleixo/model_python_jep'])");
    //	interp.exec("arquivo, caminho, descricao = imp.find_module('modelo', ['C:/Users/david/Desktop/MicroRTS/model_python_jep'])");
    	interp.exec("modulo1 = imp.load_module('modelo', arquivo, caminho, descricao)");
    	
    	interp.set("linha", linha);
    	interp.set("coluna", coluna);
    	interp.set("camada", camada);
    	interp.set("num_grupo", num_grupo);
    	interp.set("epoca", epoca);
    	interp.set("buffer", buffer);
    	interp.set("id", id);
    	interp.set("estrutura", estrutura);
    	
    	
    	
    	interp.exec("model0 = modulo1.Modelo(id+'_0',camada,linha,coluna,num_grupo,epoca,buffer,estrutura)");
    	interp.exec("model1 = modulo1.Modelo(id+'_1',camada,linha,coluna,num_grupo,epoca,buffer,estrutura)");
	 
    	
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
   
   public String converte_agrup(HashMap<Integer, Integer> input2,HashMap<Integer, Integer> agrup) {
	   String msg="";
	   for (Map.Entry me : agrup.entrySet()) {
		   if(input2.get(me.getKey())!=null) {
			   msg+=input2.get(me.getKey())+":"+me.getValue()+",";
		   }
        }
	   
	   
	   return msg;
   }
   
	public synchronized HashMap<Integer, ArrayList<Float>> amostragem(float input1[][][], HashMap<Integer, Integer> input2,int num_grupo, int player) throws IOException, JepException, InterruptedException {
		
		String msg="";
		
		msg+=converte_input1(input1);
        
        msg+="*";
        msg+=converte_input2(input2);
        msg+="*";
        msg+=num_grupo;
        msg+="*";
        semaforo.acquire();
        interp.set("msg", msg);
        
    if(player==0)   interp.exec("msg = model0.amostra(msg)");  
    else interp.exec("msg = model1.amostra(msg)");  
    
       msg = (String) interp.getValue("msg");
       semaforo.release();
       
      
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
       
     
		return resposta;
	}

	public void grava(float input1[][][], HashMap<Integer, Integer> input2, HashMap<Integer, Integer> agrup, int player, int num_grupo) throws IOException, JepException, InterruptedException {
	   
		
		String msg="";
		
		msg+=converte_input1(input1);
        
        msg+="*";
        msg+=converte_input2(input2);
        msg+="*";
        msg+=num_grupo;
        msg+="*";
        msg+=converte_agrup(input2,agrup);
        msg+="*";
        msg+="FIM";
        semaforo.acquire();
        interp.set("msg", msg);
        if(player==0)interp.exec("model0.grava(msg)");  
        else {
        	interp.exec("model1.grava(msg)");  
        	
        }
       
        semaforo.release();
        
    
       
	}

	public void treina(int player) throws IOException, JepException {
		System.out.println("tee" +player);
		if(player==0)interp.exec("model0.treina()");  
		else  interp.exec("model1.treina()");  
	}


	public synchronized ArrayList<Float> distribuicao_grupo(float input1[][][], HashMap<Integer, Integer> input2,int num_grupo, int player) throws IOException, JepException, InterruptedException {
		
		String msg="";
		
		msg+=converte_input1(input1);
        
        msg+="*";
        msg+=converte_input2(input2);
        msg+="*";
        msg+=num_grupo;
        msg+="*";
        semaforo.acquire();
        interp.set("msg", msg);
        
	    if(player==0)   interp.exec("msg = model0.amostra_grupo(msg)");  
	    else interp.exec("msg = model1.amostra_grupo(msg)");  
	    msg = (String) interp.getValue("msg");
	    semaforo.release();
 
		ArrayList<Float> amostragem = new ArrayList<>();
       
       String aux[] = msg.split(",");
       for( String ss : aux) {
       	
	     
	       		amostragem.add( Float.parseFloat(ss));
	    
       }
       
     
		return amostragem;
	}
	
	
	public void carrega(String s,int player) throws IOException, JepException {
		interp.set("s", s);
		if(player==0)interp.exec("model0.carregar(s)");  
		else interp.exec("model1.carregar(s)"); 
       
	}

	
	public void proximo(int player) throws IOException, JepException {
		if(player==0)interp.exec("model0.proximo()");  
		else interp.exec("model1.proximo()");  
	
	}
	
public static void main(String[] args)throws UnknownHostException, IOException, JSONException, JepException{
		
		RnaJep m = new RnaJep(8,8,18,8,3,3,"","");
       
           
        
            
           
    
	}


public void salvar(String s,int player) throws JepException {
	interp.set("s", s);
	if(player == 0)interp.exec("model0.salvar(s)"); 
	else interp.exec("model1.salvar(s)"); 
	
}

	
	
}
