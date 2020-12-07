/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.runners.reportEvaluation;

import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.FunctionGPCompiler;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.ahtn.AHTNAI;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import gui.PhysicalGameStatePanel;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

public class reportRoundRobinEvaluation extends Thread {

    final int QTD_MATCHES = 3;
    final int QTD_IAS = 3;
    ICompiler compiler = new MainGPCompiler();
    HashSet<String> usedCommands;
    AI ai;
    String sIA1;

    float matchesResults;

    public void setIA(String sIA1) {
        this.sIA1 = sIA1;
    }

    public float getMatchesResults() {
        return matchesResults;
    }

    public String getsIA1() {
        return sIA1;
    }

    public int exec(boolean change_side, int idIA) throws Exception {
        //String map = "maps/24x24/basesWorkers24x24A.xml";
        String map = SettingsAlphaDSL.get_map();
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(map, utt);

        //printMatchDetails(sIA1,sIA2,map);
        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 20000;
        int PERIOD = 20;
        boolean gameover = false;

        if (pgs.getHeight() == 8) {
            MAXCYCLES = 3000;
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
        List<AI> ais = new ArrayList(Arrays.asList(new AIWithComputationBudget[]{
            new WorkerRush(utt),
            new RangedRush(utt),
            new LightRush(utt)
        }));
        AI ai1;
        AI ai2;
        if (change_side) {
            ai1 = ais.get(idIA);
            ai2 = buildCommandsIA(utt, sIA1);
        } else {
            ai1 = buildCommandsIA(utt, sIA1);
            ai2 = ais.get(idIA);
        }

        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 640, 640, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);                
        do {            
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                // simulate:
                gameover = gs.cycle();
                //w.repaint();            

        } while (!gameover && (gs.getTime() <= MAXCYCLES));

        //w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        return gs.winner();
    }

    private AI buildCommandsIA(UnitTypeTable utt, String code) {
        HashMap<Long, String> counterByFunction = new HashMap<>();
        usedCommands = new HashSet<>();
        FunctionGPCompiler.counterCommands = 0;
        List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI script = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands, counterByFunction);
        return script;
    }

    @Override
    public void run() {
        matchesResults = 0.0f;
        int result;
        for (int i = 0; i < QTD_MATCHES; i++) {
            for (int j = 0; j < QTD_IAS; j++) {
                try {
                    //0 == win                
                    result = exec(false,j);
                    if (result == 0) {
                        matchesResults += 1.0f;
                    } else if (result == -1) {
                        matchesResults += 0.5f;
                    }
                    //1 == win
                    result = exec(true,j);
                    if (result == 1) {
                        matchesResults += 1.0f;
                    } else if (result == -1) {
                        matchesResults += 0.5f;
                    }

                } catch (Exception ex) {
                    Logger.getLogger(reportRoundRobinEvaluation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
