
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.SOA;

import Standard.StrategyTactics;
import ai.CMAB.A3NWithin;
import ai.CMAB.CMABBuilder;
import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;
import ai.RandomBiasedAI;
import ai.abstraction.LightRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.combat.KitterDPS;
import ai.abstraction.combat.NOKDPS;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.ahtn.AHTNAI;
import ai.aiSelection.AlphaBetaSearch.AlphaBetaSearch;
import ai.asymmetric.PGS.LightPGSSCriptChoice;
import ai.asymmetric.PGS.LightPGSSCriptChoiceNoWaits;
import ai.asymmetric.PGS.NGS;
import ai.core.AI;
import ai.asymmetric.PGS.PGSSCriptChoice;
import ai.asymmetric.PGS.PGSSCriptChoiceRandom;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SSS.LightSSSmRTSScriptChoice;
import ai.asymmetric.SSS.SSSmRTS;
import ai.asymmetric.SSS.SSSmRTSScriptChoice;
import ai.asymmetric.SSS.SSSmRTSScriptChoiceRandom;
import ai.cluster.CABA;
import ai.cluster.CABA_Enemy;
import ai.cluster.CABA_TDLearning;
import ai.cluster.CIA_Enemy;
import ai.cluster.CIA_PlayoutTemporal;
import ai.cluster.CIA_TDLearning;
import ai.competition.capivara.Capivara;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.competition.tiamat.Tiamat;
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
import ai.scv.SCV;
import ai.scv.SCVPlus;
import gui.PhysicalGameStatePanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTYpeTableBattle;
import rts.units.UnitTypeTable;
import static tests.ClusterTesteLeve.decodeScripts;

/**
 *
 * @author rubens Classe responsável por rodar os confrontos entre duas IA's.
 * Ambiente totalmente observável.
 */
public class RoundRobinClusterLeve_Cluster_GP {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;
    private HashMap<BigDecimal, String> scriptsTable1;
    private HashMap<BigDecimal, String> scriptsTable2;
    
    String pathTableScripts;
    ICompiler compiler = new MainGPCompiler();
    
    public static void main(String args[]) throws Exception {
    
    }

    public RoundRobinClusterLeve_Cluster_GP(String pathTableScripts) {
        this.pathTableScripts = pathTableScripts;
        scriptsTable1=buildScriptsTable(scriptsTable1, "isolated");
        scriptsTable2=buildScriptsTable(scriptsTable2, "lstm");

    }

