/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch;

import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.ReduceDSLController;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SearchImplementation;
import ai.synthesis.runners.reportEvaluation.reportRoundRobinEvaluation;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import ai.synthesis.runners.roundRobinLocal.SmartRRIAxGRunnable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubens
 */
public class SinglePortfolioSynthesis implements LocalSearch {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SearchImplementation searchAlgorithm;
    private BuilderDSLTreeSingleton builder;

    public SinglePortfolioSynthesis(SearchImplementation search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        builder = BuilderDSLTreeSingleton.getInstance();
    }

    @Override
    public List performRun() {
        System.out.println("Class:"+this.getClass().getSimpleName());
        System.out.println("Class:"+searchAlgorithm.getClass().getSimpleName());
        System.out.println("----------------------------------");
        iDSL iSc1 = builder.buildS1Grammar();
        System.out.println("Initial S1 =" + iSc1.translate());

        float[] itens = processThreadsMatch(iSc1);

        DetailedSearchResult res = callAlgorithm(iSc1, itens);
        for (int i = 0; i < 100; i++) {
            itens = processThreadsMatch(res.getsWinner());
            System.out.print("\n#######\n Evaluation " + i + " pos S.A.:\n Script 1: "
                    + res.getsBase().translate() + " \nScript2: " + res.getsWinner().translate()
                    + " \nWinner: " + (itens[0] + 1) + " \nWinner Score: " + itens[1]
                    + "\n#######\n");
            res = callAlgorithm(res.getsWinner(), itens);
            System.gc();
        }
        res.print();
        return new ArrayList();
    }

    private float[] processThreadsMatch(iDSL script1) {
        float[] itens = new float[2];
        float eval1 = evaluate_thread_scripts(script1);
        itens[0] = 0f;
        itens[1] = eval1;
        return itens;
    }

    private float evaluate_thread_scripts(iDSL script) {
        //System.out.println("Runnable Simulated Annealing Version");
        SmartRRIAxGRunnable runnerWR1 = new SmartRRIAxGRunnable(script, 0, false);
        SmartRRIAxGRunnable runnerWR2 = new SmartRRIAxGRunnable(script, 0, true);
        //t1
        SmartRRIAxGRunnable runnerT1_1 = new SmartRRIAxGRunnable(script, 1, false);
        SmartRRIAxGRunnable runnerT1_2 = new SmartRRIAxGRunnable(script, 1, true);
        //t2
        SmartRRIAxGRunnable runnerT2_1 = new SmartRRIAxGRunnable(script, 2, false);
        SmartRRIAxGRunnable runnerT2_2 = new SmartRRIAxGRunnable(script, 2, true);
        //t3
        SmartRRIAxGRunnable runnerT3_1 = new SmartRRIAxGRunnable(script, 3, false);
        SmartRRIAxGRunnable runnerT3_2 = new SmartRRIAxGRunnable(script, 3, true);
        

        try {
            runnerWR1.start();
            runnerWR2.start();
            runnerT1_1.start();
            runnerT1_2.start();
            runnerT2_1.start();
            runnerT2_2.start();
            runnerT3_1.start();
            runnerT3_2.start();

            runnerWR1.join();
            runnerWR2.join();
            runnerT1_1.join();
            runnerT1_2.join();
            runnerT2_1.join();
            runnerT2_2.join();
            runnerT3_1.join();
            runnerT3_2.join();
        

            float totalScript2 = runnerWR1.getResult() + runnerWR2.getResult()
                    + runnerT1_1.getResult() + runnerT1_2.getResult()
                    + runnerT2_1.getResult() + runnerT2_2.getResult()
                    + runnerT3_1.getResult() + runnerT3_2.getResult();
            
            HashSet<ICommand> uniqueCommands = new HashSet<>();
            uniqueCommands.addAll(runnerWR1.getUsedCommands());
            uniqueCommands.addAll(runnerWR2.getUsedCommands());
            uniqueCommands.addAll(runnerT1_1.getUsedCommands());
            uniqueCommands.addAll(runnerT1_2.getUsedCommands());
            uniqueCommands.addAll(runnerT2_1.getUsedCommands());
            uniqueCommands.addAll(runnerT2_2.getUsedCommands());
            uniqueCommands.addAll(runnerT3_1.getUsedCommands());
            uniqueCommands.addAll(runnerT3_2.getUsedCommands());

            ReduceDSLController.removeUnactivatedParts(script, new ArrayList<>(uniqueCommands));

            return totalScript2;
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
            return -5.0f;
        }
    }

    private DetailedSearchResult callAlgorithm(iDSL script1, float[] itens) {        
        DetailedSearchResult results;
        reportRoundRobinEvaluation report = new reportRoundRobinEvaluation();
        
        report.setIA(script1.translate());
        report.start();
        results = this.searchAlgorithm.run(script1, script1, 10, itens[1]);
        
        try {
            report.join();
            System.out.println("***** Total Victories against WR, RR e LR: " + report.getMatchesResults());
        } catch (InterruptedException ex) {
            Logger.getLogger(SinglePortfolioSynthesis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

}
