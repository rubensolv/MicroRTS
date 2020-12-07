package ai.synthesis.twophasessa.scriptInterface.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SendQuestionnaire1LoadScriptController {
	
	int map;
	private SendQuestionnaire1Controller main;

    @FXML
    private JFXTextArea txtChosenScript;

    @FXML
    private Text txtMapName;

    @FXML
    private TextField txtSearch;
    
    @FXML
    private ListView<String> lvScripts = new ListView<>();

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnLoadScript;
    
    public void initialize(SendQuestionnaire1Controller m, int map) {
    	this.main = m;
    	setMapName(map);
    	
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
    
    @FXML
    void clickSelectMap(MouseEvent event) throws IOException {
    	if(lvScripts.getSelectionModel().getSelectedItem() != null) {
	    	String fname;
	    	fname = lvScripts.getSelectionModel().getSelectedItem();
	    	
	    	File file = new File("savedScripts/" + fname);
	    	BufferedReader br = new BufferedReader(new FileReader(file)); 
	    	
	    	String st;
	    	String script = "";
		    while ((st = br.readLine()) != null) {
		    	script += st + " \n";
		    }
		    txtChosenScript.setText(script);
	    	
    	}
    }

    @FXML
    void loadSelectedScript(ActionEvent event) {
    	if(this.map == 1)
    		this.main.setScript1(txtChosenScript.getText());
    	else if(this.map == 2)
    		this.main.setScript2(txtChosenScript.getText());
    	else if(this.map == 3)
    		this.main.setScript3(txtChosenScript.getText());
    	else System.out.println("deu merda");
    	
    	Stage stage = (Stage) txtChosenScript.getScene().getWindow();
    	stage.hide();
    }
    
    public void setMapName(int id) {
    	this.map = id;
    	if(id == 1) 
    		txtMapName.setText("Map 1: " + this.main.getMapName1());
    	else if(id == 2) 
    		txtMapName.setText("Map 2: " + this.main.getMapName2());
    	else if(id == 3) 
    		txtMapName.setText("Map 2: " + this.main.getMapName3());
    	
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
