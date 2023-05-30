package prMarkovChains;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Matriz {
	
	private static int numIntervalo = 10;
	private static int rangoVentana = 4;
	
	public static int mayor(List<Integer> valores) {
		int mayor = Integer.MIN_VALUE;
		for(int i = 0; i < valores.size(); i++) {
			int n = valores.get(i);
			if(n != -1 && mayor < n) {	// Cambio de maquina
				mayor = n;
			}
		}
		return mayor;
	}
	
	public static int menor(List<Integer> valores) {
		int menor = Integer.MAX_VALUE;
		for(int i = 0; i < valores.size(); i++) {
			int n = valores.get(i);
			if(n != -1 && n < menor) {	// Cambio de maquina
				menor = n;
			}
		}
		return menor;
	}
	
	public static double[][] construirMatriz(int menor, int mayor, List<Integer> valores) {
		// Creamos la estructura de datos
		double[][] matriz = new double[numIntervalo][numIntervalo];
		for(int i = 0; i < valores.size()-1; i++) {
			if(valores.get(i) != -1 && valores.get(i+1) != -1) {	// Cambio de maquina
				int val1 = calcEstado(menor, mayor, valores.get(i));	// Fila = Desde donde viene
				int val2 = calcEstado(menor, mayor, valores.get(i+1));	// Columna = A donde va
				matriz[val1][val2]++;
			}
		}
		return matriz;
	}
	
	public static double[][] construirMatrizProb(int menor, int mayor, List<Integer> valores){
		double[][] matriz = construirMatriz(menor, mayor, valores);
		// Se suman todos los valores de la fila para probar la probabilidad
		double[][] matrizProb = new double[numIntervalo][numIntervalo];
		for(int fil = 0; fil < matriz.length; fil++) {
			double sumFila = 0;
			for(int col = 0; col < matriz.length; col++) {
				if(matriz[fil][col] == 0) {
					// Si algo no ha ocurrido, se le asigna una frecuencia baja:
					matriz[fil][col] = 0.5;
				}
				sumFila += matriz[fil][col];
			}
			for(int col = 0; col < matriz.length; col++) {
				matrizProb[fil][col] = matriz[fil][col]/sumFila;
			}
		}
		return matrizProb;
	}
	
	/* 1. Calcular estado del valor
	   2. Cuando se tengan 4 estados
	   3. Multiplicar las probabilidades de las 3 transiciones anteriores
	   4. Obtener la menor probabilidad */
	public static double puntoCorte(int menor, int mayor, List<Integer> valores) {
		double[][] matrizProb = construirMatrizProb(menor, mayor, valores);
		List<Integer> estados = new ArrayList<>();
		double menorP3T = Double.MAX_VALUE;
		for(int i = 0; i < valores.size(); i++) {
			if(valores.get(i) != -1) {	// Para no considerar el cambio de máquina
				estados.add(calcEstado(menor, mayor, valores.get(i)));
				if(estados.size() == rangoVentana) {
					double p3t = 1.0;
					for(int j = 1; j < estados.size(); j++) {
						p3t *= matrizProb[estados.get(j-1)][estados.get(j)];
					}
					if(p3t < menorP3T) {
						menorP3T = p3t;	// Obtener menor probabilidad
					}
					estados.remove(0);
				}
			}
			
		}
		return menorP3T;
	}
	
	public static List<Integer> obtenerValores(){
		List<Integer> valores = new ArrayList<>();
		for(String device : DeviceDict.hm.keySet()) {
			List<Integer> li = DeviceDict.get(device);
			for(int i = 0; i < li.size(); i++) {
				valores.add(li.get(i));
			}
			valores.add(-1);	// Para saber cuándo se cambia de máquina
		}
		return valores;
	}

	// Discretizamos en valores de 10:
	static int calcEstado(int menor, int mayor, int n) {
		float div = (float)(n-menor)/(float)(mayor-menor);
		int aux = Math.round((numIntervalo-1)*div);
		if(aux < 0) {	// Por si llegan valores menores que el menor
			aux = 0;
		}else if(aux > (numIntervalo-1)) {	// Por si llegan valores mayores que el mayor
			aux = numIntervalo-1;
		}
		return aux;
	}
	
	//APARTADO 2
	public static void matrixN2file(int menor, int mayor, List<Integer> valores) {
		double[][] matriz = construirMatriz(menor, mayor, valores);
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter("matrizN.txt"));
	        for (int i = 0; i < matriz.length; i++) {
	            for (int j = 0; j < matriz.length; j++) {
	                    bw.write(matriz[i][j] + "\t");
	                    // delimitador de columnas = "\t"
	            }
	            bw.newLine();
	        }
	        bw.flush();
	        bw.close();
	    } catch (IOException e) {}
	}
	
	public static void matrixP2file(int menor, int mayor, List<Integer> valores) {
		double[][] matriz = construirMatrizProb(menor, mayor, valores);
	    try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter("matrizP.txt"));
	        for (int i = 0; i < matriz.length; i++) {
	            for (int j = 0; j < matriz.length; j++) {
	                    bw.write(matriz[i][j] + "\t"); // delimitador de columnas = "\t"
	            }
	            bw.newLine();
	        }
	        bw.flush();
	        bw.close();
	    } catch (IOException e) {}
	}
	
	public static void PC2file(int menor, int mayor, List<Integer> valores) {
		try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter("puntoCorte.txt"));
	        double n = puntoCorte(menor, mayor, valores);
	        bw.write(String.valueOf(n));	// Lo guarda como String
	        bw.flush();
	        bw.close();
	    } catch (IOException e) {}
	}

}
