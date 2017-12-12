package fileManagers;

import models.Parking;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileExtractor {
	private String nombreArchivo;
	private String rutaArchivo;
	
	public FileExtractor() {
		
	}
	
	public FileExtractor(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;	
		this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
	}
	
	public Parking extraeParking() throws IOException {
		//TODO
		Parking newParking = new Parking();
		newParking.setRutaArchivoTXT(this.rutaArchivo);
		Path inputFile = Paths.get(this.rutaArchivo); 
		List<String> file = Files.readAllLines(inputFile);
		/*int y = file.get(0).charAt(0) - '0';
		int x = file.get(0).charAt(1) - '0';
		String[][] map = new String[y][x];
		for (int i = 0; i < y; i++) {
			int k = 0;
			for (int j = 0; j < x; j++) {
				String tmp = "" + file.get(i+1).charAt(k) + file.get(i+1).charAt(k + 1);
				k += 2;
				map[i][j] = tmp;
			}
		}*/
		for(int i=0;i<=file.size()-1;i++) {
			System.out.println(file.get(i));
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
