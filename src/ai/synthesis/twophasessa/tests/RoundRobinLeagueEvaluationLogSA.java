/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.twophasessa.tests;

import ai.competition.newBotsEval.botEmptyBase;
import ai.core.AI;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.grammar.dslTree.BooleanDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.CommandDSL;
import ai.synthesis.grammar.dslTree.EmptyDSL;
import ai.synthesis.grammar.dslTree.S1DSL;
import ai.synthesis.grammar.dslTree.S2DSL;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.ReduceDSLController;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.runners.roundRobinLocal.SmartRRGxGRunnable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class RoundRobinLeagueEvaluationLogSA {

    private static final int QTD_ROUND_ROBIN = 5;
    private static final String uniqueID = UUID.randomUUID().toString();

    public static void main(String[] args) throws Exception {
        
        BuilderDSLTreeSingleton builder = BuilderDSLTreeSingleton.getInstance();
        // Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        iDSL rec = null;
        int result = fc.showOpenDialog(new JDialog());
        long start = System.nanoTime();
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            rec = SerializableController.recoverySerializable(selectedFile.getName(),
                    selectedFile.getAbsolutePath().replace(selectedFile.getName(),
                            ""));
            System.out.println("Selected AST:" + rec.translate());
        }
        
        EmptyDSL empty = new EmptyDSL();
// comandos
        CommandDSL c1 = new CommandDSL("train(Worker,50,EnemyDir)");
        CommandDSL c2 = new CommandDSL("harvest(2)");
        CommandDSL c3 = new CommandDSL("attack(Worker,closest)");
        CommandDSL c4 = new CommandDSL("attack(Worker,mostHealthy)");
        CommandDSL c5 = new CommandDSL("moveToCoord(Worker,5,6)");
        CommandDSL c6 = new CommandDSL("moveToCoord(Worker,18,6)");
        CommandDSL c7 = new CommandDSL("moveToCoord(Worker,18,15)");
        CommandDSL c8 = new CommandDSL("moveToCoord(Worker,5,15)");
        CommandDSL c9 = new CommandDSL("harvest(10)");
//Cs
        CDSL C2 = new CDSL(c2);
        CDSL C1 = new CDSL(c1);
        CDSL C4 = new CDSL(c4);
        CDSL C3 = new CDSL(c3);
        CDSL C6 = new CDSL(c6);
        CDSL C5 = new CDSL(c5, C6);
        CDSL C8 = new CDSL(c8);
        CDSL C7 = new CDSL(c7, C8);
        CDSL C9 = new CDSL(c9);
// booleanos
        BooleanDSL b1 = new BooleanDSL("HaveUnitsinEnemyRange(Worker)");
        BooleanDSL b2 = new BooleanDSL("HaveQtdUnitsbyType(Worker,15)");
        BooleanDSL b3 = new BooleanDSL("IsPlayerInPosition(Up)");
        BooleanDSL b4 = new BooleanDSL("IsPlayerInPosition(Down)");
        BooleanDSL b5 = new BooleanDSL("HaveQtdUnitsbyType(Worker,10)");
//S2
        S2DSL S21 = new S2DSL(b1, C3);
        S2DSL S22 = new S2DSL(b2, C4);
        S2DSL S23 = new S2DSL(b3, C5);
        S2DSL S24 = new S2DSL(b4, C7);
        S2DSL S25 = new S2DSL(b5, C9);
//S1
        S1DSL S16 = new S1DSL(S25, empty);
        S1DSL S15 = new S1DSL(S24, S16);
        S1DSL S14 = new S1DSL(S23, S15);
        S1DSL S13 = new S1DSL(S22, S14);
        S1DSL S12 = new S1DSL(S21, S13);
        S1DSL S11 = new S1DSL(C2, S12);
// Raiz
        iDSL ScrI = new S1DSL(C1, S11);
        float eval = 0;
        for (int i = 0; i < 5; i++) {
            eval += evaluate_thread_scripts(rec, ScrI);
        }
        System.out.println("Total score rec "+ eval);

    }

    private static float evaluate_thread_scripts(iDSL script1, iDSL script2) {
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

            return totalScript2;
        } catch (Exception e) {
            System.err.println("ai.synthesis.localsearch.DoubleProgramSynthesis.processMatch() " + e.getMessage());
            return -5.0f;
        }
    }

}
