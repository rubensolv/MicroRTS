/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.runners.roundRobinLocal;

import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.FunctionGPCompiler;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import gui.PhysicalGameStatePanel;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

public class RoundRobinGrammarAgainstGrammar {

    ICompiler compiler = new MainGPCompiler();
    HashSet<String> usedCommands;
    AI ai;

    /**
     *
     * @param sIA1 - grammar of the script 1
     * @param sIA2 - grammar of the script 2
     * @return 0 if sIA1 won, 1 if if sIA2 won and if there is draw, the base score of 
     * LTD3 will be used to define the winner.
     * @throws Exception
     */
    public float execute(String sIA1, String sIA2) throws Exception {
        ArrayList<String> log = new ArrayList<>();        

        String map = "maps/8x8/basesWorkers8x8A.xml";
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(map, utt);
        
        printMatchDetails(sIA1,sIA2,map);
        
        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 4000;
        }
        if (pgs.getHeight() == 16) {
            MAXCYCLES = 5000;
            //MAXCYCLES = 1000;
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

        AI ai1 = buildCommandsIA(utt, sIA1);
        AI ai2 = buildCommandsIA(utt, sIA2);

        JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 840, 840, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);        
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
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
                    System.err.println("ai.synthesis.runners.roundRobinLocal.RoundRobinGrammarAgainstGrammar.run() " + e.getMessage());
                }
            }

        } while (!gameover && (gs.getTime() <= MAXCYCLES));
        
        int winner = gs.winner();
        if (winner != -1) {
            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
            System.out.println("Winner "+winner);
            return winner;
        }else{
            SimpleSqrtEvaluationFunction3 ltd3 = new SimpleSqrtEvaluationFunction3();            
            float scoreIA1 = ltd3.base_score(0, gs);
            float scoreIA2 = ltd3.base_score(1, gs);
            if (scoreIA1 > scoreIA2) {
                w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
                System.out.println("Winner 0");
                return 0;
            } else {
                w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
                System.out.println("Winner 1");
                return 1;
            }
        }
        
    }

    private AI buildCommandsIA(UnitTypeTable utt, String code) {
        HashMap<Long, String> counterByFunction = new HashMap<>();
        usedCommands = new HashSet<>();
        FunctionGPCompiler.counterCommands = 0;
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI script = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands, counterByFunction);
        return script;
    }

    private void printMatchDetails(String sIA1, String sIA2, String map) {
        System.out.println("Script 1 = "+sIA1);
        System.out.println("Script 2 = "+sIA2);
        System.out.println("Map = "+map);
    }

    

}
