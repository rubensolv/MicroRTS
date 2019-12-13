package ai.asymmetric.SSSDavid;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;

import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import rts.GameState;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;


import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;


/**A Simple Multi Layered Perceptron (MLP) applied to digit classification for
 * the MNIST Dataset (http://yann.lecun.com/exdb/mnist/).
 *
 * This file builds one input layer and one hidden layer.
 *
 * The input layer has input dimension of numRows*numColumns where these variables indicate the
 * number of vertical and horizontal pixels in the image. This layer uses a rectified linear unit
 * (relu) activation function. The weights for this layer are initialized by using Xavier initialization
 * (https://prateekvjoshi.com/2016/03/29/understanding-xavier-initialization-in-deep-neural-networks/)
 * to avoid having a steep learning curve. This layer will have 1000 output signals to the hidden layer.
 *
 * The hidden layer has input dimensions of 1000. These are fed from the input layer. The weights
 * for this layer is also initialized using Xavier initialization. The activation function for this
 * layer is a softmax, which normalizes all the 10 outputs such that the normalized sums
 * add up to 1. The highest of these normalized values is picked as the predicted class.
 *
 */
public class COM {
   
	 UnitType workerType;
	 UnitType baseType;
	 UnitType barracksType;
	 UnitType lightType;
 UnitType heavyType;
	 UnitType rangedType;
 UnitType recurso;
	ComputationGraph model;
	List<INDArray> inp; // recolhe entrada da rna
	List<INDArray> out; //saida 
	List<INDArray> inp_play0; // recolhe entrada da rna
	List<INDArray> out_play0; //saida 
	List<INDArray> inp_play1; // recolhe entrada da rna
	List<INDArray> out_play1; //saida 
	List<MultiDataSet> db; // salva os buffer de dados
	int n;// numero de treino ja feito 
	int tam_bufferRNA = 1;
	int epoca;
	HashMap<UnitType, Integer> mapea;

	int largura, altura,camadas, num_grupos;
	
	
	public void salverna(String s) throws IOException {
		String path = "rna/rna_"+s+".zip";
		File locationToSave = new File(path); 
		model.save(locationToSave,false);
	}
	
	public COM( UnitTypeTable utt, int buffer,int epoc, String rnaopcao, int largura, int altura, int camadas, int num_grupos) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		
		tam_bufferRNA = buffer;
		epoca=epoc;
		
		// entrada
		this.largura=largura;
		this.altura = altura;
		this.camadas = camadas;
		this.num_grupos = num_grupos;
		
		workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
		 recurso = utt.getUnitType("Resource");
		
		  mapea =new HashMap<UnitType, Integer>();
	//	 mapea.put(workerType, 0);
		// mapea.put(barracksType, 1);
	//	mapea.put(baseType, 1);
	//	 mapea.put(lightType, 3);
		 mapea.put(heavyType, 0);
		 mapea.put(rangedType, 1);
		// mapea.put(recurso, 6);
		
		
		
		File arq = new File("resources/simple_mlp"+rnaopcao+".h5");
		 String simpleMlp = arq.getPath();
				
				//new ClassPathResource("simple_mlp.h5").getFile().getPath();
		model =  KerasModelImport.importKerasModelAndWeights(simpleMlp);
		
		
		 inp=new ArrayList<>();
		 inp.add(Nd4j.create(0,camadas,altura,largura));
		 out=new ArrayList<>();
		 
		 inp_play0=new ArrayList<>();
		 inp_play0.add(Nd4j.create(0,camadas,altura,largura));
		 out_play0=new ArrayList<>();
		 
		 inp_play1=new ArrayList<>();
		 inp_play1.add(Nd4j.create(0,camadas,altura,largura));
		 out_play1=new ArrayList<>();
		 
		 
		 for(int tt=0;tt<altura*largura;tt++) {
			 out_play0.add( Nd4j.create(0,num_grupos+1));
			 out.add( Nd4j.create(0,num_grupos+1));
			 out_play1.add( Nd4j.create(0,num_grupos+1));
			 }
		 
