/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import PVAI.EconomyRush;
import Standard.StrategyTactics;
import ai.RandomBiasedAI;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.ahtn.AHTNAI;
import ai.asymmetric.GAB.GAB;
import ai.asymmetric.GAB.GAB_ABActionGeneration;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SSS.SSSmRTS;
import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.mcts.uct.UCT;
import ai.portfolio.PortfolioAI;
import ai.puppet.PuppetSearchMCTS;
import ai.stochastic.UnitActionProbabilityDistributionAI;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import gui.JTextAreaWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTextArea;
import rts.units.UnitTypeTable;
import tournaments.RoundRobinTournament;

/**
 *
 * @author Cleyton Silva
 */
public class TorneioGAB {
    public static void main(String args[]) throws Exception {
        int numUnits = Integer.parseInt(args[0]);
        int managerChoice = Integer.parseInt(args[1]);
        int map = Integer.parseInt(args[2]);
        
        
        List<String> maps = new ArrayList<>(Arrays.asList(
                "maps/8x8/basesWorkers8x8A.xml",
                "maps/16x16/basesWorkers16x16A.xml",
                "maps/24x24/basesWorkers24x24A.xml",
                "maps/BWDistantResources32x32.xml",
                "maps/BroodWar/(4)BloodBath.scmB.xml"
        ));
        List<Integer> lCycles = new ArrayList<Integer>();
        
        int[] cycles = {3000, 4000, 5000, 6000, 8000};
        
        for (int cycle: cycles) {
            lCycles.add(cycle);
        }
        
        
        UnitTypeTable utt = new UnitTypeTable();

        //bots
        AI ai1 = new PGSmRTS(utt);
        AI ai2 = new GAB_ABActionGeneration(100, 200,new SimpleSqrtEvaluationFunction3(),
             utt,
             new AStarPathFinding(),
             numUnits,managerChoice);
        
        
        String prefix = "tournament_" + ai1 + "_" + ai2 + "_" + Integer.toString(map);
        //int idx = 0;
        String sufix = ".tsv";
        File file = new File(prefix + sufix);
        
        final File fileToUse = file;
        final String tracesFolder = (prefix);
        Writer writer = new FileWriter(fileToUse);
        JTextArea tournamentProgressTextArea = new JTextArea();
        
        Writer writerProgress = new JTextAreaWriter(tournamentProgressTextArea);
        boolean timeoutCheck = false;
        
        List<AI> aisSelected = new ArrayList<>(Arrays.asList(ai1, ai2));
        List<String> mapsSelected = new ArrayList<>(Arrays.asList(maps.get(map)));
        RoundRobinTournament.runTournament(aisSelected,-1, mapsSelected, 5, lCycles.get(map), 100, -1, -1, true, false, timeoutCheck, true, false, utt, tracesFolder, writer, writerProgress);
        
        writer.close();
    }
}
