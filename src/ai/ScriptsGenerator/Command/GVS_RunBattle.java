/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command;

import util.SOA.*;
import ai.PassiveAI;
import ai.RandomBiasedAI;
import ai.CMAB.A3NNoWait;
import ai.CMAB.A3NWithin;
import ai.CMAB.A3NWithinNoWait;
import ai.CMAB.CmabNaiveMCTS;
import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.FunctionGPCompiler;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.ahtn.AHTNAI;
import ai.core.AI;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.asymmetric.GAB.SandBox.GABScriptChoose;
import ai.asymmetric.PGS.LightPGSSCriptChoice;
import ai.asymmetric.PGS.LightPGSSCriptChoiceNoWaits;
import ai.asymmetric.PGS.PGSSCriptChoiceRandom;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.puppet.PuppetSearchMCTS;
import gui.PhysicalGameStatePanel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import Standard.StrategyTactics;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens Classe responsável por rodar os confrontos entre duas IA's.
 * Ambiente totalmente observável.
 */
public class GVS_RunBattle {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;
    private HashMap<BigDecimal, String> scriptsTable;
    String pathTableScripts;
    String pathLogsUsedCommands;
    ICompiler compiler = new MainGPCompiler();
    HashSet<String> usedCommands;
    EvaluationFunction evaluation = new SimpleSqrtEvaluationFunction3();
    

    public GVS_RunBattle(String pathTableScripts, String pathLogsUsedCommands) {
    	this.pathLogsUsedCommands=pathLogsUsedCommands;
        this.pathTableScripts = pathTableScripts;
        buildScriptsTable();
    }

