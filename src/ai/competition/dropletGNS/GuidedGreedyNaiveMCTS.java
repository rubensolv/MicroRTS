/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.competition.dropletGNS;

import ai.RandomBiasedAI;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.InterruptibleAI;
import ai.core.ParameterSpecification;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author santi
 */
public class GuidedGreedyNaiveMCTS extends AIWithComputationBudget implements InterruptibleAI {
    public static int DEBUG = 0;
    public EvaluationFunction ef = null;

    Random r = new Random();
    public AI playoutPolicy = new RandomBiasedAI();
    protected long max_actions_so_far = 0;
    public AI[] guide = null;
    protected GameState gs_to_start_from = null;
    protected GuidedGreedyNaiveMCTSNode tree = null;
    protected int current_iteration = 0;

    public int MAXSIMULATIONTIME = 1024;
    public int MAX_TREE_DEPTH = 10;

    protected int player;

    public float epsilon_0 = 0.2f;
    public float epsilon_l = 0.25f;
    public float epsilon_g = 0.0f;
    public float epsilon_s = .33f;

    // these variables are for using a discount factor on the epsilon values above. My experiments indicate that things work better without discount
    // So, they are just maintained here for completeness:
    public float initial_epsilon_0 = 0.2f;
    public float initial_epsilon_l = 0.25f;
    public float initial_epsilon_g = 0.0f;
    public float discount_0 = 0.999f;
    public float discount_l = 0.999f;
    public float discount_g = 0.999f;

    public int global_strategy = GuidedGreedyNaiveMCTSNode.E_GREEDY;
    public boolean forceExplorationOfNonSampledActions = true;

    // statistics:
    public int tree_depth = 0;
    public long total_runs = 0;
    public long total_cycles_executed = 0;
    public long total_actions_issued = 0;
    public long total_time = 0;
    // state control
    boolean reset_search = true;


    public GuidedGreedyNaiveMCTS(UnitTypeTable utt) {
        this(100, -1, 100, 10,
                0.3f, 0.0f, 0.4f, .33f,
                new RandomBiasedAI(),
                new SimpleSqrtEvaluationFunction3(), null, true);
    }


    public GuidedGreedyNaiveMCTS(int available_time, int max_playouts, int lookahead, int max_depth,
                                 float e_l, float discout_l,
                                 float e_g, float discout_g,
                                 float e_0, float discout_0,
                                 float e_s,
                                 AI policy, EvaluationFunction a_ef,
                                 AI[] gd,
                                 boolean fensa) {
        super(available_time, max_playouts);
        MAXSIMULATIONTIME = lookahead;
        playoutPolicy = policy;
        MAX_TREE_DEPTH = max_depth;
        initial_epsilon_l = epsilon_l = e_l;
        initial_epsilon_g = epsilon_g = e_g;
        initial_epsilon_0 = epsilon_0 = e_0;
        discount_l = discout_l;
        discount_g = discout_g;
        discount_0 = discout_0;
        ef = a_ef;
        guide = gd;
        forceExplorationOfNonSampledActions = fensa;
        epsilon_s = e_s;
    }
  //GuidedGreedyNaiveMCTS(g          etTimeBudget(), -1,                  100,               100,      .2f,       0.0f,     .6f,         .75f
    public GuidedGreedyNaiveMCTS(int available_time, int max_playouts, int lookahead, int max_depth, float e_l, float e_g, float e_0, float e_s, AI policy, EvaluationFunction a_ef, AI[] gd, boolean fensa) {
        super(available_time, max_playouts);
        MAXSIMULATIONTIME = lookahead;
        playoutPolicy = policy;
        MAX_TREE_DEPTH = max_depth;
        initial_epsilon_l = epsilon_l = e_l;
        initial_epsilon_g = epsilon_g = e_g;
        initial_epsilon_0 = epsilon_0 = e_0;
        discount_l = 1.0f;
        discount_g = 1.0f;
        discount_0 = 1.0f;
        epsilon_s = e_s;
        ef = a_ef;
        guide = gd;
        forceExplorationOfNonSampledActions = fensa;
    }

