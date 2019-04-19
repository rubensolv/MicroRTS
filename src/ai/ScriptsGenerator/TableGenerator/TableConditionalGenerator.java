/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.TableGenerator;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.BasicConditional.SimpleConditional;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import java.util.ArrayList;
import java.util.Arrays;
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
        //haveQtdUnitsHarversting - ids {0-8}
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(0)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(1)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(2)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(3)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(5)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(6)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(7)))));
        conditional.add(new SimpleConditional("haveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(8)))));
        //haveUnitsBeenAttacked - ids {9-13}
        conditional.add(new SimpleConditional("haveUnitsBeenAttacked", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("haveUnitsBeenAttacked", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("haveUnitsBeenAttacked", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("haveUnitsBeenAttacked", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("haveUnitsBeenAttacked", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits()))));
    }
    
    private void generateDic() {
        int cont = 0;
        for (IConditional cond : conditional) {
            dicCoditional.put(cont, cond);
            cont++;
        }
    }
    
    public IConditional getConditionalByID(int ID) {
        return dicCoditional.get(ID);
    }
    
    
}
