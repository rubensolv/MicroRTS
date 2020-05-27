
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
import ai.competition.dropletGNS.Droplet;
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
                "maps/NoWhereToRun9x8.xml"
        		//"maps/battleMaps/Others/RangedHeavyMixed.xml"
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
        
        //AI aiChampionBattle=decodeScripts2(utt, new ArrayList<>(getListIfInteger("0;")),scriptsTable1).get(0);
        //AI aiChampion8x8=decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1).get(0);
        //AI aiChampion16=decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1).get(0);
        
        ais = new ArrayList<>(Arrays.asList(
        		//new AHTNAI(utt),
                new Droplet(utt),
                new NaiveMCTS(utt),
                new PuppetSearchMCTS(utt),
                new StrategyTactics(utt),
                new LightPGSSCriptChoice(utt, decodeScripts(utt, "0;1;2;3;"), 200, "PGSR_LIGHT"),
                new LightSSSmRTSScriptChoice(utt, decodeScripts(utt, "0;1;2;3;"), 200, "SSSR_LIGHT"),
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 4), //lr
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 5), //HR
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 6), //RR
                new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 3), //WR
                new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
                        decodeScripts(utt, "1;2;3;"), "A3N"),
//                new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                        new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//                        decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1,""), "A3N_Trace"),
                
  
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("0;")),scriptsTable1,""), "GPPD_SCHEME_1_1"),     
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1,""), "GPPD_SCHEME_1_2"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1,""), "GPPD_SCHEME_2_1"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1,""), "GPPD_SCHEME_2_2"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("4;")),scriptsTable1,""), "GPPD_SCHEME_3_1"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1,""), "GPPD_SCHEME_3_2"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("6;")),scriptsTable1,""), "GPPD_SCHEME_4_1"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("7;")),scriptsTable1,""), "GPPD_SCHEME_4_2"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("8;")),scriptsTable1,""), "GPPD_SCHEME_5_1"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("9;")),scriptsTable1,""), "GPPD_SCHEME_5_2"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("8;")),scriptsTable1,""), "GPPD_SCHEME_6_1"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("9;")),scriptsTable1,""), "GPPD_SCHEME_6_2"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("10;")),scriptsTable1,""), "GPPD__PURE_1"), 
//              
//              new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//              new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("11;")),scriptsTable1,""), "GPPD__PURE_2") 
                
                
//                decodeScripts2(utt, new ArrayList<>(getListIfInteger("0;")),scriptsTable1,"WR_OURGRAMMAR").get(0),
//                decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1,"LR_OURGRAMMAR").get(0),
//                decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1,"HR_OURGRAMMAR").get(0),
//                decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1,"RR_OURGRAMMAR").get(0)
              
              decodeScripts2(utt, new ArrayList<>(getListIfInteger("0;")),scriptsTable1,"GPPD_BEST_RR1").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1,"GPPD_BEST_RR2").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1,"GPPD_BEST_RR3").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1,"GPPD_BEST_RR4").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("4;")),scriptsTable1,"GPPD_BEST_RR5").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1,"GPPD_BEST_RR6").get(0)
              
            decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1,"GPPD_BEST_CURR_RR1").get(0)
        		
        		
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1,"GPPD_BEST_RR1").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1,"GPPD_BEST_CURR_RR1").get(0),
              //decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1,"GPPD_BEST_SCHM5_RR1").get(0)
              //decodeScripts2(utt, new ArrayList<>(getListIfInteger("6;")),scriptsTable1,"GPPD_BEST_RR1").get(0)
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1,"GPPD_BEST_SC_RR3").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1,"GPPD_BEST_SC_RR4").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("4;")),scriptsTable1,"GPPD_BEST_SC_RR5").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1,"GPPD_BEST_SC_RR6").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("6;")),scriptsTable1,"GPPD_BEST_RR1").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("7;")),scriptsTable1,"GPPD_BEST_RR2").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("8;")),scriptsTable1,"GPPD_BEST_RR3").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("9;")),scriptsTable1,"GPPD_BEST_RR4").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("10;")),scriptsTable1,"GPPD_BEST_RR5").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("11;")),scriptsTable1,"GPPD_BEST_RR6").get(0)
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1,"WR_OURGRAMMAR").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("4;")),scriptsTable1,"LR_OURGRAMMAR").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1,"HR_OURGRAMMAR").get(0),
//              decodeScripts2(utt, new ArrayList<>(getListIfInteger("6;")),scriptsTable1,"RR_OURGRAMMAR").get(0)
                
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("0;")),scriptsTable1), 200, "GPPD_SCHEME_1_1"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("1;")),scriptsTable1), 200, "GPPD_SCHEME_1_2"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("2;")),scriptsTable1), 200, "GPPD_SCHEME_2_1"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("3;")),scriptsTable1), 200, "GPPD_SCHEME_2_2"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("4;")),scriptsTable1), 200, "GPPD_SCHEME_3_1"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("5;")),scriptsTable1), 200, "GPPD_SCHEME_3_2"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("6;")),scriptsTable1), 200, "GPPD_SCHEME_4_1"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("7;")),scriptsTable1), 200, "GPPD_SCHEME_4_2"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("8;")),scriptsTable1), 200, "GPPD_SCHEME_5_1"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("9;")),scriptsTable1), 200, "GPPD_SCHEME_5_2"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("10;")),scriptsTable1), 200, "GPPD__PURE_1"),
//              new LightPGSSCriptChoiceNoWaits(utt, decodeScripts2(utt, new ArrayList<>(getListIfInteger("11;")),scriptsTable1), 200, "GPPD__PURE_2")
//              
                
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
        long countingTimeAI1=0;
        long countingTimeAI2=0;
        long counterCallsAI1=0;
        long counterCallsAI2=0;
        //System.out.println("Tempo de execução P2="+(startTime = System.currentTimeMillis() - startTime));
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                totalAction++;
                startTime = System.currentTimeMillis();

                PlayerAction pa1 = ai1.getAction(0, gs);
                //dados de tempo ai1
                timeTemp = (System.currentTimeMillis() - startTime);
                if(timeTemp>0)
                {
                	counterCallsAI1++;
                	countingTimeAI1=countingTimeAI1+timeTemp;
                }
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
                if(timeTemp>0)
                {
                	counterCallsAI2++;
                	countingTimeAI2=countingTimeAI2+timeTemp;
                }
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

        } while (!gameover && (gs.getTime() < 6000));

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
        
