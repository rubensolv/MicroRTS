package ai.synthesis.twophasessa.scriptInterface.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ai.synthesis.twophasessa.*;
import ai.synthesis.twophasessa.scriptInterface.InterfaceSettings;

public class AddScriptPlusController {
	
	private VisualScriptInterfaceController principalController;
	ArrayList<String> ScriptPreview = new ArrayList<>();
	ArrayList<String> FuncList = new ArrayList<String>();

    @FXML
    private JFXListView<String> lvScriptPreview;
    @FXML
    private JFXListView<String> lvFuncList;
    @FXML
    private Text txtAlert;
    
    
    public void init(VisualScriptInterfaceController m) {
    	principalController = m;
    }
    

    //Adiciona for no começo da lista
    @FXML
    void clickAddFor(ActionEvent event) {
    	boolean hasFor = false;
    	
    	if(!ScriptPreview.isEmpty())
    		for(int i = 0; i < ScriptPreview.size(); i++) {
    			if(ScriptPreview.get(i).contains("for"))
    				hasFor = true;
    		}
    		
	    	//if(ScriptPreview.get(0) != "for(u) (") {
    		if(!hasFor) {
	    		ArrayList<String> temp = new ArrayList<>();
	    		temp.add("for(u)");
	    		temp.addAll(ScriptPreview);
	    		
	    		ScriptPreview.clear();
	    		ScriptPreview.addAll(temp);
	    		attScriptPreview();
	    	}
 
    }
    
    //Adiciona else no final da lista
    @FXML
    void clickAddElse(ActionEvent event) {
	    ArrayList<String> temp = new ArrayList<>();
	    temp.addAll(ScriptPreview);
	    temp.add("else");
	    		
	    ScriptPreview.clear();
	    ScriptPreview.addAll(temp);
	    attScriptPreview();
    }
    
    //Adiciona else no final da lista
    @FXML
    void clickAddEpsilon(ActionEvent event) {
	    ArrayList<String> temp = new ArrayList<>();
	    temp.addAll(ScriptPreview);
	    temp.add("Z");
	    		
	    ScriptPreview.clear();
	    ScriptPreview.addAll(temp);
	    attScriptPreview();
    }

