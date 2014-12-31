package mx.com.factico.cide;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SplashActivity extends ActionBarActivity {
	private static final long SPLASH_SCREEN_DELAY = 1500;
	private boolean isPaused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(!isPaused) {
					Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
					startActivity(mainIntent);
				}
				finish();
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, SPLASH_SCREEN_DELAY);
	}

	@Override
	public void onBackPressed() {}
	
	@Override
	public void onPause() {
		isPaused  = true;
	    super.onPause();
	}
}
