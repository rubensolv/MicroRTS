package ai.competition.dropletGNS;

import ai.RandomBiasedAI;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.WorkerRushPlusPlus;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

import java.util.List;

public class Droplet4Scr extends AIWithComputationBudget {

    UnitTypeTable utt;
    boolean started = false;
    AI ai;

    public Droplet4Scr(int timeBudget, int iterationsBudget) {
        super(timeBudget, iterationsBudget);
    }

    public Droplet4Scr(UnitTypeTable utt) {
        super(100, -1);
        this.utt = utt;
    }

    @Override
    public void reset() {
        this.started = false;
    }

    @Override
    public PlayerAction getAction(int i, GameState gameState) throws Exception {
        if (gameState.canExecuteAnyAction(i)) {
            if (!started) {
                ai = new GuidedGreedyNaiveMCTS(getTimeBudget(), -1, 100, 100,
                        .2f, 0.0f, .6f, .75f, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(),
                        new AI[]{
                            new WorkerRush(utt),
                            new LightRush(utt),
                            new RangedRush(utt),
                            new HeavyRush(utt)
                        },
                        true);
                started = true;
            }
            return ai.getAction(i, gameState);
        } else {
            return new PlayerAction();
        }
    }

    @Override
    public AI clone() {
        return new Droplet4Scr(utt);
    }
        

    @Override
    public List<ParameterSpecification> getParameters() {
        return null;
    }
}
