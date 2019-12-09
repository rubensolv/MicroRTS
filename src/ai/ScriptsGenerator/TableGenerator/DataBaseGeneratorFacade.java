/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.TableGenerator;

import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.Command.Enumerators.EnumTypeUnits;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.core.AI;
import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author julian and rubens
 */
public class DataBaseGeneratorFacade {
    //list of fixed param
    private static final int MAX_QTD_WORKERS_HARVERST = 5;
    private static final int MAX_QTD_UNITS_TO_BUILD = 3;
    private static final int MAX_QTD_UNITS_TO_TRAIN = 20;
    private static final int MAP_SIZE = 8;
    private static final int MAX_QTD_NAllyUnitsAttacking = 4;
    
    //
    private HashSet<String> usedCommands;
    private HashMap<Long, String> counterByFunction;
    private ICompiler compiler = new MainGPCompiler();
    private UnitTypeTable utt = new UnitTypeTable();
    
    public HashMap<Integer, HashMap<AI, BigDecimal>> getDataBaseByType(EnumTypeUnits type){
        if (type == EnumTypeUnits.Barracks) {
            return getListOfBarracks();
        }else if (type == EnumTypeUnits.Base) {
            return getListOfBase();
        }else if (type == EnumTypeUnits.Heavy) {
            return getListOfHeavy();
        }else if (type == EnumTypeUnits.Light) {
            return getListOfLight();
        } else if (type == EnumTypeUnits.Ranged) {
            return getListOfRanged();
        } else if (type == EnumTypeUnits.Worker) {
            return getListOfWorker();
        } 
        return new HashMap();
    }

    private HashMap<Integer, HashMap<AI, BigDecimal>> getListOfBarracks() {
        List<AI> actions = new ArrayList();
        actions.add(buildCommandsIA(utt, "train(Light,20,Up)"));
        actions.add(buildCommandsIA(utt, "train(Ranged,20,Left)"));
        return transformListToMap(actions);
    }

    private HashMap<Integer, HashMap<AI, BigDecimal>> getListOfBase() {
        List<AI> actions = new ArrayList();
        actions.add(buildCommandsIA(utt, "train(Worker,1,EnemyDir)"));
        actions.add(buildCommandsIA(utt, "train(Worker,2,EnemyDir)"));
        return transformListToMap(actions);
    }

    private HashMap<Integer, HashMap<AI, BigDecimal>> getListOfHeavy() {
        List<AI> actions = new ArrayList();
        actions.add(buildCommandsIA(utt, "attack(Heavy,closest)"));
        actions.add(buildCommandsIA(utt, "attack(Heavy,mostHealthy)"));
        return transformListToMap(actions);
    }

    private HashMap<Integer, HashMap<AI, BigDecimal>> getListOfLight() {
        List<AI> actions = new ArrayList();
        actions.add(buildCommandsIA(utt, "attack(Light,closest)"));
        actions.add(buildCommandsIA(utt, "attack(Light,mostHealthy)"));
        return transformListToMap(actions);
    }

    private HashMap<Integer, HashMap<AI, BigDecimal>> getListOfRanged() {        
        List<AI> actions = new ArrayList();
        actions.add(buildCommandsIA(utt, "attack(Ranged,closest)"));
        actions.add(buildCommandsIA(utt, "attack(Ranged,mostHealthy)"));
        return transformListToMap(actions);
    }

    private HashMap<Integer, HashMap<AI, BigDecimal>> getListOfWorker() {
        List<AI> actions = new ArrayList();
        actions.add(buildCommandsIA(utt, "harvest(1)"));
        actions.add(buildCommandsIA(utt, "harvest(2)"));
        return transformListToMap(actions);
    }
    
    private AI buildCommandsIA(UnitTypeTable utt, String code) {
    	usedCommands=new HashSet<> ();
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI aiscript = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands,counterByFunction);
        return aiscript;
    }
    /**
     * This method return a HashMap<Id, HashMap<Object_AI, Weight>>
     * @param actions
     * @return 
     */
    private HashMap<Integer, HashMap<AI, BigDecimal>> transformListToMap(List<AI> actions) {
        int cont = 0;
        HashMap<Integer, HashMap<AI, BigDecimal>> map = new HashMap();
        for (AI action : actions) {
            HashMap<AI, BigDecimal> temp = new HashMap<>();
            temp.put(action, BigDecimal.ZERO);
            map.put(cont, temp);
            cont++;
        }
        
        return map;
    }
}
