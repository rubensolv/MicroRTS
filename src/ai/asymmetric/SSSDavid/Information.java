package ai.asymmetric.SSSDavid;

public class Information {
	public int contruindo_barraca;
	public int contruindo_base;
	public int barraca_tempo;
	public int base_tempo;
	public float tempo_playout;
	double inicio_playout;
	public int cont;
	public Information() {
		contruindo_barraca = 0;
		barraca_tempo = -10000;
		contruindo_base = 0;
		base_tempo = -10000;
		tempo_playout = 20;
		cont =44;
	}
	
	public void copia(Information inf) {
		contruindo_barraca = inf.contruindo_barraca;
		barraca_tempo = inf.barraca_tempo;
		contruindo_base = inf.contruindo_base;
		base_tempo = inf.base_tempo;
	}

}
