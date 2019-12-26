package ai.asymmetric.SSSDavid;

import java.util.HashMap;

import org.tensorflow.Tensor;

import rts.GameState;

public interface ConfMapa {
	
	
	
	
	
	public float[][][] montar_entrada(int player,GameState gs,boolean espelhado);
	public float[] montar_entrada2(int player,GameState gs,boolean espelhado);
	public float[][] montar_saida(int player,GameState gs,boolean espelhado,HashMap<Integer, Integer> agrup);
	
	public int getLargura();
	public int getAltura();
	public int getNumGrupos();
	public int getCamadas();

}
