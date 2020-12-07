/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.runners;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.localsearch.CumulativeDoubleProgramSynthesis;
import ai.synthesis.localsearch.CumulativeSimpleProgramSynthesis;
import ai.synthesis.localsearch.DoublePortfolioSynthesis;
import ai.synthesis.localsearch.DoubleProgramSynthesis;
import ai.synthesis.localsearch.DoubleSketchPortfolioSynthesis;
import ai.synthesis.localsearch.DoubleSketchSynthesis;
import ai.synthesis.localsearch.FPTableV1Elo;
import ai.synthesis.localsearch.FPTableV1_noWeights;
import ai.synthesis.localsearch.LocalSearch;
import ai.synthesis.localsearch.Recovery_CumulativeSimpleProgramSynthesis;
import ai.synthesis.localsearch.Recovery_SimpleProgramSynthesisForFPTable;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTable;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV3;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV3_unstop_continuous;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV3_unstoppable;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV4;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV4_unstop_continuous;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV4_unstoppable;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV5;
import ai.synthesis.localsearch.SimpleProgramSynthesisForFPTableV5_windows;
import ai.synthesis.localsearch.SinglePortfolioSynthesis;
import ai.synthesis.localsearch.searchImplementation.CumulativeSAComposed;
import ai.synthesis.localsearch.searchImplementation.PortolioSimulatedAnnealing;
import ai.synthesis.localsearch.searchImplementation.SAForFPTable;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV1Elo;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV1_noWeights;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3_unstop_continuous;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3_unstoppable;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV4;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV4_unstop_continuous;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV4_unstoppable;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV5;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV5_windows;
import ai.synthesis.localsearch.searchImplementation.SearchImplementation;
import ai.synthesis.localsearch.searchImplementation.SimulatedAnnealing;
import ai.synthesis.localsearch.searchImplementation.SinglePortolioSimulatedAnnealing;
import ai.synthesis.localsearch.searchImplementation.SketchPortolioSimulatedAnnealing;
import ai.synthesis.localsearch.searchImplementation.SketchSimulatedAnnealing;

/**
 *
 * @author rubens
 */
public class PerformEvaluation {
    
    
    public static void main(String[] args){
        SettingsAlphaDSL.setMode_debug(false);                
        if(SettingsAlphaDSL.isMode_debug()){
            System.err.println("---------MODE DEBUG ACTIVATED!---------");            
        }else{
            System.out.println("---------MODE OFFICIAL ACTIVATED!---------");
        }
        System.out.println("Map "+ SettingsAlphaDSL.get_map());
        //SA self-play
//        SearchImplementation simulatedAnnealing = new SimulatedAnnealing();
//        LocalSearch simAneal = new DoubleProgramSynthesis(simulatedAnnealing);
//        simAneal.performRun();
        
        //testing sketch variations
        //SearchImplementation simu = new SketchSimulatedAnnealing();
       // LocalSearch skSAneal = new DoubleSketchSynthesis(simu);
        //skSAneal.performRun();
        
        //testing sketch portfolio eval variations
        //SketchPortolioSimulatedAnnealing sim = new SketchPortolioSimulatedAnnealing();
        //LocalSearch skSAneal = new DoubleSketchPortfolioSynthesis(sim);
        //skSAneal.performRun();
        
        //testing portfolio eval variations
        //PortolioSimulatedAnnealing sim = new PortolioSimulatedAnnealing();
        //LocalSearch skSAneal = new DoublePortfolioSynthesis(sim);
        //skSAneal.performRun();
        
        //testing single eval variations against portfolio
        //SinglePortolioSimulatedAnnealing sim = new SinglePortolioSimulatedAnnealing();
        //LocalSearch skSAneal = new SinglePortfolioSynthesis(sim);
        //skSAneal.performRun();
        
        //testing normal SA with cumulative indiv. for score fitness.
        //CumulativeSAComposed sim = new CumulativeSAComposed();
        //LocalSearch skSAneal = new CumulativeDoubleProgramSynthesis(sim);
        //skSAneal.performRun();
        
        //testing SA with cumulative indiv. for score fitness for FP.
        //CumulativeSAComposed sim = new CumulativeSAComposed();
        //LocalSearch skSAneal = new CumulativeSimpleProgramSynthesis(sim);
        //skSAneal.performRun();
        
        //testing SA with cumulative indiv. for score fitness for FP from stopped version
        //CumulativeSAComposed sim = new CumulativeSAComposed();
        //LocalSearch skSAneal = new Recovery_CumulativeSimpleProgramSynthesis(sim);
        //skSAneal.performRun();
        
        //testing SA with table for FP
//        SAForFPTable sim = new SAForFPTable();
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTable(sim);
//        skSAneal.performRun();
        
        //testing SA with table for FP and no weights
//        SAForFPTableV1_noWeights sim = new SAForFPTableV1_noWeights();
//        LocalSearch skSAneal = new FPTableV1_noWeights(sim);
//        skSAneal.performRun();
        
        //testing SA with table for FP, no weights and Elo Ratings to control Fp-size
        SAForFPTableV1Elo sim = new SAForFPTableV1Elo();
        LocalSearch skSAneal = new FPTableV1Elo(sim);
        skSAneal.performRun();
        
        //testing SA with table for FP from stopped version 
        //SAForFPTable sim = new SAForFPTable();
        //LocalSearch skSAneal = new Recovery_SimpleProgramSynthesisForFPTable(sim);
        //skSAneal.performRun();
        
        //testing SA with table for FP version 3 (improved solution)
        //SAForFPTableV3 sim = new SAForFPTableV3();
        //LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV3(sim);
        //skSAneal.performRun();
        
        //testing SA with table for FP version 3 unstoppable (improved solution)
//        SAForFPTableV3_unstoppable sim = new SAForFPTableV3_unstoppable();;
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV3_unstoppable(sim);
//        skSAneal.performRun();
        
        //testing SA with table for FP version 3 unstoppable and continuos 
//        SAForFPTableV3_unstop_continuous sim = new SAForFPTableV3_unstop_continuous();
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV3_unstop_continuous(sim);
//        skSAneal.performRun();
        
        //testing SA with table for FP version 4 (improved solution)
        //SAForFPTableV4 sim = new SAForFPTableV4();
        //LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV4(sim);
        //skSAneal.performRun();
        
        //testing SA with table for FP version 4 unstoppable
//        SAForFPTableV4_unstoppable sim = new SAForFPTableV4_unstoppable();
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV4_unstoppable(sim);
//        skSAneal.performRun();
        
        //testing SA with table for FP version 4 unstoppable and continuos 
//        SAForFPTableV4_unstop_continuous sim = new SAForFPTableV4_unstop_continuous();
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV4_unstop_continuous(sim);
//        skSAneal.performRun();
        
        //testing SA with table for FP version 5 
//        SAForFPTableV5 sim = new SAForFPTableV5();
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV5(sim);
//        skSAneal.performRun();
        
        
        //testing SA with table for FP version 5 + windows
//        SAForFPTableV5_windows sim = new SAForFPTableV5_windows();
//        LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV5_windows(sim);
//        skSAneal.performRun();
    }
    
}
