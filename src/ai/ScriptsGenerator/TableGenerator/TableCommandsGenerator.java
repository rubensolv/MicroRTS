/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.TableGenerator;

import ai.ScriptsGenerator.Command.BasicAction.BuildBasic;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class TableCommandsGenerator {
    
    public static ICommand getCommandByID(int ID, UnitTypeTable utt){
        BuildBasic build = new BuildBasic();
        build.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
        build.addParameter(new QuantityParam(1)); //add qtd unit
        return build;
    }
    
}
