/**
 * @author Carlos Dumont & Sergio Casado
 * 
 * HEURISTICA - PRACTICA 2 - PARTE 1 - PROGRAMA DE SAT
 */
package main_parte1_sat;

import fileManagers.FileExtractor;
import models.Parking;

public class SATParking {

	public static void main(String[] args) {
		
		System.out.println("\n--= PROGRAMA DE SAT =--\n");
		FileExtractor fileExtractor = new FileExtractor(args[0]);
		System.out.println("INFO | Comprobando satisfacibilidad del parking introducido en: " + fileExtractor.getNombreArchivoTXT());
		
		Parking parking = fileExtractor.extraeParking();
		
	}

}
