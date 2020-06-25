package ai.ScriptsGenerator.Command;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jdom.JDOMException;

import ai.asymmetric.SSSDavid.Ataca;
import ai.asymmetric.SSSDavid.MoonWalker;
import ai.asymmetric.SSSDavid.Playout;
import ai.asymmetric.SSSDavid.RNA;
import ai.asymmetric.SSSDavid.RnaJep;
import ai.asymmetric.SSSDavid.SSSteste;
import ai.core.AI;
import gui.PhysicalGameStatePanel;
import jep.JepException;
import jep.MainInterpreter;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

public class Partida implements Runnable {

	static JFrame w;
	static UnitTypeTable utt;
	static double valor_empate = 0.5;
	AI ai1;
	AI ai2;
	boolean exibe;
	double resultado;
	static String pafh_mapa;
	PhysicalGameState pgs;
	int ciclos_max;
	
	
	public void conf(AI ai1,AI ai2,double valor_empate) {
		this.ai1 = ai1;
		this.ai2 = ai2;
	}
	
	
	public Partida() throws JDOMException, Exception {
		// TODO Auto-generated constructor stub
		pafh_mapa = "maps/mapadavid2.xml";
    	int num_grupos=8;
    	
    	utt = new UnitTypeTable();
    	pgs = PhysicalGameState.load(pafh_mapa, utt);
    	
    	
    	exibe = false;
    	ciclos_max=1000;
    	
	}

	@Override
	public void run() {
		GameState gs = new GameState(pgs, utt);
	       
        int PERIOD = 20;
        boolean gameover = false;

        
          
        
        //método para fazer a troca dos players
   if(exibe) {
	   ;
      w = PhysicalGameStatePanel.newVisualizer(gs, 720, 720, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
   }   //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 720, 720, false, PhysicalGameStatePanel.COLORSCHEME_WHITE); 
        long startTime = System.currentTimeMillis();
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
        	//System.out.println(gs.getTime());
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                startTime = System.currentTimeMillis();
                
                PlayerAction pa1 = null;
				try {
					pa1 = ai1.getAction(0, gs);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println("Erro player 0");
					e1.printStackTrace();
				}  
             
                //System.out.println("Action A1 ="+ pa1.toString());
                
                startTime = System.currentTimeMillis();
                PlayerAction pa2 = null;
				try {
					pa2 = ai2.getAction(1, gs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Erro player 1");
					e.printStackTrace();
				}
                
                
             
                
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                
                // simulate:
                gameover = gs.cycle();
                if(exibe)  w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          
        } while (!gameover && gs.getTime() < ciclos_max);
      if(gs.winner()==0)resultado=1;
      else if(gs.winner()==1)resultado=0;
      else resultado = valor_empate;
 

    	
	}

	public static void main(String[] args) throws JDOMException, Exception {
		// TODO Auto-generated method stub
		MainInterpreter.setJepLibraryPath("C:\\Users\\david\\Desktop\\jep-master\\jep-master\\build\\lib.win-amd64-3.5\\jep\\jep.cp35-win_amd64.dll");
		String rnaopcao = args[1];
    	String id_teste = "_"+args[0]+"_"+args[1]+"_"+args[2];
		int num_partida=5;
		
		int intervalo_amostragem=5;
    	if(args[0].equals("1"))intervalo_amostragem=30;
    	if(args[0].equals("2"))intervalo_amostragem=50;
    	if(args[0].equals("3"))intervalo_amostragem=100;
    	
    	 RnaJep model = new RnaJep(8,8,18,8,3,3,id_teste,rnaopcao);
    	
    	
    	utt = new UnitTypeTable();
    	
    	ArrayList<Partida> partidas= new ArrayList<>();;
    	ArrayList<Thread> threads= new ArrayList<>();;

    	for(int i=0; i < 1;i++) {
    		
    		SSSteste ai1 = new SSSteste(utt,8).configuracao2(intervalo_amostragem);
    		SSSteste ai2 = new SSSteste(utt,8).configuracao2(intervalo_amostragem);
    			RNA rna = new RNA(utt,3,3,8,id_teste,rnaopcao,model);
    			RNA rna2 = new RNA(utt,3,3,8,id_teste,rnaopcao,model);
    			ai1.setRNA(rna); 
    			ai2.setRNA(rna2); 
		       partidas.add( new Partida());
		       partidas.get(i).conf(ai1, ai2, 0.5);
		       threads.add( new Thread(partidas.get(i)));
				threads.get(i).start();
	       }
    	
    	for(int i=0; i<1; i++) {
    		threads.get(i).join();
    	}
    	for(int i=0; i<1; i++) {
    		System.out.print(partidas.get(i).resultado);
    	}
    	
	}

}
