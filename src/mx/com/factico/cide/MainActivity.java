package mx.com.factico.cide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	private int OPTION1 = 1;
	private int OPTION2 = 2;
	private int OPTION3 = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setSupportActionBar();
		initUI();
	}
	
	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		mToolbar.inflateMenu(R.menu.main);
		
        setSupportActionBar(mToolbar);
	}
	
	public void initUI() {
		findViewById(R.id.main_btn_option1).setOnClickListener(this);
		findViewById(R.id.main_btn_option2).setOnClickListener(this);
		findViewById(R.id.main_btn_option3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_option1:
			openIntent(OPTION1);
			break;
		case R.id.main_btn_option2:
			openIntent(OPTION2);
			break;
		case R.id.main_btn_option3:
			openIntent(OPTION3);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Open an activity
	 * 
	 * @param type (int) 
	 */
	private void openIntent(int type) {
		if (type == OPTION1) {
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			
		} else if (type == OPTION2) {
			Intent intent = new Intent(this, TestimoniosMenuActivity.class);
			startActivity(intent);
			
		} else if (type == OPTION3) {
			Intent intent = new Intent(this, PropuestasActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_main_settings) {
			openIntent();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void openIntent() {
		Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
		startActivity(intent);
	}
}