//        log.add("Avg Response "+ai1.toString()+" "+ countingTimeAI1/counterCallsAI1);
//        log.add("Avg Response "+ai2.toString()+" "+ countingTimeAI2/counterCallsAI2);

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

    public List<AI> decodeScripts2(UnitTypeTable utt, ArrayList<Integer> iScripts,HashMap<BigDecimal, String> table, String nameIA) {
        List<AI> scriptsAI = new ArrayList<>();
        System.out.println("size "+table.size());
        for (Integer idSc : iScripts) {
            System.out.println("tam tab"+scriptsTable1.size());
            System.out.println("tam tab"+scriptsTable2.size());
            System.out.println("id "+idSc+" Elems "+scriptsTable1.get(BigDecimal.valueOf(idSc)));
            System.out.println("id "+idSc+" Elems "+scriptsTable2.get(BigDecimal.valueOf(idSc)));
            scriptsAI.add(buildCommandsIA(utt, table.get(BigDecimal.valueOf(idSc)),nameIA));
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

    private AI buildCommandsIA(UnitTypeTable utt, String code,String nameIA) {
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        HashSet<String> usedCommands=new HashSet<String> ();
        HashMap<Long, String> counterByFunction=new HashMap<Long, String>();
        AI aiscript = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands,counterByFunction,nameIA);
        return aiscript;
    }
}
