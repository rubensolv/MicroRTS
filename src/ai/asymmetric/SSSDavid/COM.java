package ai.asymmetric.SSSDavid;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration.GraphBuilder;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.Builder;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.nn.conf.inputs.InputType;

import org.deeplearning4j.nn.modelimport.keras.KerasLayer;
import org.deeplearning4j.nn.modelimport.keras.KerasModel;
import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;


import rts.GameState;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;


import org.deeplearning4j.nn.conf.layers.samediff.SameDiffLambdaLayer;


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

	int largura, altura,camadas, num_grupos,tipo_mapa;
	
	
	public void salverna(String s) throws IOException {
		String path = "rna/rna_"+s+".zip";
		File locationToSave = new File(path); 
		model.save(locationToSave,false);
	}
	
	
	
	public void  carrega_modelo_python(String rnatipo_mapa) throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
		for(int i =1209;i<largura*altura+1209;i++) {
		 KerasLayer.registerLambdaLayer("lambda_"+i, new ExponentialLambda());
		}
		
		File arq = new File("resources/simple_mlp"+rnatipo_mapa+".h5");
		 String simpleMlp = arq.getPath();
				
				//new ClassPathResource("simple_mlp.h5").getFile().getPath();
		model =  new KerasModel().modelBuilder().modelHdf5Filename(simpleMlp).enforceTrainingConfig(false).buildModel().getComputationGraph();
				//KerasModelImport.importKerasModelAndWeights(simpleMlp);
		
		
		
	}
	
