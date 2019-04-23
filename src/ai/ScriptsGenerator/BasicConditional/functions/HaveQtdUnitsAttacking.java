/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.BasicConditional.functions;

import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;

import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;


/**
 *
 * @author rubens
 */
public class HaveQtdUnitsAttacking extends AbstractConditionalFunction{

    @Override
    public boolean runFunction(List lParam1) {
        GameState game = (GameState) lParam1.get(0);
        int player = (int) lParam1.get(1);
        PlayerAction currentPlayerAction = (PlayerAction) lParam1.get(2);
        //PathFinding pf = (PathFinding) lParam1.get(3);
        //UnitTypeTable a_utt = (UnitTypeTable) lParam1.get(4);
        QuantityParam qtd = (QuantityParam) lParam1.get(5);
        UnitTypeParam unitType = (UnitTypeParam) lParam1.get(6);
        parameters.add(unitType);
        
        if (getAllyUnitsAttacking(game, currentPlayerAction, player).size() >= qtd.getQuantity()){
            return true;
        }
        
        return false;
    }

    protected ArrayList<Unit> getAllyUnitsAttacking(GameState game, PlayerAction currentPlayerAction, int player) {
        ArrayList<Unit> unitAllys = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            if(u.getPlayer() == player && isUnitControlledByParam(u) && currentPlayerAction.getAction(u)!=null){
            	if(currentPlayerAction.getAction(u).getType()==5)
            		unitAllys.add(u);
            }
        }
        return unitAllys;
    }

    @Override
    public String toString() {
        return "HaveQtdUnitsAttacking";
    }
     
    
}
