/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import PVAI.EconomyRush;
import Standard.StrategyTactics;
import ai.RandomBiasedAI;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.ahtn.AHTNAI;
import ai.asymmetric.GAB.GAB_oldVersion;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SSS.SSSmRTS;
import ai.core.AI;
import ai.mcts.believestatemcts.BS3_NaiveMCTS;
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
public class Torneio {
    public static void main(String args[]) throws Exception {
        int ai1 = Integer.parseInt(args[0]);
        int ai2 = Integer.parseInt(args[1]);
        int map = Integer.parseInt(args[2]);
        List<String> maps = new ArrayList<>(Arrays.asList(
                "maps/BroodWar/(3)TauCross.scxA.xml",
                "maps/BroodWar/(3)TauCross.scxB.xml",
                "maps/BroodWar/(3)TauCross.scxC.xml",
                "maps/BroodWar/(4)CircuitBreaker.scxB.xml",
                "maps/BroodWar/(4)CircuitBreaker.scxD.xml",
                "maps/BroodWar/(4)CircuitBreaker.scxF.xml",
                "maps/BroodWar/(4)Python.scxA.xml",
                "maps/BroodWar/(4)Python.scxC.xml",
                "maps/BroodWar/(4)Python.scxF.xml"
        ));
        List<Integer> lCycles = new ArrayList<Integer>();
        
        int[] cycles = {12000, 12000, 12000, 12000, 12000 , 12000, 12000, 12000, 12000};
        
        for (int cycle: cycles) {
            lCycles.add(cycle);
        }
        
        
        UnitTypeTable utt = new UnitTypeTable();

        /*
                new NaiveMCTS(utt),
        
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 10),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 20),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 50),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 100),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 200),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 300),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 400),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 500),
                new SCVTime(new AI[]{new EconomyRush(utt)}, 100, -1, utt, 1000)
        
                new PuppetSearchMCTS(utt),
                new StrategyTactics(utt),
                new BS3_NaiveMCTS(utt),
                new AHTNAI(utt),
        */
        
        
        List<AI> ais = new ArrayList<>(Arrays.asList(
                new LightRush(utt),
                new WorkerRush(utt),
                new HeavyRush(utt),
                new RangedRush(utt),
                new EconomyRush(utt)
                
        ));
        String prefix = "tournament_" + Integer.toString(ai1) + "_" + Integer.toString(ai2) + "_" + Integer.toString(map);
        //int idx = 0;
        String sufix = ".tsv";
        File file = new File(prefix + sufix);
        /*do {
            idx++;
            file = new File(prefix + idx + sufix);
        } while (file.exists());*/
        final File fileToUse = file;
        final String tracesFolder = (prefix);
        Writer writer = new FileWriter(fileToUse);
        JTextArea tournamentProgressTextArea = new JTextArea();
        //tournamentProgressTextArea.append("Mensagem");
        Writer writerProgress = new JTextAreaWriter(tournamentProgressTextArea);
        boolean timeoutCheck = false;
        //Experimenter.runExperiments(bots, maps, utt, 10, 8000, 500, true);
        //System.out.println("Mapa 8 x 8 - FULL");
        List<AI> aisSelected = new ArrayList<>(Arrays.asList(ais.get(ai1), ais.get(ai2)));
        List<String> mapsSelected = new ArrayList<>(Arrays.asList(maps.get(map)));
        RoundRobinTournament.runTournament(aisSelected,-1, mapsSelected, 3, lCycles.get(map), 100, -1, -1, true, false, timeoutCheck, true, false, utt, tracesFolder, writer, writerProgress);
        
        writer.close();
    }
}
