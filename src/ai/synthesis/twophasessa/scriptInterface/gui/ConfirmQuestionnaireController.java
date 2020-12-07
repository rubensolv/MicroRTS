package ai.synthesis.twophasessa.scriptInterface.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmQuestionnaireController {

    @FXML
    private Button btnOk;

    @FXML
    void clickBtnOk(ActionEvent event) {
    	Stage stage = (Stage) btnOk.getScene().getWindow();
    	stage.hide();
    }

}