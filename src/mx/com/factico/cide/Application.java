package mx.com.factico.cide;

import com.parse.Parse;
import com.parse.PushService;

public class Application extends android.app.Application {

	public Application() {}

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the Parse SDK.
		Parse.initialize(this, "gbWE63wZfKRNtNJGVEXtmIggblHKDz8SEIMB2xui", "I0ngoj7SoUc1ndVMxJo7rerS4DvgHHWQSxl1P9Tn");

		// Specify an Activity to handle all pushes by default.
		PushService.setDefaultPushCallback(this.getBaseContext(), MainActivity.class);
		//PushService.startServiceIfRequired(this);
	}
	/**
	 * {
     "header": "My notification Title",
     "msg": "My Notification message",
     "action": "mx.com.factico.cide"
}
	 */
}