/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.ReduceDSLController;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SAForFPTable;
import ai.synthesis.runners.reportEvaluation.reportRoundRobinEvaluation;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rubens
 */
public class SimpleProgramSynthesisForFPTable implements LocalSearch {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SAForFPTable searchAlgorithm;
    private BuilderDSLTreeSingleton builder;
    private String uniqueID = UUID.randomUUID().toString();
    private HashMap<String, Fp_element> cum_ind;

    public SimpleProgramSynthesisForFPTable(SAForFPTable search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        builder = BuilderDSLTreeSingleton.getInstance();
        this.cum_ind = new HashMap<>();
    }

    @Override
    public List performRun() {               
        iDSL iSc1 = builder.buildS1Grammar();
        //iDSL iSc2 = builder.buildS1Grammar();
        System.out.println("Program ID " + uniqueID);        
        System.out.println("Class:"+this.getClass().getSimpleName());
        System.out.println("Class:"+searchAlgorithm.getClass().getSimpleName());
        System.out.println("----------------------------------");        
        System.out.println("Initial S1 =" + iSc1.translate());        

        //float[] itens = processThreadsMatch(iSc1, iSc1);
        DetailedSearchResult res = callAlgorithm(iSc1,0);
        for (int i = 1; i <= SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {
            System.out.print("\n#######\n Running evaluation " + i + " pos S.A.:\n Script 1: "
                    + res.getsBase().translate() + " \nScript2: " + res.getsWinner().translate());            
            System.out.print("\n#######\n Evaluation " + i + " pos S.A.:\n Script 1: "
                    + res.getsBase().translate() + " \nScript2: " + res.getsWinner().translate()                    
                    + "\n#######\n");
            res = callAlgorithm(res.getsWinner(), i);
            System.gc();
        }
        res.print();
        return new ArrayList();
    }

    private float[] processThreadsMatch(iDSL script1, iDSL script2) {
        SmartRRGxGRunnable runner1 = new SmartRRGxGRunnable(script1, script2);
        SmartRRGxGRunnable runner2 = new SmartRRGxGRunnable(script2, script1);
        float[] itens = new float[2];
        try {
            runner1.start();
            runner2.start();
            runner1.join();
            runner2.join();

            float score1 = 0f, score2 = 0f;
            if (runner1.getWinner() == 0) {
                score1++;
            } else if (runner1.getWinner() == 1) {
                score2++;
            } else {
                score1 += 0.5f;
                score2 += 0.5f;
            }
            if (runner2.getWinner() == 0) {
                score2++;
            } else if (runner2.getWinner() == 1) {
                score1++;
            } else {
                score1 += 0.5f;
                score2 += 0.5f;
            }

            HashSet<ICommand> uniqueCommands = new HashSet<>();
            uniqueCommands.addAll(runner1.getAllCommandIA1());
            uniqueCommands.addAll(runner2.getAllCommandIA2());
            ReduceDSLController.removeUnactivatedParts(script1, new ArrayList<>(uniqueCommands));

            uniqueCommands.clear();
            uniqueCommands.addAll(runner1.getAllCommandIA2());
            uniqueCommands.addAll(runner2.getAllCommandIA1());
            ReduceDSLController.removeUnactivatedParts(script2, new ArrayList<>(uniqueCommands));

            //the results here are multiplied by 2 considering that they
            //will be compared with more battles. Fix it in the future.
            if (score1 >= score2) {
                itens[0] = 0f;
                itens[1] = score1 * 2;
                if (script1.translate().equals("") && (!script2.translate().equals(""))) {
                    itens[0] = 1f;
                    itens[1] = score2 * 2;
                }
                return itens;
            } else {
                itens[0] = 1f;
                itens[1] = score2 * 2;
                return itens;
            }
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
        }
        return itens;
    }

    private DetailedSearchResult callAlgorithm(iDSL script1, int count) {
        String path = System.getProperty("user.dir").concat("/logs2/");
        DetailedSearchResult results;
        reportRoundRobinEvaluation report = new reportRoundRobinEvaluation();
        String conf = "";

        SerializableController.saveSerializable(script1, "dsl_" + uniqueID + "_id_" + count + ".ser", path);
        report.setIA(script1.translate());
        conf = script1.translate();
        report.start();
        check_inclusion((iDSL) script1.clone());
        results = this.searchAlgorithm.run(cum_ind.values(), script1,
                SettingsAlphaDSL.get_number_sa_steps(), 2, script1);

        try {
            report.join();
            System.out.println("***** Total Victories against WR, RR e LR round " + count + ": " + report.getMatchesResults() + ""
                    + "\n" + report.getsIA1() + "\n" + conf);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimpleProgramSynthesisForFPTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    private void check_inclusion(iDSL idsl) {
        String elem = idsl.translate();
        if(this.cum_ind.containsKey(elem)){
            this.cum_ind.get(elem).incrementCount();
        }else{
            Fp_element newElem = new Fp_element(idsl);
            this.cum_ind.put(elem, newElem);
        }
    }
    
    

}
