/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicAction;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.ParametersConcrete.ConstructionTypeParam;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.AbstractAction;
import ai.abstraction.Train;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class BuildBasic extends AbstractBasicAction {

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt) {
        //get units basead in type to produce
        List<Unit> unitToBuild = getUnitsToBuild(game, player);

        //produce the type of unit in param
        for (Unit unit : unitToBuild) {
            if (game.getActionAssignment(unit) == null) {
                UnitAction unTemp = translateUnitAction(game, a_utt, unit);
                currentPlayerAction.addUnitAction(unit, unTemp);
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
                if (uAct.getType() == 4) {
                    return uAct;
                }
            }
        }

        return null;
    }

}