    public GuidedGreedyNaiveMCTS(int available_time, int max_playouts, int lookahead, int max_depth, float e_l, float e_g, float e_0, float e_s, int a_global_strategy, AI policy, EvaluationFunction a_ef, AI[] gd, boolean fensa) {
        super(available_time, max_playouts);
        MAXSIMULATIONTIME = lookahead;
        playoutPolicy = policy;
        MAX_TREE_DEPTH = max_depth;
        initial_epsilon_l = epsilon_l = e_l;
        initial_epsilon_g = epsilon_g = e_g;
        initial_epsilon_0 = epsilon_0 = e_0;
        discount_l = 1.0f;
        discount_g = 1.0f;
        discount_0 = 1.0f;
        global_strategy = a_global_strategy;
        ef = a_ef;
        epsilon_s = e_s;
        guide = gd;
        forceExplorationOfNonSampledActions = fensa;
    }

    public void reset() {
        tree = null;
        gs_to_start_from = null;
        total_runs = 0;
        total_cycles_executed = 0;
        total_actions_issued = 0;
        total_time = 0;
        current_iteration = 0;
        reset_search = true;
    }

    public static int[] width = new int[50];

    public AI clone() {
        return new GuidedGreedyNaiveMCTS(TIME_BUDGET, ITERATIONS_BUDGET, MAXSIMULATIONTIME, MAX_TREE_DEPTH, epsilon_l, discount_l, epsilon_g, discount_g, epsilon_0, discount_0, epsilon_s, playoutPolicy, ef, guide, forceExplorationOfNonSampledActions);
    }    


    public PlayerAction getAction(int player, GameState gs) throws Exception {
        if (gs.canExecuteAnyAction(player)) {
            if (reset_search) {
                startNewComputation(player, gs.clone());
            }
            computeDuringOneGameFrame();
            reset_search = true;
            return getBestActionSoFar();
        } else {
            if (moveGameStateNextDecision(player, gs)) {
                computeDuringOneGameFrame();
                reset_search = false;
            } 
            return new PlayerAction();
        }
    }
    
    private boolean moveGameStateNextDecision(int player, GameState gs) throws Exception {
        int timeNextAction = gs.getNextChangeTime();
        GameState gs2 = gs.clone();
        boolean gameover = false;
        while (!gameover && (gs2.getTime() < timeNextAction)) {
            gameover = gs2.cycle();
        }
        if (gs2.canExecuteAnyAction(player)) { 
            if(reset_search){
                startNewComputation(player, gs2);
                if (DEBUG >= 2) {
                    System.out.println("State moved to "+gs_to_start_from.getTime());
                }
            }
            
            return true;
        }
        return false;
    }


    public void startNewComputation(int a_player, GameState gs) throws Exception {
        player = a_player;
        current_iteration = 0;
        tree = new GuidedGreedyNaiveMCTSNode(player, 1 - player, gs, null, ef.upperBound(gs), current_iteration++, guide, forceExplorationOfNonSampledActions);

        if (tree.moveGenerator == null) {
            max_actions_so_far = 0;
        } else {
            max_actions_so_far = Math.max(tree.moveGenerator.getSize(), max_actions_so_far);
        }
        gs_to_start_from = gs;

        epsilon_l = initial_epsilon_l;
        epsilon_g = initial_epsilon_g;
        epsilon_0 = initial_epsilon_0;
    }


    public void resetSearch() {
        if (DEBUG >= 2) System.out.println("Resetting search...");
        tree = null;
        gs_to_start_from = null;
    }


    public void computeDuringOneGameFrame() throws Exception {
        if (DEBUG >= 2) System.out.println("Search...");
        long start = System.currentTimeMillis();
        long end = start;
        long count = 0;
        while (true) {
            if (!iteration(player)) break;
            count++;
            end = System.currentTimeMillis();

            if (TIME_BUDGET >= 0 && (end - start) >= TIME_BUDGET) break;
            if (ITERATIONS_BUDGET >= 0 && count >= ITERATIONS_BUDGET) break;
        }
        total_time += (end - start);
        total_cycles_executed++;
    }


