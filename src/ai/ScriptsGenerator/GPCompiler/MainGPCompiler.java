/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.TableGenerator.FunctionsforGrammar;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author julian and rubens
 */
public class MainGPCompiler implements ICompiler {

    private FunctionGPCompiler functionCompiler = new FunctionGPCompiler();
    private IfGPCompiler ifCompiler = new IfGPCompiler();
    private FunctionsforGrammar fGrammar = new FunctionsforGrammar();

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        return buildCommands(code, utt);
    }

    private List<ICommand> buildCommands(String code, UnitTypeTable utt) {
        List<ICommand> commands = new ArrayList<>();
        String[] fragments = code.split(" ");

        for (int i = 0; i < fragments.length; i++) {
            String fragment = fragments[i];
            if(isBasicCommand(fragment)){
                //get the position to cut the fragments 
                int idToCut = FunctionGPCompiler.getLastPositionForBasicFunction(i, fragments);
                String completeBasicFunction = generateString(i, idToCut, fragments);
                //get the complete string                
                commands.addAll(functionCompiler.CompilerCode(completeBasicFunction, utt));
                i = idToCut;
            } else if (fragment.contains("if")) {
                //method to remove all String from the fragments
                int idToCut = ifCompiler.getPositionFinalIF(i, fragments, true);
                String completeIF = generateString(i, idToCut, fragments);
                commands.addAll(ifCompiler.CompilerCode(completeIF, utt));
                i = idToCut;
            }
        }

        //for (ICommand command : commands) {
        //    System.out.println(command.toString());
        //}
        
        return commands;
    }

    public static String generateString(int initialPos, int finalPos, String[] fragments) {
        String fullString = "";
        if (finalPos > (fragments.length - 1)) {
            finalPos = (fragments.length - 1);
        }
        for (int i = initialPos; i <= finalPos; i++) {
            fullString += fragments[i] + " ";
        }
        return fullString.trim();
    }

    private boolean isBasicCommand(String fragment) {
        List<FunctionsforGrammar> basicFunctions = fGrammar.getBasicFunctionsForGrammar();
        for (FunctionsforGrammar basicFunction : basicFunctions) {
            if(fragment.contains(basicFunction.getNameFunction())){
                return true;
            }
        }
        return false;
    }

}