    public boolean run(String tupleAi1, String tupleAi2, int iMap) throws Exception {
        ArrayList<String> log = new ArrayList<>();
        //controle de tempo
        Instant timeInicial = Instant.now();
        Duration duracao;

        log.add("Tupla A1 = " + tupleAi1);
        log.add("Tupla A2 = " + tupleAi2);

        List<String> maps = new ArrayList<>(Arrays.asList(
                //"maps/24x24/basesWorkers24x24A.xml"
                //"maps/32x32/basesWorkers32x32A.xml"
                //"maps/battleMaps/Others/RangedHeavyMixed.xml"
                //"maps/NoWhereToRun9x8.xml"
        //"maps/BroodWar/(4)BloodBath.scmB.xml"
        		//"maps/16x16/basesWorkers16x16A.xml"
        		"maps/8x8/basesWorkers8x8A.xml"
        ));

        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(maps.get(iMap), utt);

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
        
        //AI ai1= new PassiveAI(utt);
        //AI ai2=new RangedRush(utt);
        //AI ai2=new RangedRush(utt);
        //AI ai1=new WorkerRush(utt);
       // AI ai1=new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 3); //WR
        //AI ai2=new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 3); //WR
        //AI ai2=new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 4); //lr
        //pgs 
        //pgs 
        
        List<AI> scriptsRun1=decodeScripts(utt, iScriptsAi1);
        List<AI> scriptsRun2=decodeScriptsAlt(utt, iScriptsAi2);
        //AI ai2=scriptsRun1.get(0);
        AI ai2=scriptsRun1.get(0);
//      AI ai1 = new PGSSCriptChoiceRandom(utt, decodeScripts(utt, iScriptsAi1), "PGSR", 2, 200);
      //AI ai2 = new PGSSCriptChoiceRandom(utt, scriptsRun1, "PGSR", 1, 200);
        
        //AI ai1=new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 3); //WR
        //List<AI> scriptsRun1=decodeScripts(utt, iScriptsAi1);
        //List<AI> scriptsRun2=decodeScripts(utt, iScriptsAi2);
      	//AI ai2 = new LightPGSSCriptChoice(utt, scriptsRun1,200, "PGSR");
        //AI ai2 = new LightPGSSCriptChoiceNoWaits(utt, scriptsRun1,200, "PGSR");
      	//AI ai1 = new LightPGSSCriptChoice(utt, scriptsRun1,200, "PGSR");
      	//AI ai2=new PuppetSearchMCTS(utt);
      	//AI ai2=new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 6); //RR
       //AI ai2=new LightRush(utt);
        //AI ai2=new StrategyTactics(utt);
        //AI ai1=new WorkerRush(utt);
        AI ai1=new NaiveMCTS(utt);
//        AI ai2=new A3NWithin(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
//                new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
//                decodeScriptsOld(utt, "0;"), "A3N");
//        AI ai2=new PassiveAI();
//        
//        AI ai1 = new LightPGSSCriptChoiceNoWaits(utt, scriptsRun1,200, "PGSR");
        //AI ai1 = new LightPGSSCriptChoiceNoWaits(utt, scriptsRun1,200, "PGSR");
        
//      	AI ai2 = new LightPGSSCriptChoice(utt, scriptsRun,200, "PGSR");

//        new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//                new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,
//                RoundRobinClusterLeve.decodeScripts(utt, "0;1;2;3;"), "A1N")

//        AI ai2 = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f,
//                0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//                new SimpleSqrtEvaluationFunction3(), true, utt,
//                "ManagerRandom", 1, scriptsRun1);
//        
//        AI ai1 = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f,
//                0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//                new SimpleSqrtEvaluationFunction3(), true, utt,
//                "ManagerClosestEnemy", 3, scriptsRun1);
        
//      	AI ai1 = new A3NWithinNoWait(100, -1, 100, 1, 0.3f,
//                0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//                new SimpleSqrtEvaluationFunction3(), true, utt,
//                "ManagerRandom", 1, scriptsRun1);
//      	
//      	AI ai2 = new A3NWithinNoWait(100, -1, 100, 1, 0.3f,
//                0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//                new SimpleSqrtEvaluationFunction3(), true, utt,
//                "ManagerRandom", 1, scriptsRun1);
        
//  	AI ai1 = new A3NNoWait(100, -1, 100, 1, 0.3f,
//      0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//      new SimpleSqrtEvaluationFunction3(), true, utt,
//      "ManagerClosestEnemy", 1, scriptsRun1);
  	
//  	AI ai2 = new A3NNoWait(100, -1, 100, 1, 0.3f,
//  	      0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//  	      new SimpleSqrtEvaluationFunction3(), true, utt,
//  	      "ManagerClosestEnemy", 1, scriptsRun1);
  	
//  	AI ai2 = new CmabNaiveMCTS(100, -1, 200, 10, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//            new SimpleSqrtEvaluationFunction3(), true, "CmabCombinatorialGenerator", utt,scriptsRun1, "A1N");
        
//        AI ai2 = new CmabAssymetricMCTS(700, -1, 100, 1, 0.3f,
//        	      0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//        	      new SimpleSqrtEvaluationFunction3(), true, utt,
//        	      "ManagerRandom", 2, scriptsRun1);
//
//        			AI ai1 = new CmabAssymetricMCTS(700, -1, 100, 1, 0.3f,
//        	      0.0f, 0.4f, 0, new RandomBiasedAI(utt),
//        	      new SimpleSqrtEvaluationFunction3(), true, utt,
//        	      "ManagerRandom", 2, scriptsRun1);

//      AI ai1 = new GABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi1), "GAB");
//      AI ai2 = new GABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi2), "GAB");
//      AI ai1 = new SABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi1), "SAB");
//      AI ai2 = new SABScriptChoose(utt, 1, 2, decodeScripts(utt, iScriptsAi2), "SAB");

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
        JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 740, 740, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
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
                w.repaint();
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
//            double e = evaluation.evaluate(0, 1, gs);
//            double e2 = evaluation.evaluate(1, 0, gs);

            
            SimpleSqrtEvaluationFunction3 eval = new SimpleSqrtEvaluationFunction3();
            double e=eval.base_score(0, gs);
            double e2=eval.base_score(1, gs);
            
            System.out.println("fitness");
            System.out.println(e);
            System.out.println(e2);
        } while (!gameover && (gs.getTime() < MAXCYCLES));

        log.add("Total de actions= " + totalAction + " sumAi1= " + sumAi1 + " sumAi2= " + sumAi2 + "\n");

        log.add("Tempos de AI 1 = " + ai1.toString());
        log.add("Tempo minimo= " + ai1TempoMin + " Tempo maximo= " + ai1TempoMax + " Tempo medio= " + (sumAi1 / (long) totalAction));

        log.add("Tempos de AI 2 = " + ai2.toString());
        log.add("Tempo minimo= " + ai2TempoMin + " Tempo maximo= " + ai2TempoMax + " Tempo medio= " + (sumAi2 / (long) totalAction) + "\n");

