/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.grammar.dslTree.builderDSLTree;

import ai.synthesis.grammar.dslTree.BooleanDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.CommandDSL;
import ai.synthesis.grammar.dslTree.S1DSL;
import ai.synthesis.grammar.dslTree.S2DSL;
import ai.synthesis.grammar.dslTree.S3DSL;
import ai.synthesis.grammar.dslTree.S4DSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iNodeDSLTree;
import ai.synthesis.localsearch.DoubleProgramSynthesis;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.runners.reportEvaluation.reportRoundRobinEvaluation;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.units.Unit;

/**
 *
 * @author rubens
 */
public class localTestsValidation {

    public static void main(String[] args) {
        BuilderSketchDSLSingleton sketch = BuilderSketchDSLSingleton.getInstance();
        BuilderDSLTreeSingleton builder = BuilderDSLTreeSingleton.getInstance();
        iDSL t = builder.buildS1Grammar();
        System.out.println(t.translate());
        //builder.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) t);
        //builder.composeNeighbourPassively(t);
        //System.out.println(t.translate());
        //builder.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) t);

        for (int i = 0; i < 100000000; i++) {
            builder.changeNeighbourPassively(t);
            HashSet<iNodeDSLTree> nodes = builder.getNodesWithoutDuplicity(t);
            if(count_by_commands(nodes) > 12){
                System.out.println("Size= " + count_by_commands(nodes) + " " + t.translate());
            }
            
        }

        /* save configuration
        saveSerial(t);
        iDSL tDes = recovery();
        System.out.println("Original    ="+ t.translate());
        builder.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) t);
        System.out.println("Serializabe ="+tDes.translate());
        builder.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) tDes);
         */
 /*
        Random rand = new Random();
        BuilderSketchDSLSingleton sketch = BuilderSketchDSLSingleton.getInstance();
        for (int i = 0; i < 100; i++) {
            iDSL t1 = sketch.getSketchTypeTwo();
            System.out.println(t1.translate());
            for (int j = 0; j < 10; j++) {
                sketch.modifyTerminalParameters(t1);
                System.out.println("     "+t1.translate());
            }
        }
         */
    }

    private static void saveSerial(iDSL t) {
        try {
            FileOutputStream fout = new FileOutputStream("dsl1.ser");
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(t);
            out.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static iDSL recovery() {
        iDSL dsl = null;
        try {
            FileInputStream fIn = new FileInputStream("dsl1.ser");
            ObjectInputStream in = new ObjectInputStream(fIn);
            dsl = (iDSL) in.readObject();
            in.close();
            fIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsl;
    }

    private static int count_by_commands(HashSet<iNodeDSLTree> nodes) {
        int count = 0;
        for (iNodeDSLTree node : nodes) {
            if (node instanceof CommandDSL) {
                count++;
            } else if (node instanceof S2DSL) {
                count++;
            } else if (node instanceof S3DSL) {
                count++;
            }

        }

        return count;
    }

    public void oldteste() {
        BuilderDSLTreeSingleton builder = BuilderDSLTreeSingleton.getInstance();
        S1DSL c;
        c = builder.buildS1Grammar();
        System.out.println(c.translate());
        /*
        System.out.println(c.translate());
        int totalNodes = BuilderDSLTreeSingleton.getNumberofNodes(c);
        System.out.println("Qtd Nodes " + totalNodes);
        builder.formatedStructuredDSLTreePreOrderPrint(c);
        
        int nNode = rand.nextInt(totalNodes)+1;
        System.out.println("Looking for node "+nNode+".....");
        iNodeDSLTree targetNode = BuilderDSLTreeSingleton.getNodeByNumber(nNode, c);
        System.out.println("Fantasy name = "+targetNode.getFantasyName()+" "+ targetNode);
        
        iDSL castNode = (iDSL) targetNode;
        System.out.println(castNode+" "+castNode.translate());
        BuilderDSLTreeSingleton.formatedStructuredDSLTreePreOrderPrint(targetNode);
        System.out.println("Has S3 as father? "+builder.hasS3asFather(targetNode));
        
        iDSL n = builder.generateNewCommand(castNode, builder.hasS3asFather(targetNode));
        castNode = n;
        System.out.println("\nNew tree");
        System.out.println("New comamand"+castNode+" "+castNode.translate());
        BuilderDSLTreeSingleton.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) castNode);
        //c = (c.getClass().cast(builder.replaceDSLByNeighbour(c, castNode,builder.hasS3asFather(targetNode))));        
        System.out.println(c.translate());
        builder.formatedStructuredDSLTreePreOrderPrint(c);
         */

        for (int i = 0; i < 500; i++) {
            c = builder.buildS1Grammar();
            System.out.println(i + " " + c.translate());
            //BuilderDSLTreeSingleton.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) c);
            //callAlgorithm(c.translate());
            //changing
            /*
            int nNode = rand.nextInt(BuilderDSLTreeSingleton.getNumberofNodes(c)) + 1;
            System.out.println("Node selected " + nNode);
            iNodeDSLTree targetNode = BuilderDSLTreeSingleton.getNodeByNumber(nNode, c);
            builder.generateNewCommand((iDSL) targetNode, builder.hasS3asFather(targetNode));
            System.out.println(i + " " + c.translate());
            nNode = rand.nextInt(BuilderDSLTreeSingleton.getNumberofNodes(c)) + 1;
             */
            for (int j = 0; j < 10; j++) {
                builder.composeNeighbourPassively(c);
                System.out.println(i + " " + c.translate());
            }

            /*
            builder.composeNeighbour(c);
            System.out.println(i + " " + c.translate());
            for (int j = 0; j < 10; j++) {
                builder.composeNeighbour(c);
                System.out.println(i + " " + c.translate());
            }
             */
            //BuilderDSLTreeSingleton.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) c);
            //callAlgorithm(c.translate());
            System.out.println("_____________________________________________");
        }
    }

    private static void callAlgorithm(String script1) {
        reportRoundRobinEvaluation report = new reportRoundRobinEvaluation();
        report.setIA(script1);
        report.start();
        try {
            report.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(localTestsValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("***** Total Victories against WR,NS and LR: " + report.getMatchesResults());
    }

}
