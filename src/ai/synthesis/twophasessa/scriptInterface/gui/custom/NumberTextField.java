package ai.synthesis.twophasessa.scriptInterface.gui.custom;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

	public NumberTextField() {
		this.setPromptText("Only number");
	}
	
	public void replaceText(int i, int i1, String string) {
		if( string.matches("[0-9]") || string.isEmpty() ) {
			super.replaceText(i, i1, string);
		}
	}
	
	public void replaceSelection(String string) {
		super.replaceSelection(string);
	}
	
}