        log.add("Winner " + Integer.toString(gs.winner()));
        log.add("Game Over");
        double e = evaluation.evaluate(1, 0, gs);
        if (gs.winner() == -1) {
            System.out.println("Empate!" + ai1.toString() + " vs " + ai2.toString() + " Max Cycles =" + MAXCYCLES + " Time:" + duracao.toMinutes());
            
           // System.out.println("eval "+e);
        }
        //System.exit(0);
        recordGrammars(createFullString(scriptsRun1, iScriptsAi2));
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

    public List<AI> decodeScripts(UnitTypeTable utt, ArrayList<Integer> iScripts) {
        List<AI> scriptsAI = new ArrayList<>();
//        for (Integer idSc : iScripts) {
//            //System.out.println("tam tab"+scriptsTable.size());
//            //System.out.println("id "+idSc+" Elems "+scriptsTable.get(BigDecimal.valueOf(idSc)));
//            scriptsAI.add(buildCommandsIA(utt, scriptsTable.get(BigDecimal.valueOf(idSc))));
//        }
        
//        scriptsAI.add(buildCommandsIA(utt, " if(HaveUnitsToDistantToEnemy(Ranged,7)) (attack(Ranged,closest)) (attack(Ranged,closest) moveToUnit(Heavy,Ally,lessHealthy) attack(Heavy,strongest) moveToUnit(Heavy,Ally,lessHealthy))"));
//        scriptsAI.add(buildCommandsIA(utt, "train(Worker,50,EnemyDir) for(u) (if(HaveQtdUnitsHarversting(3)) (attack(Worker,mostHealthy,u)) (harvest(50,u)))"));
//        scriptsAI.add(buildCommandsIA(utt, "train(Worker,20,Left)"));
        scriptsAI.add(buildCommandsIA(utt, "82964 for(u) (train(Worker,50,Up,u)) for(u) (if(HaveUnitsToDistantToEnemy(Worker,4,u)) (attack(Worker,closest,u))) for(u) (harvest(50,u) attack(Worker,closest,u)) for(u) (moveToUnit(Worker,Ally,mostHealthy,u))"));
//        scriptsAI.add(buildCommandsIA(utt, "moveToUnit(Worker,Ally,farthest)"));
//        scriptsAI.add(buildCommandsIA(utt, "attack(Ranged,closest) if(HaveQtdUnitsAttacking(Ranged,3)) (moveToUnit(Heavy,Ally,mostHealthy))",counterByFunction));
        //scriptsAI.add(buildCommandsIA(utt, "0 moveToUnit(Ranged,Ally,strongest) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Heavy,closest)) attack(Ranged,closest)"));
    
 
        
       //scriptsAI.add(buildCommandsIA(utt, "80581 attack(Ranged,closest) for(u) (if(HaveUnitsToDistantToEnemy(Heavy,11,u)) (attack(Heavy,closest,u)))"));
       //scriptsAI.add(buildCommandsIA(utt, "80781 for(u) (attack(Heavy,closest,u) if(HaveUnitsToDistantToEnemy(Heavy,11,u)) (attack(Heavy,mostHealthy,u)) attack(Heavy,mostHealthy,u)) if(HaveQtdUnitsbyType(Heavy,2)) (attack(Ranged,closest))"));
       // scriptsAI.add(buildCommandsIA(utt, "69226 for(u) (if(HaveUnitsToDistantToEnemy(Heavy,4,u)) (attack(Heavy,closest,u)) attack(Ranged,closest,u) moveToUnit(Heavy,Ally,farthest,u)) attack(Heavy,mostHealthy)"));

        
  //      scriptsAI.add(buildCommandsIA(utt, "83422 for(u) (if(HaveQtdUnitsbyType(Worker,2,u)) (train(Worker,50,Right,u))) train(Worker,50,Down) for(u) (if(HaveQtdUnitsbyType(Worker,2,u)) (attack(Worker,closest,u)) (harvest(50,u) attack(Worker,closest,u)))"));

        //        scriptsAI.add(buildCommandsIA(utt, "0 moveToUnit(Ranged,Ally,strongest) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Heavy,closest)) attack(Ranged,closest)"));
//        scriptsAI.add(buildCommandsIA(utt, "10315 if(HaveUnitsToDistantToEnemy(Ranged,3)) (moveToUnit(Ranged,Ally,strongest)) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Ranged,3)) (attack(Ranged,closest)) (moveToUnit(Ranged,Ally,strongest)) moveToUnit(Ranged,Ally,strongest) attack(Heavy,farthest) attack(Heavy,farthest)"));
//        scriptsAI.add(buildCommandsIA(utt, "train(Worker,20,EnemyDir) harvest(3) harvest(3) harvest(2) attack(All,closest) harvest(2)"));
//        scriptsAI.add(buildCommandsIA(utt, "9046 if(HaveUnitsToDistantToEnemy(Ranged,14)) (attack(Heavy,closest) attack(Heavy,closest)) (moveaway(Ranged)) moveToUnit(Heavy,Ally,farthest) if(HaveUnitsToDistantToEnemy(Heavy,3)) (attack(Ranged,closest) moveToUnit(Ranged,Ally,strongest)) (moveToUnit(Heavy,Ally,farthest))"));
          //     scriptsAI.add(buildCommandsIA(utt, "80484 attack(Ranged,closest) for(u) (if(HaveQtdUnitsbyType(Ranged,4,u)) (moveToUnit(Heavy,Ally,lessHealthy,u)) (attack(Heavy,closest,u)))"));


        return scriptsAI;
    }
    
