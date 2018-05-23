/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.SOA;

import ai.aiSelection.AlphaBetaSearch.AlphaBetaSearch;
import ai.core.AI;
import ai.asymmetric.PGS.PGSSCriptChoice;
import ai.asymmetric.SSS.SSSmRTSScriptChoice;
import ai.cluster.CIA_Enemy;
import ai.cluster.CIA_PlayoutTemporal;
import ai.cluster.CIA_TDLearning;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.mcts.naivemcts.NaiveMCTS;
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
                /* "maps/battleMaps/8x8/4x4Mixed_combatRangedProtection_map8x8.xml",
                "maps/battleMaps/8x8/4x4Mixed_crazyPosition_map8x8.xml",
                "maps/battleMaps/8x8/4x4Mixed_map8x8.xml",
                "maps/battleMaps/8x8/four_goups_Battle_8x8.xml",
                "maps/battleMaps/8x8/lineBattle8x8.xml",
                "maps/battleMaps/8x8/melee2x2Mixed_map8x8.xml",
                "maps/battleMaps/16x16/ComplexBattleWithWalls16x16.xml",
                "maps/battleMaps/16x16/fourGroupsWithBlocks16x16.xml",
                "maps/battleMaps/16x16/melee7x7Mixed16.xml",
                "maps/battleMaps/16x16/melee16x16Mixed8.xml",
                "maps/battleMaps/16x16/melee16x16Mixed12.xml",
                "maps/battleMaps/16x16/melee18x18Mixed4groups.xml",
                "maps/battleMaps/24x24/ConfinedFourGroupsMixed24x24.xml",
                "maps/battleMaps/24x24/ConfinedTwoGroupsMixed24x24.xml",
                "maps/battleMaps/24x24/DoubleMapaWithBlockFourGroupsLightHeavy24x24.xml",
                "maps/battleMaps/24x24/DoubleMapaWithBlockFourGroupsMixed24x24.xml",
                "maps/battleMaps/24x24/MiddleBlockTwoGroupsMixed24x24.xml",
                "maps/battleMaps/24x24/SimpleBatlle14x14Mixed24x24.xml"*/
                "maps/battleMaps10Times/24x24/DoubleMapaWithBlockFourGroupsMixed24x24.xml"
        ));

        UnitTypeTable utt = new UnitTYpeTableBattle();
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

        List<AI> ais = new ArrayList<>(Arrays.asList(
                //new AHTNAI(utt),
                new NaiveMCTS(utt),
                //new BS3_NaiveMCTS(utt),
                //new PuppetSearchMCTS(utt),
                //new StrategyTactics(utt),
                //new PGSmRTS(utt),
                //new SSSmRTS(utt),
                //new POLightRush(utt),
                //new POWorkerRush(utt),
                //new CIA(utt),
                new CIA_Enemy(utt),
                //new CIA_EnemyWithTime(utt),
                //new CIA_EnemyEuclidieanInfluence(utt),
                new AlphaBetaSearch(utt),
                //new CIA_PlayoutCluster(utt),
                //new CIA_PlayoutPower(utt)
                new CIA_PlayoutTemporal(utt, 2, 2),
                new CIA_PlayoutTemporal(utt, 2, 4),
                new CIA_TDLearning(utt, 2, 2),
                new CIA_TDLearning(utt, 2, 4)
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
        log.add("Mapa= " + map + "\n");

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
