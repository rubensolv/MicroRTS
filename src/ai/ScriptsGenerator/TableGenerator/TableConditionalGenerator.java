/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.TableGenerator;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.BasicConditional.SimpleConditional;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class TableConditionalGenerator {
    UnitTypeTable utt;
    ArrayList<IConditional> conditional;
    HashMap<Integer, IConditional> dicCoditional;
    
    
    private static TableConditionalGenerator uniqueInstance;
    
    public static synchronized TableConditionalGenerator getInstance(UnitTypeTable utt){
        if(uniqueInstance == null){
            uniqueInstance = new TableConditionalGenerator(utt);
        }
        return uniqueInstance;
    }

    public TableConditionalGenerator(UnitTypeTable utt) {
        this.utt = utt;
        conditional = new ArrayList<>();
        dicCoditional = new HashMap<>();
        generateTable();
        generateDic();
    }
    
    private void generateTable() {
        conditional.add(new SimpleConditional("hasUnitsHarversting", new ArrayList()));
    }
    
    private void generateDic() {
        int cont = 0;
        for (IConditional cond : conditional) {
            dicCoditional.put(cont, cond);
            cont++;
        }
    }
    
    public IConditional getConditionalByID(int ID) {
        return dicCoditional.get(0);
    }
    
    
}