public void  carrega_modelo_java() {
		
		List<String> outs= new ArrayList<>();
		
		// olhe https://deeplearning4j.org/tutorials/01-multilayernetwork-and-computationgraph
		GraphBuilder graph = new Builder().seed(123).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).updater(new Nesterovs(0.9))
				.graphBuilder()
				.addInputs("input");
		
		for(int i =0 ; i < largura*altura;i++) {
			outs.add("out"+i);
			graph.addLayer("out"+i, new OutputLayer.Builder().nIn(camadas*altura*altura).nOut(num_grupos+1)
					.weightInit(WeightInit.XAVIER)
					.activation(Activation.SOFTMAX).lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).build()// n achei categorical_crossentropy
					, "input");
		}
		graph.setInputTypes(InputType.convolutionalFlat(altura, largura, camadas)).setNetworkOutputs(outs);
	
	
		model = new ComputationGraph( graph.build());
				
	}
	
	//learningRate(0.1).iterations(1).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).updater(new Nesterovs(0.9))
	public COM( UnitTypeTable utt, int buffer,int epoc, String rnatipo_mapa, int largura, int altura,  int num_grupos,int tipo_mapa) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		
		tam_bufferRNA = buffer;
		epoca=epoc;
		
		// entrada
		this.largura=largura;
		this.altura = altura;
		
		this.num_grupos = num_grupos;
		this.tipo_mapa=tipo_mapa;
		
		workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
		 recurso = utt.getUnitType("Resource");
		
		  mapea =new HashMap<UnitType, Integer>();
		  if(tipo_mapa==0) {
			  this.camadas = 8;
			  /*
			   camada 0 = hp heavy
			   camada 2 = se tem heavy
			   camada 1 = hp range
			   camada 3 = se tem range
			   camdada 4-7 = idem para o outro
			   */
			  
			//	 mapea.put(workerType, 0);
				// mapea.put(barracksType, 1);
			//	mapea.put(baseType, 1);
			//	 mapea.put(lightType, 3);
				 mapea.put(heavyType, 0);
				 mapea.put(rangedType, 1);
				// mapea.put(recurso, 6);
		
		  }
		  else if(tipo_mapa==1) {
			  camadas=7;
			  /*
			   camada 0 = hp base
			   camada 1 = se tem worker
			   camada 2 = se o worker esta com recurso
			   camdada 3-6 = idem para o outro
			   camada 7 = hp do recurso
			   */
			  
				 mapea.put(workerType, 1);
				// mapea.put(barracksType, 1);
			mapea.put(baseType, 0);
			//	 mapea.put(lightType, 3);
				// mapea.put(heavyType, 0);
				 //mapea.put(rangedType, 1);
				mapea.put(recurso, 2);
		
		  }  
		  
		  
		carrega_modelo_python(rnatipo_mapa);
		
		  //carrega_modelo_java();
		
		
		
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
	
	public class ExponentialLambda extends SameDiffLambdaLayer {
        @Override
        public SDVariable defineLayer(SameDiff sd, SDVariable x) { return x.mul(x); }

        @Override
        public InputType getOutputType(int layerIndex, InputType inputType) { return inputType; }
    }

    public class TimesThreeLambda extends SameDiffLambdaLayer {
        @Override
        public SDVariable defineLayer(SameDiff sd, SDVariable x) { return x.mul(3); }

        @Override
        public InputType getOutputType(int layerIndex, InputType inputType) { return inputType; }
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
	
		agrup.clear();
		
		
		INDArray estado = montar_entrada(player,gs,false);
		
		INDArray estado2= montar_entrada2(player,gs,false);
 
		
	
		
		
		INDArray aux= Nd4j.create(1,camadas,altura,largura);
		INDArray aux3= Nd4j.create(1,1,8,18);
		aux.putRow(0, estado);
		aux3.putRow(0,estado2);
		INDArray[] aux2= model.output(aux,aux3);
		
		for(Unit u : gs.getUnits()) {
			if(u.getPlayer()==player) {
				double maior=-1;
				int index=-1;
				INDArray ind= aux2[u.getX()+largura*u.getY()];
				if(gs.getTime()==0)System.out.println(ind.shapeInfoToString());
				
				for(int i=0;i<num_grupos;i++) {
					if(maior<ind.getDouble(0,i)) {
						maior = ind.getDouble(0,i);
						index=i;
					
					}
					
				}
				
				agrup.put((int) u.getID(), index);
				
				
			}
		
		}
		
			
	}
	

	public INDArray montar_entrada(int player,GameState gs,boolean espelhado) {
		INDArray estado = Nd4j.zeros(camadas, altura,largura);// estado
		
		
		if(tipo_mapa==0) { // para os mapas david
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					if(espelhado==false) {
						estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
						estado.putScalar(2+mapea.get(u.getType()),u.getY(),u.getX(), 1);
					}
					else {
						estado.putScalar(mapea.get(u.getType()),u.getY(),(largura-u.getX()-1), (1.0*u.getHitPoints())/4.0);
						estado.putScalar(mapea.get(u.getType())+2,u.getY(),(largura-u.getX()-1), 1);
					}
					
				}
				else if (u.getPlayer()== 1 - player){
					if(espelhado==false) {
						estado.putScalar(4+mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints())/4);
						estado.putScalar(6+mapea.get(u.getType()),u.getY(),u.getX(), 1);
					}
					else {
						estado.putScalar(4+mapea.get(u.getType()),u.getY(),largura-u.getX()-1, (1.0*u.getHitPoints())/4.0);
						estado.putScalar(6+mapea.get(u.getType()),u.getY(),largura-u.getX()-1, 1);
					}
				}
			}
			
		}
		
		
		if(tipo_mapa==1) { // para os mapas david
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					if(espelhado==false) {
						estado.putScalar(mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints()));
						if(u.getType()==workerType) {
							estado.putScalar(2,u.getY(),u.getX(), u.getHarvestAmount());
						}
						
					}
					else {
						estado.putScalar(mapea.get(u.getType()),altura - u.getY()-1,(largura-u.getX()-1), (1.0*u.getHitPoints()));
						if(u.getType()==workerType) {
							estado.putScalar(2,altura - u.getY()-1,(largura-u.getX()-1), u.getHarvestAmount());
						}
					}
					
				}
				else if (u.getPlayer()== 1 - player){
					if(espelhado==false) {
						estado.putScalar(3+mapea.get(u.getType()),u.getY(),u.getX(), (1.0*u.getHitPoints()));
						if(u.getType()==workerType) {
							estado.putScalar(5,u.getY(),u.getX(), u.getHarvestAmount());
						}
					}
					else {
						estado.putScalar(3+mapea.get(u.getType()),altura - u.getY()-1,(largura-u.getX()-1), (1.0*u.getHitPoints()));
						if(u.getType()==workerType) {
							estado.putScalar(5,altura - u.getY()-1,(largura-u.getX()-1), u.getHarvestAmount());
						}
					}
				}
			}
			
		}
		
		
		return estado;
		
		
	}
	
	public INDArray montar_entrada2(int player,GameState gs,boolean espelhado) {
		INDArray posicao = Nd4j.ones(1,altura,largura);
		for(int i =0;i<altura;i++) {
			for(int j =0 ; j<largura;j++)
				posicao.putScalar(0,i,j, 10000.0);
		}
		
		
		return posicao;
	}
	
	
    public INDArray montar_saida(int player,GameState gs,boolean espelhado,HashMap<Integer, Integer> agrup) {
    	INDArray s1 = Nd4j.zeros( largura*altura,1);
		INDArray saida = Nd4j.zeros( largura*altura,num_grupos);
		saida=Nd4j.concat(1,saida,s1);//saida do estado
		
		if(tipo_mapa==0) {
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					//System.out.println(grup);
					
					
					if(espelhado==false) {
						saida.putScalar(u.getY()*largura+u.getX(),num_grupos, 0);
						saida.putScalar(u.getY()*largura+u.getX(),agrup.get((int)u.getID()), 1);
					
					}
					//vira o mapa
					else {
						saida.putScalar(u.getY()*largura+(largura-u.getX()-1),num_grupos, 0);
						saida.putScalar(u.getY()*largura+(largura-u.getX()-1),agrup.get((int)u.getID()), 1);
						
					}
					
				}
			}
		}
		
		if(tipo_mapa==1) {
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					//System.out.println(grup);
					
					
					if(espelhado==false) {
						saida.putScalar(u.getY()*largura+u.getX(),num_grupos, 0);
						saida.putScalar(u.getY()*largura+u.getX(),agrup.get((int)u.getID()), 1);
						
					}
					//vira o mapa
					else { 
						int x = (largura-u.getX()-1);
						int y = altura-u.getY()-1;
						int index = y*largura+x;
						saida.putScalar(index,num_grupos, 0);
						
						saida.putScalar(index,agrup.get((int)u.getID()), 1);
						
					}
					
				}
			}
		}
		
		return saida;
    	
    }

 	public void grava(int player, GameState gs, UnitTypeTable utt, HashMap<Integer, Integer> agrup) { // salva o estado
		
		INDArray estado = montar_entrada(player,gs,false);
	// monta a saida
		
		INDArray saida4 = montar_saida(player,gs,false,agrup);
	
		
		
		// monta estado espelhado
		INDArray estado2 = montar_entrada(player,gs,true);
		
		INDArray s12 = Nd4j.zeros( largura*altura,1);
		
		INDArray saida42 = montar_saida(player,gs,true,agrup);
		
		
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
		
		INDArray estado = montar_entrada(player,gs,false);
		

		agrup.clear();
		
		
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
				 int g = 0;
				 for(int i =0;i<num_grupos;i++) {
					 g+= (int) (10000*ind.getDouble(0,i)); // a probabiliade da unidade esta no grupo 1 ou 2
				 } 
				 if(g==0)g=1;
		
				 g =gerador.nextInt(g); // sorteia um numeno aleatorio e define qual grupo a unidade vai pertencer 
			
				 
				 int index=0;
				 
				 double sum =0;
				 for(int i =0;i<num_grupos;i++) {
					 sum+=(int) (10000*ind.getDouble(0,i));
					 if(sum>g) {
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

}
