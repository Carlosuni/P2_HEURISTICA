/**
 * @author Carlos Dumont & Sergio Casado
 */
package parte2_busq_heur;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import modelo.Parking;
import utilidades.ReadFile;

public class AstarParking {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Queue open = new LinkedList();
		Queue close = new LinkedList();
		System.out.println("\n--= PROGRAMA DE BÚSQUEDA HEURÍSTICA CON A* =--\n");
		ReadFile start = new ReadFile("data.init");
		ReadFile end = new ReadFile("data.goal");
		Parking inicio = null;
		Parking fin = null;
		try {
			inicio = start.extraeParking();
			fin = end.extraeParking();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
