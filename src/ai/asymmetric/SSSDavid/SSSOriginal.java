package ai.asymmetric.SSSDavid;



import ai.PassiveAI;
import ai.RandomBiasedAI;
import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Build;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.aiSelection.AlphaBetaSearch.Hash.Hash;
import ai.asymmetric.SSS.AdaptableStratType;
import ai.asymmetric.common.UnitScriptData;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.InterruptibleAI;
import ai.core.ParameterSpecification;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction2;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

import java.util.HashMap;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Scanner;

//import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
//import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.jdom.JDOMException;


public class SSSOriginal extends AbstractionLayerAI  {
	boolean treinando =false;
	int intervalo_amostragem=30;
	int ultimo_grupo=0;
	
	
	 HashMap<Integer, Integer> mapea;
	int conttreino=0;
	Script4 s ;
	int num_tipo;
	int rep=1;
	Information inf;
	Information inf2;
	float tempo_incial;
	List<List<Unit>> grupos;
	List<List<Unit>> grupos2;
	ArrayList<AbstractionLayerAID> scripts;
	ArrayList<Integer> atual;
	ArrayList<Integer> melhor;
	ArrayList<Integer> sepa;
	EvaluationFunction evaluation = new SimpleSqrtEvaluationFunction3();
	HashMap<Long, Integer> link ;
	//HashMap<int,int> map;
	UnitTypeTable utt;
	UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType lightType;
    UnitType heavyType;
    UnitType rangedType;
    Random gerador;
	
    ArrayList<Thread> threads;
    ArrayList<Playout> playouts;
    FileWriter arq ;
    PrintWriter gravarArq;
  
	
    public SSSOriginal configuracao1() {
    	rep=3;
    	for(Playout play : playouts ) {
    		play.setAI(new RandomBiasedAI(utt));
    		play.setTempoSimulacao(50);
    	}
    	return this;
    }
    
    public SSSOriginal configuracao2() {
    
    	rep=1;
    	for(Playout play : playouts ) {
    	
    		play.setTempoSimulacao(10000);
    		play.setProfSimulacao(200);
    	}
    	return this;
    }
    public SSSOriginal configuracao3() {
    	for(Playout play : playouts ) {
    		play.setAI(new RandomBiasedAI(utt));
    		play.setTempoSimulacao(-1);
    	
    	}
    	return this;
    }
    
    public SSSOriginal ConfiguracaoPGS() {
   	 mapea=new HashMap<Integer, Integer>();
		 mapea.put(2, 0);
		 mapea.put(3, 1);
		 mapea.put(4, 2);
		 mapea.put(5, 3);
		 
		 mapea.put(12, 4);
		 mapea.put(13, 5);
		 mapea.put(14, 6);
		 mapea.put(15, 7);
		 
		 mapea.put(22, 0);
		 mapea.put(23, 1);
		 mapea.put(24, 2);
		 mapea.put(25, 3);
		 
		 mapea.put(32, 4);
		 mapea.put(33, 5);
		 mapea.put(34, 6);
		 mapea.put(35, 7);
	
   	return this;
   }
    
    
    public SSSOriginal ConfiguracaoTipo() {
    	 mapea=new HashMap<Integer, Integer>();
		 mapea.put(2, 1);
		 mapea.put(3, 1);
		 mapea.put(4, 1);
		 mapea.put(5, 1);
		 
		 mapea.put(12, 0);
		 mapea.put(13, 0);
		 mapea.put(14, 0);
		 mapea.put(15, 0);
		 
		 mapea.put(22, 1);
		 mapea.put(23, 1);
		 mapea.put(24, 1);
		 mapea.put(25, 1);
		 
		 mapea.put(32, 0);
		 mapea.put(33, 0);
		 mapea.put(34, 0);
		 mapea.put(35, 0);
	
    	return this;
    }
    
    
    
