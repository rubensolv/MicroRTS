package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jdom.JDOMException;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import rts.GameState;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;


public class RNA {
	
	
	 SavedModelBundle model;
	
	List<float[][][]> input1; // recolhe entrada da rna
	List<float[]> input2;
	List<float[][]> out; //saida 
	
	List<float[][][]> input1_play0; // recolhe entrada da rna
	List<float[]> input2_play0;
	List<float[][]> out_play0; //saida 
	
	List<float[][][]> input1_play1; // recolhe entrada da rna
	List<float[]> input2_play1;
	List<float[][]> out_play1; //saida 
	
	List<Tensor> t_input1;
	List<Tensor> t_input2;
	List<Tensor> t_out;
	
	
	int n;// numero de treino ja feito 
	int tam_bufferRNA = 1;
	int epoca;
	HashMap<UnitType, Integer> mapea;
	ConfMapa mapa;
	int largura, altura,camadas, num_grupos,tipo_mapa;

	public RNA( UnitTypeTable utt, int buffer,int epoc,   int num_grupos, String rnaopcao) throws JDOMException, IOException, Exception {
		
		model = SavedModelBundle.load("resources/mod"+rnaopcao, "serve"); 
		this.tam_bufferRNA = buffer;
		this.epoca = epoc;
		
		this.num_grupos =num_grupos;
		//this.mapa = new MapaDavid("maps/mapadavid2.xml",utt,num_grupos);
		this.mapa = new Mapa8x8("maps/8x8/basesWorkers8x8A.xml",utt,num_grupos);
		
		this.largura = mapa.getLargura();
		this.altura = mapa.getAltura();
		this.camadas = mapa.getCamadas();
		
		input1 = new ArrayList<>();
		input2 = new ArrayList<>();
		out = new ArrayList<>();
		
		
		
		input1_play0 = new ArrayList<>();
		input2_play0 = new ArrayList<>();
		out_play0 = new ArrayList<>();
		
		input1_play1 = new ArrayList<>();
		input2_play1 = new ArrayList<>();
		out_play1 = new ArrayList<>();
		
		t_input1 = new ArrayList<>();
		t_input2 = new ArrayList<>();
		t_out = new ArrayList<>();
		
		for(int i =0;i<tam_bufferRNA;i++) {
			t_input1.add(null);
			t_input2.add(null);
			t_out.add(null);
		}
		
	}

	

	public void seleciona_exemplo_vencedor(int vencedor) {
		
		if(vencedor==0) {
			for(int i=0;i<input1_play0.size();i++) {
				input1.add(input1_play0.get(i));
				input2.add(input2_play0.get(i));
				out.add(out_play0.get(i));
			}
		}
		
		if(vencedor==1) {
			for(int i=0;i<input1_play1.size();i++) {
				input1.add(input1_play1.get(i));
				input2.add(input2_play1.get(i));
				out.add(out_play1.get(i));
			}
		}
		
		
		out_play1.clear();
		input1_play1.clear();
		input2_play1.clear();
		
		out_play0.clear();
		input1_play0.clear();
		input2_play0.clear();
		
	}
	
	
	
	public void treina() {
		int num_exemplos = input1.size();
		float[][][][] pre_input1 = new float[num_exemplos][altura][largura][camadas];
		float[][] pre_input2 = new float[num_exemplos][altura*largura];
		float[][][] pre_out=new float[num_exemplos][altura*largura][num_grupos+1];
		
		

		for(int i =0;i<num_exemplos;i++) {
			pre_input1[i] = input1.get(i);
			pre_input2[i] = input2.get(i);
			pre_out[i] = out.get(i);
		}
		
		
		
		
		Tensor tensor_input1= Tensor.create(pre_input1);
		Tensor tensor_input2= Tensor.create(pre_input2);
		Tensor tensor_out = Tensor.create(pre_out);
		
		if(num_exemplos>0) {
			t_input1.set(n, tensor_input1);
			t_input2.set(n, tensor_input2);
			t_out.set(n, tensor_out);
		}
		else {
			t_input1.set(n, null);
			t_input2.set(n, null);
			t_out.set(n, null);
			
		}
		
		input1.clear();
		input2.clear();
		out.clear();
		
		
		Session sess = model.session();
		
		for(int i = 0;i<epoca;i++) {
			for (int j =0; j<tam_bufferRNA;j++) {
				if(t_input1.get(j)==null)continue;
				//System.out.println("treinando..........");
				
				sess.runner().addTarget("treina")
				  .feed("x", t_input1.get(n))
				  .feed("x2", t_input2.get(n))
				  .feed("out", t_out.get(n))
				  .run();
			}
		}
		n=(n+1)%tam_bufferRNA;
		
	}
	
	
	
public void grava(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) { // salva o estado
		
		float[][][] estado1 = mapa.montar_entrada(player,gs,false);
	// monta a saida
		float[] entrada2 = mapa.montar_entrada2(player, gs, false);
		float[][] saida = mapa.montar_saida(player,gs,false,agrup);
	
		
		
		// monta estado espelhado
		float[][][] estado1_espelhado = mapa.montar_entrada(player,gs,true);
		float[] entrada2_espelhado = mapa.montar_entrada2(player, gs, true);
		float[][] saida_espelhada = mapa.montar_saida(player,gs,true,agrup);
		
		
		
		if(player==0) {
			input1_play0.add(estado1);
			input2_play0.add(entrada2);
			out_play0.add(saida);
			
			input1_play0.add(estado1_espelhado);
			input2_play0.add(entrada2_espelhado);
			out_play0.add(saida_espelhada);
			
		}
		else  {
			input1_play1.add(estado1);
			input2_play1.add(entrada2);
			out_play1.add(saida);
			
			input1_play1.add(estado1_espelhado);
			input2_play1.add(entrada2_espelhado);
			out_play1.add(saida_espelhada);
			
		}
		
		
	}
	
	
	
