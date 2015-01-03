package mx.com.factico.cide.httpconnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpConnection {
	public static final String TAG_CLASS = HttpConnection.class.getName();

	// public static final String URL = "http://www.factico.com.mx/CIDE/APIBeta/expediente.php?";
	// public static final String ADD = "q=add";
	// public static final String GET_LIST = "q=getList";
	
	public static final String URL_TESTIMONIOS = "http://justiciacotidiana.mx:8080/justiciacotidiana/api/v1/testimonios";

	public static String GET(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		String result = null;
		
		HttpResponse response;
		try {
			response = client.execute(request);
			
			Dialogues.Log(TAG_CLASS, "Http Post Response:" + response.toString(), Log.DEBUG);
			
			HttpEntity httpEntity = response.getEntity();
			
			result = EntityUtils.toString(httpEntity);
			
			Dialogues.Log(TAG_CLASS, result, Log.ERROR);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String POST(String url, Testimonio.Items data) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		String result = null;
		
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(data);
			
			Dialogues.Log(TAG_CLASS, "JSON: " + json, Log.DEBUG);
			
		    httpPost.setEntity(new StringEntity(json));
		    httpPost.setHeader("Accept", "application/json");
		    httpPost.setHeader("Content-type", "application/json");
			
			// httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
			// httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			
			HttpResponse response = httpClient.execute(httpPost);
			Dialogues.Log(TAG_CLASS, "Http Post Response:" + response.toString(), Log.DEBUG);
			
			HttpEntity httpEntity = response.getEntity();
			
			result = EntityUtils.toString(httpEntity);
			
			Dialogues.Log(TAG_CLASS, result, Log.ERROR);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
