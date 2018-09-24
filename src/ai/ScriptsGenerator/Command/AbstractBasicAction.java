/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command;

import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.IParameters.IQuantity;
import ai.ScriptsGenerator.ParametersConcrete.ConstructionTypeParam;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitType;
import util.Pair;


/**
 *
 * @author rubens
 */
public abstract class AbstractBasicAction extends AbstractCommand{
    private List<IParameters> parameters;

    public AbstractBasicAction() {
        this.parameters = new ArrayList<>();
    }
    
    public List<IParameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<IParameters> parameters) {
        this.parameters = parameters;
    }
    
    public void addParameter(IParameters param){
        this.parameters.add(param);
    }
    
    protected Unit getUnitAlly(GameState game, PlayerAction currentPlayerAction, int player) {
        ArrayList<Unit> unitAllys = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            if(u.getPlayer() == player && currentPlayerAction.getAction(u) == null 
                    && game.getActionAssignment(u) == null && u.getResources() == 0){
                unitAllys.add(u);
            }
        }
        for (Unit unitAlly : unitAllys) {
            if(currentPlayerAction.getAction(unitAlly) == null){
                return unitAlly;
            }
        }
        
        return null;
    }

    protected Unit getTargetEnemyUnit(GameState game, PlayerAction currentPlayerAction, int player, Unit allyUnit) {
        IBehavior behavior = getBehavior();
        //verify if there are behavior param
        if(behavior != null){
            return getEnemybyBehavior(game, player, behavior, allyUnit);
        }else{
           return getEnemyRandomic(game, player);
        }
    }

    protected Unit getEnemyRandomic(GameState game, int player){
        int enemyPlayer = (1-player);
        ArrayList<Unit> units = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            if(u.getPlayer() == enemyPlayer){
                units.add(u);
            }
        }
        Random rand = new Random();
        try {
            return units.get(rand.nextInt(units.size()));
        } catch (Exception e) {
            return units.get(0);
        }
        
    }
    
     protected List<ConstructionTypeParam> getTypeBuildFromParam() {
        List<ConstructionTypeParam> types = new ArrayList<>();
        for (IParameters param : getParameters()) {
            if(param instanceof ConstructionTypeParam){
                types.add((ConstructionTypeParam) param);
            }
        }
        return types;
    }
    
    protected List<UnitTypeParam> getTypeUnitFromParam() {
        List<UnitTypeParam> types = new ArrayList<>();
        for (IParameters param : getParameters()) {
            if(param instanceof UnitTypeParam){
                types.add((UnitTypeParam) param);
            }
        }
        return types;
    }
    
    protected IQuantity getQuantityFromParam() {
        for(IParameters param : getParameters()){
            if(param instanceof IQuantity){
                return (IQuantity) param;
            }
        }
        return null;
    }

    private IBehavior getBehavior() {
        IBehavior beh = null;
        for (IParameters parameter : parameters) {
            if(parameter instanceof IBehavior){
                beh = (IBehavior) parameter;
            }
        }
        
        return beh;
    }

    private Unit getEnemybyBehavior(GameState game, int player, IBehavior behavior, Unit allyUnit) {
        
        return behavior.getEnemytByBehavior(game, player, allyUnit);
    }
    
    protected ResourceUsage getResourcesUsed(PlayerAction currentPlayerAction, PhysicalGameState pgs) {
        ResourceUsage res = new ResourceUsage();
        for (Pair<Unit, UnitAction> action : currentPlayerAction.getActions()) {
            res.merge(action.m_b.resourceUsage(action.m_a, pgs));
        }
        return res;
    }
    
}
