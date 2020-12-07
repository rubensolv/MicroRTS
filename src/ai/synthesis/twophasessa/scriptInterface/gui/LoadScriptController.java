package ai.synthesis.twophasessa.scriptInterface.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ai.synthesis.twophasessa.scriptInterface.InterfaceSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoadScriptController implements Initializable{

	//private VisualScriptInterfaceController principalController;
    @FXML
    private TextField txtSearch;
    @FXML
    private ListView<String> lvScripts = new ListView<>();

    //public void init(VisualScriptInterfaceController m) {
    //	principalController = m;
    //}
    
    @FXML
    void loadSelectedScript(ActionEvent event) throws IOException {
    	if(lvScripts.getSelectionModel().getSelectedItem() != null) {
	    	String fname;
	    	fname = lvScripts.getSelectionModel().getSelectedItem();
	    	
	    	File file = new File("savedScripts/" + fname);
	    	BufferedReader br = new BufferedReader(new FileReader(file)); 
	    	
	    	String st; 
	    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
	    		InterfaceSettings.getInstance().clearScriptsAI1();
		    	while ((st = br.readLine()) != null) {
		    		InterfaceSettings.getInstance().addScriptAI1(st);
		    	}
	    	} else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
	    		InterfaceSettings.getInstance().clearScriptsAI2();
		    	while ((st = br.readLine()) != null) {
		    		InterfaceSettings.getInstance().addScriptAI2(st);
		    	}
	    	}
	    	
	    	br.close();
	
	    	Stage stage = (Stage) lvScripts.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    public void initialize(URL location, ResourceBundle resources) {
		loadScripts();
	}

    @FXML
    void clickBtnSearch(ActionEvent event) {
    	if(txtSearch.getText() != null && txtSearch.getText() != "") {
    		File dirPath = new File("savedScripts");
    		lvScripts.getItems().clear();
    		
    		for(String f: dirPath.list()) {
    			if(f.contains(txtSearch.getText())) {
    				lvScripts.getItems().addAll(f);
    				lvScripts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    			}
    		}
    		
    	} else {
    		loadScripts();
    	}
    }
    
    void loadScripts() {
    	File dirPath = new File("savedScripts");
		//System.out.println("Arquivos disponíveis:");
		lvScripts.getItems().clear();
		
		for(String f: dirPath.list()) {
			if(f.contains(".txt")) {
				lvScripts.getItems().addAll(f);
				lvScripts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				//System.out.println(f);
			}
		}
    }

}
