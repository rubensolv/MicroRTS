package ai.ScriptsGenerator.Command;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import ai.core.AI;
import gui.PhysicalGameStatePanel;
import ai.abstraction.HeavyRush;

//import ai.ScriptsGenerator.BasicConditional.ConditionalBiggerThen;
import ai.ScriptsGenerator.Chromosome;
import ai.asymmetric.SSSDavid.COM;
import ai.asymmetric.SSSDavid.SSSDavid;
import ai.asymmetric.SSSDavid.SSSteste;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;

import javax.swing.JFrame;

import org.jdom.JDOMException;

import rts.GameState;
import rts.PhysicalGameState;

import rts.PlayerAction;

import rts.units.UnitTypeTable;


/**
 *
 * @author santi
 */
public class davidteste {

	static SSSteste ai1; 
	static SSSteste ai3;
	static AI ai2;  //IA de teste
	//static JFrame w;
	static UnitTypeTable utt;
	
	public static int partida(int num_partida, boolean exibe) throws JDOMException, IOException, Exception {

		int cont =0; // conta numero de vitoria
		for(int ii =0; ii<num_partida;ii++) {
			System.out.println("partida "+ ii);
        //UnitTypeTable utt = new UnitTYpeTableBattle();
       PhysicalGameState pgs = PhysicalGameState.load("maps/mapadavid.xml", utt);
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
     //   PhysicalGameState pgs = PhysicalGameState.load("maps/8x8/basesWorkers8x8A.xml", utt);
        //testes 
        //PhysicalGameState pgs = PhysicalGameState.load("maps/24x24/basesWorkers24x24A.xml", utt);
        
        GameState gs = new GameState(pgs, utt);
       
        int PERIOD = 20;
        boolean gameover = false;

        
          
        
        //método para fazer a troca dos players
  //   if(exibe) w = PhysicalGameStatePanel.newVisualizer(gs, 720, 720, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 720, 720, false, PhysicalGameStatePanel.COLORSCHEME_WHITE); 
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
                PlayerAction pa2;
                if(exibe)pa2 = ai2.getAction(1, gs);
                else pa2 = ai3.getAction(1, gs);
                
                if( (System.currentTimeMillis() - startTime) >0){
                	;
               //    System.out.println("Tempo de execução P2="+(startTime = System.currentTimeMillis() - startTime));
                }
                
                //System.out.println("Action A2 ="+ pa2.toString());
                
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                
                // simulate:
                gameover = gs.cycle();
            //   w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          
        } while (!gameover && gs.getTime() < 400);
      if(gs.winner()==0)cont++;

    	}
		return cont;
	}
   

    public static void main(String args[]) throws Exception {
    	
    
    	boolean selfplay;
    	if(args[0].equals("0"))selfplay=true;
    	else selfplay=false;
    	
    	int buffer=1;
    	if(args[1].equals("0"))buffer=1;
    	if(args[1].equals("1"))buffer=3;
    	if(args[1].equals("2"))buffer=5;
    	
    	int epoca=1;
    	if(args[2].equals("0"))epoca=1;
    	if(args[2].equals("1"))epoca=3;
    	if(args[2].equals("2"))epoca=5;
    	if(args[2].equals("3"))epoca=10;
    	
    	int num_partida=1;
    	if(args[3].equals("0"))num_partida=1;
    	if(args[3].equals("1"))num_partida=5;
    	if(args[3].equals("2"))num_partida=10;
    	if(args[3].equals("3"))num_partida=20;
    	
    	int intervalo_amostragem=10;
    	if(args[4].equals("0"))intervalo_amostragem=10;
    	if(args[4].equals("1"))intervalo_amostragem=30;
    	if(args[4].equals("2"))intervalo_amostragem=50;
    	if(args[4].equals("3"))intervalo_amostragem=100;
    	
    	
    	System.out.println("treinamento: "+selfplay+" "+ buffer+" "+epoca+" "+num_partida+" "+intervalo_amostragem);
    	
    	 utt = new UnitTypeTable();
    	ai1 = new SSSteste(utt,2).configuracao2(intervalo_amostragem); // SSS com 2 grupos,
    	ai2 = new HeavyRush(utt);
    	ai3 = new SSSteste(utt,2).configuracao2(intervalo_amostragem);
    	ArrayList<Integer> vitoria_treinamento;
    	ArrayList<Integer> vitoria_teste;
    	vitoria_teste = new ArrayList<>();;
    	vitoria_treinamento= new ArrayList<>();
    	int resultado;
    	COM rna = new COM(buffer,epoca); // a RNA � a mesma para todas IAs, salvando os exemplos em si msm.
    	COM rna2 = new COM(buffer,epoca);
    	ai1.setRNA(rna); 
    	if(selfplay)ai3.setRNA(rna);
    	else ai3.setRNA(rna2);
    	ai1.estado_treinamento(false);// se esta treinando ou n
    	ai3.estado_treinamento(true);// se esta treinando ou n
    	resultado = partida(1,true);
    	vitoria_teste.add(resultado);
    	vitoria_treinamento.add(-1);
    	System.out.println("Resultado treinamento: "+ vitoria_treinamento);
    	System.out.println("Resultado teste: "+ vitoria_teste);
    	for(int i=0;i<100;i++ ) {
    		System.out.println("Ciclo: "+ i);
    		ai1.estado_treinamento(true);
    		resultado = partida(num_partida,false);
    		vitoria_treinamento.add(resultado);
    		rna.treina(true);
    		if(!selfplay)rna2.treina(true);
    		ai1.estado_treinamento(false);
    		resultado = partida(1,true);
    		vitoria_teste.add(resultado);
    		System.out.println("Resultado treinamento: "+ vitoria_treinamento);
        	System.out.println("Resultado teste:       "+ vitoria_teste);
    		
    		
    	}
    	
    	
    	System.out.println("FIM");
    }
    	
   


}
