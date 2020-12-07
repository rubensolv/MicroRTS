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
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3_unstoppable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author rubens
 */
public class SimpleProgramSynthesisForFPTableV3_unstoppable implements LocalSearch {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SAForFPTableV3_unstoppable searchAlgorithm;
    private BuilderDSLTreeSingleton builder;
    private String uniqueID = UUID.randomUUID().toString();
    private HashMap<String, Fp_element> fp_group;

    public SimpleProgramSynthesisForFPTableV3_unstoppable(SAForFPTableV3_unstoppable search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        this.builder = BuilderDSLTreeSingleton.getInstance();
        this.fp_group = new HashMap<>();
    }

    public SimpleProgramSynthesisForFPTableV3_unstoppable(SAForFPTableV3_unstoppable search, List<iDSL> initial_fp_group) {
        this(search);
        for (iDSL ast : initial_fp_group) {
            check_inclusion(ast);
        }
    }

    @Override
    public List performRun() {
        System.out.println("Program ID " + uniqueID);
        System.out.println("Class:"+this.getClass().getSimpleName());
        System.out.println("Class:"+searchAlgorithm.getClass().getSimpleName());
        System.out.println("----------------------------------");
        String path = System.getProperty("user.dir").concat("/logs2/");
        int count = 1;
        for (int i = 1; i <= SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {

            float best_score = -9999;
            iDSL best_response_ast = null;

            for (int j = 0; j < SettingsAlphaDSL.get_qtd_iterations_for_SA(); j++) {

                DetailedSearchResult res = callAlgorithm();
                if ((!res.wasDefeat_by_fp_group()) && res.getWinner() > best_score) {
                    System.out.println(">>>>>> Iteration " + i + " step " + j
                            + " current best score: " + best_score + " new best score: " + res.getWinner()
                            + "\n New ast: " + res.getsWinner().translate()
                            + "\n>>>>>>>>>>>>>>>>>");
                    best_score = res.getWinner();
                    best_response_ast = (iDSL) res.getsWinner().clone();
                }
            }
            if (best_response_ast != null) {
                SerializableController.saveSerializable(best_response_ast, "dsl_" + uniqueID + "_id_" + count + ".ser", path);
                count++;
                System.out.println("@@>>>>>>> Included new AST: " + best_response_ast.translate());
                check_inclusion((iDSL) best_response_ast);
                System.out.print("\n#######\n Evaluation " + i
                    + " \nBest Ast: " + best_response_ast.translate()
                    + "\n#######\n");
            }else{
                System.out.println("@@>>>>>>> Any AST was included! ");
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

}
