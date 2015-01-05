package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.animations.AnimationsFactory;
import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.interfaces.OnScrollViewListener;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.utils.ScreenUtils;
import mx.com.factico.cide.views.CustomScrollView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TestimoniosActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = TestimoniosActivity.class.getName();

	public static final String TAG_CATEGORY_TYPE_INDEX = "categoyTypeIndex";
	
	private int categoyTypeIndex = -1;

	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimonios);
		
		setSupportActionBar();
		initUI();
	}
	
	public void setSupportActionBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(255);
        setSupportActionBar(mToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void initUI() {
        Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			categoyTypeIndex = bundle.getInt(TAG_CATEGORY_TYPE_INDEX);
		}
		
		if (categoyTypeIndex != -1) {
			CustomScrollView svScroll = (CustomScrollView) findViewById(R.id.testimonios_sv_scroll);
			svScroll.setOnScrollViewListener(new OnScrollViewListener() {
				public void onScrollChanged(CustomScrollView v, int l, int t, int oldl, int oldt) {
					// Dialogues.Log(TAG_CLASS, "t: " + t + ", oldt: " + oldt, Log.INFO);
					setOnScrollChanged(v, l, t, oldl, oldt);
				}
			});

			String[] listCategories = getResources().getStringArray(R.array.testimonios_categories_titles);
			String[] listDesciptions = getResources().getStringArray(R.array.testimonios_categories_descriptions);
			
			((TextView) findViewById(R.id.testimonios_tv_title)).setText(listCategories[categoyTypeIndex]);
			((TextView) findViewById(R.id.testimonios_tv_desciption)).setText(listDesciptions[categoyTypeIndex]);
			
	        Point point = ScreenUtils.getScreenSize(this);
	        int width = point.x;
			int height = point.y;
	        
			// Set dimensions depending on screen
			findViewById(R.id.testimonios_iv_logo).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 10 * 3));
			// findViewById(R.id.testimonios_vg_description).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 10 * 3));
			// findViewById(R.id.testimonios_vg_container).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height / 10 * 3));
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width / 7, width / 7);
			params.topMargin = (height / 10 * 3) - (width / 6 / 3);
			params.rightMargin = width / 20;
			params.gravity = Gravity.END;
			Button btnAddTestimonio = (Button) findViewById(R.id.testimonios_btn_addtestimonio);
			btnAddTestimonio.setLayoutParams(params);
			btnAddTestimonio.setOnClickListener(this);
			
	        loadDataTestimonios();
		}
	}
	
	boolean isToolbarHide = false;
	private void setOnScrollChanged(CustomScrollView v, int l, int t, int oldl, int oldt) {
		//Dialogues.Log(TAG_CLASS, "t: " + t + ", oldt: " + oldt, Log.INFO);
		
		Dialogues.Log(TAG_CLASS, "Scroll: " + v.getScrollY(), Log.INFO);
		if (v.getScrollY() >= android.R.attr.actionBarSize) {
			Dialogues.Log(TAG_CLASS, "Hidding toolbar", Log.INFO);
			
			if (isToolbarHide) {
				Animation animation = AnimationsFactory.translateY(500, 0, -1 * android.R.attr.actionBarSize);
				mToolbar.startAnimation(animation);
			}
		}
	}
	
	public void loadDataTestimonios() {
		GetDataAsyncTask task = new GetDataAsyncTask();
		task.execute();
	}
	
	private class GetDataAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private ProgressBar pbLoading;
		
		public GetDataAsyncTask() {}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(TestimoniosActivity.this);
			dialog.setMessage(getResources().getString(R.string.getdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			//dialog.show();
			
			pbLoading = (ProgressBar) findViewById(R.id.testimonios_pb_loading);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = HttpConnection.GET(HttpConnection.URL_TESTIMONIOS);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			if (pbLoading.getVisibility() == View.VISIBLE) {
				pbLoading.setVisibility(View.GONE);
			}
			
			// Dialogues.Toast(getApplicationContext(), "Result: " + result, Toast.LENGTH_LONG);
			Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);
			
			Testimonio testimonio = null;
			try {
				testimonio = GsonParser.getTestimonioFromJSON(result);
				
				if (testimonio != null) {
					loadTestimoniosViews(testimonio);
				} else {
					Dialogues.Toast(getBaseContext(), getString(R.string.error), Toast.LENGTH_LONG);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadTestimoniosViews(Testimonio testimonio) {
		LinearLayout containerTestimonios = (LinearLayout) findViewById(R.id.testimonios_vg_container);
		
		Dialogues.Log(TAG_CLASS, "/***** Testimonio", Log.INFO);
		Dialogues.Log(TAG_CLASS, "Count: " + testimonio.getCount(), Log.INFO);
		
		List<Testimonio.Items> listItems = testimonio.getItems();
		Dialogues.Log(TAG_CLASS, "Items Size: " + listItems.size(), Log.INFO);
		
		if (listItems != null  && listItems.size() > 0) {
			for (Testimonio.Items item : listItems) {
				Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Name: " + item.getName(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Email: " + item.getEmail(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Category: " + item.getCategory(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Explanation: " + item.getExplanation(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items EntidadFederativa: " + item.getEntidadFederativa(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Age: " + item.getAge(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Gender: " + item.getGender(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Grade: " + item.getGrade(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Created: " + item.getCreated(), Log.INFO);
				
				View view = createItemView(item);
				containerTestimonios.addView(view);
			}
		}
	}
	
	@SuppressLint("InflateParams")
	private View createItemView(Testimonio.Items item) {
		View view = getLayoutInflater().inflate(R.layout.item_testimonios, null, false);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_testimonios_tv_title);
		if (item.getName() != null && !item.getName().equals("")) {
			tvTitle.setText(item.getName());
		} else {
			tvTitle.setVisibility(View.GONE);
		}
		
		TextView tvDescription = (TextView) view.findViewById(R.id.item_testimonios_tv_description);
		tvDescription.setText(item.getExplanation());
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.testimonios_btn_addtestimonio:
			openAddTetsimonioIntent();
			break;

		default:
			break;
		}
	}
	
	private void openAddTetsimonioIntent() {
		Intent intent = new Intent(getBaseContext(), TestimoniosAddActivity.class);
		startActivity(intent);
	}
}
