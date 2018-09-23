/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicAction;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.IParameters.IQuantity;
import ai.ScriptsGenerator.ParametersConcrete.ConstructionTypeParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.AbstractAction;
import ai.abstraction.Train;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;
import util.Pair;

/**
 *
 * @author rubens
 */
public class TrainBasic extends AbstractBasicAction {

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt) {
        if (game.getPlayer(player).getResources()>0
                && limitReached(game, player, currentPlayerAction)) {
            //get units basead in type to produce
            List<Unit> unitToBuild = getUnitsToBuild(game, player);

            //produce the type of unit in param
            for (Unit unit : unitToBuild) {
                if (game.getActionAssignment(unit) == null) {
                    UnitAction unTemp = translateUnitAction(game, a_utt, unit);
                    currentPlayerAction.addUnitAction(unit, unTemp);
                }
            }
        }
        return currentPlayerAction;
    }

    private List<Unit> getUnitsToBuild(GameState game, int player) {
        List<Unit> units = new ArrayList<>();
        //pick the units basead in the types
        List<ConstructionTypeParam> types = getTypeBuildFromParam();

        for (Unit un : game.getUnits()) {
            if (un.getPlayer() == player) {
                if (unitIsType(un, types)) {
                    units.add(un);
                }
            }
        }

        return units;
    }

    private boolean unitIsType(Unit un, List<ConstructionTypeParam> types) {
        for (ConstructionTypeParam type : types) {
            if (type.getParamTypes().contains(EnumTypeUnits.byName(un.getType().name))) {
                return true;
            }
        }

        return false;
    }

    private UnitAction translateUnitAction(GameState game, UnitTypeTable a_utt, Unit unit) {
        List<UnitTypeParam> types = getTypeUnitFromParam();

        for (UnitTypeParam type : types) {
            for (EnumTypeUnits en : type.getParamTypes()) {
                AbstractAction action = new Train(unit, a_utt.getUnitType(en.code()));
                UnitAction uAct = action.execute(game);
                if (uAct != null && uAct.getType() == 4) {
                    return uAct;
                }
            }
        }

        return null;
    }

    private boolean limitReached(GameState game, int player, PlayerAction currentPlayerAction) {
        IQuantity qtt = getQuantityFromParam();
        
        //verify if the quantity of units associated with the specific type were reached.
        if(qtt.getQuantity() <= getQuantityUnitsBuilded(game, player, currentPlayerAction)){
            return false;
        }
        return true;
    }

    private int getQuantityUnitsBuilded(GameState game, int player, PlayerAction currentPlayerAction) {
        int ret = 0;
        HashSet<EnumTypeUnits> types = new HashSet<>();
        //get types in EnumTypeUnits
        for(IParameters param : getParameters()){
            if(param instanceof UnitTypeParam){
                types.addAll(((UnitTypeParam) param).getParamTypes());
            }
        }
        //count
        for (EnumTypeUnits type : types) {
            ret += countUnitsByType(game, player, currentPlayerAction, type);
        }
        
        return ret;
    }

    private int countUnitsByType(GameState game, int player, PlayerAction currentPlayerAction, EnumTypeUnits type) {
        int qtt = 0;
        
        //count units in state
        for (Unit unit : game.getUnits()) {
            if(unit.getPlayer() == player && unit.getType().ID == type.code() ){
                qtt++;
            }
        }
        // count units in currentPlayerAction 
        for (Pair<Unit, UnitAction> action : currentPlayerAction.getActions()) {
            if(action.m_b.getUnitType().ID == type.code()){
                qtt++;
            }
        }
        
        
        return qtt;
    }
    
    

}
