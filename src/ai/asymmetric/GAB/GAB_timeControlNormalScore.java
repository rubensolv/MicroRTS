/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.asymmetric.GAB;

import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.asymmetric.IDABCD.IDABCDAsymmetric;
import ai.asymmetric.ManagerUnits.IManagerAbstraction;
import ai.asymmetric.ManagerUnits.ManagerRandom;
import ai.asymmetric.common.UnitScriptData;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.InterruptibleAI;
import ai.core.ParameterSpecification;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 * Versão do GAB que não faz um playout ao final para comparar as ações.
 * @author rubens
 */
public class GAB_timeControlNormalScore extends AIWithComputationBudget implements InterruptibleAI {
    
    EvaluationFunction evaluation = null;
    UnitTypeTable utt;
    PathFinding pf;
    PGSLimit _pgs = null;
    IDABCDAsymmetric _ab = null;
    GameState gs_to_start_from = null;
    private int playerForThisComputation;
    int _time;
    int _max_playouts;
    HashSet<Unit> _unitsAbsAB;
    int _numUnits;
    IManagerAbstraction manager = null;

    public GAB_timeControlNormalScore(UnitTypeTable utt) {
        this(100, 200,new SimpleSqrtEvaluationFunction3(),
             //new SimpleSqrtEvaluationFunction2(),
             //new LanchesterEvaluationFunction(),
             utt,
             new AStarPathFinding());
    }
    
    public GAB_timeControlNormalScore(int time, int max_playouts, EvaluationFunction e, UnitTypeTable a_utt, PathFinding a_pf) {
        super(time, max_playouts);
        
        evaluation = e;
        utt = a_utt;
        pf = a_pf;
        _pgs = new PGSLimit(utt);
        _ab = new IDABCDAsymmetric(utt);
        _time = time;;
        _max_playouts = max_playouts;
        _unitsAbsAB = new HashSet<>();
        _numUnits = 2;
    }



    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlayerAction getAction(int player, GameState gs ) throws Exception {
         if (gs.canExecuteAnyAction(player)) {
            startManager(player, _numUnits);
            startNewComputation(player,gs);
            return getBestActionSoFar();
        } else {
            return new PlayerAction();        
        } 
    }
    
    
    protected void startManager(int playerID, int numUnits){
        if(manager == null){
                manager = new ManagerRandom(playerID, _numUnits);
        }
    }

    @Override
    public AI clone() {
        return new GAB_timeControlNormalScore(_time, _max_playouts, evaluation, utt, pf);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        List<ParameterSpecification> parameters = new ArrayList<>();
        
        parameters.add(new ParameterSpecification("TimeBudget",int.class,100));
        parameters.add(new ParameterSpecification("IterationsBudget",int.class,-1));
        parameters.add(new ParameterSpecification("PlayoutLookahead",int.class,200));
        parameters.add(new ParameterSpecification("EvaluationFunction", EvaluationFunction.class, new SimpleSqrtEvaluationFunction3()));
        parameters.add(new ParameterSpecification("PathFinding", PathFinding.class, new AStarPathFinding()));
        
        return parameters;
    }

    @Override
    public void startNewComputation(int player, GameState gs) throws Exception {
        playerForThisComputation = player;
        gs_to_start_from = gs;
    }

    @Override
    public void computeDuringOneGameFrame() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlayerAction getBestActionSoFar() throws Exception {
        long start = System.currentTimeMillis();
         UnitScriptData currentScriptData = _pgs.getUnitScript(playerForThisComputation, gs_to_start_from);
         
         if( (System.currentTimeMillis()-start)< 90 ){
             //aplico o AB
            manager.controlUnitsForAB(gs_to_start_from, _unitsAbsAB);
            
            _ab.setPlayoutAI(_pgs.defaultScript);
            int timeUsed = (int)(System.currentTimeMillis()-start);
            _ab.setTimeBudget(100 -timeUsed);
             System.out.println("----------------------------------------"+_unitsAbsAB);
            PlayerAction pa = _ab.getActionForAssymetric(playerForThisComputation, gs_to_start_from, currentScriptData, _unitsAbsAB);
            if(_ab.getBestScore() > _pgs.getBestScore()){
                return pa;
            }
         }
         
         return _pgs.getFinalAction(currentScriptData);

         
    }

}
