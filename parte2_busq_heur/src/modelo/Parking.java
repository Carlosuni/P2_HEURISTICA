package modelo;

public class Parking {

	private int filas;
	private int columnas;
	private Coche[][] coches;

	public Parking(int filas, int columnas, Coche[][] coches) {
		this.filas = filas;
		this.columnas = columnas;
		this.coches = coches;
	}

	public Parking() {

	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public int getColumnas() {
		return columnas;
	}

	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}

	public Coche[][] getCoches() {
		return coches;
	}

	public void setCoches(Coche[][] coches) {
		this.coches = coches;
	}

	public void addCoche(int fila, int columna, Coche coche) {
		coches[fila][columna] = coche;
	}
}
