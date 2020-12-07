/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch;

import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderSketchDSLSingleton;
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
public class DoubleSketchPortfolioSynthesis implements LocalSearch {

    //private final String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
    //private ScriptsTable scrTable;
    private SearchImplementation searchAlgorithm;
    private BuilderSketchDSLSingleton builder;

    public DoubleSketchPortfolioSynthesis(SearchImplementation search) {
        //this.scrTable = new ScriptsTable(pathTableScripts);
        this.searchAlgorithm = search;
        builder = BuilderSketchDSLSingleton.getInstance();
    }

    @Override
    public List performRun() {
        System.out.println("Class:"+this.getClass().getSimpleName());
        System.out.println("Class:"+searchAlgorithm.getClass().getSimpleName());
        System.out.println("----------------------------------");
        iDSL iSc1 = builder.getSketchTypeTwo();
        iDSL iSc2 = builder.getSketchTypeTwo();
        System.out.println("Initial S1 =" + iSc1.translate());
        System.out.println("Initial S2 =" + iSc2.translate() + "\n");

        float[] itens = processThreadsMatch(iSc1, iSc2);

        DetailedSearchResult res = callAlgorithm(iSc1, iSc2, itens);
        for (int i = 0; i < 100; i++) {
            itens = processThreadsMatch(res.getsBase(), res.getsWinner());
            System.out.print("\n#######\n Evaluation " + i + " pos S.A.:\n Script 1: "
                    + res.getsBase().translate() + " \nScript2: " + res.getsWinner().translate()
                    + " \nWinner: " + (itens[0] + 1) + " \nWinner Score: " + itens[1]
                    + "\n#######\n");
            res = callAlgorithm(res.getsBase(), res.getsWinner(), itens);
            System.gc();
        }
        res.print();
        return new ArrayList();
    }

    private float[] processThreadsMatch(iDSL script1, iDSL script2) {
        float[] itens = new float[2];
        float eval1 = evaluate_thread_scripts(script1);
        float eval2 = evaluate_thread_scripts(script2);                
        if (eval1 >= eval2) {
            itens[0] = 0f;
            itens[1] = eval1;
            return itens;
        } else {
            itens[0] = 1f;
            itens[1] = eval2;
            return itens;
        }        
    }

    private float evaluate_thread_scripts(iDSL script) {
        //System.out.println("Runnable Simulated Annealing Version");
        SmartRRIAxGRunnable runnerWR1 = new SmartRRIAxGRunnable(script, 0, false);
        SmartRRIAxGRunnable runnerWR2 = new SmartRRIAxGRunnable(script, 0, true);
        SmartRRIAxGRunnable runnerNS1 = new SmartRRIAxGRunnable(script, 1, false);
        SmartRRIAxGRunnable runnerNS2 = new SmartRRIAxGRunnable(script, 1, true);
        SmartRRIAxGRunnable runnerLR1 = new SmartRRIAxGRunnable(script, 2, false);
        SmartRRIAxGRunnable runnerLR2 = new SmartRRIAxGRunnable(script, 2, true);

        try {
            runnerWR1.start();
            runnerWR2.start();
            runnerNS1.start();
            runnerNS2.start();
            runnerLR1.start();
            runnerLR2.start();

            runnerWR1.join();
            runnerWR2.join();
            runnerNS1.join();
            runnerNS2.join();
            runnerLR1.join();
            runnerLR2.join();

            float totalScript2 = runnerWR1.getResult() + runnerWR2.getResult()
                    + runnerNS1.getResult() + runnerNS2.getResult()
                    + runnerLR1.getResult() + runnerLR2.getResult();
            
            HashSet<ICommand> uniqueCommands = new HashSet<>();
            uniqueCommands.addAll(runnerWR1.getUsedCommands());
            uniqueCommands.addAll(runnerWR2.getUsedCommands());
            uniqueCommands.addAll(runnerNS1.getUsedCommands());
            uniqueCommands.addAll(runnerNS2.getUsedCommands());
            uniqueCommands.addAll(runnerLR1.getUsedCommands());
            uniqueCommands.addAll(runnerLR2.getUsedCommands());
            
            ReduceDSLController.removeUnactivatedParts(script, new ArrayList<>(uniqueCommands));

            return totalScript2;
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
            return -5.0f;
        }
    }

    private DetailedSearchResult callAlgorithm(iDSL script1, iDSL script2, float[] itens) {
        float winner = itens[0];
        DetailedSearchResult results;
        reportRoundRobinEvaluation report = new reportRoundRobinEvaluation();
        if (winner == 1.0f) {
            report.setIA(script2.translate());
            report.start();
            results = this.searchAlgorithm.run(script2, script1, 10, itens[1]);
        } else {
            report.setIA(script1.translate());
            report.start();
            results = this.searchAlgorithm.run(script1, script2, 10, itens[1]);
        }
        try {
            report.join();
            System.out.println("***** Total Victories against WR, NS e LR: " + report.getMatchesResults());
        } catch (InterruptedException ex) {
            Logger.getLogger(DoubleSketchPortfolioSynthesis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

}
