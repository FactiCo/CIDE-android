package mx.com.factico.cide.views;

import mx.com.factico.cide.R;
import mx.com.factico.cide.typeface.TypefaceFactory;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {
	
	public CustomButton(Context context) {
		super(context);
		if (!isInEditMode())
			init(context);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			init(context, attrs);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode())
			init(context, attrs);
	}
	
	public void init(Context context) {
		
	}
	
	public void init(Context context, AttributeSet attrs) {
		if (attrs == null || getContext() == null) {
            return;
        }
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

		if (typedArray == null) {
            return;
        }
		
		final int N = typedArray.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CustomTextView_typefaceRoboto:
				int type = Integer.parseInt(typedArray.getString(attr));
				
				Typeface typeface = TypefaceFactory.createTypeface(context, type);
				setTypeface(typeface);
				
				break;
			}
		}
		typedArray.recycle();
	}
}