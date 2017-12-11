package models;

public class Parking {
	private String rutaArchivo;
	private int numCalles;
	private int numHuecosCalle;
	private Coche[][] coches;
	private boolean existenBloqueados;

	public Parking() {
		
	}

	public String getRutaArchivoTXT() {
		return rutaArchivo;
	}

	public void setRutaArchivoTXT(String rutaArchivoTXT) {
		this.rutaArchivo = rutaArchivoTXT;
	}

	public int getNumCalles() {
		return numCalles;
	}

	public void setNumCalles(int numCalles) {
		this.numCalles = numCalles;
	}

	public int getNumHuecosCalle() {
		return numHuecosCalle;
	}

	public void setNumHuecosCalle(int numHuecosCalle) {
		this.numHuecosCalle = numHuecosCalle;
	}

	public Coche[][] getCoches() {
		return coches;
	}

	public void setCoches(Coche[][] coches) {
		this.coches = coches;
	}

	public boolean isExistenBloqueados() {
		return existenBloqueados;
	}

	public void setExistenBloqueados(boolean existenBloqueados) {
		this.existenBloqueados = existenBloqueados;
	}
	
}
