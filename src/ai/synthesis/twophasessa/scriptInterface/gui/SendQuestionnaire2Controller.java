package ai.synthesis.twophasessa.scriptInterface.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SendQuestionnaire2Controller {

    @FXML
    private ComboBox<String> cbQ1;
    private List<String> optionsQ1 = new ArrayList<>();
    private ObservableList<String> obsCbQ1;

    @FXML
    private JFXTextArea txtQ2;

    @FXML
    private ComboBox<String> cbQ3;
    private List<String> optionsQ3 = new ArrayList<>();
    private ObservableList<String> obsCbQ3;

    @FXML
    private JFXTextArea txtQ4;

    @FXML
    private ComboBox<String> cbQ5;
    private List<String> optionsQ5 = new ArrayList<>();
    private ObservableList<String> obsCbQ5;

    @FXML
    private JFXTextArea txtQ6;

    @FXML
    private ToggleGroup groupQ7;
    @FXML
    private RadioButton rb7Scale1;
    @FXML
    private RadioButton rb7Scale2;
    @FXML
    private RadioButton rb7Scale3;
    @FXML
    private RadioButton rb7Scale4;
    @FXML
    private RadioButton rb7Scale5;

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSubmit;
    
    // Inicialização da tela
 	public void initialize() {
 		loadCBQ1();
 		loadCBQ3();
 		loadCBQ5();
 	}
 	
 	private void loadCBQ1() {
 		String op1 = "Yes";
		String op2 = "No";
		String op3 = "They mostly draw";
		
		optionsQ1.add(op1);
		optionsQ1.add(op2);
		optionsQ1.add(op3);
		
		obsCbQ1 = FXCollections.observableArrayList(optionsQ1);
		cbQ1.setItems(obsCbQ1);
 	}
 	
 	private void loadCBQ3() {
 		String op1 = "Yes";
		String op2 = "No";
		
		optionsQ3.add(op1);
		optionsQ3.add(op2);
		
		obsCbQ3 = FXCollections.observableArrayList(optionsQ3);
		cbQ3.setItems(obsCbQ3);
 	}

 	private void loadCBQ5() {
 		String op1 = "Yes";
		String op2 = "No";
		
		optionsQ5.add(op1);
		optionsQ5.add(op2);
		
		obsCbQ5 = FXCollections.observableArrayList(optionsQ5);
		cbQ5.setItems(obsCbQ5);
	}

    @FXML
    void clickBtnCancel(ActionEvent event) {
    	Stage stage = (Stage) btnCancel.getScene().getWindow();
    	stage.hide();
    }

    @FXML
    void clickBtnClear(ActionEvent event) {
    	cbQ1.valueProperty().set(null);
    	txtQ2.clear();
    	cbQ3.valueProperty().set(null);
    	txtQ4.clear();
    	cbQ5.valueProperty().set(null);
    	txtQ6.clear();
    	groupQ7.getSelectedToggle().setSelected(false);
    }

    @FXML
    void clickBtnSubmit(ActionEvent event) throws IOException {
    	// TESTE    -- Enviar esses scripts e respostas no WebService
    	String txtQ7 = null;
    	if(groupQ7.getSelectedToggle() != null) {
    		if(rb7Scale1.isSelected()) txtQ7 = "Absolutely Agree";
    		else if(rb7Scale2.isSelected()) txtQ7 = "Agree";
    		else if(rb7Scale3.isSelected()) txtQ7 = "Indifferent";
    		else if(rb7Scale4.isSelected()) txtQ7 = "Disagree";
    		else if(rb7Scale5.isSelected()) txtQ7 = "Absolutely Disagree";
    	}
    	
    	System.out.println("Resposta Q1: " + cbQ1.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q2: " + txtQ2.getText());
    	System.out.println("Resposta Q3: " + cbQ3.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q4: " + txtQ4.getText());
    	System.out.println("Resposta Q5: " + cbQ5.getSelectionModel().getSelectedItem());
    	System.out.println("Resposta Q6: " + txtQ6.getText());
    	System.out.println("Resposta Q6: " + txtQ7);
    	
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

}
