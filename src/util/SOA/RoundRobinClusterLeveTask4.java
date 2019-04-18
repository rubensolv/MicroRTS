/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.SOA;

import Standard.StrategyTactics;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.CMAB.CmabNaiveMCTS;
import ai.RandomBiasedAI;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.ahtn.AHTNAI;
import ai.aiSelection.AlphaBetaSearch.AlphaBetaSearch;
import ai.asymmetric.GAB.SandBox.GAB;
import ai.asymmetric.GAB.SandBox.GABScriptChoose;
import ai.asymmetric.PGS.NGS;
import ai.asymmetric.PGS.NGSLimit;
import ai.asymmetric.PGS.NGSLimitRandom;
import ai.asymmetric.PGS.NGSRandom;
import ai.asymmetric.PGS.PGSIteration;
import ai.asymmetric.PGS.PGSIterationRandom;
import ai.asymmetric.PGS.PGSResponseMRTS;
import ai.asymmetric.PGS.PGSResponseMRTSRandom;
import ai.core.AI;
import ai.asymmetric.PGS.PGSSCriptChoice;
import ai.asymmetric.PGS.PGSSCriptChoiceRandom;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SAB.SAB;
import ai.asymmetric.SAB.SABScriptChoose;
import ai.asymmetric.SSS.NSSS;
import ai.asymmetric.SSS.NSSSLimit;
import ai.asymmetric.SSS.NSSSLimitRandom;
import ai.asymmetric.SSS.NSSSRandom;
import ai.asymmetric.SSS.SSSIteration;
import ai.asymmetric.SSS.SSSIterationRandom;
import ai.asymmetric.SSS.SSSResponseMRTS;
import ai.asymmetric.SSS.SSSResponseMRTSRandom;
import ai.asymmetric.SSS.SSSmRTS;
import ai.asymmetric.SSS.SSSmRTSScriptChoice;
import ai.asymmetric.SSS.SSSmRTSScriptChoiceRandom;
import ai.cluster.CABA;
import ai.cluster.CABA_Enemy;
import ai.cluster.CABA_TDLearning;
import ai.cluster.CIA_Enemy;
import ai.cluster.CIA_PlayoutTemporal;
import ai.cluster.CIA_TDLearning;
import ai.competition.tiamat.Tiamat;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.believestatemcts.BS3_NaiveMCTS;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.puppet.PuppetSearchMCTS;
import ai.puppet.PuppetSearchMCTSBasicScripts;
import ai.scv.SCV;
import ai.scv.SCVPlus;
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
import static tests.ClusterTesteLeve.decodeScripts;
import static tests.ClusterTesteLeve_Cluster.decodeScripts;
import static tests.ClusterTesteLeve_Combination.decodeScripts;
import static util.SOA.RoundRobinClusterBehaviorTask3.getManager;

/**
 *
 * @author rubens Classe responsável por rodar os confrontos entre duas IA's.
 * Ambiente totalmente observável.
 */
public class RoundRobinClusterLeveTask4 {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;

    public boolean run(String sIA1, String sIA2, String sMap, String sIte, String pathLog) throws Exception {
        int iAi1 = Integer.parseInt(sIA1);
        int iAi2 = Integer.parseInt(sIA2);
        int map = Integer.parseInt(sMap);
        
        if (map > 9) {
            return true;
        }

        ArrayList<String> log = new ArrayList<>();
        //controle de tempo
        Instant timeInicial = Instant.now();
        Duration duracao;

        List<String> maps = new ArrayList<>(Arrays.asList(
                "maps/8x8/basesWorkers8x8A.xml",
                "maps/8x8/FourBasesWorkers8x8.xml",
                "maps/16x16/basesWorkers16x16A.xml",
                "maps/16x16/TwoBasesBarracks16x16.xml",
                "maps/24x24/basesWorkers24x24A.xml",
                "maps/24x24/basesWorkers24x24A_Barrack.xml",
                "maps/32x32/basesWorkers32x32A.xml",
                "maps/32x32/basesWorkersBarracks32x32.xml",
                "maps/BroodWar/(4)BloodBath.scmB.xml",
                "maps/BroodWar/(4)BloodBath.scmD.xml"
        ));

        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(maps.get(map), utt);

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 4000;
        }
        if (pgs.getHeight() == 16) {
            MAXCYCLES = 5000;
        }
        if (pgs.getHeight() == 24) {
            MAXCYCLES = 6000;
        }
        if (pgs.getHeight() == 32) {
            MAXCYCLES = 7000;
        }
        if (pgs.getHeight() == 64) {
            MAXCYCLES = 12000;
        }

        /*List<AI> ais = new ArrayList<>(Arrays.asList(
                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 2,
                        decodeScripts(utt, "1;2;"), "A3N_50"),
                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 2,
                        decodeScripts(utt, "1;2;"), "A3N_100"),
                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 2,
                        decodeScripts(utt, "1;2;"), "A3N_150"),
                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 2,
                        decodeScripts(utt, "1;2;"), "A3N_200") 
        ));*/
        List<AI> ais = new ArrayList<>();

        switch (maps.get(map)) {
            case "maps/8x8/basesWorkers8x8A.xml": //1
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerLessLife", 8,
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/8x8/FourBasesWorkers8x8.xml": //2
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 8, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/16x16/basesWorkers16x16A.xml": //3
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/16x16/TwoBasesBarracks16x16.xml": //4
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosest", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/24x24/basesWorkers24x24A.xml": //5
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/24x24/basesWorkers24x24A_Barrack.xml": //6
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/32x32/basesWorkers32x32A.xml": //7
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt,"ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/32x32/basesWorkersBarracks32x32.xml": //8
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/BroodWar/(4)BloodBath.scmB.xml": //9
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 100, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;
            case "maps/BroodWar/(4)BloodBath.scmD.xml": //10
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N1"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N2"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 3, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N3"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 4, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N4"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 5, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N5"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 6, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N6"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 7, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N7"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 8, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N8"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 9, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N9"));
                ais.add(new CmabAssymetricMCTS(100, -1, 50, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerFather", 1, 
                        decodeScripts(utt, "1;2;3;"), "A3N10"));
                ais.add(new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
                        RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N"));
                break;            
            default: //"maps/BroodWar/(4)EmpireoftheSun.scmC.xml"
                System.out.println("Error!");
                break;
        }

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

        } while (!gameover && (gs.getTime() < MAXCYCLES) && (duracao.toMinutes() < 200));

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

    public static List<AI> decodeScripts(UnitTypeTable utt, String sScripts) {

        //decompõe a tupla
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = sScripts.split(";");

        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }

        List<AI> scriptsAI = new ArrayList<>();

        ScriptsCreator sc = new ScriptsCreator(utt, 300);
        ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet = sc.getScriptsMixReducedSet();

        iScriptsAi1.forEach((idSc) -> {
            scriptsAI.add(scriptsCompleteSet.get(idSc));
        });

        return scriptsAI;
    }

}
