/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.BasicConditional.functions;

import ai.ScriptsGenerator.Command.Enumerators.EnumPlayerTarget;
import ai.ScriptsGenerator.ParametersConcrete.PlayerTargetParam;
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
public class HaveUnitsStrongest extends AbstractConditionalFunction {

    @Override
    public boolean runFunction(List lParam1) {
        GameState game = (GameState) lParam1.get(0);
        int player = (int) lParam1.get(1);
        PlayerAction currentPlayerAction = (PlayerAction) lParam1.get(2);
        //PathFinding pf = (PathFinding) lParam1.get(3);
        //UnitTypeTable a_utt = (UnitTypeTable) lParam1.get(4);
        UnitTypeParam unitType = (UnitTypeParam) lParam1.get(5);
//        PlayerTargetParam playerTargetP= (PlayerTargetParam) lParam1.get(6);
//        EnumPlayerTarget enumPlayer=playerTargetP.getSelectedPlayerTarget().get(0);
//        String playerT=enumPlayer.name();
//        int playerTarget=-1;
//        if(playerT=="Ally")
//        	playerTarget=player;
//        if(playerT=="Enemy")
//        	playerTarget=1-player;
        
        parameters.add(unitType);

        PhysicalGameState pgs = game.getPhysicalGameState();

        //now whe iterate for all ally units in order to discover wich one satisfy the condition
        for (Unit unAlly : getPotentialUnits(game, currentPlayerAction, player)) {
            if (currentPlayerAction.getAction(unAlly) == null) {

                for (Unit u2 : pgs.getUnits()) {

                    if (u2.getPlayer() >= 0 && u2.getPlayer() != player) {

                    	
                    	int damage = unAlly.getMaxDamage();
                    	int HP= u2.getHitPoints();
                    	
                    	if(damage>HP)
                    	{
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
