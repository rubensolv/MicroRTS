/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.SOA.ScriptedEval;

import util.SOA.*;
import PVAI.util.Permutation;
import ai.RandomBiasedAI;
import ai.ScriptsGenerator.ChromosomeAI_UCB;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;

import static ai.asymmetric.PGS.GameVisualSimulationTest.decodeScripts;
import ai.core.AI;
import ai.asymmetric.GAB.SandBox.GABScriptChoose;
import ai.asymmetric.PGS.PGSSCriptChoice;
import ai.asymmetric.PGS.PGSSCriptChoiceRandom;
import ai.asymmetric.SSS.SSSmRTSScriptChoice;
import ai.asymmetric.SSS.SSSmRTSScriptChoiceRandom;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.sqlLite.Log_Facade;
import util.sqlLite.UCB_Facade;
import util.sqlLite.Ucb;

/**
 *
 * @author rubens Classe responsável por rodar os confrontos entre duas IA's.
 * Ambiente totalmente observável.
 */
public class RoundRobinSampleUCB {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;
    private HashMap<BigDecimal, ArrayList<Integer>> scriptsTable;
    String pathTableScripts;

    public RoundRobinSampleUCB(String pathTableScripts) {
        this.pathTableScripts = pathTableScripts;
        buildScriptsTable();
    }

    public boolean run(String tupleAi1, String tupleAi2, Integer IDMatch, Integer Generation, String pathLog, int iMap) throws Exception {
        ArrayList<String> log = new ArrayList<>();
        //controle de tempo
        Instant timeInicial = Instant.now();
        Duration duracao;

        log.add("Tupla A1 = " + tupleAi1);
        log.add("Tupla A2 = " + tupleAi2);

        List<String> maps = new ArrayList<>(Arrays.asList(
                //"maps/24x24/basesWorkers24x24A.xml"
                //"maps/32x32/basesWorkers32x32A.xml"
                //"maps/8x8/basesWorkers8x8A.xml"
        //"maps/BroodWar/(4)BloodBath.scmB.xml"
        "maps/16x16/basesWorkers16x16A.xml"
        //"maps/8x8/basesWorkers8x8Obstacle.xml"
        //"maps/16x16/BasesWithWalls16x16.xml"
        ));

        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(maps.get(iMap), utt);

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 4000;
        }
        if (pgs.getHeight() == 9) {
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

        //decompõe a tupla
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = tupleAi1.replace("(", "").replace(")", "").split(";");

        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }

        ArrayList<Integer> iScriptsAi2 = new ArrayList<>();
        itens = tupleAi2.replace("(", "").replace(")", "").split(";");

        for (String element : itens) {
            iScriptsAi2.add(Integer.decode(element));
        }

        //check for possible updates in scriptsTable
        updateTableIfnecessary();

        //pgs 
//        AI ai1 = new PGSSCriptChoiceRandom(utt, decodeScripts(utt, iScriptsAi1), "PGSR", 2, 200);
//        AI ai2 = new PGSSCriptChoiceRandom(utt, decodeScripts(utt, iScriptsAi2), "PGSR", 2, 200);
//          AI ai1 = new PGSSCriptChoice(utt, decodeScripts(utt, iScriptsAi1), "PGS");
//          AI ai2 = new PGSSCriptChoice(utt, decodeScripts(utt, iScriptsAi2), "PGS");
        AI ai1 = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f,
                0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                new SimpleSqrtEvaluationFunction3(), true, utt,
                "ManagerClosestEnemy", 1, decodeScripts(utt, iScriptsAi1, 0));
        AI ai2 = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f,
                0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                new SimpleSqrtEvaluationFunction3(), true, utt,
                "ManagerClosestEnemy", 1, decodeScripts(utt, iScriptsAi2, 1));
//        AI ai1 = new GABScriptChoose(utt, 1, 7, decodeScripts(utt, iScriptsAi1), "GAB");
//        AI ai2 = new GABScriptChoose(utt, 1, 7, decodeScripts(utt, iScriptsAi1), "GAB");

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
        log.add("Mapa= " + maps.get(iMap) + "\n");

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

        } while (!gameover && (gs.getTime() < 8000));

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
        String stMatch = Integer.toString(IDMatch) + "" + Integer.toString(iMap);
        gravarLog(log, tupleAi1, tupleAi2, stMatch, Generation, pathLog);
        CmabAssymetricMCTS realAi1 = (CmabAssymetricMCTS) ai1;
        CmabAssymetricMCTS realAi2 = (CmabAssymetricMCTS) ai2;
        saveActivationRules(realAi1.getScripts(), realAi2.getScripts(), gs.winner());
        //System.exit(0);
        return true;
    }

    public void updateTableIfnecessary() {
        int currentSizeTable = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(pathTableScripts + "SizeTable.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                currentSizeTable = Integer.valueOf(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (scriptsTable.size() < currentSizeTable) {
            buildScriptsTable();
        }

    }