    public boolean iteration(int player) throws Exception {

        GuidedGreedyNaiveMCTSNode leaf = tree.selectLeaf(player, 1 - player, epsilon_l, epsilon_g, epsilon_0, epsilon_s, global_strategy, MAX_TREE_DEPTH, current_iteration++);
        if (leaf.depth > tree_depth) {
            tree_depth = leaf.depth;
        }
        if (leaf != null) {
            GameState gs2 = leaf.gs.clone();
            simulate(gs2, gs2.getTime() + MAXSIMULATIONTIME);

            int time = gs2.getTime() - gs_to_start_from.getTime();
            double evaluation = ef.evaluate(player, 1 - player, gs2) * Math.pow(0.99, time / 10.0);

            leaf.propagateEvaluation(evaluation, null);

            // update the epsilon values:
            epsilon_0 *= discount_0;
            epsilon_l *= discount_l;
            epsilon_g *= discount_g;
            total_runs++;


        } else {
            // no actions to choose from :)
            System.err.println(this.getClass().getSimpleName() + ": claims there are no more leafs to explore...");
            return false;
        }
        return true;
    }

    public PlayerAction getBestActionSoFar() {
        int idx = getMostVisitedActionIdx();
//        if (idx!=0) System.out.println("new moves");
        if (idx == -1) {
            if (DEBUG >= 1) System.out.println("NaiveMCTS no children selected. Returning an empty asction");
            return new PlayerAction();
        }
        if (DEBUG >= 2) tree.showNode(0, 1, ef);
        if (DEBUG >= 1) {
            GuidedGreedyNaiveMCTSNode best = (GuidedGreedyNaiveMCTSNode) tree.children.get(idx);
            System.out.println("NaiveMCTS selected children " + tree.actions.get(idx) + " explored " + best.visit_count + " Avg evaluation: " + (best.accum_evaluation / ((double) best.visit_count)));
        }
        return tree.actions.get(idx);
    }


    public int getMostVisitedActionIdx() {
        total_actions_issued++;

        int bestIdx = -1;
        GuidedGreedyNaiveMCTSNode best = null;
        double bestSofar = -1;
        if (DEBUG >= 2) {
//            for(Player p:gs_to_start_from.getPlayers()) {
//                System.out.println("Resources P" + p.getID() + ": " + p.getResources());
//            }
            System.out.println("Number of playouts: " + tree.visit_count);
            tree.printUnitActionTable();
        }
        if (tree.children == null) return -1;
        for (int i = 0; i < tree.children.size(); i++) {
            GuidedGreedyNaiveMCTSNode child = (GuidedGreedyNaiveMCTSNode) tree.children.get(i);
            if (DEBUG >= 2) {
                System.out.println("child " + tree.actions.get(i) + " explored " + child.visit_count + " Avg evaluation: " + (child.accum_evaluation / ((double) child.visit_count)));
            }
//            if (best == null || (child.accum_evaluation/child.visit_count)>(best.accum_evaluation/best.visit_count)) {
            if (best == null || child.visit_count > bestSofar) {
                best = child;
                bestIdx = i;
                bestSofar = best.visit_count;
                if (bestIdx==0)
                    bestSofar*=.75;
            }
        }

        return bestIdx;
    }


    public int getHighestEvaluationActionIdx() {
        total_actions_issued++;

        int bestIdx = -1;
        GuidedGreedyNaiveMCTSNode best = null;
        if (DEBUG >= 2) {
//            for(Player p:gs_to_start_from.getPlayers()) {
//                System.out.println("Resources P" + p.getID() + ": " + p.getResources());
//            }
            System.out.println("Number of playouts: " + tree.visit_count);
            tree.printUnitActionTable();
        }
        for (int i = 0; i < tree.children.size(); i++) {
            GuidedGreedyNaiveMCTSNode child = (GuidedGreedyNaiveMCTSNode) tree.children.get(i);
            if (DEBUG >= 2) {
                System.out.println("child " + tree.actions.get(i) + " explored " + child.visit_count + " Avg evaluation: " + (child.accum_evaluation / ((double) child.visit_count)));
            }
//            if (best == null || (child.accum_evaluation/child.visit_count)>(best.accum_evaluation/best.visit_count)) {
            if (best == null || (child.accum_evaluation / ((double) child.visit_count)) > (best.accum_evaluation / ((double) best.visit_count))) {
                best = child;
                bestIdx = i;
            }
        }

        return bestIdx;
    }


    public void simulate(GameState gs, int time) throws Exception {
        boolean gameover = false;

        do {
            if (gs.isComplete()) {
                gameover = gs.cycle();
            } else {
                gs.issue(playoutPolicy.getAction(0, gs));
                gs.issue(playoutPolicy.getAction(1, gs));
            }
        } while (!gameover && gs.getTime() < time);
    }

