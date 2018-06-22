/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.SOA;

import Standard.StrategyTactics;
import ai.CMAB.CMABBuilder;
import ai.RandomBiasedAI;
import ai.abstraction.combat.KitterDPS;
import ai.abstraction.combat.NOKDPS;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.ahtn.AHTNAI;
import ai.aiSelection.AlphaBetaSearch.AlphaBetaSearch;
import ai.core.AI;
import ai.asymmetric.PGS.PGSSCriptChoice;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SSS.SSSmRTS;
import ai.asymmetric.SSS.SSSmRTSScriptChoice;
import ai.cluster.CABA;
import ai.cluster.CABA_Enemy;
import ai.cluster.CABA_TDLearning;
import ai.cluster.CIA_Enemy;
import ai.cluster.CIA_PlayoutTemporal;
import ai.cluster.CIA_TDLearning;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.evaluation.LTD2;
import ai.evaluation.PlayoutFunction;
import ai.evaluation.SimpleEvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.believestatemcts.BS3_NaiveMCTS;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.mcts.naivemcts.NaiveMCTSNoGamma;
import ai.mcts.uct.UCT;
import ai.minimax.ABCD.ABCD;
import ai.montecarlo.MonteCarlo;
import ai.montecarlo.lsi.LSI;
import ai.puppet.PuppetSearchMCTS;
import gui.PhysicalGameStatePanel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTYpeTableBattle;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens Classe responsável por rodar os confrontos entre duas IA's.
 * Ambiente totalmente observável.
 */
public class RoundRobinClusterLeve_Cluster {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;

    public boolean run(String sIA1, String sIA2, String sMap, String sIte, String pathLog) throws Exception {
        int iAi1 = Integer.parseInt(sIA1);
        int iAi2 = Integer.parseInt(sIA2);
        int map = Integer.parseInt(sMap);

        ArrayList<String> log = new ArrayList<>();
        //controle de tempo
        Instant timeInicial = Instant.now();
        Duration duracao;

        List<String> maps = new ArrayList<>(Arrays.asList(
                "maps/8x8/basesWorkers8x8A.xml",
                "maps/NoWhereToRun9x8.xml",
                "maps/16x16/basesWorkers16x16A.xml",
                "maps/16x16/TwoBasesBarracks16x16.xml",
                "maps/24x24/basesWorkers24x24A.xml",
                "maps/DoubleGame24x24.xml",
                "maps/32x32/basesWorkers32x32A.xml",
                "maps/BWDistantResources32x32.xml",  //8 maps
                //"maps/BroodWar/(4)BloodBath.scmB.xml" //9 maps
                //3 16
                //"maps/16x16/BasesWithWalls16x16.xml",
                //"maps/16x16/BasesTwoBarracksWithWalls16x16.xml",
                //"maps/16x16/NoWhereWithBlocks16x16.xml",
                // 3 24
                //"maps/24x24/DoubleMapaWithBlockTwoBarracks24x24.xml",
                //"maps/24x24/DoubleMapaWithBlock24x24.xml",
                //"maps/24x24/DoubleMapaWithBlockTwoBases24x24.xml",
                //3 32 
                //"maps/32x32/centerResources32x32.xml",
                //"maps/32x32/ComplexPathToFight32x32.xml",
                //"maps/32x32/RuntoGoldWithBlocksBarracks32x32.xml",
                //3 64
                //"maps/BroodWar/(4)BloodBath.scmB.xml",
                //"maps/64x64/SimplePathToFight64x64.xml",
                //"maps/64x64/ComplexPathToFight64x64.xml"
                //new maps Starcraft
                "maps/BroodWar/(4)BloodBath.scmA.xml",
                "maps/BroodWar/(4)BloodBath.scmB.xml",
                "maps/BroodWar/(4)BloodBath.scmC.xml",
                "maps/BroodWar/(4)BloodBath.scmD.xml",
                "maps/BroodWar/(2)Destination.scxA.xml",
                "maps/BroodWar/(4)Andromeda.scxE.xml",
                "maps/BroodWar/(4)CircuitBreaker.scxF.xml",
                "maps/BroodWar/(4)Fortress.scxA.xml",
                "maps/BroodWar/(4)Python.scxB.xml"
                
        ));

        //UnitTypeTable utt = new UnitTYpeTableBattle();
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(maps.get(map), utt);

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 6000;
        }
        if (pgs.getHeight() == 16) {
            MAXCYCLES = 9000;
        }
        if (pgs.getHeight() == 24) {
            MAXCYCLES = 10000;
        }
        if (pgs.getHeight() == 32) {
            MAXCYCLES = 11000;
        }
        if (pgs.getHeight() == 64) {
            MAXCYCLES = 15000;
        }

