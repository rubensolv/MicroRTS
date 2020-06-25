package ai.ScriptsGenerator.Command;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import gui.PhysicalGameStatePanel;
import jep.MainInterpreter;
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
import ai.asymmetric.SSSDavid.RnaJep;
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
public class davidteste {

	static SSSteste ai1; 
	static SSSteste ai3;
	static AI ai2;  //IA de teste
	static AI ai4;
	static AI ai5;
	static AI ai6;
	static AI ai7;
	static JFrame w;
	static UnitTypeTable utt;
	static RNA rna;
	static RNA rna2;
	static double valor_empate = 0.5;
	
	static String pafh_mapa;
	
	public static double partida(int num_partida, boolean exibe,int adv) throws JDOMException, IOException, Exception {
	//	exibe=true;
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
        	//System.out.println(gs.getTime());
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
                else if(adv==3) pa2 = ai3.getAction(1, gs);
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
          
        } while (!gameover && gs.getTime() < 1000);
      if(gs.winner()==0)cont+=1;
      if(gs.winner()==-1)cont+= valor_empate;
 

    	}
		return cont;
	}
   

	
	
	
    public static void main(String args[]) throws Exception {
    //	MainInterpreter.setJepLibraryPath("/project/6046773/dsaleixo/MicroRTS/jep_teste/jep-master/jep-master/build/lib.linux-x86_64-3.6/jep/jep.cpython-36m-x86_64-linux-gnu.so");
    	MainInterpreter.setJepLibraryPath("/project/6046773/dsaleixo/jep_teste/jep-master/build/lib.linux-x86_64-3.6/jep/jep.cpython-36m-x86_64-linux-gnu.so");
    //     MainInterpreter.setJepLibraryPath("C:\\Users\\david\\Desktop\\jep-master\\jep-master\\build\\lib.win-amd64-3.5\\jep\\jep.cp35-win_amd64.dll");
    	//pafh_mapa="maps/8x8/basesWorkers8x8A.xml";
    	pafh_mapa = "maps/mapadavid2.xml";
    	int num_grupos=8;
    	
    	
    	
    	int num_partida=5;
    	//if(args[3].equals("0"))num_partida=1;
    	//if(args[3].equals("1"))num_partida=5;
    	//if(args[3].equals("2"))num_partida=10;
    	//if(args[3].equals("3"))num_partida=20;
    	
    	int intervalo_amostragem=5;
    	
    	if(args[0].equals("0"))intervalo_amostragem=10;
    	if(args[0].equals("1"))intervalo_amostragem=30;
    	if(args[0].equals("2"))intervalo_amostragem=50;
   // 	if(args[2].equals("4"))intervalo_amostragem=150;
   // 	if(args[2].equals("5"))intervalo_amostragem=400;

    
    	
    	//String iteracao = args[0];
    	String rnaopcao = args[1];
    	String id_teste = "_"+args[0]+"_"+args[1]+"_"+args[2];
    	
    	
    	
    	int num_teste=3;
    	 utt = new UnitTypeTable();
    	 
    	List<AI> Scripts = new ArrayList<>();
    	Scripts.add(new MoonWalker(utt));
    	Scripts.add(new Ataca(utt));
    		 
    	 
    	 
    	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem); // SSS com 2 grupos,
    	ai2 = new WorkerRush(utt);//PassiveAI(utt);//SSSDavid(utt,2).configuracao2();
    	ai3 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
    	ai4 = new SSSDavid(utt, 3,intervalo_amostragem).configuracao2();	
    	ai5 = new CmabAssymetricMCTS(100, -1, 150, 1, 0.3f, 
                                        0.0f, 0.4f, 0, new Ataca(utt), 
                                           new SimpleSqrtEvaluationFunction3(), true, utt, 
                                           "ManagerClosestEnemy", 1,Scripts); //A3N
    	ai6 = new SSSOriginal(utt,8).configuracao2().ConfiguracaoPGS();
    	ai7 = new SSSOriginal(utt,2).configuracao2().ConfiguracaoTipo();
    	
    	RnaJep model = new RnaJep(8,8,18,8,3,3,id_teste,rnaopcao);
    	rna = new RNA(utt,10,3,num_grupos,id_teste,rnaopcao,model);
    	rna2 = new RNA(utt,10,3,num_grupos,id_teste,rnaopcao,model);
    	
    	
    	
    	
    	ai1.setRNA(rna); 
    	ai3.setRNA(rna2);
    	rna.salvar("",0);
    	rna2.salvar("",1);

    	
    	
    	ArrayList<Double> vitoria_treinamento= new ArrayList<>();
    	ArrayList<Double> vitoria_teste= new ArrayList<>();
    	ArrayList<Double> vitoria_SSSDavid= new ArrayList<>();
    	ArrayList<Double> vitoria_A3N= new ArrayList<>();
    	ArrayList<Double> vitoria_PGS= new ArrayList<>();
    	ArrayList<Double> vitoria_SSS= new ArrayList<>();
    	ArrayList<Double> atualiza0= new ArrayList<>();
    	ArrayList<Double> atualiza1= new ArrayList<>();
    	ArrayList<Double> rr1= new ArrayList<>();
    	ArrayList<Double> rr2= new ArrayList<>();
    	ArrayList<Double> rr3= new ArrayList<>();
    	double resultado;
    	
    	
    
    	ai1.estado_treinamento(false);// se esta treinando ou n
    	ai3.estado_treinamento(true);// se esta treinando ou n
    	
    
    	
    	for(int i =0;i<0;i++) {
	    	valor_empate=0.01;
	    	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    	resultado = partida(num_teste,false,2);
	    	vitoria_teste.add(resultado);
	    	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    	resultado = partida(num_teste,false,4);
	    	vitoria_SSSDavid.add(resultado);
	    	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    	resultado = partida(num_teste,false,5);
	    	vitoria_A3N.add(resultado);
	    	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    	resultado = partida(num_teste,false,6);
	    	vitoria_PGS.add(resultado);
	    	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    	resultado = partida(num_teste,false,7);
	    	vitoria_SSS.add(resultado);
	    	
	    	vitoria_treinamento.add(-1.0);
	    	System.out.println("Resultado treinamento: "+ vitoria_treinamento);
	    	System.out.println("Resultado teste: "+ vitoria_teste);
	    	System.out.println("Resultado SSSDavid: "+ vitoria_SSSDavid);
	    	System.out.println("Resultado A3N: "+ vitoria_A3N);
	    	System.out.println("Resultado PGS: "+ vitoria_PGS);
	    	System.out.println("Resultado SSS: "+ vitoria_SSS);
	    	
    	}
    	
    	
    	
    	
    	
    	for(int i=0;i<100;i++ ) {
    		if(i%5==0) {
	    		rna.salvar("_"+i,0);
	    		rna2.salvar("_"+i,1);
    		}
    		ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem); // SSS com 2 grupos,
        	ai2 = new WorkerRush(utt);//PassiveAI(utt);//SSSDavid(utt,2).configuracao2();
        	ai3 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
        	ai4 = new SSSDavid(utt, 3,intervalo_amostragem).configuracao2();	
        	ai5 = new CmabAssymetricMCTS(100, -1, 150, 1, 0.3f, 
                                            0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                                               new SimpleSqrtEvaluationFunction3(), true, utt, 
                                               "ManagerClosestEnemy", 1,Scripts); //A3N
        	ai6 = new SSSOriginal(utt,8).configuracao2().ConfiguracaoPGS();
        	ai7 = new SSSOriginal(utt,2).configuracao2().ConfiguracaoTipo();
       
        	ai1.setRNA(rna); 
        	ai3.setRNA(rna2);
    		
    		
    		System.out.println("Ciclo: "+ i);
    		ai1.estado_treinamento(true);
    		ai3.estado_treinamento(true);
    		valor_empate=0.5;
    		resultado = partida(num_partida,false,3);
    		vitoria_treinamento.add(resultado);
    		System.out.println("Resultado treinamento: "+ vitoria_treinamento);
    	
    			rna.salvar("",0);
    			rna.treina(0);
    			rna.salvar("_copia",0);
    			rna.proximo(0);
    			
    			rna2.salvar("",1);
    			rna2.treina(1);
    			rna2.salvar("_copia",1);
    			rna2.proximo(1);
    		
    		double r2,r3;
    		
    		
    		ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem); // SSS com 2 grupos
        	ai3 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
        	ai1.setRNA(rna); 
        	ai3.setRNA(rna2);
    		ai1.estado_treinamento(false);
    		ai3.estado_treinamento(false);
    		rna.carregar("",0);
    		rna2.carregar("",1);
    		resultado = partida(num_teste,false,3);
    		
    		ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem); // SSS com 2 grupos
        	ai3 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
        	ai1.setRNA(rna); 
        	ai3.setRNA(rna2);
        	ai1.estado_treinamento(false);
    		ai3.estado_treinamento(false);
    		rna.carregar("",0);
    		rna2.carregar("_copia",1);
    		r2 = partida(num_teste,false,3);
    		
    		ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem); // SSS com 2 grupos
        	ai3 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
        	ai1.setRNA(rna); 
        	ai3.setRNA(rna2);	
        	ai1.estado_treinamento(false);
    		ai3.estado_treinamento(false);
    		rna.carregar("_copia",0);
    		rna2.carregar("",1);
    		r3 = partida(num_teste,false,3);

    		if(resultado>r2) {
    			System.out.println("atualizou 1");
    			rna2.carregar("_copia",1);
    			rna2.salvar("",1);
    			atualiza1.add((double) 1);
    			
    		}else {
    			atualiza1.add((double) 0);
    		}
    		
    		if(resultado<r3) {
    			System.out.println("atualizou 0");
    			rna.carregar("_copia",0);
    			rna.salvar("",0);
    			atualiza0.add((double) 1);
    		}else {
    			atualiza0.add((double) 0);
    		}
    		rr1.add(resultado);
    		rr2.add(r2);
    		rr3.add(r3);
    		
    		rna2.proximo(1);
    		rna.proximo(0);
    		rna.carregar("",0);
    		rna2.carregar("",1);
    		
    		System.out.println("r1: "+ rr1);
    		System.out.println("r2: "+ rr2);
    		System.out.println("r3: "+ rr3);
    		System.out.println("Atualiza0: "+ atualiza0);
	    	System.out.println("Atualiza1: : "+ atualiza1);
    		
    		
    		if(i%5==0&&false) {
    			valor_empate=0.01;
	    		ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    		ai1.setRNA(rna); 
	    		ai1.estado_treinamento(false);
	    		
	    		resultado = partida(num_teste,false,2);
	        	vitoria_teste.add(resultado);
	        	System.out.println("Resultado teste: "+ vitoria_teste);
	        	
	        	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    		ai1.setRNA(rna); 
	    		ai1.estado_treinamento(false);
	    		ai4 = new SSSDavid(utt, 3,intervalo_amostragem).configuracao2();	
	        	
	    		
	        	resultado = partida(num_teste,false,4);
	        	vitoria_SSSDavid.add(resultado);
	        	System.out.println("Resultado SSSDavid: "+ vitoria_SSSDavid);
	        	
	        	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    		ai1.setRNA(rna); 
	    		ai1.estado_treinamento(false);
	    		ai5.reset();
	    		
	        	resultado = partida(num_teste,false,5);
	        	vitoria_A3N.add(resultado);
	        	System.out.println("Resultado A3N: "+ vitoria_A3N);
	        	
	        	
	        	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    		ai1.setRNA(rna); 
	    		ai1.estado_treinamento(false);
	    		ai6 = new SSSOriginal(utt,8).configuracao2().ConfiguracaoPGS();
	        	
	        	
	        	resultado = partida(num_teste,false,6);
	        	vitoria_PGS.add(resultado);
	        	System.out.println("Resultado PGS: "+ vitoria_PGS);
	        	
	        	ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem);
	    		ai1.setRNA(rna); 
	    		ai1.estado_treinamento(false);
	    		ai7 = new SSSOriginal(utt,2).configuracao2().ConfiguracaoTipo();
	        	
	        	resultado = partida(num_teste,false,7);
	        	vitoria_SSS.add(resultado);
	        	System.out.println("Resultado SSS: "+ vitoria_SSS);
	    		
    		}
    		
        	
        	
    		
    	}
    	
    	//rna.salverna("_"+args[0]+"_"+args[1]+"_"+args[2] ); 
    	System.out.println("FIM");
    }
    	
   


}
