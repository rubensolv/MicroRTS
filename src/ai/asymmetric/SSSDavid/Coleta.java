package ai.asymmetric.SSSDavid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ai.abstraction.AbstractAction;
import ai.abstraction.Harvest;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class Coleta extends AbstractionLayerAID {

	Random r = new Random();
    protected UnitTypeTable utt;
    UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType rangedType;
	

    public Coleta(UnitTypeTable a_utt) {
        this(a_utt, new AStarPathFinding());
    }


    public Coleta(UnitTypeTable a_utt, PathFinding a_pf) {
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
        return new Coleta(utt);
    }
    
    public PlayerAction getAction(int player, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);
//        System.out.println("LightRushAI for player " + player + " (cycle " + gs.getTime() + ")");
        List<Unit> workers = new LinkedList<Unit>();
        // behavior of bases:
        for (Unit u : pgs.getUnits()) {
            
        	if (u.getType().canHarvest
                    && u.getPlayer() == player) {
        		coleta(u,p,pgs);
            }
        
           
        }

        return translateActions(player, gs);
    }
    
    
    public PlayerAction getAction(int player, GameState gs, List<Unit> Units,List<Unit> Units_aux, Information inf,
			HashMap<Unit, AbstractAction> act) {
    		
		PhysicalGameState pgs = gs.getPhysicalGameState();
		Player p = gs.getPlayer(player);
		actions = act;
		//System.out.println("LightRushAI for player " + player + " (cycle " + gs.getTime() + ")");
		List<Unit> workers = new LinkedList<Unit>();
		// behavior of bases:
		for (Unit u : Units) {
			if (u.getType().canHarvest
                    && u.getPlayer() == player) {
				coleta(u,p,pgs);
            }
		}
		
		
		for (Unit u : Units_aux) {
		
			if (u.getType().canHarvest
                    && u.getPlayer() == player) {
				coleta(u,p,pgs);
            }
		
		}
		
		
		
		actions = null;
		return null;
}
    
    
    public void coleta(Unit u,Player p, PhysicalGameState pgs) {
        Unit closestBase = null;
        Unit closestResource = null;
        int closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType().isResource) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestResource == null || d < closestDistance) {
                    closestResource = u2;
                    closestDistance = d;
                }
            }
        }
        closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestBase == null || d < closestDistance) {
                    closestBase = u2;
                    closestDistance = d;
                }
            }
        }
        if (closestResource != null && closestBase != null) {
            AbstractAction aa = getAbstractAction(u);
            if (aa instanceof Harvest) {
                Harvest h_aa = (Harvest)aa;
                if (h_aa.getTarget() != closestResource || h_aa.getBase()!=closestBase) harvest(u, closestResource, closestBase);
            } else {
                harvest(u, closestResource, closestBase);
            }
        }
    
}

 
}
