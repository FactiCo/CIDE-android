package mx.com.factico.cide.parser;

import android.util.Log;

import com.google.gson.Gson;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;

public class GsonParser {
	private static String TAG_CLASS = GsonParser.class.getName();
	
	public static final String json = "{"
			+ "\"id\": \"24\","
			+ "\"name\": \"Mario\","
			+ "\"email\": \"mario\","
			+ "\"category\": \"Justicia en el trabajo\","
			+ "\"description\": \"Prueba\","
			+ "\"city\": \"Distrito Federal\","
			+ "\"age\": \"\","
			+ "\"gender\": \"Masculino\","
			+ "\"scholarity\": \"Superior\","
			+ "\"fecha_add\": \"0\","
			+ "\"dispositivo\": \"Android\" }";
	
	public static Testimonio parseJSON(String json) {
		Gson gson = new Gson();
		Testimonio testimonio = gson.fromJson(json, Testimonio.class);
		
		Dialogues.Log(TAG_CLASS, "Name: " + testimonio.getName(), Log.INFO);
		Dialogues.Log(TAG_CLASS, "Age: " + testimonio.getAge(), Log.INFO);
		
		return testimonio;
	}
}
