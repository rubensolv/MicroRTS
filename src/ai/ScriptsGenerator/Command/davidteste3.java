package ai.ScriptsGenerator.Command;

import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import gui.PhysicalGameStatePanel;
import jep.JepException;
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




public class davidteste3 {

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
	
	
	public static double partida(int num_partida, boolean exibe,int adv, int lado) throws JDOMException, IOException, Exception  {

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
                
                PlayerAction pa1 = ai1.getAction(lado, gs);  
                if( (System.currentTimeMillis() - startTime) >0){
                	;
           //     System.out.println("Tempo de execução P1="+(startTime = System.currentTimeMillis() - startTime));
                }
                //System.out.println("Action A1 ="+ pa1.toString());
                
                startTime = System.currentTimeMillis();
                PlayerAction pa2=null;
                if(adv==2)pa2 = ai2.getAction(1-lado, gs);
                else if(adv==3) pa2 = ai3.getAction(1-lado, gs);
                else if(adv==4) pa2 = ai4.getAction(1-lado, gs);
                else if(adv==5) pa2 = ai5.getAction(1-lado, gs);
                else if(adv==6) pa2 = ai6.getAction(1-lado, gs);
                else if(adv==7) pa2 = ai7.getAction(1-lado, gs);
                
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
   
	
	
	
	public static void main(String[] args) throws  Exception {
		// TODO Auto-generated method stub
		MainInterpreter.setJepLibraryPath("/project/6046773/dsaleixo/MicroRTS/jep_teste/jep-master/jep-master/build/lib.linux-x86_64-3.6/jep/jep.cpython-36m-x86_64-linux-gnu.so");
	//	MainInterpreter.setJepLibraryPath("/project/6046773/dsaleixo/jep_teste/jep-master/build/lib.linux-x86_64-3.6/jep/jep.cpython-36m-x86_64-linux-gnu.so");
	    //MainInterpreter.setJepLibraryPath("C:\\Users\\david\\Desktop\\jep-master\\jep-master\\build\\lib.win-amd64-3.5\\jep\\jep.cp35-win_amd64.dll");
	    	pafh_mapa="maps/8x8/basesWorkers8x8A.xml";
	    	pafh_mapa = "maps/mapadavid2.xml";
	    	int num_grupos=8;
	    	int tipo_mapa;
	    	tipo_mapa=0;// mapas david
	    	tipo_mapa=1;// mapa 8x8
	    	
	    	
	    	
	    	boolean selfplay = false;
	    	//if(args[0].equals("0"))selfplay=true;
	    	//else selfplay=false;
	    	
	    	int buffer=10;
	    	//if(args[1].equals("0"))buffer=1;
	    	//if(args[1].equals("1"))buffer=3;
	    	//if(args[1].equals("2"))buffer=5;
	    	
	    	int epoca=3;
	    	//if(args[2].equals("0"))epoca=1;
	    	//if/(args[2].equals("1"))epoca=3;
	    	//if(args[2].equals("2"))epoca=5;
	    	//if(args[2].equals("3"))epoca=10;
	    	
	    	int num_partida=5;
	    	//if(args[3].equals("0"))num_partida=1;
	    	//if(args[3].equals("1"))num_partida=5;
	    	//if(args[3].equals("2"))num_partida=10;
	    	//if(args[3].equals("3"))num_partida=20;
	    	
	    	int intervalo_amostragem=5;
	    	
	    	if(args[0].equals("1"))intervalo_amostragem=30;
	    	if(args[0].equals("2"))intervalo_amostragem=50;
	    	if(args[0].equals("3"))intervalo_amostragem=100;
	   // 	if(args[2].equals("4"))intervalo_amostragem=150;
	   // 	if(args[2].equals("5"))intervalo_amostragem=400;

	    
	    	
	    	//String iteracao = args[0];
	    	String rnaopcao = args[1];
	    	String id_teste = "_"+args[0]+"_"+args[1]+"_"+args[2];
	    	
	    	
	    	int lado = 0;
	    	if(args[3].equals("0"))lado =0;
	    	if(args[3].equals("1"))lado =1;
	    	
	    	int num_teste=30;
	    	 utt = new UnitTypeTable();
	    	 
	    	 List<AI> Scripts = new ArrayList<>();
	    	 Scripts.add(new MoonWalker(utt));
	    		Scripts.add(new Ataca(utt));
	    		 
	    	 
	    	 
	    	
	    	RnaJep model = new RnaJep(8,8,18,8,3,3,id_teste,rnaopcao);
	    	rna = new RNA(utt,buffer,epoca,num_grupos,id_teste,rnaopcao,model);
	    	
	    	
	    
	    	

	    	
	    	
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
	    
	    	
	    	
	    	
	    	
	    
	    	
	    	int analise[]= {0,9,19,29,39,49,59,69,79,89,99};
	    	
	    	for(int i : analise) {
	    		
	    		System.out.println("Ciclo " + i);
	    		ai1 = new SSSteste(utt,num_grupos).configuracao2(intervalo_amostragem); // SSS com 2 grupos,
		    	ai1.setRNA(rna); 
		    	ai1.estado_treinamento(false);// se esta treinando ou n
			    
		    	rna.carregar("_"+i,lado);
		    	ai1.estado_treinamento(false);// se esta treinando ou n
		    	ai2 = new WorkerRush(utt);//PassiveAI(utt);//SSSDavid(utt,2).configuracao2();
		    	ai4 = new SSSDavid(utt, 3,intervalo_amostragem).configuracao2();	
		    	ai5 = new CmabAssymetricMCTS(100, -1, 200, 1, 0.3f, 
		                                        0.0f, 0.4f, 0, new Ataca(utt), 
		                                           new SimpleSqrtEvaluationFunction3(), true, utt, 
		                                           "ManagerClosestEnemy", 1,Scripts); //A3N
		    	ai6 = new SSSOriginal(utt,8).configuracao2().ConfiguracaoPGS();
		    	ai7 = new SSSOriginal(utt,2).configuracao2().ConfiguracaoTipo();
		    	
	    		
		    	valor_empate=0.01;
		    	resultado = partida(num_teste,false,2,lado);
		    	vitoria_teste.add(resultado);
		    	resultado = partida(num_teste,false,4,lado);
		    	vitoria_SSSDavid.add(resultado);
		    	resultado = partida(num_teste,false,5,lado);
		    	vitoria_A3N.add(resultado);
		    	resultado = partida(num_teste,false,6,lado);
		    	vitoria_PGS.add(resultado);
		    	resultado = partida(num_teste,false,7,lado);
		    	vitoria_SSS.add(resultado);
		    	
		    	
		    	System.out.println("Resultado teste: "+ vitoria_teste);
		    	System.out.println("Resultado SSSDavid: "+ vitoria_SSSDavid);
		    	System.out.println("Resultado A3N: "+ vitoria_A3N);
		    	System.out.println("Resultado PGS: "+ vitoria_PGS);
		    	System.out.println("Resultado SSS: "+ vitoria_SSS);
		    	
	    	}
	    	
	    	
	        
		
	}

}
