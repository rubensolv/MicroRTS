/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.twophasessa;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.grammar.dslTree.*;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iNodeDSLTree;
import ai.synthesis.grammar.dslTree.utils.SerializableController;
import ai.synthesis.localsearch.searchImplementation.CumulativeSAComposed;
import ai.synthesis.localsearch.searchImplementation.DetailedSearchResult;
import ai.synthesis.localsearch.searchImplementation.SAForFPTableV3init;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ai.synthesis.twophasessa.TradutorDSL;


public class RunSASPFP {

    public static void main(String[] args) {

        String file = "ScrM";
        String path = System.getProperty("user.dir").concat("/logs2/");
        String uniqueID = UUID.randomUUID().toString();

        List<iDSL> cum_ind = new ArrayList<>();
        CumulativeSAComposed search = new CumulativeSAComposed();

        SettingsAlphaDSL.setMode_debug(false);
        if (SettingsAlphaDSL.isMode_debug()) {
            System.err.println("---------MODE DEBUG ACTIVATED!---------");
        } else {
            System.err.println("---------MODE OFFICIAL ACTIVATED!---------");
        }

        System.out.println("unique ID: " + uniqueID);
        System.err.println("Map " + SettingsAlphaDSL.get_map());
        //System.err.println("File to be exploited = " + file);
        //iDSL ScrI = SerializableController.recoverySerializable(file, path);
        TradutorDSL tScrTeste = new TradutorDSL("harvest(1) train(Worker,50,EnemyDir) if(HaveUnitsToDistantToEnemy(Worker,3)) then(attack(Worker,closest))");
        iDSL ScrI = tScrTeste.getAST();

         // Script do mapa 8x8
        //EmptyDSL empty = new EmptyDSL();
        // comandos
        //CommandDSL c1 = new CommandDSL("attack(Worker,closest)");
        //CommandDSL c2 = new CommandDSL("train(Worker,5,EnemyDir)");
        //Cs
        //CDSL C1 = new CDSL(c1);
        //CDSL C2 = new CDSL(c2);
        //S1
        //S1DSL S1 = new S1DSL(C2, new S1DSL(empty));
        // Raiz
        //iDSL ScrI = new S1DSL(C1, C2);
        //System.out.println("TESTE ScrI: " + ScrI.translate());
        
        //BuilderDSLTreeSingleton builder = BuilderDSLTreeSingleton.getInstance();
        //builder.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) ScrI);
        
        //builder.formatedStructuredDSLTreePreOrderPrint((iNodeDSLTree) ScrTeste);


        // Teste da montagem da DSL
        String intencao = "harvest(1) train(Worker,50,EnemyDir) if(HaveUnitsToDistantToEnemy(Worker,3)) then(attack(Worker,closest))";
        System.out.println("Intenção: ");
        System.out.println(intencao);
        System.out.println("DSL montada: ");
        System.out.println(ScrI.translate());
        if (intencao.equals(ScrI.translate())) {
            System.out.println("DSL montada corretamente :D");
        } else {
            System.out.println("DSL incorreta :(");
        }

        // SA (self play)
        System.out.println("\n ---------- S.A. (self play) ---------- \n");
        System.err.println("Script exploited = " + ScrI.translate());

        DetailedSearchResult results;
        cum_ind.add((iDSL) ScrI.clone());

        results = search.run(cum_ind, (iDSL) ScrI.clone(),
                SettingsAlphaDSL.get_number_sa_steps(), 2, ScrI);

        System.out.println("\n#######");
        System.out.println("Evaluation with score: " + results.getWinner() + " pos S.A.:");
        System.out.println("Script 1 (base): " + results.getsBase().translate());
        System.out.println("Script 2 (winner): " + results.getsWinner().translate());
        System.out.println("#######\n");
        System.out.println("=========================== fim da iteração 0 ===========================\n");

        // Salva arquivo com o vencedor da primeira iteração (iteração 0)
        SerializableController.saveSerializable(results.getsWinner(), uniqueID + "_" + file + "_id_" + 0 + ".ser", path);

        for (int i = 1; i < SettingsAlphaDSL.get_number_alphaDSL_iterations(); i++) {
            // Encerra as iterações caso a pontuação máxima já tenha sido encontrada
            if (results.getWinner() == 4.0f) {
                System.out.println("Best score reached.");
                SerializableController.saveSerializable(results.getsWinner(), uniqueID + "_" + "ScrM" + "dsl_exploit_" + "_id_" + i + ".ser", path);
                break;
            }

            //Avaliações e busca do S.A.
            results = search.run(cum_ind, results.getsWinner(),
                    SettingsAlphaDSL.get_number_sa_steps(), 2, ScrI);

            System.out.println("\n#######");
            System.out.println("Evaluation with score: " + results.getWinner() + " iteration " + i + " pos S.A.:");
            System.out.println("Script 1 (base): " + results.getsBase().translate());
            System.out.println("Script 2 (winner): " + results.getsWinner().translate());
            System.out.println("#######\n");

            // Salva arquivo com o vencedor da iteração atual (iteração i)
            SerializableController.saveSerializable(results.getsWinner(), uniqueID + "_" + file + "_id_" + i + ".ser", path);
            // Atualiza melhor script para a próxima fase

            System.out.println("=========================== fim da iteração " + i + " ===========================\n");
        }

        // SA (fictitious play)
        System.out.println("\n ---------- S.A. (fictitious play) ---------- \n");

        // Script inicial -> ScrI
        // Script melhor resposta (da 1ª fase): ScrM
        // Script equilíbrio Nash (resposta da 2ª fase): ScrME
        iDSL ScrM = results.getsWinner();
        System.out.println("ScrI: " + ScrI.translate());
        System.out.println("ScrM: " + ScrM.translate());

        List<iDSL> initial_group = new ArrayList<>();
        initial_group.add(ScrI);
        initial_group.add(ScrM);

        // v1
        //CumulativeBrownie skSAneal = new CumulativeBrownie(search);
        //skSAneal.performRun(ScrI, ScrM);
        // v3
        //LocalSearch skSAneal = new SimpleProgramSynthesisForFPTableV3Tathy(new SAForFPTableV3(), initial_group);
        //skSAneal.performRun();
        // v3.1
        SimpleProgramSynthesisForFPTableV3initGroup skSAneal = new SimpleProgramSynthesisForFPTableV3initGroup(new SAForFPTableV3init(), initial_group);
        skSAneal.performRun(ScrI, ScrM);

    }

}
