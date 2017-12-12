package fileManagers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import models.Parking;


public class FileExtractor {
	private String nombreArchivo;
	private String rutaArchivo;
	
	public FileExtractor() {
		
	}
	
	public FileExtractor(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;	
		this.rutaArchivo = "../semillasInput/" + nombreArchivoTXT;
	}
	
	public Parking extraeParking() {
		//TODO
		Parking newParking = new Parking();
		newParking.setRutaArchivoTXT(rutaArchivo);
		//leo todas las lineas del archivo y las almaceno en una variable, para luego crear los coches y el parking
		System.out.println(Paths.get(newParking.getRutaArchivoTXT()).toString());
		try {
			FileReader fr = new FileReader(Paths.get(newParking.getRutaArchivoTXT()).toString());
			BufferedReader bufferedReader = new BufferedReader(fr);
	        List<String> lines = new ArrayList<String>();
	        String line = null;
	        while ((line = bufferedReader.readLine()) != null) {
	            lines.add(line);
	        }
	        bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newParking; 
	}
	

	public String getNombreArchivoTXT() {
		return nombreArchivo;
	}

	public void setNombreArchivoTXT(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;
	}
	
	
}
