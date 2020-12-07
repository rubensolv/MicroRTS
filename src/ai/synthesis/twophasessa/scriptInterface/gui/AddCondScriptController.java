package ai.synthesis.twophasessa.scriptInterface.gui;

import com.jfoenix.controls.JFXRadioButton;

import ai.synthesis.twophasessa.scriptInterface.InterfaceSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class AddCondScriptController {
	
	//private boolean fecharJanela = false;
	private VisualScriptInterfaceController principalController;
	private AddScriptPlusController addPlusController;

    //Ally Conditional
    
    @FXML
    private JFXRadioButton rbHaveQtdUnitsbyType;
    @FXML
    private JFXRadioButton rbHaveQtdUnitsHarversting;
    @FXML
    private JFXRadioButton rbHaveUnitsStrongest;
    @FXML
    private JFXRadioButton rbHaveUnitsinEnemyRange;
    @FXML
    private JFXRadioButton rbHaveUnitsToDistantToEnemy;
    @FXML
    private JFXRadioButton rbIsPlayerInPosition;
    @FXML
    private ToggleGroup groupConditionalAllies;

    //Ally Type

    @FXML
    private ToggleButton tbCondAllyWorker;
    @FXML
    private ToggleButton tbCondAllyLight;
    @FXML
    private ToggleButton tbCondAllyHeavy;
    @FXML
    private ToggleButton tbCondAllyRanged;
    @FXML
    private ToggleButton tbCondAllyAll;
    @FXML
    private ToggleGroup groupCondAllyTypes;
    
    //Ally Direction

    @FXML
    private ToggleButton tbCondAllyRight;
    @FXML
    private ToggleButton tbCondAllyLeft;
    @FXML
    private ToggleButton tbCondAllyUp;
    @FXML
    private ToggleButton tbCondAllyDown;
    @FXML
    private ToggleGroup groupCondAllyDirections;

    //Ally Quantity
    
    @FXML
    private TextField edtCondAllyQnt;
    @FXML
    private Text txtQntDistAlly;
    
    //Enemy Conditional
    
    @FXML
    private JFXRadioButton rbHaveQtdEnemiesbyType;
    @FXML
    private JFXRadioButton rbHaveQtdUnitsAttacking;
    @FXML
    private JFXRadioButton rbHaveEnemiesStrongest;
    @FXML
    private JFXRadioButton rbHaveEnemiesinUnitsRange;
    @FXML
    private ToggleGroup groupConditionalEnemies;
    
    //Enemy Type

    @FXML
    private ToggleButton tbCondEnemyWorker;
    @FXML
    private ToggleButton tbCondEnemyLight;
    @FXML
    private ToggleButton tbCondEnemyHeavy;
    @FXML
    private ToggleButton tbCondEnemyRanged;
    @FXML
    private ToggleButton tbCondEnemyAll;
    @FXML
    private ToggleGroup groupCondEnemyTypes;

    //Enemy Quantity
    
    @FXML
    private TextField edtCondEnemyQnt;
    @FXML
    private Text txtQntDistEnemy;
    
    // Alerts
    @FXML
    private Text txtAlertEnemies;
    @FXML
    private Text txtAlertAllies;
    
    
    
    public void init(VisualScriptInterfaceController m) {
    	principalController = m;
    }
    
    public void initp(AddScriptPlusController m) {
    	addPlusController = m;
    }

    @FXML
    void verifyParameters(ActionEvent event) {
    	
    	//define quais estruturas devem ficar ativadas ou n�o
    	//aba Allies
    	if(groupConditionalAllies.getSelectedToggle() != null) {

	        if(rbHaveQtdUnitsbyType.isSelected()) {
	       		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		txtQntDistAlly.setText("Quantity");
	       		edtCondAllyQnt.setDisable(false);
	       		tbCondAllyRight.setDisable(true);
	       	    tbCondAllyLeft.setDisable(true);
	       	    tbCondAllyUp.setDisable(true);
	       	    tbCondAllyDown.setDisable(true);
	       		
        	}else if(rbHaveQtdUnitsHarversting.isSelected()) {
	        	tbCondAllyWorker.setDisable(true);
	       		tbCondAllyLight.setDisable(true);
	      		tbCondAllyHeavy.setDisable(true);
	       		tbCondAllyRanged.setDisable(true);
	       		tbCondAllyAll.setDisable(true);
	       		txtQntDistAlly.setText("Quantity");
	       		edtCondAllyQnt.setDisable(false);
	       		tbCondAllyRight.setDisable(true);
	       	    tbCondAllyLeft.setDisable(true);
	       	    tbCondAllyUp.setDisable(true);
	       	    tbCondAllyDown.setDisable(true);
	       		
        	}else if(rbHaveUnitsStrongest.isSelected()) {
	      		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		edtCondAllyQnt.setDisable(true);
	       		tbCondAllyRight.setDisable(true);
	       	    tbCondAllyLeft.setDisable(true);
	       	    tbCondAllyUp.setDisable(true);
	       	    tbCondAllyDown.setDisable(true);
	        		
	       	}else if(rbHaveUnitsinEnemyRange.isSelected()) {
	       		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		edtCondAllyQnt.setDisable(true);
	       		tbCondAllyRight.setDisable(true);
	       	    tbCondAllyLeft.setDisable(true);
	       	    tbCondAllyUp.setDisable(true);
	       	    tbCondAllyDown.setDisable(true);
	       		
	       	}else if(rbHaveUnitsToDistantToEnemy.isSelected()) {
	       		tbCondAllyWorker.setDisable(false);
	       		tbCondAllyLight.setDisable(false);
	       		tbCondAllyHeavy.setDisable(false);
	       		tbCondAllyRanged.setDisable(false);
	       		tbCondAllyAll.setDisable(false);
	       		txtQntDistAlly.setText("Distance");
	       		edtCondAllyQnt.setDisable(false);
	       		tbCondAllyRight.setDisable(true);
	       	    tbCondAllyLeft.setDisable(true);
	       	    tbCondAllyUp.setDisable(true);
	       	    tbCondAllyDown.setDisable(true);
	        		
	       	}else if(rbHaveQtdUnitsAttacking.isSelected()) {
        		tbCondAllyWorker.setDisable(false);
        		tbCondAllyLight.setDisable(false);
        		tbCondAllyHeavy.setDisable(false);
        		tbCondAllyRanged.setDisable(false);
        		tbCondAllyAll.setDisable(false);
        		txtQntDistAlly.setText("Quantity");
	       		edtCondAllyQnt.setDisable(false);
	       		tbCondAllyRight.setDisable(true);
	       	    tbCondAllyLeft.setDisable(true);
	       	    tbCondAllyUp.setDisable(true);
	       	    tbCondAllyDown.setDisable(true);
	       	    
	       	}else if(rbIsPlayerInPosition.isSelected()) {
        		tbCondAllyWorker.setDisable(true);
        		tbCondAllyLight.setDisable(true);
        		tbCondAllyHeavy.setDisable(true);
        		tbCondAllyRanged.setDisable(true);
        		tbCondAllyAll.setDisable(true);
	       		edtCondAllyQnt.setDisable(true);
	       		tbCondAllyRight.setDisable(false);
	       	    tbCondAllyLeft.setDisable(false);
	       	    tbCondAllyUp.setDisable(false);
	       	    tbCondAllyDown.setDisable(false);
	       	}
    	}
    	
    	//aba Enemies
    	if(groupConditionalEnemies.getSelectedToggle() != null) {

	        if(rbHaveQtdEnemiesbyType.isSelected()) {
	        	tbCondEnemyWorker.setDisable(false);
	        	tbCondEnemyLight.setDisable(false);
	        	tbCondEnemyHeavy.setDisable(false);
	        	tbCondEnemyRanged.setDisable(false);
	        	tbCondEnemyAll.setDisable(false);
	        	txtQntDistEnemy.setText("Quantity");
	       		edtCondEnemyQnt.setDisable(false);
	       		
        	}else if(rbHaveEnemiesStrongest.isSelected()) {
        		tbCondEnemyWorker.setDisable(false);
        		tbCondEnemyLight.setDisable(false);
        		tbCondEnemyHeavy.setDisable(false);
        		tbCondEnemyRanged.setDisable(false);
        		tbCondEnemyAll.setDisable(false);
        		edtCondEnemyQnt.setDisable(true);
	        		
	       	}else if(rbHaveEnemiesinUnitsRange.isSelected()) {
	       		tbCondEnemyWorker.setDisable(false);
	       		tbCondEnemyLight.setDisable(false);
	       		tbCondEnemyHeavy.setDisable(false);
	       		tbCondEnemyRanged.setDisable(false);
	       		tbCondEnemyAll.setDisable(false);
	       		edtCondEnemyQnt.setDisable(true);
	        		
	       	}
    	}
    	
    }
    
    @FXML
    void clickBtnAddCondAllies(ActionEvent event) {
    	principalController.checkSelectedTab();
    	String s = "if(";
    	boolean validCommand = false;
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
    		
    		//HaveQtdUnitsbyType
    		if( rbHaveQtdUnitsbyType.isSelected() && groupCondAllyTypes.getSelectedToggle() != null && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdUnitsbyType(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdUnitsbyType.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    			
    		}
    		
    		//HaveQtdUnitsHarversting
    		if( rbHaveQtdUnitsHarversting.isSelected() && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdUnitsHarversting(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    	    		
    	    		//Quantity
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdUnitsHarversting.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveUnitsStrongest
    		if( rbHaveUnitsStrongest.isSelected() && groupCondAllyTypes.getSelectedToggle() != null) {
    			validCommand = true;
    			s += "HaveUnitsStrongest(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveUnitsStrongest.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveUnitsinEnemyRange
    		if( rbHaveUnitsinEnemyRange.isSelected() && groupCondAllyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveUnitsinEnemyRange(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {

    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveUnitsinEnemyRange.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveUnitsToDistantToEnemy
    		if( rbHaveUnitsToDistantToEnemy.isSelected() && groupCondAllyTypes.getSelectedToggle() != null && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveUnitsToDistantToEnemy(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveUnitsToDistantToEnemy.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveQtdUnitsAttacking
    		if( rbHaveQtdUnitsAttacking.isSelected() && groupCondAllyTypes.getSelectedToggle() != null && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdUnitsAttacking(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";

    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdUnitsAttacking.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//IsPlayerInPosition
    		if( rbIsPlayerInPosition.isSelected() && groupCondAllyDirections.getSelectedToggle() != null) {
    			validCommand = true;
    			s += "IsPlayerInPosition(";
    			
    			if(groupCondAllyDirections.getSelectedToggle() != null) {
    				
    				//Directions
    	    		if(tbCondAllyRight.isSelected())
        				s += "Right)";
        			else if(tbCondAllyLeft.isSelected())
        				s += "Left)";
        			else if(tbCondAllyUp.isSelected())
        				s += "Up)";
        			else if(tbCondAllyDown.isSelected())
        				s += "Down)";

    	    		//String provisória, apagar depois
    	    		s += ")";

    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbIsPlayerInPosition.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//Atualização das listas
    		if(validCommand == true) {
				if(addPlusController != null) {
					addPlusController.addListViewFuncList(s);
				} else if(principalController != null){
					InterfaceSettings.getInstance().addScriptAI1(s);
					principalController.attListViewAI1();
				}
    		}
    		
    	}else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
    		//HaveQtdUnitsbyType
    		if( rbHaveQtdUnitsbyType.isSelected() && groupCondAllyTypes.getSelectedToggle() != null && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdUnitsbyType(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdUnitsbyType.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveQtdUnitsHarversting
    		if( rbHaveQtdUnitsHarversting.isSelected() && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdUnitsHarversting(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    	    		
    	    		//Quantity
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdUnitsHarversting.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveUnitsStrongest
    		if( rbHaveUnitsStrongest.isSelected() && groupCondAllyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveUnitsStrongest(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveUnitsStrongest.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveUnitsinEnemyRange
    		if( rbHaveUnitsinEnemyRange.isSelected() && groupCondAllyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveUnitsinEnemyRange(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {

    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light)";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondAllyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveUnitsinEnemyRange.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveUnitsToDistantToEnemy
    		if( rbHaveUnitsToDistantToEnemy.isSelected() && groupCondAllyTypes.getSelectedToggle() != null && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveUnitsToDistantToEnemy(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveUnitsToDistantToEnemy.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//HaveQtdUnitsAttacking
    		if( rbHaveQtdUnitsAttacking.isSelected() && groupCondAllyTypes.getSelectedToggle() != null && edtCondAllyQnt.getText() != null && !edtCondAllyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdUnitsAttacking(";
    			
    			if(groupConditionalAllies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondAllyQnt.getText());
    				
    				//Type
    	    		if(tbCondAllyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondAllyLight.isSelected())
        				s += "Light,";
        			else if(tbCondAllyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondAllyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondAllyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdUnitsAttacking.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//IsPlayerInPosition
    		if( rbIsPlayerInPosition.isSelected() && groupCondAllyDirections.getSelectedToggle() != null) {
    			validCommand = true;
    			s += "IsPlayerInPosition(";
    			
    			if(groupCondAllyDirections.getSelectedToggle() != null) {
    				
    				//Directions
    	    		if(tbCondAllyRight.isSelected())
        				s += "Right)";
        			else if(tbCondAllyLeft.isSelected())
        				s += "Left)";
        			else if(tbCondAllyUp.isSelected())
        				s += "Up)";
        			else if(tbCondAllyDown.isSelected())
        				s += "Down)";

    	    		//String provisória, apagar depois
    	    		s += ")";

    			}
    			
    			txtAlertAllies.setOpacity(0.0);
    		} else {
    			if( rbIsPlayerInPosition.isSelected() )
    				txtAlertAllies.setOpacity(1.0);
    		}
    		
    		//Atualização das listas
    		if(validCommand == true) {
				if(addPlusController != null) {
					addPlusController.addListViewFuncList(s);
				} else if(principalController != null){
					InterfaceSettings.getInstance().addScriptAI2(s);
					principalController.attListViewAI2();
				}
    		}
    	}
    }
    
    @FXML
    void clickBtnAddCondEnemies(ActionEvent event) {
    	principalController.checkSelectedTab();
    	String s = "if(";
    	boolean validCommand = false;
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
    		
    		//HaveQtdEnemiesbyType
    		if( rbHaveQtdEnemiesbyType.isSelected() && groupCondEnemyTypes.getSelectedToggle() != null && edtCondEnemyQnt.getText() != null && !edtCondEnemyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdEnemiesbyType(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondEnemyQnt.getText());
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provisória, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertEnemies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdEnemiesbyType.isSelected() )
    				txtAlertEnemies.setOpacity(1.0);
    		}
    		
    		//HaveEnemiesStrongest
    		if( rbHaveEnemiesStrongest.isSelected() && groupCondEnemyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveEnemiesStrongest(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertEnemies.setOpacity(0.0);
    		} else {
    			if( rbHaveEnemiesStrongest.isSelected() )
    				txtAlertEnemies.setOpacity(1.0);
    		}
    		
    		//HaveEnemiesinUnitsRange
    		if( rbHaveEnemiesinUnitsRange.isSelected() && groupCondEnemyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveEnemiesinUnitsRange(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertEnemies.setOpacity(0.0);
    		} else {
    			if( rbHaveEnemiesinUnitsRange.isSelected() )
    				txtAlertEnemies.setOpacity(1.0);
    		}
    		
    		//Atualização das listas
    		if(validCommand == true) {
				if(addPlusController != null) {
					addPlusController.addListViewFuncList(s);
				} else if(principalController != null){
					InterfaceSettings.getInstance().addScriptAI1(s);
					principalController.attListViewAI1();
				}
    		}
    		
    	}else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
    		
    		//HaveQtdEnemiesbyType
    		if( rbHaveQtdEnemiesbyType.isSelected() && groupCondEnemyTypes.getSelectedToggle() != null && edtCondEnemyQnt.getText() != null && !edtCondEnemyQnt.getText().trim().isEmpty() ) {
    			validCommand = true;
    			s += "HaveQtdEnemiesbyType(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				Integer q = Integer.parseInt(edtCondEnemyQnt.getText());
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker,";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light,";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy,";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged,";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All,";
    	    		
    	    		//Quantity or Distance
    	    		s += Integer.toString(q) + ")";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertEnemies.setOpacity(0.0);
    		} else {
    			if( rbHaveQtdEnemiesbyType.isSelected() )
    				txtAlertEnemies.setOpacity(1.0);
    		}
    		
    		//HaveEnemiesStrongest
    		if( rbHaveEnemiesStrongest.isSelected() && groupCondEnemyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveEnemiesStrongest(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertEnemies.setOpacity(0.0);
    		} else {
    			if( rbHaveEnemiesStrongest.isSelected() )
    				txtAlertEnemies.setOpacity(1.0);
    		}
    		
    		//HaveEnemiesinUnitsRange
    		if( rbHaveEnemiesinUnitsRange.isSelected() && groupCondEnemyTypes.getSelectedToggle() != null ) {
    			validCommand = true;
    			s += "HaveEnemiesinUnitsRange(";
    			
    			if(groupConditionalEnemies.getSelectedToggle() != null) {
    				
    				//Type
    	    		if(tbCondEnemyWorker.isSelected())
        				s += "Worker)";
        			else if(tbCondEnemyLight.isSelected())
        				s += "Light)";
        			else if(tbCondEnemyHeavy.isSelected())
        				s += "Heavy)";
        			else if(tbCondEnemyRanged.isSelected())
        				s += "Ranged)";
        			else if(tbCondEnemyAll.isSelected())
        				s += "All)";
    	    		
    	    		//String provis�ria, apagar depois
    	    		s += ")";
    			}
    			
    			txtAlertEnemies.setOpacity(0.0);
    		} else {
    			if( rbHaveEnemiesinUnitsRange.isSelected() )
    				txtAlertEnemies.setOpacity(1.0);
    		}
    		
    		//Atualização das listas
    		if(validCommand == true) {
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    		}
    		
    	}
    	
    }
    
    

}
