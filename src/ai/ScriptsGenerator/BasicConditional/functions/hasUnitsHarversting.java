/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.BasicConditional.functions;

import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class hasUnitsHarversting extends AbstractConditionalFunction{

    @Override
    public boolean runFunction(List lParam1) {
        GameState game = (GameState) lParam1.get(0);
        int player = (int) lParam1.get(1);
        PlayerAction currentPlayerAction = (PlayerAction) lParam1.get(2);
        //PathFinding pf = (PathFinding) lParam1.get(3);
        //UnitTypeTable a_utt = (UnitTypeTable) lParam1.get(4);
        boolean eval = false;
        
        if (getAllyUnitsHarvesting(game, currentPlayerAction, player).size() > 0){
            return true;
        }
        
        return eval;
    }

     protected ArrayList<Unit> getAllyUnitsHarvesting(GameState game, PlayerAction currentPlayerAction, int player) {
        ArrayList<Unit> unitAllys = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            if(u.getPlayer() == player && currentPlayerAction.getAction(u)!=null){
            	if(currentPlayerAction.getAction(u).getType()==2 || currentPlayerAction.getAction(u).getType()==3 )
            		unitAllys.add(u);
            }
        }
        return unitAllys;
    }

    @Override
    public String toString() {
        return " ( hasUnitsHarversting ) ";
    }
     
    
}
