/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.twophasessa.tests;

import Standard.StrategyTactics;
import ai.CMAB.A3NNoWait;
import ai.CMAB.A3NWithin;
import ai.CMAB.CmabNaiveMCTS;
import ai.RandomBiasedAI;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.ahtn.AHTNAI;
import ai.asymmetric.GAB.SandBox.GABScriptChoose;
import ai.asymmetric.PGS.LightPGSSCriptChoice;
import ai.asymmetric.SAB.SABScriptChoose;
import ai.asymmetric.SSS.LightSSSmRTSScriptChoice;
import ai.competition.dropletGNS.DropletWithin;
import ai.competition.newBotsEval.botEmptyBase;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.puppet.PuppetSearchMCTS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.units.UnitTypeTable;
import static util.SOA.RoundRobinClusterLeve_Cluster.decodeScripts;

/**
 *
 * @author rubens
 */
public class RoundRobinLeagueEvaluationLog {

    private static final int QTD_ROUND_ROBIN = 1;
    private static final String uniqueID = UUID.randomUUID().toString();

    public static void main(String[] args) throws Exception {       
        String scriptInicial = "train(Worker,50,EnemyDir) harvest(2) if(HaveUnitsinEnemyRange(Worker)) (attack(Worker,closest)) if(HaveQtdUnitsbyType(Worker,15)) (attack(Worker,mostHealthy)) if(IsPlayerInPosition(Up)) (moveToCoord(Worker,5,6) moveToCoord(Worker,18,6)) if(IsPlayerInPosition(Down)) (moveToCoord(Worker,18,15) moveToCoord(Worker,5,15)) if(HaveQtdUnitsbyType(Worker,10)) (harvest(10))";

        String script1f1 = "train(Worker,50,EnemyDir) harvest(2) if(HaveUnitsinEnemyRange(Worker)) (attack(Worker,closest)) if(HaveQtdUnitsbyType(Worker,1)) (attack(Worker,mostHealthy)) if(IsPlayerInPosition(Up)) (moveaway(Heavy) moveOnceToCoord(Ranged,4,9,8) moveToCoord(Worker,0,0) attack(Ranged,weakest)) if(IsPlayerInPosition(Down)) (moveToCoord(Worker,18,15) moveToCoord(Heavy,0,0) moveToUnit(Heavy,Enemy,lessHealthy)) if(HaveQtdUnitsbyType(Worker,10)) (train(Worker,9,EnemyDir) moveToUnit(Ranged,Enemy,closest) moveaway(Heavy))";
        String script2f1 = "harvest(3) moveaway(Worker) moveOnceToCoord(Heavy,4,0,13) train(Ranged,3,Up) train(Light,5,Down) attack(Heavy,mostHealthy) build(Barrack,1,Left) moveToCoord(Light,0,0) moveToCoord(Heavy,0,0) moveOnceToCoord(Heavy,9,5,4) if(HaveUnitsinEnemyRange(Heavy)) (moveOnceToCoord(Light,3,7,12) moveaway(Light) build(Barrack,1,Left)) (moveToUnit(Light,Enemy,lessHealthy) attack(Ranged,closest)) for(u) (moveToUnit(Heavy,Enemy,lessHealthy,u) moveToUnit(Worker,Ally,mostHealthy,u) harvest(1,u) harvest(3,u) moveToUnit(Ranged,Ally,weakest,u) build(Barrack,1,Down,u)";
        String script3f1 = "if(HaveUnitsToDistantToEnemy(Ranged,7)) (build(Base,1,Down) attack(Ranged,closest) build(Base,1,Down)) (moveaway(Light) moveToUnit(Light,Enemy,lessHealthy) build(Base,1,Up)) for(u) (train(Ranged,3,Down) harvest(3,u) if(HaveUnitsinEnemyRange(Heavy,u)) (moveOnceToCoord(Worker,9,12,9,u)) (build(Barrack,1,Down,u) moveToUnit(Heavy,Ally,lessHealthy,u) attack(Ranged,weakest,u))) for(u) (if(HaveQtdUnitsAttacking(Light,6)) (moveToCoord(Light,8,2,u) moveOnceToCoord(Worker,5,14,6,u)) (moveOnceToCoord(Ranged,6,14,13,u) harvest(1,u)) moveOnceToCoord(Ranged,2,14,0,u) moveOnceToCoord(Ranged,6,7,0,u) train(Worker,9,EnemyDir) if(HaveQtdEnemiesbyType(Heavy,8)) (moveOnceToCoord(Worker,6,6,12,u) train(Ranged,6,Down) train(Worker,1,EnemyDir)) build(Barrack,1,Right,u) moveOnceToCoord(Ranged,4,13,12,u) harvest(2,u) if(HaveQtdUnitsAttacking(Heavy,7)) (moveToCoord(Light,5,6,u) moveToCoord(Worker,2,5,u) moveOnceToCoord(Heavy,5,4,6,u)) (build(Base,1,Up,u) harvest(2,u)))";
        String script4f1 = "train(Worker,50,EnemyDir) harvest(2) if(HaveUnitsinEnemyRange(Worker)) (attack(Worker,closest)) if(HaveQtdUnitsbyType(Worker,15)) (train(Heavy,8,Left)) for(u) (moveOnceToCoord(Light,4,0,11,u) build(Base,1,Up,u) harvest(1,u) train(Light,7,Down) train(Worker,4,Down) moveToCoord(Heavy,14,2,u)) for(u) (train(Light,2,Left) train(Worker,2,Right) moveToCoord(Ranged,6,13,u) build(Base,1,Right,u)) for(u) (if(HaveEnemiesinUnitsRange(Heavy,u)) (attack(Worker,lessHealthy,u) moveaway(Ranged,u) train(Light,7,Left) moveaway(Ranged,u))) for(u) (if(HaveUnitsStrongest(Heavy,u)) (moveaway(Ranged,u)) if(HaveUnitsStrongest(Light,u)) (harvest(2,u) attack(Ranged,weakest,u)) if(HaveQtdUnitsAttacking(Light,5)) (moveOnceToCoord(Light,4,6,4,u))) moveToUnit(Worker,Enemy,lessHealthy) moveToCoord(Light,0,0)";
        String script5f1 = "train(Worker,50,EnemyDir) build(Barrack,1,Right) harvest(2) build(Base,1,Right) attack(Ranged,strongest) for(u) (attack(Ranged,farthest,u) moveToCoord(Light,14,2,u)) for(u) (attack(Ranged,closest,u) attack(Heavy,weakest,u) harvest(1,u) attack(Worker,closest,u) attack(Worker,weakest,u) attack(Heavy,lessHealthy,u) moveaway(Worker,u) attack(Ranged,farthest,u) attack(Light,strongest,u) attack(Worker,mostHealthy,u) moveaway(Ranged,u) moveOnceToCoord(Light,9,1,3,u) harvest(1,u))";
        String script6f1 = "for(u) (if(HaveQtdUnitsHarversting(7)) (build(Base,1,Left,u) moveToUnit(Heavy,Ally,farthest,u) moveaway(Heavy,u)) (train(Ranged,9,Left)) if(HaveQtdEnemiesbyType(Ranged,3)) (build(Barrack,1,Right,u))) if(IsPlayerInPosition(Left)) (build(Barrack,1,Right)) for(u) (if(HaveQtdEnemiesbyType(Worker,8)) (harvest(3,u)) (moveaway(Worker,u) build(Barrack,1,Right,u) harvest(2,u)) moveaway(Light,u) harvest(2,u) train(Worker,6,Left) attack(Light,lessHealthy,u) moveToUnit(Heavy,Enemy,weakest,u)) for(u) (if(HaveQtdUnitsAttacking(Ranged,8)) (attack(Heavy,strongest,u)) attack(Ranged,closest,u) attack(Ranged,strongest,u) moveToUnit(Ranged,Enemy,closest,u) build(Base,1,Down,u) moveOnceToCoord(Ranged,1,8,1,u) moveOnceToCoord(Ranged,1,5,6,u) moveaway(Light,u)) for(u) (harvest(3,u)) moveOnceToCoord(Worker,3,4,12) if(HaveUnitsinEnemyRange(Light)) (moveToCoord(Light,0,0) moveToCoord(Ranged,0,0)) (attack(Worker,weakest) moveaway(Light))";
        String script7f1 = "harvest(3) attack(Heavy,closest) for(u) (train(Light,8,EnemyDir) attack(Light,closest,u) if(HaveQtdEnemiesbyType(Heavy,5)) (moveaway(Light,u) harvest(3,u))) for(u) (if(HaveQtdUnitsAttacking(Ranged,2)) (moveOnceToCoord(Ranged,1,3,12,u)) (attack(Ranged,strongest,u) build(Barrack,1,Down,u) moveToUnit(Ranged,Ally,mostHealthy,u)) attack(Light,strongest,u) harvest(3,u) moveToUnit(Ranged,Enemy,lessHealthy,u)) attack(Ranged,mostHealthy) moveToUnit(Heavy,Ally,strongest)";
        String script8f1 = "train(Worker,50,EnemyDir) harvest(2) if(HaveUnitsinEnemyRange(Worker)) (attack(Worker,closest)) if(HaveQtdUnitsbyType(Worker,15)) (attack(Worker,strongest) moveToUnit(Light,Enemy,weakest) train(Heavy,3,Down) harvest(2)) if(IsPlayerInPosition(Up)) (moveToCoord(Worker,5,6) moveToCoord(Worker,18,6)) if(IsPlayerInPosition(Down)) (moveToCoord(Worker,18,15) harvest(1)) if(HaveQtdUnitsbyType(Worker,10)) (harvest(10))";

        String script1f2 = "train(Worker,50,EnemyDir) harvest(2) if(HaveUnitsStrongest(Worker)) (harvest(1) attack(Light,weakest)) (harvest(3)) for(u) (if(HaveUnitsStrongest(Ranged,u)) (harvest(1,u) moveaway(Light,u)) train(Light,5,Up) train(Heavy,5,EnemyDir) attack(Worker,closest,u) moveToUnit(Ranged,Ally,lessHealthy,u) moveOnceToCoord(Heavy,7,4,4,u) if(HaveEnemiesinUnitsRange(Ranged,u)) (attack(Ranged,closest,u) moveToUnit(Worker,Ally,closest,u) moveaway(Light,u) train(Ranged,5,EnemyDir)) (train(Light,8,Down)) build(Base,1,Left,u) train(Worker,3,Down) moveaway(Light,u) attack(Worker,closest,u) attack(Heavy,lessHealthy,u) if(HaveQtdUnitsHarversting(3)) (attack(Light,weakest,u) moveToUnit(Worker,Enemy,lessHealthy,u)))";
        String script2f2 = "harvest(3) moveaway(Worker) moveOnceToCoord(Heavy,4,0,13) train(Ranged,3,Up) train(Light,5,Down) attack(Heavy,mostHealthy) build(Barrack,1,Left) moveToCoord(Light,0,0) moveToCoord(Heavy,0,0) moveOnceToCoord(Heavy,9,5,4) if(HaveUnitsinEnemyRange(Heavy)) (moveToCoord(Worker,0,0) build(Base,1,Right) moveToCoord(Ranged,0,0)) (moveToUnit(Light,Enemy,lessHealthy) attack(Ranged,closest)) for(u) (if(HaveEnemiesinUnitsRange(Ranged,u)) (moveToUnit(Heavy,Enemy,closest,u) build(Base,1,Down,u)) (harvest(3,u)) moveToCoord(Light,2,13,u) harvest(1,u) moveOnceToCoord(Ranged,2,12,3,u) if(HaveUnitsinEnemyRange(Light,u)) (moveaway(Light,u) moveToUnit(Ranged,Ally,lessHealthy,u)) (harvest(3,u) build(Base,1,Up,u))) if(HaveUnitsStrongest(Worker)) (moveToCoord(Light,0,0) moveaway(Heavy)) (moveaway(Worker) moveToUnit(Ranged,Ally,lessHealthy) train(Light,2,Up) train(Ranged,8,Right) build(Base,1,Up))";
        String script3f2 = "if(HaveUnitsToDistantToEnemy(Ranged,7)) (build(Barrack,1,Right) attack(Ranged,closest) build(Base,1,Down)) (moveaway(Light) moveToUnit(Light,Enemy,lessHealthy) build(Base,1,Up)) for(u) (train(Ranged,3,Down) harvest(3,u) if(HaveUnitsinEnemyRange(Heavy,u)) (moveOnceToCoord(Worker,9,12,9,u)) (build(Barrack,1,Down,u) moveToUnit(Heavy,Ally,lessHealthy,u) attack(Ranged,weakest,u))) for(u) (if(HaveQtdUnitsAttacking(Light,6)) (moveaway(Heavy,u) moveToUnit(Light,Enemy,weakest,u)) (moveOnceToCoord(Ranged,6,14,6,u) harvest(1,u)) moveOnceToCoord(Ranged,2,14,0,u) moveOnceToCoord(Ranged,6,7,0,u) train(Worker,9,EnemyDir) if(HaveQtdEnemiesbyType(Heavy,8)) (moveOnceToCoord(Worker,6,6,12,u) train(Ranged,6,Down) train(Worker,1,EnemyDir)) build(Barrack,1,Right,u) moveOnceToCoord(Ranged,4,13,12,u) harvest(2,u) if(HaveQtdUnitsAttacking(Heavy,7)) (attack(Worker,closest,u) moveToCoord(Worker,2,5,u) moveOnceToCoord(Heavy,5,4,6,u)) (moveaway(Ranged,u) attack(Heavy,lessHealthy,u) moveToUnit(Worker,Enemy,mostHealthy,u) attack(Worker,closest,u) build(Base,1,Right,u)))";
        String script4f2 = "train(Worker,50,EnemyDir) harvest(2) for(u) (train(Light,8,Up) moveaway(Heavy,u) if(HaveQtdUnitsbyType(Light,4)) (moveaway(Ranged,u)) if(HaveEnemiesStrongest(Light,u)) (harvest(2,u) moveToCoord(Ranged,6,5,u) train(Light,7,Left)) (harvest(1,u) moveaway(Worker,u))) if(HaveQtdUnitsHarversting(4)) (harvest(2) moveaway(Worker)) for(u) (if(HaveEnemiesinUnitsRange(Heavy,u)) (moveOnceToCoord(Worker,4,12,8,u) attack(Heavy,closest,u) harvest(2,u) moveToCoord(Heavy,2,14,u)) (moveOnceToCoord(Heavy,9,12,0,u) attack(Worker,closest,u) harvest(3,u) train(Ranged,5,Up)) if(HaveUnitsinEnemyRange(Ranged,u)) (build(Base,1,Up,u) moveToCoord(Worker,5,2,u) attack(Ranged,closest,u) attack(Heavy,strongest,u)) (moveToUnit(Light,Enemy,farthest,u) moveOnceToCoord(Heavy,7,8,6,u) train(Heavy,1,Right)) moveToUnit(Light,Ally,farthest,u) moveToCoord(Heavy,12,13,u)) for(u) (if(HaveQtdUnitsAttacking(Ranged,6)) (train(Worker,2,Left) moveaway(Ranged,u)) if(HaveEnemiesinUnitsRange(Ranged,u)) (moveToCoord(Heavy,6,12,u) moveToUnit(Light,Enemy,mostHealthy,u) moveOnceToCoord(Light,2,4,5,u))) if(HaveEnemiesStrongest(Light)) (harvest(3)) (attack(Ranged,closest) moveOnceToCoord(Worker,1,7,8))";
        String script5f2 = "train(Worker,50,EnemyDir) build(Barrack,1,Right) harvest(2) build(Base,1,Right) attack(Ranged,strongest) for(u) (harvest(1,u) harvest(2,u) attack(Worker,closest,u) build(Base,1,Right,u) if(HaveQtdUnitsHarversting(3)) (harvest(1,u)) (moveaway(Worker,u) moveToCoord(Worker,4,13,u)) train(Light,8,Right) build(Barrack,1,Up,u)) moveOnceToCoord(Worker,2,1,1) attack(Light,closest) for(u) (if(HaveQtdUnitsAttacking(Ranged,5)) (harvest(3,u) moveOnceToCoord(Light,4,3,5,u)) (moveOnceToCoord(Worker,5,7,6,u) train(Heavy,7,EnemyDir)) if(HaveUnitsStrongest(Light,u)) (moveaway(Ranged,u) moveToCoord(Worker,12,10,u) moveToUnit(Heavy,Ally,lessHealthy,u)))";
        String script6f2 = "for(u) (if(HaveQtdUnitsHarversting(7)) (build(Base,1,Left,u) moveToCoord(Light,13,13,u) moveOnceToCoord(Ranged,3,9,4,u) moveToUnit(Heavy,Enemy,mostHealthy,u)) (train(Ranged,9,Left)) if(HaveQtdEnemiesbyType(Ranged,3)) (build(Barrack,1,Right,u))) if(HaveQtdUnitsHarversting(3)) (build(Barrack,1,Right)) for(u) (if(HaveQtdEnemiesbyType(Worker,8)) (harvest(3,u)) (harvest(2,u) moveToUnit(Heavy,Ally,lessHealthy,u) train(Ranged,7,EnemyDir)) moveaway(Light,u) harvest(2,u) train(Worker,6,Left) attack(Light,lessHealthy,u) moveToUnit(Heavy,Enemy,weakest,u)) for(u) (if(HaveQtdUnitsAttacking(Heavy,8)) (moveOnceToCoord(Heavy,5,14,1,u) attack(Light,farthest,u)) attack(Ranged,closest,u) attack(Ranged,strongest,u) moveToUnit(Ranged,Enemy,closest,u) build(Base,1,Down,u) if(HaveEnemiesinUnitsRange(Ranged,u)) (train(Heavy,1,Left)) (moveaway(Ranged,u) moveToCoord(Ranged,4,11,u))) for(u) (if(HaveQtdEnemiesbyType(Ranged,6)) (moveaway(Ranged,u) moveToUnit(Heavy,Ally,weakest,u) moveaway(Ranged,u)) (moveToUnit(Worker,Ally,weakest,u) train(Worker,6,EnemyDir) harvest(3,u)) if(HaveEnemiesinUnitsRange(Worker,u)) (moveaway(Heavy,u) attack(Light,closest,u) harvest(1,u)) (moveToCoord(Worker,0,12,u))) for(u) (if(HaveQtdUnitsHarversting(7)) (build(Base,1,Down,u) moveOnceToCoord(Ranged,3,9,2,u)) moveToUnit(Worker,Ally,mostHealthy,u) if(HaveQtdEnemiesbyType(Light,9)) (build(Base,1,Left,u))) build(Barrack,1,Left) moveToUnit(Worker,Enemy,lessHealthy) train(Light,1,Up)";
        String script7f2 = "harvest(3) attack(Heavy,closest) for(u) (train(Light,8,EnemyDir) attack(Light,closest,u) if(HaveQtdEnemiesbyType(Heavy,2)) (moveToCoord(Worker,11,6,u)) (moveaway(Light,u))) for(u) (if(HaveQtdUnitsAttacking(Ranged,7)) (build(Barrack,1,Left,u) moveOnceToCoord(Ranged,9,0,2,u) moveOnceToCoord(Light,3,3,13,u) moveOnceToCoord(Heavy,3,4,0,u)) (build(Barrack,1,Up,u) build(Barrack,1,Down,u) attack(Worker,strongest,u)) if(HaveQtdUnitsAttacking(Worker,7)) (harvest(1,u) attack(Worker,farthest,u) moveToCoord(Worker,10,9,u)) moveToCoord(Light,6,4,u) attack(Heavy,farthest,u) moveToCoord(Light,10,13,u)) build(Base,1,Up) train(Worker,3,Down) build(Base,1,Right) harvest(1) harvest(1) moveOnceToCoord(Worker,9,0,6) moveOnceToCoord(Heavy,4,11,10) harvest(3) for(u) (train(Worker,5,Up) moveToCoord(Worker,11,8,u) harvest(3,u) attack(Heavy,weakest,u) if(HaveUnitsinEnemyRange(Light,u)) (attack(Worker,farthest,u) build(Barrack,1,Up,u) attack(Ranged,farthest,u)) (build(Base,1,Left,u) moveToUnit(Heavy,Enemy,mostHealthy,u) train(Light,3,Left) moveToUnit(Heavy,Ally,weakest,u))) moveToUnit(Light,Enemy,weakest)";
        String script8f2 = "train(Worker,50,EnemyDir) harvest(2) if(HaveUnitsinEnemyRange(Worker)) (attack(Worker,closest)) if(HaveQtdUnitsbyType(Worker,15)) (attack(Worker,strongest) moveToUnit(Light,Enemy,weakest) train(Heavy,3,Down) harvest(2)) moveToUnit(Heavy,Ally,lessHealthy) train(Ranged,2,Down) harvest(2) for(u) (if(HaveQtdUnitsAttacking(Worker,5)) (train(Light,3,Down)) (moveaway(Worker,u) moveaway(Heavy,u) train(Ranged,7,Right))) moveToUnit(Worker,Enemy,closest) harvest(1) harvest(3) if(HaveUnitsStrongest(Light)) (train(Ranged,3,Left))";
        //bla
        /*
        String script1f1 = "";
        String script2f1 = "";
        String script3f1 = "";
        String script4f1 = "";
        String script5f1 = "";
        String script6f1 = "";
        String script7f1 = "";
        String script8f1 = "";
         */
        /*
        String script1f2 = "";
        String script2f2 = "";
        String script3f2 = "";
        String script4f2 = "";
        String script5f2 = "";
        String script6f2 = "";
        String script7f2 = "";
        String script8f2 = "";
         */
        
        // Avaliações da Fase 1
                          
        executaBatalhas("Inicial", scriptInicial, "Script 1 (F1)", script1f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 2 (F1)", script2f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 3 (F1)", script3f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 4 (F1)", script4f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 5 (F1)", script5f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 6 (F1)", script6f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 7 (F1)", script7f1);
        //executaBatalhas("Inicial", scriptInicial, "Script 8 (F1)", script8f1);
        
        
        // Avaliações da Fase 2 - 1
        /*
        executaBatalhas("Inicial", scriptInicial, "Script 1 (F2)", script1f2);
        executaBatalhas("Inicial", scriptInicial, "Script 2 (F2)", script2f2);
        executaBatalhas("Inicial", scriptInicial, "Script 3 (F2)", script3f2);
        executaBatalhas("Inicial", scriptInicial, "Script 4 (F2)", script4f2);
        executaBatalhas("Inicial", scriptInicial, "Script 5 (F2)", script5f2);
        executaBatalhas("Inicial", scriptInicial, "Script 6 (F2)", script6f2);
        executaBatalhas("Inicial", scriptInicial, "Script 7 (F2)", script7f2);
        executaBatalhas("Inicial", scriptInicial, "Script 8 (F2)", script8f2);
        */
        // Avaliações da Fase 2 - 2
        /*
        executaBatalhas("Script 1 (F1)", script1f1, "Script 1 (F2)", script1f2);
        executaBatalhas("Script 2 (F1)", script2f1, "Script 2 (F2)", script2f2);
        executaBatalhas("Script 3 (F1)", script3f1, "Script 3 (F2)", script3f2);
        executaBatalhas("Script 4 (F1)", script4f1, "Script 4 (F2)", script4f2);
        executaBatalhas("Script 5 (F1)", script5f1, "Script 5 (F2)", script5f2);
        executaBatalhas("Script 6 (F1)", script6f1, "Script 6 (F2)", script6f2);
        executaBatalhas("Script 7 (F1)", script7f1, "Script 7 (F2)", script7f2);
        executaBatalhas("Script 8 (F1)", script8f1, "Script 8 (F2)", script8f2);
        */
        
        System.out.println("======= FIM! =======");
        
    }
    
