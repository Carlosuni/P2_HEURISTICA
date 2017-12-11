package models;

public class Coche {
	private int id;
	private int posX;
	private int poY;
	private String categoria;
	private int ordenLlegada;
	private boolean bloqueadoIzda;
	private boolean bloqueadoDcha;
	private boolean bloqueado;
	
	public Coche() {
		
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
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
	
}