    @FXML
    void clickAddScript(ActionEvent event) {
    	txtAlert.setText("Incorrect indentation!");
    	principalController.checkSelectedTab();
    	
    	String s = "";
    	int open = 0;
    	int tab = 0;
    	int new_tab = 0;
    	
    	boolean inFor = false;
    	boolean inIf = false; //NOVO
    	boolean inElse = false;
    	int nvFor = 0;
    	boolean hasElseInsideIf = false;
    	
    	//Contagem de tabs
    	// e adição de parênteses
    	for(int i = 0; i < ScriptPreview.size(); i++) {   		
    		int mult = 0;
    		char l[] = ScriptPreview.get(i).toCharArray();
    		
    		while(true) {
    			if(l[mult*8] == ' ')
    				mult++;
    			else
    				break;
    		}
    		
    		new_tab = mult;
    		
    		//Teste de nível de identação para o for (acréscimo do u no final dos comandos)
    		if( inFor && nvFor > new_tab) inFor = false;
    		if(ScriptPreview.get(i).contains("for")) { inFor = true; nvFor = new_tab + 1; }
    		
    		//Teste de presença do if
    		if(ScriptPreview.get(i).contains("if")) { inIf = true;}
    		
    		//Teste de presença do else
    		if(ScriptPreview.get(i).contains("else")) { inElse = false;}
    		
    		if(i > 0) {
    			if(ScriptPreview.get(i-1).contains("for"))
    				s += "(";
    			else if(ScriptPreview.get(i-1).contains("if"))
    				s += "then(";
    			else if(ScriptPreview.get(i-1).contains("else")) {
    				if(!inIf) {
    					//System.out.println("Tem else sem if");
    					hasElseInsideIf = true;
    	    			txtAlert.setText("Incorrect indentation!");
    				}
    				s = s.trim();
    				s += "(";
    				inIf = false;
    				inElse = true;
    			}
    		}
    		
    		//Verifica identação do else com o if
    		if(new_tab == tab && ScriptPreview.get(i).contains("else")) {
    			//System.out.println("identação incorreta do else");
    			hasElseInsideIf = true;
    			txtAlert.setText("Incorrect indentation!");
    		}
    		
    		//Verificações com nível de identação e adição de parênteses
    		if(new_tab < tab) {
    			if(inIf && !ScriptPreview.get(i).contains("else")) {
    				inIf = false;
    			}
    			s = s.trim();
	    		for(int k = 0; k < tab - new_tab; k++)
	    			s += ")";
	    		s += " ";

    		}
    		
    		//TESTE
    		//if(inFor) System.out.println("Linha dentro do for");
    		//else System.out.println("Linha fora do for");
    		
    		//adiciona linha atual no script
    		s += ScriptPreview.get(i).trim();
    		
    		//adiciona u caso esteja dentro de um for
    		if(inFor && s.toCharArray()[s.length()-1] == ')') {
    			if(!ScriptPreview.get(i).contains("for") && !ScriptPreview.get(i).contains("if")){
	    			s = s.substring(0,s.length()-1);
	    			s += ",u)"; 
    			}
    			
    			if(ScriptPreview.get(i).contains("if")) {
    				s = s.substring(0,s.length()-2);
    				s += ",u))";
    			}
    		}
    		//System.out.println("último caracteres:");
    		//System.out.println(s.toCharArray()[s.length()-1]);
    		
    		if( i != ScriptPreview.size() - 1) {
    			s += " ";
    		} else {
    			String script = ScriptPreview.get(i);
    			/*
    			if(inIf) {	//Coloca else no final caso o script termine com if
    				System.out.println("A última linha está dentro de um IF");
            		System.out.println("String s: " + s);
            		s = s.trim();
            		s += ") (Z)";
            		new_tab--;
            		inIf = false;
    			}
    			*/
    			char ll[] = script.toCharArray();
    			if(ll[0] == ' ')
    				for(int k = 0; k < new_tab; k++)
    					s += ")";
    		}
    		
    		tab = new_tab;
    		mult = 0;
    	}
    	
    	//Impress�o da string completa s
    	//System.out.println("Script completo:");
    	//System.out.println(s);
    	//System.out.println();
    	
    	//Verificação de epsilons excessivos
    	boolean hasIllegalEpsilon = false;
    	hasIllegalEpsilon = verifyEpsilons(s);
    	if(hasIllegalEpsilon) txtAlert.setText("Please, check the Z(s).");
    	
    	//Verificação de if na última linha
    	boolean hasLastLineIF = false;
    	hasLastLineIF = verifyLastLineIF();
    	
    	//Verificação de else na última linha
    	boolean hasLastLineElse = false;
    	hasLastLineElse = verifyLastLineElse();
    	
    	//Verificação dos ifs
    	boolean hasNestedIF = false;
    	hasNestedIF = verifyNestedIF(s);
    	
    	//Contagem de parênteses
    	char sc[] = s.toCharArray();
    	for(int i = 0; i < s.length(); i++) {
    		if(sc[i] == '(') open++;
    		else if(sc[i] == ')') open--;
    	}
    	if(open != 0) txtAlert.setText("Incorrect indentation!");
    	
    	
    	
    	//Mensagem de falta de parênteses caso o número esteja errado
    	// adiciona na lista principal se estiver correto
    	if(open == 0 && s.length() > 8 && !hasNestedIF && !hasLastLineIF && !hasElseInsideIf && !hasLastLineElse && !hasIllegalEpsilon) {
    		txtAlert.setOpacity(0.0);
    		//s = addThenElse(s);
    		
    		if(InterfaceSettings.getInstance().getAbaAddScript() == 1) {
    			InterfaceSettings.getInstance().addScriptAI1(s);
    			principalController.attListViewAI1();
    		}else if(InterfaceSettings.getInstance().getAbaAddScript() == 2) {
    			InterfaceSettings.getInstance().addScriptAI2(s);
    			principalController.attListViewAI2();
    		}
    		
    	}else {
    		txtAlert.setOpacity(1.0);
    	}
    }


