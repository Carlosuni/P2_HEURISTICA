/**
 * @author Carlos Dumont & Sergio Casado
 * 
 * HEURISTICA - PRACTICA 2 - PARTE 1 - PROGRAMA DE SAT
 */
package main_parte1_sat;

import fileManagers.FileExtractor;

import java.io.IOException;

import org.jacop.core.BooleanVar;
import org.jacop.core.Store;
import org.jacop.jasat.utils.structures.IntVec;
import org.jacop.satwrapper.SatWrapper;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;
import models.Parking;



public class SATParking {

	public static void main(String[] args) {
		
		System.out.println("\n--= PROGRAMA DE SAT =--\n");
		FileExtractor fileExtractor = new FileExtractor(args[0]);
		System.out.println("INFO | Comprobando satisfacibilidad del parking introducido en: " + fileExtractor.getNombreArchivo());
		Parking parking = null;
			
		try {
			parking = fileExtractor.extraeParking();
		} catch (IOException e) {
			System.out.println("¡¡ERROR DE FORMATO .INPUT, POR FAVOR, REVÍSELO!!");
			e.printStackTrace();
		}
		
		boolean satisfacible = parking.checkParkingSAT();
		
		if(satisfacible)
			System.out.println("¡BIEN! EL PARKING ES SATISFACIBLE");	
		else
			System.out.println("¡MAL! EL PARKING NO ES SATISFACIBLE");
	}
}

