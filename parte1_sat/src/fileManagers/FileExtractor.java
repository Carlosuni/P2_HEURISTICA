package fileManagers;

import models.Parking;

public class FileExtractor {
	private String nombreArchivo;
	private String rutaArchivo;
	
	public FileExtractor() {
		
	}
	
	public FileExtractor(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;	
		this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
	}
	
	public Parking extraeParking() {
		//TODO
		Parking newParking = new Parking();
		newParking.setRutaArchivoTXT(rutaArchivo);
		
		
		return newParking; 
	}
	

	public String getNombreArchivoTXT() {
		return nombreArchivo;
	}

	public void setNombreArchivoTXT(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;
	}
	
	
}
