package mx.com.factico.cide;

import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class TestimoniosAddActivity extends ActionBarActivity implements OnClickListener {
	private static final String TAG_CLASS = TestimoniosAddActivity.class.getName();
	
	private EditText etName;
	private EditText etEmail;
	private Spinner spCategory;
	private EditText etDescription;
	private Spinner spCity;
	private Spinner spAge;
	private RadioGroup rgGender;
	private RadioButton rbGenderMale;
	private RadioButton rbGenderFemale;
	private Spinner spScholarity;
	
	private Button btnSendData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimonios_add);

		initUI();
	}

	public void initUI() {
		etName = (EditText) findViewById(R.id.testimonios_add_et_name); // Name
		etEmail = (EditText) findViewById(R.id.testimonios_add_et_email); // Email

		spCategory = (Spinner) findViewById(R.id.testimonios_add_sp_category); // Category
		loadDataFromResources(spCategory); // Load data to spinner from resources
		
		etDescription = (EditText) findViewById(R.id.testimonios_add_et_description); // Description
		
		spCity = (Spinner) findViewById(R.id.testimonios_add_sp_city); // City
		loadDataFromResources(spCity); // Load data to spinner from resources
		spAge = (Spinner) findViewById(R.id.testimonios_add_sp_age); // Age
		loadDataFromResources(spAge); // Load data to spinner from resources

		rgGender = (RadioGroup) findViewById(R.id.testimonios_add_rg_gender); // Gender
		rbGenderMale = (RadioButton) findViewById(R.id.testimonios_add_rb_gender_male); // Gender Male
		rbGenderFemale = (RadioButton) findViewById(R.id.testimonios_add_rb_gender_female); // Gender Female

		spScholarity = (Spinner) findViewById(R.id.testimonios_add_sp_scholarity); // Scholarity
		loadDataFromResources(spScholarity); // Load data to spinner from resources
		
		btnSendData = (Button) findViewById(R.id.testimonios_add_btn_senddata);
		btnSendData.setOnClickListener(this);
	}

	public void loadDataFromResources(Spinner spinner) {
		String[] listData = null;
		
		switch (spinner.getId()) {
			case R.id.testimonios_add_sp_category:
				listData = getResources().getStringArray(R.array.testimonios_add_categories);
				loadDataArrayAdapter(spinner, listData);
				break;
	
			case R.id.testimonios_add_sp_city:
				listData = getResources().getStringArray(R.array.testimonios_add_cities);
				loadDataArrayAdapter(spinner, listData);
				break;
	
			case R.id.testimonios_add_sp_age:
				listData = getResources().getStringArray(R.array.testimonios_add_ages);
				loadDataArrayAdapter(spinner, listData);
				break;
	
			case R.id.testimonios_add_sp_scholarity:
				listData = getResources().getStringArray(R.array.testimonios_add_scholarities);
				loadDataArrayAdapter(spinner, listData);
				break;
	
			default:
				break;
		}
	}

	public void loadDataArrayAdapter(Spinner spinner, String[] listData) {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listData);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		//spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	private class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> perent, View view, int position, long id) {}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	}
	
	private class CustomOnItemSelectedListenerMaterial implements android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener {
		@Override
		public void onItemSelected(android.support.v7.internal.widget.AdapterViewCompat<?> perent, View view, int position, long id) {}

		@Override
		public void onNothingSelected(android.support.v7.internal.widget.AdapterViewCompat<?> parent) {}
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
		Testimonio testimonio = new Testimonio();
		testimonio.setName(etName.getText().toString()); // Getting and setting Name
		testimonio.setEmail(etEmail.getText().toString()); // Getting and setting Email
		testimonio.setCategory(spCategory.getSelectedItem().toString()); // Getting and setting Category
		testimonio.setDescription(etDescription.getText().toString()); // Getting and setting Description
		testimonio.setCity(spCity.getSelectedItem().toString()); // Getting and setting City
		
		int radioButtonID = rgGender.getCheckedRadioButtonId();
		RadioButton radioButton = (RadioButton) rgGender.findViewById(radioButtonID);
		testimonio.setGender(radioButton.getText().toString()); // Getting and setting Gender
		
		testimonio.setScholarity(spScholarity.getSelectedItem().toString()); // Getting and setting Scholarity
		testimonio.setScholarity(spScholarity.getSelectedItem().toString()); // Getting and setting Scholarity
		
		SendDataAsyncTask sendDataTask = new SendDataAsyncTask(testimonio); // Starting sending data task
		sendDataTask.execute();
	}
	
	private class SendDataAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private Testimonio testimonio;
		
		public SendDataAsyncTask(Testimonio testimonio) {
			this.testimonio = testimonio;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(TestimoniosAddActivity.this);
			dialog.setMessage(getResources().getString(R.string.testimonios_add_senddata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = HttpConnection.POST(HttpConnection.URL + HttpConnection.ADD, testimonio);
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
}
