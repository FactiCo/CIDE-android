package mx.com.factico.cide.shapes;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class ShapeDrawableFactory {

	public static LayerDrawable createOvalDrawable(int color) {
		ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
		smallerCircle.setIntrinsicHeight(10);
		smallerCircle.setIntrinsicWidth(10);
		smallerCircle.setBounds(new Rect(0, 0, 10, 10));
		smallerCircle.getPaint().setColor(color);
		smallerCircle.setPadding(50, 50, 50, 50);
		Drawable[] d = { smallerCircle};
		
		return new LayerDrawable(d);
	}
}
