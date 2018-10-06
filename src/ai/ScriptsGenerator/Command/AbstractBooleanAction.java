/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command;

import java.util.ArrayList;
import java.util.List;

import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToUnitBasic;
import ai.ScriptsGenerator.Command.BasicAction.TrainBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.IPlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.LessHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.UnitTypeParam;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;
import util.Pair;

/**
 *
 * @author rubens Julian
 */
public abstract class AbstractBooleanAction extends AbstractCommand {
	
	protected AttackBasic attack;
	
    private List<IParameters> parameters;
    List<ICommand> commands = new ArrayList<>();
    
    UnitTypeTable utt;

    public AbstractBooleanAction() {
        this.parameters = new ArrayList<>();
    }
    
    public List<IParameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<IParameters> parameters) {
        this.parameters = parameters;
    }
    
    public void addParameter(IParameters param){
        this.parameters.add(param);
    }
	
    protected ResourceUsage getResourcesUsed(PlayerAction currentPlayerAction, PhysicalGameState pgs) {
        ResourceUsage res = new ResourceUsage();
        for (Pair<Unit, UnitAction> action : currentPlayerAction.getActions()) {
            if(action.m_a != null && action.m_b != null){
                res.merge(action.m_b.resourceUsage(action.m_a, pgs));
            }
        }
        return res;
    }
    
    protected List<UnitTypeParam> getTypeUnitFromParam() {
        List<UnitTypeParam> types = new ArrayList<>();
        for (IParameters param : getParameters()) {
            if(param instanceof UnitTypeParam){
                types.add((UnitTypeParam) param);
            }
        }
        return types;
    }
    
    protected void ActionsInstantation() {
    	this.attack = new AttackBasic();
        attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
        attack.addParameter(new IPlayerTargetParam(0)); //add player target
        attack.addParameter(new ClosestEnemy()); //add behavior
        commands.add(attack);
 
    }
    
    public void appendCommands(int player, GameState gs, PlayerAction currentActions) {
        PathFinding pf = new AStarPathFinding();

        //simulate one WR
        for (ICommand command : commands) {
            currentActions = command.getAction(gs, player, currentActions, pf, utt);
        }

    }
    
}
