/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicAction;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.abstraction.AbstractAction;
import ai.abstraction.Attack;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.Random;
import rts.GameState;
import rts.PlayerAction;
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
        while (hasUnitsStopped(game, player, currentPlayerAction)) {
            //pick one ally unit to set the action 
            Unit unAlly = getUnitAlly(game, currentPlayerAction, player);
            //pick one enemy unit to set the action
            Unit targetEnemy = getTargetEnemyUnit(game, currentPlayerAction, player);
            if (game.getActionAssignment(unAlly) == null) {
                AbstractAction action = new Attack(unAlly, targetEnemy, pf);

                UnitAction uAct = action.execute(game);

                if (uAct.getType() == 5 || uAct.getType() == 1) {
                    currentPlayerAction.addUnitAction(unAlly, uAct);
                }
            }
        }
        return currentPlayerAction;
    }

    private boolean hasUnitsStopped(GameState game, int player, PlayerAction currentPlayerAction) {
        for(Unit un : game.getUnits()){
            if(un.getPlayer() == player){
                if(currentPlayerAction.getAction(un) == null && 
                        game.getActionAssignment(un) == null){
                    return true;
                }
            }
        }
        
        return false;
    }

}
