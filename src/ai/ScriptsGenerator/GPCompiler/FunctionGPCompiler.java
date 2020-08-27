/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.BuildBasic;
import ai.ScriptsGenerator.Command.BasicAction.ClusterBasic;
import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveAwayBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToCoordinatesBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToCoordinatesOnce;
import ai.ScriptsGenerator.Command.BasicAction.MoveToUnitBasic;
import ai.ScriptsGenerator.Command.BasicAction.TrainBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumPlayerTarget;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.CoordinatesParam;
import ai.ScriptsGenerator.ParametersConcrete.PlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class FunctionGPCompiler extends AbstractCompiler {

	static public int counterCommands=0;
    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        List<ICommand> commands = new ArrayList<>();
        ICommand tFunction = buildFunctionByCode(code, utt);

        commands.add(tFunction);
        return commands;
    }

    public int getLastPositionForBasicFunction(int initialPosition, String[] fragments) {
        int contOpen = 0, contClosed = 0;

        for (int i = initialPosition; i < fragments.length; i++) {
            String fragment = fragments[i];
            contOpen += countCaracter(fragment, "(");
            contClosed += countCaracter(fragment, ")");
            if (contOpen == contClosed) {
                return i;
            }
        }

        return fragments.length;
    }
    
    public int getLastPositionForBasicFunctionInFor(int initialPosition, String[] fragments) {
        boolean removeInitialParen = false;
        int contOpen = 0, contClosed = 0;
        //check if starts with (
        if(fragments[initialPosition].startsWith("(")){
            removeInitialParen = true;
        }
        for (int i = initialPosition; i < fragments.length; i++) {
            String fragment = fragments[i];
            if(i == initialPosition && removeInitialParen){
                fragment = fragment.substring(1);
            }
            
            contOpen += countCaracter(fragment, "(");
            contClosed += countCaracter(fragment, ")");
            if (contOpen == contClosed) {
                return i;
            }
        }

        return fragments.length;
    }

    private ICommand buildFunctionByCode(String code, UnitTypeTable utt) {
        if (code.contains("build")) {
            return buildCommand(code, utt);
        }
        if (code.contains("attack")) {
            return attackCommand(code, utt);
        }
        if (code.contains("harvest")) {
            return harvestCommand(code, utt);
        }
        if (code.contains("moveToCoord")) {
            return moveToCoordCommand(code, utt);
        }
        if (code.contains("moveToUnit")) {
            return moveToUnitCommand(code, utt);
        }
        if (code.contains("train")) {
            return trainCommand(code, utt);
        }
        if (code.contains("moveaway")) {
            return moveAwayCommand(code, utt);
        }
        if (code.contains("cluster")) {
            return clusterCommand(code, utt);
        } 
        if (code.contains("moveOnceToCoord")) {
            return moveToCoordOnceCommand(code, utt);
        }

        return null;
    }

    private ICommand buildCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("build(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        BuildBasic build = new BuildBasic();
        build.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        build.setOriginalPieceGrammarWord("build");
        if (params[0].equals("Base")) {
            build.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
        } else if (params[0].equals("Barrack")) {
            build.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
        } else {
            build.addParameter(TypeConcrete.getTypeConstruction());
        }
        build.addParameter(new QuantityParam(Integer.decode(params[1]))); //add qtd unit
        //add position
        PriorityPositionParam pos = new PriorityPositionParam();
        pos.addPosition(EnumPositionType.byName(params[2]));
        build.addParameter(pos);
        //If there are more than 3 parameters, we need a unit
        if (params.length > 3) {
            build.setUnitIsNecessary();
        }
        return build;
    }

    private ICommand harvestCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("harvest(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        HarvestBasic harverst = new HarvestBasic();
        harverst.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        harverst.setOriginalPieceGrammarWord("harvest");
        harverst.addParameter(TypeConcrete.getTypeWorker()); //add unit type
        harverst.addParameter(new QuantityParam(Integer.decode(params[0]))); //add qtd unit

        if (params.length > 1) {
            harverst.setUnitIsNecessary();
        }
        return harverst;
    }

    private ICommand attackCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("attack(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        AttackBasic attack = new AttackBasic();
        attack.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        attack.setOriginalPieceGrammarWord("attack");
        attack.addParameter(getTypeUnitByString(params[0])); //add unit type
        PlayerTargetParam pt = new PlayerTargetParam();
        pt.addPlayer(EnumPlayerTarget.Enemy);
        attack.addParameter(pt);
        attack.addParameter(getBehaviorByName(params[1])); //add behavior

        if (params.length > 2) {
            attack.setUnitIsNecessary();
        }
        return attack;
    }

    private ICommand moveToCoordCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("moveToCoord(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        MoveToCoordinatesBasic moveToCoordinates = new MoveToCoordinatesBasic();
        moveToCoordinates.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        moveToCoordinates.setOriginalPieceGrammarWord("moveToCoordinates");
        int x = Integer.decode(params[1]);
        int y = Integer.decode(params[2]);
        moveToCoordinates.addParameter(new CoordinatesParam(x, y));
        moveToCoordinates.addParameter(getTypeUnitByString(params[0]));//add unit type
        if (params.length > 3) {
            moveToCoordinates.setUnitIsNecessary();
        }
        return moveToCoordinates;
    }

    private ICommand moveToUnitCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("moveToUnit(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        MoveToUnitBasic moveToUnit = new MoveToUnitBasic();
        moveToUnit.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        moveToUnit.setOriginalPieceGrammarWord("moveToUnit");
        moveToUnit.addParameter(getTypeUnitByString(params[0])); //add unit type
        PlayerTargetParam pt = new PlayerTargetParam();
        pt.addPlayer(getPlayerTargetByNumber(params[1]));
        moveToUnit.addParameter(pt);
        moveToUnit.addParameter(getBehaviorByName(params[2])); //add behavior
        if (params.length > 3) {
            moveToUnit.setUnitIsNecessary();
        }
        return moveToUnit;
    }

    private ICommand trainCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("train(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        TrainBasic train = new TrainBasic();
        train.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        train.setOriginalPieceGrammarWord("train");
        //train.addParameter(getTypeConstructByName(params[1])); //add unit construct type
        if (params[0].equals("All")) {
            train.addParameter(getTypeConstructByName("All"));
        } else if (params[0].equals("Worker")) {
            train.addParameter(getTypeConstructByName("Base")); //add unit construct type
        } else {
            train.addParameter(getTypeConstructByName("Barrack"));
        }
        train.addParameter(getTypeUnitByString(params[0])); //add unit Type
        //train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
        train.addParameter(new QuantityParam(Integer.decode(params[1]))); //add qtd unit
        PriorityPositionParam pos = new PriorityPositionParam();
        //for (Integer position : Permutation.getPermutation(j)) {
        pos.addPosition(EnumPositionType.byName(params[2]));
        //}

        train.addParameter(pos);
        return train;
    }

    private ICommand moveAwayCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("moveaway(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        MoveAwayBasic moveAway = new MoveAwayBasic();
        moveAway.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        moveAway.setOriginalPieceGrammarWord("moveAway");
        moveAway.addParameter(getTypeUnitByString(params[0]));
        if (params.length > 1) {
            moveAway.setUnitIsNecessary();
        }

        return moveAway;
    }

    private ICommand clusterCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("cluster(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        ClusterBasic cluster = new ClusterBasic();
        cluster.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        cluster.setOriginalPieceGrammarWord("cluster");
        cluster.addParameter(getTypeUnitByString(params[0]));

        return cluster;
    }
    
    private ICommand moveToCoordOnceCommand(String code, UnitTypeTable utt) {
    	String originalCode=code;
        if(code.startsWith("(")){
            code = code.substring(1);
        }
        code = code.replace("moveOnceToCoord(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        MoveToCoordinatesOnce moveToCoordinates = new MoveToCoordinatesOnce();
        moveToCoordinates.setOriginalPieceGrammar(String.valueOf(FunctionGPCompiler.counterCommands++));
        moveToCoordinates.setOriginalPieceGrammarWord("moveOnceToCoord");
        int x = Integer.decode(params[2]);
        int y = Integer.decode(params[3]);
        moveToCoordinates.addParameter(new CoordinatesParam(x, y));
        moveToCoordinates.addParameter(getTypeUnitByString(params[0]));//add unit type
        moveToCoordinates.addParameter(new QuantityParam(Integer.decode(params[1]))); //add qtd unit
        if (params.length > 4) {
            moveToCoordinates.setUnitIsNecessary();
        }
        return moveToCoordinates;
    }

}