	public void agrupa(int player, GameState gs,UnitTypeTable utt,HashMap<Integer, Integer> agrup) {

		
		agrup.clear();
		
		Tensor entrada1 = Tensor.create(mapa.montar_entrada(player, gs, false));
		Tensor entrada2 = Tensor.create(mapa.montar_entrada2(player, gs, false));
	
		Tensor<?> saida= model.session().runner().fetch("saida")
				  .feed("x", entrada1)
				  .feed("x2", entrada2)
				  .run().get(0);
				FloatBuffer dst=FloatBuffer.allocate(largura*altura*(num_grupos+2));
				
				saida.writeTo(dst);
				
			for(Unit u: gs.getUnits()) {
				if(player==u.getPlayer()) {
					double  max= -1;
					int index=-1;
					for(int i=0;i<num_grupos;i++) {
						int posicao =u.getY()*largura+u.getX();
						double aux = dst.get(posicao*(num_grupos+1)+i);
					//if(gs.getTime()==0)System.out.print(aux+" ");
						if(aux>max) {
							index=i;
							max=aux;
						}
					}
					//if(gs.getTime()==0)System.out.println();
					agrup.put((int) u.getID(), index);
				}
			}
		
		
		
	}
	
	
	
	
	
	public void amostra(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) {
		
		agrup.clear();
		
		Tensor entrada1 = Tensor.create( mapa.montar_entrada(player, gs, false));
		Tensor entrada2 = Tensor.create(mapa.montar_entrada2(player, gs, false));
		
		Tensor<?> saida= model.session().runner().fetch("saida")
				  .feed("x", entrada1)
				  .feed("x2", entrada2)
				  .run().get(0);
				FloatBuffer dst=FloatBuffer.allocate(largura*altura*(num_grupos+2));
				saida.writeTo(dst);
		
		
				for(Unit u: gs.getUnits()) {
					if(player==u.getPlayer()) {
						int  soma = 0;
						for(int i=0;i<num_grupos;i++) {
							int posicao =u.getY()*largura+u.getX();
							double aux = dst.get(posicao*(num_grupos+1)+i);
							soma+=aux*10000;
						}
						
						Random gerador;
						gerador = new Random();
						
						int g =gerador.nextInt(soma);
						
						int index =0;
						soma =0;
						
						for(int i=0;i<num_grupos;i++) {
							int posicao =u.getY()*largura+u.getX();
							double aux = dst.get(posicao*(num_grupos+1)+i);
							soma+=aux*10000;
							if(soma>=g) {
								index=i;
								break;
							}
						}
						
						g =gerador.nextInt(100);// epsilon guloso;
						 
						if(g<5) {
							index=g =gerador.nextInt(1000)%num_grupos;
						}
						
						agrup.put((int) u.getID(), index);
						
						
					}
				}
		
		
	}
	
	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
	
		
		SavedModelBundle model = SavedModelBundle.load("resources/mod", "serve"); 
		
		
		float[][][] b = new float[8][18][8];
		float[] b2 = new float[8*18];
		float[][][] b12 = new float[8][18][8];
		float[] b22 = new float[8*18];
		float erer[][][][] = new float[2][8][18][8];
		float teste[][] = new float[2][8*18];
		erer[0]=b;
		
		erer[1]=b12;
		
		
		for(int i=0;i<18;i++) {
			b2[i]=10000.0f;
			b22[143-i]=10000.0f;
		}
		teste[0]=b2;
		teste[1]=b22;
		
		
		Tensor t = Tensor.create(erer);
		Tensor t2 = Tensor.create(teste);
		
		List<Tensor<?>> saida= model.session().runner().fetch("saida")
		  .feed("x", t)
		  .feed("x2", t2)
		  .run();
		FloatBuffer dst=FloatBuffer.allocate(2*8*18*5);
		saida.get(0).writeTo(dst);
		System.out.println(saida.get(0));
		
		
		
		for(int i =144;i<144*2;i++) {
			for (int j =0;j<3;j++) {
				System.out.print(dst.get(i*3+j)+" ");
			}
			System.out.println("");
		}
		
	}
}
