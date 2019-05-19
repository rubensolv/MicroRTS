/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.Command.BasicLoops.ForFunction;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.TableGenerator.FunctionsforGrammar;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class ForGPCompiler extends AbstractCompiler {
    private IfGPCompiler ifCompiler = new IfGPCompiler();    
    protected FunctionGPCompiler functionCompiler = new FunctionGPCompiler();
    protected ConditionalGPCompiler conditionalCompiler = new ConditionalGPCompiler();

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        ForFunction forFunction = new ForFunction();
        List<ICommand> commands = new ArrayList<>();
        code = code.substring(6).trim();
        if(code.startsWith("(")){
            code = code.substring(1);
            code = code.substring(0, code.length()-1);
        }
        String[] fragments = code.split(" ");        

        //build the items inside of the compiler
        //i starts with 1 just to remove the word for(u) from the fragments
        for (int i = 0; i < fragments.length; i++) {
            String fragment = fragments[i];
            if(isBasicCommand(fragment)){                
                //get the position to cut the fragments 
                int idToCut = functionCompiler.getLastPositionForBasicFunctionInFor(i, fragments);
                String completeBasicFunction = generateString(i, idToCut, fragments);
                //get the complete string                
                forFunction.setCommandsFor(functionCompiler.CompilerCode(completeBasicFunction, utt));
                i = idToCut;
            } else if (fragment.contains("if")) {
                //method to remove all String from the fragments
                int idToCut = ifCompiler.getPositionFinalIF(i, fragments, true);
                String completeIF = generateString(i, idToCut, fragments);
                forFunction.setCommandsFor(ifCompiler.CompilerCode(completeIF, utt));
                i = idToCut;
            }
        }
        
        commands.add(forFunction);
        return commands;
    }

    public int getLastPositionForFor(int initialPosition, String[] fragments) {
        //first get the name for(u)
        if (isForInitialClause(fragments[initialPosition])) {
            initialPosition++;
        }
        //second, we get the full () to complet the for. 
        return getPositionParentClose(initialPosition, fragments);
    }

    private boolean isForInitialClause(String fragment) {
        if (fragment.contains("for(u)")) {
            return true;
        }
        return false;
    }
    
    
    public static List<ICommand> CompilerCodeStatic(String code, UnitTypeTable utt) {
        FunctionGPCompiler functionCompiler = new FunctionGPCompiler();
        IfGPCompiler ifCompiler = new IfGPCompiler();  
        ForFunction forFunction = new ForFunction();
        List<ICommand> commands = new ArrayList<>();
        code = code.substring(6).trim();
        if(code.startsWith("(")){
            code = code.substring(1);
            code = code.substring(0, code.length()-1);
        }
        String[] fragments = code.split(" ");                

        //build the items inside of the compiler
        //i starts with 1 just to remove the word for(u) from the fragments
        for (int i = 0; i < fragments.length; i++) {
            String fragment = fragments[i];
            if(isBasicCommandStatic(fragment)){                
                //get the position to cut the fragments 
                int idToCut = functionCompiler.getLastPositionForBasicFunctionInFor(i, fragments);
                String completeBasicFunction = generateString(i, idToCut, fragments);
                //get the complete string                
                forFunction.setCommandsFor(functionCompiler.CompilerCode(completeBasicFunction, utt));
                i = idToCut;
            } else if (fragment.contains("if")) {
                //method to remove all String from the fragments
                int idToCut = ifCompiler.getPositionFinalIF(i, fragments, true);
                String completeIF = generateString(i, idToCut, fragments);
                forFunction.setCommandsFor(ifCompiler.CompilerCode(completeIF, utt));
                i = idToCut;
            }
        }
        
        commands.add(forFunction);
        return commands;
    }
    
    protected static boolean isBasicCommandStatic(String fragment) {
        FunctionsforGrammar fGrammar = new FunctionsforGrammar();
        List<FunctionsforGrammar> basicFunctions = fGrammar.getBasicFunctionsForGrammar();
        for (FunctionsforGrammar basicFunction : basicFunctions) {
            if (fragment.contains(basicFunction.getNameFunction())) {
                return true;
            }
        }
        return false;
    }

}
