package mx.com.factico.cide;

import java.util.ArrayList;
import java.util.Arrays;

import mx.com.factico.cide.adapters.SpinnerAdapter;
import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.regularexpressions.RegularExpressions;
import mx.com.factico.cide.views.CustomEditText;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestimoniosAddActivity extends ActionBarActivity implements OnClickListener {
	private static final String TAG_CLASS = TestimoniosAddActivity.class.getName();
	
	private CustomEditText etName;
	private CustomEditText etEmail;
	private Spinner spCategory;
	private CustomEditText etExplication;
	private Spinner spCity;
	private Spinner spAge;
	private Spinner spGender;
	private Spinner spGrade;
	
	private Button btnSendData;
	
	private int categoyTypeIndex = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimonios_add);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(getResources().getString(R.string.testimonios_add_new));
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
        setSupportActionBar(mToolbar);
	}
	
	public void initUI() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			categoyTypeIndex = bundle.getInt(TestimoniosActivity.TAG_CATEGORY_TYPE_INDEX);
		}
		
		etName = (CustomEditText) findViewById(R.id.testimonios_add_et_name); // Name
		etEmail = (CustomEditText) findViewById(R.id.testimonios_add_et_email); // Email

		etName.setRegexType(RegularExpressions.KEY_IS_STRING);
		etEmail.setRegexType(RegularExpressions.KEY_IS_EMAIL);
		
		spCategory = (Spinner) findViewById(R.id.testimonios_add_sp_category); // Category
		loadDataFromResources(spCategory); // Load data to spinner from resources
		
		etExplication = (CustomEditText) findViewById(R.id.testimonios_add_et_explication); // Explication
		
		spCity = (Spinner) findViewById(R.id.testimonios_add_sp_city); // City
		loadDataFromResources(spCity); // Load data to spinner from resources
		
		spAge = (Spinner) findViewById(R.id.testimonios_add_sp_age); // Age
		loadDataFromResources(spAge); // Load data to spinner from resources

		spGender = (Spinner) findViewById(R.id.testimonios_add_sp_gender); // Gender
		loadDataFromResources(spGender); // Load data to spinner from resources

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
				
			case R.id.testimonios_add_sp_gender:
				arayData = getResources().getStringArray(R.array.testimonios_add_gender);
				loadDataArrayAdapter(spinner, arayData, getResources().getString(R.string.testimonios_add_gender));
				break;
	
			default:
				break;
		}
	}

	public void loadDataArrayAdapter(Spinner spinner, String[] arrayData, String prompt) {
		ArrayList<String> listData = new ArrayList<String>();
		listData.add(prompt + " (Obligatorio)");
		listData.addAll(new ArrayList<String>(Arrays.asList(arrayData)));
		
		SpinnerAdapter dataAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, listData);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(dataAdapter);
		
		if (spinner.getId() == R.id.testimonios_add_sp_category) {
			spCategory.setSelection(categoyTypeIndex + 1);
			spCategory.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.testimonios_add_btn_senddata:
			validateEditText();
			break;
			
		case R.id.dialog_result_post_ok:
			finish();
			break;
			
		case R.id.dialog_result_post_share:
			shareInSocialMedia();
			break;

		default:
			break;
		}
	}
	
	private void validateEditText() {
		boolean isOkToSend = true;
		boolean hasError = false;
		
		if (etName.hasSyntaxError()) {
			hasError = true;
		}
		if (etEmail.hasSyntaxError()) {
			hasError = true;
		}
		if (etExplication.isEmpty()) {
			isOkToSend = false;
		}
		if (spCity.getSelectedItemPosition() == 0) {
			isOkToSend = false;
		}
		if (spAge.getSelectedItemPosition() == 0) {
			isOkToSend = false;
		}
		if (spGender.getSelectedItemPosition() == 0) {
			isOkToSend = false;
		}
		
		if (hasError) {
			Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.edittext_wrong_info), Toast.LENGTH_LONG);
		} else {
			if (!isOkToSend) {
				Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.edittext_emtpy), Toast.LENGTH_LONG);
			} else {
				sendData();
			}
		}
	}
	
	private void sendData() {
		Testimonio.Items testimonio = new Testimonio().new Items();
		
		testimonio.setName(etName.getText().toString()); // Getting and setting Name
		testimonio.setEmail(etEmail.getText().toString()); // Getting and setting Email
		testimonio.setCategory(spCategory.getSelectedItem().toString()); // Getting and setting Category
		testimonio.setExplanation(etExplication.getText().toString()); // Getting and setting Description
		testimonio.setState(String.valueOf(spCity.getSelectedItemPosition())); // Getting and setting City
		testimonio.setAge(spAge.getSelectedItem().toString()); // Getting and setting Age
		testimonio.setGender(spGender.getSelectedItem().toString()); // Getting and setting Gender
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
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			String json = gson.toJson(this.testimonio);
			
			String result = HttpConnection.POST(HttpConnection.ACTION_TESTIMONIOS, json);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			if (result != null) {
				// Dialogues.Toast(getApplicationContext(), "Result: " + result, Toast.LENGTH_LONG);
				Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);
				
				String resultCode = GsonParser.getResultFromJSON(result);
				if (resultCode.equals(GsonParser.TAG_RESULT_OK)) {
					showResultDialog(getResources().getString(R.string.dialog_message_testimonio));
				} else if (resultCode.equals(GsonParser.TAG_RESULT_ERROR)) {
					Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
				}
			} else {
				Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
			}
		}
	}
	
	@SuppressLint("InflateParams")
	private void showResultDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(R.layout.dialog_result_post, null, false);
		
		((TextView) view.findViewById(R.id.dialog_result_post_message)).setText(message);
		
		view.findViewById(R.id.dialog_result_post_ok).setOnClickListener(this);
		view.findViewById(R.id.dialog_result_post_share).setOnClickListener(this);
		
		builder.setView(view);
		
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private void shareInSocialMedia() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.close_white, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_close) {
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
