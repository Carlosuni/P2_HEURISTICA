package fileManagers;

import models.Coche;
import models.Parking;
import java.io.BufferedWriter;
import java.io.File;
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
		String workingDir = System.getProperty("user.dir");
		String dir = workingDir + "/src/semillasInput/" + nombreArchivoTXT;
		// this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
		this.rutaArchivo = dir;
	}

	public Parking extraeParking() throws IOException {
		// TODO
		Parking newParking = new Parking();
		newParking.setRutaArchivoTXT(this.rutaArchivo);
		Path inputFile = Paths.get(this.rutaArchivo);
		List<String> file = Files.readAllLines(inputFile);

		String[] l1 = file.get(0).split(" ");
		String[] l2 = file.get(1).split(" ");
		int columnas = l2.length;
		int ids = 0;
		for (int i = 0; i <= file.size() - 1; i++) {
			System.out.println(i + " --> " + file.get(i));
		}
		newParking.setNumCalles(Integer.parseInt(l1[0]));
		newParking.setNumHuecosCalle(Integer.parseInt(l1[1]));
		newParking.setCoches(new Coche[Integer.parseInt(l1[0])][columnas]);

		for (int l = 1; l <= Integer.parseInt(l1[0]) - 1; l++) {
			for (int c = 0; c <= file.get(l).split(" ").length - 1; c++) {
				String[] fila = file.get(l).split(" ");
				if (fila[c].equals("__") == false) {
					char cat = fila[c].charAt(0);
					char tiempo = fila[c].charAt(1);				
					newParking.addCoche(l, c, new Coche(ids, l, c, cat, (int) tiempo));
				} else {
					newParking.addCoche(l, c, new Coche(ids, l, c));
				}
				
			}
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