    public static void executaBatalhas(String jogador1, String script1, String jogador2, String script2) {
    	UnitTypeTable utt = new UnitTypeTable();
    	String map = "maps/DoubleGame24x24.xml";
        List<AI> leagueAIs;
        String path = System.getProperty("user.dir").concat("/logs/");
    	
        System.out.println("------- Nova partida: " + jogador1 + " vs " + jogador2);
        float pontuacao1 = 0;
        float pontuacao2 = 0;
        float pontuacao1_dir = 0;
        float pontuacao2_dir = 0;
        float pontuacao1_esq = 0;
        float pontuacao2_esq = 0;
    	
    	botEmptyBase s1 = new botEmptyBase(utt, script1, jogador1);
        botEmptyBase s2 = new botEmptyBase(utt, script2, jogador2);
        
        leagueAIs = new ArrayList(Arrays.asList(new AI[]{ s1, s2 }));

        List<AI> ais;
        ais = new ArrayList(Arrays.asList(new AI[]{ 
        //new WorkerRush(utt)//,
        //new RangedRush(utt),
        //new LightRush(utt)
        //new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 4), //lr
        //new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 4), //lr
        //new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 5), //HR
        //new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 6), //RR
        //new BasicExpandedConfigurableScript(utt, new AStarPathFinding(), 18, 0, 0, 1, 2, 2, -1, -1, 3), //WR 
        //new NaiveMCTS(utt), //
        //new PuppetSearchMCTS(utt),
        //new StrategyTactics(utt),//, 
        //new A3NNoWait(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
        //    new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
        //    decodeScripts(utt, "0;"), "A3N")
        }));

        for (int i = 0; i < QTD_ROUND_ROBIN; i++) {
            //themselves            
            for (int j = 0; j < leagueAIs.size(); j++) {
                for (int k = 0; k < leagueAIs.size(); k++) {
                    if (j != k) {
                        AI ai1 = leagueAIs.get(j);
                        AI aiEnemy = leagueAIs.get(k);
                        //System.out.println(i + " Running battles themselves " + ai1 + " vs " + aiEnemy); 
                        
                        ThreadBattleLog  battle = new ThreadBattleLog(ai1, aiEnemy, map, String.valueOf(i),  // jogador da esquerda, jogador da direita  
                                path, 300 + j, 200 + k, uniqueID, utt);
                        battle.run();
                        // Log: vencedor
                        //if(battle.getWinner() != null)
                        	//System.out.print("- - - Vencedor da batalha: " + battle.getWinner()); 
                        //else 
                        	//System.out.println("- - - Empate!");
                        //if(battle.getWinnerSide() == 'e') System.out.println(" à esquerda.");
                        //else if(battle.getWinnerSide() == 'd') System.out.println(" à direita.");
                        
                        // Atribuição de pontuações
                        if(battle.getWinner() == s1) {
                        	pontuacao1 += 1;
                        	if(battle.getWinnerSide() == 'e') pontuacao1_esq += 1;
                        	else if(battle.getWinnerSide() == 'd') pontuacao1_dir += 1;
                        }
                        else if(battle.getWinner() == s2) {
                        	pontuacao2 += 1;
                        	if(battle.getWinnerSide() == 'e') pontuacao2_esq += 1;
                        	else if(battle.getWinnerSide() == 'd') pontuacao2_dir += 1;
                        }
                        else { 
                        	pontuacao2 += 0.5; 
                        	pontuacao1 += 0.5; 
                        	if(battle.getAI1() == s1) {
                        		pontuacao1_esq += 0.5; 
                        		pontuacao2_dir += 0.5; 
                        	} else if(battle.getAI1() == s2) {
                        		pontuacao1_dir += 0.5; 
                            	pontuacao2_esq += 0.5;
                        	}
                        }
                        //System.out.println("- Placar atual: (" + jogador1 + ") " + pontuacao1 + " x " + pontuacao2 + " (" + jogador2 + ")");
                        
                        battle = new ThreadBattleLog(aiEnemy, ai1, map, String.valueOf(i), path, 200 + k,
                                300 + j, uniqueID, utt);
                        battle.run();
                        
                        // Lob: vencedor  
                        //if(battle.getWinner() != null)
                        //	System.out.print("- - - Vencedor da batalha: " + battle.getWinner()); 
                        //else 
                        //	System.out.println("- - - Empate!");
                        //if(battle.getWinnerSide() == 'e') System.out.println(" à esquerda.");
                        //else if(battle.getWinnerSide() == 'd') System.out.println(" à direita.");
                        
                        // Atribuição de pontuações
                        if(battle.getWinner() == s1) {
                        	pontuacao1 += 1;
                        	if(battle.getWinnerSide() == 'e') pontuacao1_esq += 1;
                        	else if(battle.getWinnerSide() == 'd') pontuacao1_dir += 1;
                        }
                        else if(battle.getWinner() == s2) {
                        	pontuacao2 += 1;
                        	if(battle.getWinnerSide() == 'e') pontuacao2_esq += 1;
                        	else if(battle.getWinnerSide() == 'd') pontuacao2_dir += 1;
                        }
                        else { 
                        	pontuacao2 += 0.5; 
                        	pontuacao1 += 0.5; 
                        	if(battle.getAI1() == s1) {
                        		pontuacao1_esq += 0.5; 
                        		pontuacao2_dir += 0.5; 
                        	} else if(battle.getAI1() == s2) {
                        		pontuacao1_dir += 0.5; 
                            	pontuacao2_esq += 0.5;
                        	}
                        }
                        //System.out.println("- Placar atual: (" + jogador1 + ") " + pontuacao1 + " x " + pontuacao2 + " (" + jogador2 + ")");
                    }
                }
            }

            //enemy battles            
            for (int j = 0; j < leagueAIs.size(); j++) {
                for (int y = 0; y < ais.size(); y++) {
                    AI ai1 = leagueAIs.get(j);
                    AI aiEnemy = ais.get(y);
                    System.out.println(i + " Running battles " + ai1 + " vs " + aiEnemy);
                    ThreadBattleLog battle = new ThreadBattleLog(ai1, aiEnemy, map, String.valueOf(i), path, j, 100 + y, uniqueID, utt);
                    battle.run();
                    battle = new ThreadBattleLog(aiEnemy, ai1, map, String.valueOf(i), path, 100 + y, j, uniqueID, utt);
                    battle.run();
                    System.gc();
                }
            }

        }
        
        // Resultados finais do teste
        System.out.println("====================================");
        System.out.println("- Placar final: (" + jogador1 + ") " + pontuacao1 + " x " + pontuacao2 + " (" + jogador2 + ")");
        System.out.println("Porcentagens de vitória:");
        System.out.println("    " + jogador1 + " = " + (pontuacao1*100)/(QTD_ROUND_ROBIN*4) + "%");
        System.out.println("    " + jogador2 + " = " + (pontuacao2*100)/(QTD_ROUND_ROBIN*4) + "%");
        System.out.println("Vitórias por lado:");
        System.out.println(jogador1 + " lado esquerdo = " + pontuacao1_esq + " " + (pontuacao1_esq*100)/(QTD_ROUND_ROBIN*2) + "%"); 
        System.out.println(jogador2 + " lado direito = " + pontuacao2_dir + " " + (pontuacao2_dir*100)/(QTD_ROUND_ROBIN*2) + "%");
        System.out.println(jogador1 + " lado direito = " + pontuacao1_dir + " " + (pontuacao1_dir*100)/(QTD_ROUND_ROBIN*2) + "%");
        System.out.println(jogador2 + " lado esquerdo = " + pontuacao2_esq + " " + (pontuacao2_esq*100)/(QTD_ROUND_ROBIN*2) + "%");
        

    }
    
    
}
