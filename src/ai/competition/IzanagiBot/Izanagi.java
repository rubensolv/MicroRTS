/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.competition.IzanagiBot;

import ai.RandomBiasedAI;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.asymmetric.PGS.LightPGSSCriptChoice;
import ai.asymmetric.SSS.LightSSSmRTSScriptChoice;
import ai.asymmetric.SSS.SSSmRTSScriptChoiceRandom;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.core.AI;
import ai.core.ParameterSpecification;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author Julian, Rubens and Levi
 */
public class Izanagi extends AbstractionLayerAI {
    
    GameState gs_to_start_from = null;
    int playerForThisComputation;
    long start_time = 0;
    boolean started = false;
    AI baseAI = null;
    UnitTypeTable utt;
    ScriptsCreator sc;
    ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet;
    Random rand = new Random();

    public Izanagi(UnitTypeTable utt){
        super(new AStarPathFinding(),100, 200);
        this.utt = utt;
        sc = new ScriptsCreator(utt,300);
        scriptsCompleteSet = sc.getScriptsMixReducedSet();
    }
    
    public Izanagi(int time, int max_playouts) {
        super(new AStarPathFinding(),time, max_playouts);
        started = false;
    }

    @Override
    public void reset() {
        this.started = false;
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        if (gs.canExecuteAnyAction(player)) {
            if(!started){
                inicializeIA(player, gs);
                started = true;
            }
            startNewComputation(player, gs);
            return getBestActionSoFar();
        } else {
            return new PlayerAction();
        }
    }

    @Override
    public AI clone() {
        return new Izanagi(utt);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        List<ParameterSpecification> parameters = new ArrayList<>();

        parameters.add(new ParameterSpecification("TimeBudget", int.class, this.TIME_BUDGET));
        parameters.add(new ParameterSpecification("IterationsBudget", int.class, -1));
        parameters.add(new ParameterSpecification("PlayoutLookahead", int.class, this.ITERATIONS_BUDGET));        
        parameters.add(new ParameterSpecification("EvaluationFunction", EvaluationFunction.class, new SimpleSqrtEvaluationFunction3()));
        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));

        return parameters;
    }

    
    public void startNewComputation(int player, GameState gs) throws Exception {
        playerForThisComputation = player;
        gs_to_start_from = gs;
        start_time = System.currentTimeMillis();
    }

    
    public void computeDuringOneGameFrame() throws Exception {
        
    }

    
    public PlayerAction getBestActionSoFar() throws Exception {
        return baseAI.getAction(playerForThisComputation, gs_to_start_from);
    }

    private void inicializeIA(int player, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        if(pgs.getHeight() == 8 && pgs.getWidth() == 8){
            //8x8
            this.baseAI = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 
                                             0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                                             new SimpleSqrtEvaluationFunction3(), true, utt, 
                                            "ManagerClosestEnemy", 1,decodeScripts(utt, "48;0;"));
        } else if(pgs.getHeight() == 8 && pgs.getWidth() == 9){
            //9x8
            this.baseAI = new SSSmRTSScriptChoiceRandom(utt, decodeScripts(utt, "203;3;"), "Izanagi",4,200);
        } else if( (pgs.getHeight() >= 9 && pgs.getHeight() <= 16) && (pgs.getWidth() >= 9 && pgs.getWidth() <= 16) ){
            //16x16
            int choice = rand.nextInt(2);
            if(choice == 0){
                this.baseAI = new LightSSSmRTSScriptChoice(utt, decodeScripts(utt, "281;"), 200, "Izanagi");
            }else{                
                this.baseAI = new CmabAssymetricMCTS(100, -1, 100, 1, 0.3f, 
                                             0.0f, 0.4f, 0, new RandomBiasedAI(utt), 
                                             new SimpleSqrtEvaluationFunction3(), true, utt, 
                                            "ManagerClosestEnemy", 1, decodeScripts(utt, "1;270;"));
            }
        } else if( (pgs.getHeight() > 16 && pgs.getHeight() <= 24) && (pgs.getWidth() > 16 && pgs.getWidth() <= 24) ){
            //24
            int choice = rand.nextInt(2);
            if(choice == 0){
                this.baseAI = new LightSSSmRTSScriptChoice(utt, decodeScripts(utt, "281;"), 200, "Izanagi");
            }else{
                this.baseAI = new LightPGSSCriptChoice(utt, decodeScripts(utt, "241;25;"), 200, "Izanagi");
            }
        }else if( (pgs.getHeight() > 24 && pgs.getHeight() <= 32) && (pgs.getWidth() > 24 && pgs.getWidth() <= 32) ){
            //32
            int choice = rand.nextInt(2);
            if(choice == 0){
                this.baseAI = new SSSmRTSScriptChoiceRandom(utt, decodeScripts(utt, "1;285;244;272;198;"), "Izanagi",4,200);
            }else{
                this.baseAI = new LightSSSmRTSScriptChoice(utt, decodeScripts(utt, "233;273;"), 200, "Izanagi");
            }
        }else{
            this.baseAI = new CmabAssymetricMCTS(100, -1, 50, 2, 0.3f, 0.0f, 0.4f, 0, new RandomBiasedAI(utt),
                new SimpleSqrtEvaluationFunction3(), true, utt, "ManagerClosestEnemy", 1,
                decodeScripts(utt, "217;257;"), "Izanagi");
        }
    }
    
    
    private List<AI> decodeScripts(UnitTypeTable utt, String sScripts) {
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = sScripts.split(";");
        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }
        List<AI> scriptsAI = new ArrayList<>();
        iScriptsAi1.forEach((idSc) -> {
            scriptsAI.add(scriptsCompleteSet.get(idSc));
        });
        return scriptsAI;
    }
}
