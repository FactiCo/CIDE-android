package mx.com.factico.cide;

import java.util.Arrays;
import java.util.List;

import mx.com.factico.cide.beans.Facebook;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.preferences.PreferencesUtils;
import mx.com.factico.cide.utils.IntentUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = FacebookLoginActivity.class.getName();

	public static final int RESULT_CODE_LOGIN_OK = 1;
	public static final int RESULT_CODE_LOGIN_CANCEL = 0;
	public static final int REQUEST_CODE_LOGIN = 2015;
	
	private final List<String> PERMISSIONS_TO_READ = Arrays.asList("public_profile");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);
		
		if (IntentUtils.appIsALreadyInstalled(this, getResources().getString(R.string.facebook_package))) {
			if (isFacebookSessionActived()) {
				setResultOk();
			} else {
				PreferencesUtils.putPreference(getApplication(), PreferencesUtils.FACEBOOK, null);
				
				initUI();
			}
		} else {
			Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.facebook_is_not_installed), Toast.LENGTH_LONG);
		}
	}

	private void setResultOk() {
		setResult(FacebookLoginActivity.RESULT_CODE_LOGIN_OK);
		finish();
		overridePendingTransition(0, 0);
	}
	
	private void initUI() {
		findViewById(R.id.facebook_btn_facebook_login).setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	private boolean isFacebookSessionActived() {
		Session session = Session.getActiveSession();
		return (session != null && session.isOpened());
	}

	private void initFacebookSession() {
		Session session = new Session(this);
		Session.setActiveSession(session);
		Session.OpenRequest request = new Session.OpenRequest(this);
		request.setPermissions(PERMISSIONS_TO_READ);
		request.setCallback(SessionStatusCallback);
		session.openForRead(request);
	}

	private Session.StatusCallback SessionStatusCallback = new Session.StatusCallback() {

		// callback when session changes state
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (session.isOpened()) {
				// make request to the /me API
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							Facebook facebook = new Facebook(user.getId(), user.getName());
							String json = GsonParser.createJsonFromObject(facebook);
							
							PreferencesUtils.putPreference(getApplication(), PreferencesUtils.FACEBOOK, json);
							
							Dialogues.Toast(getApplicationContext(), "Hola " + user.getName() + "!" + "\nCon id: " + user.getId(), Toast.LENGTH_LONG);
							
							Settings.sdkInitialize(getBaseContext());
							
							setResultOk();
						}
					}
				}).executeAsync();
			} else {
				Dialogues.Toast(getApplicationContext(), "Edgar " + getResources().getString(R.string.facebook_session_is_not_open), Toast.LENGTH_LONG);
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.facebook_btn_facebook_login:
			initFacebookSession();
			break;

		default:
			break;
		}
	}
}
