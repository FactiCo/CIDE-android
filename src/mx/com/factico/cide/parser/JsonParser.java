package mx.com.factico.cide.parser;

import mx.com.factico.cide.dialogues.Dialogues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
	private static final String TAG_CLASS = JsonParser.class.getName();
	
	// JSON TAG PROPUESTAS
    private static final String TAG_PROPUESTAS = "propuestas";
    
    private static final String TAG_PROPUESTAS_ID = "id";
    private static final String TAG_PROPUESTAS_CATEGORIA = "categoria";
    private static final String TAG_PROPUESTAS_TITULO = "titulo";
    private static final String TAG_PROPUESTAS_DESCRIPCION = "descripcion";
    private static final String TAG_PROPUESTAS_TIMESTAMP = "fecha_propuesta";
    
    // JSON TAG PROPUESTAS AUTOR
    private static final String TAG_PROPUESTAS_AUTOR = "autor";
    
    private static final String TAG_PROPUESTAS_AUTOR_ID = "id_FB";
    private static final String TAG_PROPUESTAS_AUTOR_NOMBRE = "nombre";
    private static final String TAG_PROPUESTAS_AUTOR_MAIL = "mail";
    
    // JSON TAG PROPUESTAS VOTOS
    private static final String TAG_PROPUESTAS_VOTOS = "votos";
    
    private static final String TAG_PROPUESTAS_VOTOS_FAVOR = "favor";
    private static final String TAG_PROPUESTAS_VOTOS_CONTRA = "contra";
    private static final String TAG_PROPUESTAS_VOTOS_ABSTENCION = "abstencion";
    
    private static final String TAG_PROPUESTAS_VOTOS_LINK = "link";
    
    // JSON TAG PROPUESTAS VOTOS
    private static final String TAG_PROPUESTAS_VOTOS_PARTICIPANTES = "participantes";
    
    private static final String TAG_PROPUESTAS_VOTOS_PARTICIPANTES_ID = "id";
    private static final String TAG_PROPUESTAS_VOTOS_PARTICIPANTES_NAME = "name";
    
    
	public void parseJson(String json) {
		if (json != null) {
			try {
				JSONObject root = new JSONObject(json);
				
				if (root != null) {
					Dialogues.Log(TAG_CLASS, "/******* root", Log.INFO);
					
					// 
					JSONArray stc_metro = root.optJSONArray(TAG_PROPUESTAS);
					
					if (stc_metro != null) {
						Dialogues.Log(TAG_CLASS, "/******* root", Log.INFO);
						
						for (int i = 0; i < stc_metro.length(); i++) {
							JSONObject metro = stc_metro.optJSONObject(i);
							
							if (metro != null) {
								Dialogues.Log(TAG_CLASS, "/******* next", Log.INFO);
								
								// LINEAS
								JSONArray lineas = metro.optJSONArray(TAG_PROPUESTAS_AUTOR);
								
								if (lineas != null) {
									for (int j = 0; j < lineas.length(); j++) {
										JSONObject linea = lineas.optJSONObject(j);
										
										if (linea != null) {
											
										}
									} // end for next
								} // end next != null
								
							} // end root != null
						} // end for root
					} // end root != null
					
				} // end root
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
