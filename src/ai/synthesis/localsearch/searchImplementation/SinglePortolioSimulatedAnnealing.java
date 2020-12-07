/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.ReduceDSLController;
import ai.synthesis.runners.roundRobinLocal.SmartRRIAxGRunnable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 *
 * @author rubens
 */
public class SinglePortolioSimulatedAnnealing implements SearchImplementation {

    private List<String> scripts;
    private List<Float> winners;

    static Random rand = new Random();

    public List<String> getScripts() {
        return scripts;
    }

    public List<Float> getWinners() {
        return winners;
    }

    @Override
    public DetailedSearchResult run(iDSL sc_base, iDSL sc_improve, int n_steps, float score) {
        System.out.println("S.A. score base = " + score
                + "\nS.A. Script to improve = " + sc_improve.translate() + "\n");
        DetailedSearchResult results = new DetailedSearchResult();
        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) sc_base.clone();
        float best_score = score;
        float temp_current = 1.0f;
        float temp_final = 0.1f;
        float alpha = 0.01f;
        //search
        while (temp_current >= temp_final) {
            for (int i = 0; i < n_steps; i++) {                
                iDSL sc_neighbour = (iDSL) sc_improve.clone();
                //BuilderDSLTreeSingleton.changeNeighbourPassively(sc_neighbour);
                BuilderDSLTreeSingleton.changeNeighbourPassively(sc_neighbour);
                System.out.println("    ** Neighbour =" + sc_neighbour.translate());
                float new_score = evaluate_thread_scripts(sc_neighbour);
                if (new_score == 8.0f) {
                    best_sol = (iDSL) sc_neighbour.clone();
                    best_score = new_score;
                    results.setWinner(best_score);
                    results.setsBase((iDSL) sc_base.clone());
                    results.setsWinner((iDSL) best_sol.clone());
                    return results;
                }
                if (new_score > score) {
                    sc_improve = (iDSL) sc_neighbour.clone();
                    score = new_score;
                    if (score > best_score) {
                        best_sol = (iDSL) sc_improve.clone();
                        best_score = score;
                    }
                } else {
                    //temperature
                    double uniValue = Math.random();
                    float delta = score - new_score;
                    if (uniValue < Math.exp(-delta / temp_current)) {                        
                        if(!sc_neighbour.translate().equals("")){
                            sc_improve = (iDSL) sc_neighbour.clone();
                            score = new_score;
                        }
                        
                    }
                }                
            }
            temp_current = temp_current * (1 - alpha);
            System.out.println("******************************************");
            System.out.println("temperature going doing..." + temp_current);
            System.out.println("Script improve           : " + sc_improve.translate());
            System.out.println("Best script for nkw      : " + best_sol.translate());
            System.out.println("******************************************");
        }
        results.setWinner(best_score);
        results.setsBase(sc_base);
        results.setsWinner(best_sol);
        return results;
    }

    private float evaluate_thread_scripts(iDSL script) {
        //System.out.println("Runnable Simulated Annealing Version");
        SmartRRIAxGRunnable runnerWR1 = new SmartRRIAxGRunnable(script, 0, false);
        SmartRRIAxGRunnable runnerWR2 = new SmartRRIAxGRunnable(script, 0, true);
        SmartRRIAxGRunnable runnerNS1 = new SmartRRIAxGRunnable(script, 1, false);
        SmartRRIAxGRunnable runnerNS2 = new SmartRRIAxGRunnable(script, 1, true);
        SmartRRIAxGRunnable runnerLR1 = new SmartRRIAxGRunnable(script, 2, false);
        SmartRRIAxGRunnable runnerLR2 = new SmartRRIAxGRunnable(script, 2, true);

        try {
            runnerWR1.start();
            runnerWR2.start();
            runnerNS1.start();
            runnerNS2.start();
            runnerLR1.start();
            runnerLR2.start();

            runnerWR1.join();
            runnerWR2.join();
            runnerNS1.join();
            runnerNS2.join();
            runnerLR1.join();
            runnerLR2.join();

            float totalScript2 = runnerWR1.getResult() + runnerWR2.getResult()
                    + runnerNS1.getResult() + runnerNS2.getResult()
                    + runnerLR1.getResult() + runnerLR2.getResult();

            HashSet<ICommand> uniqueCommands = new HashSet<>();
            uniqueCommands.addAll(runnerWR1.getUsedCommands());
            uniqueCommands.addAll(runnerWR2.getUsedCommands());
            uniqueCommands.addAll(runnerNS1.getUsedCommands());
            uniqueCommands.addAll(runnerNS2.getUsedCommands());
            uniqueCommands.addAll(runnerLR1.getUsedCommands());
            uniqueCommands.addAll(runnerLR2.getUsedCommands());

            ReduceDSLController.removeUnactivatedParts(script, new ArrayList<>(uniqueCommands));

            return totalScript2;
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
            return -5.0f;
        }
    }

}
