/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.ParametersConcrete;

import rts.GameState;
import rts.PhysicalGameState;
import rts.units.Unit;

/**
 *
 * @author rubens
 */
public class StrongestEnemy extends BehaviorAbstract{

    @Override
    public Unit getEnemytByBehavior(GameState game, int player, Unit unitAlly) {
        PhysicalGameState pgs = game.getPhysicalGameState();
        Unit strongestEnemy = null;
        int maximumDamage = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() == player) {
                int d = u2.getMaxDamage();
                if (strongestEnemy == null || d > maximumDamage) {
                    strongestEnemy = u2;
                    maximumDamage = d;
                }
            }
        }
        return strongestEnemy;
    }
    
}
