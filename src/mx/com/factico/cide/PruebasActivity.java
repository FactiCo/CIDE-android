package mx.com.factico.cide;

import java.lang.reflect.InvocationTargetException;

import mx.com.factico.cide.views.CircleChartView;
import mx.com.factico.cide.views.CustomWebView;
import mx.com.factico.cide.webviewsettings.VideoEnabledWebChromeClient;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class PruebasActivity extends ActionBarActivity {
	private CustomWebView webView;
	private VideoEnabledWebChromeClient webChromeClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pruebas);
		
		LinearLayout vgContainer = (LinearLayout) findViewById(R.id.pruebas_vg_container);
		
		CircleChartView pieChart = new CircleChartView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		pieChart.setLayoutParams(params);
		pieChart.startDraw(null);
		
		vgContainer.addView(pieChart);
		
		// initUI();
		
		//initWebView();
	}
	
	private void initWebView() {
		// Save the web view
		webView = (CustomWebView) findViewById(R.id.webview_data);

		// Initialize the VideoEnabledWebChromeClient and set event handlers
		View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view,read classcomments
		ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
		View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
		webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
		{
			// Subscribe to standard events, such as onProgressChanged()...
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Your code...
			}
		};
		webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
			@Override
			public void toggledFullscreen(boolean fullscreen) {
				// Your code to handle the full-screen change, for example
				// showing and hiding the title bar. Example:
				if (fullscreen) {
					WindowManager.LayoutParams attrs = getWindow().getAttributes();
					attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
					attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
					getWindow().setAttributes(attrs);
					if (android.os.Build.VERSION.SDK_INT >= 14) {
						getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
					}
				} else {
					WindowManager.LayoutParams attrs = getWindow().getAttributes();
					attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
					attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
					getWindow().setAttributes(attrs);
					if (android.os.Build.VERSION.SDK_INT >= 14) {
						getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
					}
				}

			}
		});
		webView.setWebChromeClient(webChromeClient);

		// Navigate everywhere you want, this classes have only been tested on
		// YouTube's mobile site
		webView.loadUrl("https://www.youtube.com/embed/il7WTHIKvq8");
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		// Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
		/*if (webChromeClient != null) {
			if (!webChromeClient.onBackPressed()) {
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					// Close app (presumably)
					super.onBackPressed();
				}
			}
		}*/
		
		((AudioManager) getSystemService(Context.AUDIO_SERVICE)).requestAudioFocus(new OnAudioFocusChangeListener() {
			@Override
			public void onAudioFocusChange(int focusChange) {}
		}, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		try {
	        Class.forName("android.webkit.WebView").getMethod("onPause", (Class[]) null).invoke(wvData, (Object[]) null);
	    } catch(ClassNotFoundException cnfe) {
	    } catch(NoSuchMethodException nsme) {
	    } catch(InvocationTargetException ite) {
	    } catch (IllegalAccessException iae) {
	    }
		/*((AudioManager) getSystemService(Context.AUDIO_SERVICE)).requestAudioFocus(new OnAudioFocusChangeListener() {
			@Override
			public void onAudioFocusChange(int focusChange) {}
		}, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);*/
	}
	
	
	private String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	private WebView wvData;
	
	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void initUI() {
		
		/*try {
			String html = AssetsUtils.getStringFromAssets(getBaseContext(), "prueba.html");
			
			HtmlParser.parseHtml3(getBaseContext(), html);
			
			/*Pattern p = Pattern.compile(imgRegex);
			Matcher m = p.matcher(html);
			if (m.find()) {
			   String s = m.group(1);
			   Dialogues.Log("Prueba", "Url imagen: " + s, Log.INFO);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		//String url = "<p>Propuesta para <strong>juntas<\/strong> <strong>vecinales<\/strong>, puedes ver un ejemplo en el siguiente video:<\/p>\r\n<p><iframe src=\"\/\/www.youtube.com\/embed\/il7WTHIKvq8\" width=\"405\" height=\"228\" frameborder=\"0\" allowfullscreen=\"allowfullscreen\"><\/iframe><\/p>\r\n<p>O en el siguiente enlace&nbsp;<a href=\"http:\/\/youtu.be\/il7WTHIKvq8\">http:\/\/youtu.be\/il7WTHIKvq8<\/a><\/p>\r\n<p>&nbsp;<\/p>\r\n<p><img src=\"http:\/\/l3.yimg.com\/bt\/api\/res\/1.2\/NgBOlAyfZJ3OA6.W8ZEZzQ--\/YXBwaWQ9eW5ld3M7cT04NQ--\/http:\/\/media.zenfs.com\/es-ES\/blogs\/finlaotracaradelamoneda\/justicia.jpg\" alt=\"\" width=\"200\" height=\"133\" \/><\/p>\r\n<p>&nbsp;<\/p>";
		
		wvData = (WebView) findViewById(R.id.webview_data);
		//wvData.loadDataWithBaseURL(mStory.getUrl(), mStory.getUnparsedContent(), "text/html; charset=utf-8", "UTF-8", null);
		wvData.loadUrl("file:///android_asset/prueba.html");
		wvData.setWebChromeClient(new WebChromeClient());
		wvData.getSettings().setPluginState(WebSettings.PluginState.ON);
		wvData.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
		wvData.setWebViewClient(new WebViewClient());
		wvData.getSettings().setJavaScriptEnabled(true);
	}
}
