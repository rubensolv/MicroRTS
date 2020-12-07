/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.ReduceDSLController;
import ai.synthesis.localsearch.Fp_element;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rubens
 */
public class SAForFPTableV1_noWeights  extends SearchImplementationAbstract{

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

    
    public DetailedSearchResult run(Collection<Fp_element> sc_base, iDSL sc_improve, int n_steps, float score, iDSL base) {        
        DetailedSearchResult results = new DetailedSearchResult();        
        try {
            score = evaluate_list_enemies(sc_base, sc_improve);
        } catch (InterruptedException ex) {
            Logger.getLogger(SAForFPTableV1_noWeights.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SAForFPTableV1_noWeights.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("@@@@@@@@@@ \nS.A. Script base ("+sc_base.size()+") = \n" + get_string(sc_base)
                + "\nScore Against Opponents = " + score
                + "\nS.A. Script to improve = " + sc_improve.translate() + "\n@@@@@@@@@@");
        
        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) base.clone();
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
//                System.out.println("    ** Neighbour =" + sc_neighbour.translate());
                //System.out.println(uniqueID+"    ** base =" + sc_base.translate());
                float new_score = 0.0f;                
                try {
                    new_score = evaluate_list_enemies(sc_base, sc_neighbour);                           
                    set_dsl_transformation(base_pre_reduction, sc_neighbour.translate());
                    update_dsl_scores(sc_neighbour, new_score);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SAForFPTableV1_noWeights.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(SAForFPTableV1_noWeights.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (new_score == 4.0f) {
                    best_sol = (iDSL) sc_neighbour.clone();
                    best_score = new_score;
                    results.setWinner(best_score);
                    results.setsBase((iDSL) base.clone());
                    results.setsWinner((iDSL) best_sol.clone());
                    System.out.println(uniqueID+"    #### Best Score reached! \nBase AST: "
                            +base.translate()+" \nImproved AST : "+best_sol.translate());
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
            System.out.println("Best script for now      : " + best_sol.translate());
            System.out.println("******************************************");
        }
        results.setWinner(best_score);
        results.setsBase(base);
        results.setsWinner(best_sol);
        System.out.println(uniqueID+"    #### Normal execution Finished!!\n"+
                best_sol.translate()+"    ####");
        return results;
    }

    @Override
    public DetailedSearchResult run(iDSL sc_base, iDSL sc_improve, int n_steps, float winner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private float evaluate_list_enemies(Collection<Fp_element> sc_base, iDSL sc_neighbour) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        float new_score = 0.0f;
        Collection<Callable<resultsThreadsWithWeight>> tasks = new ArrayList<>();
        for (Fp_element dSL : sc_base) {
            //new_score += evaluate_thread_scripts(dSL, sc_neighbour);   
            callableMultiThreadsBattleWithWeight t = new callableMultiThreadsBattleWithWeight(dSL.getAst(), 
                    sc_neighbour, dSL.getCounter());
            tasks.add(t);
        }
        List<Future<resultsThreadsWithWeight>> taskFutureList =executor.invokeAll(tasks);
        HashSet<ICommand> uniqueCommands = new HashSet<>();
        for (Future<resultsThreadsWithWeight> future : taskFutureList) {
            new_score += future.get().result;
            uniqueCommands.addAll(future.get().uniqueCommands);
        }
        executor.shutdown();
        ReduceDSLController.removeUnactivatedParts(sc_neighbour, new ArrayList<>(uniqueCommands));
        return new_score/get_full_size(sc_base);         
    }

    private String get_string(Collection<Fp_element> sc_base) {
        String ret = "";
        for (Fp_element el : sc_base) {
            ret += "|"+el.getAst().translate()+" | weight: "+el.getCounter()+" |\n";
        }

        return ret;
    }

    private float get_full_size(Collection<Fp_element> sc_base) {
        float value = 0.0f;
        for (Fp_element fp_element : sc_base) {
            value += (float) fp_element.getCounter();
        }
        return value;
    }
    
}
