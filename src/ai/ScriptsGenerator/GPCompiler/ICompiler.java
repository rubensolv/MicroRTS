/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public interface ICompiler {
    
    
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt);
}
