/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.localsearch.Fp_element;
import java.util.ArrayList;
import java.util.Collection;
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
import util.Pair;

/**
 *
 * @author rubens
 */
public class SAForFPTableV4_unstop_continuous extends SearchImplementationAbstract {

    private List<String> scripts;
    private List<Float> winners;
    private String uniqueID = UUID.randomUUID().toString();
    private BuilderDSLTreeSingleton builder = BuilderDSLTreeSingleton.getInstance();

    static Random rand = new Random();

    public List<String> getScripts() {
        return scripts;
    }

    public List<Float> getWinners() {
        return winners;
    }

    public DetailedSearchResult run(iDSL sc_improve, Collection<Fp_element> sc_base) {
        DetailedSearchResult results = new DetailedSearchResult();
        DetailedSearchResult score = null;        
        try {
            if(sc_base.isEmpty()){                
                score = new DetailedSearchResult(sc_improve, 2.0f, false);
            }else{
                score = evaluate_list_enemies(sc_base, sc_improve);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SAForFPTableV4_unstop_continuous.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SAForFPTableV4_unstop_continuous.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("@@@@@@@@@@ \nS.A. Script base (" + sc_base.size() + ") = \n" + get_string(sc_base)
                + "\nScore Against Opponents = " + score.getWinner()
                + "\nS.A. Script to improve = " + sc_improve.translate() + "\n@@@@@@@@@@");

        //SimulatedAnnealing settings
        iDSL best_sol = (iDSL) sc_improve.clone();
        DetailedSearchResult best_score = score;
        float temp_current = SettingsAlphaDSL.get_current_temperature();
        float temp_final = SettingsAlphaDSL.get_final_temperature();
        float alpha = SettingsAlphaDSL.get_alpha();
        int num_iter = 1;
        //search
        while (temp_current >= temp_final) {
            iDSL sc_neighbour = (iDSL) sc_improve.clone();
            sc_neighbour = get_neighbour_configurated(sc_neighbour, temp_current);
            String base_pre_reduction = sc_neighbour.translate();
            //System.out.println("    ** Neighbour =" + sc_neighbour.translate());
            //System.out.println(uniqueID+"    ** base =" + sc_base.translate());
            DetailedSearchResult new_score = null;
            try {
                if(sc_base.isEmpty()){
                    Fp_element temp = new Fp_element(best_sol);
                    Collection<Fp_element> t_base = new ArrayList<>();
                    t_base.add(temp);
                    new_score = evaluate_list_enemies(t_base, sc_neighbour);
                }else{
                    new_score = evaluate_list_enemies(sc_base, sc_neighbour);
                }
                //System.out.println("    ** Neighbour (cleaned) =" + sc_neighbour.translate());
                set_dsl_transformation(base_pre_reduction, sc_neighbour.translate());
                update_dsl_scores(sc_neighbour, new_score.getWinner());
            } catch (InterruptedException ex) {
                Logger.getLogger(SAForFPTableV4_unstop_continuous.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(SAForFPTableV4_unstop_continuous.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (new_score.getWinner() == 4.0f) {
                best_sol = (iDSL) sc_neighbour.clone();
                best_score = new_score;
                results.setWinner(best_score.getWinner());                
                results.setDefeat_by_fp_group(best_score.wasDefeat_by_fp_group());
                results.setsWinner((iDSL) best_sol.clone());
                results.setMatchs(best_score.getMatchs());
                System.out.println(uniqueID + "    #### Best Score reached!"
                        + " \nImproved AST : " + best_sol.translate());
                return results;
            }
            if (new_score.getWinner() > score.getWinner()) {
                sc_improve = (iDSL) sc_neighbour.clone();
                score = new_score;
                if (score.getWinner() > best_score.getWinner()) {
                    best_sol = (iDSL) sc_improve.clone();
                    best_score = score;
                }
            } else {
                //temperature
                double uniValue = Math.random();
                float delta = score.getWinner() - new_score.getWinner();
                if (uniValue < Math.exp(-delta / temp_current)) {
                    if (!sc_neighbour.translate().equals("")) {
                        sc_improve = (iDSL) sc_neighbour.clone();
                        score = new_score;
                    }
                }
            }

            //temp_current = (float) (SettingsAlphaDSL.get_current_temperature() / Math.log(num_iter+1));            
            temp_current = temp_current * (1 - alpha);
            num_iter++;
            System.out.println("******************************************");
            System.out.println("temperature going doing..." + temp_current);
            System.out.println("Script improve           : " + sc_improve.translate());
            System.out.println("Best script for now      : " + best_sol.translate());
            System.out.println("******************************************");
        }
        results.setWinner(best_score.getWinner());        
        results.setsWinner(best_sol);
        results.setDefeat_by_fp_group(best_score.wasDefeat_by_fp_group());
        results.setMatchs(score.getMatchs());
        System.out.println(uniqueID + "    #### Normal execution Finished!!\n"
                + best_sol.translate() + "    ####");
        return results;
    }

    @Override
    public DetailedSearchResult run(iDSL sc_base, iDSL sc_improve, int n_steps, float winner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private DetailedSearchResult evaluate_list_enemies(Collection<Fp_element> sc_base, iDSL sc_neighbour) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        DetailedSearchResult t_result = new DetailedSearchResult();
        
        float new_score = 0.0f;
        Collection<Callable<resultsThreadsWithAIs>> tasks = new ArrayList<>();
        for (Fp_element dSL : sc_base) {            
            callableMultiThreadsBattleWithAIs t = new callableMultiThreadsBattleWithAIs(dSL.getAst(),
                    sc_neighbour, dSL.getCounter());
            tasks.add(t);
        }
        List<Future<resultsThreadsWithAIs>> taskFutureList = executor.invokeAll(tasks);        
        boolean loser = false;
        float t_score = 0.0f;
        for (Future<resultsThreadsWithAIs> future : taskFutureList) {
            t_score = future.get().result;
            t_result.addMatch(new Pair<>(future.get().enemy, t_score)); // include detailed matchs
            new_score += t_score;
            if(t_score < 2.0f){
                loser = true;
            }            
        }
        executor.shutdown();        
        t_result.setDefeat_by_fp_group(loser); // set the defeat condition
        t_result.setWinner((new_score / get_full_size(sc_base))); // total score
        t_result.setsWinner(sc_neighbour); // base dsl.
        return t_result;
    }

    private String get_string(Collection<Fp_element> sc_base) {
        String ret = "";
        for (Fp_element el : sc_base) {
            ret += "|" + el.getAst().translate() + " | weight: " + el.getCounter() + " |\n";
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
