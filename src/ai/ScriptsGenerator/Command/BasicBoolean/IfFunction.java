/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command.BasicBoolean;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.BasicConditional.SimpleConditional;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.CommandInterfaces.IUnitCommand;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class IfFunction implements ICommand, IUnitCommand {

    protected SimpleConditional conditional;
    protected List<ICommand> commandsThen = new ArrayList<>();
    protected List<ICommand> commandsElse = new ArrayList<>();

    public void setConditional(SimpleConditional conditional) {
        this.conditional = conditional;
    }
    
    public SimpleConditional getConditional() {
        return this.conditional;
    }

    public void setCommandsThen(List<ICommand> commandsThen) {
        this.commandsThen = commandsThen;
    }

    public void includeFullCommandsThen(List<ICommand> commandsThen) {
        this.commandsThen.addAll(commandsThen);
    }

    public void includeFullCommandsElse(List<ICommand> commandsElse) {
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
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt, HashSet<String> usedCommands, HashMap<Long, String> counterByFunction) {
        if (conditional.runConditional(game, player, currentPlayerAction, pf, a_utt,counterByFunction)) {
            for (ICommand command : commandsThen) {
            	
                currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
            }
        } else {
            if (!commandsElse.isEmpty()) {
                for (ICommand command : commandsElse) {                	
                    currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt,usedCommands,counterByFunction);
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

    @Override
    public Boolean isNecessaryUnit() {
        //look in the conditional if it needs a Unit
        if (conditional.isNecessaryUnit()) {
            return true;
        }
        for (ICommand iCommand : commandsThen) {
            if (iCommand instanceof IUnitCommand) {
                IUnitCommand iUnitCom = (IUnitCommand) iCommand;
                if (iUnitCom.isNecessaryUnit()) {
                    return true;
                }
            }

        }

        for (ICommand iCommand : commandsElse) {
            if (iCommand instanceof IUnitCommand) {
                IUnitCommand iUnitCom = (IUnitCommand) iCommand;
                if (iUnitCom.isNecessaryUnit()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public PlayerAction getAction(GameState game, int player, PlayerAction currentPlayerAction, PathFinding pf, UnitTypeTable a_utt, Unit u, HashSet<String> usedCommands, HashMap<Long, String> counterByFunction) {
        if (conditional.isNecessaryUnit()) {
            if (conditional.runConditional(game, player, currentPlayerAction, pf, a_utt, u, counterByFunction)) {
                for (ICommand command : commandsThen) {
                    //currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt);
                    if (command instanceof IUnitCommand) {
                        IUnitCommand tempUnit = (IUnitCommand) command;
                        if (tempUnit.isNecessaryUnit()) {
                            currentPlayerAction = tempUnit.getAction(game, player, currentPlayerAction, pf, a_utt, u, usedCommands,counterByFunction);
                        } else {
                            currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt,usedCommands,counterByFunction);
                        }
                    } else {
                        currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt,usedCommands,counterByFunction);
                    }
                }
            } else {            	
                if (!commandsElse.isEmpty()) {
                    for (ICommand command : commandsElse) {
                        if (command instanceof IUnitCommand) {
                            IUnitCommand tempUnit = (IUnitCommand) command;
                            if (tempUnit.isNecessaryUnit()) {
                                currentPlayerAction = tempUnit.getAction(game, player, currentPlayerAction, pf, a_utt, u, usedCommands,counterByFunction);
                            } else {
                                currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
                            }
                        } else {
                            currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
                        }

                    }
                }
            }
        } else {        	
            if (conditional.runConditional(game, player, currentPlayerAction, pf, a_utt, counterByFunction)) {            	
                for (ICommand command : commandsThen) {
                    //currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt);
                    if (command instanceof IUnitCommand) {
                        IUnitCommand tempUnit = (IUnitCommand) command;
                        if (tempUnit.isNecessaryUnit()) {
                            currentPlayerAction = tempUnit.getAction(game, player, currentPlayerAction, pf, a_utt, u, usedCommands,counterByFunction);
                        } else {
                            currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
                        }
                    } else {
                        currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
                    }
                }
            } else {
                if (!commandsElse.isEmpty()) {
                    for (ICommand command : commandsElse) {
                        if (command instanceof IUnitCommand) {
                            IUnitCommand tempUnit = (IUnitCommand) command;
                            if (tempUnit.isNecessaryUnit()) {
                                currentPlayerAction = tempUnit.getAction(game, player, currentPlayerAction, pf, a_utt, u, usedCommands,counterByFunction);
                            } else {
                                currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
                            }
                        } else {
                            currentPlayerAction = command.getAction(game, player, currentPlayerAction, pf, a_utt, usedCommands,counterByFunction);
                        }

                    }
                }
            }
        }
        return currentPlayerAction;
    }

}
