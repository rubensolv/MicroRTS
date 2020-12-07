/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.ReduceDSLController;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author rubens
 */
public class SimulatedAnnealing extends SearchImplementationAbstract {

    private List<String> scripts;
    private List<Float> winners;
    private String uniqueID = UUID.randomUUID().toString();

    static Random rand = new Random();

    public List<String> getScripts() {
        return scripts;
    }

    public List<Float> getWinners() {
        return winners;
    }

    @Override
    public DetailedSearchResult run(iDSL sc_base, iDSL sc_improve, int n_steps, float score) {
        System.out.println(uniqueID+" S.A. Script base = " + sc_base.translate()
                + "\nS.A. Script to improve = " + sc_improve.translate() + "\n");
        DetailedSearchResult results = new DetailedSearchResult();
        System.out.println("Score passed as default "+ score);
        float ev_score = evaluate_thread_scripts(sc_improve, sc_base);
        System.out.println("Score recalculed "+ ev_score);
        if(ev_score > score){
            score = ev_score;
        }
        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) sc_base.clone();
        float best_score = score;
        float temp_current = SettingsAlphaDSL.get_current_temperature();
        float temp_final = SettingsAlphaDSL.get_final_temperature();
        float alpha = SettingsAlphaDSL.get_alpha();
        //search
        while (temp_current >= temp_final) {
            for (int i = 0; i < n_steps; i++) {
                iDSL sc_neighbour = (iDSL) sc_improve.clone();
                sc_neighbour = get_neighbour_configurated(sc_neighbour, temp_current);
                String base_pre_reduction = sc_neighbour.translate();
                //System.out.println("  **** Temperature: "+ temp_current);
                //System.out.println(uniqueID+"    ** Neighbour =" + sc_neighbour.translate());
                //System.out.println(uniqueID+"    ** base =" + sc_base.translate());
                float new_score = evaluate_thread_scripts(sc_base, sc_neighbour);                
                set_dsl_transformation(base_pre_reduction, sc_neighbour.translate());
                update_dsl_scores(sc_neighbour, new_score);
                if (new_score == 4.0f) {
                    best_sol = (iDSL) sc_neighbour.clone();
                    best_score = new_score;
                    results.setWinner(best_score);
                    results.setsBase((iDSL) sc_base.clone());
                    results.setsWinner((iDSL) best_sol.clone());
                    System.out.println(uniqueID+"    #### Best Score reached! "+sc_base+"  "+best_sol.translate());
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
                        if (!sc_neighbour.translate().equals("")) {
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
            System.out.println("Best script for n0w      : " + best_sol.translate());
            System.out.println("******************************************");
        }
        results.setWinner(best_score);
        results.setsBase(sc_base);
        results.setsWinner(best_sol);
        System.out.println(uniqueID+"    #### Normal execution Finished!!\n"+
                best_sol.translate()+"    ####");
        return results;
    }

    private float evaluate_thread_scripts(iDSL script1, iDSL script2) {
        //System.out.println("Runnable Simulated Annealing Version");

        SmartRRGxGRunnable runner1 = new SmartRRGxGRunnable(script1, script2);
        SmartRRGxGRunnable runner2 = new SmartRRGxGRunnable(script1, script2);
        SmartRRGxGRunnable runner3 = new SmartRRGxGRunnable(script2, script1);
        SmartRRGxGRunnable runner4 = new SmartRRGxGRunnable(script2, script1);

        try {
            runner1.start();
            runner2.start();
            runner3.start();
            runner4.start();

            runner1.join();
            runner2.join();
            runner3.join();
            runner4.join();

            float totalScript2 = 0.0f;
            if (runner1.getWinner() == 1) {
                totalScript2 += runner1.getResult();
            } else if (runner1.getWinner() == -1) {
                totalScript2 += runner1.getResult();
            }
            if (runner2.getWinner() == 1) {
                totalScript2 += runner2.getResult();
            } else if (runner2.getWinner() == -1) {
                totalScript2 += runner2.getResult();
            }

            if (runner3.getWinner() == 0) {
                totalScript2 += runner3.getResult();
            } else if (runner3.getWinner() == -1) {
                totalScript2 += runner3.getResult();
            }
            if (runner4.getWinner() == 0) {
                totalScript2 += runner4.getResult();
            } else if (runner4.getWinner() == -1) {
                totalScript2 += runner4.getResult();
            }

            HashSet<ICommand> uniqueCommands = new HashSet<>();
            uniqueCommands.addAll(runner1.getAllCommandIA2());
            uniqueCommands.addAll(runner2.getAllCommandIA2());
            uniqueCommands.addAll(runner3.getAllCommandIA1());
            uniqueCommands.addAll(runner4.getAllCommandIA1());
            ReduceDSLController.removeUnactivatedParts(script2, new ArrayList<>(uniqueCommands));

            return totalScript2;
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
            return -5.0f;
        }
    }
    
}
