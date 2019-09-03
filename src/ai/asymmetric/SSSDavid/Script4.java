package ai.asymmetric.SSSDavid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Harvest;
import ai.abstraction.WorkerRush;
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

public class Script4 extends AbstractionLayerAID {

	 Random r = new Random();
	    protected UnitTypeTable utt;
	    UnitType workerType;
	    UnitType baseType;

	    // Strategy implemented by this class:
	    // If we have more than 1 "Worker": send the extra workers to attack to the nearest enemy unit
	    // If we have a base: train workers non-stop
	    // If we have a worker: do this if needed: build base, harvest resources
	    public Script4(UnitTypeTable a_utt) {
	        this(a_utt, new AStarPathFinding());
	    }

	        
	    public Script4(UnitTypeTable a_utt, PathFinding a_pf) {
	        super(a_pf);
	        reset(a_utt);
	    }
	    
	    public void reset() {
	    	super.reset();
	    }
	    
	    public void reset(UnitTypeTable a_utt)  
	    {
	        utt = a_utt;
	        if (utt!=null) {
	            workerType = utt.getUnitType("Worker");
	            baseType = utt.getUnitType("Base");
	        }
	    }   
	    
	    
	    public AbstractionLayerAID clone() {
	        return new Script4(utt);
	    }
	    
	    public PlayerAction getAction(int player, GameState gs) {
	        PhysicalGameState pgs = gs.getPhysicalGameState();
	        Player p = gs.getPlayer(player);
	        PlayerAction pa = new PlayerAction();
//	        System.out.println("LightRushAI for player " + player + " (cycle " + gs.getTime() + ")");
	        List<Unit> workers = new LinkedList<Unit>();  
	        // behavior of bases:

	        for(Unit u:pgs.getUnits()) {
	            if (u.getType()==baseType && 
	                u.getPlayer() == player ) {
	                baseBehavior(u,p,pgs);
	            }
	     
	            else if (u.getType().canAttack && !u.getType().canHarvest && 
	                u.getPlayer() == player) {
	                meleeUnitBehavior(u,p,gs);
	            }        
	        

	            else if (u.getType().canHarvest && 
	                u.getPlayer() == player) {
	                workers.add(u);
	            }        
	        }
	        workersBehavior(workers,p,gs);
	        
	                
	        return translateActions(player,gs);
	    }
	    
	    
	    public PlayerAction getAction(int player, GameState gs, List<Unit> Units, List<Unit> Units_aux, Information inf,
	    		HashMap<Unit, AbstractAction> act) {
	        PhysicalGameState pgs = gs.getPhysicalGameState();
	        Player p = gs.getPlayer(player);
	        actions = act;
	        
	        List<Unit> workers = new LinkedList<Unit>();
	        // behavior of bases:
	        for(Unit u: Units) {
	        
	            if (u.getType()==baseType && 
	            		gs.getActionAssignment(u) == null && 
	                u.getPlayer() == player ) {
	                baseBehavior(u,p,pgs);
	               // System.out.println("kkkkkkkkk");
	          
	            }

	        // behavior of melee units:	       
	            else   if (u.getType().canAttack && !u.getType().canHarvest  && gs.getActionAssignment(u) == null && 
	                u.getPlayer() == player) {
	                meleeUnitBehavior(u,p,gs);
	            }        

	        // behavior of workers:
	            else if (u.getType().canHarvest && 
	                u.getPlayer() == player) {
	                workers.add(u);
	            }        
	        }
	        
	        for(Unit u: Units_aux) {
	            if (u.getType()==baseType && gs.getActionAssignment(u) == null && 
	                u.getPlayer() == player ) {
	            	 
	                baseBehavior(u,p,pgs);
	            }

	        // behavior of melee units:	       
	            if (u.getType().canAttack && !u.getType().canHarvest  && gs.getActionAssignment(u) == null && 
	                u.getPlayer() == player) {
	                meleeUnitBehavior(u,p,gs);
	            }        

	        // behavior of workers:
	            if (u.getType().canHarvest && 
	                u.getPlayer() == player  ) {
	                workers.add(u);
	            }        
	        }
	        
	        workersBehavior(workers,p,gs);
	        
	       return null;
	     
	    }
	    
	    
	    
	    
	    
	    public void baseBehavior(Unit u,Player p, PhysicalGameState pgs) {
	        if (p.getResources()>=workerType.cost ) {
	        	
	        	train(u, workerType);
	        }
	    }
	    
	    public void meleeUnitBehavior(Unit u, Player p, GameState gs) {
	        PhysicalGameState pgs = gs.getPhysicalGameState();
	        Unit closestEnemy = null;
	        int closestDistance = 0;
	        for(Unit u2:pgs.getUnits()) {
	            if (u2.getPlayer()>=0 && u2.getPlayer()!=p.getID()) { 
	                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
	                if (closestEnemy==null || d<closestDistance) {
	                    closestEnemy = u2;
	                    closestDistance = d;
	                }
	            }
	        }
	        if (closestEnemy!=null) {
	            attack(u,closestEnemy);
	        }
	    }
	    
	    public void workersBehavior(List<Unit> workers,Player p, GameState gs) {
	        PhysicalGameState pgs = gs.getPhysicalGameState();
	        int nbases = 0;
	        int resourcesUsed = 0;
	        Unit harvestWorker = null;
	        List<Unit> freeWorkers = new LinkedList<Unit>();
	        freeWorkers.addAll(workers);
	        
	        if (workers.isEmpty()) return;
	        
	        for(Unit u2:pgs.getUnits()) {
	            if (u2.getType() == baseType && 
	                u2.getPlayer() == p.getID()) nbases++;
	        }
	        
	        List<Integer> reservedPositions = new LinkedList<Integer>();
	        if (nbases==0 && !freeWorkers.isEmpty()) {
	            // build a base:
	            if (p.getResources()>=baseType.cost + resourcesUsed) {
	                Unit u = freeWorkers.remove(0);
	                buildIfNotAlreadyBuilding(u,baseType,u.getX(),u.getY(),reservedPositions,p,pgs);
	                resourcesUsed+=baseType.cost;
	            }
	        }
	        
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
	            else meleeUnitBehavior(harvestWorker, p, gs);
	        }
	        
	        for(Unit u:freeWorkers) meleeUnitBehavior(u, p, gs);
	        
	    }
	    
	    
	    @Override
	    public List<ParameterSpecification> getParameters()
	    {
	        List<ParameterSpecification> parameters = new ArrayList<>();
	        
	        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));

	        return parameters;
	    }
	}
