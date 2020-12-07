package ai.synthesis.twophasessa.scriptInterface.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.synthesis.twophasessa.scriptInterface.InterfaceSettings;
//import ai.scriptInterface.scriptInterface.gui.MapPath;
import rts.units.UnitTypeTable;


public class VisualScriptInterfaceController implements Initializable {

    
    @FXML
    private ComboBox<MapPath> cbMaps;
    private List<MapPath> maps = new ArrayList<>();
    private ObservableList<MapPath> obsMaps;
    
    @FXML
    private AddScriptController  addScriptController = new AddScriptController();
    
    ICompiler compiler = new MainGPCompiler();
    
    @FXML
    private ComboBox<String> cbAI1;
    @FXML
    private ComboBox<String> cbAI2;
    
    private List<String> ais = new ArrayList<>();
    private ObservableList<String> obsAIs;
    
    @FXML
    private ListView<String> lvScriptsAI1 = new ListView<>();
    @FXML
    private ListView<String> lvScriptsAI2 = new ListView<>();
    @FXML
    private Tab tabScripts1;
    @FXML
    private Tab tabScripts2;
    @FXML
    private ToggleButton tbAddScripts1;
    @FXML
    private ToggleButton tbAddScripts2;
    @FXML
    private TextField txtSaveScripts;
    
    //Segunda coluna
    
    // -- Build
    
    @FXML
    private ToggleGroup groupBuildTypes;
    @FXML
    private TextField edtBuildQnt;
    @FXML
    private ToggleButton tbBase;
    @FXML
    private ToggleButton tbBarrack;
    
    // -- Train
    
    @FXML
    private ToggleGroup groupTrainTypes;
    @FXML
    private ToggleGroup groupTrainDir;
    @FXML
    private TextField edtTrainQnt;
    @FXML
    private ToggleButton tbTrainWorker;
    @FXML
    private ToggleButton tbTrainLight;
    @FXML
    private ToggleButton tbTrainHeavy;
    @FXML
    private ToggleButton tbTrainRanged;
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
    
    // -- Harvest
    
    @FXML
    private TextField edtHarvestQnt;
    
    // -- Attack
    
    @FXML
    private ToggleGroup groupAttackTypes;
    @FXML
    private ToggleGroup groupAttackEnemy;
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
    private Button btnSendQ1;
    @FXML
    private Button btnSendQ2;
    @FXML
    private Button btnSwitch;
    
 
    
