/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.Command.BasicAction.BuildBasic;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class FunctionGPCompiler implements ICompiler {

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        List<ICommand> commands = new ArrayList<>();

        ICommand tFunction = buildFunctionByCode(code, utt);

        commands.add(tFunction);
        return commands;
    }

    public static ICommand getFunctionByID(int ID, UnitTypeTable utt) {
        TableCommandsGenerator tcg = TableCommandsGenerator.getInstance(utt);
        return tcg.getCommandByID(ID);
    }

    public static int getLastPositionForBasicFunction(int initialPosition, String[] fragments) {
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

    public static int countCaracter(String fragment, String toFind) {
        int total = 0;
        for (int i = 0; i < fragment.length(); i++) {
            char ch = fragment.charAt(i);
            String x1 = String.valueOf(ch);
            if (x1.equalsIgnoreCase(toFind)) {
                total = total + 1;
            }
        }
        return total;
    }

    private ICommand buildFunctionByCode(String code, UnitTypeTable utt) {
        if (code.contains("build")) {
            return buildCommand(code, utt);
        }else if(code.contains("attack")){
            
        }else if(code.contains("harvest")){
            
        }else if(code.contains("moveToCoord")){
            
        }else if(code.contains("moveToUnit")){
            
        }else if(code.contains("train")){
            
        }
        
        return null;
    }

    private ICommand buildCommand(String code, UnitTypeTable utt) {
        code = code.replace("build(", "");
        code = code.replace(")", "").replace(",", "");
        String[] params = code.split(" ");
        BuildBasic build = new BuildBasic();
        if(params[0].equals("Base")) {
            build.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
        }else{
            build.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
        }
        build.addParameter(new QuantityParam(Integer.decode(params[1]))); //add qtd unit
        return build;
    }

}
