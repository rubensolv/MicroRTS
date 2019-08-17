/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author julian and rubens
 */
public class MainGPCompiler extends AbstractCompiler {
    private IfGPCompiler ifCompiler = new IfGPCompiler();
    private ForGPCompiler forCompiler = new ForGPCompiler();
    protected FunctionGPCompiler functionCompiler = new FunctionGPCompiler();
    protected ConditionalGPCompiler conditionalCompiler = new ConditionalGPCompiler();

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        return buildCommands(code, utt);
    }

    private List<ICommand> buildCommands(String code, UnitTypeTable utt) {
        List<ICommand> commands = new ArrayList<>();
        String[] fragments = code.trim().split(" ");

        for (int i = 0; i < fragments.length; i++) {
            String fragment = fragments[i];
            if(isBasicCommand(fragment)){
                //get the position to cut the fragments 
                int idToCut = functionCompiler.getLastPositionForBasicFunction(i, fragments);
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
            }else if(fragment.contains("for")){
                int idToCut = forCompiler.getLastPositionForFor(i, fragments);
                String completeFor = generateString(i, idToCut, fragments);
                commands.addAll(forCompiler.CompilerCode(completeFor, utt));
                i = idToCut;
            }
        }

        //for (ICommand command : commands) {
        //    System.out.println(command.toString());
        //}
        
        return commands;
    }

    

}
