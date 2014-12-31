package mx.com.factico.cide;

import mx.com.factico.cide.dialogues.Dialogues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

public class GcmRegisterActivity extends ActionBarActivity {
	private static final String TAG_CLASS = GcmRegisterActivity.class.getName();

	private RadioButton genderFemaleButton;
	private RadioButton genderMaleButton;
	private EditText ageEditText;
	private RadioGroup genderRadioGroup;
	private TextView messageTextView;

	public static final String GENDER_MALE = "male";
	public static final String GENDER_FEMALE = "female";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gcmregister);

		// Track app opens.
		ParseAnalytics.trackAppOpened(getIntent());

		// Set up our UI member properties.
		this.genderFemaleButton = (RadioButton) findViewById(R.id.gender_female_button);
		this.genderMaleButton = (RadioButton) findViewById(R.id.gender_male_button);
		this.ageEditText = (EditText) findViewById(R.id.age_edit_text);
		this.genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
		this.messageTextView = (TextView) findViewById(R.id.gcmregister_tv_message);
	}

	@Override
	public void onStart() {
		super.onStart();

		// Display the current values for this user, such as their age and
		// gender.
		displayUserProfile();
		refreshUserProfile();
	}

	public void saveUserProfile(View view) {
		String ageTextString = ageEditText.getText().toString();

		if (ageTextString.length() > 0) {
			ParseInstallation.getCurrentInstallation().put("age", Integer.valueOf(ageTextString));
		}

		if (genderRadioGroup.getCheckedRadioButtonId() == genderFemaleButton.getId()) {
			ParseInstallation.getCurrentInstallation().put("gender", GENDER_FEMALE);
		} else if (genderRadioGroup.getCheckedRadioButtonId() == genderMaleButton.getId()) {
			ParseInstallation.getCurrentInstallation().put("gender", GENDER_MALE);
		} else {
			ParseInstallation.getCurrentInstallation().remove("gender");
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ageEditText.getWindowToken(), 0);

		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_success, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					e.printStackTrace();

					Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_failed, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}

	// Refresh the UI with the data obtained from the current ParseInstallation
	// object.
	private void displayUserProfile() {
		String gender = ParseInstallation.getCurrentInstallation().getString("gender");
		int age = ParseInstallation.getCurrentInstallation().getInt("age");

		String message = ParseInstallation.getCurrentInstallation().getString("message");
		messageTextView.setText(message);
		Dialogues.Log(TAG_CLASS, "Message: " + message, Log.INFO);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String json = bundle.getString("com.parse.Data", "No data from json parse");
			messageTextView.setText(json);
			Dialogues.Log(TAG_CLASS, "JSON: " + json, Log.INFO);
		}

		ParseObject jsonObject = ParseInstallation.getCurrentInstallation().getParseObject("factico");
		// messageTextView.setText(message);
		if (jsonObject != null)
			Dialogues.Log(TAG_CLASS, "JSON: " + jsonObject.toString(), Log.INFO);

		if (gender != null) {
			genderMaleButton.setChecked(gender.equalsIgnoreCase(GENDER_MALE));
			genderFemaleButton.setChecked(gender.equalsIgnoreCase(GENDER_FEMALE));
		} else {
			genderMaleButton.setChecked(false);
			genderFemaleButton.setChecked(false);
		}

		if (age > 0) {
			ageEditText.setText(Integer.valueOf(age).toString());
		}
	}

	// Get the latest values from the ParseInstallation object.
	private void refreshUserProfile() {
		ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					displayUserProfile();
				}
			}
		});
	}
}
