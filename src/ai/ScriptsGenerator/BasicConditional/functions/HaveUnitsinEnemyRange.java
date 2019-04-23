/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.BasicConditional.functions;

import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import java.util.List;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.Unit;

/**
 *
 * @author rubens
 */
public class HaveUnitsinEnemyRange extends AbstractConditionalFunction {

    @Override
    public boolean runFunction(List lParam1) {
        GameState game = (GameState) lParam1.get(0);
        int player = (int) lParam1.get(1);
        PlayerAction currentPlayerAction = (PlayerAction) lParam1.get(2);
        //PathFinding pf = (PathFinding) lParam1.get(3);
        //UnitTypeTable a_utt = (UnitTypeTable) lParam1.get(4);
        UnitTypeParam unitType = (UnitTypeParam) lParam1.get(5);
        parameters.add(unitType);

        PhysicalGameState pgs = game.getPhysicalGameState();

        //now whe iterate for all ally units in order to discover wich one satisfy the condition
        for (Unit unAlly : getPotentialUnits(game, currentPlayerAction, player)) {
            if (currentPlayerAction.getAction(unAlly) == null) {

                for (Unit u2 : pgs.getUnits()) {

                    if (u2.getPlayer() >= 0 && u2.getPlayer() != player) {

                        int dx = u2.getX() - unAlly.getX();
                        int dy = u2.getY() - unAlly.getY();
                        double d = Math.sqrt(dx * dx + dy * dy);

                        //If satisfies, an action is applied to that unit. Units that not satisfies will be set with
                        // an action wait.
                        if ((d <= u2.getAttackRange())) {
                            return true;
                        }
                    }

                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "HaveUnitsinEnemyRange";
    }

}
