package ai.asymmetric.SSSDavid;

import java.io.IOException;
import java.util.HashMap;

import org.jdom.JDOMException;

import rts.GameState;
import rts.PhysicalGameState;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class Mapa8x8 implements ConfMapa {
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
		
	
	public Mapa8x8(String path_mapa,UnitTypeTable utt,int num_grupos) throws JDOMException, IOException, Exception {
		// TODO Auto-generated constructor stub
		
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
		
		  this.camadas = 7;
		  
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

	@Override
	public float[][][] montar_entrada(int player, GameState gs, boolean espelhado) {
		float estado[][][]=new float[altura][largura][camadas];
		
			for(Unit u : gs.getUnits()) {
				if(u.getPlayer()==player) {
					if(espelhado==false) {
						estado[u.getY()][u.getX()][mapea.get(u.getType())]=1.0f*u.getHitPoints();
					
						if(u.getType()==workerType) {
			
							estado[u.getY()][u.getX()][mapea.get(u.getType())]=u.getHarvestAmount();
						}
						
					}
					else {
						int x =largura-u.getX()-1;
						int y = altura - u.getY()-1;
						
						estado[y][x][mapea.get(u.getType())]=1.0f*u.getHitPoints();
						if(u.getType()==workerType) {
							estado[y][x][2]= u.getHarvestAmount();
						}
					}
					
				}
				else if (u.getPlayer()== 1 - player){
					if(espelhado==false) {
						estado[u.getY()][u.getX()][mapea.get(u.getType())+3]=1.0f*u.getHitPoints();
						
						if(u.getType()==workerType) {
							estado[u.getY()][u.getX()][5]=u.getHarvestAmount();
						}
					}
					else {
						int x =largura-u.getX()-1;
						int y = altura - u.getY()-1;
						estado[y][x][mapea.get(u.getType())+3]=1.0f*u.getHitPoints();
						
						if(u.getType()==workerType) {
							estado[y][x][5]= u.getHarvestAmount();
						}
					}
				}else {
					if(espelhado==false) {
						estado[u.getY()][u.getX()][6]=u.getMaxHitPoints();
					}
					else {
						int x =largura-u.getX()-1;
						int y = altura - u.getY()-1;
						estado[y][x][6]=u.getMaxHitPoints();
						
					}
				}
			}
		return estado;
	}

	@Override
	public float[] montar_entrada2(int player, GameState gs, boolean espelhado) {
		// TODO Auto-generated method stub
		
		float entrada2[] = new float[altura*largura];
		for(int i =0;i<altura;i++) {
			for(int j =0 ; j<largura;j++)
				entrada2[i*largura+j]= 10000.0f;
		}
		for(Unit u: gs.getUnits()) {
			if(player==u.getPlayer()) {
				if(espelhado) {
					int x = (largura-u.getX()-1);
					int y = altura-u.getY()-1;
					int index = y*largura+x;
					entrada2[index]=-10000.0f;
				}
				else entrada2[u.getY()*largura+u.getX()]=-10000.0f;
			}
		}
		
		
		return entrada2;
	}

	@Override
	public float[][] montar_saida(int player, GameState gs, boolean espelhado, HashMap<Integer, Integer> agrup) {
		// TODO Auto-generated method stub
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
					
						int x = (largura-u.getX()-1);
						int y = altura-u.getY()-1;
						int aux = y*largura+x;
						
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
