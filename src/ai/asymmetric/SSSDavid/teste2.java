package ai.asymmetric.SSSDavid;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import gui.PhysicalGameStatePanel;
import ai.abstraction.HeavyRush;
import ai.abstraction.WorkerRush;
import ai.PassiveAI;
import ai.RandomBiasedAI;
//import ai.ScriptsGenerator.BasicConditional.ConditionalBiggerThen;
import ai.ScriptsGenerator.Chromosome;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SSS.SSSmRTS;
import ai.asymmetric.SSSDavid.Ataca;
import ai.asymmetric.SSSDavid.COM;
import ai.asymmetric.SSSDavid.MoonWalker;
import ai.asymmetric.SSSDavid.RNA;
import ai.asymmetric.SSSDavid.SSSDavid;
import ai.asymmetric.SSSDavid.SSSOriginal;
import ai.asymmetric.SSSDavid.SSSteste;
import ai.competition.capivara.CmabAssymetricMCTS;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import org.jdom.JDOMException;

import rts.GameState;
import rts.PhysicalGameState;

import rts.PlayerAction;

import rts.units.UnitTypeTable;


/**
 *
 * @author david
 */
public class teste2 {

	static AI ai1;
	static AI ai2;  //IA de teste
	static AI ai4;
	static AI ai5;
	static AI ai6;
	static AI ai7;
	static JFrame w;
	static UnitTypeTable utt;

	
	static String pafh_mapa;
	
