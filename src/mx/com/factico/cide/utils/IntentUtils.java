package mx.com.factico.cide.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

public class IntentUtils {

	public static boolean appIsALreadyInstalled(Activity activity, String packagename) {
		PackageManager pm = activity.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
}
