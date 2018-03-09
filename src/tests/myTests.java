/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ai.RandomBiasedAI;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.mcts.uct.UCT;
import ai.minimax.ABCD.ABCD;
import ai.montecarlo.lsi.LSI;
import ai.portfolio.PortfolioAI;
import ai.puppet.PuppetSearchMCTS;
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
public class myTests {

    public static void main(String args[]) throws Exception {
        int t = Integer.parseInt(args[0]);
        int map = Integer.parseInt(args[1]);
        //System.out.println("Main do meu arquivo" + t + " " + map);
        UnitTypeTable utt = new UnitTypeTable();

        List<AI> bots1 = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt),
                new NaiveMCTS(utt)
        ));
        List<AI> bots2 = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt),
                new PuppetSearchMCTS(utt)
        ));
        List<AI> bots3 = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt),
                new RandomBiasedAI()
        ));
        List<AI> bots4 = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt),
                new POWorkerRush(utt)
        ));
        List<AI> bots5 = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt),
                new POLightRush(utt)
        ));
        List<AI> bots6 = new ArrayList<>(Arrays.asList(
                new PortfolioAI(utt)                
        ));

        /*List<PhysicalGameState> maps = new ArrayList<>(Arrays.asList(
                PhysicalGameState.load("maps/8x8/basesWorkers8x8A.xml", utt),
                PhysicalGameState.load("maps/16x16/basesWorkers16x16A.xml", utt),
                PhysicalGameState.load("maps/BWDistantResources32x32.xml", utt),
                PhysicalGameState.load("maps/BroodWar/(4)BloodBath.scmB.xml", utt)
        ));*/
        List<String> maps1 = new ArrayList<>(Arrays.asList(
                "maps/8x8/basesWorkers8x8A.xml"
        ));
        List<String> maps2 = new ArrayList<>(Arrays.asList(
                "maps/16x16/basesWorkers16x16A.xml"
        ));
         List<String> maps3 = new ArrayList<>(Arrays.asList(
                "maps/24x24/basesWorkers24x24A.xml"
        ));
        List<String> maps4 = new ArrayList<>(Arrays.asList(
                "maps/BWDistantResources32x32.xml"
        ));
        List<String> maps5 = new ArrayList<>(Arrays.asList(
                "maps/BroodWar/(4)BloodBath.scmB.xml"
        ));
       

        String prefix = "tournament_";
        int idx = 0;
        String sufix = ".tsv";
        File file;
        do {
            idx++;
            file = new File(prefix + idx + sufix);
        } while (file.exists());
        final File fileToUse = file;
        final String tracesFolder = (prefix + idx);
        Writer writer = new FileWriter(fileToUse);
        JTextArea tournamentProgressTextArea = new JTextArea();
        //tournamentProgressTextArea.append("Mensagem");
        Writer writerProgress = new JTextAreaWriter(tournamentProgressTextArea);
        boolean timeoutCheck = false;
        //Experimenter.runExperiments(bots, maps, utt, 10, 8000, 500, true);
        //System.out.println("Mapa 8 x 8 - FULL");
        switch (t) {
            case 1:
                switch (map) { 
                    case 1:
                        RoundRobinTournament.runTournament(bots1, -1, maps1, 3, 3000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 2:
                        RoundRobinTournament.runTournament(bots1,-1, maps2, 3, 4000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 3:
                        RoundRobinTournament.runTournament(bots1,-1, maps3, 3, 5000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 4:
                        RoundRobinTournament.runTournament(bots1,-1, maps4, 3, 6000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    default:
                        RoundRobinTournament.runTournament(bots1,-1, maps5, 3, 8000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                }
                break;
            case 2:
                switch (map) {
                    case 1:
                        RoundRobinTournament.runTournament(bots2,-1, maps1, 3, 3000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 2:
                        RoundRobinTournament.runTournament(bots2,-1, maps2, 3, 4000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 3:
                        RoundRobinTournament.runTournament(bots2,-1, maps3, 3, 5000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 4:
                        RoundRobinTournament.runTournament(bots2,-1, maps4, 3, 6000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    default:
                        RoundRobinTournament.runTournament(bots2,-1, maps5, 3, 8000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                }
                break;
            case 3:
                switch (map) {
                    case 1:
                        RoundRobinTournament.runTournament(bots3,-1, maps1, 3, 3000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 2:
                        RoundRobinTournament.runTournament(bots3,-1, maps2, 3, 4000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 3:
                        RoundRobinTournament.runTournament(bots3,-1, maps3, 3, 5000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 4:
                        RoundRobinTournament.runTournament(bots3,-1, maps4, 3, 6000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    default:
                        RoundRobinTournament.runTournament(bots3,-1, maps5, 3, 8000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                }
                break;
            case 4:
                switch (map) {
                    case 1:
                        RoundRobinTournament.runTournament(bots4,-1, maps1, 3, 3000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 2:
                        RoundRobinTournament.runTournament(bots4,-1, maps2, 3, 4000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 3:
                        RoundRobinTournament.runTournament(bots4,-1, maps3, 3, 5000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 4:
                        RoundRobinTournament.runTournament(bots4,-1, maps4, 3, 6000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    default:
                        RoundRobinTournament.runTournament(bots4,-1, maps5, 3, 8000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                }
                break;
            case 5:
                switch (map) {
                    case 1:
                        RoundRobinTournament.runTournament(bots5,-1, maps1, 3, 3000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 2:
                        RoundRobinTournament.runTournament(bots5,-1, maps2, 3, 4000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 3:
                        RoundRobinTournament.runTournament(bots5,-1, maps3, 3, 5000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 4:
                        RoundRobinTournament.runTournament(bots5,-1, maps4, 3, 6000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    default:
                        RoundRobinTournament.runTournament(bots5,-1, maps5, 3, 8000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                }
                break;
            default:
                switch (map) {
                    case 1:
                        RoundRobinTournament.runTournament(bots6,-1, maps1, 3, 3000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 2:
                        RoundRobinTournament.runTournament(bots6,-1, maps2, 3, 4000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 3:
                        RoundRobinTournament.runTournament(bots6,-1, maps3, 3, 5000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    case 4:
                        RoundRobinTournament.runTournament(bots6,-1, maps4, 3, 6000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                    default:
                        RoundRobinTournament.runTournament(bots6,-1, maps5, 3, 8000, 100, -1, 100, true, false, timeoutCheck, true, true, utt, tracesFolder, writer, writerProgress);
                        break;
                }
                break;
        }
        //RoundRobinTournament.runTournament(bots, maps8x8, 3, 3000, 100, -1, true, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 16 x 16 - FULL");
        //RoundRobinTournament.runTournament(bots, maps16x16, 3, 4000, 100, -1, true, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 32 x 32 - FULL");
        //RoundRobinTournament.runTournament(bots, maps32x32, 3, 6000, 100, -1, true, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 64 x 64 - FULL");
        //RoundRobinTournament.runTournament(bots, maps64x64, 3, 8000, 100, -1, true, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 16 x 16 - Melle FULL");
        //RoundRobinTournament.runTournament(bots, mapsMelee, 3, 8000, 100, -1, true, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 8 x 8 - PARTIAL");
        //RoundRobinTournament.runTournament(bots, maps8x8, 3, 3000, 100, -1, false, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 16 x 16 - PARTIAL");
        //RoundRobinTournament.runTournament(bots, maps16x16, 3, 4000, 100, -1, false, false, true, true, utt, tracesFolder, writer, writerProgress);
        //System.out.println("Mapa 32 x 32 - PARTIAL");
        //RoundRobinTournament.runTournament(bots, maps32x32, 3, 6000, 100, -1, false, false, true, true, utt, tracesFolder, writer, writerProgress);
        // System.out.println("Mapa 64 x 64 - PARTIAL");
        //RoundRobinTournament.runTournament(bots, maps64x64, 3, 8000, 100, -1, false, false, true, true, utt, tracesFolder, writer, writerProgress);
        writer.close();
    }
}