    public boolean run(String sIA1, String sIA2, String sMap, String sIte, String pathLog) throws Exception {
        int iAi1 = Integer.parseInt(sIA1);
        int iAi2 = Integer.parseInt(sIA2);
        int map = Integer.parseInt(sMap);

        ArrayList<String> log = new ArrayList<>();
        //controle de tempo
        Instant timeInicial = Instant.now();
        Duration duracao;

        List<String> maps = new ArrayList<>(Arrays.asList(
                //"maps/24x24/basesWorkers24x24A.xml",
                //"maps/DoubleGame24x24.xml",
                //"maps/32x32/basesWorkers32x32A.xml"
                //"maps/BWDistantResources32x32.xml"
                //"maps/BroodWar/(4)BloodBath.scmB.xml"
                //"maps/8x8/basesWorkers8x8A.xml"
                //"maps/16x16/BasesWithWalls16x16.xml"
                //"maps/16x16/basesWorkers16x16A.xml"
                //"maps/NoWhereToRun9x8.xml"
        		"maps/battleMaps/Others/RangedHeavyMixed.xml"
        ));

        //UnitTypeTable utt = new UnitTYpeTableBattle();
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(maps.get(map), utt);

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 9000;
        }
        if (pgs.getHeight() == 9) {
            MAXCYCLES = 9000;
        }
        if (pgs.getHeight() == 16) {
            MAXCYCLES = 10000;
        }
        if (pgs.getHeight() == 24) {
            MAXCYCLES = 11000;
        }
        if (pgs.getHeight() == 32) {
            MAXCYCLES = 12000;
        }
        if (pgs.getHeight() == 64) {
            MAXCYCLES = 17000;
        }
        //BEST AAAI for 8x8
        //String bestGAAAAI = "198;272;100;168;78;86;27;120;279;93;";
        String bestGAAAAI = "225;82;39;276;";
        List<AI> ais;
        ais = new ArrayList<>(Arrays.asList(
                new AHTNAI(utt),
                new NaiveMCTS(utt),
                new PuppetSearchMCTS(utt),
                new StrategyTactics(utt),
                new LightPGSSCriptChoice(utt, decodeScripts(utt, "0;1;2;3;"), 200, "PGSR_LIGHT"),
                new LightSSSmRTSScriptChoice(utt, decodeScripts(utt, "0;1;2;3;"), 200, "SSSR_LIGHT"),
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 4), //lr
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 5), //HR
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 6), //RR
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 3), //WR
//                new LightPGSSCriptChoiceNoWaits(utt, decodeScripts(utt, bestGAAAAI), 200, "GAAAAI_PGS"),
//                new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1014;1020;1021;1022;1023;1024;1008;")),scriptsTable1), 200, "GPP_PGS_ISO_0"),
                new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
                        decodeScripts(utt, "0;"), "A3N"),
                
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("0;")),scriptsTable1), 200, "GPP_SC_R4_it1_0"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1), 200, "GPP_SC_R4_it1_5"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1), 200, "GPP_SC_R4_it1_10"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1), 200, "GPP_SC_R4_it1_15"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("4;")),scriptsTable1), 200, "GPP_SC_R4_it1_20"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1), 200, "GPP_PURE_R4_it1_0"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("6;")),scriptsTable1), 200, "GPP_PURE_R4_it1_5"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("7;")),scriptsTable1), 200, "GPP_PURE_R4_it1_10"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("8;")),scriptsTable1), 200, "GPP_PURE_R4_it1_15"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("9;")),scriptsTable1), 200, "GPP_PURE_R4_it1_20")
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("10;")),scriptsTable1), 200, "GPP_SC_R5_it1_0"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("11;")),scriptsTable1), 200, "GPP_SC_R5_it1_5"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("12;")),scriptsTable1), 200, "GPP_SC_R5_it1_10"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("13;")),scriptsTable1), 200, "GPP_SC_R5_it1_15"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("14;")),scriptsTable1), 200, "GPP_SC_R5_it1_20"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("15;")),scriptsTable1), 200, "GPP_PURE_R5_it1_0"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("16;")),scriptsTable1), 200, "GPP_PURE_R5_it1_5"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("17;")),scriptsTable1), 200, "GPP_PURE_R5_it1_10"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("18;")),scriptsTable1), 200, "GPP_PURE_R5_it1_15"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("19;")),scriptsTable1), 200, "GPP_PURE_R5_it1_20"),
//
//              
              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("20;")),scriptsTable1), 200, "GPP_SC_R4_it1_20"),
              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("21;")),scriptsTable1), 200, "GPP_SC_R4_it2_20"),
              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("22;")),scriptsTable1), 200, "GPP_SC_R5_it1_20"),
              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("23;")),scriptsTable1), 200, "GPP_SC_R5_it2_20") 
              
              
              
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1249;1250;1206;1251;1327;1328;1329;")),scriptsTable1), 200, "GPP_PGS_ISO_1"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2377;1423;2291;2529;1328;")),scriptsTable1), 200, "GPP_PGS_ISO_50"),
//                new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1014;1286;1044;1149;1589;1445;")),scriptsTable1), 200, "GPP_PGS_ISO_100"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2286;2749;4275;3894;3257;")),scriptsTable1), 200, "GPP_PGS_ISO_150"),
//                new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1014;1044;1149;1286;1368;1445;1039;")),scriptsTable1), 200, "GPP_PGS_ISO_200"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2286;5610;5769;2749;3640;")),scriptsTable1), 200, "GPP_PGS_ISO_250"),
//                new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1014;1044;1149;1445;1155;1039;1286;")),scriptsTable1), 200, "GPP_PGS_ISO_300")
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2749;7135;2286;7088;5256;")),scriptsTable1), 200, "GPP_PGS_ISO_350"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("3923;1296;3517;1874;")),scriptsTable1), 200, "GPP_PGS_ISO_400"),
////                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2286;6700;")),scriptsTable1), 200, "GPP_PGS_ISO_400"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1296;2444;1353;3923;")),scriptsTable1), 200, "GPP_PGS_ISO_500"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2790;1296;6971;")),scriptsTable1), 200, "GPP_PGS_ISO_600"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("3639;1296;5106;2444;")),scriptsTable1), 200, "GPP_PGS_ISO_700"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("4654;1296;3517;")),scriptsTable1), 200, "GPP_PGS_ISO_800"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1296;2769;1042;3517;")),scriptsTable1), 200, "GPP_PGS_ISO_900")
                
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1137;1138;1139;1140;1141;1142;1042;")),scriptsTable2), 200, "GPP_PGS_LST_0"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1137;1138;1139;1140;1141;1142;1042;")),scriptsTable2), 200, "GPP_PGS_LST_1"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2071;2069;3726;2979;2203;3810;1705;4607;4828;1859")),scriptsTable2), 200, "GPP_PGS_LST_50"),                
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("5194;6642;3726;1497;2931;5195;5801;5830;5868;")),scriptsTable2), 200, "GPP_PGS_LST_100"),
//                new LightPGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2687;3789;11022;11089;3434;9073")),scriptsTable2), 200, "GPP_PGS_LST_FLA"),                 
                //new SCVPlus(utt),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts(utt, bestGAAAAI), "GAAAAI_A3N"), //12
