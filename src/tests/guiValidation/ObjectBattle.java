/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.guiValidation;

import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.core.AI;
import gui.PhysicalGameStatePanel;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class ObjectBattle {

    public boolean paused = false;
    UnitTypeTable utt = new UnitTypeTable();
    PhysicalGameState pgs;
    GameState gs;
    int MAXCYCLES = 5000;
    int PERIOD = 20;
    boolean gameover = false;
    AI ai1;
    AI ai2;
    JFrame w;

    public ObjectBattle() throws Exception {
        pgs = PhysicalGameState.load("maps/16x16/basesWorkers16x16.xml", utt);
        gs = new GameState(pgs, utt);
        ai1 = new LightRush(utt);
        ai2 = new RangedRush(utt);
        w = PhysicalGameStatePanel.newVisualizer(gs, 640, 640, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
    }

    public void runForOneFrame() throws Exception {

//      JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        boolean executed = false;
        do {

            if (System.currentTimeMillis() >= nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);

                // simulate:
                gameover = gs.cycle();
                w.repaint();                
                //w.validate();
                nextTimeToUpdate += PERIOD;
                System.out.println("Time in game" + gs.getTime());
                executed = true;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } while (!executed);

    }

}
