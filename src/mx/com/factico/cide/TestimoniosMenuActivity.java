package mx.com.factico.cide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

public class TestimoniosMenuActivity extends ActionBarActivity implements OnClickListener {
	private int OPTION1 = 1;
	private int OPTION2 = 2;
	private int OPTION3 = 3;
	private int OPTION4 = 4;
	private int OPTION5 = 5;
	
	private int OPTION_MAPVIEW = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimonios_menu);
		
		setSupportActionBar();
		initUI();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, 0);
	}
	
	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(getResources().getString(R.string.testimonio_name));
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
        setSupportActionBar(mToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void initUI() {
		findViewById(R.id.testimonios_menu_btn_justice1).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice2).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice3).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice4).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice5).setOnClickListener(this);
		
		findViewById(R.id.testimonios_btn_map).setOnClickListener(this);;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.testimonios_menu_btn_justice1:
			openIntent(OPTION1, 0);
			break;
			
		case R.id.testimonios_menu_btn_justice2:
			openIntent(OPTION2, 1);
			break;
			
		case R.id.testimonios_menu_btn_justice3:
			openIntent(OPTION3, 2);
			break;
			
		case R.id.testimonios_menu_btn_justice4:
			openIntent(OPTION4, 3);
			break;
			
		case R.id.testimonios_menu_btn_justice5:
			openIntent(OPTION5, 4);
			break;

		case R.id.testimonios_btn_map:
			openIntent(OPTION_MAPVIEW, -1);
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
	private void openIntent(int type, int index) {
		if (type == OPTION1) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION2) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION3) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION4) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION5) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION_MAPVIEW) {
			Intent intent = new Intent(this, MexicoMapActivity.class);
			startActivity(intent);
		}
	}
}