    public GuidedGreedyNaiveMCTSNode getTree() {
        return tree;
    }

    public GameState getGameStateToStartFrom() {
        return gs_to_start_from;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + TIME_BUDGET + ", " + ITERATIONS_BUDGET + ", " + MAXSIMULATIONTIME + "," + MAX_TREE_DEPTH + "," + epsilon_l + ", " + discount_l + ", " + epsilon_g + ", " + discount_g + ", " + epsilon_0 + ", " + discount_0 + ", " + playoutPolicy + ", " + ef + ")";
    }

    @Override
    public String statisticsString() {
        return "Total runs: " + total_runs +
                ", runs per action: " + (total_runs / (float) total_actions_issued) +
                ", runs per cycle: " + (total_runs / (float) total_cycles_executed) +
                ", average time per cycle: " + (total_time / (float) total_cycles_executed) +
                ", max branching factor: " + max_actions_so_far;
    }


    @Override
    public List<ParameterSpecification> getParameters() {
        List<ParameterSpecification> parameters = new ArrayList<>();

        parameters.add(new ParameterSpecification("TimeBudget", int.class, 100));
        parameters.add(new ParameterSpecification("IterationsBudget", int.class, -1));
        parameters.add(new ParameterSpecification("PlayoutLookahead", int.class, 100));
        parameters.add(new ParameterSpecification("MaxTreeDepth", int.class, 10));

        parameters.add(new ParameterSpecification("E_l", float.class, 0.3));
        parameters.add(new ParameterSpecification("Discount_l", float.class, 1.0));
        parameters.add(new ParameterSpecification("E_g", float.class, 0.0));
        parameters.add(new ParameterSpecification("Discount_g", float.class, 1.0));
        parameters.add(new ParameterSpecification("E_0", float.class, 0.4));
        parameters.add(new ParameterSpecification("Discount_0", float.class, 1.0));

        parameters.add(new ParameterSpecification("DefaultPolicy", AI.class, playoutPolicy));
        parameters.add(new ParameterSpecification("EvaluationFunction", EvaluationFunction.class, new SimpleSqrtEvaluationFunction3()));

        parameters.add(new ParameterSpecification("ForceExplorationOfNonSampledActions", boolean.class, true));

        return parameters;
    }


    public int getPlayoutLookahead() {
        return MAXSIMULATIONTIME;
    }


    public void setPlayoutLookahead(int a_pola) {
        MAXSIMULATIONTIME = a_pola;
    }


    public int getMaxTreeDepth() {
        return MAX_TREE_DEPTH;
    }


    public void setMaxTreeDepth(int a_mtd) {
        MAX_TREE_DEPTH = a_mtd;
    }


    public float getE_l() {
        return epsilon_l;
    }


    public void setE_l(float a_e_l) {
        epsilon_l = a_e_l;
    }


    public float getDiscount_l() {
        return discount_l;
    }


    public void setDiscount_l(float a_discount_l) {
        discount_l = a_discount_l;
    }


    public float getE_g() {
        return epsilon_g;
    }


    public void setE_g(float a_e_g) {
        epsilon_g = a_e_g;
    }


    public float getDiscount_g() {
        return discount_g;
    }


    public void setDiscount_g(float a_discount_g) {
        discount_g = a_discount_g;
    }


    public float getE_0() {
        return epsilon_0;
    }


    public void setE_0(float a_e_0) {
        epsilon_0 = a_e_0;
    }


    public float getDiscount_0() {
        return discount_0;
    }


    public void setDiscount_0(float a_discount_0) {
        discount_0 = a_discount_0;
    }


    public AI getDefaultPolicy() {
        return playoutPolicy;
    }


    public void setDefaultPolicy(AI a_dp) {
        playoutPolicy = a_dp;
    }


    public EvaluationFunction getEvaluationFunction() {
        return ef;
    }


    public void setEvaluationFunction(EvaluationFunction a_ef) {
        ef = a_ef;
    }

    public boolean getForceExplorationOfNonSampledActions() {
        return forceExplorationOfNonSampledActions;
    }

    public void setForceExplorationOfNonSampledActions(boolean fensa) {
        forceExplorationOfNonSampledActions = fensa;
    }
}