    public void estado_treinamento(boolean b) {
    	treinando = b;
    }
    
 
    
    
	public SSSOriginal(UnitTypeTable a_utt, int numGrupos) throws JDOMException, Exception {
		
		   super( new AStarPathFinding());
		   s= new Script4(a_utt);
		   num_tipo = numGrupos;
		
		   
		  
	 mapea=new HashMap<Integer, Integer>();
			 mapea.put(2, 1);
			 mapea.put(3, 1);
			 mapea.put(4, 1);
			 mapea.put(5, 1);
			 
			 mapea.put(12, 0);
			 mapea.put(13, 0);
			 mapea.put(14, 0);
			 mapea.put(15, 0);
			 
			 mapea.put(22, 1);
			 mapea.put(23, 1);
			 mapea.put(24, 1);
			 mapea.put(25, 1);
			 
			 mapea.put(32, 0);
			 mapea.put(33, 0);
			 mapea.put(34, 0);
			 mapea.put(35, 0);
		
		   gerador = new Random();
		   link = new HashMap<Long, Integer>();
		  atual = new ArrayList<>();
		  melhor =   new ArrayList<>();
		  sepa = new ArrayList<>();
		inf = new Information();
		inf2 = new Information();
		inf2.tempo_playout=-1;
		  for(int i=0;i<num_tipo;i++) {
			  atual.add(0);
			  melhor.add(0);
			  
		  }
		   scripts = new ArrayList<>();
		   grupos = new ArrayList<>();;
		   grupos2 = new ArrayList<>();;
		   for(int i = 0; i < num_tipo;i++) {
			   grupos.add(new LinkedList<Unit>() );
			   grupos2.add(new LinkedList<Unit>() );
		   }
		   utt = a_utt;
	       workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
	       
	       buildPortfolio();
	       inf.tempo_playout = 100/scripts.size() ;
	       
	       playouts = new ArrayList<>();
	       threads = new ArrayList<>();
	       for(int i=0; i < scripts.size();i++) {
		       playouts.add( new Playout(utt,num_tipo,scripts));
		       threads.add(new Thread());
		       sepa.add(0);
	       }
	       

			
	       
	       
	}
	
	protected void buildPortfolio() {
		 
		 this.scripts.add(new Ataca(utt));
		 this.scripts.add(new MoonWalker(utt));
		//this.scripts.add(new Coleta(utt));
		//this.scripts.add(new ConstroiWorker(utt));
	
		//this.scripts.add(new Coleta(utt));
		
		
		//this.scripts.add(new Script4(utt));
		 //this.scripts.add(new Ataca(utt));
		
		
		
		// this.scripts.add(new Script2(utt));
	//	this.scripts.add(new LightDefenseD(utt));
	//	this.scripts.add(new EconomyRushBursterD(utt));
 //   this.scripts.add(new EconomyMiliratyRushD(utt));
 //this.scripts.add(new EconomyRushD(utt));
// this.scripts.add(new Script1(utt));
    // this.scripts.add(new LightDefenseD(utt));
 //this.scripts.add(new Script4(utt));
    //  this.scripts.add(new Script3(utt));
      
     
     
       // this.scripts.add(new POHeavyRush(utt));
        //this.scripts.add(new PORangedRush(utt));
        //this.scripts.add(new NOKDPS(utt));
       // this.scripts.add(new KitterDPS(utt));
       // this.scripts.add(new Cluster(utt));
        

        //this.scripts.add(new POHeavyRush(utt, new FloodFillPathFinding()));
        //this.scripts.add(new POLightRush(utt, new FloodFillPathFinding()));
        //this.scripts.add(new PORangedRush(utt, new FloodFillPathFinding()));
        
    }


	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	
	public void analisa(GameState gs) {
		int w1=0;
		int br1=0;
		int ba1=0;
		int r1=0;
		int l1=0;
		int h1=0;
		int w2=0;
		int br2=0;
		int ba2=0;
		int r2=0;
		int l2=0;
		int h2=0;
		
		 for(Unit u : gs.getUnits()) {
			 if(u.getPlayer()==0) {
				 if(u.getType() == baseType) {
					 ba1++;
				 }
				 else if (u.getType() == barracksType ) {
					 br1++;
				 }
				 else if (u.getType() == lightType) {
					 l1++;
				 }
				 else if (u.getType() == rangedType ) {
					 r1++;
				 }
				 else if (u.getType() == heavyType ) {
					 h1++;
				 }
				 else if (u.getType() == workerType ) {
					 w1++;
				 }
			 }
			 
			 if(u.getPlayer()==1) {
				 if(u.getType() == baseType) {
					 ba2++;
				 }
				 else if (u.getType() == barracksType ) {
					 br2++;
				 }
				 else if (u.getType() == lightType) {
					 l2++;
				 }
				 else if (u.getType() == rangedType ) {
					 r2++;
				 }
				 else if (u.getType() == heavyType ) {
					 h2++;
				 }
				 else if (u.getType() == workerType ) {
					 w2++;
				 }
			 }
		 }
		 
		 
		 System.out.println("----------------------------------------------------");
		 System.out.println("Player 0 avaliacao = " + evaluation.evaluate(0, 1 , gs) );
		 System.out.println("		Recurso = " + gs.getPlayer(0).getResources());
		 System.out.println("		Base = " + ba1);
		 System.out.println("		barracks = " + br1);
		 System.out.println("		Worker = " + w1);
		 System.out.println("		Light = " + l1);
		 System.out.println("		Heavy = " + h1);
		 System.out.println("		Ranged = " + r1);
		 
		 System.out.println("Player 1 avaliacao = " + evaluation.evaluate(1, 0 , gs) );
		 System.out.println("		Recurso = " + gs.getPlayer(1).getResources());
		 System.out.println("		Base = " + ba2);
		 System.out.println("		barracks = " + br2);
		 System.out.println("		Worker = " + w2);
		 System.out.println("		Light = " + l2);
		 System.out.println("		Heavy = " + h2);
		 System.out.println("		Ranged = " + r2);
		 
		 
		 
		 
	}
	
	
	
