import java.time.chrono.MinguoDate;
import java.util.ArrayList;
import java.util.Random;

public class MiJugador extends Jugador {

	/*
	* Jugadores van del 0 a 3 = 0 para azul 1 para amarillo 2 para rojo y 3 para verde
	* Las posiciones de las piezas están dadas por una matriz 4x4 cada fila es un jugador
	* cada columna de la fila es la posicion de una pieza
	* Desde cada casilla 0 de un jugador hasta la siguiente casa hay 17 casillas
	* por ejemplo 8 jugadores se deberian tener 8 * 17 casillas sin contar las cassa internas de cada
	* uno
	* */

	/* INICIO DEL JUEGO
	* 	(1) Se inicializa la matriz 4x4 con todas las piezas en la carcel de cada jugador, por ejemplo, sea
	* 	M la matriz de posiciones, se vería representada de esta manera
	* 	0: [-4, -4, -4, -4]
	* 	1: [-21, -21, -21, -21]
	* 	2: [-38, -38, -38, -38]
	* 	3: [-55, -55, -55, -55] Fin de (1)
	*
	* 	(2) Se crea un generador de valores para el dado (que retornara valores pseudoaleatorios entre 1 y 6),
	*  y se crea un vector para indicar el valor obtenido por cada jugador
	* 	y se crea una función que valide si todos los jugadores han lanzado al menos una vez
	* 	el arreglo de lanzamiento tendría por nombre lanzamientoJugadores tendría 4 elementos enteros
	* 	y habría una función verificarLanzamientos() que recibe el vector lanzamientoJugadores y
	* 	se asegura de que todas las entradas de ese vector son numeros válidos para un dado (valores
	* 	entre 1 y 6 inclusivos)
	*
	* */

	/*
	* Para hacer una jugada: Se recibe la matriz de posiciones, se reciben los dos dados, se
	* recibe la cantidad de pares acumulados sin contar los utilzados para sacar piezas de la carcel
	* Primera pregunta despues de arrojar los dados:
	* 	P: ¿Qué jugador tiene el turno?
	* 	R: un contador, con la función modulo, sea c el contador entero y n el numero de jugadores
	* 	el jugador con el turno actual está dado por la función ( c % n)
	* 	P: ¿Tiene el jugador actual piezas en la cárcel?
	* 	R: Cada jugador tiene un numero negativo que representa su cárcel, dicho esto
	* 	para saber si el jugador tiene piezas en la carcel, basta con recorrer la fila que
	* 	corresponde a ese jugador y revisar si alguna de los valores es su entero  negativo predefinido
	*
	*
	* */

	int[][] piezas = new int[4][4]; //Matriz de las piezas
	int[] dados; // arreglo para dados
	int paresAcumulados; //pares acumulados
	final int[] carceles = {-4, -21, -38, -55};
	int turnoInicial=-1;
	int[] casillasSeguras = {4, 11, 16, 21, 28, 33, 38, 45, 50, 55, 62, 67}; //Solo funciona con 4 jugadores

	public boolean verificarLanzamientos( int[] lanzamientosJugadores ){
		boolean valido = true;
		for(int i = 0; i < lanzamientosJugadores.length; ++i){
			if(lanzamientosJugadores[i] == 0){
				valido = false;
			}
		}
		return valido;
	}

	public int[] elMayor( int[] arreglo){
		int[] resultados = {0,-1}; // resultados[0] = max del arreglo resultados[1] = indice del max
		int max =0;
		for(int i = 0; i < arreglo.length; ++i){
			if ( arreglo[i] > max) {
				max = arreglo[i];
				resultados[0] = max;
				resultados[1] = i;
			}
		}
		return resultados;
	}

	public ArrayList<Integer> maxRepetido(int[] lanzamientosJugadores, int maximo, int indiceDelMax ){ //equivalente a Hay dos o mas valores iguales
		ArrayList<Integer> indicesEmpatados = new ArrayList<>();
		indicesEmpatados.clear(); //Lista vacia
		boolean anadirIndiceMax = false;
		for(int i = 0; i < lanzamientosJugadores.length; ++i){
			if(i != indiceDelMax){
				if(lanzamientosJugadores[i] == maximo){
					indicesEmpatados.add(i);
					anadirIndiceMax = true;
				}
			}
		}
		if(anadirIndiceMax){
			indicesEmpatados.add(indiceDelMax);
		}
		return indicesEmpatados;
	}

