/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.Novelty.Calculator;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.CommandDSL;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iCommandDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iNodeDSLTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rubens
 */
public class NoveltyCommandSingleton {

    //Singleton instance
    private static NoveltyCommandSingleton uniqueInstance;    
    private HashSet<String> key_commands;

    private NoveltyCommandSingleton() {
        this.key_commands = new HashSet<>();        
    }

    public static NoveltyCommandSingleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new NoveltyCommandSingleton();
        }
        return uniqueInstance;
    }
    
    public void clear(){
        this.key_commands.clear();
    }

    public synchronized Integer get_novelty_factor(iDSL dsl, float temp_current) {
        Integer ret = 1;
        //get all commands from the DSL
        List<iDSL> com = get_commands(dsl);                        
        //get all combinations to eval                
        List<String> combinations;
        for (int i = 1; i <= SettingsAlphaDSL.get_novelty_size(); i++) {
            if (i <= com.size()) {
                combinations = get_combinations_by_size(com, i);
                for (String elem : combinations) {
                    if (!this.key_commands.contains(elem)) {
                        return ret;
                    }
                }
            }
            ret++;
            //System.out.println("---------- "+dsl.translate()+" "+ret+"\n"
            //+key_commands);
        }

        return ret;
    }

    private synchronized List<iDSL> get_commands(iDSL dsl) {
        HashSet<iDSL> lnodes = new HashSet<>();
        HashSet<iNodeDSLTree> nodes = BuilderDSLTreeSingleton.getAllNodes((iNodeDSLTree) dsl);        
        for (iNodeDSLTree node : nodes) {
            if (node instanceof CDSL) {
                CDSL t = (CDSL) node;
                lnodes.add(t.getRealCommand());
            }
            if(node instanceof CommandDSL){
                lnodes.add((iDSL) node);
            }
        }

        return new ArrayList<>(lnodes);
    }

    private List<List<iDSL>> combinations(List<iDSL> values, int size) {
        if (0 == size) {
            return Collections.singletonList(Collections.<iDSL>emptyList());
        }

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<iDSL>> combination = new LinkedList<List<iDSL>>();

        iDSL actual = values.iterator().next();

        List<iDSL> subSet = new LinkedList<iDSL>(values);
        subSet.remove(actual);

        List<List<iDSL>> subSetCombination = combinations(subSet, size - 1);

        for (List<iDSL> set : subSetCombination) {
            List<iDSL> newSet = new LinkedList<iDSL>(set);
            newSet.add(0, actual);
            combination.add(newSet);
        }

        combination.addAll(combinations(subSet, size));
        return combination;
    }

    private List<String> get_combinations(List<iDSL> com) {
        List<List<iDSL>> powerSet = new LinkedList<List<iDSL>>();
        int total = SettingsAlphaDSL.get_novelty_size();
        if(total > com.size()){
            total = com.size();
        }
        for (int i = 1; i <= total; i++) {
            powerSet.addAll(combinations(com, i));
        }
        List<String> keys = new ArrayList<>();

        for (List<iDSL> list : powerSet) {
            String key = "";
            for (iDSL dSL : list) {
                key += dSL.translate();
            }
            keys.add(key);
        }
        return keys;
    }

    private List<String> get_combinations_by_size(List<iDSL> com, int size) {
        List<List<iDSL>> powerSet = new LinkedList<List<iDSL>>();
        powerSet.addAll(combinations(com, size));
        List<String> keys = new ArrayList<>();

        for (List<iDSL> list : powerSet) {
            String key = "";
            for (iDSL dSL : list) {
                key += dSL.translate();
            }
            keys.add(key);
        }
        return keys;
    }

    public synchronized void add_commands(iDSL dsl) {
        if(!dsl.translate().equals("")){
            List<iDSL> com = get_commands(dsl);
            List<String> combinations = get_combinations(com);
            this.key_commands.addAll(combinations);
            //System.out.println("------ $$$ "+ this.key_commands.size()+" "+ combinations+
            //        "\n"+this.key_commands);
            //System.out.println("  @@@@@@@@  "+this.key_commands.size());
        }
        
    }

}
