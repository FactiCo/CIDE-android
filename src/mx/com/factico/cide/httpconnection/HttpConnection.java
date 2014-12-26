package mx.com.factico.cide.httpconnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpConnection {
	public static final String TAG_CLASS = HttpConnection.class.getName();

	public static final String URL = "http://www.factico.com.mx/CIDE/APIBeta/expediente.php?";
	public static final String ADD = "q=add";
	public static final String GET_LIST = "q=getList";

	public static void GET(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		HttpResponse response;
		try {
			response = client.execute(request);

			Log.d("Response of GET request", response.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String POST(String url, Testimonio data) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("nombre", data.getName()));
		nameValuePair.add(new BasicNameValuePair("correo", data.getEmail()));
		nameValuePair.add(new BasicNameValuePair("categoria", data.getCategory()));
		nameValuePair.add(new BasicNameValuePair("explicacion", data.getDescription()));
		nameValuePair.add(new BasicNameValuePair("entidad", data.getCity()));
		nameValuePair.add(new BasicNameValuePair("edad", data.getAge()));
		nameValuePair.add(new BasicNameValuePair("genero", data.getGender()));
		nameValuePair.add(new BasicNameValuePair("escolaridad", data.getScholarity()));
		nameValuePair.add(new BasicNameValuePair("fecha_add", data.getTimestamp()));
		
		String result = null;
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
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
