// Classe Singleton com informa��es capturadas da tela

package ai.synthesis.twophasessa.scriptInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ListIterator;

import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.synthesis.twophasessa.scriptInterface.gui.MapPath;


public class InterfaceSettings {
	
	//private static Context uniqueInstance = new Context();
	private static InterfaceSettings uniqueInstance;
	
	private InterfaceSettings() {}
	
	//Abas à esquerda
	private int abaAddScript = 1;
	
	// Barra Inferior
	private boolean pause = true; //true
	private boolean restart = false;
	private boolean play = false;
	private boolean apply = false;
	private boolean applyScripts = false;
	
	// Map
	private String map = "maps/basesWorkers8x8A.xml";
	ArrayList<String> scriptsAi1 = new ArrayList<>();
	ArrayList<String> scriptsAi2 = new ArrayList<>();
	
	//public static Context getInstance() {
	//    return uniqueInstance;
	//}
	
	synchronized public static InterfaceSettings getInstance() {
	    if (uniqueInstance == null) uniqueInstance = new InterfaceSettings();
	    return uniqueInstance;
	}
	
	
	public void setAbaAddScript(int op) {
		abaAddScript = op;
	}
	
	public int getAbaAddScript() {
		return abaAddScript;
	}
	
	
	// Barra inferior
	
	public void setApply(boolean op) {
		apply = op;
	}
	
	public boolean isApplied() {
		return apply;
	}
	
	public void setPause(boolean op) {
		pause = op;
	}
	
	public boolean isPaused() {
		return pause;
	}
		
	public void setRestart(boolean op) {
		if(op)
			setPause(true);
		restart = op;
	}
	
	public boolean isRestarted() {
		return restart;
	}
	
	public void setPlay(boolean op) {
		play = op;
	}
	
	public boolean isPlayClicked() {
		return play;
	}
	
	public void setApplyScripts(boolean op) {
		applyScripts = op;
	}
	
	public boolean isAppliedScripts() {
		return applyScripts;
	}

	// Telinha Main
	public String getMap() {
		return map;
	}
	
	public void setMap(MapPath m) {
		if(m != null)
			map = m.getPath();
	}

	/*
	public String getAI1() {
		return ai1;
	}

	public String getAI2() {
		return ai2;
	}

	public void setAI1(String s_ai1) {
		if(s_ai1 != null)
			ai1 = s_ai1;
	}
	
	public void setAI2(String s_ai2) {
		if(s_ai2 != null)
			ai2 = s_ai2;
	}
	*/
	
	public void setScritpsAi1 (ArrayList<String> s) {
		this.scriptsAi1 = s;
	}
	
	public void setScritpsAi2 (ArrayList<String> s) {
		this.scriptsAi2 = s;
	}
	
	public void addScriptAI1(String i) {
		scriptsAi1.add(i);
	}
	
	public void clearScriptsAI1() {
		scriptsAi1.clear();
	}
	
	public ArrayList<String> getScritpsAi1(){
		return scriptsAi1;
	}
	
	public void addScriptAI2(String i) {
		scriptsAi2.add(i);
	}
	
	public void clearScriptsAI2() {
		scriptsAi2.clear();
	}
	
	public ArrayList<String> getScritpsAi2(){
		return scriptsAi2;
	}
	
	public void removeScriptAi1(String a) {
		scriptsAi1.remove(a);
	}
	
	public void removeScriptAi2(String a) {
		scriptsAi2.remove(a);
	}
	
	public void upScriptAi1(String a) {
		ListIterator<String> itr1 = scriptsAi1.listIterator();
		ListIterator<String> itr2 = scriptsAi1.listIterator();
		String a1, a2 = "";
		
		if(itr1.next() != a) {
			
			while(itr1.hasNext()) {
				if(itr1.next() == a) {
					itr1.previous();
					break;
				}
				itr2.next();
			}
			
			a2 = itr1.next();
			a1 = itr2.next();
			itr1.previous();
			itr2.previous();
			
			itr2.set(a2);
			itr1.set(a1);
		}
	}
	
	public void upScriptAi2(String a) {
		ListIterator<String> itr1 = scriptsAi2.listIterator();
		ListIterator<String> itr2 = scriptsAi2.listIterator();
		String a1, a2 = "";
		
		if(itr1.next() != a) {
			
			while(itr1.hasNext()) {
				if(itr1.next() == a) {
					itr1.previous();
					break;
				}
				itr2.next();
			}
			
			a2 = itr1.next();
			a1 = itr2.next();
			itr1.previous();
			itr2.previous();
			
			itr2.set(a2);
			itr1.set(a1);
		}
	}
	
	public void downScriptAi1(String a) {
		ListIterator<String> itr1 = scriptsAi1.listIterator();
		ListIterator<String> itr2 = scriptsAi1.listIterator();
		String a1 = "", a2 = "";
		
		if(scriptsAi1.get(scriptsAi1.size()-1) != a) {
		
			while(itr1.hasNext()) {
				a2 = itr2.next();
				if(itr1.next() == a){
					break;
				}
			}
				
			itr1.previous();
				
			a2 = itr2.next();
			a1 = itr1.next();

			itr2.set(a1);
			itr1.set(a2);

		}
	}
	
	public void downScriptAi2(String a) {
		ListIterator<String> itr1 = scriptsAi2.listIterator();
		ListIterator<String> itr2 = scriptsAi2.listIterator();
		String a1 = "", a2 = "";
		
		if(scriptsAi2.get(scriptsAi2.size()-1) != a) {
		
			while(itr1.hasNext()) {
				a2 = itr2.next();
				if(itr1.next() == a){
					break;
				}
			}
				
			itr1.previous();
				
			a2 = itr2.next();
			a1 = itr1.next();

			itr2.set(a1);
			itr1.set(a2);

		}
	}

	
}