////                /*
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("1062;1063;1064;1065;1066;1067;")),scriptsTable1), "GPP_A3N_ISO_0"),     
//                
////              new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
////              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
////              decodeScripts2(utt, new ArrayList<>(getListIfInteger("1249;1250;1206;1251;1327;1328;1329;")),scriptsTable1), "GPP_A3N_ISO_1"), 
////// 
////                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
////                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
////                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("2377;1423;2291;2529;1328;")),scriptsTable1), "GPP_A3N_ISO_50"), 
//////                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("3280;2681;2291;1551;2892;3101;2510;")),scriptsTable1), "GPP_A3N_ISO_100"), 
////                
////                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
////                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
////                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("2286;2749;4275;3894;3257;")),scriptsTable1), "GPP_A3N_ISO_150"), 
////                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("5036;3524;4978;4523;")),scriptsTable1), "GPP_A3N_ISO_200"), 
////                
////                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
////                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
////                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("2286;5610;5769;2749;3640;")),scriptsTable1), "GPP_A3N_ISO_250"), 
////                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("5637;5826;4523;5248;5166;5654;5470;6171;")),scriptsTable1), "GPP_A3N_ISO_300"), 
//                
////                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
////                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
////                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("2749;7135;2286;7088;5256;;")),scriptsTable1), "GPP_A3N_ISO_350"), 
////                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("6417;7047;6454;")),scriptsTable1), "GPP_A3N_ISO_400"), 
//                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("8681;8000;7995;7047;")),scriptsTable1), "GPP_A3N_ISO_500"),
//                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("8621;8074;9075;9514;7988;9475;9447;")),scriptsTable1), "GPP_A3N_ISO_600"),
//                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("10554;5878;10955;")),scriptsTable1), "GPP_A3N_ISO_700"), 
//                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("10554;5878;10955;")),scriptsTable1), "GPP_A3N_ISO_800"), 
//                
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("10554;5878;10955;")),scriptsTable1), "GPP_A3N_ISO_900") 

                //************************

//            new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//            decodeScripts2(utt, new ArrayList<>(getListIfInteger("1137;1138;1139;1140;1141;1142;1042;")),scriptsTable2), "GPP_A3N_LST_0"),  
                
//            new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//            decodeScripts2(utt, new ArrayList<>(getListIfInteger("1137;1138;1139;1140;1141;1142;1042;")),scriptsTable2), "GPP_A3N_LST_1"),  
//                
//            new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//            decodeScripts2(utt, new ArrayList<>(getListIfInteger("2071;2069;3726;2979;2203;3810;1705;4607;4828;1859")),scriptsTable2), "GPP_A3N_LST_50"),
//                
//            new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//            decodeScripts2(utt, new ArrayList<>(getListIfInteger("5194;6642;3726;1497;2931;5195;5801;5830;5868;")),scriptsTable2), "GPP_A3N_LST_100"),  
//      
//            new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//            decodeScripts2(utt, new ArrayList<>(getListIfInteger("4018")),scriptsTable2), "GPP_A3N_LST_125")
            
//            new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//            decodeScripts2(utt, new ArrayList<>(getListIfInteger("2687;3789;11022;11089;3434;9073")),scriptsTable2), "GPP_A3N_LST_FLA")
                
