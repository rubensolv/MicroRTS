/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.competition.dropletGNS;

import ai.abstraction.pathfinding.PathFinding;
import rts.GameState;
import rts.PhysicalGameState;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import util.XMLWriter;

import java.util.Random;

/**
 *
 * @author santi
 */
public class Move extends AbstractAction {

    int x,y;
    PathFinding pf;

    
    public Move(Unit u, int a_x, int a_y, PathFinding a_pf) {
        super(u);
        x = a_x;
        y = a_y;
        pf = a_pf;
    }
    
    public boolean completed(GameState gs) {
        if (unit.getX()==x && unit.getY()==y) return true;
        return false;
    }
    
    
    public boolean equals(Object o)
    {
        if (!(o instanceof Move)) return false;
        Move a = (Move)o;
        if (x != a.x) return false;
        if (y != a.y) return false;
        if (pf.getClass() != a.pf.getClass()) return false;
        
        return true;
    }

    
    public void toxml(XMLWriter w)
    {
        w.tagWithAttributes("Move","unitID=\""+unit.getID()+"\" x=\""+x+"\" y=\""+y+"\" pathfinding=\""+pf.getClass().getSimpleName()+"\"");
        w.tag("/Move");
    }       

    public UnitAction execute(GameState gs, ResourceUsage ru) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        UnitAction move = pf.findPath(unit, x+y*pgs.getWidth(), gs, ru);
//        System.out.println("AStarAttak returns: " + move);
        if (move!=null && gs.isUnitActionAllowed(unit, move)) return move;
        int m = new Random().nextInt(5);
        if (m == 4) return null;
        UnitAction m1 = new UnitAction(UnitAction.TYPE_MOVE, m);
        UnitAction m2 = new UnitAction(UnitAction.TYPE_MOVE, (m + 1) % 4);
        UnitAction m3 = new UnitAction(UnitAction.TYPE_MOVE, (m + 2) % 4);
        UnitAction m4 = new UnitAction(UnitAction.TYPE_MOVE, (m + 3) % 4);
        if (gs.isUnitActionAllowed(unit, m1)) return m1;
        if (gs.isUnitActionAllowed(unit, m2)) return m2;
        if (gs.isUnitActionAllowed(unit, m3)) return m3;
        if (gs.isUnitActionAllowed(unit, m4)) return m4;
        return null;
    }
}
