package mx.com.factico.cide.parser;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mx.com.factico.cide.beans.Facebook;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;

public class GsonParser {
	private static String TAG_CLASS = GsonParser.class.getName();
	
	public static final String TAG_RESULT = "result";
	public static final String TAG_RESULT_OK = "OK";
	public static final String TAG_RESULT_ERROR = "ERROR";
	
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
		
		return propuesta;
	}
	
	public static String getResultFromJSON(String json) {
		JSONObject root;
		String result = "";
		
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
	
	public static Facebook getFacebookFromJSON(String json) throws Exception {
		Gson gson = new Gson();
		Facebook facebook = gson.fromJson(json, Facebook.class);
		
		return facebook;
	}
	
	public static String createJsonFromObject(Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		
		Dialogues.Log(TAG_CLASS, "Json: " + json, Log.INFO);
		
		return json;
	}
	
	public static String createJsonFromObjectWithoutExposeAnnotations(Object object) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(object);
		
		Dialogues.Log(TAG_CLASS, "Json: " + json, Log.INFO);
		
		return json;
	}
}