	public void inicializar(){
		//(1) INICIO DEL JUEGO
		for(int i = 0; i <= 3; ++i){ //Ubicarse en la fila
			for(int j = 0; j <= 3; ++j){ //Ubicarse en la columna
				piezas[i][j] = carceles[i];
			}
		} // Fin de (1)
		// (2) -> dados
		Random generador = new Random();
		int[] lanzamientosJugadores = new int[4];
		for(int i=0; i <= 3; ++i){
			lanzamientosJugadores[i] = 0;
		}
		int counter = 0;//Contador
		while( !verificarLanzamientos(lanzamientosJugadores) ){
			lanzamientosJugadores[counter % 4] = (generador.nextInt(6)+1);
		} //Hasta aqui sabemos que todos los jugadores lanzaron el dado al menos una vez
		//Determinar el maximo
		int[] info = elMayor(lanzamientosJugadores);
		int max = info[0];
		int index = info[1]; //Si no se repite indica el jugador que tiene el turno
		//Buscar si se ha repetido

		ArrayList<Integer> indicesEmpatados = maxRepetido(lanzamientosJugadores, max, index);
		while( !indicesEmpatados.isEmpty() ){ //Mientras haya jugadores empatados,
			// repetimos los lanzamientos para esos indices
			//{ i0, i1, i2, i3} indices de esta forma cuando hay empates aqui ya tenemos los indices empatados
			// y se reemplazara con nueva información
			//Se pueden sustituir todas las casillas por un valor menor a 1 ya que las casillas relevantes
			//seran re asignadas
			for(){

			}
			for(int i=0; i<indicesEmpatados.size(); ++i){
				lanzamientosJugadores[indicesEmpatados.get(i)] = (generador.nextInt(6)+1); //Relanzar para cada jugador empatado
			}
			info = elMayor(lanzamientosJugadores);
			max = info[0];
			index = info[1];
			indicesEmpatados = maxRepetido(lanzamientosJugadores, max, index);
			turnoInicial = index;

		}
		//int numeroJugadores = 4;
		//int totalCasillas = 17 * numeroJugadores;
		//int numeroCasillasSeguras = numeroJugadores * 3;
		//casillasSeguras = new int[numeroCasillasSeguras];

		//Llegados a este punto, ya se ha decidido un ganador y se empieza a jugar, por esa fila
		iniciarPartida(); // ?????



	}
	
	public int[] hacerJugada(int[][] piezas, int[] dados, int pares_acumulados) { //Asumimos que los elementos
		// han sido inicializados de forma correcta previamente
		int turno = turnoInicial; //el turno inicial debe ser el indice obtenido al momento de inicializar el juego
		int totalEncarceladas = cuantasEnPrision(piezas[turno], turno);
		if(dados[0] == dados[1]){ //Salió par, decidir si mover o salir de prisión
			//Para considerar salir de prisión, debo estar en prisión...
			if (totalEncarceladas == 0) { //No hay nada que decidir
				//mover();//
			} else { //Decidir
				/*
				* Bajo que circunstancias es mejor mover una pieza a sacar de la cárcel?:
				* 	- Si hay una pieza en una casilla que no es segura
				* 		Prioridad: La pieza más adelantada
				* 	-Si con el valor de uno de los dos dados o su suma se puede capturar
				* 	una pieza rival
				*
				* En otro caso es recomendable sacar de la cárcel
				*
				* */

			}

		}


		if(dados[0] == dados[1]) { //Hay par

			if (dados[0] == 6 || dados[0] == 1) { //se sacan todas las piezas de la carcel
				if (totalEncarceladas == 0) {
					//mover();//
				} else {
					for(int i=0; i<piezas[turno].length; ++i){
						piezas[turno][i] = -carceles[turno]; //Casilla inicial
					}
				}
			}
			//En caso de que el par no sea de 6s o 1s (2, 3, 4, 5)

		}

		return null;
	}

	public int cuantasEnPrision( int[] filaDelJugador, int indiceJugador){
		int contador = 0;

		for(int j = 0; j < filaDelJugador.length; ++j){ //Recorrer columnas de la fila
			if( filaDelJugador[j] == carceles[indiceJugador] ){
				++contador;
			}
		}

		return contador;
	}

}
