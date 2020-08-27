/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicAction;

import ai.ScriptsGenerator.Command.AbstractBasicAction;
import ai.ScriptsGenerator.Command.Enumerators.EnumPlayerTarget;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.CommandInterfaces.IUnitCommand;
import ai.ScriptsGenerator.IParameters.IBehavior;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.PlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.AbstractAction;
import ai.abstraction.Attack;
import ai.abstraction.Move;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
 * @author rubens & Julian
 */
public class MoveAwayBasic extends AbstractBasicAction implements IUnitCommand {

    boolean needUnit = false;
    String originalPieceGrammar;
    String originalPieceGrammarWord;

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt, HashSet<String> usedCommands, HashMap<Long, String> counterByFunction) {
    	
    	ResourceUsage resources = new ResourceUsage();
        PhysicalGameState pgs = game.getPhysicalGameState();
        //update variable resources
        resources = getResourcesUsed(currentPlayerAction, pgs);
        for (Unit unAlly : getPotentialUnits(game, currentPlayerAction, player)) {

            //pick one enemy unit to set the action
            Unit targetEnemy = farthestAllyBase(pgs, player, unAlly);

            if (game.getActionAssignment(unAlly) == null && unAlly != null && targetEnemy != null) {

                UnitAction uAct = null;
                UnitAction move = pf.findPathToAdjacentPosition(unAlly, targetEnemy.getX() + targetEnemy.getY() * pgs.getWidth(), game, resources);
                if (move != null && game.isUnitActionAllowed(unAlly, move));
                uAct = move;

                if (uAct != null && (uAct.getType() == 5 || uAct.getType() == 1)) {
                	usedCommands.add(getOriginalPieceGrammar());
                	if(counterByFunction.containsKey(unAlly.getID()))
                	{
                		if(!counterByFunction.get(unAlly.getID()).equals("moveAway"))
                			counterByFunction.put(unAlly.getID(), "moveAway");
                	}
                	else
                	{
                		counterByFunction.put(unAlly.getID(), "moveAway");
                	}
                    currentPlayerAction.addUnitAction(unAlly, uAct);
                    resources.merge(uAct.resourceUsage(unAlly, pgs));
                }

//                if(move==null)
//                {
//                	uAct=wait;
//                }
            }
        }
        return currentPlayerAction;
    }

    public Unit farthestAllyBase(PhysicalGameState pgs, int player, Unit unitAlly) {

        Unit farthestBase = null;
        int farthesttDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType().name == "Base") {

                if (u2.getPlayer() >= 0 && u2.getPlayer() == player) {
                    int d = Math.abs(u2.getX() - unitAlly.getX()) + Math.abs(u2.getY() - unitAlly.getY());
                    if (farthestBase == null || d > farthesttDistance) {
                        farthestBase = u2;
                        farthesttDistance = d;
                    }
                }
            }
        }
        return farthestBase;
    }

    @Override
    public String toString() {
        String listParam = "Params:{";
        for (IParameters parameter : getParameters()) {
            listParam += parameter.toString() + ",";
        }
        //remove the last comma.
        listParam = listParam.substring(0, listParam.lastIndexOf(","));
        listParam += "}";

        return "{MoveToUnitBasic:{" + listParam + "}}";
    }

    public void setUnitIsNecessary() {
        this.needUnit = true;
    }

    public void setUnitIsNotNecessary() {
        this.needUnit = false;
    }

    @Override
    public Boolean isNecessaryUnit() {
        return needUnit;
    }

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt, Unit unAlly, HashSet<String> usedCommands, HashMap<Long, String> counterByFunction) {
    	//usedCommands.add(getOriginalPieceGrammar()+")");
    	if(unAlly != null && currentPlayerAction.getAction(unAlly) != null &&
                unAlly.getPlayer() != player){
            return currentPlayerAction ;
        }
        ResourceUsage resources = new ResourceUsage();
        PhysicalGameState pgs = game.getPhysicalGameState();
        //update variable resources
        resources = getResourcesUsed(currentPlayerAction, pgs);

        //pick one enemy unit to set the action
        Unit targetEnemy = farthestAllyBase(pgs, player, unAlly);

        if (game.getActionAssignment(unAlly) == null && unAlly != null && targetEnemy != null && hasInPotentialUnits(game, currentPlayerAction, unAlly,player)) {

            UnitAction uAct = null;
            UnitAction move = pf.findPathToPositionInRange(unAlly, targetEnemy.getX() + targetEnemy.getY() * pgs.getWidth(), unAlly.getAttackRange(), game, resources);
            if (move != null && game.isUnitActionAllowed(unAlly, move));
            uAct = move;

            if (uAct != null && (uAct.getType() == 5 || uAct.getType() == 1)) {
            	usedCommands.add(getOriginalPieceGrammar());
            	if(counterByFunction.containsKey(unAlly.getID()))
            	{
            		if(!counterByFunction.get(unAlly.getID()).equals("moveAway"))
            			counterByFunction.put(unAlly.getID(), "moveAway");
            	}
            	else
            	{
            		counterByFunction.put(unAlly.getID(), "moveAway");
            	}
                currentPlayerAction.addUnitAction(unAlly, uAct);
                resources.merge(uAct.resourceUsage(unAlly, pgs));
            }

//                if(move==null)
//                {
//                	uAct=wait;
//                }
        }

        return currentPlayerAction;
    }
    
	/**
	 * @return the originalPieceGrammar
	 */
	public String getOriginalPieceGrammar() {
		return originalPieceGrammar;
	}

	/**
	 * @param originalPieceGrammar the originalPieceGrammar to set
	 */
	public void setOriginalPieceGrammar(String originalPieceGrammar) {
		this.originalPieceGrammar = originalPieceGrammar;
	}

	public String getOriginalPieceGrammarWord() {
		return originalPieceGrammarWord;
	}

	public void setOriginalPieceGrammarWord(String originalPieceGrammarWord) {
		this.originalPieceGrammarWord = originalPieceGrammarWord;
	}
}
