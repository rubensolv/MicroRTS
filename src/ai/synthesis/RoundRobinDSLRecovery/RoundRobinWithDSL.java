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
public class RoundRobinWithDSL {

    private static final int QTD_ROUND_ROBIN = 1;
    private static final String uniqueID = UUID.randomUUID().toString();

    public static void main(String[] args) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        // path where results will be saved
        String pathLog = System.getProperty("user.dir").concat("/logs/");

        //map settings
        String map = "maps/battleMaps/Others/RangedHeavyMixed.xml";
        //String map = "maps/8x8/basesWorkers8x8A.xml";
        //String map = "maps/16x16/basesWorkers16x16A.xml";
        
        List<AI> astAIs = new ArrayList<>();
        //path to recovery the dsl's serialized. 
        //SP path
        String pathDSLs = System.getProperty("user.dir").concat("/logs2/SP/");
        check_if_not_build(pathDSLs);
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_SP");        

        //list of complement agents.
        List<AI> bot_ais;
        bot_ais = new ArrayList(Arrays.asList(new AI[]{
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
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);

        // run all battles
        run_battles(all_ais, map, utt, pathLog);
        System.out.println("Finished SP"+ new java.util.Date());
        
        //FPv1 path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FP/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FP");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FP1"+ new java.util.Date());
        
        
        //FPv3 path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv3/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv3");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FPv3"+ new java.util.Date());
        
        //FPv3 unstoppable path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv3un/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv3un");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FPv3un"+ new java.util.Date());
        
        //FPv3 unstoppable and continuous path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv3uncon/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv3uncon");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FPv3uncon"+ new java.util.Date());
        
        //FPv4 path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv4/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv4");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FPv4"+ new java.util.Date());
        
        //FPv4 unstoppable path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv4un/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv4un");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FPv4un"+ new java.util.Date());
        
        //FPv4 unstoppable and continuous path
        pathDSLs = System.getProperty("user.dir").concat("/logs2/FPv4uncon/");
        astAIs.clear();
        read_serial_dsl(astAIs, pathDSLs, utt, "ast_FPv4uncon");
        
        all_ais.clear();
        all_ais.addAll(astAIs);
        all_ais.addAll(bot_ais);                
        run_battles(all_ais, map, utt, pathLog);        
        System.out.println("Finished FPv4uncon"+ new java.util.Date());
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
                        Pair<AI, AI> p = new Pair(ai1,ai2);
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
            Logger.getLogger(RoundRobinWithDSL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void check_if_not_build(String pathDSLs) {
        File f = new File(pathDSLs);
        if(!f.exists()){
            f.mkdir();
        }
    }

}
