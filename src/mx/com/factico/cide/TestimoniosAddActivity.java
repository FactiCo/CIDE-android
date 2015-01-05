package mx.com.factico.cide;

import java.util.ArrayList;
import java.util.Arrays;

import mx.com.factico.cide.adapters.SpinnerAdapter;
import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.regularexpressions.RegularExpressions;
import mx.com.factico.cide.views.CustomEditText;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class TestimoniosAddActivity extends ActionBarActivity implements OnClickListener {
	private static final String TAG_CLASS = TestimoniosAddActivity.class.getName();
	
	private CustomEditText etName;
	private CustomEditText etEmail;
	private Spinner spCategory;
	private CustomEditText etDescription;
	private Spinner spCity;
	private Spinner spAge;
	private RadioGroup rgGender;
	private Spinner spGrade;
	
	private Button btnSendData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimonios_add);

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
		etName = (CustomEditText) findViewById(R.id.testimonios_add_et_name); // Name
		etEmail = (CustomEditText) findViewById(R.id.testimonios_add_et_email); // Email

		etName.setRegexType(RegularExpressions.KEY_IS_STRING);
		etEmail.setRegexType(RegularExpressions.KEY_IS_EMAIL);
		
		spCategory = (Spinner) findViewById(R.id.testimonios_add_sp_category); // Category
		loadDataFromResources(spCategory); // Load data to spinner from resources
		
		etDescription = (CustomEditText) findViewById(R.id.testimonios_add_et_description); // Description
		
		spCity = (Spinner) findViewById(R.id.testimonios_add_sp_city); // City
		loadDataFromResources(spCity); // Load data to spinner from resources
		spAge = (Spinner) findViewById(R.id.testimonios_add_sp_age); // Age
		loadDataFromResources(spAge); // Load data to spinner from resources

		rgGender = (RadioGroup) findViewById(R.id.testimonios_add_rg_gender); // Gender

		spGrade = (Spinner) findViewById(R.id.testimonios_add_sp_grade); // Grade
		loadDataFromResources(spGrade); // Load data to spinner from resources
		
		btnSendData = (Button) findViewById(R.id.testimonios_add_btn_senddata);
		btnSendData.setOnClickListener(this);
	}

	public void loadDataFromResources(Spinner spinner) {
		String[] arayData = null;
		
		switch (spinner.getId()) {
			case R.id.testimonios_add_sp_category:
				arayData = getResources().getStringArray(R.array.testimonios_categories_titles);
				loadDataArrayAdapter(spinner, arayData, getResources().getString(R.string.testimonios_add_category));
				break;
	
			case R.id.testimonios_add_sp_city:
				arayData = getResources().getStringArray(R.array.testimonios_add_cities);
				loadDataArrayAdapter(spinner, arayData, getResources().getString(R.string.testimonios_add_city));
				break;
	
			case R.id.testimonios_add_sp_age:
				arayData = getResources().getStringArray(R.array.testimonios_add_ages);
				loadDataArrayAdapter(spinner, arayData, getResources().getString(R.string.testimonios_add_age));
				break;
	
			case R.id.testimonios_add_sp_grade:
				arayData = getResources().getStringArray(R.array.testimonios_add_grades);
				loadDataArrayAdapter(spinner, arayData, getResources().getString(R.string.testimonios_add_grade));
				break;
	
			default:
				break;
		}
	}

	public void loadDataArrayAdapter(Spinner spinner, String[] arrayData, String prompt) {
		ArrayList<String> listData = new ArrayList<String>();
		listData.add(prompt);
		listData.addAll(new ArrayList<String>(Arrays.asList(arrayData)));
		
		SpinnerAdapter dataAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, listData);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(dataAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.testimonios_add_btn_senddata:
			sendData();
			break;

		default:
			break;
		}
	}
	
	public void sendData() {
		Testimonio.Items testimonio = new Testimonio().new Items();
		
		testimonio.setName(etName.getText().toString()); // Getting and setting Name
		testimonio.setEmail(etEmail.getText().toString()); // Getting and setting Email
		testimonio.setCategory(spCategory.getSelectedItem().toString()); // Getting and setting Category
		testimonio.setExplanation(etDescription.getText().toString()); // Getting and setting Description
		testimonio.setEntidadFederativa(String.valueOf(spCity.getSelectedItemPosition())); // Getting and setting City
		testimonio.setAge(spAge.getSelectedItem().toString()); // Getting and setting Age
		
		int radioButtonID = rgGender.getCheckedRadioButtonId();
		RadioButton radioButton = (RadioButton) rgGender.findViewById(radioButtonID);
		testimonio.setGender(radioButton.getText().toString()); // Getting and setting Gender
		
		testimonio.setGrade(spGrade.getSelectedItem().toString()); // Getting and setting Grade
		
		SendDataAsyncTask sendDataTask = new SendDataAsyncTask(testimonio); // Starting sending data task
		sendDataTask.execute();
	}
	
	private class SendDataAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private Testimonio.Items testimonio;
		
		public SendDataAsyncTask(Testimonio.Items testimonio) {
			this.testimonio = testimonio;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(TestimoniosAddActivity.this);
			dialog.setMessage(getResources().getString(R.string.postdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = HttpConnection.POST(HttpConnection.URL_TESTIMONIOS, testimonio);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			Dialogues.Toast(getApplicationContext(), "Result: " + result, Toast.LENGTH_LONG);
			Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.close, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_close) {
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
