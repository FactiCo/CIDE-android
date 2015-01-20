package mx.com.factico.cide.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class AssetsUtils {

	public static String getStringFromAssets(Context context, String file) throws IOException {

		InputStream inputStream = context.getAssets().open(file);

		String text = convertStreamToString(inputStream);
		
		return text;
	}
	
	public static InputStream getInputStreamFromAssets(Context context, String file) throws IOException {

		InputStream inputStream = context.getAssets().open(file);
		
		return inputStream;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