    public List<AI> decodeScriptsAlt(UnitTypeTable utt, ArrayList<Integer> iScripts) {
        List<AI> scriptsAI = new ArrayList<>();
//        for (Integer idSc : iScripts) {
//            //System.out.println("tam tab"+scriptsTable.size());
//            //System.out.println("id "+idSc+" Elems "+scriptsTable.get(BigDecimal.valueOf(idSc)));
//            scriptsAI.add(buildCommandsIA(utt, scriptsTable.get(BigDecimal.valueOf(idSc))));
//        }
        //System.out.println("ay " +new BigDecimal(0.87));
       scriptsAI.add(buildCommandsIA(utt, "80531 if(HaveUnitsToDistantToEnemy(Worker,14)) (train(Worker,50,Up) attack(Worker,closest)) (train(Worker,50,Up)) for(u) (harvest(50,u))"));
 //       scriptsAI.add(buildCommandsIA(utt, "80467 for(u) (attack(Ranged,closest,u)) for(u) (if(HaveUnitsToDistantToEnemy(Heavy,11,u)) (attack(Heavy,closest,u)))"));
 //       scriptsAI.add(buildCommandsIA(utt, "80668 for(u) (if(HaveUnitsToDistantToEnemy(Heavy,11,u)) (attack(Heavy,closest,u))) attack(Ranged,closest)"));
  //      scriptsAI.add(buildCommandsIA(utt, "80689 if(HaveEnemiesStrongest(Ranged)) (attack(Ranged,closest)) for(u) (attack(Ranged,closest,u)) for(u) (if(HaveUnitsToDistantToEnemy(Heavy,11,u)) (attack(Heavy,closest,u)))"));
 //       scriptsAI.add(buildCommandsIA(utt, "80484 attack(Ranged,closest) for(u) (if(HaveQtdUnitsbyType(Ranged,4,u)) (moveToUnit(Heavy,Ally,lessHealthy,u)) (attack(Heavy,closest,u)))"));
   //  scriptsAI.add(buildCommandsIA(utt, "10942 train(Worker,20,EnemyDir) harvest(3) attack(Worker,closest) harvest(3) harvest(3) harvest(3) train(Worker,20,Up)"));

        
        
        //        scriptsAI.add(buildCommandsIA(utt, "837 if(HaveQtdEnemiesbyType(Heavy,3)) (moveToUnit(Ranged,Ally,strongest)) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Ranged,closest) attack(Ranged,closest)) (attack(Heavy,closest) attack(Ranged,closest)) if(HaveEnemiesStrongest(Ranged)) (moveToUnit(Ranged,Ally,strongest))"));
//        scriptsAI.add(buildCommandsIA(utt, "0 moveToUnit(Ranged,Ally,strongest) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Heavy,closest)) attack(Ranged,closest)"));
//        scriptsAI.add(buildCommandsIA(utt, "10188 attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Ranged,3)) (attack(Heavy,farthest)) (attack(Heavy,farthest)) moveToUnit(Ranged,Ally,strongest) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Heavy,closest)) attack(Ranged,closest)"));
//        scriptsAI.add(buildCommandsIA(utt, "10195 moveToUnit(Ranged,Ally,strongest) if(HaveUnitsToDistantToEnemy(Heavy,19)) (attack(Ranged,closest)) attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Ranged,lessHealthy)) (attack(Ranged,closest)) attack(Ranged,closest)"));
//        scriptsAI.add(buildCommandsIA(utt, "8158 moveToUnit(Ranged,Ally,strongest) attack(Heavy,closest) attack(Ranged,closest)"));
//        scriptsAI.add(buildCommandsIA(utt, "10227 attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,11)) (attack(Heavy,closest)) attack(Ranged,lessHealthy) moveToUnit(Ranged,Ally,strongest)"));
//        scriptsAI.add(buildCommandsIA(utt, "10248 attack(Heavy,closest) if(HaveUnitsToDistantToEnemy(Heavy,15)) (attack(Ranged,lessHealthy)) (attack(Ranged,lessHealthy)) attack(Ranged,lessHealthy) if(HaveUnitsToDistantToEnemy(Heavy,15)) (attack(Ranged,lessHealthy)) attack(Ranged,lessHealthy) if(HaveUnitsToDistantToEnemy(Ranged,8)) (attack(Ranged,lessHealthy)) attack(Heavy,closest)"));
//          scriptsAI.add(buildCommandsIA(utt, "attack(Light,closest)",counterByFunction));
        
        return scriptsAI;
    }
    
