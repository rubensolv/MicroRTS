/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.BasicConditional.functions;

import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;


/**
 *
 * @author rubens
 */
public class HaveQtdUnitsHarversting extends AbstractConditionalFunction{

    @Override
    public boolean runFunction(List lParam1) {
        GameState game = (GameState) lParam1.get(0);
        int player = (int) lParam1.get(1);
        PlayerAction currentPlayerAction = (PlayerAction) lParam1.get(2);
        //PathFinding pf = (PathFinding) lParam1.get(3);
        //UnitTypeTable a_utt = (UnitTypeTable) lParam1.get(4);
        QuantityParam qtd = (QuantityParam) lParam1.get(5);
        
        if (getAllyUnitsHarvesting(game, currentPlayerAction, player).size() >= qtd.getQuantity()){
            return true;
        }
        
        return false;
    }

     protected ArrayList<Unit> getAllyUnitsHarvesting(GameState game, PlayerAction currentPlayerAction, int player) {
    	 HarvestBasic hb =new HarvestBasic(); 
    	 HashSet<Long> unitsID = hb.unitsID;
    	ArrayList<Unit> unitAllys = new ArrayList<>();
        for (Unit u : game.getUnits()) {
            //if(u.getPlayer() == player && currentPlayerAction.getAction(u)!=null){
            	if(unitsID.contains(u.getID()))
            		unitAllys.add(u);
            //}
        }
        return unitAllys;
    }

    @Override
    public String toString() {
        return "HaveQtdUnitsHarversting";
    }
     
    
}
