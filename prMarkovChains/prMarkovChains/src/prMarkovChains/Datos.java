package prMarkovChains;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Datos {
	
	public static void datos() throws IOException {
		Writer wr = new FileWriter("prueba.txt");
		List<Integer> numeros = Matriz.obtenerValores();
		for(int i = 0; i < numeros.size(); i++) {
			int num = numeros.get(i);
			if(num != -1) {	// Para obviar el cambio de máquina
				wr.write(String.valueOf(num) + "\n");
			}
		}
		wr.close();
	}
	
	/* Considerando que hay 10 dispositivos en training.txt, se emplearán los
	 * 6 primeros para el aprendizaje y los 4 siguientes para el testeo */
	public static List<Integer> entrenamiento(List<Integer> valores) throws IOException {
		List<Integer> entrenamiento = new ArrayList<>();
		List<Integer> testeo = new ArrayList<>();
		int cnt = 0;
		for(int i = 0; i < valores.size(); i++) {
			int num = valores.get(i);
			if(num == -1) {
				cnt++;
			}
			if(cnt < 6) {	// Valores a entrenar
				entrenamiento.add(num);
			}else {	// Valores a testear
				if(num != -1) {
					testeo.add(num);
				}
			}
		}
		testeo(testeo);
		return entrenamiento;
	}

	public static void testeo(List<Integer> testeo) throws IOException {
		Writer wr = new FileWriter("test.txt");
		for(int i = 0; i < testeo.size(); i++) {
			wr.write(String.valueOf(testeo.get(i)) + "\n");
		}
		wr.close();
	}

}