    public static List<AI> decodeScriptsOld(UnitTypeTable utt, String sScripts) {

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

    public String buildCompleteGrammar(UnitTypeTable utt, ArrayList<Integer> iScripts) {
        List<AI> scriptsAI = new ArrayList<>();
        String portfolioGrammar = "";

        for (Integer idSc : iScripts) {
            //System.out.println("tam tab"+scriptsTable.size());
            //System.out.println("id "+idSc+" Elems "+scriptsTable.get(BigDecimal.valueOf(idSc)));
            portfolioGrammar = portfolioGrammar + scriptsTable.get(BigDecimal.valueOf(idSc)) + ";";
        }

        return portfolioGrammar;
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
        AI aiscript = new ChromosomeAI(utt, commands, "P1", "", new HashSet<String>(), new HashMap<Long, String>());

        return aiscript;
    }

    public HashMap<BigDecimal, String> buildScriptsTable() {
        scriptsTable = new HashMap<>();
        String line="";
        try (BufferedReader br = new BufferedReader(new FileReader(pathTableScripts + "/ScriptsTable.txt"))) {
            while ((line = br.readLine()) != null) {
                String code = line.substring(line.indexOf(" "), line.length());
                String[] strArray = line.split(" ");
                int idScript = Integer.decode(strArray[0]);
                scriptsTable.put(BigDecimal.valueOf(idScript), code);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block            
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(Exception e){
            System.out.println(line);
            System.out.println(e);
        }

        return scriptsTable;
    }

    private AI buildCommandsIA(UnitTypeTable utt, String code) {
    	HashMap<Long, String> counterByFunction=new HashMap<Long, String>();
    	usedCommands=new HashSet<String> ();
    	FunctionGPCompiler.counterCommands=0;
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        System.out.println("ia "+commandsGP);
        AI aiscript = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands,counterByFunction);
        return aiscript;
    }
    
    private List<String> createFullString(List<AI> scriptsRun, ArrayList<Integer> iScriptsAi)
    {
    	
    	List<String> listOfCompleteStrings=new ArrayList<String>();
    	String newComplete="";
		for (int i=0; i<scriptsRun.size();i++) {
			newComplete="";
			newComplete=newComplete+String.valueOf(iScriptsAi.get(i))+" ";
			for(String str :((ChromosomeAI)((scriptsRun).get(i))).usedCommands)
    		{
				newComplete=newComplete+str+" ";
    		}
			
			listOfCompleteStrings.add(newComplete);

		}
		
		return listOfCompleteStrings;
    }
    
    private void recordGrammars(List<String> listOfCompleteStrings) {
		
    	
    	File pathCommandsUsed = new File(pathLogsUsedCommands);
        if (!pathCommandsUsed.exists()) {
        	pathCommandsUsed.mkdir();
        }
        
    	try(FileWriter fw = new FileWriter(pathLogsUsedCommands+"LogsGrammars.txt", true);
    		    BufferedWriter bw = new BufferedWriter(fw);
    			PrintWriter out = new PrintWriter(bw))
    		{	

    		for(String str :listOfCompleteStrings)
    		{
    			out.println(str);
    		}

	   
    		} catch (IOException e) {
    		    //exception handling left as an exercise for the reader
    		}
		
	}
}
