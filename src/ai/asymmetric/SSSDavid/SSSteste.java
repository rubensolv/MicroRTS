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

import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;


public class SSSteste extends AbstractionLayerAI  {
	COM rna;
	boolean treinando =false;
	int intervalo_amostragem=30;
	
	
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
	
    public SSSteste configuracao1() {
    	rep=2;
    	for(Playout play : playouts ) {
    		play.setAI(new RandomBiasedAI(utt));
    		play.setTempoSimulacao(50);
    	}
    	return this;
    }
    
    public SSSteste configuracao2(int intervaloAmostragem) {
    	intervalo_amostragem= intervaloAmostragem;
    	rep=1;
    	for(Playout play : playouts ) {
    	
    		play.setTempoSimulacao(10000);
    		play.setProfSimulacao(800);
    	}
    	return this;
    }
    public SSSteste configuracao3() {
    	for(Playout play : playouts ) {
    		play.setAI(new RandomBiasedAI(utt));
    		play.setTempoSimulacao(-1);
    	
    	}
    	return this;
    }
    
    public void estado_treinamento(boolean b) {
    	treinando = b;
    }
    
    public void setRNA(COM rna) {
    	this.rna= rna;
    }
    
    
	public SSSteste(UnitTypeTable a_utt, int numGrupos) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		
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
	       
