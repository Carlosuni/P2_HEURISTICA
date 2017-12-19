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
		
		return plazasCoche[columna][calle];
	}
	
	/*A�ade plza y coche que la ocupa*/
	public void addCoche(int fila, int columna, Coche coche) {
		plazasCoche[columna][fila] = new PlazaCoche(true, columna, fila, coche);
	}

	/*A�ade una plaza vac�a sin coche(null)*/
	public void addPlazaVacia(int fila, int columna) {
		plazasCoche[columna][fila] = new PlazaCoche(false, columna, fila, null);		
	}
	
	
	/*Comprueba si el Parking es satisfacible y devuelve true en ese caso*/
	public boolean checkParkingSAT() {
		//Comprobamos la satisfacibilidad de cada coche, de si est� bloqueado por alg�n motivo o no
		for(int calle = 0; (calle <= this.plazasCoche.length - 1) && !this.existenBloqueados ; calle++) {
			for(int plaza = 0; (plaza <= this.plazasCoche[calle].length - 1) && !this.existenBloqueados; plaza++) {
				//Comprobamos s�lo las plazas que est�n ocupadas por coches
				if(this.plazasCoche[calle][plaza].isOcupada()) {
					//Comprobamos la sat del coche de esa plaza en concreto
					boolean satisfacible = checkCarSAT(this.plazasCoche[calle][plaza].getCoche());
					
					/*Actualizamos la existencia de bloqueos en el parking para detener el bucle
					 ya que al no ser satisfacible alguno de los coches estando bloqueado,
					 no lo ser� a su vez el parking introducido*/
					if(!satisfacible)
						this.existenBloqueados = true;
				}			
			}				
		}
		//Devolvemos si no existen coches bloqueados (satisfacible) o no despu�s de comprobar la SAT
		return !this.existenBloqueados;	
	}
	
	//Comprueba la SAT de un coche vs todos los dem�s coche, es decir, uno a uno si le bloquea
	public boolean checkCarSAT(Coche cocheEvaluado) {
		
		boolean bloqMayorIzq = false;
		boolean bloqOrdenIzq = false;
		boolean bloqMayorDer = false;;
		boolean bloqOrdenDer = false;
		
			//Comprobamos si tiene bloque de alg�n tipo por la izquierda
			bloqMayorIzq = checkMayorIzq(cocheEvaluado);
			bloqOrdenIzq = checkOrdenIzq(cocheEvaluado);
			//Actualizo los valores izq en los objetos Coche
			if(bloqMayorIzq || bloqOrdenIzq)
				cocheEvaluado.setBloqueadoIzda(true);
			
			//Comprobamos si tiene bloque de alg�n tipo por la derecha
			bloqMayorDer = checkMayorDer(cocheEvaluado);
			bloqOrdenDer = checkOrdenDer(cocheEvaluado);
			//Actualizo los valores der en los objetos Coche
			if(bloqMayorDer || bloqOrdenDer)
				cocheEvaluado.setBloqueadoDcha(true);
			
			if(cocheEvaluado.isBloqueadoIzda() || cocheEvaluado.isBloqueadoDcha())
				cocheEvaluado.setBloqueado(true);
		
		
		//Almacenar� y devolver� el resultado del SAT que me d� de ese coche
		boolean resultadoSAT = false;
		//Es true si S� es satisfacible
		resultadoSAT = ejecutaSAT(bloqMayorIzq, bloqOrdenIzq, bloqMayorDer, bloqOrdenDer, cocheEvaluado);
		
		//Devuelvo true si es satisfacible o false si est� bloqueado
		return resultadoSAT;
		
	}
	
	/*Comprueba si hay alg�n coche de mayor categor�a a la izquierda del coche evaluado*/
	public boolean checkMayorIzq(Coche cocheEvaluado) {
		boolean encontrado = false;
		//Si el coche no est� a la izquierda del todo
		if(cocheEvaluado.getPosX() > 0) {
			//Comprueba en todos los coches que est�n a la izquierda en la misma calle
			for(int posIzq = cocheEvaluado.getPosX() - 1; posIzq >= 0; posIzq--) {
				//Comprueba s�lo con las plazas ocupadas por coches
				if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el "char" categor�a del coche con el que compara es mayor que el del coche que se est� evaluando
					if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].getCoche().getCategoria() > cocheEvaluado.getCategoria()) {
						encontrado = true;
					}
				}
				
			}
		}
		return encontrado;		 
	}
	
	/*Comprueba si hay alg�n coche de misma categor�a y posterior orden de llegada a la izquierda del coche evaluado*/
	public boolean checkOrdenIzq(Coche cocheEvaluado) {
		boolean encontrado = false;
		
		//Si el coche no est� a la izquierda del todo
		if(cocheEvaluado.getPosX() > 0) {
			//Comprueba en todos los coches que est�n a la izquierda en la misma calle
			for(int posIzq = cocheEvaluado.getPosX() - 1; posIzq >= 0; posIzq--) {
				//System.out.println(this.plazasCoche[posIzq][cocheEvaluado.getPosY()]);
				//Comprueba s�lo con las plazas ocupadas por coches
				if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el int orden de llegada del coche con el que compara es mayor que el del coche que se est� evaluando
					if(this.plazasCoche[posIzq][cocheEvaluado.getPosY()].getCoche().getOrdenLlegada() > cocheEvaluado.getOrdenLlegada() && this.plazasCoche[posIzq][cocheEvaluado.getPosY()].getCoche().getCategoria() == cocheEvaluado.getCategoria()) {
						encontrado = true;
					}
				}			
			}
		}
		return encontrado;		
	}
	
	/*Comprueba si hay alg�n coche de mayor categor�a a la derecha del coche evaluado*/
	public boolean checkMayorDer(Coche cocheEvaluado) {
		boolean encontrado = false;
		//Si el coche no est� a la derecha del todo
		if(cocheEvaluado.getPosX() < this.numHuecosCalle - 1) {
			//Comprueba en todos los coches que est�n a la derecha en la misma calle
			for(int posDer = cocheEvaluado.getPosX() + 1; posDer <= this.numHuecosCalle - 1; posDer++) {
				//System.out.println(cocheEvaluado.getPosY());
				//System.out.println(this.plazasCoche[posDer][cocheEvaluado.getPosY()]);
				//Comprueba s�lo con las plazas ocupadas por coches
				if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el "char" categor�a del coche con el que compara es mayor que el del coche que se est� evaluando
					if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].getCoche().getCategoria() > cocheEvaluado.getCategoria()) {
						
						encontrado = true;
					}
				}			
			}
		}
		return encontrado;
	}
	
	/*Comprueba si hay alg�n coche de misma categor�a y posterior orden de llegada a la derecha del coche evaluado*/
	public boolean checkOrdenDer(Coche cocheEvaluado) {
		boolean encontrado = false;
		//Si el coche no est� a la derecha del todo
		if(cocheEvaluado.getPosX() < this.numHuecosCalle - 1) {
			//Comprueba en todos los coches que est�n a la derecha en la misma calle
			for(int posDer = cocheEvaluado.getPosX() + 1;  posDer <= this.numHuecosCalle - 1; posDer++) {
				//Comprueba s�lo con las plazas ocupadas por coches
				if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].isOcupada()) {
					//Compara si el int orden de llegada del coche con el que compara es mayor que el del coche que se est� evaluando
					if(this.plazasCoche[posDer][cocheEvaluado.getPosY()].getCoche().getOrdenLlegada() > cocheEvaluado.getOrdenLlegada() && this.plazasCoche[posDer][cocheEvaluado.getPosY()].getCoche().getCategoria() == cocheEvaluado.getCategoria()) {
						encontrado = true;
					}
				}			
			}
		}
		return encontrado;		
	}
	
	
	/*M�TODO PRINCIPAL DE EJECUCI�N DEL SAT (EL QUE JACOP)*/
	public boolean ejecutaSAT(boolean bloqMayorIzq, boolean bloqOrdenIzq, boolean bloqMayorDer, boolean bloqOrdenDer, Coche cocheEvaluado) {
		
		long tiempoInicial = System.nanoTime();
		
		System.out.println("ESTADO ACTUAL DE BLOQUEOS LATERALES (ANTES DEL SAT)");
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� bloqueado por otro otro a la IZQUIERDA de MAYOR CATEGOR�A = " + bloqMayorIzq);
		
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� bloqueado por otro otro a la DERECHA de MAYOR CATEGOR�A = " + bloqMayorDer);
		
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� bloqueado por otro a la IZQUIERDA de MISMA CATEGOR�A pero MAYOR ORDEN = " + bloqOrdenIzq);
		
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� bloqueado por otro otro a la DERECHA de MISMA CATEGOR�A pero MAYOR ORDEN = " + bloqOrdenDer);
		
		System.out.println("\nComprobando SAT con Jacop del coche " + cocheEvaluado.getCategoria() + 
				cocheEvaluado.getOrdenLlegada() +" (X = " + cocheEvaluado.getPosX() + ", Y = " + 
				cocheEvaluado.getPosY() + ")");
		
		int numLibreCategIzq = 1;
		int numLibreCategDer = 1;
		int numLibreOrdenIzq = 1;
		int numLibreOrdenDer = 1;	
		
		//Valen uno si no est� bloqueado seg�n lo comprobado en los m�todos anteriores
		if(bloqMayorIzq)
			numLibreCategIzq = -1;		
		if(bloqMayorDer)
			numLibreCategDer = -1;		
		if(bloqOrdenIzq)
			numLibreOrdenIzq = -1;		
		if(bloqOrdenDer)
			numLibreOrdenDer = -1;
		
		//TODO: BORRAR SYSO INNECESARIO
		System.out.println("ESTADO ACTUAL DE LOS NUMEROS DEL SAT");
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la IZQUIERDA en cuanto a coches de MAYOR CATEGOR�A = " + numLibreCategIzq);
		
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la DERECHA en cuanto a coches de MAYOR CATEGOR�A = " + numLibreCategDer);
		
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la IZQUIERDA en cuanto a coches de MISMA CATEGOR�A Y MAYOR ORDEN DE LLEGADA = " + numLibreOrdenIzq);
		
		System.out.println("\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la DERECHA en cuanto a coches de MISMA CATEGOR�A Y MAYOR ORDEN DE LLEGADA = " + numLibreOrdenDer);
				
		System.out.println("Resultados de las variables: 1 = TRUE, 0 = FALSE\n");
		
		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper(); 
		store.impose(satWrapper);
		
		/*Variables binarias del SAT*/
		BooleanVar libreCategIzq = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la IZQUIERDA en cuanto a coches de MAYOR CATEGOR�A");
		BooleanVar libreCategDcha = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la DERECHA en cuanto a coches de MAYOR CATEGOR�A");
		BooleanVar libreOrdenIzq = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la IZQUIERDA en cuanto a coches de MISMA CATEGOR�A Y MAYOR ORDEN DE LLEGADA");
		BooleanVar libreOrdenDcha = new BooleanVar(store, "\nEl coche " + cocheEvaluado.getCategoria() +
				cocheEvaluado.getOrdenLlegada() + " est� libre por la DERECHA en cuanto a coches de MISMA CATEGOR�A Y MAYOR ORDEN DE LLEGADA");
		
		/* Todas las variables: es necesario para el SimpleSelect */
		BooleanVar[] allVariables = new BooleanVar[]{libreCategIzq, libreCategDcha, libreOrdenIzq, libreOrdenDcha};
		
		/* Registramos las variables en el sat wrapper */
		satWrapper.register(libreCategIzq);
		satWrapper.register(libreCategDcha);
		satWrapper.register(libreOrdenIzq);
		satWrapper.register(libreOrdenDcha);
					 
		/* Obtenemos los literales no negados de las variables */
		int libreCategIzqLit = satWrapper.cpVarToBoolVar(libreCategIzq, 1, true);
		int libreCategDerLit = satWrapper.cpVarToBoolVar(libreCategDcha, 1, true);
		int libreOrdenIzqLit = satWrapper.cpVarToBoolVar(libreOrdenIzq, 1, true);
		int libreOrdenDerLit = satWrapper.cpVarToBoolVar(libreOrdenDcha, 1, true);
		
		/* El problema se va a definir en forma CNF, por lo tanto, tenemos
		   que a�adir una a una todas las clausulas del problema. Cada 
		   clausula ser� una disjunci�n de literales. Por ello, s�lo
		   utilizamos los literales anteriormente obtenidos. Si fuese
		   necesario utilizar un literal negado, �ste se indica con un
		   signo negativo delante. Ejemplo: -xLiteral */
		
		/* Bloqueos por la izquierda y derecha */
		/* Cl�usulas en CNF que controlan satisfacibilidad dependiendo
		   de los coches que bloquean lateralmente */
		addClause(satWrapper, libreCategIzqLit, libreCategDerLit);	/* C1: (p v q) */
		addClause(satWrapper, libreCategIzqLit, libreOrdenDerLit);	/* C2: (p v s) */
		addClause(satWrapper, libreOrdenIzqLit, libreCategDerLit);	/* C3: (r v q) */
		addClause(satWrapper, libreOrdenIzqLit, libreOrdenDerLit);	/* C4: (r v s) */
		
		addClause(satWrapper, (libreCategIzqLit * numLibreCategIzq));	/* C4aux: (p=1 si libre, -1 si bloqueado) */
		addClause(satWrapper, (libreCategDerLit * numLibreCategDer));	/* C5aux: (q=1 si libre, -1 si bloqueado) */
		addClause(satWrapper, (libreOrdenIzqLit * numLibreOrdenIzq));	/* C6aux: (r=1 si libre, -1 si bloqueado) */
		addClause(satWrapper, (libreOrdenDerLit * numLibreOrdenDer));	/* C7aux: (s=1 si libre, -1 si bloqueado) */
		
		
		
		/* Resolvemos el problema */
	    Search<BooleanVar> search = new DepthFirstSearch<BooleanVar>();
		SelectChoicePoint<BooleanVar> select = new SimpleSelect<BooleanVar>(allVariables,
							 new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
		Boolean resultSAT = search.labeling(store, select);
		
		long tiempoEjecucion = System.nanoTime() - tiempoInicial;

		if (resultSAT) {
			System.out.println("RESULTADO:");

			if(libreCategIzq.dom().value() == 1){
				System.out.println(libreCategIzq.id());
			}

			if(libreCategDcha.dom().value() == 1){
				System.out.println(libreCategDcha.id());
			}

			if(libreOrdenIzq.dom().value() == 1){
				System.out.println(libreOrdenIzq.id());
			}

			if(libreOrdenDcha.dom().value() == 1){
				System.out.println(libreOrdenDcha.id());
			}
		} else{
			System.out.println("*** COCHE NO SATISFACIBLE (BLOQUEADO)***");
		}
		System.out.println("Tiempo de ejecuci�n del SAT para este coche concreto = " + tiempoEjecucion + " nanosegundos");
		System.out.println(resultSAT);
		
		//Tal y como est�n formuladas las cl�usulas y variables, si el SAT da resultado true, el Parking no es satisfacible
		return resultSAT;
	}
	
	/*A�ade las cl�usulas necesarias a partir de los literales*/
	public static void addClause(SatWrapper satWrapper, int literal1, int literal2){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		satWrapper.addModelClause(clause.toArray());
	}
	
	public static void addClause(SatWrapper satWrapper, int literal){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal);
		satWrapper.addModelClause(clause.toArray());
	}

	@Override
	public String toString() {
		return "Parking [rutaArchivo=" + rutaArchivo + ", numCalles=" + numCalles + ", numHuecosCalle=" + numHuecosCalle
				+ ", plazasCoche=" + Arrays.toString(plazasCoche) + ", existenBloqueados=" + existenBloqueados + "]";
	}		
	
	
}
