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
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3_unstop_continuous;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author rubens
 */
public class SimpleProgramSynthesisForFPTableV3_unstop_continuous implements LocalSearch {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SAForFPTableV3_unstop_continuous searchAlgorithm;
    private BuilderDSLTreeSingleton builder;
    private String uniqueID = UUID.randomUUID().toString();
    private HashMap<String, Fp_element> fp_group;

    public SimpleProgramSynthesisForFPTableV3_unstop_continuous(SAForFPTableV3_unstop_continuous search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        this.builder = BuilderDSLTreeSingleton.getInstance();
        this.fp_group = new HashMap<>();
    }

    public SimpleProgramSynthesisForFPTableV3_unstop_continuous(SAForFPTableV3_unstop_continuous search, List<iDSL> initial_fp_group) {
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
        iDSL last_best_iteration = null;
        for (int i = 1; i <= SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {

            float best_score = -9999;
            iDSL best_response_ast = null;

            for (int j = 0; j < SettingsAlphaDSL.get_qtd_iterations_for_SA(); j++) {
                DetailedSearchResult res;
                if(last_best_iteration != null){
                    res = callAlgorithm(last_best_iteration);
                }else{                    
                     res = callAlgorithm(builder.buildS1Grammar());
                }
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
                if(last_best_iteration != null){
                    System.out.println("@@>>>>>>> old last best AST: "+last_best_iteration.translate());
                }
                last_best_iteration = (iDSL) best_response_ast.clone();
                System.out.println("@@>>>>>>> new last best AST: "+last_best_iteration.translate());
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

    private DetailedSearchResult callAlgorithm(iDSL sc_improve) {
        return this.searchAlgorithm.run(sc_improve,fp_group.values());
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
