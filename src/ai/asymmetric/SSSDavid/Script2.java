package ai.asymmetric.SSSDavid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Harvest;
import ai.abstraction.LightRush;
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
//LightRush
public class Script2 extends AbstractionLayerAID {

	Random r = new Random();
    protected UnitTypeTable utt;
    UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType lightType;

    // Strategy implemented by this class:
    // If we have any "light": send it to attack to the nearest enemy unit
    // If we have a base: train worker until we have 1 workers
    // If we have a barracks: train light
    // If we have a worker: do this if needed: build base, build barracks, harvest resources

    public Script2(UnitTypeTable a_utt) {
        this(a_utt, new AStarPathFinding());
    }
    
    
    public Script2(UnitTypeTable a_utt, PathFinding a_pf) {
        super(a_pf);
        reset(a_utt);
    }

    public void reset() {
    	super.reset();
    }
    
    public void reset(UnitTypeTable a_utt)  
    {
        utt = a_utt;
        workerType = utt.getUnitType("Worker");
        baseType = utt.getUnitType("Base");
        barracksType = utt.getUnitType("Barracks");
        lightType = utt.getUnitType("Light");
    }   
    

    public AbstractionLayerAID clone() {
        return new Script2(utt);
    }

    /*
        This is the main function of the AI. It is called at each game cycle with the most up to date game state and
        returns which actions the AI wants to execute in this cycle.
        The input parameters are:
        - player: the player that the AI controls (0 or 1)
        - gs: the current game state
        This method returns the actions to be sent to each of the units in the gamestate controlled by the player,
        packaged as a PlayerAction.
     */
    public PlayerAction getAction(int player, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);
//        System.out.println("LightRushAI for player " + player + " (cycle " + gs.getTime() + ")");
        List<Unit> workers = new LinkedList<Unit>();
        // behavior of bases:
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == baseType
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null) {
                baseBehavior(u, p, pgs);
            }
            else if (u.getType() == barracksType
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null) {
                barracksBehavior(u, p, pgs);
            }
            else   if (u.getType().canAttack && !u.getType().canHarvest
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null) {
                meleeUnitBehavior(u, p, gs);
            }
            else  if (u.getType().canHarvest
                    && u.getPlayer() == player) {
                workers.add(u);
            }
        }
     //   workersBehavior(workers, p, gs);

        // This method simply takes all the unit actions executed so far, and packages them into a PlayerAction
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
            if (u.getType() == baseType && gs.getActionAssignment(u) == null
                    && u.getPlayer() == player) {
                baseBehavior(u, p, pgs);
            }

        // behavior of barracks:
  
            if (u.getType() == barracksType && gs.getActionAssignment(u) == null
                    && u.getPlayer() == player) {
                barracksBehavior(u, p, pgs);
            }
        
        // behavior of melee units:
       
            if (u.getType().canAttack && !u.getType().canHarvest && gs.getActionAssignment(u) == null
                    && u.getPlayer() == player) {
                meleeUnitBehavior(u, p, gs);
            }
        
        // behavior of workers:
        
       
            if (u.getType().canHarvest 
                    && u.getPlayer() == player) {
                workers.add(u);
            }
        }
        
        
        for (Unit u : Units_aux ) {
            if (u.getType() == baseType && gs.getActionAssignment(u) == null
                    && u.getPlayer() == player) {
                baseBehavior(u, p, pgs);
            }

        // behavior of barracks:
  
            if (u.getType() == barracksType && gs.getActionAssignment(u) == null
                    && u.getPlayer() == player) {
                barracksBehavior(u, p, pgs);
            }
        
        // behavior of melee units:
       
            if (u.getType().canAttack && !u.getType().canHarvest && gs.getActionAssignment(u) == null
                    && u.getPlayer() == player) {
                meleeUnitBehavior(u, p, gs);
            }
        
        // behavior of workers:
        
       
            if (u.getType().canHarvest 
                    && u.getPlayer() == player) {
                workers.add(u);
            }
        }
        
        workersBehavior(workers, p, gs,inf);
        return null;
    }
    
    
    
    
    
    public void baseBehavior(Unit u, Player p, PhysicalGameState pgs) {
        int nworkers = 0;
        
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType() == workerType
                    && u2.getPlayer() == p.getID()) {
                nworkers++;
            }
        }
        if (nworkers < 1 && p.getResources() >= workerType.cost) {
            train(u, workerType);
        }
    }

    public void barracksBehavior(Unit u, Player p, PhysicalGameState pgs) {
        if (p.getResources() >= lightType.cost) {
            train(u, lightType);
        }
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

    public void workersBehavior(List<Unit> workers, Player p, GameState gs,Information inf) {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
        int nbases =  inf.contruindo_base;
        int nbarracks = inf.contruindo_barraca;;
       
        int resourcesUsed = 0;
        
        List<Unit> freeWorkers = new LinkedList<Unit>();
        freeWorkers.addAll(workers);
        
        if (workers.isEmpty()) {
            return;
        }
       
      int sobra = 20;
        int basex=-1;
        int basey=-1;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType() == baseType
                    && u2.getPlayer() == p.getID()) {
                nbases++;
                basex = u2.getX();
                basey= u2.getY();
            }
            if (u2.getType() == barracksType
                    && u2.getPlayer() == p.getID()) {
                nbarracks++;
            }
        }
       if(nbases - inf.contruindo_base > 1)sobra=5;
       else if(inf.contruindo_base>0 && gs.getTime() > 300)sobra=10;
        if (p.getResources()>sobra  )nbarracks--;;
        
        List<Integer> reservedPositions = new LinkedList<Integer>();
        if (nbases == 0 && !freeWorkers.isEmpty()) {
            // build a base:
            if (p.getResources() >= baseType.cost + resourcesUsed) {
                Unit u = freeWorkers.remove(0);
                buildIfNotAlreadyBuilding(u,baseType,u.getX(),u.getY(),reservedPositions,p,pgs);
                resourcesUsed += baseType.cost;
                inf.contruindo_base++;
              
            }
        }

        if (nbarracks <= 0) {
            // build a barracks:
            if (p.getResources() >= barracksType.cost + resourcesUsed && !freeWorkers.isEmpty()) {
                Unit u = freeWorkers.remove(0);
                if(basex!=-1)  buildIfNotAlreadyBuilding(u,barracksType,basex+2-4*u.getPlayer(),basey+2-4*u.getPlayer(),reservedPositions,p,pgs);
                else buildIfNotAlreadyBuilding(u,barracksType,u.getX(),u.getY(),reservedPositions,p,pgs);
                resourcesUsed += barracksType.cost;
                inf.contruindo_barraca++;
                
            }
        }


        // harvest with all the free workers:
        for (Unit u : freeWorkers) {
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

    
    @Override
    public List<ParameterSpecification> getParameters()
    {
        List<ParameterSpecification> parameters = new ArrayList<>();
        
        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));

        return parameters;
    }    
    
}
