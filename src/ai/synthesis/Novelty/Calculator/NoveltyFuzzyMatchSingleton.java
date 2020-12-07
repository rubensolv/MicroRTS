/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.Novelty.Calculator;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.HashMap;
import java.util.HashSet;
import me.xdrop.fuzzywuzzy.FuzzySearch;


/**
 *
 * @author rubens
 */
public class NoveltyFuzzyMatchSingleton {
    //Singleton instance
    private static NoveltyFuzzyMatchSingleton uniqueInstance;
    
    
    //normal attributes
    private HashMap<Integer, String> key_map;
    private HashMap<String, Integer> string_map;
    private Integer cont_id;
    private HashMap<Integer, HashSet<Integer>> transform_relations;
    private HashMap<Integer, ScoreNoveltyStore> key_score;
    
    private NoveltyFuzzyMatchSingleton(){
        this.key_map = new HashMap<>();
        this.string_map = new  HashMap<>();
        this.cont_id = 1;
        this.transform_relations = new HashMap<>();
        this.key_score = new HashMap<>();
    }
        
    public static NoveltyFuzzyMatchSingleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new NoveltyFuzzyMatchSingleton();
        }
        return uniqueInstance;
    }
    
    /**
     * The method adds the DSL to the repository with some constraints:
     * 1 - Check if the DSL exists. If the DSL is in the repository it will return false.
     * 2 - If not, the DSL is put in the repository and the method returns true.
     * @param dsl - a DSL instance
     * @return True or False
     */
    public synchronized boolean add_dsl(String sDSL){        
        if(this.string_map.containsKey(sDSL)){
            return false;
        }else{
            //insert the necessary dsl in the two databases.
            include_string(sDSL);
            return true;
        }
    }

    private synchronized Integer include_string(String sDSL) {
        this.key_map.put(cont_id, sDSL);
        this.string_map.put(sDSL, cont_id);
        Integer ret = this.cont_id.intValue();
        this.cont_id++;
        return ret;
    }
    
    public synchronized void add_transformation(String dsl_orig, String dsl_reduced){
        if(dsl_orig.equals(dsl_reduced)){
            return;
        }
        
        Integer iorig;
        Integer ireduced;
        if(this.string_map.containsKey(dsl_orig)){
            iorig = string_map.get(dsl_orig);
        }else{
            iorig = include_string(dsl_orig);
        }        
        if(this.string_map.containsKey(dsl_reduced)){
            ireduced = string_map.get(dsl_reduced);
        }else{
            ireduced = include_string(dsl_reduced);
        }
        HashSet<Integer> elements;
        if(this.transform_relations.containsKey(iorig)){
            elements= transform_relations.get(iorig);
            
        }else{
            elements = new HashSet<>();            
        }
        elements.add(ireduced);
        transform_relations.put(iorig, elements);
        
        if(this.transform_relations.containsKey(ireduced)){
            elements= transform_relations.get(ireduced);
            
        }else{
            elements = new HashSet<>();            
        }
        elements.add(iorig);
        transform_relations.put(ireduced, elements);
    }
    
    public synchronized void update_score(String dsl, double score){
        Integer iorig;        
        if(this.string_map.containsKey(dsl)){
            iorig = string_map.get(dsl);
        }else{
            iorig = include_string(dsl);
        }   
        ScoreNoveltyStore scoreData;
        if(this.key_score.containsKey(iorig)){
            scoreData = this.key_score.get(iorig);
        }else{
            scoreData = new ScoreNoveltyStore();
        }
        scoreData.addInSum(score);
        scoreData.incrQuantity();
        this.key_score.put(iorig, scoreData);
    }
    
    public synchronized double get_novelty_factor(String dsl){
        //get score from editing factor
        double ed_fac = get_editing_factor(dsl);
        //get score factor
        //get existence factor
        
        return ed_fac;
    }

    private double get_editing_factor(String dsl) {
        if(this.string_map.isEmpty()){
            return 1.0d;
        }
        if(this.string_map.containsKey(dsl)){
            return 0.0d;
        }
        double max_simetry = 0.0d;        
        for (String visited : this.key_map.values()) {                                    
            double eval = (FuzzySearch.ratio(dsl, visited) + FuzzySearch.tokenSortRatio(dsl, visited))/2;            
            if(eval > max_simetry ){
                max_simetry = eval;                
            }
        }
        max_simetry = 100.0d - max_simetry;
        if(max_simetry == 0.0d){
            return 0.0d;
        }        
        return (max_simetry/100.0d);
    }
}
