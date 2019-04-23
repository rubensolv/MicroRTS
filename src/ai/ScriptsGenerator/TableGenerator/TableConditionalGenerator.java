/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.TableGenerator;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.BasicConditional.SimpleConditional;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.DistanceParam;
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
        //HaveQtdUnitsHarversting - ids {0-8}
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(0)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(1)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(2)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(3)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(5)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(6)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(7)))));
        conditional.add(new SimpleConditional("HaveQtdUnitsHarversting", new ArrayList(Arrays.asList(new QuantityParam(8)))));
        
        //HaveUnitsinEnemyRange - ids {9-13}
        conditional.add(new SimpleConditional("HaveUnitsinEnemyRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveUnitsinEnemyRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveUnitsinEnemyRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveUnitsinEnemyRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveUnitsinEnemyRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits()))));
        
        //HaveEnemiesinUnitRange - ids {14-15}
        conditional.add(new SimpleConditional("HaveEnemiesinUnitsRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveEnemiesinUnitsRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveEnemiesinUnitsRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveEnemiesinUnitsRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveEnemiesinUnitsRange", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits()))));
        
        //HaveQtdEnemiesbyType - ids {16-20}
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeWorker()))));
        //HaveQtdEnemiesbyType - ids {21-25}
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeLight()))));
        //HaveQtdEnemiesbyType - ids {26-30}
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeRanged()))));
        //HaveQtdEnemiesbyType - ids {31-35}
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeHeavy()))));
        //HaveQtdEnemiesbyType - ids {36-40}
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdEnemiesbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeUnits()))));
        
        //HaveQtdUnitsAttacking - ids {41-45}
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeWorker()))));
        //HaveQtdUnitsAttacking - ids {46-50}
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeLight()))));
        //HaveQtdUnitsAttacking - ids {51-55}
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeRanged()))));
        //HaveQtdUnitsAttacking - ids {56-60}
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeHeavy()))));
        //HaveQtdUnitsAttacking - ids {61-65}
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsAttacking", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeUnits()))));
       
        //HaveQtdUnitsbyType - ids {66-70}
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeWorker()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeWorker()))));
        //HaveQtdUnitsbyType - ids {71-75}
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeLight()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeLight()))));
        //HaveQtdUnitsbyType - ids {76-80}
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeRanged()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeRanged()))));
        //HaveQtdUnitsbyType - ids {81-85}
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeHeavy()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeHeavy()))));
        //HaveQtdUnitsbyType - ids {86-90}
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(2),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(4),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(6),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(8),TypeConcrete.getTypeUnits()))));
        conditional.add(new SimpleConditional("HaveQtdUnitsbyType", new ArrayList(Arrays.asList(new QuantityParam(10),TypeConcrete.getTypeUnits()))));
        
        //HaveUnitsToDistantToEnemy - ids {91-95}
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker(),new DistanceParam(2)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker(),new DistanceParam(4)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker(),new DistanceParam(6)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker(),new DistanceParam(8)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeWorker(),new DistanceParam(10)))));
        //HaveUnitsToDistantToEnemy - ids {96-100}
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight(),new DistanceParam(2)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight(),new DistanceParam(4)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight(),new DistanceParam(6)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight(),new DistanceParam(8)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeLight(),new DistanceParam(10)))));
        //HaveUnitsToDistantToEnemy - ids {101-105}
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged(),new DistanceParam(2)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged(),new DistanceParam(4)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged(),new DistanceParam(6)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged(),new DistanceParam(8)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeRanged(),new DistanceParam(10)))));
        //HaveUnitsToDistantToEnemy - ids {106-110}
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy(),new DistanceParam(2)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy(),new DistanceParam(4)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy(),new DistanceParam(6)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy(),new DistanceParam(8)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeHeavy(),new DistanceParam(10)))));
        //HaveUnitsToDistantToEnemy - ids {111-115}
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits(),new DistanceParam(2)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits(),new DistanceParam(4)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits(),new DistanceParam(6)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits(),new DistanceParam(8)))));
        conditional.add(new SimpleConditional("HaveUnitsToDistantToEnemy", new ArrayList(Arrays.asList(TypeConcrete.getTypeUnits(),new DistanceParam(10)))));
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