//    public static List<AI> decodeScripts(UnitTypeTable utt, ArrayList<Integer> iScripts) {
//        List<AI> scriptsAI = new ArrayList<>();
//
//        ScriptsCreator sc = new ScriptsCreator(utt, 300);
//        ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet = sc.getScriptsMixReducedSet();
//        
//        for (Integer idSc : iScripts) {
//            scriptsAI.add(scriptsCompleteSet.get(idSc));
//        }
//
//        return scriptsAI;
//    }
    public List<AI> decodeScripts(UnitTypeTable utt, ArrayList<Integer> iScripts, int player) {
        List<AI> scriptsAI = new ArrayList<>();

        for (Integer idSc : iScripts) {
            //System.out.println("tam tab"+scriptsTable.size());
            //System.out.println("id "+idSc+" Elems "+scriptsTable.get(BigDecimal.valueOf(idSc)));
            scriptsAI.add(buildScript(utt, scriptsTable.get(BigDecimal.valueOf(idSc)), player));
        }

        return scriptsAI;
    }

    public static AI buildScript(UnitTypeTable utt, ArrayList<Integer> iRules, int player) {
        //System.out.println("laut");
        TableCommandsGenerator tcg = TableCommandsGenerator.getInstance(utt);
        List<ICommand> commands = new ArrayList<>();
        HashMap<ICommand, Integer> dicCommands = new HashMap<>();
        //System.out.println("sizeeiRules "+iRules.size());
        for (Integer idSc : iRules) {
            //System.out.println("idSc "+idSc);
            ICommand temp = tcg.getCommandByID(idSc);
            commands.add(temp);
            dicCommands.put(temp, idSc);
            
        }
        AI aiscript = new ChromosomeAI_UCB(utt, commands, "P1", player, dicCommands);

        return aiscript;
    }

    public HashMap<BigDecimal, ArrayList<Integer>> buildScriptsTable() {

        scriptsTable = new HashMap<>();
        ArrayList<Integer> idsRulesList;
        try (BufferedReader br = new BufferedReader(new FileReader(pathTableScripts + "/ScriptsTable.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                idsRulesList = new ArrayList<>();
                String[] strArray = line.split(" ");
                int[] intArray = new int[strArray.length];
                for (int i = 0; i < strArray.length; i++) {
                    intArray[i] = Integer.parseInt(strArray[i]);
                }
                int idScript = intArray[0];
                int[] rules = Arrays.copyOfRange(intArray, 1, intArray.length);

                for (int i : rules) {
                    idsRulesList.add(i);
                }

                scriptsTable.put(BigDecimal.valueOf(idScript), idsRulesList);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return scriptsTable;
    }

    private void gravarLog(ArrayList<String> log, String tupleAi1, String tupleAi2, String IDMatch, Integer Generation, String pathLog) throws IOException {
        if (!pathLog.endsWith("/")) {
            pathLog += "/";
        }
        String nameArquivo = pathLog + "Eval_" + tupleAi1 + "_" + tupleAi2 + "_" + IDMatch + "_" + Generation + ".txt";
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

    private void saveActivationRules(List<AI> scriptsIA0, List<AI> scriptsIA1, int winner) {
        List<ChromosomeAI_UCB> AI0 = translate(scriptsIA0);
        List<ChromosomeAI_UCB> AI1 = translate(scriptsIA1);
        //get all rules activated
        List<Integer> rules_AI0_used = getRulesUsed(AI0);
        List<Integer> rules_AI1_used = getRulesUsed(AI1);
        
        if(winner == 0){
            saveRules(rules_AI0_used, 1);
            saveRules(rules_AI1_used, -1);
        }else{
            saveRules(rules_AI0_used, -1);
            saveRules(rules_AI1_used, 1);
        }
    }

    private List<ChromosomeAI_UCB> translate(List<AI> scripts) {
        List<ChromosomeAI_UCB> temp = new ArrayList<>();
        for (AI ai : scripts) {
            temp.add((ChromosomeAI_UCB) ai);
        }
        return temp;
    }

    private List<Integer> getRulesUsed(List<ChromosomeAI_UCB> AI0) {
        HashSet<Integer> rulesUsed = new HashSet<>();
        
        for (ChromosomeAI_UCB sc : AI0) {
            rulesUsed.addAll(sc.getRulesUsed());
        }
        
        return new ArrayList<>(rulesUsed);
    }

    private void saveRules(List<Integer> rules_AI0_used, int i) {
        for (Integer ruleID : rules_AI0_used) {            
            UCB_Facade.incrementQtdUsed(ruleID);
            Log_Facade.createNewReward(ruleID, i);
        }
    }
}
