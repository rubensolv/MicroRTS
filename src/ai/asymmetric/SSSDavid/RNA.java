package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jdom.JDOMException;
import org.tensorflow.Graph;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;

import jep.JepException;
import rts.GameState;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;


public class RNA {
	
	
	RnaJep model;
	HashMap<Integer,HashMap<Integer, ArrayList<Float>>> full_distribuicao ;
	ArrayList<Float> num_grupo_distribuicao ;
	
	int n;// numero de treino ja feito 
	int tam_bufferRNA = 1;
	int epoca;
	HashMap<UnitType, Integer> mapea;
	ConfMapa mapa;
	int largura, altura,camadas, num_grupos,tipo_mapa;

	public RNA( UnitTypeTable utt, int buffer,int epoc,   int num_grupos,String id, String rnaopcao,RnaJep model) throws JDOMException, IOException, Exception {
		
		this.tam_bufferRNA = buffer;
		this.epoca = epoc;
		
		this.num_grupos =num_grupos;
		this.mapa = new MapaDavid("maps/mapadavid2.xml",utt,num_grupos);
		//this.mapa = new Mapa8x8("maps/8x8/basesWorkers8x8A.xml",utt,num_grupos);
		
		this.largura = mapa.getLargura();
		this.altura = mapa.getAltura();
		this.camadas = mapa.getCamadas();
		
		
		this.model = model;
		full_distribuicao = new HashMap<Integer,  HashMap<Integer, ArrayList<Float>>>() ;;
		
		for(int i =-1;i<8;i++) {
			full_distribuicao.putIfAbsent(i, new HashMap<Integer, ArrayList<Float>>());
		}
	}

	

	public void seleciona_exemplo_vencedor(int vencedor) {
		
		
	}
	
	
	
	public void treina(int player) throws IOException, JepException {
		model.treina(player);
		return;
	 
	}
	
	
	
	public void grava(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup, int num_grupo) throws IOException, JepException, InterruptedException { // salva o estado
		float[][][][] pre_input1 = new float[1][altura][largura][camadas];
		HashMap<Integer, Integer> pre_input2;
		pre_input1[0]=mapa.montar_entrada(player, gs, false);
		pre_input2=mapa.montar_entrada2(player, gs, false);
	   
		model.grava(pre_input1[0],pre_input2,agrup,player,num_grupo);
	
	}
	
	
	
	public void atualiza_distribuicao(int player, GameState gs,boolean treinamento) throws IOException, JepException, InterruptedException {
		float[][][][] pre_input1 = new float[1][altura][largura][camadas];
		HashMap<Integer, Integer> pre_input2;
		pre_input1[0]=mapa.montar_entrada(player, gs, false);
		pre_input2=mapa.montar_entrada2(player, gs, false);
		double inicio_playout =System.currentTimeMillis();
		
		num_grupo_distribuicao = model.distribuicao_grupo(pre_input1[0],pre_input2,-1,player);
		if(treinamento) {
			
			for(int i=0;i<8;i++) {
				full_distribuicao.put(i, model.amostragem(pre_input1[0],pre_input2,i,player));
			}
		} else {
			full_distribuicao.put(-1, model.amostragem(pre_input1[0],pre_input2,-1,player));
		}
		// System.out.println(full_distribuicao);
		 inicio_playout =System.currentTimeMillis()-inicio_playout;
		
	}
	
	
	public int agrupa(int player, GameState gs,UnitTypeTable utt,HashMap<Integer, Integer> agrup) throws IOException {

		
		agrup.clear();
	
		double  max= -1;
		int index=-1;
		for(int i=0;i<num_grupos;i++) {
			double aux = this.num_grupo_distribuicao.get(i);
		
			if(aux>max) {
				index=i;
				max=aux;
			}
		}
		
		int grupo=index;
	//	System.out.println("grupo rea "+this.num_grupo_distribuicao );
		
		
		for (Map.Entry me : full_distribuicao.get(-1).entrySet()) {
				
					 ArrayList<Float> 	distribuicao = (ArrayList<Float>) me.getValue();
					  max= -1;
					 index=-1;
					for(int i=0;i<num_grupos;i++) {
						double aux = distribuicao.get(i);
					//if(gs.getTime()==0)System.out.print(aux+" ");
						if(aux>max) {
							index=i;
							max=aux;
						}
					}
					//if(gs.getTime()==0)System.out.println();
					agrup.put((int) me.getKey(), index);
				
			}
		return grupo;
		
	//	System.out.println(resposta);
		//System.out.println(agrup);
		
	}
	
	
	public int amostra(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) throws IOException {
		
		
		int grupo = -1;
		int  soma = 0;
		
		for(int i=0;i<num_grupos;i++) {
			double aux = num_grupo_distribuicao.get(i);
			
			soma+=aux*10000;
		}
	
		Random gerador;
		gerador = new Random();
		
		int g =gerador.nextInt(soma);
		
		soma =0;
		
		for(int i=0;i<num_grupos;i++) {
			double aux = num_grupo_distribuicao.get(i);
			soma+=aux*10000;
			if(soma>=g) {
				grupo=i;
				break;
			}
		}
		
		g =gerador.nextInt(100);// epsilon guloso;
		 
		if(g<5) {
			grupo =gerador.nextInt(1000)%8;
		}
	
	
		for (Map.Entry me : full_distribuicao.get(grupo).entrySet()) {
			 ArrayList<Float> 	distribuicao = (ArrayList<Float>) me.getValue();
				 soma = 0;
				
				for(int i=0;i<num_grupos;i++) {
					double aux = distribuicao.get(i);
					
					soma+=aux*10000;
				}
			
				
				gerador = new Random();
				
				 g =gerador.nextInt(soma);
				
				int index =0;
				soma =0;
				
				for(int i=0;i<num_grupos;i++) {
					double aux = distribuicao.get(i);
					soma+=aux*10000;
					if(soma>=g) {
						index=i;
						break;
					}
				}
				
				g =gerador.nextInt(100);// epsilon guloso;
				 
				if(g<-5) {
					index=g =gerador.nextInt(1000)%grupo;
				}
				
				agrup.put((int) me.getKey(), index);
				
				
			
		}
				
		return  grupo;
	}
	
	
	public void carregar(String s,int player) throws IOException, JepException {
		model.carrega(s,player);
	}
	
	public void proximo(int player) throws IOException, JepException {
		model.proximo(player);
	}
	
	public void salvar(String s,int player) throws JepException {
		model.salvar(s,player);
	}
	
}
