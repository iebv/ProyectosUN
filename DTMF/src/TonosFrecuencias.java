public class TonosFrecuencias {

	float[] frecuenciasBajas = {(float)697,(float)770,(float)852,(float)941};
	float[] frecuenciasAltas = {(float)1209,(float)1336, (float)1477};


	
	String[][] tonos = {{"1","2","3"},{"4","5","6"},{"7","8","9"}, {"*","0","#"}};
	
	String cadenaTonos = "//";
	String marcacion = "";

	
	public String[] detectarTono(float frecuenciaAlta, float frecuenciaBaja, float maxAlta){
		int detecciones = 0;
		int posicionFBaja = 0;
		int posicionFAlta = 0;
		String [] result = {"",""};
		
		for(int i = 0; i < frecuenciasBajas.length; i++){
			if(frecuenciaBaja >= frecuenciasBajas[i] - 0.009 * frecuenciasBajas[i]  && frecuenciaBaja <= frecuenciasBajas[i] + 0.009 * frecuenciasBajas[i]){
				posicionFBaja = i;
				detecciones += 1;
			}
		}
		for(int j = 0; j < frecuenciasAltas.length; j++){
			if(frecuenciaAlta >= frecuenciasAltas[j] - 0.009 * frecuenciasAltas[j]  && frecuenciaAlta <= frecuenciasAltas[j] + 0.009 * frecuenciasAltas[j] && maxAlta > 2.0){
				posicionFAlta = j;
				detecciones += 1;
			}
		}
		if(detecciones == 2){ 
			cadenaTonos += tonos[posicionFBaja][posicionFAlta];
		}else{
			if(cadenaTonos.charAt(cadenaTonos.length()-1) != '/') cadenaTonos += "/";
		}
		
		if(cadenaTonos.charAt(cadenaTonos.length()-2) == '/' && cadenaTonos.charAt(cadenaTonos.length()-1) != '/' ){
			marcacion += String.valueOf(cadenaTonos.charAt(cadenaTonos.length()-1));
		}
		else{
			marcacion = "";
		}
		result[0] = marcacion;
		result[1] = cadenaTonos;
		
		return result;
	
	}
	



}
