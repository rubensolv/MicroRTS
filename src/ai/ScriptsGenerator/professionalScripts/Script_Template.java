/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.professionalScripts;

import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens e julian
 */
public class Script_Template extends AbstractionLayerAI {

    protected UnitTypeTable utt;
    HashSet<String> usedCommands = new HashSet<>();
    HashMap<Long, String> counterByFunction=new HashMap<Long, String>();
    ICompiler compiler = new MainGPCompiler();

    public Script_Template(UnitTypeTable t_utt) {
        this(t_utt, new AStarPathFinding());
    }

    public Script_Template(UnitTypeTable t_utt, PathFinding a_pf) {
        super(a_pf);
        this.utt = t_utt;
    }

    public void reset() {
        super.reset();
    }

    public void reset(UnitTypeTable a_utt) {
        utt = a_utt;
    }

    @Override
    public AI clone() {
        return new Script_Template(utt, pf);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        List<ParameterSpecification> parameters = new ArrayList<>();
        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));
        return parameters;
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        PlayerAction pa = new PlayerAction();        
        pa = runCode(pa,"train(Light,20,Left)", player, gs);        
              
        return pa;
    }

    private PlayerAction runCode(PlayerAction pa, String code, int player, GameState gs) throws Exception {
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI script = new ChromosomeAI(utt, commandsGP, "", code, usedCommands,counterByFunction);
        return pa.merge(script.getAction(player, gs));
    }

}
