/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.twophasessa;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.localsearch.Fp_element;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3init;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author rubens
 */
public class SimpleProgramSynthesisForFPTableV3initGroup {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SAForFPTableV3init searchAlgorithm;
    private BuilderDSLTreeSingleton builder;
    private String uniqueID = UUID.randomUUID().toString();
    private HashMap<String, Fp_element> fp_group;

    public SimpleProgramSynthesisForFPTableV3initGroup(SAForFPTableV3init search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        builder = BuilderDSLTreeSingleton.getInstance();
        this.fp_group = new HashMap<>();
    }
    
    public SimpleProgramSynthesisForFPTableV3initGroup(SAForFPTableV3init search, List<iDSL> initial_fp_group) {
        this(search);
        for (iDSL ast : initial_fp_group) {
            check_inclusion(ast);
        }
    }

    
    public List performRun(iDSL ScrI, iDSL ScrM) {
        System.out.println("Program ID " + uniqueID);
        String path = System.getProperty("user.dir").concat("/logs2/");         
        int count = 1;
        int stage = 0;
        iDSL initial = (iDSL) ScrM.clone();
        
        for (int i = 1; i <= SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {
            float best_score = -9999;
            iDSL best_response_ast = null;
            
            for (int j = 0; j < SettingsAlphaDSL.get_qtd_iterations_for_SA(); j++) {
            	
                DetailedSearchResult res = callAlgorithm(initial);
                
                if (res.getWinner() > best_score && (!res.wasDefeat_by_fp_group())){
                    System.out.println(">>>>>> Iteration "+ i +" step " + j
                            +" current best score: " + best_score + " new best score: " + res.getWinner()+
                            "\n New ast: "+ res.getsWinner().translate()+
                            "\n>>>>>>>>>>>>>>>>>");
                    best_score = res.getWinner();
                    best_response_ast = res.getsWinner();                    
                }
            }
            
            if(best_response_ast == null){
            	System.out.println("---- Finalização com busca mal sucedida :(");
            	System.out.println("Search stopped by defeat condition. SA(FP) was unable to find better solution"
                        + " during "+ SettingsAlphaDSL.get_qtd_iterations_for_SA()+ " iterations.");
            	System.out.println("---- ---- ---- ---- ----");
                return new ArrayList(this.fp_group.keySet());
            } else {
            	System.out.println("---- Finalização com busca bem sucedida :D");
            	System.out.println("Salvando DSL da segunda fase com ID: " + uniqueID);
	            SerializableController.saveSerializable(best_response_ast, "dsl_" + uniqueID + "_id_" + count + ".ser", path);
	                count++;
	                System.out.println("@@>>>>>>> Included new AST: "+ best_response_ast.translate());
	                check_inclusion((iDSL) best_response_ast.clone());
	            
	            System.out.print("\n#######\n Evaluation " + i +
	                    " \nBest Ast: " + best_response_ast.translate()
	                    + "\n#######\n");
	            
	            System.out.println("fp_group atualizado:");
	            System.out.println(fp_group);
	            
	            System.out.println("---- ---- ---- ---- ----\n");
	            
	            System.gc();
            
            }
        }
        
        return new ArrayList(this.fp_group.keySet());
    }

    private DetailedSearchResult callAlgorithm(iDSL initial) {
        return this.searchAlgorithm.run(fp_group.values(), initial);
        //return this.searchAlgorithm.run(fp_group.values());
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
