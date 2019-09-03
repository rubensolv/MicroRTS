package ai.asymmetric.SSSDavid;

import java.util.HashMap;
import java.util.List;

import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import rts.GameState;
import rts.PlayerAction;
import rts.units.Unit;

public class AbstractionLayerAID extends AbstractionLayerAI {

	
	 public PlayerAction getAction(int player, GameState gs, List<Unit> Units, List<Unit> Units_aux, Information inf, HashMap<Unit, AbstractAction> act)throws Exception {
	    	
			return null;
	    }
	
	
	public AbstractionLayerAID(PathFinding a_pf) {
		super(a_pf);
		// TODO Auto-generated constructor stub
	}

	public AbstractionLayerAID(PathFinding a_pf, int timebudget, int cyclesbudget) {
		super(a_pf, timebudget, cyclesbudget);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PlayerAction getAction(int player, GameState gs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AI clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParameterSpecification> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