	public double simula(int player, GameState gs,HashMap<Integer, Integer> mapea2) throws InterruptedException {
		

		link.clear();
		 
		 for(int i = 0;i < grupos.size();i++) grupos.get(i).clear();
		 for(Unit un : gs.getUnits()) {
			 	if(un.getPlayer()==player) {
					int g =(int) un.getID();
					if(mapea2.get(g)!=null) {
						grupos.get(mapea2.get(g)).add(un);
						link.put(un.getID(), mapea2.get(g));
					}
					else {
						int gg = gerador.nextInt(100)%num_tipo;
					
							grupos.get(gg).add(un);
							link.put(un.getID(), gg);
							mapea2.put((int) un.getID(), gg);
						
					}
			}
		 }	
		 
		
		
		 long startTime = System.currentTimeMillis();
		 double e_m_s=-111;
		 
		 for(int nt=0;nt<num_tipo;nt++) {
			 int i =  (ultimo_grupo+nt)%num_tipo;
			 if(grupos.get(i).isEmpty()) {
				 ultimo_grupo++;
				
				 continue;
			 }
			 if(90<System.currentTimeMillis()-startTime) {
			
				 break;
			 }
			
			for(int k =0 ; k<melhor.size();k++) {
				atual.set(k , melhor.get(k));
			}
			
			
			
			ArrayList<Double> pont= new ArrayList<>();
			for(int j=0;j<playouts.size();j++)	pont.add((double) 0);
			for(int r =0;r<rep;r++) {
				
				for(int j=0;j<scripts.size();j++) {
					inf.inicio_playout =System.currentTimeMillis();
					
					atual.set(i, j);
					
					playouts.get(j).configura(player, gs, actions, i, inf, atual,link);
					
					
				
				}
					
				for(int t =0 ; t<threads.size();t++) {
					threads.set(t, new Thread(playouts.get(t)));
					threads.get(t).start();
					threads.get(t).join();
				}
					
				
				
				for(int t =0; t<playouts.size();t++) pont.set(t, pont.get(t) +playouts.get(t).resultado);
			
			}
				
				double e_m= -111;
				for(int t =0; t<playouts.size();t++) {
					double e = pont.get(t);
					
					if(e_m<e) {
						
						melhor.set(i, t);
						e_m=e;
					}
			}
				if(e_m_s<e_m) {
					e_m_s=e_m;
				}
				ultimo_grupo++;
			}
		return e_m_s;
	}
	
	@Override
	public PlayerAction getAction(int player, GameState gs) throws Exception {
		
		PhysicalGameState pgs = gs.getPhysicalGameState();
		Player p = gs.getPlayer(player);
		
		
			 double e_m = simula(player,gs,mapea);
			
		
			//System.out.println(melhor.get(0));
		List<Unit> Units_aux = new ArrayList<>();
		for(int j = 0;j < num_tipo;j++) {
			scripts.get(melhor.get(j)).getAction(player, gs, grupos.get(j), Units_aux, inf,actions);
		}
	
		if(false) {
			System.out.print("aprendendo  ");
			for(int kk =0;kk<melhor.size();kk++)
			System.out.print(" " + melhor.get(kk));
			System.out.println("------------------ " + e_m);
		}
		return translateActions(player, gs);
	}

	
	
	@Override
	public AI clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParameterSpecification> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
