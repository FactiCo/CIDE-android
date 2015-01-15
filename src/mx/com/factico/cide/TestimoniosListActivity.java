package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.typeface.TypefaceFactory;
import mx.com.factico.cide.views.CustomTextView;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestimoniosListActivity extends ActionBarActivity {
	public static final String TAG_CLASS = TestimoniosListActivity.class.getName();

	public static final String TAG_TESTIMONIO = "testimonio";

	private int categoyTypeIndex = -1;

	private String categoryName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimonios_list);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		Typeface typeface = TypefaceFactory.createTypeface(getBaseContext(), TypefaceFactory.RobotoSlab_Bold);
		mTitle.setTypeface(typeface);
		mTitle.setText(getResources().getString(R.string.testimonios_news));
		setSupportActionBar(mToolbar);

		getSupportActionBar().setElevation(5);
		mToolbar.setNavigationIcon(R.drawable.ic_action_close_white);
	}

	private void initUI() {
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			Testimonio testimonio = (Testimonio) bundle.getSerializable(TAG_TESTIMONIO);

			categoyTypeIndex = bundle.getInt(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX);

			String[] listCategories = getResources().getStringArray(R.array.testimonios_categories_titles);

			categoryName = listCategories[categoyTypeIndex];

			loadTestimoniosViews(testimonio);
		}
	}

	private void loadTestimoniosViews(Testimonio testimonio) {
		LinearLayout containerTestimonios = (LinearLayout) findViewById(R.id.testimonios_list_vg_container);

		// Dialogues.Log(TAG_CLASS, "/***** Testimonio", Log.INFO);
		// Dialogues.Log(TAG_CLASS, "Count: " + testimonio.getCount(), Log.INFO);

		List<Testimonio.Items> listItems = testimonio.getItems();
		// Dialogues.Log(TAG_CLASS, "Items Size: " + listItems.size(), Log.INFO);

		if (listItems != null && listItems.size() > 0) {
			for (int count = 0; count < listItems.size(); count++) {
				Testimonio.Items item = listItems.get(count);

				if (item != null) {
					// Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Name: " + item.getName(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Email: " + item.getEmail(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Category: " + item.getCategory(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Explanation: " + item.getExplanation(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items EntidadFederativa: " + item.getEntidadFederativa(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Age: " + item.getAge(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Gender: " + item.getGender(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Grade: " + item.getGrade(), Log.INFO);
					// Dialogues.Log(TAG_CLASS, "Items Created: " + item.getCreated(), Log.INFO);

					if (item.getCategory().equals(categoryName)) {
						if (item.isValid()) {
							View view = createItemView(item);
							containerTestimonios.addView(view);
						}
					}
				}
			}
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

		TextView tvDescription = (TextView) view.findViewById(R.id.item_testimonios_tv_description);
		tvDescription.setText(item.getExplanation());
		tvDescription.setMaxLines(Integer.MAX_VALUE);
		tvDescription.setTag(true);

		view.setTag(tvDescription);
		view.setOnClickListener(BtnShowTestimonioOnClickListener);

		return view;
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