//                 */
                //bg1
                //new PGSSCriptChoiceRandom(utt, decodeScripts(utt, GA_PGS), "GA_PGS",2,200),
                //new SSSmRTSScriptChoiceRandom(utt, decodeScripts(utt, GA_SSS), "GA_SSS",2,200),
                //plus
                //new PGSSCriptChoiceRandom(utt, decodeScripts(utt, "0;1;2;3;100;101;102;103;299;"), "PGS+",2,200),
                //new SSSmRTSScriptChoiceRandom(utt, decodeScripts(utt, "0;1;2;3;100;101;102;103;299;"), "SSS+",2,200),
                //new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt), new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,decodeScripts(utt, GA_A3N)),
                /*
                new PGSSCriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("8756;6292;7903;7486;8265;9031;8953;"))), "PGS_GA"),
                new SSSmRTSScriptChoice(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("8756;6292;7903;7486;8265;9031;8953;"))), "SSS_GA"),
                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("8756;6292;7903;7486;8265;9031;8953;"))), "GA-225")
                 
                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
                        decodeScripts(utt, bestGAAAAI), "GAAAAI"),*/
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("254;687;637;664;931;"))), "GA-0"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("839;4149;2050;4521;4569;3132;3478;3634;2326;4152;2049;4160;3725;2707;2500;3867;4300;4570;4289;4410;4290;3922;3524;3257;"))), "GA-50"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("2470;839;7514;7017;6359;7214;5249;6967;7707;2185;6711;6918;4410;2050;7765;6426;7487;7567;"))), "GA-100"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("839;10414;10804;10031;8651;8038;4410;9051;10081;10165;10307;10483;9976;9784;8563;10545;"))), "GA-150"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("839;13103;13512;7704;12992;12185;12080;12454;12839;13106;13291;13292;"))), "GA-200"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("15166;12839;13814;13826;14895;14860;9507;15006;15750;15753;15938;15963;14689;15752;15216;15063;15499;15630;15631;"))), "GA-250"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("15166;16647;12839;14895;15963;15006;14689;16509;17916;17917;"))), "GA-300"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("15166;19221;12839;14895;19100;15006;20483;19712;20340;20232;14689;18348;20277;19925;"))), "GA-350"),
//                new CmabAssymetricMCTS(100, -1, 100, 1, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("20738;21331;18149;19100;15006;20844;17307;16780;"))), "GA-400")
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

        } while (!gameover && (gs.getTime() < MAXCYCLES));

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

    public List<AI> decodeScripts2(UnitTypeTable utt, ArrayList<Integer> iScripts,HashMap<BigDecimal, String> table) {
        List<AI> scriptsAI = new ArrayList<>();
        System.out.println("size "+table.size());
        for (Integer idSc : iScripts) {
            System.out.println("tam tab"+scriptsTable1.size());
            System.out.println("tam tab"+scriptsTable2.size());
            System.out.println("id "+idSc+" Elems "+scriptsTable1.get(BigDecimal.valueOf(idSc)));
            System.out.println("id "+idSc+" Elems "+scriptsTable2.get(BigDecimal.valueOf(idSc)));
            scriptsAI.add(buildCommandsIA(utt, table.get(BigDecimal.valueOf(idSc))));
        }

        return scriptsAI;
    }

    public static AI buildScript(UnitTypeTable utt, ArrayList<Integer> iRules) {
        //System.out.println("laut");
        TableCommandsGenerator tcg = TableCommandsGenerator.getInstance(utt);
        List<ICommand> commands = new ArrayList<>();
        //System.out.println("sizeeiRules "+iRules.size());
        for (Integer idSc : iRules) {
            //System.out.println("idSc "+idSc);
            commands.add(tcg.getCommandByID(idSc));;
        }
        AI aiscript = new ChromosomeAI(utt, commands, "P1", "", new HashSet<String>(),new HashMap<Long, String>());

        return aiscript;
    }

    public HashMap<BigDecimal, String> buildScriptsTable(HashMap<BigDecimal, String> tabl, String complement) {
    	tabl = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathTableScripts + "/ScriptsTable_"+complement+".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String code = line.substring(line.indexOf(" "), line.length());
                String[] strArray = line.split(" ");
                int idScript = Integer.decode(strArray[0]);
                tabl.put(BigDecimal.valueOf(idScript), code);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tabl;
    }

    public ArrayList<Integer> getListIfInteger(String conf) {
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = conf.split(";");

        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }

        return iScriptsAi1;
    }

    private AI buildCommandsIA(UnitTypeTable utt, String code) {
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        HashSet<String> usedCommands=new HashSet<String> ();
        HashMap<Long, String> counterByFunction=new HashMap<Long, String>();
        AI aiscript = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands,counterByFunction);
        return aiscript;
    }
}
