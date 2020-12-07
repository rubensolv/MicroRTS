/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.runners.roundRobinLocal;


import ai.abstraction.LightRush;
import ai.abstraction.WorkerRush;
import ai.competition.newBotsEval.botEmptyBase;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
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

public class SmartRRIAxGRunnable extends Thread {

    IDSLCompiler compiler = new MainDSLCompiler();    
    AI ai;
    iDSL sIA1;    
    float result;
    int winner;
    int idIA;
    boolean changeSide;
    //Smart Evaluation Settings
    String initialState;
    private final int CYCLES_LIMIT = 200;
    List<ICommand> allCommand;

    public float getResult() {
        return result;
    }

    public int getWinner() {
        return winner;
    }

    public List<ICommand> getUsedCommands() {
        return allCommand;
    }

    public SmartRRIAxGRunnable(iDSL sIA1, int idIA, boolean side) {
        this.sIA1 = sIA1;        
        this.idIA = idIA;
        this.changeSide = side;
        allCommand = new ArrayList<>();
    }

    public int execute() throws Exception {        
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
            MAXCYCLES = 4000;
            //MAXCYCLES = 1000;
        }
        if (pgs.getHeight() == 24) {
            MAXCYCLES = 5000;
        }
        if (pgs.getHeight() == 32) {
            MAXCYCLES = 6000;
        }
        if (pgs.getHeight() == 64) {
            MAXCYCLES = 8000;
        }
        
        List<AI> ais = new ArrayList(Arrays.asList(new AIWithComputationBudget[]{
            new WorkerRush(utt),
            new botEmptyBase(utt, "for(u) (attack(Worker,lessHealthy,u)) train(Worker,3,Right)", "t1"),
            new botEmptyBase(utt, "for(u) (harvest(7,u) train(Worker,17,Down) attack(Worker,weakest,u))", "t2"),
            new botEmptyBase(utt, "for(u) (attack(Worker,closest,u)) for(u) (train(Worker,2,Up) train(Worker,8,Down))", "t3")
        }));

        AI ai1 = buildCommandsIA(utt, sIA1);
        AI ai2 = ais.get(idIA);
        if(changeSide){
            AI aiT = ai1;
            ai1 = ai2;
            ai2 = aiT;
        }
        
        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 640, 640, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);        
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                if (smartEvaluationForStop(gs)) {
                    result = 0.5f;
                    return -1;
                }
                // simulate:
                gameover = gs.cycle();
                //w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.err.println("ai.synthesis.runners.roundRobinLocal.RoundRobinGrammarAgainstGrammar.run() " + e.getMessage());
                }
            }

        } while (!gameover && (gs.getTime() <= MAXCYCLES));        
        winner = gs.winner();
        //w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        this.allCommand.clear();
        if(ai1 instanceof DslAI){
            this.allCommand.addAll(((DslAI) ai1).getCommands());
        } else if(ai2 instanceof DslAI){
            this.allCommand.addAll(((DslAI) ai2).getCommands());
        }
        
        result = 0.0f;
        if (winner == -1) {
            result = 0.5f;            
        }else if(changeSide){
            if (winner == 1) {
                result = 1.0f;
            }
        }else{
            if (winner == 0) {
                result = 1.0f;
            }
        }  
        return 0;
    }

    private AI buildCommandsIA(UnitTypeTable utt, iDSL code) {
        HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
        List<ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand> commandsGP = compiler.CompilerCode(code, utt);
        AI aiscript = new DslAI(utt, commandsGP, "P1", code, counterByFunction);
        return aiscript;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (Exception ex) {
            Logger.getLogger(SmartRRIAxGRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Check if after a quantity of cycles (defined by CYCLES_LIMIT) the game
     * can be stopped. If the state of the game continues the same for more than
     * CYCLES_LIMIT, the function will return true.
     *
     * @param gs - Game to be considered.
     * @return True - If the game can be stopped and defined as draw. False if the 
     * game changed after CYCLES_LIMIT games cycles.
     */
    private boolean smartEvaluationForStop(GameState gs) {
        if (gs.getTime() == 0) {            
            String cleanState = cleanStateInformation(gs);
            this.initialState = cleanState;
        } else if (gs.getTime() % CYCLES_LIMIT == 0) {
            String cleanState = cleanStateInformation(gs);
            if(cleanState.equals(initialState)){
                //System.out.println("** Smart Stop activate.\n Original state =\n"+initialState+
                //        " verified same state at \n"+cleanState);
                return true;
            }else{
                initialState = cleanState;
            }
        }

        return false;
    }

    private String cleanStateInformation(GameState gs) {
        String sGame = gs.toString().replace("\n", "");
        sGame = sGame.substring(sGame.indexOf("PhysicalGameState:")); 
        sGame = sGame.replace("PhysicalGameState:", "").trim();
        return sGame;
    }

}
