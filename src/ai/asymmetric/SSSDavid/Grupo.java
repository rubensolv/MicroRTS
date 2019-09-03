package ai.asymmetric.SSSDavid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import ai.abstraction.pathfinding.AStarPathFinding;
import rts.GameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

public class Grupo {
	//protected HashMap<Long, Unit> unidades = new LinkedHashMap<>();
	List<Unit> units;
	
	public Grupo(UnitTypeTable a_utt) {
	 
	        units = new LinkedList<Unit>();
	}
	

	
	/*
	public PlayerAction getactions(PlayerAction pa, int player, GameState gs) throws Exception {
		return script.getAction(pa,base,workers, player, gs);
	}
	public void addunit(Unit u) {
		workers.add(u);
	}
	public void addbase(Unit u) {
		base =u;
	}
	public void resert() {
		workers.clear();
		base = null;
	}
	*/
}
