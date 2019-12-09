/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.BasicConditional.functions;

import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;

/**
 *
 * @author rubens
 */
public abstract class AbstractConditionalFunction implements IConditionalFunction{
    protected List<IParameters> parameters = new ArrayList<>();
    
    protected List<IParameters> getParameters() {
        return parameters;
    }
    
    protected Iterable<Unit> getPotentialUnits(GameState game, PlayerAction currentPlayerAction, int player) {
        ArrayList<Unit> unitAllys = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            if(u.getPlayer() == player && currentPlayerAction.getAction(u) == null 
                    && game.getActionAssignment(u) == null && u.getResources() == 0
                    && isUnitControlledByParam(u)){
                unitAllys.add(u);
            }
        }
        return unitAllys;
    }
    
    protected Iterable<Unit> getPotentialUnitsSimpleWay(GameState game, PlayerAction currentPlayerAction, int player) {
        ArrayList<Unit> unitAllys = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            if(u.getPlayer() == player && isUnitControlledByParam(u)){
                unitAllys.add(u);
            }
        }
        return unitAllys;
    }
    
    protected boolean isUnitControlledByParam(Unit u) {
        List<UnitTypeParam> unType = getTypeUnitFromParam();
        for (UnitTypeParam unitTypeParam : unType) {
        	
            for (EnumTypeUnits paramType : unitTypeParam.getParamTypes()) {
                if(u.getType().ID == paramType.code()){
                    return true;
                }
            }
        }
        return false;
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
  
    
    protected boolean hasUnitInParam(List lParam1) {
        for (Object object : lParam1) {
            if(object instanceof Unit){
                return true;
            }
        }
        return false;
    }
    
    protected Unit getUnitFromParam(List lParam1) {
        for (Object object : lParam1) {
            if(object instanceof Unit){
                return (Unit) object;
            }
        }
        
        return null;
    }
    
    protected int getNumberUnitsDoingAction(String action, HashMap<Long, String> counterByFunction, GameState game, PlayerAction currentPlayerAction) {
    	int counterUnits=0;
    	HashMap<Long, String> counterByFunctionNew = new HashMap<Long,String>(counterByFunction);
    	Iterator it = counterByFunction.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		if(pair.getValue().equals(action))
    		{
    			if(getUnitById(game, currentPlayerAction, (Long)pair.getKey(), counterByFunction))
    					counterUnits++;
    		}
    		else
    		{
    			counterByFunctionNew.remove((Long)pair.getKey());
    		}
    	}
    	counterByFunction=counterByFunctionNew;
    	return counterUnits;
    }
    
    protected boolean getUnitById(GameState game, PlayerAction currentPlayerAction, Long idUnit, HashMap<Long, String> counterByFunction)
    {
        for (Unit u : game.getUnits()) {
            if(currentPlayerAction.getAction(u) == null && game.getActionAssignment(u) == null 
            		 && u.getID()==idUnit){            	
                return false;
            }
        }
        return true;
    }
}
