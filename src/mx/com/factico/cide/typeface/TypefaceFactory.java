package mx.com.factico.cide.typeface;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceFactory {
	public static final int ROBOTOSLAB_BOLD = 0;
	public static final int ROBOTOSLAB_LIGHT = 1;
	public static final int ROBOTOSLAB_REGULAR = 2;
	public static final int ROBOTOSLAB_THIN = 3;
	
	private static String typefaceDirRoboto = "fonts/RobotoSlab/";
	
	public static Typeface createTypeface(Context context, int type) {
		if(type == ROBOTOSLAB_BOLD) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRoboto + "RobotoSlab-Bold.ttf");
			return typeface;
		} else if(type == ROBOTOSLAB_LIGHT) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRoboto + "RobotoSlab-Light.ttf");
			return typeface;
		} else if(type == ROBOTOSLAB_REGULAR) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRoboto + "RobotoSlab-Regular.ttf");
			return typeface;
		} else if(type == ROBOTOSLAB_THIN) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRoboto + "RobotoSlab-Thin.ttf");
			return typeface;
		} else {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRoboto + "RobotoSlab-Regular.ttf");
			return typeface;
		}
	}
}