    // Inicialização da interface
	public void initialize(URL location, ResourceBundle resources) {
		loadMaps();		
		
		btnSendQ1.setTooltip(new Tooltip("Send Questionnaire 1."));
        btnSendQ2.setTooltip(new Tooltip("Send Questionnaire 2."));
        btnSwitch.setTooltip(new Tooltip("Switch Side Scripts."));
		
		lvScriptsAI1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		lvScriptsAI2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	public void loadMaps() {
		
		MapPath map1 = new MapPath("16x16 - Bases Workers","maps/16x16/basesWorkers16x16.xml");
        MapPath map2 = new MapPath("16x16 - Bases With Walls","maps/16x16/BasesWithWalls16x16.xml");     
        MapPath map4 = new MapPath("24x24 - Bases Workers A","maps/24x24/basesWorkers24x24A.xml");
        MapPath map5 = new MapPath("16x16 - 2 Bases Barracks","maps/16x16/TwoBasesBarracks16x16.xml");
        MapPath map6 = new MapPath("32x32 - Distant Resources","maps/BWDistantResources32x32.xml");
        MapPath map7 = new MapPath("63x63 - BloodBath","maps/BroodWar/(4)BloodBath.scmB.xml");
        MapPath map8 = new MapPath("8x8 - 4 Bases Workers","maps/8x8/FourBasesWorkers8x8.xml");
        MapPath map10 = new MapPath("24x24 - Double Game","maps/DoubleGame24x24.xml");
        MapPath map11 = new MapPath("8x8 - Bases Workers Obstacle","maps/8x8/basesWorkers8x8Obstacle.xml");
        MapPath map13 = new MapPath("18x8 - 1 Base", "maps/RangedHeavyMixed.xml");
        MapPath map14 = new MapPath("8x8 - Barreiras (teste)", "maps/8x8Barreiras.xml");
        MapPath map15 = new MapPath("16x16 - Barreiras (teste)", "maps/16x16Barreiras.xml");
        
      //Mapas do teste
        MapPath map12 = new MapPath("9x8 - Nowhere to Run","maps/NoWhereToRun9x8.xml");
        MapPath map9 = new MapPath("8x8 - Bases Workers A","maps/basesWorkers8x8A.xml");
        MapPath map16 = new MapPath("24x24 - Double Game","maps/DoubleGame24x24.xml");
        //MapPath map13 = new MapPath("18x8 - 1 Base", "maps/RangedHeavyMixed.xml");
        
        maps.add(map1);
        maps.add(map2);
        maps.add(map4);
        maps.add(map5);
        maps.add(map6);
        maps.add(map7);
        maps.add(map8);
        maps.add(map10);
        maps.add(map11);
        maps.add(map13);
        maps.add(map14);
        maps.add(map15);
        
        maps.add(map9);
        maps.add(map12);
        maps.add(map16);
		
		obsMaps = FXCollections.observableArrayList(maps);
		cbMaps.setItems(obsMaps);
	}
	
	public void loadAIs() {
		String ai1 = "Passive";
		String ai2 = "Worker Rush";
		String ai3 = "Light Rush";
		String ai4 = "Ranged Rush";
		String ai5 = "Heavy Rush";
		String ai6 = "Chromosome";
        
        ais.add(ai1);
        ais.add(ai2);
        ais.add(ai3);
        ais.add(ai4);
        ais.add(ai5);
        ais.add(ai6);
		
		obsAIs = FXCollections.observableArrayList(ais);
		cbAI1.setItems(obsAIs);
		cbAI2.setItems(obsAIs);
	}
	
	//Reposicionamento de Scripts
	
	@FXML
    void clickBtnUp(ActionEvent event) {
    	if(tabScripts1.isSelected()) {
    		if(lvScriptsAI1.getSelectionModel().getSelectedItem() != null) {
    			Integer aux = lvScriptsAI1.getSelectionModel().getSelectedIndex();
    			InterfaceSettings.getInstance().upScriptAi1(lvScriptsAI1.getSelectionModel().getSelectedItem());
    			System.out.println(lvScriptsAI1.getFocusModel().getFocusedItem());
    			attListViewAI1();
    		}
    	} else if(tabScripts2.isSelected()) {
    		if(lvScriptsAI2.getSelectionModel().getSelectedItem() != null) {
    			InterfaceSettings.getInstance().upScriptAi2(lvScriptsAI2.getSelectionModel().getSelectedItem());
    			attListViewAI2();
    		}
    	}
    }
    
    @FXML
    void clickBtnDown(ActionEvent event) {
    	if(tabScripts1.isSelected()) {
    		if(lvScriptsAI1.getSelectionModel().getSelectedItem() != null) {
    			InterfaceSettings.getInstance().downScriptAi1(lvScriptsAI1.getSelectionModel().getSelectedItem());
    			attListViewAI1();
    		}
    	} else if(tabScripts2.isSelected()) {
    		if(lvScriptsAI2.getSelectionModel().getSelectedItem() != null) {
    			InterfaceSettings.getInstance().downScriptAi2(lvScriptsAI2.getSelectionModel().getSelectedItem());
    			attListViewAI2();
    		}
    	}
    }
        
    @FXML
    void clickBtnPause(ActionEvent event) {
    	InterfaceSettings.getInstance().setPause(true);
    }
 
    @FXML
    void clickBtnPlay(ActionEvent event) {
    	InterfaceSettings.getInstance().setPause(false);
    	InterfaceSettings.getInstance().setPlay(true);
    	InterfaceSettings.getInstance().setApply(true);
    }

    @FXML
    void clickBtnRestart(ActionEvent event) {
    	InterfaceSettings.getInstance().setMap(cbMaps.getValue());
    	InterfaceSettings.getInstance().setPause(true);
    	InterfaceSettings.getInstance().setRestart(true);
    }
    
    @FXML
    void clickBtnDelete(ActionEvent event) {
    	if(tabScripts1.isSelected()) {
    		if(lvScriptsAI1.getSelectionModel().getSelectedItem() != null) {
    			InterfaceSettings.getInstance().removeScriptAi1(lvScriptsAI1.getSelectionModel().getSelectedItem());
    			attListViewAI1();
    		}
    	} else if(tabScripts2.isSelected()) {
    		if(lvScriptsAI2.getSelectionModel().getSelectedItem() != null) {
    			InterfaceSettings.getInstance().removeScriptAi2(lvScriptsAI2.getSelectionModel().getSelectedItem());
    			attListViewAI2();
    		}
    	}
    }
	
    
	public void attListViewAI1() {
		lvScriptsAI1.getItems().clear();
		lvScriptsAI1.getItems().addAll(InterfaceSettings.getInstance().getScritpsAi1());
	}
	
	public void attListViewAI2() {
		lvScriptsAI2.getItems().clear();
		lvScriptsAI2.getItems().addAll(InterfaceSettings.getInstance().getScritpsAi2());
	}

	
	@FXML
	void clickBtnAdd(ActionEvent event) throws IOException {
		checkSelectedTab();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddScript.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		AddScriptController addScriptController = fxmlLoader.getController();
		addScriptController.init(this);
		
		Stage stage = new Stage();
		stage.setTitle("Add Scripts");
		stage.setScene(new Scene(root1));
		stage.setHeight(721);
		stage.setWidth(275);
		//stage.initModality(Modality.APPLICATION_MODAL);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
		
		attListViewAI1();
		attListViewAI2();
			
	}
	
	@FXML
	void clickBtnLoad(ActionEvent event) throws IOException {
		checkSelectedTab();
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoadScript.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		//vsi_loadScriptController loadScriptController = fxmlLoader.getController();
		//loadScriptController.init(this);
		
		Stage stage = new Stage();
		stage.setTitle("Load Script");
		stage.setScene(new Scene(root1));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setHeight(721);
		stage.setWidth(275);
		stage.setResizable(false);
		
		stage.showAndWait();
		
		attListViewAI1();
		attListViewAI2();
		
	}
	
	//Retorna List View pra ser atualizada pela janela ADD
	public ListView<String> getListView() {
		if(tabScripts1.isSelected())
			return lvScriptsAI1;
		else 
			return lvScriptsAI2;
	}
	
	@FXML
	void clickBtnSave(ActionEvent event) throws IOException {
		if(!txtSaveScripts.getText().trim().isEmpty()) {
			String fname = "savedScripts/" + txtSaveScripts.getText().trim() + ".txt";
			
			if(tabScripts1.isSelected()) {

				BufferedWriter writer = null;
		        try {
		            File file = new File(fname);
		            writer = new BufferedWriter(new FileWriter(file));
		            for(String f: InterfaceSettings.getInstance().getScritpsAi1()) {
		            	writer.write(f + "\n");
		            }
 		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                writer.close();
		            } catch (Exception e) {
		            	System.out.println("Erro ao escrever arquivo.");
		            }
		        }
			} else if(tabScripts2.isSelected()) {
				BufferedWriter writer = null;
		        try {
		            File file = new File(fname);
		            writer = new BufferedWriter(new FileWriter(file));
		            for(String f: InterfaceSettings.getInstance().getScritpsAi2())
		            	writer.write(f + "\n");
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                writer.close();
		            } catch (Exception e) {
		            	System.out.println("Erro ao escrever arquivo.");
		            }
		        }
			} else {
				System.out.println("clickBtnAdd com valor inválido");
				//InterfaceSettings.getInstance().setAbaAddScript(1);
			}	
		}
		
