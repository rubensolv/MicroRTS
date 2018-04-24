/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.setCover;

import ai.asymmetric.GAB.PGSLimitScriptC;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.core.AI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class SetCover {

    public void teste() {

        UnitTypeTable utt = new UnitTypeTable();
        //faça um loop para selecionar as amostras de scripts condutores....
        Random rand = new Random();
        int scriptCondutor = rand.nextInt(300);

        //jogue o jogo com o script condutor....
        GameSimulationSetCover game = new GameSimulationSetCover();
        //enquanto joga o jogo vai salvando os tempos desejados == os estados!

        //10 estados em formato Json de exemplo...
        ArrayList<String> caminho = new ArrayList<>();
        caminho.add("10"); // adicionamos os 10 estados em formato Json
        for (String conteudo : caminho) {
            for (int i = 0; i < 300; i++) {
                AI ai = new PGSLimitScriptC(utt, decodeScripts(utt, String.valueOf(i).concat(";"))); // carregamos o script que desejamos simular
                GameState gsSimulator = GameState.fromJSON(conteudo, utt);
                //método que simule a ação e log a ação do script i para o estamo carregado....
                
                
                System.gc(); // forço o garbage para tentar liberar memoria....
            }

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
