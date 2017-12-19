package models;

import java.util.Arrays;

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

public class Parking {
	private String rutaArchivo;
	private int numCalles;
	private int numHuecosCalle;
	private PlazaCoche[][] plazasCoche;
	private boolean existenBloqueados;

	public Parking() {
		
	}

	public Parking(String rutaArchivo, int numCalles, int numHuecosCalle, PlazaCoche[][] plazasCoche, boolean existenBloqueados) {
		this.rutaArchivo = rutaArchivo;
		this.numCalles = numCalles;
		this.numHuecosCalle = numHuecosCalle;
		this.plazasCoche = plazasCoche;
		this.existenBloqueados = existenBloqueados;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public int getNumCalles() {
		return numCalles;
	}

	public void setNumCalles(int numCalles) {
		this.numCalles = numCalles;
	}

	public int getNumHuecosCalle() {
		return numHuecosCalle;
	}

	public void setNumHuecosCalle(int numHuecosCalle) {
		this.numHuecosCalle = numHuecosCalle;
	}

	public PlazaCoche[][] getPlazasCoche() {
		return plazasCoche;
	}

	public void setPlazasCoche(PlazaCoche[][] plazasCoche) {
		this.plazasCoche = plazasCoche;
	}

	public boolean isExistenBloqueados() {
		return existenBloqueados;
	}

	public void setExistenBloqueados(boolean existenBloqueados) {
		this.existenBloqueados = existenBloqueados;
	}
	
	/*Devuelve la plaza y sus datos de una posicion concreta del parking*/
	public PlazaCoche getPlaza(int calle, int columna) {
		
		return plazasCoche[calle][columna];
	}
	
	/*Añade plza y coche que la ocupa*/
	public void addCoche(int fila, int columna, Coche coche) {
		plazasCoche[fila][columna] = new PlazaCoche(true, columna, fila, coche);
	}

	/*Añade una plaza vacía sin coche(null)*/
	public void addPlazaVacia(int fila, int columna) {
		plazasCoche[fila][columna] = new PlazaCoche(false, columna, fila, null);		
	}
	
	@Override
	public String toString() {
		return "Parking [rutaArchivo=" + rutaArchivo + ", numCalles=" + numCalles + ", numHuecosCalle=" + numHuecosCalle
				+ ", coches=" + Arrays.toString(plazasCoche) + ", existenBloqueados=" + existenBloqueados + "]";
	}
	
	/*Comprueba si el Parking es satisfacible y devuelve true en ese caso*/
	public boolean checkParkingSAT() {
		//Comprobamos la satisfacibilidad de cada coche, de si está bloqueado por algún motivo o no
		for(int calle = 0; (calle <= this.plazasCoche.length - 1) && !this.existenBloqueados ; calle++) {
			for(int plaza = 0; (plaza <= this.plazasCoche[calle].length - 1) && !this.existenBloqueados; plaza++) {
				//Comprobamos sólo las plazas que están ocupadas por coches
				if(this.plazasCoche[calle][plaza].isOcupada()) {
					//Comprobamos la sat del coche de esa plaza en concreto
					boolean satisfacible = checkCarSAT(this.plazasCoche[calle][plaza].getCoche());
					
					/*Actualizamos la existencia de bloqueos en el parking para detener el bucle
					 ya que al no ser satisfacible alguno de los coches estando bloqueado,
					 no lo será a su vez el parking introducido*/
					if(!satisfacible)
						this.existenBloqueados = true;
				}			
			}				
		}
		//Devolvemos si no existen coches bloqueados (satisfacible) o no después de comprobar la SAT
		return !this.existenBloqueados;	
	}
	
	/*Añade las cláusulas necesarias a partir de los literales*/
	public static void addClause(SatWrapper satWrapper, int literal1, int literal2){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		satWrapper.addModelClause(clause.toArray());
	}
	
	//Comprueba la SAT de un coche vs todos los demás coche, es decir, uno a uno si le bloquea
	public boolean checkCarSAT(Coche cocheEvaluado) {
		//Comprobamos si tiene bloque de algún tipo por la izquierda
		boolean bloqMayorIzq = checkMayorIzq(cocheEvaluado);
		boolean bloqOrdenIzq = checkOrdenIzq(cocheEvaluado);
		//Actualizo los valores izq en los objetos Coche
		if(bloqMayorIzq || bloqOrdenIzq)
			cocheEvaluado.setBloqueadoIzda(true);
		
		//Comprobamos si tiene bloque de algún tipo por la derecha
		boolean bloqMayorDer = checkMayorDer(cocheEvaluado);
		boolean bloqOrdenDer = checkOrdenDer(cocheEvaluado);
		//Actualizo los valores der en los objetos Coche
		if(bloqMayorDer || bloqOrdenDer)
			cocheEvaluado.setBloqueadoDcha(true);
		
		if(cocheEvaluado.isBloqueadoIzda() || cocheEvaluado.isBloqueadoDcha())
			cocheEvaluado.setBloqueado(true);
		
		//Almacenaré y devolveré el resultado del SAT que me dé de ese coche
		boolean resultadoSAT = false;
		//Es true si SÍ es satisfacible
		resultadoSAT = ejecutaSAT(bloqMayorIzq, bloqOrdenIzq, bloqMayorDer, bloqOrdenDer, cocheEvaluado);
		
		//Devuelvo true si es satisfacible o false si está bloqueado
		return resultadoSAT;
		
	}
	
	/*Comprueba si hay algún coche de mayor categoría a la izquierda del coche evaluado*/
	public boolean checkMayorIzq(Coche cocheEvaluado) {
		boolean encontrado = false;
		//Si el coche no está a la izquierda del todo
		if(cocheEvaluado.getPosX() > 0) {
			//Comprueba en todos los coches que están a la izquierda en la misma calle
			for(int posIzq = cocheEvaluado.getPosX() - 1; posIzq >= 0; posIzq--) {
				//Comprueba sólo con las plazas ocupadas por coches
				if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el "char" categoría del coche con el que compara es mayor que el del coche que se está evaluando
					if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].getCoche().getCategoria() > cocheEvaluado.getCategoria()) {
						encontrado = true;
					}
				}
				
			}
		}
		return encontrado;		 
	}
	
	/*Comprueba si hay algún coche de misma categoría y posterior orden de llegada a la izquierda del coche evaluado*/
	public boolean checkOrdenIzq(Coche cocheEvaluado) {
		boolean encontrado = false;
		
		//Si el coche no está a la izquierda del todo
		if(cocheEvaluado.getPosX() > 0) {
			//Comprueba en todos los coches que están a la izquierda en la misma calle
			for(int posIzq = cocheEvaluado.getPosX() - 1; posIzq >= 0; posIzq--) {
				//Comprueba sólo con las plazas ocupadas por coches
				if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el int orden de llegada del coche con el que compara es mayor que el del coche que se está evaluando
					if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].getCoche().getOrdenLlegada() > cocheEvaluado.getOrdenLlegada()) {
						encontrado = true;
					}
				}			
			}
		}
		return encontrado;		
	}
	
	/*Comprueba si hay algún coche de mayor categoría a la derecha del coche evaluado*/
	public boolean checkMayorDer(Coche cocheEvaluado) {
		boolean encontrado = false;
		//Si el coche no está a la derecha del todo
		if(cocheEvaluado.getPosX() < this.numHuecosCalle - 1) {
			//Comprueba en todos los coches que están a la derecha en la misma calle
			for(int posDer = cocheEvaluado.getPosX() + 1; posDer < this.numHuecosCalle - 1; posDer++) {
				//Comprueba sólo con las plazas ocupadas por coches
				if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el "char" categoría del coche con el que compara es mayor que el del coche que se está evaluando
					if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].getCoche().getCategoria() > cocheEvaluado.getCategoria()) {
						encontrado = true;
					}
				}			
			}
		}
		return encontrado;
	}
	
	/*Comprueba si hay algún coche de misma categoría y posterior orden de llegada a la derecha del coche evaluado*/
	public boolean checkOrdenDer(Coche cocheEvaluado) {
		boolean encontrado = false;
		//Si el coche no está a la derecha del todo
		if(cocheEvaluado.getPosX() < this.numHuecosCalle - 1) {
			//Comprueba en todos los coches que están a la derecha en la misma calle
			for(int posDer = cocheEvaluado.getPosX() + 1;  posDer < this.numHuecosCalle - 1; posDer++) {
				//Comprueba sólo con las plazas ocupadas por coches
				if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el int orden de llegada del coche con el que compara es mayor que el del coche que se está evaluando
					if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].getCoche().getOrdenLlegada() > cocheEvaluado.getOrdenLlegada()) {
						encontrado = true;
					}
				}			
			}
		}
		return encontrado;		
	}
	
	public boolean ejecutaSAT(boolean bloqMayorIzq, boolean bloqOrdenIzq, boolean bloqMayorDer, boolean bloqOrdenDer, Coche cocheEvaluado) {
			
		System.out.println("Comprobando SAT con Jacop del coche " + cocheEvaluado.getCategoria() + 
				cocheEvaluado.getOrdenLlegada() +" (X = " + cocheEvaluado.getPosX() + ", Y = " + 
				cocheEvaluado.getPosY() + ")");
		System.out.println("Resultados de las variables: 1 = SÍ, 0 = NO\n");
		
		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper(); 
		store.impose(satWrapper);
		
		/*Variables binarias del SAT*/
		BooleanVar mayorCategIzq = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " está bloqueado por otro otro a la IZQUIERDA de MAYOR CATEGORÍA");
		BooleanVar mayorCategDcha = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " está bloqueado por otro otro a la DERECHA de MAYOR CATEGORÍA");
		BooleanVar mayorOrdenIzq = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " está bloqueado por otro a la IZQUIERDA de MISMA CATEGORÍA pero MAYOR ORDEN");
		BooleanVar mayorOrdenDcha = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " está bloqueado por otro otro a la DERECHA de MISMA CATEGORÍA pero MAYOR ORDEN");
		
		/* Todas las variables: es necesario para el SimpleSelect */
		BooleanVar[] allVariables = new BooleanVar[]{mayorCategIzq, mayorCategDcha, mayorOrdenIzq, mayorOrdenDcha};
		
		/* Registramos las variables en el sat wrapper */
		satWrapper.register(mayorCategIzq);
		satWrapper.register(mayorCategDcha);
		satWrapper.register(mayorOrdenIzq);
		satWrapper.register(mayorOrdenDcha);
		
		int numCategIzq = 0;
		int numCategDer = 0;
		int numOrdenIzq = 0;
		int numOrdenDer = 0;
		
		
		/*Valen uno si es verdad que está bloqueado según lo comprobado en los métodos anteriores
		Es decir, prepara los datos para que el SAT pueda leer la situación real (valores 1=no negada, o 0=negada)*/
		if(bloqMayorIzq)
			numCategIzq = 1;
		
		if(bloqMayorDer)
			numCategDer = 1;
		
		if(bloqOrdenIzq)
			numOrdenIzq = 1;
		
		if(bloqOrdenDer)
			numOrdenDer = 1;
			 
		/* Obtenemos los literales no negados de las variables */
		int mayorCategIzqLit = satWrapper.cpVarToBoolVar(mayorCategIzq, numCategIzq, true);
		int mayorCategDerLit = satWrapper.cpVarToBoolVar(mayorCategDcha, numCategDer, true);
		int mayorOrdenIzqLit = satWrapper.cpVarToBoolVar(mayorOrdenIzq, numOrdenIzq, true);
		int mayorOrdenDerLit = satWrapper.cpVarToBoolVar(mayorOrdenDcha, numOrdenDer, true);
		
		/* El problema se va a definir en forma CNF, por lo tanto, tenemos
		   que añadir una a una todas las clausulas del problema. Cada 
		   clausula será una disjunción de literales. Por ello, sólo
		   utilizamos los literales anteriormente obtenidos. Si fuese
		   necesario utilizar un literal negado, Éste se indica con un
		   signo negativo delante. Ejemplo: -xLiteral */
		
		/* Bloqueos por la izquierda y derecha */
		/* Cláusulas en CNF que controlan satisfacibilidad dependiendo
		   de los coches que bloquean lateralmente */
		addClause(satWrapper, -mayorCategDerLit, -mayorCategIzqLit);	/* (¬r v ¬q) */
		addClause(satWrapper, -mayorOrdenDerLit, -mayorCategIzqLit);	/* (¬t v ¬q) */
		addClause(satWrapper, -mayorCategDerLit, -mayorOrdenIzqLit);	/* (¬r v ¬s) */
		addClause(satWrapper, -mayorOrdenDerLit, -mayorOrdenIzqLit);	/* (¬t v ¬s) */
		
		
		/* Resolvemos el problema */
	    Search<BooleanVar> search = new DepthFirstSearch<BooleanVar>();
		SelectChoicePoint<BooleanVar> select = new SimpleSelect<BooleanVar>(allVariables,
							 new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
		Boolean resultSAT = search.labeling(store, select);

		if (resultSAT) {
			System.out.println("RESULTADO:");

			if(mayorCategIzq.dom().value() == 1){
				System.out.println(mayorCategIzq.id());
			}

			if(mayorCategDcha.dom().value() == 1){
				System.out.println(mayorCategDcha.id());
			}

			if(mayorOrdenIzq.dom().value() == 1){
				System.out.println(mayorOrdenIzq.id());
			}

			if(mayorOrdenDcha.dom().value() == 1){
				System.out.println(mayorOrdenDcha.id());
			}
		} else{
			System.out.println("*** COCHE SATISFACIBLE - NO BLOQUEADO***");
		}

		System.out.println();
		
		//Tal y como están formuladas las cláusulas y variables, si el SAT da resultado true, el Parking no es satisfacible
		return !resultSAT;
	}			
}
