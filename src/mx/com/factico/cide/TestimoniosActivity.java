package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.httpconnection.NetworkUtils;
import mx.com.factico.cide.interfaces.OnScrollViewListener;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.typeface.TypefaceFactory;
import mx.com.factico.cide.utils.ScreenUtils;
import mx.com.factico.cide.views.CustomScrollView;
import mx.com.factico.cide.views.CustomTextView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TestimoniosActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = TestimoniosActivity.class.getName();

	public static final String TAG_CATEGORY_TYPE_INDEX = "categoyTypeIndex";

	private int categoyTypeIndex = -1;

	private Toolbar mToolbar;

	private Testimonio testimonio = null;

	private String categoryName;

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
		mToolbar.getBackground().setAlpha(0);
		setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void initUI() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			categoyTypeIndex = bundle.getInt(TAG_CATEGORY_TYPE_INDEX);
		}

		if (categoyTypeIndex != -1) {
			CustomScrollView svScroll = (CustomScrollView) findViewById(R.id.testimonios_sv_scroll);
			svScroll.setOnScrollViewListener(new OnScrollViewListener() {
				public void onScrollChanged(CustomScrollView v, int l, int t, int oldl, int oldt) {
					// setOnScrollChanged(v, l, t, oldl, oldt);
				}
			});

			String[] listCategories = getResources().getStringArray(R.array.testimonios_categories_titles);
			String[] listDesciptions = getResources().getStringArray(R.array.testimonios_categories_descriptions);

			categoryName = listCategories[categoyTypeIndex];

			((CustomTextView) findViewById(R.id.testimonios_tv_title)).setText(categoryName);
			((CustomTextView) findViewById(R.id.testimonios_tv_desciption)).setText(listDesciptions[categoyTypeIndex]);

			Point point = ScreenUtils.getScreenSize(this);
			int width = point.x;
			int height = point.y;

			// Set dimensions depending on screen
			ImageView ivLogo = (ImageView) findViewById(R.id.testimonios_iv_logo);
			ivLogo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 10 * 2));
			ivLogo.setScaleType(ScaleType.CENTER_INSIDE);
			if (categoyTypeIndex == 0)
				ivLogo.setImageResource(R.drawable.ic_justicia_trabajo);
			if (categoyTypeIndex == 1)
				ivLogo.setImageResource(R.drawable.ic_justicia_familias);
			if (categoyTypeIndex == 2)
				ivLogo.setImageResource(R.drawable.ic_justicia_vecinal);
			if (categoyTypeIndex == 3)
				ivLogo.setImageResource(R.drawable.ic_justicia_ciudadanos);
			if (categoyTypeIndex == 4)
				ivLogo.setImageResource(R.drawable.ic_justicia_emprendedores);
			
			findViewById(R.id.testimonios_vg_description).setOnClickListener(this);

			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width / 7, width / 7);
			params.topMargin = (height / 10 * 3) - (width / 6 / 3);
			params.rightMargin = width / 20;
			params.gravity = Gravity.END;
			TextView btnAddTestimonio = (TextView) findViewById(R.id.testimonios_btn_addtestimonio);
			// btnAddTestimonio.setLayoutParams(params);
			btnAddTestimonio.setOnClickListener(this);

			loadDataTestimonios();
		}
	}

	public void loadDataTestimonios() {
		if (NetworkUtils.isNetworkConnectionAvailable(getBaseContext())) {
			GetDataAsyncTask task = new GetDataAsyncTask();
			task.execute();
		} else {
			Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
		}
	}

	private class GetDataAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private ProgressBar pbLoading;

		public GetDataAsyncTask() {
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(TestimoniosActivity.this);
			dialog.setMessage(getResources().getString(R.string.getdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			// dialog.show();

			pbLoading = (ProgressBar) findViewById(R.id.testimonios_pb_loading);
			pbLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = HttpConnection.GET(HttpConnection.ACTION_TESTIMONIOS);
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

			// Dialogues.Toast(getApplicationContext(), "Result: " + result,
			// Toast.LENGTH_LONG);
			// Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);

			loadDataFromJson(result);
		}
	}

	private void loadDataFromJson(String json) {
		try {
			testimonio = GsonParser.getTestimonioFromJSON(json);

			if (testimonio != null) {
				loadTestimoniosViews(testimonio);
			} else {
				Dialogues.Toast(getBaseContext(), getString(R.string.error_testimonios_recientes), Toast.LENGTH_LONG);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadTestimoniosViews(Testimonio testimonio) {
		LinearLayout containerTestimonios = (LinearLayout) findViewById(R.id.testimonios_vg_container_items);

		// Dialogues.Log(TAG_CLASS, "/***** Testimonio", Log.INFO);
		// Dialogues.Log(TAG_CLASS, "Count: " + testimonio.getCount(),
		// Log.INFO);

		List<Testimonio.Items> listItems = testimonio.getItems();
		// Collections.reverse(listItems);

		// Dialogues.Log(TAG_CLASS, "Items Size: " + listItems.size(),
		// Log.INFO);

		if (listItems != null && listItems.size() > 0) {
			int countToShow = 3;
			int i = 0;

			boolean hasInCategory = false;

			for (int count = 0; count < listItems.size(); count++) {
				Testimonio.Items item = listItems.get(count);

				if (item != null) {
					// Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(),
					// Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Name: " + item.getName(),
					// Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Email: " +
					// item.getEmail(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Category: " +
					// item.getCategory(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Explanation: " +
					// item.getExplanation(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items State: " +
					// item.getState(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Age: " + item.getAge(),
					// Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Gender: " +
					// item.getGender(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Grade: " +
					// item.getGrade(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Created: " +
					// item.getCreated(), Log.INFO);

					if (item.getCategory().equals(categoryName)) {
						hasInCategory = true;
						i += 1;

						if (i <= countToShow) {
							View view = createItemView(item);
							containerTestimonios.addView(view);
						} else {
							break;
						}
					}
				}
			}

			if (hasInCategory) {
				LinearLayout vgContainerItemsBtn = (LinearLayout) findViewById(R.id.testimonios_vg_container_items_btn);

				Button btnMoreTestimonios = new Button(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.CENTER_HORIZONTAL;
				params.topMargin = -40;
				btnMoreTestimonios.setLayoutParams(params);

				btnMoreTestimonios.setPadding(100, 0, 100, 0);
				btnMoreTestimonios.setBackgroundResource(R.drawable.selector_btn_other);
				btnMoreTestimonios.setText(getResources().getString(R.string.testimonios_seemore));
				btnMoreTestimonios.setTextColor(getResources().getColor(R.color.white));
				btnMoreTestimonios.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				btnMoreTestimonios.setOnClickListener(BtnSeeMoreOnClickListener);

				Typeface typeface = TypefaceFactory.createTypeface(getBaseContext(), TypefaceFactory.RobotoSlab_Regular);
				btnMoreTestimonios.setTypeface(typeface);

				containerTestimonios.setOnClickListener(BtnSeeMoreOnClickListener);
				vgContainerItemsBtn.addView(btnMoreTestimonios);
			} else {
				TextView tvNoTestimonios = new TextView(this);
				tvNoTestimonios.setPadding(30, 30, 30, 30);
				tvNoTestimonios.setBackgroundColor(getResources().getColor(R.color.white));
				tvNoTestimonios.setText(getResources().getString(R.string.testimonios_nomore));
				tvNoTestimonios.setTextColor(getResources().getColor(R.color.title_color));
				tvNoTestimonios.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				tvNoTestimonios.setGravity(Gravity.CENTER);

				Typeface typeface = TypefaceFactory.createTypeface(getBaseContext(), TypefaceFactory.RobotoSlab_Regular);
				tvNoTestimonios.setTypeface(typeface);

				containerTestimonios.addView(tvNoTestimonios);
			}
		} else {
			TextView tvNoTestimonios = new TextView(this);
			tvNoTestimonios.setPadding(30, 30, 30, 30);
			tvNoTestimonios.setBackgroundColor(getResources().getColor(R.color.white));
			tvNoTestimonios.setText(getResources().getString(R.string.testimonios_nomore));
			tvNoTestimonios.setTextColor(getResources().getColor(R.color.title_color));
			tvNoTestimonios.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			tvNoTestimonios.setGravity(Gravity.CENTER);

			Typeface typeface = TypefaceFactory.createTypeface(getBaseContext(), TypefaceFactory.SourceSansPro_Regular);
			tvNoTestimonios.setTypeface(typeface);

			containerTestimonios.addView(tvNoTestimonios);
		}
	}

	@SuppressLint("InflateParams")
	private View createItemView(Testimonio.Items item) {
		View view = getLayoutInflater().inflate(R.layout.item_testimonios, null, false);

		ImageView ivIcUser = (ImageView) view.findViewById(R.id.item_testimonios_iv_icuser);
		if (item.getGender().equals("Hombre")) {
			ivIcUser.setImageResource(R.drawable.ic_hombre);
		} else if (item.getGender().equals("Mujer")) {
			ivIcUser.setImageResource(R.drawable.ic_mujer);
		}

		TextView tvTitle = (TextView) view.findViewById(R.id.item_testimonios_tv_title);
		if (item.getName() != null && !item.getName().equals("")) {
			tvTitle.setText(item.getName());
		} else {
			tvTitle.setVisibility(View.GONE);
		}

		CustomTextView tvDescription = (CustomTextView) view.findViewById(R.id.item_testimonios_tv_description);
		tvDescription.setText(item.getExplanation());
		tvDescription.setTag(false);

		view.setTag(tvDescription);
		view.setOnClickListener(BtnShowTestimonioOnClickListener);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.testimonios_btn_addtestimonio:
			openAddTestimonioIntent();
			break;

		case R.id.testimonios_vg_description:
			openSeeMoreDescriptionIntent();
			break;
		
		default:
			break;
		}
	}

	OnClickListener BtnShowTestimonioOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			CustomTextView tvDescription = (CustomTextView) v.findViewById(R.id.item_testimonios_tv_description);
			boolean state = (boolean) tvDescription.getTag();
			
			if (tvDescription.getLineCount() > 3) {
				if (state) {
					tvDescription.setMaxLines(3);
					state = false;
				} else {
					tvDescription.setMaxLines(Integer.MAX_VALUE);
					state = true;
				}
			}
			
			tvDescription.setTag(state);
		}
	};

	OnClickListener BtnSeeMoreOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			openListTestimonioIntent();
		}
	};

	private void openListTestimonioIntent() {
		Intent intent = new Intent(getBaseContext(), TestimoniosListActivity.class);
		intent.putExtra(TestimoniosListActivity.TAG_TESTIMONIO, testimonio);
		intent.putExtra(TAG_CATEGORY_TYPE_INDEX, categoyTypeIndex);
		startActivity(intent);
	}

	private void openSeeMoreDescriptionIntent() {
		Intent intent = new Intent(getBaseContext(), SeeMoreDescriptionActivity.class);
		intent.putExtra(SeeMoreDescriptionActivity.TITLE, "");
		intent.putExtra(SeeMoreDescriptionActivity.DESCRIPTION, "");
		startActivity(intent);
	}
	
	private void openAddTestimonioIntent() {
		Intent intent = new Intent(getBaseContext(), TestimoniosAddActivity.class);
		intent.putExtra(TAG_CATEGORY_TYPE_INDEX, categoyTypeIndex);
		startActivity(intent);
	}
}
