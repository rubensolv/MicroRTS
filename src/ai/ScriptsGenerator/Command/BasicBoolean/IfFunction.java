/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicBoolean;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class IfFunction implements ICommand {

    protected IConditional conditional;
    protected List<ICommand> commandsThen = new ArrayList<>();
    protected List<ICommand> commandsElse = new ArrayList<>();

    public void setConditional(IConditional conditional) {
        this.conditional = conditional;
    }

    public void setCommandsThen(List<ICommand> commandsThen) {
        this.commandsThen = commandsThen;
    }
    
    public void includeFullCommandsThen(List<ICommand> commandsThen){
        this.commandsThen.addAll(commandsThen);
    }
    public void includeFullCommandsElse(List<ICommand> commandsElse){
        this.commandsElse.addAll(commandsElse);
    }

    public void setCommandsElse(List<ICommand> commandsElse) {
        this.commandsElse = commandsElse;
    }

    public void addCommandsThen(ICommand commandThen) {
        this.commandsThen.add(commandThen);
    }

    public void addCommandsElse(ICommand commandElse) {
        this.commandsElse.add(commandElse);
    }

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt) {
        if (conditional.runConditional(game, player, currentPlayerAction, pf, a_utt)) {
            for (ICommand command : commandsThen) {
                currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt);
            }
        } else {
            if (!commandsElse.isEmpty()) {
                for (ICommand command : commandsThen) {
                    currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt);
                }
            }
        }

        return currentPlayerAction;
    }

    public String toString() {
        String listParam = conditional.toString();

        listParam += " Then:{";
        for (ICommand command : commandsThen) {
            listParam += command.toString() + ",";
        }
        //remove the last comma.
        listParam = listParam.substring(0, listParam.lastIndexOf(","));
        listParam += "}";

        if (!commandsElse.isEmpty()) {
            listParam += " Else:{";
            for (ICommand command : commandsElse) {
                listParam += command.toString() + ",";
            }
            //remove the last comma.
            listParam = listParam.substring(0, listParam.lastIndexOf(","));
            listParam += "}";
        }

        return "{If:{" + listParam + "}}";
    }

}