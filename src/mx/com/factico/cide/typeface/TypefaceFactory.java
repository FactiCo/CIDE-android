package mx.com.factico.cide.typeface;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceFactory {
	public static final int RobotoSlab_Light = 0;
	public static final int RobotoSlab_Regular = 1;
	public static final int RobotoSlab_Bold = 2;
	public static final int RobotoSlab_Black = 3;
	public static final int SourceSansPro_Bold = 4;
	public static final int SourceSansPro_BoldItalic = 5;
	public static final int SourceSansPro_Light = 6;
	public static final int SourceSansPro_Regular = 7;
	public static final int SourceSansPro_Semibold = 8;
	public static final int SourceSansPro_SemiboldItalic = 9;
	
	private static String typefaceDirRobotoSlab = "fonts/Roboto_Slab/";
	
	private static String typefaceDirSourceSansPro = "fonts/Source_Sans_Pro/";
	
	public static Typeface createTypeface(Context context, int type) {
		if(type == RobotoSlab_Light) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRobotoSlab + "RobotoSlab-Light.ttf");
			return typeface;
		} else if(type == RobotoSlab_Regular) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRobotoSlab + "RobotoSlab-Regular.ttf");
			return typeface;
		} else if(type == RobotoSlab_Bold) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirRobotoSlab + "RobotoSlab-Bold.ttf");
			return typeface;
		} else if(type == SourceSansPro_Bold) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-Bold.ttf");
			return typeface;
		} else if(type == SourceSansPro_BoldItalic) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-BoldItalic.ttf");
			return typeface;
		} else if(type == SourceSansPro_Light) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-Light.ttf");
			return typeface;
		} else if(type == SourceSansPro_Regular) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-Regular.ttf");
			return typeface;
		} else if(type == SourceSansPro_Semibold) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-Semibold.ttf");
			return typeface;
		} else if(type == SourceSansPro_SemiboldItalic) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-SemiboldItalic.ttf");
			return typeface;
		} else {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceDirSourceSansPro + "SourceSansPro-Regular.ttf");
			return typeface;
		}
	}
}
