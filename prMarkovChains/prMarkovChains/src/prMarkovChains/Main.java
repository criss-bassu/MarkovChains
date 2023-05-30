package prMarkovChains;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void leerFichero() throws IOException {
		// Abre un fichero en modo lectura  
        FileReader file = new FileReader("training.txt ");  
        BufferedReader br = new BufferedReader(file);  
  
        // Obtiene cada linea hasta el fin del fichero
        String line;
        while((line = br.readLine()) != null) {  
            // Divide las lineas en palabras (delimitador = ",")  
            String words[] = line.split(",");
            boolean existe = (DeviceDict.get(words[0]) != null);
            if(existe) {
            	List<Integer> listDevice = DeviceDict.get(words[0]);
            	listDevice.add(Integer.parseInt(words[2]));
            	DeviceDict.put(words[0], listDevice);
            }else {	// El dispositivo aún no está reconocido
                List<Integer> li = new ArrayList<>();
            	li.add(Integer.parseInt(words[2]));	// measurement
            	DeviceDict.put(words[0], li);
            }
        }  
        br.close();  
    }  
	
	// Para probar el programa con training.txt como entrenamiento insertando datos aleatorios
	public static void main1() {
		// Apartado 1
		try {
			leerFichero();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Integer> valores = Matriz.obtenerValores();
		int menor = Matriz.menor(valores);
		int mayor = Matriz.mayor(valores);
		
		Matriz.puntoCorte(menor, mayor, valores);
		Matriz.matrixN2file(menor, mayor, valores);
		Matriz.matrixP2file(menor, mayor, valores);
		Matriz.PC2file(menor, mayor, valores);

		// Apartado 2
		try {
			Datos.datos();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			DetectorAnomalias.detectarAnomalia(menor, mayor);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main1f(File file) {
		// Apartado 1
		try {
			leerFichero();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Integer> valores = Matriz.obtenerValores();
		int menor = Matriz.menor(valores);
		int mayor = Matriz.mayor(valores);
		
		Matriz.puntoCorte(menor, mayor, valores);
		Matriz.matrixN2file(menor, mayor, valores);
		Matriz.matrixP2file(menor, mayor, valores);
		Matriz.PC2file(menor, mayor, valores);

		// Apartado 2
		try {
			Datos.datos();
			/* Para crear los datos. Son las mismas mediciones que training.txt */
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			List<Integer> contenido = leerMedidasFichero(file);
			DetectorAnomalias.detector(menor, mayor, contenido);
			/* Si funciona correctamente devolverá únicamente la ventana que
			 * originó el punto de corte como anomalía */
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static List<Integer> leerMedidasFichero(File file) {
		List<Integer> contenido = new ArrayList<>();
		try {
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        String line;
	        while((line = br.readLine()) != null) {
	        	contenido.add(Integer.parseInt(line));
	        }
	        br.close();
	        fr.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
		return contenido;
	}

	// Entrena con 6 dispositivo y testea con 4, del training.txt
	public static void main2f(File file) {
		// Apartado 1
		try {
			leerFichero();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Integer> vals = Matriz.obtenerValores();
		List<Integer> valores = new ArrayList<>();
		try {
			valores = Datos.entrenamiento(vals);	// Devuelve los datos a entrenar
			// Se crean los datos a testear
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int menor = Matriz.menor(valores);
		int mayor = Matriz.mayor(valores);
		
		Matriz.puntoCorte(menor, mayor, valores);
		Matriz.matrixN2file(menor, mayor, valores);
		Matriz.matrixP2file(menor, mayor, valores);
		Matriz.PC2file(menor, mayor, valores);

		// Apartado 2
		try {
			List<Integer> contenido = leerMedidasFichero(file);
			DetectorAnomalias.detector(menor, mayor, contenido);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if(args.length == 0) {
			main1();
		}else {
			if(args[0].equals("prueba.txt")) {
				main1f(new File(args[0]));
			}else if(args[0].equals("test.txt")) {
				main2f(new File(args[0]));
			}else {
				main1f(new File(args[0]));
			}
		}
	}

}
