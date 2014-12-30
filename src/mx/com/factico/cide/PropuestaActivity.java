package mx.com.factico.cide;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.views.CustomHtmlView;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class PropuestaActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propuestas);
		
		Bundle bundle = getIntent().getExtras();
		
		initUI(bundle);
	}
	
	private void initUI(Bundle bundle) {
		
		if (bundle != null) {
			TextView tvTitle = (TextView) findViewById(R.id.propuestas_tv_title);
			TextView tvDescription = (TextView) findViewById(R.id.propuestas_tv_description);
			
			String data = bundle.getString("data", null);
			Propuesta propuesta = GsonParser.getPropuestaFromJSON(data);
			
			tvTitle.setText(propuesta.getTitulo());
			tvDescription.setText(propuesta.getDescripcion());
		}
		
		WebView wbD3 = (WebView) findViewById(R.id.propuestas_wv_d3);
		WebSettings webSettings = wbD3.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wbD3.loadUrl("file:///android_asset/d3.html");
		
		/*ViewGroup container = (ViewGroup) findViewById(R.id.propuestas_vg_container);
		CustomHtmlView custom = new CustomHtmlView(getBaseContext());
		
		String html = readFromfile("prueba.html", this);
		custom.createCustomView(html);
		
		// Dialogues.Toast(getBaseContext(), html, Toast.LENGTH_LONG);
		container.addView(custom);*/
	}
	
	public String readFromfile(String fileName, Context context) {
		StringBuilder returnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				returnString.append(line);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return returnString.toString();
	}
}