	     //arq = new FileWriter("C:\\Users\\barba\\Desktop\\testesss\\resultado.txt",true);
	    // gravarArq = new PrintWriter(arq);
		//gravarArq.append("ticks eval_atual eval_futura S0 S1 S2 TS0 TS1 TS2 Atualisa_grupo n_unidades\n" );
		//arq.close();
		     
			
	       
	       
	}
	
	protected void buildPortfolio() {
		this.scripts.add(new Script4(utt));
		 //this.scripts.add(new Ataca(utt));
		 this.scripts.add(new MoonWalker(utt));
		
		
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
	
	public double playout(int player, GameState gs, int teste_grupo) throws Exception {
        float inicio_playout =System.currentTimeMillis();
        GameState gs2 = gs.clone();
       
        inf2.copia(inf);
        int timeLimit =  400;
        List<Unit> aux =new LinkedList<Unit>();
        boolean gameover = false;
        List<Unit> aux2 =new LinkedList<Unit>();
     
        HashMap<Unit, AbstractAction> action2;
        action2 = (HashMap<Unit, AbstractAction>) actions.clone();
        
        
        for(int i = 0;i < grupos.size();i++) grupos2.get(i).clear();

        for(Unit u : gs2.getUnits()) {
        	
        	if(link.get(u.getID())!= null ){
        		int g = link.get(u.getID());
        		grupos2.get(g).add(u);
        	    
        	}
        }
        inf.inicio_playout =System.currentTimeMillis();
        int i=0;
        for( i=0; !gameover && inf.tempo_playout > System.currentTimeMillis() - inf.inicio_playout && i < 500000 ;i++) {
           
           
            	
            	aux.clear();
           
            	
            	
                for(Unit u : gs2.getUnits()) {
                	if(link.get(u.getID())== null && player==u.getPlayer()) {
                		
                		aux.add(u);
                	    
                	}
                	
                }
              //  System.out.println(aux.size());
            //	s.getAction(player, gs2, aux, actions);
            	for(int j = 0;j < grupos.size();j++) {
            		//System.out.println("será "+ j);
            		if(teste_grupo == j ) {
            			scripts.get(atual.get(j)).getAction(player, gs2, grupos2.get(j),aux,inf2, actions);
            			
            		}
            			else {
            			
            			scripts.get(atual.get(j)).getAction(player, gs2, grupos2.get(j),aux2,inf2, actions);
            		}
        		
        		}
            	
           gs2.issue( translateActions(player, gs2));
            	AI ai4 = new LightRush(utt);
            			//new WorkerRush(utt);;
            			
            			//new LightRush(utt);// ;
                gs2.issue(ai4.getAction(1 - player, gs2));
               
                gameover = gs2.cycle();
        }

        //System.out.println(i);
     
        
     //  analisa(gs2);
     //   analisa(gs);
        actions = (HashMap<Unit, AbstractAction>) action2.clone();
        return evaluation.evaluate(player, 1 - player, gs2);
    }
	
	
	
	public void treina(boolean b) {
		rna.treina(b);
		conttreino++;
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
						int gg = gerador.nextInt(100);
						if(gg<50) {
							grupos.get(0).add(un);
							link.put(un.getID(), 0);
						}
						else {
							grupos.get(1).add(un);
							link.put(un.getID(), 1);
						}
					}
			}
		 }	
		
		
		
		for(int k =0 ; k<melhor.size();k++) {
   		atual.set(k , melhor.get(k));
   	}
		int i = gs.getTime() % grupos.size();
		ArrayList<Double> pont= new ArrayList<>();
		for(int j=0;j<playouts.size();j++)	pont.add((double) 0);
		for(int r =0;r<1;r++) {
			for(int j=0;j<scripts.size();j++) {
				inf.inicio_playout =System.currentTimeMillis();
				
				atual.set(i, j);
				
				playouts.get(j).configura(player, gs, actions, i, inf, atual,link);
				
				
			
			}
				
			for(int t =0 ; t<threads.size();t++) {
				threads.set(t, new Thread(playouts.get(t)));
				threads.get(t).start();
			}
				
			
			for(int t =0 ; t<threads.size();t++) {
				
				threads.get(t).join();
				
			}
			for(int t =0; t<playouts.size();t++) pont.set(t, pont.get(t) +playouts.get(t).resultado);
		
		}
			
			double e_m= -11111;
			for(int t =0; t<playouts.size();t++) {
				double e = pont.get(t);
				
				if(e_m<e) {
					
					melhor.set(i, t);
					e_m=e;
				}
		}
			return e_m;
		
	}
	
	@Override
	public PlayerAction getAction(int player, GameState gs) throws Exception {
		
		PhysicalGameState pgs = gs.getPhysicalGameState();
		Player p = gs.getPlayer(player);
		
	//if((gs.getTime()+1)%10==0)rna.test(player, gs, utt);;
		/*for(Unit un : gs.getUnits()) {
			if(!link.containsKey(un.getID())){
			int g =gerador.nextInt(num_tipo);
			grupos.get(g).add(un);
			link.put(un.getID(), g);
		}
			if(un.getType()==rangedType) {
				grupos.get(0).add(un);
				link.put(un.getID(), 0);
			}
			else {
				grupos.get(1).add(un);
				link.put(un.getID(), 1);
			}
		}*/
		
		
		 int simulacao =5;
		 double mg=-1111;
		 int num_unt=-1;
		 HashMap<Integer,Integer> mapea2 = (HashMap<Integer, Integer>) mapea.clone();
			 
			 if(!treinando)simulacao=1;
			 
		for(int sim=0;sim<simulacao;sim++)	 {
			if(gs.getTime()%intervalo_amostragem==0) {
				if(sim==0)rna.agrupa(player, gs, utt, mapea2);
				else rna.amostra(player, gs, utt, mapea2);
				if(!this.treinando&&player==0)System.out.println(mapea2);
				
			}
			/* agrupaem canonica, O menor id sempre estará no grupo 0
			Integer menor_id =11111;
			for (Iterator<Integer> iterator = mapea2.keySet().iterator(); iterator.hasNext();) {
				Integer i = iterator.next();
				if(menor_id > i ) {
					menor_id =i;
				}
			}
			Integer valor_id =mapea2.get(menor_id);	
			if(valor_id==1) {
				for (Iterator<Integer> iterator = mapea2.keySet().iterator(); iterator.hasNext();) {
					Integer i = iterator.next();
					Integer troca = mapea2.get(i);
					mapea2.put(i,  (troca+1)%2);
				}
			}
			*/
			
			double e_m = simula(player,gs,mapea2);
			gerador = new Random();
			
				if(e_m>=mg) {
					if(e_m==1) {
						if(num_unt<=gs.getUnits().size()) {
							mapea=(HashMap<Integer, Integer>) mapea2.clone();
							mg=e_m;
							num_unt = gs.getUnits().size();
						}
					}
					else {
						mapea=(HashMap<Integer, Integer>) mapea2.clone();
						mg=e_m;
					}
					
				}
	}	
		
		
			 double e_m = simula(player,gs,mapea);
			
		
			if(e_m>0.8&&mapea.size()>2&&gs.getTime()%10==0) {
				//	System.out.println(mapea);
					if(treinando)rna.grava(player, gs, utt,mapea);
					if(gs.getTime()==0)System.out.println(mapea);
				}
		
		
		
		
		
			//System.out.println(melhor.get(0));
		List<Unit> Units_aux = new ArrayList<>();
		for(int j = 0;j < grupos.size();j++) {
			scripts.get(melhor.get(j)).getAction(player, gs, grupos.get(j), Units_aux, inf,actions);
		}
	
		
		//for(int kk =0;kk<melhor.size();kk++)
		//System.out.print(" " + melhor.get(kk));
		//System.out.println("------------------ " + e_m);
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
