/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.asymmetric.PGS;

import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.FunctionGPCompiler;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class botEmptyBase extends AbstractionLayerAI {

    ICompiler compiler = new MainGPCompiler();
    HashSet<String> usedCommands;
    AI ai;
    String identification;

    public botEmptyBase(UnitTypeTable utt, String sIA, String ident) {
        super(new AStarPathFinding());
        this.identification = ident;
        List<AI> scriptsRun1 = decodeScripts(utt, sIA);
        ai = scriptsRun1.get(0);
    }

    public List<AI> decodeScripts(UnitTypeTable utt, String sIA) {
        List<AI> scriptsAI = new ArrayList<>();
        scriptsAI.add(buildCommandsIA(utt, sIA));
        return scriptsAI;
    }

    private AI buildCommandsIA(UnitTypeTable utt, String code) {
        HashMap<Long, String> counterByFunction = new HashMap<>();
        usedCommands = new HashSet<>();
        FunctionGPCompiler.counterCommands = 0;
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        //System.out.println("code = " + code);
        AI aiscript = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands, counterByFunction);
        return aiscript;
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {        
        return ai.getAction(player, gs);
    }

    @Override
    public AI clone() {
        return ai.clone();
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "botEmptyBase{"+ identification + '}';
    }
    
    

}
