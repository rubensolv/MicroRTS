package ai.synthesis.twophasessa.scriptInterface.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SendQuestionnaire1Controller {
	
	String map1 = "8x8";
	String map2 = "18x8";
	String map3 = "16x16";
	
	String script1;
	String script2;
	String script3;

    @FXML
    private TextField txtQ1;
    @FXML
    private TextField txtQ2;
    @FXML
    private ComboBox<String> cbQ3;
    private List<String> optionsQ3 = new ArrayList<>();
    private ObservableList<String> obsCbQ3;
    @FXML
    private ComboBox<String> cbQ4;
    private List<String> optionsQ4 = new ArrayList<>();
    private ObservableList<String> obsCbQ4;
    @FXML
    private TextField txtQ5;
    @FXML
    private ComboBox<String> cbQ6;
    private List<String> optionsQ6 = new ArrayList<>();
    private ObservableList<String> obsCbQ6;
    @FXML
    private ComboBox<String> cbQ7;
    private List<String> optionsQ7 = new ArrayList<>();
    private ObservableList<String> obsCbQ7;
    @FXML
    private ComboBox<String> cbQ8;
    private List<String> optionsQ8 = new ArrayList<>();
    private ObservableList<String> obsCbQ8;
    @FXML
    private ComboBox<String> cbQ9;
    private List<String> optionsQ9 = new ArrayList<>();
    private ObservableList<String> obsCbQ9;
    @FXML
    private TextField txtQ10;
    @FXML
    private ToggleGroup groupQ11;
    @FXML
    private JFXTextArea txtQ12;
    
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSubmit;
    
    @FXML
    private RadioButton rb11Scale1;
    @FXML
    private RadioButton rb11Scale2;
    @FXML
    private RadioButton rb11Scale3;
    @FXML
    private RadioButton rb11Scale4;
    @FXML
    private RadioButton rb11Scale5;
    
    // Inicialização da tela
	public void initialize() {
		loadCBQ3();
		loadCBQ4();
		loadCBQ6();
		loadCBQ7();
		loadCBQ8();
		loadCBQ9();
	}
	
	private void loadCBQ3() {
		String op1 = "Female";
		String op2 = "Non-binary";
		String op3 = "Male";
		String op4 = "Other";
		
		optionsQ3.add(op1);
		optionsQ3.add(op2);
		optionsQ3.add(op3);
		optionsQ3.add(op4);
		
		obsCbQ3 = FXCollections.observableArrayList(optionsQ3);
		cbQ3.setItems(obsCbQ3);
	}
	
	private void loadCBQ4() {
		String op1 = "Yes";
		String op2 = "No";
		
		optionsQ4.add(op1);
		optionsQ4.add(op2);
		
		obsCbQ4 = FXCollections.observableArrayList(optionsQ4);
		cbQ4.setItems(obsCbQ4);
	}
	
	private void loadCBQ6() {
		String op1 = "0";
		String op2 = "1";
		String op3 = "2 or more";
		
		optionsQ6.add(op1);
		optionsQ6.add(op2);
		optionsQ6.add(op3);
		
		obsCbQ6 = FXCollections.observableArrayList(optionsQ6);
		cbQ6.setItems(obsCbQ6);
	}

	private void loadCBQ7() {
		String op1 = "Yes";
		String op2 = "No";
		
		optionsQ7.add(op1);
		optionsQ7.add(op2);
		
		obsCbQ7 = FXCollections.observableArrayList(optionsQ7);
		cbQ7.setItems(obsCbQ7);
	}
	
	private void loadCBQ8() {
		String op1 = "Never";
		String op2 = "Rarely";
		String op3 = "Occasionally";
		String op4 = "Often";
		
		optionsQ8.add(op1);
		optionsQ8.add(op2);
		optionsQ8.add(op3);
		optionsQ8.add(op4);
		
		obsCbQ8 = FXCollections.observableArrayList(optionsQ8);
		cbQ8.setItems(obsCbQ8);
	}
	
	private void loadCBQ9  () {
		String op1 = "Never";
		String op2 = "Once or twice";
		String op3 = "Played a lot, but that was a long time ago";
		String op4 = "I still occasionally play RTS games";
		String op5 = "I often play RTS games";
		
		optionsQ9.add(op1);
		optionsQ9.add(op2);
		optionsQ9.add(op3);
		optionsQ9.add(op4);
		optionsQ9.add(op5);
		
		obsCbQ9 = FXCollections.observableArrayList(optionsQ9);
		cbQ9.setItems(obsCbQ9);
	}
	
    @FXML
    void clickBtnCancel(ActionEvent event) {
    	Stage stage = (Stage) btnCancel.getScene().getWindow();
    	stage.hide();
    }

    @FXML
    void clickBtnClear(ActionEvent event) {
    	txtQ1.clear();
    	txtQ2.clear();
    	cbQ3.valueProperty().set(null);
    	cbQ4.valueProperty().set(null);
    	txtQ5.clear();
    	cbQ6.valueProperty().set(null);
    	cbQ7.valueProperty().set(null);
    	cbQ8.valueProperty().set(null);
    	cbQ9.valueProperty().set(null);
    	txtQ10.clear();
    	groupQ11.getSelectedToggle().setSelected(false);
    	txtQ12.clear();
    }

    @FXML
    void clickBtnMap1(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire1LoadScript.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		SendQuestionnaire1LoadScriptController sendQuestionnaire1LoadScriptController = fxmlLoader.getController();
		sendQuestionnaire1LoadScriptController.initialize(this,1);
		
		Stage stage = new Stage();
		stage.setTitle("Load Script: Map 1 " + map1);
		stage.setScene(new Scene(root1));
		stage.setHeight(530);
		stage.setWidth(904);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    }

    @FXML
    void clickBtnMap2(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire1LoadScript.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		SendQuestionnaire1LoadScriptController sendQuestionnaire1LoadScriptController = fxmlLoader.getController();
		sendQuestionnaire1LoadScriptController.initialize(this,2);
		
		Stage stage = new Stage();
		stage.setTitle("Load Script: Map 2 " + map2);
		stage.setScene(new Scene(root1));
		stage.setHeight(530);
		stage.setWidth(904);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    }

    @FXML
    void clickBtnMap3(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire1LoadScript.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		SendQuestionnaire1LoadScriptController sendQuestionnaire1LoadScriptController = fxmlLoader.getController();
		sendQuestionnaire1LoadScriptController.initialize(this,3);
		
		Stage stage = new Stage();
		stage.setTitle("Load Script: Map 3 " + map3);
		stage.setScene(new Scene(root1));
		stage.setHeight(530);
		stage.setWidth(904);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    }

    @FXML
    void clickBtnSubmit(ActionEvent event) throws IOException {
    	// TESTE    -- Enviar esses scripts e respostas no WebService
    	String txtQ11 = null;
    	if(groupQ11.getSelectedToggle() != null) {
    		if(rb11Scale1.isSelected()) txtQ11 = "Absolutely Agree";
    		else if(rb11Scale2.isSelected()) txtQ11 = "Agree";
    		else if(rb11Scale3.isSelected()) txtQ11 = "Indifferent";
    		else if(rb11Scale4.isSelected()) txtQ11 = "Disagree";
    		else if(rb11Scale5.isSelected()) txtQ11 = "Absolutely Disagree";
    	}
    	
    	System.out.println("Script 1: " + this.script1);
    	System.out.println("Script 2: " + this.script2);
    	System.out.println("Script 3: " + this.script3);
    	System.out.println("Resposta Q1: " + txtQ1.getText());
    	System.out.println("Resposta Q2: " + txtQ2.getText());
    	System.out.println("Resposta Q3: " + cbQ3.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q4: " + cbQ4.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q5: " + txtQ5.getText());
    	System.out.println("Resposta Q6: " + cbQ6.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q7: " + cbQ7.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q8: " + cbQ8.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q9: " + cbQ9.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q10: " + txtQ10.getText());
    	System.out.println("Resposta Q11: " + txtQ11);
    	System.out.println("Resposta Q12: " + txtQ12.getText());
    	
    	//ConfirmQuestionnaireController
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfirmQuestionnaire.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		ConfirmQuestionnaireController confirmQuestionnaireController = fxmlLoader.getController();
		//confirmQuestionnaireController.initialize(this,3);
		
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setHeight(200);
		stage.setWidth(400);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    	
    	Stage st = (Stage) btnSubmit.getScene().getWindow();
    	st.hide();
    }
    
    public String getMapName1() {
    	return this.map1;
    }
    
    public String getMapName2() {
    	return this.map2;
    }
    
    public String getMapName3() {
    	return this.map3;
    }
    
    public void setScript1(String s) {
    	this.script1 = s;
    }
    
    public void setScript2(String s) {
    	this.script2 = s;
    }
    
    public void setScript3(String s) {
    	this.script3 = s;
    }

}
