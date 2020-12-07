/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import util.Pair;

/**
 *
 * @author rubens
 */
public class SimpleProgramSynthesisForFPTableV4 implements LocalSearch {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SAForFPTableV4 searchAlgorithm;
    private BuilderDSLTreeSingleton builder;
    private String uniqueID = UUID.randomUUID().toString();
    private HashMap<String, Fp_element> fp_group;

    public SimpleProgramSynthesisForFPTableV4(SAForFPTableV4 search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        this.builder = BuilderDSLTreeSingleton.getInstance();
        this.fp_group = new HashMap<>();
    }

    public SimpleProgramSynthesisForFPTableV4(SAForFPTableV4 search, List<iDSL> initial_fp_group) {
        this(search);
        for (iDSL ast : initial_fp_group) {
            check_inclusion(ast);
        }
    }

    @Override
    public List performRun() {
        System.out.println("Program ID " + uniqueID);
        System.out.println("Class:" + this.getClass().getSimpleName());
        System.out.println("Class:" + searchAlgorithm.getClass().getSimpleName());
        System.out.println("----------------------------------");
        String path = System.getProperty("user.dir").concat("/logs2/");
        List<Pair<String, Float>> last_best_matchs = new ArrayList<>();
        int count = 1;
        for (int i = 1; i <= SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {

            float best_score = -9999;
            iDSL best_response_ast = null;
            List<Pair<String, Float>> best_matchs = new ArrayList<>();
            for (int j = 0; j < SettingsAlphaDSL.get_qtd_iterations_for_SA(); j++) {

                DetailedSearchResult res = callAlgorithm();
                if ((!res.wasDefeat_by_fp_group()) && res.getWinner() > best_score) {
                    System.out.println(">>>>>> Iteration " + i + " step " + j
                            + " current best score: " + best_score + " new best score: " + res.getWinner()
                            + "\n New ast: " + res.getsWinner().translate()
                            + "\n>>>>>>>>>>>>>>>>>");
                    best_score = res.getWinner();
                    best_response_ast = (iDSL) res.getsWinner().clone();
                    best_matchs = res.getMatchs();
                }
            }
            if (best_response_ast == null) {
                System.out.println("Search stopped by defeat condition. SA(FP) was unable to find better solution"
                        + " during " + SettingsAlphaDSL.get_qtd_iterations_for_SA() + " iterations.");
                return new ArrayList(this.fp_group.keySet());
            }

            if (!best_than_last_ast_included(last_best_matchs, best_matchs)) {
                System.out.println("##>>> Solution ignored by non general improvement in qualitative evaluation!"
                        + "\n#######\n Evaluation " + i
                        + " \nBest Ast: " + best_response_ast.translate()
                        + "\n#######\n");
            } else {
                SerializableController.saveSerializable(best_response_ast, "dsl_" + uniqueID + "_id_" + count + ".ser", path);
                count++;
                System.out.println("@@>>>>>>> Included new AST: " + best_response_ast.translate());
                check_inclusion((iDSL) best_response_ast);
                last_best_matchs = best_matchs;
                System.out.print("\n#######\n Evaluation " + i
                        + " \nBest Ast: " + best_response_ast.translate()
                        + "\n#######\n");
            }

            System.gc();
        }
        return new ArrayList(this.fp_group.keySet());
    }

    private DetailedSearchResult callAlgorithm() {
        return this.searchAlgorithm.run(fp_group.values());
    }

    private void check_inclusion(iDSL idsl) {
        String elem = idsl.translate();
        if (this.fp_group.containsKey(elem)) {
            this.fp_group.get(elem).incrementCount();
        } else {
            Fp_element newElem = new Fp_element(idsl);
            this.fp_group.put(elem, newElem);
        }
    }

    private boolean best_than_last_ast_included(List<Pair<String, Float>> last_best_matchs, List<Pair<String, Float>> best_matchs) {

        if (last_best_matchs.isEmpty() || (last_best_matchs.size() == best_matchs.size())) {
            return true;
        }
        float last = 0.0f;
        List<String> elements = new ArrayList<>();
        for (Pair<String, Float> t : last_best_matchs) {
            last += t.m_b;
            elements.add(t.m_a);
        }
        float best = 0.0f;
        float dif = 0.0f;
        boolean use_dif = false;
        for (Pair<String, Float> t : best_matchs) {
            if (elements.contains(t.m_a)) {
                best += t.m_b;
            } else {
                dif = t.m_b;
                use_dif = true;
            }
        }
        float dif_enemy = 0;
        if (use_dif) {
            dif_enemy = 4 - dif;
        }
        if ((best + dif) < (last + dif_enemy)) {
            return false;
        }
        return true;
    }

}
