package utilidades;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import modelo.Coche;
import modelo.Parking;

public class Writter {

	private String nombreArchivo;
	private String rutaArchivo;

	public Writter() {

	}

	public Writter(String nombreArchivoTXT) {
		this.nombreArchivo = nombreArchivoTXT;
		String workingDir = System.getProperty("user.dir");
		String dir = workingDir + "/src/datos/" + nombreArchivoTXT;
		// this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
		this.rutaArchivo = dir;
	}

	public void escribirPlan(List<String> datos) {
		String workingDir = System.getProperty("user.dir");
		String dir = workingDir + "/src/datos/data.plan";
		// this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
		this.rutaArchivo = dir;
		// escribir el plan
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(this.rutaArchivo, "UTF-8");
			for (int i = 0; i <= datos.size() - 1; i++) {
				writer.println(datos.get(i));
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

	public void escribirInfo(List<String> datos) {
		String workingDir = System.getProperty("user.dir");
		String dir = workingDir + "/src/datos/data.info";
		// this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
		this.rutaArchivo = dir;
		// escribir pasos hechos
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(this.rutaArchivo, "UTF-8");
			for (int i = 0; i <= datos.size() - 1; i++) {
				writer.println(datos.get(i));
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}

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
