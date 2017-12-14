package modelo;

public class Coche {

	private boolean moverDer;
	private boolean moverIzq;
	private char dir;
	private int ordenLlegada = 0;
	private int coste = 0;
	private int coches = 0; // coches a mover para mover este coche

	public Coche(boolean moverDer, boolean moverIzq, char dir) {
		this.moverDer = moverDer;
		this.moverIzq = moverIzq;
		this.dir = dir;
	}

	public Coche() {

	}

	public Coche(char dir) {
		this.dir = dir;
		if (dir == '>') {
			this.moverDer = true;
			this.moverIzq = false;
		}
		if (dir == '<') {
			this.moverDer = false;
			this.moverIzq = true;
		}
	}

	public boolean isMoverDer() {
		return moverDer;
	}

	public void setMoverDer(boolean moverDer) {
		this.moverDer = moverDer;
	}

	public boolean isMoverIzq() {
		return moverIzq;
	}

	public void setMoverIzq(boolean moverIzq) {
		this.moverIzq = moverIzq;
	}

	public char getDir() {
		return dir;
	}

	public void setDir(char dir) {
		this.dir = dir;
	}

	public int getMovimientos() {
		return coste;
	}

	public void setCoste(int coste) {
		this.coste = coste;
	}

	public int getOrdenLlegada() {
		return ordenLlegada;
	}

	public void setOrdenLlegada(int ordenLlegada) {
		this.ordenLlegada = ordenLlegada;
	}

	public int getCoches() {
		return coches;
	}

	public void setCoches(int coches) {
		this.coches = coches;
	}

	public int getCoste() {
		return coste;
	}

	public void sumarMovimientos() {
		this.coste = this.coste + 1;
	}
}