		 db = new ArrayList<>();
		 for(int i =0 ; i < tam_bufferRNA;i++) {
			 db.add(new MultiDataSet());
		 }
		 
		 
	
	}
	
	
	
	
	public void treina(boolean b) {
		
	
	INDArray[] x = new INDArray[inp.size()];
	inp.toArray(x);
	INDArray[] y = new INDArray[out.size()];
	out.toArray(y);
	
	 db.set(n, new MultiDataSet(x,y));	
	for(int j =0;j<epoca;j++)
		for(int i=0; i< tam_bufferRNA;i++) {
			
			if(db.get(i).numFeatureArrays()==0)continue;
			try {
			db.get(i).shuffle();
			model.fit(db.get(i));
			}catch (Exception e) {
				 ;
			}
			
		}
	
	 inp=new ArrayList<>();
	 inp.add(Nd4j.create(0,camadas,altura,largura));
	 out=new ArrayList<>();
	 for(int tt=0;tt<altura*largura;tt++)out.add( Nd4j.create(0,num_grupos + 1));
	 
	n= (n+1) % tam_bufferRNA;
	}
	
	
	public void  agrupa(int player, GameState gs,UnitTypeTable utt,HashMap<Integer, Integer> agrup) {
		//pega o agrupamendo gerado pela rna
	
		INDArray estado = Nd4j.zeros(camadas, altura,largura);
		

		agrup.clear();
		

		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(2+mapea.get(u.getType()),u.getY(),u.getX(), 1);
				
			}
			else if (u.getPlayer()== 1 - player){
			
				estado.putScalar(4+mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(6+mapea.get(u.getType()),u.getY(),u.getX(), 1);
			
			}
			
		}
		
		INDArray aux= Nd4j.create(1,camadas,altura,largura);
		aux.putRow(0, estado);
		INDArray[] aux2= model.output(aux);
		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				INDArray ind= aux2[u.getX()+largura*u.getY()];
				if(gs.getTime()==0)System.out.println(ind);
				
				if(ind.getDouble(0,0)>ind.getDouble(0,1)) {
					agrup.put((int) u.getID(), 0);
				}
				else {
					agrup.put((int) u.getID(), 1);
				}
				
			}
		
		}
		
		
	
		
	}
	
	
    

	public void grava(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) { // salva o estado
		
		INDArray estado = Nd4j.zeros(camadas, altura,largura);// estado
		
	// monta a saida
		INDArray s1 = Nd4j.zeros( largura*altura,1);
		INDArray saida4 = Nd4j.zeros( largura*altura,num_grupos);
		saida4=Nd4j.concat(1,saida4,s1);//saida do estado
		
		
		// monta estado espelhado
		INDArray estado2 = Nd4j.zeros(camadas, altura,largura);// estado espelhado
		
		INDArray s12 = Nd4j.zeros( largura*altura,1);
		
		INDArray saida42 = Nd4j.zeros( largura*altura,num_grupos);
		saida42=Nd4j.concat(1,saida42,s12); //saida do estado espelhada 
		
		
		
		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				//System.out.println(grup);
				
				
				estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4.0);
				estado.putScalar(mapea.get(u.getType())+2,u.getY(),u.getX(), 1);
				saida4.putScalar(u.getY()*altura+u.getX(),2, 0);
				saida4.putScalar(u.getY()*altura+u.getX(),agrup.get((int)u.getID()), 1);
				saida4.putScalar(u.getY()*altura+u.getX(),1-agrup.get((int)u.getID()), 0);

				//vira o mapa
				estado2.putScalar(mapea.get(u.getType()),u.getY(),(largura-u.getX()-1), (1.0*u.getHitPoints())/4.0);
				estado2.putScalar(mapea.get(u.getType())+2,u.getY(),(largura-u.getX()-1), 1);
				saida42.putScalar(u.getY()*altura+(largura-u.getX()-1),2, 0);
				saida42.putScalar(u.getY()*altura+(largura-u.getX()-1),agrup.get((int)u.getID()), 1);
				saida42.putScalar(u.getY()*altura+(largura-u.getX()-1),1-agrup.get((int)u.getID()), 0);
	
				
			}
			else if (u.getPlayer()==1-player){
				estado.putScalar(4+mapea.get(u.getType()),u.getY(),(largura-u.getX()-1), (1.0*u.getHitPoints())/4.0);
				estado.putScalar(6+mapea.get(u.getType()),u.getY(),(largura-u.getX()-1),1);
				//vira o mapa
				estado2.putScalar(4+mapea.get(u.getType()),u.getY(),largura-u.getX()-1, (1.0*u.getHitPoints())/4.0);
				estado2.putScalar(6+mapea.get(u.getType()),u.getY(),largura-u.getX()-1, 1);
				
				
			}
			
		}
		
		// salva o mapa e a saida
		INDArray aux= Nd4j.create(1,camadas,altura,largura);
		aux.putRow(0, estado);
		
		if(player==0)inp_play0.set(0, Nd4j.concat(0, aux,inp_play0.get(0)));
		else inp_play1.set(0, Nd4j.concat(0, aux,inp_play1.get(0)));
		
		for(int tt=0;tt<largura*altura;tt++) {
			aux = Nd4j.create(1,num_grupos + 1);
			aux.putRow(0,saida4.getRow(tt));
			if(player==0) out_play0.set(tt, Nd4j.concat(0, aux,out_play0.get(tt)));
			else out_play1.set(tt, Nd4j.concat(0, aux,out_play1.get(tt)));
		}
		
		// salva o mapa espelhado e a saida
		INDArray aux2= Nd4j.create(1,camadas,altura,largura);
		aux2.putRow(0, estado);
		
		if(player==0) inp_play0.set(0, Nd4j.concat(0, aux2,inp_play0.get(0)));
		else inp_play1.set(0, Nd4j.concat(0, aux2,inp_play1.get(0)));

		for(int tt=0;tt<largura*altura;tt++) {
			aux2 = Nd4j.create(1,num_grupos + 1);
			aux2.putRow(0,saida42.getRow(tt));
			if(player==0)  out_play0.set(tt, Nd4j.concat(0, aux2,out_play0.get(tt)));
			else out_play1.set(tt, Nd4j.concat(0, aux2,out_play1.get(tt)));
		}
		
		
		
	}

	public void seleciona_exemplo_vencedor(int vencedor) {
	
		if(vencedor==0) {
			inp.set(0, Nd4j.concat(0, inp.get(0),inp_play0.get(0)));
			for(int tt=0;tt<largura*altura;tt++) {
				out.set(tt, Nd4j.concat(0, out.get(tt),out_play0.get(tt)));
			}
		}
		
		if(vencedor==1) {
			inp.set(0, Nd4j.concat(0, inp.get(0),inp_play1.get(0)));
			for(int tt=0;tt<altura*largura;tt++) {
				out.set(tt, Nd4j.concat(0, out.get(tt),out_play1.get(tt)));
			}
		}
		
		
		 inp_play0=new ArrayList<>();
		 inp_play0.add(Nd4j.create(0,camadas,altura,largura));
		 out_play0=new ArrayList<>();
		 for(int tt=0;tt<altura*largura;tt++)out_play0.add( Nd4j.create(0,num_grupos +1));
		 
		 inp_play1=new ArrayList<>();
		 inp_play1.add(Nd4j.create(0,camadas,altura,largura));
		 out_play1=new ArrayList<>();
		 for(int tt=0;tt<altura*largura;tt++)out_play1.add( Nd4j.create(0,num_grupos + 1));
		
	}
	
	
	public void amostra(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) {
		
		INDArray estado = Nd4j.zeros(camadas, altura,largura);// canal
		

		agrup.clear();
		
			
		
		
		// constroi o estado
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				
				estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(mapea.get(u.getType())+2,u.getY(),u.getX(), 1);
				
			}
			else if(u.getPlayer()==1-player){
				estado.putScalar(4+mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(6+mapea.get(u.getType()),u.getY(),u.getX(), 1);
			
			}
			
		}
		
		// encapsula para passar pela rna
		INDArray aux= Nd4j.create(1,camadas,altura,largura);
		aux.putRow(0, estado);
		INDArray[] aux2= model.output(aux); // saida da rede neural
		
		for(Unit u : gs.getUnits()) { // amostra os grupos com relação a saida da RNA, 
			if(u.getPlayer()==player) {
				INDArray ind= aux2[u.getX()+largura*u.getY()];
				//System.out.print(ind);
				Random gerador;
				 gerador = new Random();
				 int g = (int) (100*(ind.getDouble(0,0)+ind.getDouble(0,1))); // a probabiliade da unidade esta no grupo 1 ou 2
				 if(g==0)g=1;
		
				 g =gerador.nextInt(g); // sorteia um numeno aleatorio e define qual grupo a unidade vai pertencer 
				if(ind.getDouble(0,0)*100>g) {
					
					
					g =gerador.nextInt(100);
					if(g<5) {
						agrup.put((int) u.getID(), 1);
					}
					else {
						agrup.put((int) u.getID(), 0);
					}
				
				}
				else {
					g =gerador.nextInt(100);
					if(g<5) {
						agrup.put((int) u.getID(), 0);
					}
					else {
						agrup.put((int) u.getID(), 1);
					}
				
					
				}
				
			}
		
		}
		
	}

}
