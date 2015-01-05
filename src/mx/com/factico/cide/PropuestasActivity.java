package mx.com.factico.cide;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class PropuestasActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propuestas);
		
		setSupportActionBar();
	}
	
	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
        setSupportActionBar(mToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
