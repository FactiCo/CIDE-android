package mx.com.factico.cide;

import mx.com.factico.cide.facebook.FacebookUtils;
import mx.com.factico.cide.views.CustomTextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView.BufferType;

public class SettingsActivity extends ActionBarActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
        setSupportActionBar(mToolbar);
	}
	
	private void initUI() {
		CustomTextView tvAcercaDeTitle = (CustomTextView) findViewById(R.id.settings_tv_acercade_title);
		String textAcercaDe = tvAcercaDeTitle.getText().toString();
		SpannableString contentAcercaDe = new SpannableString(textAcercaDe);
	    contentAcercaDe.setSpan(new UnderlineSpan(), 0, textAcercaDe.length(), 0);
	    tvAcercaDeTitle.setText(contentAcercaDe, BufferType.SPANNABLE);
	    
	    CustomTextView tvTerminosTitle = (CustomTextView) findViewById(R.id.settings_tv_acercade_politicas);
		String textTerminos = tvTerminosTitle.getText().toString();
		SpannableString contentTerminos = new SpannableString(textTerminos);
	    contentTerminos.setSpan(new UnderlineSpan(), 0, textTerminos.length(), 0);
	    tvTerminosTitle.setText(contentTerminos, BufferType.SPANNABLE);
		
		findViewById(R.id.settings_tv_acercade_politicas).setOnClickListener(this);
		
		//findViewById(R.id.settings_btn_facebook_logout).setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.close_white, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_close) {
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_tv_acercade_politicas:
			openSeeMoreDescriptionIntent();
			break;

		/*case R.id.settings_btn_facebook_logout:
			FacebookUtils.callFacebookLogout(getBaseContext());
			break;*/
			
		default:
			break;
		}
	}
	
	private void openSeeMoreDescriptionIntent() {
		Intent intent = new Intent(getBaseContext(), PoliticasActivity.class);
		startActivity(intent);
	}
}