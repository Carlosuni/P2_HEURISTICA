package models;

/*Representa los objetos PlazaCoche[][] que forman el objeto Parking*/
public class PlazaCoche {
	private boolean ocupada;
	int posX;
	int posY;
	Coche coche;
	
	
	
	public PlazaCoche() {
		super();
	}

	public PlazaCoche(boolean ocupada, int posX, int posY, Coche coche) {
		super();
		this.ocupada = ocupada;
		this.posX = posX;
		this.posY = posY;
		this.coche = coche;
	}

	public boolean isOcupada() {
		return ocupada;
	}

	public void setOcupada(boolean ocupada) {
		this.ocupada = ocupada;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public Coche getCoche() {
		return coche;
	}

	public void setCoche(Coche coche) {
		this.coche = coche;
	}
	
	
}
