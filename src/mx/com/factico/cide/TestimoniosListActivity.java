package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestimoniosListActivity extends ActionBarActivity {
	public static final String TAG_CLASS = TestimoniosListActivity.class.getName();
	
	public static final String TAG_TESTIMONIO = "testimonio";
	
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
        setSupportActionBar(mToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	private void initUI() {
		Bundle bundle = getIntent().getExtras();
		
		if (bundle != null) {
			Testimonio testimonio = (Testimonio) bundle.getSerializable(TAG_TESTIMONIO);
			
			loadTestimoniosViews(testimonio);
		}
	}
	
	private void loadTestimoniosViews(Testimonio testimonio) {
		LinearLayout containerTestimonios = (LinearLayout) findViewById(R.id.testimonios_list_vg_container);
		
		//Dialogues.Log(TAG_CLASS, "/***** Testimonio", Log.INFO);
		//Dialogues.Log(TAG_CLASS, "Count: " + testimonio.getCount(), Log.INFO);
		
		List<Testimonio.Items> listItems = testimonio.getItems();
		Dialogues.Log(TAG_CLASS, "Items Size: " + listItems.size(), Log.INFO);
		
		if (listItems != null  && listItems.size() > 0) {
			for (Testimonio.Items item : listItems) {
				//Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Name: " + item.getName(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Email: " + item.getEmail(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Category: " + item.getCategory(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Explanation: " + item.getExplanation(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items EntidadFederativa: " + item.getEntidadFederativa(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Age: " + item.getAge(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Gender: " + item.getGender(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Grade: " + item.getGrade(), Log.INFO);
				//Dialogues.Log(TAG_CLASS, "Items Created: " + item.getCreated(), Log.INFO);
				
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
}
