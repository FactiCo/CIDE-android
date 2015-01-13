package mx.com.factico.cide;

import mx.com.factico.cide.views.CustomTextView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class SeeMoreDescriptionActivity extends ActionBarActivity {
	public static String TITLE = "";
	public static String DESCRIPTION = "";
	
	private String title = "";
	private String description = "";
	private TextView mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seemoredescription);

		setSupportActionBar();
		initUI(savedInstanceState);
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		mTitle.setTextColor(getResources().getColor(R.color.title_color));
		setSupportActionBar(mToolbar);
		
		mToolbar.setNavigationIcon(R.drawable.ic_action_close_gray);
		
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initUI(Bundle bundle) {
		if (bundle != null) {
			title = bundle.getString(TITLE, "");
			
			description = bundle.getString(DESCRIPTION, "");
			
			if (title.equals("")) {
				mTitle.setText("Justicia en el trabajo");
				mTitle.setTextColor(getResources().getColor(R.color.title_color));
			}
			if (description.equals(""))
				((CustomTextView)findViewById(R.id.seemoredescription_tv_description)).setText(description);
		}
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