	public static double partida(int num_partida, boolean exibe,int adv) throws JDOMException, IOException, Exception {

		double cont =0; // conta numero de vitoria
		for(int ii =0; ii<num_partida;ii++) {
		//	System.out.println("partida "+ ii);
        //UnitTypeTable utt = new UnitTYpeTableBattle();
       PhysicalGameState pgs = PhysicalGameState.load(pafh_mapa, utt);
      // PhysicalGameState pgs = PhysicalGameState.load("maps/16x16/basesWorkers16x16.xml", utt);
      //  PhysicalGameState pgs = PhysicalGameState.load("maps/16x16/BasesWithWalls16x16.xml", utt);
     //  PhysicalGameState pgs = PhysicalGameState.load("maps/16x16/basesWorkers16x16A.xml", utt);        
     //   PhysicalGameState pgs = PhysicalGameState.load("maps/BWDistantResources32x32.xml", utt);
     //  PhysicalGameState pgs = PhysicalGameState.load("maps/32x32/basesWorkers32x32A.xml", utt);
    //  PhysicalGameState pgs = PhysicalGameState.load("maps/24x24/basesWorkers24x24A.xml", utt);
   //     PhysicalGameState pgs = PhysicalGameState.load("maps/BroodWar/(4)BloodBath.scmB.xml", utt);
      //  PhysicalGameState pgs = PhysicalGameState.load("maps/8x8/FourBasesWorkers8x8.xml", utt);
     //// PhysicalGameState pgs = PhysicalGameState.load("maps/16x16/TwoBasesBarracks16x16.xml", utt);
    // PhysicalGameState pgs = PhysicalGameState.load("maps/NoWhereToRun9x8.xml", utt);
      //  PhysicalGameState pgs = PhysicalGameState.load("maps/DoubleGame24x24.xml", utt);
        //PhysicalGameState pgs = MapGenerator.basesWorkers8x8Obstacle();
      //  PhysicalGameState pgs = PhysicalGameState.load("maps/8x8/basesWorkers8x8Obstacle.xml", utt);
//        PhysicalGameState pgs = PhysicalGameState.load("maps/8x8/basesWorkers8x8A.xml", utt);
        //testes 
        //PhysicalGameState pgs = PhysicalGameState.load("maps/24x24/basesWorkers24x24A.xml", utt);
        
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
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                startTime = System.currentTimeMillis();
                
                PlayerAction pa1 = ai1.getAction(0, gs);  
                if( (System.currentTimeMillis() - startTime) >0){
                	;
           //     System.out.println("Tempo de execução P1="+(startTime = System.currentTimeMillis() - startTime));
                }
                //System.out.println("Action A1 ="+ pa1.toString());
                
                startTime = System.currentTimeMillis();
                PlayerAction pa2=null;
                if(adv==2)pa2 = ai2.getAction(1, gs);
             
                else if(adv==4) pa2 = ai4.getAction(1, gs);
                else if(adv==5) pa2 = ai5.getAction(1, gs);
                else if(adv==6) pa2 = ai6.getAction(1, gs);
                else if(adv==7) pa2 = ai7.getAction(1, gs);
                
                if( (System.currentTimeMillis() - startTime) >0){
                	;
               //    System.out.println("Tempo de execução P2="+(startTime = System.currentTimeMillis() - startTime));
                }
                
                //System.out.println("Action A2 ="+ pa2.toString());
                
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
          
        } while (!gameover && gs.getTime() < 1500);
      if(gs.winner()==0)cont+=1;
      if(gs.winner()==-1)cont+=0.01;
   
    	}
		return cont;
	}
   

    public static void main(String args[]) throws Exception {
    	
    	pafh_mapa = "maps/mapadavid2.xml";
    	int num_grupos=8;
    	
    	
    	System.out.println("Playout nao aleatorio");
    	
    
    	
    	
    	
    	int num_teste=100;
    	 utt = new UnitTypeTable();
    	 
    	 List<AI> Scripts = new ArrayList<>();
    	 Scripts.add(new MoonWalker(utt));
    		Scripts.add(new Ataca(utt));
    		 
    	 
    	 
    	
    	ai2 = new WorkerRush(utt);//PassiveAI(utt);//SSSDavid(utt,2).configuracao2();
    	ai4 = new SSSDavid(utt, 3).configuracao2();	
    	ai5 = new CmabAssymetricMCTS(100, -1, 150, 1, 0.3f, 
                                             0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                                             new SimpleSqrtEvaluationFunction3(), true, utt, 
                                           "ManagerClosestEnemy", 1,Scripts); //A3N
    	ai6 = new SSSOriginal(utt,8).configuracao2().ConfiguracaoPGS();
    	ai7 = new SSSOriginal(utt,2).configuracao2().ConfiguracaoTipo();
    	
    	
    	ArrayList<Double> vitoria_teste= new ArrayList<>();
    	ArrayList<Double> vitoria_SSSDavid= new ArrayList<>();
    	ArrayList<Double> vitoria_A3N= new ArrayList<>();
    	ArrayList<Double> vitoria_PGS= new ArrayList<>();
    	ArrayList<Double> vitoria_SSS= new ArrayList<>();
    	
    	
    	
    	
    	
    	double resultado;
    	
    	PhysicalGameState pgs = PhysicalGameState.load(pafh_mapa, utt);
    	
    	
    	ai1=  new WorkerRush(utt);
    	resultado = partida(num_teste,false,2);
    	vitoria_teste.add(resultado);
    	resultado = partida(num_teste,false,4);
    	vitoria_teste.add(resultado);
    	resultado = partida(num_teste,false,5);
    	vitoria_teste.add(resultado);
    	resultado = partida(num_teste,false,6);
    	vitoria_teste.add(resultado);
    	resultado = partida(num_teste,false,7);
    	vitoria_teste.add(resultado);
    	
    	ai1= new SSSDavid(utt, 4).configuracao2();	
    	resultado = partida(num_teste,false,2);
    	vitoria_SSSDavid.add(resultado);
    	resultado = partida(num_teste,false,4);
    	vitoria_SSSDavid.add(resultado);
    	resultado = partida(num_teste,false,5);
    	vitoria_SSSDavid.add(resultado);
    	resultado = partida(num_teste,false,6);
    	vitoria_SSSDavid.add(resultado);
    	resultado = partida(num_teste,false,7);
    	vitoria_SSSDavid.add(resultado);
    	
    	ai1=  new CmabAssymetricMCTS(100, -1, 150, 1, 0.3f, 
                0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                new SimpleSqrtEvaluationFunction3(), true, utt, 
              "ManagerClosestEnemy", 1,Scripts); //A3N
    	resultado = partida(num_teste,false,2);
    	vitoria_A3N.add(resultado);
    	resultado = partida(num_teste,false,4);
    	vitoria_A3N.add(resultado);
    	resultado = partida(num_teste,false,5);
    	vitoria_A3N.add(resultado);
    	resultado = partida(num_teste,false,6);
    	vitoria_A3N.add(resultado);
    	resultado = partida(num_teste,false,7);
    	vitoria_A3N.add(resultado);
    	
    	ai1=  new SSSOriginal(utt,8).configuracao2().ConfiguracaoPGS();
    	resultado = partida(num_teste,false,2);
    	vitoria_PGS.add(resultado);
    	resultado = partida(num_teste,false,4);
    	vitoria_PGS.add(resultado);
    	resultado = partida(num_teste,false,5);
    	vitoria_PGS.add(resultado);
    	resultado = partida(num_teste,false,6);
    	vitoria_PGS.add(resultado);
    	resultado = partida(num_teste,false,7);
    	vitoria_PGS.add(resultado);
    	
    	ai1=  new SSSOriginal(utt,2).configuracao2().ConfiguracaoTipo();
    	resultado = partida(num_teste,false,2);
    	vitoria_SSS.add(resultado);
    	resultado = partida(num_teste,false,4);
    	vitoria_SSS.add(resultado);
    	resultado = partida(num_teste,false,5);
    	vitoria_SSS.add(resultado);
    	resultado = partida(num_teste,false,6);
    	vitoria_SSS.add(resultado);
    	resultado = partida(num_teste,false,7);
    	vitoria_SSS.add(resultado);
    	
    	
    	
    	
    	
    	System.out.println("Resultado teste: "+ vitoria_teste);
    	System.out.println("Resultado SSSDavid: "+ vitoria_SSSDavid);
    	System.out.println("Resultado A3N: "+ vitoria_A3N);
    	System.out.println("Resultado PGS: "+ vitoria_PGS);
    	System.out.println("Resultado SSS: "+ vitoria_SSS);
   
    	
    	//rna.salverna("_"+args[0]+"_"+args[1]+"_"+args[2] ); 
    	System.out.println("FIM");
    }
    	
   


}
