package ai.synthesis.twophasessa.scriptInterface.gui;

import java.io.IOException;

import ai.synthesis.twophasessa.scriptInterface.InterfaceSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddScriptController {
	
	private boolean fecharJanela = false;
	private VisualScriptInterfaceController principalController;
	private AddScriptPlusController addPlusController;
	
	// Build
	
    @FXML
    private ToggleButton tbBase;
    @FXML
    private ToggleButton tbBarrack;
    @FXML
    private ToggleButton tbBuildRight;
    @FXML
    private ToggleButton tbBuildLeft;
    @FXML
    private ToggleButton tbBuildUp;
    @FXML
    private ToggleButton tbBuildDown;
    @FXML
    private TextField edtBuildQnt;
    @FXML
    private ToggleGroup groupBuildTypes;
    @FXML
    private ToggleGroup groupBuildDir;
    @FXML
    private Button closeAddBuild;
    
    // Train
    
    @FXML
    private ToggleButton tbTrainWorker;
    @FXML
    private ToggleButton tbTrainLight;
    @FXML
    private ToggleButton tbTrainHeavy;
    @FXML
    private ToggleButton tbTrainRanged;
    @FXML
    private TextField edtTrainQnt;
    @FXML
    private ToggleButton tbTrainRight;
    @FXML
    private ToggleButton tbTrainLeft;
    @FXML
    private ToggleButton tbTrainUp;
    @FXML
    private ToggleButton tbTrainDown;
    @FXML
    private ToggleButton tbTrainEnemyDir;
    @FXML
    private ToggleGroup groupTrainTypes;
    @FXML
    private ToggleGroup groupTrainDir;
    @FXML
    private Button closeAddTrain;

    // Harvest
    
    @FXML
    private TextField edtHarvestQnt;
    @FXML
    private Button closeAddHarvest;
    
    // Attack
 
    @FXML
    private ToggleButton tbAttackWorker;
    @FXML
    private ToggleButton tbAttackLight;
    @FXML
    private ToggleButton tbAttackHeavy;
    @FXML
    private ToggleButton tbAttackRanged;
    @FXML
    private ToggleButton tbAttackAll;
    @FXML
    private ToggleButton tbAttackStrongest;
    @FXML
    private ToggleButton tbAttackWeakest;
    @FXML
    private ToggleButton tbAttackClosest;
    @FXML
    private ToggleButton tbAttackFarthest;
    @FXML
    private ToggleButton tbAttackLessHealthy;
    @FXML
    private ToggleButton tbAttackMostHealthy;
    @FXML
    private ToggleButton tbAttackRandom;
    @FXML
    private ToggleGroup groupAttackTypes;
    @FXML
    private ToggleGroup groupAttackEnemy;
    @FXML
    private Button closeAddAttack;
    
    //Move to Unit
    
    @FXML
    private ToggleButton tbMoveToUnitWorker;
    @FXML
    private ToggleButton tbMoveToUnitLight;
    @FXML
    private ToggleButton tbMoveToUnitHeavy;
    @FXML
    private ToggleButton tbMoveToUnitRanged;
    @FXML
    private ToggleButton tbMoveToUnitAll;
    @FXML
    private ToggleButton tbMoveToUnitAlly;
    @FXML
    private ToggleButton tbMoveToUnitEnemy;
    @FXML
    private ToggleButton tbMoveToUnitStrongest;
    @FXML
    private ToggleButton tbMoveToUnitWeakest;
    @FXML
    private ToggleButton tbMoveToUnitClosest;
    @FXML
    private ToggleButton tbMoveToUnitFarthest;
    @FXML
    private ToggleButton tbMoveToUnitLessHealthy;
    @FXML
    private ToggleButton tbMoveToUnitMostHealthy;
    @FXML
    private ToggleButton tbMoveToUnitRandom;
    @FXML
    private ToggleGroup groupMoveToUnitTypes;
    @FXML
    private ToggleGroup groupMoveToUnitTargets;
    @FXML
    private ToggleGroup groupMoveToUnitBehaviour;
    
    // Move Away
    
    @FXML
    private ToggleButton tbMoveAwayWorker;
    @FXML
    private ToggleButton tbMoveAwayLight;
    @FXML
    private ToggleButton tbMoveAwayHeavy;
    @FXML
    private ToggleButton tbMoveAwayRanged;
    @FXML
    private ToggleButton tbMoveAwayAll;
    @FXML
    private ToggleGroup groupMoveAwayTypes;
    
    // Move To Coordinates
    
    @FXML
    private ToggleButton tbMoveToCoordWorker;
    @FXML
    private ToggleButton tbMoveToCoordLight;
    @FXML
    private ToggleButton tbMoveToCoordHeavy;
    @FXML
    private ToggleButton tbMoveToCoordRanged;
    @FXML
    private TextField edtMoveToCoordX;
    @FXML
    private TextField edtMoveToCoordY;
    @FXML
    private ToggleGroup groupMoveToCoordTypes;
    
    // Move Once To Coordinates
    
    @FXML
    private ToggleButton tbMoveOnceToCoordWorker;
    @FXML
    private ToggleButton tbMoveOnceToCoordLight;
    @FXML
    private ToggleButton tbMoveOnceToCoordHeavy;
    @FXML
    private ToggleButton tbMoveOnceToCoordRanged;
    @FXML
    private TextField edtMoveOnceToCoordQnt;
    @FXML
    private TextField edtMoveOnceToCoordX;
    @FXML
    private TextField edtMoveOnceToCoordY;
    @FXML
    private Button closeAddMoveOnceToCoord;
    @FXML
    private ToggleGroup groupMoveOnceToCoordTypes;
    
    // Alerts
    
    @FXML
    private Text txtAlertBuild;
    @FXML
    private Text txtAlertTrain;
    @FXML
    private Text txtAlertHarvest;
    @FXML
    private Text txtAlertAttack;
    @FXML
    private Text txtAlertMoveToUnit;
    @FXML
    private Text txtAlertMoveAway;
    @FXML
    private Button closeAddMoveToCoord;
    @FXML
    private Text txtAlertMoveToCoord;
    @FXML
    private Text txtAlertMoveOnceToCoord;
    
    
    public void init(VisualScriptInterfaceController m) {
    	principalController = m;
    }
    
    public void initp(AddScriptPlusController m) {
    	addPlusController = m;
    }


    // Attack
    @FXML
    void clickBtnAttackAdd(ActionEvent event) throws IOException {
    	principalController.checkSelectedTab();
    	
    	String s = "";
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
    		if(groupAttackTypes.getSelectedToggle() != null && groupAttackEnemy.getSelectedToggle() != null) {
    			if(tbAttackWorker.isSelected())
    				s += "attack(Worker,";
    			else if(tbAttackLight.isSelected())
    				s += "attack(Light,";
    			else if(tbAttackHeavy.isSelected())
    				s += "attack(Heavy,";
    			else if(tbAttackRanged.isSelected())
    				s += "attack(Ranged,";
    			else if(tbAttackAll.isSelected())
    				s += "attack(All,";

    			if(tbAttackStrongest.isSelected())
    				s += "strongest)"; 
    			else if(tbAttackWeakest.isSelected())
    				s += "weakest)"; 
    			else if(tbAttackClosest.isSelected())
    				s += "closest)"; 
    			else if(tbAttackFarthest.isSelected())
    				s += "farthest)"; 
    			else if(tbAttackLessHealthy.isSelected())
    				s += "lessHealthy)"; 
    			else if(tbAttackMostHealthy.isSelected())
    				s += "mostHealthy)"; 
    			else if(tbAttackRandom.isSelected())
    				s += "random)"; 
    		
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			txtAlertAttack.setOpacity(0.0);
    		} else {
    			System.out.println("Faltam parâmetros!!");
    			txtAlertAttack.setOpacity(1.0);
    		}
    		
    	} else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
    		if(groupAttackTypes.getSelectedToggle() != null && groupAttackEnemy.getSelectedToggle() != null) {
    			if(tbAttackWorker.isSelected())
    				s += "attack(Worker,";
    			else if(tbAttackLight.isSelected())
    				s += "attack(Light,";
    			else if(tbAttackHeavy.isSelected())
    				s += "attack(Heavy,";
    			else if(tbAttackRanged.isSelected())
    				s += "attack(Ranged,";
    			else if(tbAttackAll.isSelected())
    				s += "attack(All,";
    			
    			if(tbAttackStrongest.isSelected())
    				s += "strongest)"; 
    			else if(tbAttackWeakest.isSelected())
    				s += "weakest)"; 
    			else if(tbAttackClosest.isSelected())
    				s += "closest)"; 
    			else if(tbAttackFarthest.isSelected())
    				s += "farthest)"; 
    			else if(tbAttackLessHealthy.isSelected())
    				s += "lessHealthy)"; 
    			else if(tbAttackMostHealthy.isSelected())
    				s += "mostHealthy)"; 
    			else if(tbAttackRandom.isSelected())
    				s += "random)"; 
    		
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			txtAlertAttack.setOpacity(0.0);
    		} else {
    			System.out.println("Faltam parâmetros!!");
    			txtAlertAttack.setOpacity(1.0);
    		}
    	}

    	if(fecharJanela) {
    		Stage stage = (Stage) closeAddAttack.getScene().getWindow();
        	stage.hide();
    	}
    	
    }

    //Build
    @FXML
    void clickBtnBuildAdd(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1){
    		
			String s = "";
			
    		if(groupBuildTypes.getSelectedToggle() != null && groupBuildDir.getSelectedToggle() != null && edtBuildQnt.getText() != null && !edtBuildQnt.getText().trim().isEmpty() && Integer.parseInt(edtBuildQnt.getText()) != 0) {
    			Integer q = Integer.parseInt(edtBuildQnt.getText());
    			
    			if(tbBase.isSelected())
    				s = "build(Base,";// + Integer.toString(q) + ")";
    			if(tbBarrack.isSelected())
    				s = "build(Barrack,";// + Integer.toString(q) + ")";
    			
    			s += Integer.toString(q) + ",";
    			
    			if(tbBuildRight.isSelected())
    				s += "Right)";
    			else if(tbBuildLeft.isSelected())
    				s += "Left)";
    			else if(tbBuildUp.isSelected())
    				s += "Up)";
    			if(tbBuildDown.isSelected())
    				s += "Down)";
    			
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			txtAlertBuild.setOpacity(0.0);
    		} else {
    			System.out.println("Faltam parâmetros!!");
    			txtAlertBuild.setOpacity(1.0);
    		}
    		
    	} else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
			String s = "";
			
			if(groupBuildTypes.getSelectedToggle() != null && groupBuildDir.getSelectedToggle() != null && edtBuildQnt.getText() != null && !edtBuildQnt.getText().trim().isEmpty() && Integer.parseInt(edtBuildQnt.getText()) != 0) {
				Integer q = Integer.parseInt(edtBuildQnt.getText());
				
    			if(tbBase.isSelected())
    				s = "build(Base,";// + Integer.toString(q) + ")";
    			if(tbBarrack.isSelected())
    				s = "build(Barrack,";// + Integer.toString(q) + ")";
    			
    			s += Integer.toString(q) + ",";
    			
    			if(tbBuildRight.isSelected())
    				s += "Right)";
    			else if(tbBuildLeft.isSelected())
    				s += "Left)";
    			else if(tbBuildUp.isSelected())
    				s += "Up)";
    			if(tbBuildDown.isSelected())
    				s += "Down)";
    			
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			txtAlertBuild.setOpacity(0.0);
    		} else {
    			txtAlertBuild.setOpacity(1.0);
    			System.out.println("Faltam parâmetros!!");
    		}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddBuild.getScene().getWindow();
	    	stage.hide();
    	}
    	
    }

    //Harvest
    @FXML
    void clickBtnHarvestAdd(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
	    	if(edtHarvestQnt.getText() != null && !edtHarvestQnt.getText().trim().isEmpty() && Integer.parseInt(edtHarvestQnt.getText()) != 0) {
	    		Integer q = Integer.parseInt(edtHarvestQnt.getText());
	    		String s = "harvest(" + Integer.toString(q) + ")";
	    		
	    		/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
   
    			txtAlertHarvest.setOpacity(0.0);
	    	} else {
	    		System.out.println("Faltam parâmetros!!");
	    		txtAlertHarvest.setOpacity(1.0);
	    	}
    	} else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
	    	if(edtHarvestQnt.getText() != null && !edtHarvestQnt.getText().trim().isEmpty() && Integer.parseInt(edtHarvestQnt.getText()) != 0) {
	    		Integer q = Integer.parseInt(edtHarvestQnt.getText());
	    		String s = "harvest(" + Integer.toString(q) + ")";
	    		/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			txtAlertHarvest.setOpacity(0.0);
	    	} else {
	    		System.out.println("Faltam parâmetros!!");
	    		txtAlertHarvest.setOpacity(1.0);
	    	}
	    	
	    	
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    
    //Move Away
    @FXML
    void clickBtnAddMoveAway(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
	    	if(groupMoveAwayTypes.getSelectedToggle() != null) {
	    		String s = "moveaway(";
	    		
	    		//Type
	    		if(tbMoveAwayWorker.isSelected())
    				s += "Worker)";
    			else if(tbMoveAwayLight.isSelected())
    				s += "Light)";
    			else if(tbMoveAwayHeavy.isSelected())
    				s += "Heavy)";
    			else if(tbMoveAwayRanged.isSelected())
    				s += "Ranged)";
    			else if(tbMoveAwayAll.isSelected())
    				s += "All)";
	    		
	    		/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			txtAlertMoveAway.setOpacity(0.0);
	    	} else {
	    		System.out.println("Faltam parâmetros!!");
	    		txtAlertMoveAway.setOpacity(1.0);
	    	}
    	} else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
	    	if(groupMoveAwayTypes.getSelectedToggle() != null) {
	    		String s = "moveaway(";
	    		
	    		//Type
	    		if(tbMoveAwayWorker.isSelected())
    				s += "Worker)";
    			else if(tbMoveAwayLight.isSelected())
    				s += "Light)";
    			else if(tbMoveAwayHeavy.isSelected())
    				s += "Heavy)";
    			else if(tbMoveAwayRanged.isSelected())
    				s += "Ranged)";
    			else if(tbMoveAwayAll.isSelected())
    				s += "All)";
	    		
	    		/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			txtAlertMoveAway.setOpacity(0.0);
	    	} else {
	    		System.out.println("Faltam parâmetros!!");
	    		txtAlertMoveAway.setOpacity(1.0);
	    	}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }
    

    //Move to Unit
    @FXML
    void clickBtnAddMoveToUnit(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
	    	if(groupMoveToUnitTypes.getSelectedToggle() != null && groupMoveToUnitTargets.getSelectedToggle() != null && groupMoveToUnitBehaviour.getSelectedToggle() != null) {
	    		//moveToUnit(Worker,Ally,closest,u)
	    		String s = "moveToUnit(";
	    		
	    		//Type
	    		if(tbMoveToUnitWorker.isSelected())
    				s += "Worker,";
    			else if(tbMoveToUnitLight.isSelected())
    				s += "Light,";
    			else if(tbMoveToUnitHeavy.isSelected())
    				s += "Heavy,";
    			else if(tbMoveToUnitRanged.isSelected())
    				s += "Ranged,";
    			else if(tbMoveToUnitAll.isSelected())
    				s += "All,";
	    		
	    		//Target
	    		if(tbMoveToUnitAlly.isSelected())
    				s += "Ally,";
    			else if(tbMoveToUnitEnemy.isSelected())
    				s += "Enemy,";
	    		
	    		//Behaviour
	    		if(tbMoveToUnitStrongest.isSelected())
    				s += "strongest)"; 
    			else if(tbMoveToUnitWeakest.isSelected())
    				s += "weakest)"; 
    			else if(tbMoveToUnitClosest.isSelected())
    				s += "closest)"; 
    			else if(tbMoveToUnitFarthest.isSelected())
    				s += "farthest)"; 
    			else if(tbMoveToUnitLessHealthy.isSelected())
    				s += "lessHealthy)"; 
    			else if(tbMoveToUnitMostHealthy.isSelected())
    				s += "mostHealthy)"; 
    			else if(tbMoveToUnitRandom.isSelected())
    				s += "random)"; 
	    		

	    		/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			txtAlertMoveToUnit.setOpacity(0.0);
	    	} else {
	    		System.out.println("Faltam parâmetros!!");
	    		txtAlertMoveToUnit.setOpacity(1.0);
	    	}
    	} else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
	    	if(groupMoveToUnitTypes.getSelectedToggle() != null && groupMoveToUnitTargets.getSelectedToggle() != null && groupMoveToUnitBehaviour.getSelectedToggle() != null) {
	    		String s = "moveToUnit(";
	    		
	    		//Type
	    		if(tbMoveToUnitWorker.isSelected())
    				s += "Worker,";
    			else if(tbMoveToUnitLight.isSelected())
    				s += "Light,";
    			else if(tbMoveToUnitHeavy.isSelected())
    				s += "Heavy,";
    			else if(tbMoveToUnitRanged.isSelected())
    				s += "Ranged,";
    			else if(tbMoveToUnitAll.isSelected())
    				s += "All,";
	    		
	    		//Target
	    		if(tbMoveToUnitAlly.isSelected())
    				s += "Ally,";
    			else if(tbMoveToUnitEnemy.isSelected())
    				s += "Enemy,";
	    		
	    		//Behaviour
	    		if(tbMoveToUnitStrongest.isSelected())
    				s += "strongest)"; 
    			else if(tbMoveToUnitWeakest.isSelected())
    				s += "weakest)"; 
    			else if(tbMoveToUnitClosest.isSelected())
    				s += "closest)"; 
    			else if(tbMoveToUnitFarthest.isSelected())
    				s += "farthest)"; 
    			else if(tbMoveToUnitLessHealthy.isSelected())
    				s += "lessHealthy)"; 
    			else if(tbMoveToUnitMostHealthy.isSelected())
    				s += "mostHealthy)"; 
    			else if(tbMoveToUnitRandom.isSelected())
    				s += "random)"; 
	    		

	    		/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			txtAlertMoveToUnit.setOpacity(0.0);
	    	} else {
	    		System.out.println("Faltam parâmetros!!");
	    		txtAlertMoveToUnit.setOpacity(1.0);
	    	}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddHarvest.getScene().getWindow();
	    	stage.hide();
    	}
    }

    //Train
    @FXML
    void clickBtnTrainAdd(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
			String s = "";
			
    		if(groupTrainTypes.getSelectedToggle() != null && groupTrainDir.getSelectedToggle() != null && edtTrainQnt.getText() != null && !edtTrainQnt.getText().trim().isEmpty() && Integer.parseInt(edtTrainQnt.getText()) != 0) {
    			Integer q = Integer.parseInt(edtTrainQnt.getText());
    			
    			if(tbTrainWorker.isSelected())
    				s = "train(Worker," + Integer.toString(q) + ",";
    			else if(tbTrainLight.isSelected())
    				s = "train(Light," + Integer.toString(q) + ",";
    			else if(tbTrainHeavy.isSelected())
    				s = "train(Heavy," + Integer.toString(q) + ",";
    			else if(tbTrainRanged.isSelected())
    				s = "train(Ranged," + Integer.toString(q) + ",";	
    			
    			if(tbTrainRight.isSelected())
    				s += "Right)";
    			else if(tbTrainLeft.isSelected())
    				s += "Left)";
    			else if(tbTrainUp.isSelected())
    				s += "Up)";
    			else if(tbTrainDown.isSelected())
    				s += "Down)";
    			else if(tbTrainEnemyDir.isSelected())
    				s += "EnemyDir)";
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			txtAlertTrain.setOpacity(0.0);
    		}else {
    			txtAlertTrain.setOpacity(1.0);
    			System.out.println("Faltam parâmetros!!");
    		}
    		
    	}else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
			String s = "";
			
    		if(groupTrainTypes.getSelectedToggle() != null && groupTrainDir.getSelectedToggle() != null && edtTrainQnt.getText() != null && !edtTrainQnt.getText().trim().isEmpty() && Integer.parseInt(edtTrainQnt.getText()) != 0) {
    			Integer q = Integer.parseInt(edtTrainQnt.getText());
    			
    			if(tbTrainWorker.isSelected())
    				s = "train(Worker," + Integer.toString(q) + ",";
    			else if(tbTrainLight.isSelected())
    				s = "train(Light," + Integer.toString(q) + ",";
    			else if(tbTrainHeavy.isSelected())
    				s = "train(Heavy," + Integer.toString(q) + ",";
    			else if(tbTrainRanged.isSelected())
    				s = "train(Ranged," + Integer.toString(q) + ",";	
    			
    			if(tbTrainRight.isSelected())
    				s += "Right)";
    			else if(tbTrainLeft.isSelected())
    				s += "Left)";
    			else if(tbTrainUp.isSelected())
    				s += "Up)";
    			else if(tbTrainDown.isSelected())
    				s += "Down)";
    			else if(tbTrainEnemyDir.isSelected())
    				s += "EnemyDir)";
    			
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			txtAlertTrain.setOpacity(0.0);
    		} else {
    			System.out.println("Faltam parâmetros!!");
    			txtAlertTrain.setOpacity(1.0);
    		}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddBuild.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    
    //Move to Coordinates
    @FXML
    void clickBtnAddMoveToCoord(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
			String s = "";
			
    		if(groupMoveToCoordTypes.getSelectedToggle() != null  && edtMoveToCoordX.getText() != null && !edtMoveToCoordX.getText().trim().isEmpty() 
    				&& edtMoveToCoordY.getText() != null && !edtMoveToCoordY.getText().trim().isEmpty()) {
    			Integer x = Integer.parseInt(edtMoveToCoordX.getText());
    			Integer y = Integer.parseInt(edtMoveToCoordY.getText());
    			
    			if(tbMoveToCoordWorker.isSelected())
    				s = "moveToCoord(Worker," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveToCoordLight.isSelected())
    				s = "moveToCoord(Light," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveToCoordHeavy.isSelected())
    				s = "moveToCoord(Heavy," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveToCoordRanged.isSelected())
    				s = "moveToCoord(Ranged," + Integer.toString(x) + "," + Integer.toString(y) + ")";	
    			
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			System.out.println("Comando adicionado (1): " + s);
    			
    			txtAlertMoveToCoord.setOpacity(0.0);
    		}else {
    			txtAlertMoveToCoord.setOpacity(1.0);
    			System.out.println("Faltam parâmetros!!");
    		}
    		
    	}else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
    		String s = "";
			
    		if(groupMoveToCoordTypes.getSelectedToggle() != null  && edtMoveToCoordX.getText() != null && !edtMoveToCoordX.getText().trim().isEmpty() 
    				&& edtMoveToCoordY.getText() != null && !edtMoveToCoordY.getText().trim().isEmpty()) {
    			Integer x = Integer.parseInt(edtMoveToCoordX.getText());
    			Integer y = Integer.parseInt(edtMoveToCoordY.getText());
    			
    			if(tbMoveToCoordWorker.isSelected())
    				s = "moveToCoord(Worker," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveToCoordLight.isSelected())
    				s = "moveToCoord(Light," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveToCoordHeavy.isSelected())
    				s = "moveToCoord(Heavy," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveToCoordRanged.isSelected())
    				s = "moveToCoord(Ranged," + Integer.toString(x) + "," + Integer.toString(y) + ")";	
    			
    			/*
    			//Atualização das listas
    			if(principalController != null) {
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}else if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			}
    			*/
    			
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			System.out.println("Comando adicionado (2): " + s);
    			
    			txtAlertMoveToCoord.setOpacity(0.0);
    		}else {
    			txtAlertMoveToCoord.setOpacity(1.0);
    			System.out.println("Faltam parâmetros!!");
    		}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddMoveToCoord.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
    //Move Once to Coordinates
    @FXML
    void clickBtnAddMoveOnceToCoord(ActionEvent event) {
    	principalController.checkSelectedTab();
    	
    	if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
    		String s = "";
			
    		if(groupMoveOnceToCoordTypes.getSelectedToggle() != null  && edtMoveOnceToCoordX.getText() != null && !edtMoveOnceToCoordX.getText().trim().isEmpty() 
    				&& edtMoveOnceToCoordY.getText() != null && !edtMoveOnceToCoordY.getText().trim().isEmpty()
    				&& Integer.parseInt(edtMoveOnceToCoordQnt.getText()) != 0) {
    			Integer x = Integer.parseInt(edtMoveOnceToCoordX.getText());
    			Integer y = Integer.parseInt(edtMoveOnceToCoordY.getText());
    			Integer q = Integer.parseInt(edtMoveOnceToCoordQnt.getText());
    			
    			if(tbMoveOnceToCoordWorker.isSelected())
    				s = "moveOnceToCoord(Worker," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveOnceToCoordLight.isSelected())
    				s = "moveOnceToCoord(Light," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveOnceToCoordHeavy.isSelected())
    				s = "moveOnceToCoord(Heavy," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveOnceToCoordRanged.isSelected())
    				s = "moveOnceToCoord(Ranged," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";	
    			
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI1(s);
    				principalController.attListViewAI1();
    			}
    			
    			System.out.println("Comando adicionado (1): " + s);
    			
    			txtAlertMoveOnceToCoord.setOpacity(0.0);
    		}else {
    			txtAlertMoveOnceToCoord.setOpacity(1.0);
    			System.out.println("Faltam parâmetros!!");
    		}
    		
    	}else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
    		String s = "";
			
    		if(groupMoveOnceToCoordTypes.getSelectedToggle() != null  && edtMoveOnceToCoordX.getText() != null && !edtMoveOnceToCoordX.getText().trim().isEmpty() 
    				&& edtMoveOnceToCoordY.getText() != null && !edtMoveOnceToCoordY.getText().trim().isEmpty()
    				&& Integer.parseInt(edtMoveOnceToCoordQnt.getText()) != 0) {
    			Integer x = Integer.parseInt(edtMoveOnceToCoordX.getText());
    			Integer y = Integer.parseInt(edtMoveOnceToCoordY.getText());
    			Integer q = Integer.parseInt(edtMoveOnceToCoordQnt.getText());
    			
    			if(tbMoveOnceToCoordWorker.isSelected())
    				s = "moveOnceToCoord(Worker," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveOnceToCoordLight.isSelected())
    				s = "moveOnceToCoord(Light," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveOnceToCoordHeavy.isSelected())
    				s = "moveOnceToCoord(Heavy," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";
    			else if(tbMoveOnceToCoordRanged.isSelected())
    				s = "moveOnceToCoord(Ranged," + Integer.toString(q) + "," + Integer.toString(x) + "," + Integer.toString(y) + ")";	
    			
    			//Atualização das listas
    			if(addPlusController != null) {
    				addPlusController.addListViewFuncList(s);
    			} else if(principalController != null){
    				InterfaceSettings.getInstance().addScriptAI2(s);
    				principalController.attListViewAI2();
    			}
    			
    			System.out.println("Comando adicionado (2): " + s);
    			
    			txtAlertMoveOnceToCoord.setOpacity(0.0);
    		}else {
    			txtAlertMoveOnceToCoord.setOpacity(1.0);
    			System.out.println("Faltam parâmetros!!");
    		}
    	}
    	
    	if(fecharJanela) {
	    	Stage stage = (Stage) closeAddMoveOnceToCoord.getScene().getWindow();
	    	stage.hide();
    	}
    }
    
}
