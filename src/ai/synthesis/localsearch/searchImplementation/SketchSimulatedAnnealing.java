/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderSketchDSLSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.List;
import java.util.Random;

/**
 *
 * @author rubens
 */
public class SketchSimulatedAnnealing implements SearchImplementation {

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
        System.out.println("S.A. Script base = " + sc_base.translate()
                + "\nS.A. Script to improve = " + sc_improve.translate() + "\n");
        DetailedSearchResult results = new DetailedSearchResult();
        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) sc_base.clone();
        float best_score = score;
        float temp_current = 100.0f;
        float temp_final = 0.1f;
        float alpha = 0.01f;
        //search
        while (temp_current >= temp_final) {
            for (int i = 0; i < n_steps; i++) {
                iDSL sc_neighbour = (iDSL) sc_improve.clone();
                //BuilderDSLTreeSingleton.changeNeighbourPassively(sc_neighbour);
                BuilderSketchDSLSingleton.neightbourParametersChange(sc_neighbour);
                System.out.println("    ** Neighbour =" + sc_neighbour.translate());
                float new_score = evaluate_thread_scripts(sc_base,sc_neighbour);
                if (new_score == 4.0f) {
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
                        //System.out.println("Sc changed from \n" + sc_improve.translate()
                        //        + " to " + sc_neighbour.translate() + "\n"
                        //        + " with uniValue= " + uniValue + " and delta= "
                        //        + Math.exp(-delta / temp_current));
                        sc_improve = (iDSL) sc_neighbour.clone();
                        score = new_score;
                    }/* else if (Math.random() < Math.exp(-delta / temp_current)) {
                        //included perturbation change
                        //System.out.println("Perturbation eval");
                        ScriptsTable scrTable = new ScriptsTable("");
                        iDSL sc_perturb = BuilderDSLTreeSingleton.getInstance().buildS1Grammar();
                        float score_perturb = evaluate_thread_scripts(sc_neighbour.translate(), 
                                sc_perturb.translate());
                        if (score_perturb > new_score) {
                            sc_improve = sc_perturb;
                            score = score_perturb;
                        }
                    }*/
                }
            }
            temp_current = temp_current * (1 - alpha);
            System.out.println("temperature going doing..." + temp_current);
            System.out.println("Script improve: " + sc_improve.translate());
        }
        results.setWinner(best_score);
        results.setsBase(sc_base);
        results.setsWinner(best_sol);
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
            //final result considering script 2
            /* remove to simplify the initial tests.
            if (totalScript2 > 0.0f) {
                return totalScript2 / 4.0f;
            }
             */
            return totalScript2;
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
            return -5.0f;
        }
    }

}