	@FXML
    void clickIndUp(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		ListIterator<String> itr1 = ScriptPreview.listIterator();
    		ListIterator<String> itr2 = ScriptPreview.listIterator();
    		String a = lvScriptPreview.getSelectionModel().getSelectedItem();
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
    		attScriptPreview();
    	}
    	
    	
    }

    @FXML
    void clickIndDown(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
	    	ListIterator<String> itr1 = ScriptPreview.listIterator();
			ListIterator<String> itr2 = ScriptPreview.listIterator();
			String a = lvScriptPreview.getSelectionModel().getSelectedItem();
			String a1 = "", a2 = "";
			
			if(ScriptPreview.get(ScriptPreview.size()-1) != a) {
			
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
			attScriptPreview();
    	}
    }

    
    //Abre janela de adi��o de Fun��es Simples
    //Janela ADD Simple Script
  	@FXML
  	void clickNewSimpleScript(ActionEvent event) throws IOException {
  		
  		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddScript.fxml"));
  		Parent root1 = (Parent) fxmlLoader.load();
  		
  		AddScriptController addScriptController = fxmlLoader.getController();
  		addScriptController.initp(this);
  		addScriptController.init(principalController);
  		
  		Stage stage = new Stage();
  		stage.setTitle("Add Scripts");
  		stage.setScene(new Scene(root1));
  		stage.setHeight(721);
  		stage.setWidth(275);
  		//stage.initModality(Modality.APPLICATION_MODAL);
  		stage.initModality(Modality.WINDOW_MODAL);
  		stage.setResizable(false);
  		
  		stage.showAndWait();	
  	}
  	
    //Janela ADD Conditional Script
  	
  	//Abre janela de adi��o de Fun��es Condicionais
  	@FXML
  	void clickNewCondScript(ActionEvent event) throws IOException {
  		
  		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddConditionalScript.fxml"));
  		Parent root1 = (Parent) fxmlLoader.load();
  		
  		AddCondScriptController addCondScriptController = fxmlLoader.getController();
  		addCondScriptController.initp(this);
  		addCondScriptController.init(principalController);
  		
  		Stage stage = new Stage();
  		stage.setTitle("Add Scripts");
  		stage.setScene(new Scene(root1));
  		stage.setHeight(721);
  		stage.setWidth(275);
  		//stage.initModality(Modality.APPLICATION_MODAL);
  		stage.initModality(Modality.WINDOW_MODAL);
  		stage.setResizable(false);
  		
  		stage.showAndWait();	
  	}
  	
  	//Adições às listas

  	public void addListViewFuncList(String s) {
  		//if(!FuncList.contains(s) || !ScriptPreview.contains(s)) {
  			FuncList.add(s);
  			attFuncList();
  		//}
  	}
  	
  	
  	public void addListViewScriptPreview(String s) {
  		//if(!ScriptPreview.contains(s) || !FuncList.contains(s)) {
  			ScriptPreview.add(s);
  			attScriptPreview();
  		//}
  	}
  	
  	//Remoções das Listas
  	
  	public void delListViewScriptPreview(String s) {
  		if(ScriptPreview.contains(s))
  			ScriptPreview.remove(s);
  		attScriptPreview();
  	}
  	
  	
  	public void delListViewFuncList(String s) {
  		if(FuncList.contains(s))
  			FuncList.remove(s);
  		attFuncList();
  	}
  	
  	//Atualiza��es dos ListView
  	private void attFuncList() {
  		lvFuncList.getItems().clear();
  		lvFuncList.getItems().addAll(FuncList);
  	}
  	
  	
  	private void attScriptPreview() {
  		lvScriptPreview.getItems().clear();
  		lvScriptPreview.getItems().addAll(ScriptPreview);
  	}
  	
  	
  	
  	//Move script Functions List --> Script Preview
  	@FXML
    void clickMoveScriptLeft(ActionEvent event) {
  		if(lvFuncList.getSelectionModel().getSelectedItem() != null) {
  			String s = lvFuncList.getSelectionModel().getSelectedItem();
  			addListViewScriptPreview(s);
  			delListViewFuncList(s);
  			attScriptPreview();
  			attFuncList();
  		}
    }

    //Move script Script Preview --> Functions List
  	@FXML
    void clickMoveScriptRight(ActionEvent event) {
  		if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
  			String s = lvScriptPreview.getSelectionModel().getSelectedItem();
  			addListViewFuncList(s);
  			delListViewScriptPreview(s);
  		}
    	
    }
  	
    //Remove script selecionado no Script Preview
  	@FXML
    void clickDeleteScript(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		String s = lvScriptPreview.getSelectionModel().getSelectedItem();
    		delListViewScriptPreview(s);
    	}
    }

  	//Adiciona espa�o na identa��o
  	@FXML
    void clickIndRight(ActionEvent event) {
  		if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		String s = "        " + lvScriptPreview.getSelectionModel().getSelectedItem();
    		ScriptPreview.set((ScriptPreview.indexOf(lvScriptPreview.getSelectionModel().getSelectedItem())), s);
    		attScriptPreview();
    	}
    }
  	

  	//Remove espa�o na identa��o
    @FXML
    void clickIndLeft(ActionEvent event) {
    	if(lvScriptPreview.getSelectionModel().getSelectedItem() != null) {
    		if(lvScriptPreview.getSelectionModel().getSelectedItem().startsWith("        ")) {
	    		String s = lvScriptPreview.getSelectionModel().getSelectedItem();
	    		s = s.substring(8);
	    		ScriptPreview.set((ScriptPreview.indexOf(lvScriptPreview.getSelectionModel().getSelectedItem())), s);
	    		attScriptPreview();
    		}
    	}
    }
    
    //Verifica existência de if aninhado
    private boolean verifyNestedIF(String s) {
    	//System.out.println("Verificação dos IFs");
    	boolean insideIF = false;
    	//boolean nestedIF = false;
    	int open = 0, close = 0;
    	char sc[] = s.toCharArray();
    	
    	for(int i = 0; i < s.length(); i++) {
    		// encontra if
    		if(i+1 < s.length() && !insideIF) {
    			if(sc[i] == 'i' && sc[i+1] == 'f') {
    				//System.out.println("Encontrou if");
    				insideIF = true;
    				//continue;
    			}
    		}
    		
    		if(insideIF) {
    			if(sc[i] == '(') open++;
    			else if(sc[i] == ')') close++;
    			// IF aninhado
    			if(i+1 < s.length() && open != close && open != 2) {
    				if(sc[i] == 'i' && sc[i+1] == 'f') {
    					txtAlert.setText("The language doesn't accept nested if.");
    					//System.out.println("Encontrou if aninhado");
        				return true;
    				}
    			}
    			// FOR aninhado
    			if(i+2 < s.length() && open != close && open != 2) {
    				if(sc[i] == 'f' && sc[i+1] == 'o' && sc[i+2] == 'r') {
    					txtAlert.setText("The language doesn't accept nested for.");
    					//System.out.println("Encontrou for aninhado");
        				return true;
    				}
    			}
    			
    			if(open == close && open != 2 && open != 0) insideIF = false;
    		}
    		
    	}
    	
    	return false;
    }
    
    //Verificação de IF ou FOR na última linha do List View
    private boolean verifyLastLineIF() {
    	String s = ScriptPreview.get(ScriptPreview.size()-1);
    	if(s.contains("if")) return true;
    	if(s.contains("for")) return true;
    	return false;
    }
    
    //Verificação de else na última linha do List View
    private boolean verifyLastLineElse() {
    	String s = ScriptPreview.get(ScriptPreview.size()-1);
    	if(s.contains("else")) { return true; }
    	return false;
    }
    
  //Verificação de excesso ou mau uso de epsilon
    private boolean verifyEpsilons(String script) {
    	script = script.trim();
    	boolean hasIllegalEpsilon = false;
    	
    	char sc[] = script.toCharArray();
    	for(int i = 0; i < sc.length; i++) {
    		if(sc[i] == 'Z') {
    			if(i-1 > 0 && i+1 < sc.length) {
    				if(sc[i-1] != '(' || sc[i+1] != ')') { hasIllegalEpsilon = true; }
    			}
    		}
    		if(i == sc.length-1 && sc[i] == 'Z') {
    			hasIllegalEpsilon = true;
    		}
    	}
    	
    	return hasIllegalEpsilon;
    }
    
    
}