		txtSaveScripts.setText(null);
		
		
	}
	
	//Janela ADD+
	@FXML
	void clickBtnAddConditionals(ActionEvent event) throws IOException {
		checkSelectedTab();
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddScriptPlus.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		AddScriptPlusController addScriptPlusController = fxmlLoader.getController();
		addScriptPlusController.init(this);
		
		Stage stage = new Stage();
		stage.setTitle("Add Scripts");
		stage.setScene(new Scene(root1));
		stage.setHeight(721);
		stage.setWidth(545);
		//stage.initModality(Modality.APPLICATION_MODAL);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
		
		attListViewAI1();
		attListViewAI2();
			
	}

    void checkSelectedTab() {
    	if(tabScripts1.isSelected()) {
			InterfaceSettings.getInstance().setAbaAddScript(1);
		} else if(tabScripts2.isSelected()) {
			InterfaceSettings.getInstance().setAbaAddScript(2);
		} else {
			System.out.println("clickBtnAdd com valor inválido");
			InterfaceSettings.getInstance().setAbaAddScript(1);
		}
    }
    
    // Janela SEND (questionário1)
    @FXML
    void clickBtnSendQ1(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire1.fxml"));
    	//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire2.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		SendQuestionnaire1Controller sendQuestionnaire1Controller = fxmlLoader.getController();
		//SendQuestionnaire2Controller sendQuestionnaire2Controller = fxmlLoader.getController();
		//sendQuestionnaire1Controller.initialize();
		
		Stage stage = new Stage();
		stage.setTitle("Questionnaire 1");
		//stage.setTitle("Questionnaire 2");
		stage.setScene(new Scene(root1));
		stage.setHeight(721);
		stage.setWidth(987);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    }
    
 // Janela SEND (questionário2)
    @FXML
    void clickBtnSendQ2(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire1.fxml"));
    	//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SendQuestionnaire2.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		
		SendQuestionnaire1Controller sendQuestionnaire1Controller = fxmlLoader.getController();
		//SendQuestionnaire2Controller sendQuestionnaire2Controller = fxmlLoader.getController();
		//sendQuestionnaire1Controller.initialize();
		
		Stage stage = new Stage();
		stage.setTitle("Questionnaire 1");
		//stage.setTitle("Questionnaire 2");
		stage.setScene(new Scene(root1));
		stage.setHeight(721);
		stage.setWidth(987);
		stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		stage.showAndWait();
    }
    
    // Botão switch scripts
    @FXML
    void clickBtnSwitchScripts(ActionEvent event) {
    	//InterfaceSettings.getInstance().removeScriptAi1(lvScriptsAI1.getSelectionModel().getSelectedItem());
    	ArrayList<String> s = new ArrayList<>();
    	
    	s = InterfaceSettings.getInstance().getScritpsAi1();
    	InterfaceSettings.getInstance().setScritpsAi1(InterfaceSettings.getInstance().getScritpsAi2());
    	InterfaceSettings.getInstance().setScritpsAi2(s);
    	
    	attListViewAI1();
    	attListViewAI2();
    }

}
