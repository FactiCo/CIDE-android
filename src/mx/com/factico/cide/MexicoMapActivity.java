package mx.com.factico.cide;

import mx.com.factico.cide.views.CustomMexicoMapView;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MexicoMapActivity extends ActionBarActivity {

	private ProgressBar progres;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mexicomap);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		mTitle.setText(getResources().getString(R.string.testimonios_btn_map));
		setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void initUI() {
		progres = (ProgressBar) findViewById(R.id.mexicomap_pb_loading);
		
		CustomMexicoMapView wvMap = (CustomMexicoMapView) findViewById(R.id.mexicomap_wv_map);

		WebSettings webSettings = wvMap.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wvMap.setWebChromeClient(new WebChromeClient());
		wvMap.setWebViewClient(new MyWebViewClient());
		wvMap.loadUrl(CustomMexicoMapView.MEXICO_MAP);
	}

	private class MyWebViewClient extends WebViewClient {
		private boolean loadingFinished;
		private boolean redirect;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
			if (!loadingFinished) {
				redirect = true;
			}

			loadingFinished = false;
			view.loadUrl(urlNewString);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap facIcon) {
			loadingFinished = false;
			// SHOW LOADING IF IT ISNT ALREADY VISIBLE
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (!redirect) {
				loadingFinished = true;
			}

			if (loadingFinished && !redirect) {
				// HIDE LOADING IT HAS FINISHED
				
				progres.setVisibility(View.INVISIBLE);
			} else {
				redirect = false;
			}

		}
	}
}