        List<AI> ais = new ArrayList<>(Arrays.asList(
                new POLightRush(utt),
                new POWorkerRush(utt),
                new PORangedRush(utt),
                new POHeavyRush(utt),
                new AHTNAI(utt),
                new NaiveMCTS(utt),
                new BS3_NaiveMCTS(utt),
                new PuppetSearchMCTS(utt),
                new StrategyTactics(utt), //9
                //NSS
                new CMABBuilder(100, -1, 100, 1, 0, new RandomBiasedAI(utt), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabCombinatorialGenerator"),
                //behavior
                new CMABBuilder(100, -1, 100, 1, 0, new RandomBiasedAI(utt), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabCombinatorialGenerator", "ManagerClosestEnemy", 2),
                //asymmetric cluster
                //new CMABBuilder(100, -1, 100, 2, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterEuDistGenerator", 2, 2),
                //new CMABBuilder(100, -1, 200, 10, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterEuDistGenerator", 4, 2),
                //new CMABBuilder(100, -1, 200, 10, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterEuDistGenerator", 6, 2),
                //new CMABBuilder(100, -1, 100, 2, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterPlayoutGenerator", 2, 2),
                //new CMABBuilder(100, -1, 200, 10, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterPlayoutGenerator", 4, 2),
                //new CMABBuilder(100, -1, 200, 10, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterPlayoutGenerator", 6, 2),
                //new CMABBuilder(100, -1, 100, 2, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterGammaGenerator", 2, 2),
                //new CMABBuilder(100, -1, 200, 10, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterGammaGenerator", 4, 2),
                //new CMABBuilder(100, -1, 200, 10, 0, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabClusterGammaGenerator", 6, 2)
                //NSAA
                new CMABBuilder(100, -1, 100, 1, 0, new RandomBiasedAI(utt), new SimpleSqrtEvaluationFunction3(), 0, utt, new ArrayList<>(), "CmabAsyReduzedGenerator")
        ));

        AI ai1 = ais.get(iAi1);
        AI ai2 = ais.get(iAi2);

        /*
            Variáveis para coleta de tempo
         */
        double ai1TempoMin = 9999, ai1TempoMax = -9999;
        double ai2TempoMin = 9999, ai2TempoMax = -9999;
        double sumAi1 = 0, sumAi2 = 0;
        int totalAction = 0;

        log.add("---------AIs---------");
        log.add("AI 1 = " + ai1.toString());
        log.add("AI 2 = " + ai2.toString() + "\n");

        log.add("---------Mapa---------");
        log.add("Mapa= " + maps.get(map) + "\n");

        //método para fazer a troca dos players
        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 840, 840, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);
        long startTime;
        long timeTemp;
        //System.out.println("Tempo de execução P2="+(startTime = System.currentTimeMillis() - startTime));
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                totalAction++;
                startTime = System.currentTimeMillis();

                PlayerAction pa1 = ai1.getAction(0, gs);
                //dados de tempo ai1
                timeTemp = (System.currentTimeMillis() - startTime);
                sumAi1 += timeTemp;
                //coleto tempo mínimo
                if (ai1TempoMin > timeTemp) {
                    ai1TempoMin = timeTemp;
                }
                //coleto tempo maximo
                if (ai1TempoMax < timeTemp) {
                    ai1TempoMax = timeTemp;
                }

                startTime = System.currentTimeMillis();
                PlayerAction pa2 = ai2.getAction(1, gs);
                //dados de tempo ai2
                timeTemp = (System.currentTimeMillis() - startTime);
                sumAi2 += timeTemp;
                //coleto tempo mínimo
                if (ai2TempoMin > timeTemp) {
                    ai2TempoMin = timeTemp;
                }
                //coleto tempo maximo
                if (ai2TempoMax < timeTemp) {
                    ai2TempoMax = timeTemp;
                }

                gs.issueSafe(pa1);
                gs.issueSafe(pa2);

                // simulate:
                gameover = gs.cycle();
                //w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //avaliacao de tempo
            duracao = Duration.between(timeInicial, Instant.now());

        } while (!gameover && (gs.getTime() < MAXCYCLES) && (duracao.toMinutes() < 40));

        log.add("Total de actions= " + totalAction + " sumAi1= " + sumAi1 + " sumAi2= " + sumAi2 + "\n");

        log.add("Tempos de AI 1 = " + ai1.toString());
        log.add("Tempo minimo= " + ai1TempoMin + " Tempo maximo= " + ai1TempoMax + " Tempo medio= " + (sumAi1 / (long) totalAction));

        log.add("Tempos de AI 2 = " + ai2.toString());
        log.add("Tempo minimo= " + ai2TempoMin + " Tempo maximo= " + ai2TempoMax + " Tempo medio= " + (sumAi2 / (long) totalAction) + "\n");

        log.add("Winner " + Integer.toString(gs.winner()));
        log.add("Game Over");

        if (gs.winner() == -1) {
            System.out.println("Empate!" + ai1.toString() + " vs " + ai2.toString() + " Max Cycles =" + MAXCYCLES + " Time:" + duracao.toMinutes());
        }

        gravarLog(log, sIA1, sIA2, sMap, sIte, pathLog);
        //System.exit(0);
        return true;
    }

    private void gravarLog(ArrayList<String> log, String sIA1, String sIA2, String sMap, String sIte, String pathLog) throws IOException {
        if (!pathLog.endsWith("/")) {
            pathLog += "/";
        }
        String nameArquivo = pathLog + "match_" + sIA1 + "_" + sIA2 + "_" + sMap + "_" + sIte + ".scv";
        File arqLog = new File(nameArquivo);
        if (!arqLog.exists()) {
            arqLog.createNewFile();
        }
        //abre o arquivo e grava o log
        try {
            FileWriter arq = new FileWriter(arqLog, false);
            PrintWriter gravarArq = new PrintWriter(arq);
            for (String l : log) {
                gravarArq.println(l);
            }

            gravarArq.flush();
            gravarArq.close();
            arq.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
