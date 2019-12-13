package ai.asymmetric.SSSDavid;
import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Harvest;
import ai.abstraction.Move;
import ai.abstraction.RangedRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.core.AI;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.ParameterSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.*;


public class MoonWalker extends AbstractionLayerAID  {

	Random r = new Random();
    protected UnitTypeTable utt;
    UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType rangedType;

    // If we have any "light": send it to attack to the nearest enemy unit
    // If we have a base: train worker until we have 1 workers
    // If we have a barracks: train light
    // If we have a worker: do this if needed: build base, build barracks, harvest resources
    public MoonWalker(UnitTypeTable a_utt) {
        this(a_utt, new AStarPathFinding());
    }


    public MoonWalker(UnitTypeTable a_utt, PathFinding a_pf) {
        super(a_pf);
        reset(a_utt);
    }

    public void reset() {
    	super.reset();
    }
    
    public void reset(UnitTypeTable a_utt) {
        utt = a_utt;
        workerType = utt.getUnitType("Worker");
        baseType = utt.getUnitType("Base");
        barracksType = utt.getUnitType("Barracks");
        rangedType = utt.getUnitType("Ranged");
    }

    public AI clone() {
        return new MoonWalker(utt);
    }

    public PlayerAction getAction(int player, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);
//        System.out.println("LightRushAI for player " + player + " (cycle " + gs.getTime() + ")");
        List<Unit> workers = new LinkedList<Unit>();
        // behavior of bases:
        for (Unit u : pgs.getUnits()) {
            
            if (u.getType().canAttack
                    && u.getPlayer() == player
                    ) {
                meleeUnitBehavior(u, p, gs);
            }
        
           
        }
    //    workersBehavior(workers, p, gs,inf);

        actions = null;
        return translateActions(player, gs);
    }
    
    
    public PlayerAction getAction(int player, GameState gs, List<Unit> Units,List<Unit> Units_aux, Information inf,
    																	HashMap<Unit, AbstractAction> act) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);
        actions = act;
//        System.out.println("LightRushAI for player " + player + " (cycle " + gs.getTime() + ")");
        List<Unit> workers = new LinkedList<Unit>();
        // behavior of bases:
        for (Unit u : Units) {
           if (u.getType().canAttack
                    && u.getPlayer() == player
                    ) {
                meleeUnitBehavior(u, p, gs);
            }
        }
        
        
        for (Unit u : Units_aux) {
           
        
        // behavior of melee units:
     
           if (u.getType().canAttack && !u.getType().canHarvest
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null && !actions.containsKey(u)) {
                meleeUnitBehavior(u, p, gs);
            }
        
        // behavior of workers:
       
        }
        
        
   

        actions = null;
        return null;
    }

   

    public void meleeUnitBehavior(Unit u, Player p, GameState gs) {
    	int y = (int) (u.getY());
    	boolean[][] free = gs.getPhysicalGameState().getAllFree();
    	if(p.getID()==0) {
    			if(u.getX()==0) {
    				actions.put(u, new Move(u, 0, y, pf));
    				return;
    			}
    			else if(free[u.getX()-1][y]) {
    				actions.put(u, new Move(u, u.getX()-1, y, pf));
    			}
    			else if(u.getX()>1) {
    				if(free[u.getX()-2][y]) {
        				actions.put(u, new Move(u, u.getX()-2, y, pf));
        			}
    			}
    	}
    	if(p.getID()==1) {
    		int aaa= gs.getPhysicalGameState().getWidth()-1;
			if(u.getX()==gs.getPhysicalGameState().getWidth()-1) {
				actions.put(u, new Move(u, aaa, y, pf));
				return;
			}
			else if(free[u.getX()+1][y]) {
				actions.put(u, new Move(u, u.getX()+1, y, pf));
			}
			else if(u.getX()>1) {
				if(free[u.getX()-2][y]) {
    				actions.put(u, new Move(u, u.getX()+2, y, pf));
    			}
			}
	}
    	
    }
   
    @Override
    public List<ParameterSpecification> getParameters()
    {
        List<ParameterSpecification> parameters = new ArrayList<>();
        
        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));

        return parameters;
    }

}
