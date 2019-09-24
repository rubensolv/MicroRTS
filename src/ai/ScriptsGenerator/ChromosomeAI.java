/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator;

import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.BuildBasic;
import ai.ScriptsGenerator.Command.BasicAction.TrainBasic;
import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToCoordinatesBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToUnitBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.FunctionGPCompiler;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.CoordinatesParam;
import ai.ScriptsGenerator.ParametersConcrete.FarthestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.LessHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.MostHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.PlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.StrongestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.WeakestEnemy;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class ChromosomeAI extends AI{

    List<ICommand> commands = new ArrayList<>();
    UnitTypeTable utt;
    String name;
    String originalGrammar;
    ICompiler compiler = new MainGPCompiler();
    public HashSet<String> usedCommands;

    public ChromosomeAI(UnitTypeTable utt) {
        this.utt = utt;

    }
    
    public ChromosomeAI(UnitTypeTable utt, List<ICommand> commands, String name, String originalGrammar, HashSet<String> usedCommands) {
        this.utt = utt;
        this.commands = commands;
        this.name = name;
        this.originalGrammar=originalGrammar;
        this.usedCommands=usedCommands;
    }

    public PlayerAction getAction(int player, GameState gs) {
        PlayerAction currentActions = new PlayerAction();
        PathFinding pf = new AStarPathFinding();        

        for (ICommand command : commands) {
            currentActions = command.getAction(gs, player, currentActions, pf, utt, usedCommands);
        }
        currentActions = fillWithWait(currentActions, player, gs, utt);
        return currentActions;
    }

    @Override
    public void reset() {
        
    }

    @Override
    public AI clone() {
        return buildCommandsIA(utt, this.originalGrammar);
    }
    
    private AI buildCommandsIA(UnitTypeTable utt, String code) {
    	FunctionGPCompiler.counterCommands=0;
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        HashSet<String> usedCommands=new HashSet<String>();
        AI aiscript = new ChromosomeAI(utt, commandsGP, "P1",code, usedCommands);
        return aiscript;
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        List<ParameterSpecification> list = new ArrayList<>();
        return list;
    }

    @Override
    public String toString() {
    	String nameCommand="";
    	for (Iterator iterator = commands.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			nameCommand+=iCommand.toString();
			
		}
        return "ChromosomeAI_"+ name+" "+nameCommand;
    }

    private PlayerAction fillWithWait(PlayerAction currentActions, int player, GameState gs, UnitTypeTable utt) {
        currentActions.fillWithNones(gs, player, 10);
        return currentActions;
    }
    
    
}
