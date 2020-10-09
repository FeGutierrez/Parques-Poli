import java.awt.image.AreaAveragingScaleFilter;
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
	int[][] diferenciaConFinal = new int[4][4];
	int[] puntajes; // arreglo para dados
	int paresAcumulados; //pares acumulados
	final int[] carceles = {-4, -21, -38, -55};
	int turnoInicial=-1;
	int contadorTurnos;
	int[] casillasSeguras = {4, 11, 16, 21, 28, 33, 38, 45, 50, 55, 62, 67}; //Solo funciona con 4 jugadores

	public void inicializar(){
		//(1) INICIO DEL JUEGO
		for(int i = 0; i <= 3; ++i){ //Ubicarse en la fila
			for(int j = 0; j <= 3; ++j){ //Ubicarse en la columna
				piezas[i][j] = carceles[i];
			}
		} // Fin de (1)
		//INICIALIZAR MATRIZ AUXILIAR PARA VER DISTANCIAS HASTA LA META
		for(int i = 0; i < 4; ++i){
			for( int j = 0; j< 4; ++j){
				diferenciaConFinal[i][j] = 75 - (carceles[i]);
			}
		}
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
			contadorTurnos = index;

		}
		//int numeroJugadores = 4;
		//int totalCasillas = 17 * numeroJugadores;
		//int numeroCasillasSeguras = numeroJugadores * 3;
		//casillasSeguras = new int[numeroCasillasSeguras];

		//Llegados a este punto, ya se ha decidido un ganador y se empieza a jugar, por esa fila
		iniciarPartida(); // ?????



	}
	
	public int[] hacerJugada(int[][] piezas, int[] dados, int pares_acumulados) { //Asumimos que los elementos
		int turnoActual = contadorTurnos % 4; // turnos cíclicos en el rango {0, 1, 2 ,3}
		puntajes = dados;
		// han sido inicializados de forma correcta previamente
		//el turno inicial debe ser el indice obtenido al momento de inicializar el juego
		int totalEncarceladas = cuantasEnPrision(piezas[turnoActual], turnoActual);

		//
		if(dados[0] == dados[1]){ //Salió par, decidir si mover o salir de prisión

			//Para considerar salir de prisión, debo estar en prisión...
			if (totalEncarceladas == 0) { //No hay nada que decidir
				ArrayList<Integer> validas = piezasValidos(piezas[turnoActual], turnoActual);
				++paresAcumulados; //Se incrementan los pares acumulados cuando no se saca de la cárcel
				mover(validas);//
			} else { //Decidir porque hay piezas en la carcel
				/*
				* Los pares especiales de 6s y 1s siempre serán prioritarios para sacar de la cárcel
				* Bajo que circunstancias es mejor mover una pieza a sacar de la cárcel?:
				* 	- Si hay una pieza en una casilla que no es segura
				* 		Prioridad: La pieza más adelantada
				* 	-Si con el valor de uno de los dos dados o su suma se puede capturar
				* 	una pieza rival
				*
				* En otro caso es recomendable sacar de la cárcel
				*
				* */
				if( dados[0] == 6 || dados[0] == 1) { //Caso de pares especiales
					ArrayList<Integer> validas = piezasValidos(piezas[turnoActual], turnoActual); //Aqui tenemos las piezas que no estan en la carcel
					//pero tampoco han salido del juego, en caso de que esta lista sea vacía, se debe mover con una pieza salida de la carcel
					ArrayList<Integer> salidas = sacarTodasDeLaCarcel( piezas[turnoActual], turnoActual); //Hasta aquí saca a todas las piezas de la cárcel
					// Tenemos una lista que indica cuales piezas salieron para mover con ellas en caso de que no haya
					//mas piezas para hacer un movimiento
					if( salidas.size() == 1){ //Si solo habia una en la cárcel
						//Verificar si hay más piezas para distribuir el puntaje
						if( !validas.isEmpty()){ //Hay al menos una pieza que no estaba en la cárcel
							//y tampoco había salido del juego
							//Se mueve con una de esas piezas validas
							moverPostCarcel( validas);
						} else {
							moverPostCarcel( salidas);
						}
					} else { //Hay más de una pieza en la cárcel, no hay movimientos adicionales

					}
				} else { //Fin de los pares especiales (6s o 1s) pero si tenemos pieza(s) en la cárcel
					ArrayList<Integer> piezasJugables = piezasValidos( piezas[turnoActual], turnoActual);
					//Vemos si hay piezas que puedan mover antes de sacar de la carcel
					ArrayList<Integer> piezasSalidas = sacarMaxDosDeLaCarcel( piezas[turnoActual], turnoActual );
					if( !piezasJugables.isEmpty() ){ //Habia piezas validas para distribuir el puntaje?
						moverPostCarcel(piezasJugables);
					} else { //Esto quiere decir que debemos mover con una de las piezas salidas de la carcel
						moverPostCarcel(piezasSalidas);
					}
				}

			}
			--turnoActual;
		}


		++turnoActual;
		return null;
	}

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

	public int[] elMenor( int[] arreglo){
		int[] resultados = {100,-1}; // resultados[0] = max del arreglo resultados[1] = indice del max
		int min = 100;
		for(int i = 0; i < arreglo.length; ++i){
			if ( arreglo[i] < min) {
				min = arreglo[i];
				resultados[0] = min;
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

	public int cuantasEnPrision( int[] filaDelJugador, int indiceJugador){ //Retorna un contador
		//con el numero de piezas en prisión del jugador
		int contador = 0;

		for(int j = 0; j < filaDelJugador.length; ++j){ //Recorrer columnas de la fila
			if( filaDelJugador[j] == carceles[indiceJugador] ){
				++contador;
			}
		}
		return contador;
	}

	public int cuantasFueraDeJuego( int[] filaDelJugador, int indiceJugador ){ //Cantidad de piezas del jugador que se encuentran en el tablero
		//Sea en una casilla de la casa, del camino, o de la cárcel
		int contador = 0;
		for( int i = 0; i < filaDelJugador.length; ++i){
			if(filaDelJugador[i] == 75){
				++contador;
			}
		}
		return contador;
	}

	public int piezasCorriendo(int[] filaDelJugador, int indiceJugador){
		return 4 - (cuantasEnPrision(filaDelJugador, indiceJugador) - (cuantasFueraDeJuego(filaDelJugador, indiceJugador)));
	}

	public ArrayList<Integer> piezasValidos( int[] filaDelJugador, int indiceJugador ){
		// Cuantas piezas estaban fuera de la cárcel pero sin llegar al final del tablero
		// en un determinado momento (lo solemos usar antes de sacar de la cárcel )
		ArrayList<Integer> validos = new ArrayList<>();
		for( int i = 0; i<filaDelJugador.length; ++i){
			if( (filaDelJugador[i] != carceles[indiceJugador]) && ( filaDelJugador[i] != 75) ){
				validos.add(i);
			}
		}
		return validos;
	}

	public ArrayList<Integer> sacarTodasDeLaCarcel( int[] filaDelJugador, int indiceJugador ){
		// Saca todas las piezas de la cárcel
		// retorna una lista indicando cuales piezas salieron
		ArrayList<Integer> recienSalidos = new ArrayList<>();
		while( cuantasEnPrision(filaDelJugador, indiceJugador) > 0 ){
			int j = 0;
			if( filaDelJugador[j] == carceles[indiceJugador] ){
				filaDelJugador[j] = -carceles[indiceJugador];
				recienSalidos.add(j);
			}
			++j;
		}
		return recienSalidos;
	}

	public ArrayList<Integer> sacarMaxDosDeLaCarcel( int[] filaDelJugador, int indiceJugador ){
		// Saca todas las piezas de la cárcel
		// retorna una lista indicando cuales piezas salieron
		ArrayList<Integer> recienSalidos = new ArrayList<>();
		int j = 0;
		while( recienSalidos.size() < 3 && j < 4){
			if(filaDelJugador[j] == carceles[indiceJugador]){
				filaDelJugador[j] = -carceles[indiceJugador];
				recienSalidos.add(j);
			}
			++j;
		}


		return recienSalidos;
	}

	public boolean pertenece( int[] arreglo, int valor){
		boolean p = false;
		for(int i = 0; i < arreglo.length; ++i){
			if( arreglo[i] == valor){
				p = true;
			}
		}
		return true;
	}

	public ArrayList<Integer> esPosibleAsegurar( int[] filaDelJugador, int indiceJugador ){
		//Recorremos la fila del jugador y si para algun valor P_i del arreglo el valor de uno de los dados
		//o su suma, permite que la pieza con el valor P_i termine en una casilla segura se retorna
		//cuales piezas pueden ser aseguradas (puede ser, 0, 1 o varias)
		int sumaDados = puntajes[0] + puntajes[1];
		//Tenemos un arreglo con casillas seguras, sea P_i el valor de la casilla y d_1 y d_2 los valores
		//de los dados respectivamente, debemos veriticar si el arreglo de casillas seguras contiene a:
		// P_i + d_1
		// P_i + d_2
		// P_i + d_1 + d_2
		ArrayList<Integer> validos = piezasValidos(filaDelJugador, indiceJugador);
		for( int i = 0; i < validos.size(); ++i){ //Para cada pieza valida verificar
			int v1, v2, v3;
			v1 = piezas[indiceJugador][validos.get(i)] + puntajes[0];
			v2 = piezas[indiceJugador][validos.get(i)] + puntajes[1];
			v3 = piezas[indiceJugador][validos.get(i)] + puntajes[0] + puntajes[1];
			if( (pertenece(casillasSeguras, v1)) || pertenece(casillasSeguras, v2) || pertenece(casillasSeguras, v3) ){
				
			}
		}
		return null;
	}

	public ArrayList<Integer> esPosibleCapturar( int[] filaDelJugador, int indiceJugador ){
		return null;
	}

	public void moverPostCarcel( ArrayList<Integer> validas){ //Implementar movimiento, debe recibir una lista de piezas válidas para mover
		//Este metodo se usa cuando se acaba de salir de la carcel y solo se podrá mover con uno de los indices
		++contadorTurnos;
	}
	public void mover( ArrayList<Integer> validas ){ //Implementar movimiento, debe recibir una lista de piezas válidas para mover
		if( paresAcumulados == 3){ // Cuando ocurra este evento, se escoge sacar la pieza más atrasada si no se puede asegurar una pieza con el puntaje


		}
		++contadorTurnos;
	}

}
