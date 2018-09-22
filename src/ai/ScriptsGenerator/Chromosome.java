/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator;

import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.BuildBasic;
import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
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
public class Chromosome {
    List<ICommand> commands = new ArrayList<>();
    UnitTypeTable utt;

    public Chromosome(UnitTypeTable utt) {
        this.utt = utt;
    }
    
    public PlayerAction getAction(int player, GameState gs) {
        PlayerAction currentActions = new PlayerAction();
        PathFinding pf  = new AStarPathFinding();
        //simulate one WR
        
        //build action
        BuildBasic build = new BuildBasic();
        build.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
        build.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
        build.addParameter(null); //add qtd unit
        currentActions = build.getAction(gs, player, currentActions, pf, utt );
        
        //harverst action
        HarvestBasic harverst = new HarvestBasic();
        harverst.addParameter(TypeConcrete.getTypeWorker()); //add unit type
        harverst.addParameter(null); //add qtd unit
        //currentActions = harverst.getAction(gs, player, currentActions, pf, utt);
        
        //attack action
        AttackBasic attack = new AttackBasic();
        attack.addParameter(null); //add unit type
        attack.addParameter(null); //add behavior
        currentActions = attack.getAction(gs, player, currentActions, pf, utt);
        
        return currentActions;
    }
}
