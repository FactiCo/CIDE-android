package mx.com.factico.cide.parser;

import mx.com.factico.cide.dialogues.Dialogues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
	private static final String TAG_CLASS = JsonParser.class.getName();
	
	// JSON TAG 
    private static final String TAG_ROOT = "root";
    
    // JSON TAG LINEA
    private static final String TAG_NEXT = "next";
    
	public void parseJson(String json) {
		if (json != null) {
			try {
				JSONObject root = new JSONObject(json);
				
				if (root != null) {
					Dialogues.Log(TAG_CLASS, "/******* root", Log.INFO);
					
					// 
					JSONArray stc_metro = root.optJSONArray(TAG_ROOT);
					
					if (stc_metro != null) {
						Dialogues.Log(TAG_CLASS, "/******* root", Log.INFO);
						
						for (int i = 0; i < stc_metro.length(); i++) {
							JSONObject metro = stc_metro.optJSONObject(i);
							
							if (metro != null) {
								Dialogues.Log(TAG_CLASS, "/******* next", Log.INFO);
								
								// LINEAS
								JSONArray lineas = metro.optJSONArray(TAG_NEXT);
								
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
