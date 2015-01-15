package mx.com.factico.cide;

import mx.com.factico.cide.utils.ScreenUtils;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestimoniosMenuActivity extends ActionBarActivity implements OnClickListener {
	private int OPTION_TRABAJO = 1;
	private int OPTION_CIUDADANOS = 2;
	private int OPTION_FAMILIAS = 3;
	private int OPTION_EMPRENDEDORES = 4;
	private int OPTION_VECINAL = 5;
	private int OPTION_OTROS = 6;
	
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
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		mTitle.setText(getResources().getString(R.string.testimonio_name));
        setSupportActionBar(mToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void initUI() {
		Point point = ScreenUtils.getScreenSize(getBaseContext());
		int width = point.x;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width / 5);
		
		findViewById(R.id.testimonios_menu_iv_justice_trabajo).setLayoutParams(params);
		findViewById(R.id.testimonios_menu_iv_justice_ciudadanos).setLayoutParams(params);
		findViewById(R.id.testimonios_menu_iv_justice_familias).setLayoutParams(params);
		findViewById(R.id.testimonios_menu_iv_justice_emprendedores).setLayoutParams(params);
		findViewById(R.id.testimonios_menu_iv_justice_vecinal).setLayoutParams(params);
		findViewById(R.id.testimonios_menu_iv_justice_otros).setLayoutParams(params);
		
		findViewById(R.id.testimonios_menu_btn_justice_trabajo).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice_ciudadanos).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice_familias).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice_emprendedores).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice_vecinal).setOnClickListener(this);
		findViewById(R.id.testimonios_menu_btn_justice_otros).setOnClickListener(this);
		
		findViewById(R.id.testimonios_btn_map).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.testimonios_menu_btn_justice_trabajo:
			openIntent(OPTION_TRABAJO, 0);
			break;
			
		case R.id.testimonios_menu_btn_justice_ciudadanos:
			openIntent(OPTION_CIUDADANOS, 1);
			break;
			
		case R.id.testimonios_menu_btn_justice_familias:
			openIntent(OPTION_FAMILIAS, 2);
			break;
			
		case R.id.testimonios_menu_btn_justice_emprendedores:
			openIntent(OPTION_EMPRENDEDORES, 3);
			break;
			
		case R.id.testimonios_menu_btn_justice_vecinal:
			openIntent(OPTION_VECINAL, 4);
			break;
			
		case R.id.testimonios_menu_btn_justice_otros:
			openIntent(OPTION_OTROS, 5);
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
		if (type == OPTION_TRABAJO) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION_CIUDADANOS) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION_FAMILIAS) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION_EMPRENDEDORES) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION_VECINAL) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		}  else if (type == OPTION_OTROS) {
			Intent intent = new Intent(this, TestimoniosActivity.class);
			intent.putExtra(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX, index);
			startActivity(intent);
			
		} else if (type == OPTION_MAPVIEW) {
			Intent intent = new Intent(this, MexicoMapActivity.class);
			startActivity(intent);
		}
	}
}
