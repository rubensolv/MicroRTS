/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command;

import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.ConstructionTypeParam;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitType;


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
            if(u.getPlayer() == player){
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

    protected Unit getTargetEnemyUnit(GameState game, PlayerAction currentPlayerAction, int player) {
        IBehavior behavior = null;
        //verify if there are behavior param
        for (IParameters param : getParameters()) {
            if(param instanceof IBehavior){
                behavior = (IBehavior) param;
            }
        }
        
        if(behavior != null){
            return getEnemyRandomic(game, player);
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
    
    
}
