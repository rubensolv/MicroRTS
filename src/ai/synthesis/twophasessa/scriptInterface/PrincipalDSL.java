
package ai.synthesis.twophasessa.scriptInterface;

import ai.CMAB.*;
import ai.PassiveAI;
import ai.RandomBiasedAI;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.competition.newBotsEval.botEmptyBase;
import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.twophasessa.TradutorDSL;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import gui.PhysicalGameStatePanel;
import ai.ScriptsGenerator.professionalScripts.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import ai.synthesis.twophasessa.scriptInterface.gui.PhysicalGameStateScriptInterfaceJFrame;

public class PrincipalDSL {
	

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;

    public static void main(String args[]) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load(InterfaceSettings.getInstance().getMap(), utt);
        
        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 8000;
        int PERIOD = 20;
        boolean gameover = false;

        AI ai1 = new PassiveAI(utt);
        AI ai2 = new PassiveAI(utt);    
        
        /// Criação da tela
        int larguraTela = 983;
        int alturaTela = 725;
        PhysicalGameStatePanel pgsp = new PhysicalGameStatePanel(gs);
        PhysicalGameStateScriptInterfaceJFrame tela = new PhysicalGameStateScriptInterfaceJFrame("Script Interface", larguraTela, alturaTela, pgsp);
        
        long startTime = System.currentTimeMillis();
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        
        do {
	        do {
	            if (System.currentTimeMillis() >= nextTimeToUpdate) {
	            	
	            	// Pause
	            	while(InterfaceSettings.getInstance().isPaused() && !InterfaceSettings.getInstance().isRestarted()) {
	            		nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
	            		pauseGame();
	        			ai1 = attAI(utt, 1);
	        			ai2 = attAI(utt, 2);
	            	}
	            	// Apply durante a simulação
	            	if(InterfaceSettings.getInstance().isApplied()) {
	            		ai1 = attAI(utt, 1);
	        			ai2 = attAI(utt, 2);
	            		InterfaceSettings.getInstance().setApply(false);
	            	}

	                startTime = System.currentTimeMillis();
	                PlayerAction pa1 = ai1.getAction(0, gs);

	                startTime = System.currentTimeMillis();
	                PlayerAction pa2 = ai2.getAction(1, gs);
	                
	                gs.issueSafe(pa1);
	                gs.issueSafe(pa2);
	                
	                // simulate:
	                gameover = gs.cycle();
	                tela.repaint();
	                nextTimeToUpdate += PERIOD;
	            } else {
	                try {
	                    Thread.sleep(1);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }

	        } while (!gameover && gs.getTime() < MAXCYCLES && !InterfaceSettings.getInstance().isRestarted());
	        
	        if(!InterfaceSettings.getInstance().isRestarted()) {
	        	System.out.println("Winner " + Integer.toString(gs.winner()));
	        	System.out.println("Game Over");
	        	InterfaceSettings.getInstance().setPause(true);
	        	while(InterfaceSettings.getInstance().isPaused() && !InterfaceSettings.getInstance().isRestarted() ) {
	        		pauseGame();
	        	}
	        	
	        	InterfaceSettings.getInstance().setRestart(true);
	        }
	        
	        InterfaceSettings.getInstance().setPause(true);
	        do {
		        //Restart Game
				pgs = PhysicalGameState.load(InterfaceSettings.getInstance().getMap(), utt);
				gs = new GameState(pgs, utt);
				gameover = gs.cycle();
				pgsp = new PhysicalGameStatePanel(gs);
				tela.recreate(larguraTela, alturaTela, pgsp);
				ai1 = attAI(utt, 1);
				ai2 = attAI(utt, 2);
				
				InterfaceSettings.getInstance().setPlay(false);
				InterfaceSettings.getInstance().setRestart(false);
				
				startTime = System.currentTimeMillis();
		        nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
		        
		        InterfaceSettings.getInstance().setRestart(false);

	        } while(InterfaceSettings.getInstance().isRestarted());
	        
        } while(true);
    }
    
    public static List<AI> decodeScripts(UnitTypeTable utt, String sScripts) {
        
        //decompõe a tupla
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = sScripts.split(";");

        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }
        
        List<AI> scriptsAI = new ArrayList<>();

        ScriptsCreator sc = new ScriptsCreator(utt,300);
        ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet = sc.getScriptsMixReducedSet();

        iScriptsAi1.forEach((idSc) -> {
            scriptsAI.add(scriptsCompleteSet.get(idSc));
        });

        return scriptsAI;
    }
    

    public static void pauseGame() {
    	try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    //atualiza a IA de acordo com seu ID e nome pela interface
    public static AI attAI(UnitTypeTable utt, int id) {
    	String script = "";
    	if(id == 2) {
    		for(int i = 0; i < (InterfaceSettings.getInstance().getScritpsAi2()).size(); i++ ) {
    			script = script + InterfaceSettings.getInstance().getScritpsAi2().get(i);
    			script = script + " ";
    		}
    		
    		if(script != "" && script != null) {
	    		HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
	    		IDSLCompiler compiler = new MainDSLCompiler();
	    		TradutorDSL tScript = new TradutorDSL(script);
	    		//System.out.println(tScript.getAST().translate());
	    		List<ICommand> commandsDSL = compiler.CompilerCode(tScript.getAST(), utt);
	    		return new DslAI(utt, commandsDSL, "P2", tScript.getAST(), counterByFunction);
    		}
    		return new botEmptyBase(utt, script, "IA2");
    	}else {
    		for(int i = 0; i < (InterfaceSettings.getInstance().getScritpsAi1()).size(); i++ ) {
    			script = script + InterfaceSettings.getInstance().getScritpsAi1().get(i);
    			script = script + " ";
    		}
    		
    		if(script != "" && script != null) {
	    		HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
	    		IDSLCompiler compiler = new MainDSLCompiler();
	    		TradutorDSL tScript = new TradutorDSL(script);
	    		//System.out.println(tScript.getAST().translate());
	    		List<ICommand> commandsDSL = compiler.CompilerCode(tScript.getAST(), utt);
	    		return new DslAI(utt, commandsDSL, "P2", tScript.getAST(), counterByFunction);
    		}
    		return new botEmptyBase(utt, script, "IA1");
    	}
    }


}
