package utilidades;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import modelo.Coche;
import modelo.Parking;

public class ReadFile {

	private String nombreArchivo;
	private String rutaArchivo;

	public ReadFile() {

	}

	public ReadFile(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;
		String workingDir = System.getProperty("user.dir");
		String dir = workingDir + "/src/datos/" + nombreArchivoTXT;
		// this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
		this.rutaArchivo = dir;
	}

	public Parking extraeParking() throws IOException {
		Path inputFile = Paths.get(this.rutaArchivo);
		List<String> file = Files.readAllLines(inputFile);
		String[] linea = file.get(0).split(" ");
		Parking parking = new Parking(file.size(), linea.length, new Coche[file.size()][linea.length]);
		for (int i = 0; i <= file.size() - 1; i++) {
			for (int j = 0; j <= linea.length - 1; j++) {
				String[] col = file.get(i).split(" ");
				if (col[j].equals(">") || col[j].equals("<")) {
					parking.addCoche(i, j, new Coche(col[j].charAt(0)));
				} else {
					parking.addCoche(i, j, null);
				}
			}
		}
		return parking;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
}
