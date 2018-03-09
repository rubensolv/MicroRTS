/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import PVAI.runTournament;
import ai.RandomBiasedAI;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.core.AI;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.mcts.uct.UCT;
import ai.portfolio.PortfolioAI;
import ai.puppet.PuppetSearchMCTS;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import gui.JTextAreaWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import rts.units.UnitTypeTable;
import tournaments.RoundRobinTournament;

/**
 *
 * @author Cleyton Silva
 */
public class TorneioThread {
    

    
    public static void main(String args[]) throws Exception {
        int ai1 = Integer.parseInt(args[0]);
        int ai2 = Integer.parseInt(args[1]);
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

        List<AI> ais = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt),
                new NaiveMCTS(utt),
                new PuppetSearchMCTS(utt),
                new RandomBiasedAI(),
                new POWorkerRush(utt),
                new POLightRush(utt)
                
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
        
        runTournament torneio;
        torneio = new runTournament(aisSelected, mapsSelected, lCycles.get(map), timeoutCheck, utt, tracesFolder, writer, writerProgress);
        
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(torneio);
        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        
        
        //Thread t1 = new Thread(torneio);
        //t1.start();
        
        
        writer.close();
    }
}
