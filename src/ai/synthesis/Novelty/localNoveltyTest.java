/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.Novelty;

import PVAI.util.Permutation;
import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.Novelty.Calculator.NoveltyFuzzyMatchSingleton;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderSketchDSLSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iNodeDSLTree;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SearchImplementation;
import ai.synthesis.localsearch.searchImplementation.SimulatedAnnealing;
import java.util.ArrayList;

/**
 *
 * @author rubens
 */
public class localNoveltyTest {
    
    public static void main(String[] args) {          
        
        /*
        BuilderDSLTreeSingleton builder = BuilderDSLTreeSingleton.getInstance();
        iDSL t = builder.buildS1Grammar();
        iDSL t2 = builder.buildS1Grammar();
        SettingsAlphaDSL.setMode_debug(true);      
        NoveltyFuzzyMatchSingleton nov = NoveltyFuzzyMatchSingleton.getInstance();
        SearchImplementation searchAlgorithm = new SimulatedAnnealing(); 
        
        DetailedSearchResult results = searchAlgorithm.run(t, t2, 1, 2.5f);
        
        for (int i = 0; i < 10; i++) {
            
            builder.composeNeighbourPassively(t);
            results = searchAlgorithm.run(t, t2, 1, 2.5f);
            t = builder.buildS1Grammar();
            results = searchAlgorithm.run(t, t2, 1, 2.5f);
            
        }
        System.out.println("ai.synthesis.Novelty.localNoveltyTest.main()");
        
        */
        
        
    }
    
}
