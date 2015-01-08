package mx.com.factico.cide.parser;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;

public class GsonParser {
	private static String TAG_CLASS = GsonParser.class.getName();
	
	public static final String TAG_RESULT = "result";
	public static final String TAG_RESULT_OK = "OK";
	
	public static Testimonio getTestimonioFromJSON(String json) throws Exception {
		Gson gson = new Gson();
		Testimonio testimonio = gson.fromJson(json, Testimonio.class);
		
		return testimonio;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Testimonio> getListTestimoniosFromJSON(String json) {
		Gson gson = new Gson();
		
		Type listType = new TypeToken<List<Testimonio>>(){}.getType();
		List<Testimonio> listTestimonios = (List<Testimonio>) gson.fromJson(json, listType);
		
		Dialogues.Log(TAG_CLASS, "List Testimonios: " + listTestimonios.size(), Log.INFO);
		
		return listTestimonios;
	}
	
	public static Propuesta getPropuestaFromJSON(String json) {
		Gson gson = new Gson();
		Propuesta propuesta = gson.fromJson(json, Propuesta.class);
		
		Dialogues.Log(TAG_CLASS, "Propuestas: " + propuesta.getTitulo(), Log.INFO);
		Dialogues.Log(TAG_CLASS, "Autor nombre: " + propuesta.getAutor().getNombre(), Log.INFO);
		
		return propuesta;
	}
	
	public static String getResultFromJSON(String json) {
		JSONObject root;
		String result = null;
		
		try {
			root = new JSONObject(json);
			
			if (root != null) {
				result = root.optString(TAG_RESULT);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String createJSON(Propuesta propuesta) {
		Gson gson = new Gson();
		String json = gson.toJson(propuesta);
		
		Dialogues.Log(TAG_CLASS, "Json Propuestas: " + json, Log.INFO);
		
		return json;
	}
}
