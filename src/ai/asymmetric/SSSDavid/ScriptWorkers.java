package ai.asymmetric.SSSDavid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Harvest;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class ScriptWorkers extends AbstractionLayerAI {

	protected UnitTypeTable utt;
    UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType rangedType;
	
	public void workersBehavior(Unit base, List<Unit> workers,Player p, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
       
        
           
        if (p.getResources() >= workerType.cost && base != null ) {
            train(base, workerType);
        }
        
        
        
        
        List<Unit> freeWorkers = new LinkedList<Unit>();
        freeWorkers.addAll(workers);

        if (workers.isEmpty()) {
            return;
        }

       
		for(int i=0; i <1;i++) {
		        Unit harvestWorker = null;
		        
		if (freeWorkers.size()>0) harvestWorker = freeWorkers.remove(0);
		        
		        // harvest with the harvest worker:
		        if (harvestWorker!=null) {
		            Unit closestBase = null;
		            Unit closestResource = null;
		            int closestDistance = 0;
		            for(Unit u2:pgs.getUnits()) {
		                if (u2.getType().isResource) { 
		                    int d = Math.abs(u2.getX() - harvestWorker.getX()) + Math.abs(u2.getY() - harvestWorker.getY());
		                    if (closestResource==null || d<closestDistance) {
		                        closestResource = u2;
		                        closestDistance = d;
		                    }
		                }
		            }
		            closestDistance = 0;
		            for(Unit u2:pgs.getUnits()) {
		                if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) { 
		                    int d = Math.abs(u2.getX() - harvestWorker.getX()) + Math.abs(u2.getY() - harvestWorker.getY());
		                    if (closestBase==null || d<closestDistance) {
		                        closestBase = u2;
		                        closestDistance = d;
		                    }
		                }
		            }
		            if (closestResource!=null && closestBase!=null) {
		                AbstractAction aa = getAbstractAction(harvestWorker);
		                if (aa instanceof Harvest) {
		                    Harvest h_aa = (Harvest)aa;
		                    if (h_aa.getTarget() != closestResource || h_aa.getBase()!=closestBase) harvest(harvestWorker, closestResource, closestBase);
		                } else {
		                    harvest(harvestWorker, closestResource, closestBase);
		                }
		            }
		        }
		}
        for(Unit u:freeWorkers) meleeUnitBehavior(u, p, gs);
        
        
    }
	
	public void meleeUnitBehavior(Unit u, Player p, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestEnemy = null;
        int closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestEnemy == null || d < closestDistance) {
                    closestEnemy = u2;
                    closestDistance = d;
                }
            }
        }
        if (closestEnemy != null) {
//            System.out.println("LightRushAI.meleeUnitBehavior: " + u + " attacks " + closestEnemy);
            attack(u, closestEnemy);
        }
	}
	
	public ScriptWorkers(UnitTypeTable a_utt) {
        this(a_utt, new AStarPathFinding());
    }
	
	public ScriptWorkers(UnitTypeTable a_utt, PathFinding a_pf) {
        super(a_pf);
        utt = a_utt;
        workerType = utt.getUnitType("Worker");
        baseType = utt.getUnitType("Base");
        barracksType = utt.getUnitType("Barracks");
        rangedType = utt.getUnitType("Ranged");
    }
	
	public ScriptWorkers(PathFinding a_pf) {
		super(a_pf);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PlayerAction getAction(int player, GameState gs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public PlayerAction getAction(PlayerAction pa,Unit base,List<Unit> workers, int player, GameState gs) throws Exception {
		Player p = gs.getPlayer(player);
		workersBehavior(base,workers, p, gs);
		
        return translateActions( player, gs);
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
