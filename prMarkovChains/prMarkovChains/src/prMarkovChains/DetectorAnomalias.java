package prMarkovChains;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetectorAnomalias {
	
	// Mismos valores que en la clase Matriz:
	private static int rangoVentana = 4;
	private static int numIntervalo = 10;
	 
	public static List<Integer> leerTeclado(){
        Scanner in = new Scanner(System.in);
        System.out.println("Se deben leer al menos 4 números de teclado");
        System.out.println("\"FIN\" para terminar de escribir en la consola");
        List<Integer> numeros = new ArrayList<>();
        boolean salir = false;
        while(!salir) {
        	String s = in.nextLine();
        	if(s.toUpperCase().equals("FIN")) {
        		if(numeros.size() < 4) {
        			System.out.println("Se deben leer al menos 4 números de teclado");
        		}else {
        			salir = true;
        		}
        	}else {
        		int n = (int) Math.round(Double.parseDouble(s));
        		numeros.add(n);
        	}
        }
        in.close();
        return numeros;
	}
	
	// Lee la matriz de probabilidades
	public static double[][] leerMatrizP() throws java.io.FileNotFoundException{
	    Scanner sc = new Scanner(new File("MatrizP.txt"));
        double[][] mp = new double[numIntervalo][numIntervalo];

        int fil = 0;
        while(sc.hasNextLine()) {
	        String[] numsFila = sc.nextLine().split("\t");
	        int col = 0;
	        for(int i = 0; i < numIntervalo; i++) {
	        	mp[fil][col] = Double.parseDouble(numsFila[i]);
	        	col++;
	        }
	        fil++;
        }
        sc.close();	
        return mp;
	}
	
	// Lee el punto de corte
	public static double leerPuntoCorte() throws java.io.FileNotFoundException{
	    Scanner sc = new Scanner(new File("puntoCorte.txt"));
	    double pc = Double.parseDouble(sc.nextLine());
        sc.close();
        return pc;
	}
	
	public static void detectarAnomalia(int menor, int mayor) throws FileNotFoundException {
		List<Integer> nums = leerTeclado();
		detector(menor, mayor, nums);
	}
	
	public static void detector(int menor, int mayor, List<Integer> nums) throws FileNotFoundException {
		double pc = leerPuntoCorte();
		double[][] matrizProb = leerMatrizP();
		List<Integer> estados = new ArrayList<>();
		boolean anomalia = false;
		System.out.println("Punto de corte: " + pc);
		for(int i = 0; i < nums.size(); i++) {
			estados.add(Matriz.calcEstado(menor, mayor, nums.get(i)));
			if(estados.size() == rangoVentana) {
				double p3t = 1.0;
				for(int j = 1; j < estados.size(); j++) {
					p3t *= matrizProb[estados.get(j-1)][estados.get(j)];
				}
				if(p3t <= pc) {
					anomalia = true;
					System.out.println("Anomalía en la ventana de mediciones: " + nums.get(i-3)
					+ " - " + nums.get(i-2) + " - " + nums.get(i-1) + " - " + nums.get(i) +
					"\n\tProbabilidad: " + p3t);
				}
				estados.remove(0);
			}
		}
		if(!anomalia) {
			System.out.println("No se han detectado anomalias");
		}
	}
	
}


