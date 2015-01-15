package mx.com.factico.cide;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class PoliticasActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_politicas);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		mTitle.setTextColor(getResources().getColor(R.color.title_color));
		mTitle.setText(getResources().getText(R.string.acercade_politicas));
		setSupportActionBar(mToolbar);
		
		mToolbar.setNavigationIcon(R.drawable.ic_action_close_gray);
		
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initUI() {
		WebView wvDescription = ((WebView)findViewById(R.id.politicas_wb_description));
		wvDescription.loadUrl("file:///android_asset/politicas/politicas.html"); 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
