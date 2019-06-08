package fileManagers;

import models.Coche;
import models.Parking;
import models.PlazaCoche;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileExtractor {
	private String nombreArchivo;
	private String rutaArchivo;

	public FileExtractor() {

	}

	/*Extrae el archivo .input introducido por línea de comandos*/
	public FileExtractor(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
		String workingDir = System.getProperty("user.dir");
		String dir = workingDir + "/src/semillasInput/" + nombreArchivo;
		// this.rutaArchivo = "./semillasInput/" + nombreArchivoTXT;
		this.rutaArchivo = dir;
	}

	/*Pasa el parking del archivo extraido a un objeto Parking basado en modelos
	 * de PlazaCoche y Coche progresivamente*/
	public Parking extraeParking() throws IOException {
		Parking newParking = new Parking();
		newParking.setRutaArchivo(this.rutaArchivo);
		Path inputFile = Paths.get(this.rutaArchivo);
		List<String> fileLines = Files.readAllLines(inputFile);

		String[] l1 = fileLines.get(0).split(" ");
		int columnas = Integer.parseInt(l1[1]);
		
		//TODO: COMPROBAR AQUI ERRORES DE SINTAXIS DEL ARCHIVO INPUT(CONTROL 
		//DE ERRORES, NUMERO DE COLUMNAS IGUAL EN TODAS LAS FILAS ETC.
		
		
		int ids = 1;
		
		//Leemos las dimensiones del coche
		for (int i = 0; i <= fileLines.size() - 1; i++) {
			System.out.println(i + " --> " + fileLines.get(i));
		}
		newParking.setNumCalles(Integer.parseInt(l1[0]));
		newParking.setNumHuecosCalle(Integer.parseInt(l1[1]));
		//Creamos las plazas de coche vacías
		newParking.setPlazasCoche(new PlazaCoche[Integer.parseInt(l1[1])][Integer.parseInt(l1[0])]);
		
		System.out.println("COLUMNAS = " + Integer.parseInt(l1[1]));
		System.out.println("FILAS = " + Integer.parseInt(l1[0]));

		//Recorremos todas las lineas y palabras del archivo		
		for (int c = 0; c < newParking.getNumHuecosCalle(); c++) {			
			int linea = 1;
			while (linea <= newParking.getNumCalles()) {
				System.out.println("Extrayendo coche: X=" + c +", Y= " + (linea - 1));	
				System.out.println(newParking.getNumHuecosCalle());					
				System.out.println(newParking.getNumCalles());
				System.out.println("LINEA = " + linea);
				List<String> auxFileLines = fileLines;
				String[] palabras = auxFileLines.get(linea).split(" ");
				System.out.println(Arrays.toString(palabras));
				
				//Evalúa si no está vacía (= está ocupada por un coche)
				//Si fuera otra cosa, ni __ ni coche, saltaría el error de formato
				if (!palabras[c].equals("__")) {
					System.out.println("Dentro de if");
					
					System.out.println(palabras[c]);
					//Obtenemos categoría y orden de llegada de los coches
					char categoria = palabras[c].charAt(0);
					int ordenLlegada = Character.getNumericValue(palabras[c].charAt(1));
					/*Ocupamos la plaza[l - 1][c] cuando encontramos coche
					El "-1" es por descontar la linea inicial
					con las dimensiones del parking*/
					newParking.addPlazaVacia(linea - 1, c);
					newParking.getPlaza(linea - 1, c).setOcupada(true);
					System.out.println(newParking.getPlaza(linea - 1, c).isOcupada());
					//Guardamos el coche en la plaza[l-1][c]
					newParking.addCoche(linea - 1, c, new Coche(ids, linea - 1, c, categoria, ordenLlegada));
					ids++;
					
					System.out.println(newParking.getPlaza(linea - 1, c));
					Coche cochePrint = newParking.getPlaza(linea - 1, c).getCoche();
					System.out.println(cochePrint);						
				} else {
					//Añade una plaza vacía si es == "__"
					newParking.addPlazaVacia(linea - 1, c);
				}
				
				linea++;				
			}
		}
		
		System.out.println("Acaba bucle");
		return newParking;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

}