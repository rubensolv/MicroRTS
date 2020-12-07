/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.RoundRobinDSLRecovery;

import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.competition.newBotsEval.botEmptyBase;
import ai.core.AI;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.puppet.PuppetSearchMCTS;
import ai.scv.SCVPlus;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.units.UnitTypeTable;
import util.Pair;

/**
 *
 * @author rubens
 */
public class RoundRobinWithDSLFull {

    private static final int QTD_ROUND_ROBIN = 5;
    private static final String uniqueID = UUID.randomUUID().toString();

    public static void main(String[] args) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        // path where results will be saved
        String pathLog = System.getProperty("user.dir").concat("/logs/");
        check_if_not_build(pathLog);
        //map settings
        String map = "maps/8x8/basesWorkers8x8A.xml";
        //String map = "maps/NoWhereToRun9x8.xml";
        //String map = "maps/battleMaps/Others/RangedHeavyMixed.xml";                        
        //String map = "maps/DoubleGame24x24.xml";           
        //String map = "maps/16x16/basesWorkers16x16A.xml";

        //list of complementary agents.
        List<AI> bot_ais;
        bot_ais = new ArrayList(Arrays.asList(new AI[]{
//            new botEmptyBase(utt, "attack(Worker,closest) train(Worker,5,EnemyDir)", "user1"),
//            new botEmptyBase(utt, "harvest(1) train(Worker,100,EnemyDir) attack(Worker,closest)", "user2"),
//            new botEmptyBase(utt, "harvest(1) train(Worker,50,EnemyDir) if(HaveUnitsToDistantToEnemy(All,3)) (attack(All,closest))", "user3")
        new WorkerRush(utt),
        new HeavyRush(utt),
        new RangedRush(utt),
        new LightRush(utt),
        new NaiveMCTS(utt), //
        new PuppetSearchMCTS(utt),
        new SCVPlus(utt)
        //new StrategyTactics(utt),//, 
        //new A3NNoWait(100, -1, 100, 8, 0.3F, 0.0F, 0.4F, 0, new RandomBiasedAI(utt),
        //    new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 3,
        //    decodeScripts(utt, "0;"), "A3N")
        }));
        List<AI> all_ais = new ArrayList<>();

        List<AI> astAIs = new ArrayList<>();
        //path to recovery the dsl's serialized. 
        //SP path
        String pathDSLs = System.getProperty("user.dir").concat("/logs2/SP/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_SP");

        //FPv1 path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FP/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FP");

        //FPv3 path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv3/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv3");

        //FPv3 unstoppable path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv3un/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv3un");

        //FPv3 unstoppable and continuous path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv3uncon/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv3uncon");

        //FPv4 path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv4/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv4");

        //FPv4 unstoppable path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv4un/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv4un");

        //FPv4 unstoppable and continuous path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv4uncon/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv4uncon");
        
        //FPv5
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv5/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv5");
        
        //FPv5 windows
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv5Wind/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv5Wind");
        
        //FPv5 windows + weight
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv5WindWeight/");
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv5WindWeight");

        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);
        System.out.println("Total of IA's =" + all_ais.size());
        run_battles(all_ais, map, utt, pathLog);
        System.out.println("Finished all battles " + new java.util.Date());
    }

    private static void read_serial_dsl(List<AI> leagueAIs, String pathDSLs, UnitTypeTable utt, String ai_prefix) {
        File file = new File(pathDSLs);
        for (String ast : file.list()) {
            if (ast.contains(".ser")) {
                iDSL rec = SerializableController.recoverySerializable(ast, pathDSLs);
                String ident = ast.replace(".ser", "").substring(ast.lastIndexOf("id_") + 3);
                leagueAIs.add(buildCommandsIA(utt, rec, ai_prefix + ident));
            }
        }
    }

    private static AI buildCommandsIA(UnitTypeTable utt, iDSL code, String nameAI) {
        IDSLCompiler compiler = new MainDSLCompiler();
        HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
        List<ICommand> commandsDSL = compiler.CompilerCode(code, utt);
        AI aiscript = new DslAI(utt, commandsDSL, nameAI, code, counterByFunction);
        return aiscript;
    }

    private static void run_battles(List<AI> all_ais, String map, UnitTypeTable utt, String pathLog) {
        List<Pair<AI, AI>> matches = new ArrayList<>();
        for (int i = 0; i < QTD_ROUND_ROBIN; i++) {
            for (AI ai1 : all_ais) {
                for (AI ai2 : all_ais) {
                    if (!ai1.equals(ai2)) {
                        Pair<AI, AI> p = new Pair(ai1, ai2);
                        matches.add(p);
                    }
                    if (matches.size() == 25) {
                        System.out.println("Batch send...");
                        run_in_threads(i, matches, map, utt, pathLog);
                        matches.clear();
                        System.gc();
                        System.out.println("Batch finished...");
                    }
                }
            }
            run_in_threads(i, matches, map, utt, pathLog);
            matches.clear();
        }

    }

    private static void run_in_threads(int i, List<Pair<AI, AI>> matches, String map, UnitTypeTable utt, String pathLog) {
        int qtdCore = Runtime.getRuntime().availableProcessors() - 1;
        ExecutorService pool = Executors.newFixedThreadPool(qtdCore);
        for (Pair<AI, AI> matche : matches) {
            AI ai1 = matche.m_a;
            AI ai2 = matche.m_b;
            ThreadBattleWithLog battleForward = new ThreadBattleWithLog(ai1, ai2, map, String.valueOf(i),
                    pathLog, ai1.toString(), ai2.toString(), uniqueID, utt);
            pool.execute(battleForward);
            ThreadBattleWithLog battleBackward = new ThreadBattleWithLog(ai2, ai1, map, String.valueOf(i),
                    pathLog, ai2.toString(), ai1.toString(), uniqueID, utt);
            pool.execute(battleBackward);
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            Logger.getLogger(RoundRobinWithDSLFull.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void check_if_not_build(String pathDSLs) {
        File f = new File(pathDSLs);
        if (!f.exists()) {
            f.mkdir();
        }
    }

}
