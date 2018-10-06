/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicAction;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.AbstractAction;
import ai.abstraction.Attack;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class AttackBasic extends AbstractBasicAction {

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt) {
        ResourceUsage resources = new ResourceUsage();
        PhysicalGameState pgs = game.getPhysicalGameState();
        //update variable resources
        resources = getResourcesUsed(currentPlayerAction, pgs);
        int playerTarget=getPlayerTargetFromParam().getPlayerTarget();
        for(Unit unAlly : getPotentialUnits(game, currentPlayerAction, player)){
             
             //pick one enemy unit to set the action
             Unit targetEnemy = getTargetEnemyUnit(game, currentPlayerAction, playerTarget, unAlly);  
            
            if (game.getActionAssignment(unAlly) == null && unAlly != null && targetEnemy != null) {
                AbstractAction action = new Attack(unAlly, targetEnemy, pf);

                UnitAction uAct = action.execute(game, resources);

                if (uAct != null && (uAct.getType() == 5 || uAct.getType() == 1)) {
                    currentPlayerAction.addUnitAction(unAlly, uAct);
                    resources.merge(uAct.resourceUsage(unAlly, pgs));
                }
            }
        }
        return currentPlayerAction;
    }

    private boolean hasUnitsStopped(GameState game, int player, PlayerAction currentPlayerAction) {
        for(Unit un : game.getUnits()){
            if(un.getPlayer() == player && un.getResources() == 0){
                if(currentPlayerAction.getAction(un) == null && 
                        game.getActionAssignment(un) == null){
                    return true;
                }
            }
        }
        
        return false;
    }

    private Iterable<Unit> getPotentialUnits(GameState game, PlayerAction currentPlayerAction, int player) {
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

    private boolean isUnitControlledByParam(Unit u) {
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

}
