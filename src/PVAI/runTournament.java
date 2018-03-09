/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PVAI;

import ai.core.AI;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.units.UnitTypeTable;
import tests.TorneioThread;
import tournaments.RoundRobinTournament;

/**
 *
 * @author rubens
 */
    public class runTournament implements Runnable{

        private List<AI> aisSelected;
        private List<String> mapsSelected;
        private int cycles;
        private boolean timeoutCheck;
        private UnitTypeTable utt;
        private String tracesFolder;
        private Writer writer;
        private Writer writerProgress;
        
        public runTournament(List<AI> aisSelected, List<String> mapsSelected, int cycles, boolean timeoutCheck, UnitTypeTable utt, String tracesFolder, Writer writer,
                            Writer writerProgress){
            this.aisSelected =  aisSelected;
            this.mapsSelected = mapsSelected;
            this.cycles = cycles;
            this.timeoutCheck = timeoutCheck;
            this.utt = utt;
            this.tracesFolder = tracesFolder;
            this.writer = writer;
            this.writerProgress = writerProgress;
            
        }
        
        @Override
        public void run() {
            try {
                RoundRobinTournament.runTournament(aisSelected,-1, mapsSelected, 3, cycles, 100, -1, -1, true, false, timeoutCheck, true, false, utt, tracesFolder, writer, writerProgress);
            } catch (Exception ex) {
                Logger.getLogger(TorneioThread.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }
        }
    
    }
