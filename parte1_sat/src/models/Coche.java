package models;

public class Coche {
	private int id;
	private int posX;
	private int poY;
	private char categoria;
	private int ordenLlegada;
	private boolean bloqueadoIzda;
	private boolean bloqueadoDcha;
	private boolean bloqueado;
	
	public Coche() {
			
	}

	public Coche(int id, int posX, int poY, char categoria, int ordenLlegada) {
		this.id = id;
		this.posX = posX;
		this.poY = poY;
		this.categoria = categoria;
		this.ordenLlegada = ordenLlegada;
	}
	
	public Coche(int id, int posX, int poY) {
		this.id = id;
		this.posX = posX;
		this.poY = poY;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPoY() {
		return poY;
	}

	public void setPoY(int poY) {
		this.poY = poY;
	}

	public char getCategoria() {
		return categoria;
	}

	public void setCategoria(char categoria) {
		this.categoria = categoria;
	}

	public int getOrdenLlegada() {
		return ordenLlegada;
	}

	public void setOrdenLlegada(int ordenLlegada) {
		this.ordenLlegada = ordenLlegada;
	}

	public boolean isBloqueadoIzda() {
		return bloqueadoIzda;
	}

	public void setBloqueadoIzda(boolean bloqueadoIzda) {
		this.bloqueadoIzda = bloqueadoIzda;
	}

	public boolean isBloqueadoDcha() {
		return bloqueadoDcha;
	}

	public void setBloqueadoDcha(boolean bloqueadoDcha) {
		this.bloqueadoDcha = bloqueadoDcha;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	@Override
	public String toString() {
		return "Coche [id=" + id + ", posX=" + posX + ", poY=" + poY + ", categoria=" + categoria + ", ordenLlegada="
				+ ordenLlegada + ", bloqueadoIzda=" + bloqueadoIzda + ", bloqueadoDcha=" + bloqueadoDcha
				+ ", bloqueado=" + bloqueado + "]";
	}
	
	
	
}
