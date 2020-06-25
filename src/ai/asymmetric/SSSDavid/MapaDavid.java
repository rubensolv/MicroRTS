package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.JDOMException;

import org.tensorflow.Tensor;

import rts.GameState;
import rts.PhysicalGameState;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class MapaDavid implements ConfMapa {
	 UnitType workerType;
	 UnitType baseType;
	 UnitType barracksType;
	 UnitType lightType;
	 UnitType heavyType;
	 UnitType rangedType;
	 UnitType recurso;
	 PhysicalGameState pgs;
	 int num_grupos,largura,altura,camadas;
	 HashMap<UnitType, Integer> mapea;
	

	public MapaDavid(String path_mapa,UnitTypeTable utt,int num_grupos) throws JDOMException, IOException, Exception {
		pgs = PhysicalGameState.load(path_mapa, utt);
		this.num_grupos = num_grupos;
		this.largura = pgs.getWidth();
		this.altura = pgs.getHeight();
		
		workerType = utt.getUnitType("Worker");
	       baseType = utt.getUnitType("Base");
	       barracksType = utt.getUnitType("Barracks");
	       rangedType = utt.getUnitType("Ranged");
	       heavyType = utt.getUnitType("Heavy");
	       lightType = utt.getUnitType("Light");
		 recurso = utt.getUnitType("Resource");
		
		  mapea =new HashMap<UnitType, Integer>();
		
		  this.camadas = 8;
		  /*
		   camada 0 = hp heavy
		   camada 2 = se tem heavy
		   camada 1 = hp range
		   camada 3 = se tem range
		   camdada 4-7 = idem para o outro
		   */
		  
			 mapea.put(heavyType, 0);
			 mapea.put(rangedType, 1);
			
	}

	@Override
	public float[][][]  montar_entrada(int player, GameState gs, boolean espelhado) {

			float estado[][][]=new float[camadas][altura][largura];
		
		
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					if(espelhado==false) {
						
						estado[mapea.get(u.getType())][u.getY()][u.getX()]= 1.0f*u.getHitPoints()/4;
						estado[mapea.get(u.getType())+2][u.getY()][u.getX()]= 1.0f;
						
					}
					else {
				
						estado[mapea.get(u.getType())][u.getY()][largura-u.getX()-1]= 1.0f*u.getHitPoints()/4;
						estado[mapea.get(u.getType())+2][u.getY()][largura-u.getX()-1]= 1.0f;
					}
					
				}
				else if (u.getPlayer()== 1 - player){
					if(espelhado==false) {
						
						estado[mapea.get(u.getType())+4][u.getY()][u.getX()]= 1.0f*u.getHitPoints()/4;
						estado[mapea.get(u.getType())+6][u.getY()][u.getX()]= 1.0f;
						
					}
					else {
				
						estado[mapea.get(u.getType())+4][u.getY()][largura-u.getX()-1]= 1.0f*u.getHitPoints()/4;
						estado[mapea.get(u.getType())+6][u.getY()][largura-u.getX()-1]= 1.0f;
					}
				}
			
			
			}
		
		return estado;
	}

	
	@Override
	public HashMap<Integer, Integer>  montar_entrada2(int player, GameState gs, boolean espelhado) {
		
		HashMap<Integer, Integer> input2 = new HashMap<Integer, Integer>() ;
	
		for(Unit u: gs.getUnits()) {
			if(player==u.getPlayer()) {
				input2.put((int) u.getID(), u.getY()*largura+u.getX());
				
			}
		}
		
		
		return input2;
	}
	
	
	@Override
	public float[][] montar_saida(int player, GameState gs, boolean espelhado,HashMap<Integer, Integer> agrup) {


		float saida[][] = new float[altura*largura][num_grupos+1];
		
		for(int i =0;i<largura*altura;i++) {
			saida[i][num_grupos]=1;
		}
		
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					//System.out.println(grup);
					
					if(espelhado==false) {
						
						saida[u.getY()*largura+u.getX()][num_grupos]=0;
						saida[u.getY()*largura+u.getX()][agrup.get((int)u.getID())]=1;
					
					}
					//vira o mapa
					else {
					
						int aux = largura+(u.getY()*largura-u.getX()-1);
						saida[aux][num_grupos]=0;
						saida[aux][agrup.get((int)u.getID())]=1;
						
					}
					
				}
			}
		
		
		
		return saida;
	}

	
	
	
	
	
	@Override
	public int getLargura() {
		// TODO Auto-generated method stub
		return largura;
	}

	@Override
	public int getAltura() {
		// TODO Auto-generated method stub
		return altura;
	}

	@Override
	public int getNumGrupos() {
		// TODO Auto-generated method stub
		return num_grupos;
	}

	@Override
	public int getCamadas() {
		// TODO Auto-generated method stub
		return camadas;
	}

	
	

}
