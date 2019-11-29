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
	List<MultiDataSet> db; // salva os buffer de dados
	int n;// numero de treino ja feito 
	int tam_bufferRNA = 1;
	int epoca;

	
	
	
	public COM(int buffer,int epoc) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		//ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:app-context.xml");
		tam_bufferRNA = buffer;
		epoca=epoc;
		File arq = new File("resources/simple_mlp.h5");
		 String simpleMlp = arq.getPath();
				
				//new ClassPathResource("simple_mlp.h5").getFile().getPath();
		model =  KerasModelImport.importKerasModelAndWeights(simpleMlp);
		 inp=new ArrayList<>();
		 inp.add(Nd4j.create(0,8,8,18));
		 out=new ArrayList<>();
		 for(int tt=0;tt<144;tt++)out.add( Nd4j.create(0,3));
		 
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
			if(db.get(i).numLabelsArrays()==0)continue;
			db.get(i).shuffle();
			model.fit(db.get(i));
		}
	
	 inp=new ArrayList<>();
	 inp.add(Nd4j.create(0,8,8,18));
	 out=new ArrayList<>();
	 for(int tt=0;tt<144;tt++)out.add( Nd4j.create(0,3));
	 
	n= (n+1) % tam_bufferRNA;
	}
	
	
	public void  agrupa(int player, GameState gs,UnitTypeTable utt,HashMap<Integer, Integer> agrup) {
		//pega o agrupamendo gerado pela rna
		int linha = 8;
		int coluna= 18;
		int camadas = 8;
		INDArray estado = Nd4j.zeros(camadas, linha,coluna);
		

		agrup.clear();
		
			workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
		 recurso = utt.getUnitType("Resource");
		
		 HashMap<UnitType, Integer> mapea =new HashMap<UnitType, Integer>();
		// mapea.put(workerType, 0);
		// mapea.put(barracksType, 1);
		// mapea.put(baseType, 2);
	//	 mapea.put(lightType, 3);
		 mapea.put(heavyType, 0);
		 mapea.put(rangedType, 1);
		// mapea.put(recurso, 6);
		 
		
		
		
		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(2+mapea.get(u.getType()),u.getY(),u.getX(), 1);
				
			}
			else {
				estado.putScalar(4+mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(6+mapea.get(u.getType()),u.getY(),u.getX(), 1);
			
			}
			
		}
		
		INDArray aux= Nd4j.create(1,8,8,18);
		aux.putRow(0, estado);
		INDArray[] aux2= model.output(aux);
		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				INDArray ind= aux2[u.getX()+18*u.getY()];
				//System.out.print(ind);
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
		int linha = 8;
		int coluna= 18;
		int camadas =8;
		INDArray estado = Nd4j.zeros(camadas, linha,coluna);// estado
		
		INDArray s1 = Nd4j.ones( 18*8,1);
		
		INDArray saida4 = Nd4j.zeros( 18*8,2);
		saida4=Nd4j.concat(1,saida4,s1);//saida do estado
		
		
		INDArray estado2 = Nd4j.zeros(camadas, linha,coluna);// estado espelhado
		
		INDArray s12 = Nd4j.ones( 18*8,1);
		
		INDArray saida42 = Nd4j.zeros( 18*8,2);
		saida42=Nd4j.concat(1,saida42,s12); //saida do estado espelhada 
		
		
		
			workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
		 recurso = utt.getUnitType("Resource");
		
		 HashMap<UnitType, Integer> mapea =new HashMap<UnitType, Integer>();
		// mapea.put(workerType, 0);
		// mapea.put(barracksType, 1);
		// mapea.put(baseType, 2);
	//	 mapea.put(lightType, 3);
		 mapea.put(heavyType, 0);
		 mapea.put(rangedType, 1);
		// mapea.put(recurso, 6);
		 
		
		
		
		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				//System.out.println(grup);
				
				
				estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4.0);
				estado.putScalar(mapea.get(u.getType())+2,u.getY(),u.getX(), 1);
				saida4.putScalar(u.getY()*18+u.getX(),2, 0);
				saida4.putScalar(u.getY()*18+u.getX(),agrup.get((int)u.getID()), 1);
				saida4.putScalar(u.getY()*18+u.getX(),1-agrup.get((int)u.getID()), 0);

				//vira o mapa
				estado2.putScalar(mapea.get(u.getType()),u.getY(),(coluna-u.getX()-1), (1.0*u.getHitPoints())/4.0);
				estado2.putScalar(mapea.get(u.getType())+2,u.getY(),(coluna-u.getX()-1), 1);
				saida42.putScalar(u.getY()*18+(coluna-u.getX()-1),2, 0);
				saida42.putScalar(u.getY()*18+(coluna-u.getX()-1),agrup.get((int)u.getID()), 1);
				saida42.putScalar(u.getY()*18+(coluna-u.getX()-1),1-agrup.get((int)u.getID()), 0);
	
				
			}
			else {
				estado.putScalar(4+mapea.get(u.getType()),u.getY(),(coluna-u.getX()-1), (1.0*u.getHitPoints())/4.0);
				estado.putScalar(6+mapea.get(u.getType()),u.getY(),(coluna-u.getX()-1),1);
				//vira o mapa
				estado2.putScalar(4+mapea.get(u.getType()),u.getY(),coluna-u.getX()-1, (1.0*u.getHitPoints())/4.0);
				estado2.putScalar(6+mapea.get(u.getType()),u.getY(),coluna-u.getX()-1, 1);
				
				
			}
			
		}
		
		// salva o mapa e a saida
		INDArray aux= Nd4j.create(1,8,8,18);
		aux.putRow(0, estado);
		
		inp.set(0, Nd4j.concat(0, aux,inp.get(0)));

		for(int tt=0;tt<144;tt++) {
			aux = Nd4j.create(1,3);
			aux.putRow(0,saida4.getRow(tt));
			out.set(tt, Nd4j.concat(0, aux,out.get(tt)));
		}
		
		// salva o mapa espelhado e a saida
		INDArray aux2= Nd4j.create(1,8,8,18);
		aux2.putRow(0, estado);
		
		inp.set(0, Nd4j.concat(0, aux2,inp.get(0)));

		for(int tt=0;tt<144;tt++) {
			aux2 = Nd4j.create(1,3);
			aux2.putRow(0,saida42.getRow(tt));
			out.set(tt, Nd4j.concat(0, aux2,out.get(tt)));
		}
		
		
		
		
	}

	public void amostra(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) {
		int linha = 8;
		int coluna= 18;
		int camadas = 8;
		INDArray estado = Nd4j.zeros(camadas, linha,coluna);// canal
		

		agrup.clear();
		
			workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
		 recurso = utt.getUnitType("Resource");
		
		 HashMap<UnitType, Integer> mapea =new HashMap<UnitType, Integer>();
		// mapea.put(workerType, 0);
		// mapea.put(barracksType, 1);
		// mapea.put(baseType, 2);
	//	 mapea.put(lightType, 3);
		 mapea.put(heavyType, 0);
		 mapea.put(rangedType, 1);
		// mapea.put(recurso, 6);
		 
		
		
		
		// constroi o estado
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(mapea.get(u.getType())+2,u.getY(),u.getX(), 1);
				
			}
			else {
				estado.putScalar(4+mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
				estado.putScalar(6+mapea.get(u.getType()),u.getY(),u.getX(), 1);
			
			}
			
		}
		
		// encapsula para passar pela rna
		INDArray aux= Nd4j.create(1,8,8,18);
		aux.putRow(0, estado);
		INDArray[] aux2= model.output(aux); // saida da rede neural
		
		for(Unit u : gs.getUnits()) { // amostra os grupos com relação a saida da RNA, 
			if(u.getPlayer()==player) {
				INDArray ind= aux2[u.getX()+18*u.getY()];
				//System.out.print(ind);
				Random gerador;
				 gerador = new Random();
				 int g = (int) (100*(ind.getDouble(0,0)+ind.getDouble(0,1))); // a probabiliade da unidade esta no grupo 1 ou 2
				 if(g==0)g=1;
		
				 g =gerador.nextInt(g); // sorteia um numeno aleatorio e define qual grupo a unidade vai pertencer 
				if(ind.getDouble(0,0)*100>g) {
					
					agrup.put((int) u.getID(), 0);
				
				}
				else {
					agrup.put((int) u.getID(), 1);
				
					
				}
				
			}
		
		}
		
	}

}
