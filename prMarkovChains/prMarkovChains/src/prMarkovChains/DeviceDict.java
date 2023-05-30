package prMarkovChains;

import java.util.HashMap;
import java.util.List;

public class DeviceDict {
						//  Device   Medidas
	protected static HashMap<String, List<Integer>> hm = new HashMap<>();
	
	public static void put(String variable, List<Integer> valor) {
		hm.put(variable, valor);
	}
	
	public static List<Integer> get(String variable) {
		List<Integer> valor = hm.get(variable);
		return valor;
	}
	
}
