package ai.asymmetric.SSSDavid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ai.abstraction.AbstractAction;
import ai.abstraction.Build;
import ai.abstraction.LightRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import rts.GameState;
import rts.Player;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class Playout extends AbstractionLayerAID implements Runnable{

	 float resultado;
	 int prof;
	 
	 
	int player;
	GameState gs2;
	int teste_grupo;
	ArrayList<AbstractionLayerAID> scripts;
	HashMap<Long, Integer> link ;
	Information inf2;
	EvaluationFunction evaluation = new SimpleSqrtEvaluationFunction3();
	List<List<Unit>> grupos2;
	HashMap<Unit, AbstractAction> action2;
	int num_tipo;
	List<Unit> aux =new LinkedList<Unit>();
	List<Unit> aux2 =new LinkedList<Unit>();
	ArrayList<Integer> atual;
	UnitTypeTable utt;
	UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType lightType;
    UnitType heavyType;
    UnitType rangedType;
    Player p;

    
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
   
    
    public Playout(UnitTypeTable a_utt,int num_g,ArrayList<AbstractionLayerAID> scripts2) {
		   super( new AStarPathFinding());	 
		   link = new HashMap<Long, Integer>();
		   inf2 = new Information();
		   
		   atual = new ArrayList<>();
		   scripts = new ArrayList<>();
		   for(AbstractionLayerAID  s : scripts2) {
			   scripts.add((AbstractionLayerAID) s.clone());
		   }   
		   
		   num_tipo = num_g;
		   grupos2 = new ArrayList<>();;
		   for(int i = 0; i < num_tipo;i++) {
			 
			   grupos2.add(new LinkedList<Unit>() );
		   }
		   utt = a_utt;
		   for(int i=0;i<num_tipo;i++) {
				  atual.add(0);
			  }
	       workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
	       
	      
	      
	      // inf.tempo_playout =  2;
	}
    
    
    public void configura(int player,GameState gs,HashMap<Unit, AbstractAction> action,
    										int teste_grupo,Information inf,ArrayList<Integer> conf, HashMap<Long, Integer> link2) {
    	
    	this.player = player;
    	gs2=gs.clone();
    	prof=0;
    	action2= (HashMap<Unit, AbstractAction>) action.clone();
    	this.teste_grupo = teste_grupo;
    	inf2.copia(inf);
    	for(int i =0 ; i<conf.size();i++) {
    		atual.set(i , conf.get(i));
    	}
    	 p = gs.getPlayer(player);
    	link = link2;
    	 for(int i = 0;i < grupos2.size();i++) grupos2.get(i).clear();

         for(Unit u : gs2.getUnits()) {
         	
         	if(link.get(u.getID())!= null ){
         		int g = link.get(u.getID());
         		grupos2.get(g).add(u);
         	    
         	}
         }
     //    System.out.println(grupos2.get(0).size());
    	
    }
    
    
    
	public void run() {
		
		double inicio_playout =System.currentTimeMillis();
   
        int timeLimit =  400;
        
        boolean gameover = false;
       
        
        
       
        
        
        for(; !gameover && 90 > System.currentTimeMillis() - inicio_playout && prof < 500000 ;prof++) {
           
        	if (gs2.isComplete()|| prof%10!=0) {
                gameover = gs2.cycle();
        	} 	
            	aux.clear();
           
            	
            	
                for(Unit u : gs2.getUnits()) {
                	if(player!=u.getPlayer()) continue; 
                	 AbstractAction action = getAbstractAction(u);
 			        if (action instanceof Build ) {
 			        	
 			        	if (((Build) action).type == baseType) {
 			        		
 			        		if(p.getResources() >= baseType.cost) {
 			        			
 			        			inf2.contruindo_base++;
 			        			continue;
 			        		}
 			        	}
 			        	else if (((Build) action).type == barracksType) {
 			        		if(p.getResources() >= barracksType.cost) {
 			        			inf2.contruindo_barraca++;
 			        			continue;
 			        		}
 			        	
 			        	}
                	
                	
 			        }
                	if(link.get(u.getID())== null ) {
                		
                		aux.add(u);
                	    
                	}
                
                	
                }
              //  System.out.println(aux.size());
            //	s.getAction(player, gs2, aux, actions);
            	for(int j = 0;j < grupos2.size();j++) {
            		
            		if(teste_grupo == j ) {
            			try {
							scripts.get(atual.get(j)).getAction(player, gs2, grupos2.get(j),aux,inf2, actions);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			
            		}
            			else {
            			
            			try {
							scripts.get(atual.get(j)).getAction(player, gs2, grupos2.get(j),aux2,inf2, actions);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
        		
        		}
            	
           gs2.issue( translateActions(player, gs2));
            	AI ai4 = new LightRush(utt);
            			//new WorkerRush(utt);//
            			
            			//new LightRush(utt);// ;
                try {
					gs2.issue(ai4.getAction(1 - player, gs2));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
                //gameover = gs2.cycle();
        }

     // analisa(gs2);
       
        resultado =  evaluation.evaluate(player, 1 - player, gs2);
        
    //  System.out.println(prof);
        return;
    }
		
		
		
		
}
