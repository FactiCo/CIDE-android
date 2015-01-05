package mx.com.factico.cide.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {
	
	public static Point getScreenSize(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		return size;
	}
	
	public static Point getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		Point size = new Point();
		display.getSize(size);
		
		return size;
	}
}
