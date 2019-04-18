/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class FunctionGPCompiler implements ICompiler{

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        TableCommandsGenerator tcg=TableCommandsGenerator.getInstance(utt);
        List<ICommand> commands = new ArrayList<>();
        code = code.replace("!", "");
        String[] codes = code.split(" ");
        for (int i = 0; i < codes.length; i++) {
            String code1 = codes[i];
            int idFunction = Integer.decode(code1.trim());
            commands.add(tcg.getCommandByID(idFunction));
        }
        
        return commands;
    }
    
    
    public static ICommand getFunctionByID(int ID, UnitTypeTable utt){
        TableCommandsGenerator tcg=TableCommandsGenerator.getInstance(utt);
        return tcg.getCommandByID(ID);
    }
    
    

    
    
}
