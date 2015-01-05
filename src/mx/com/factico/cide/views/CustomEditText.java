package mx.com.factico.cide.views;

import mx.com.factico.cide.R;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.regularexpressions.RegularExpressions;
import mx.com.factico.cide.typeface.TypefaceFactory;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

public class CustomEditText extends EditText {
	private int regexType;

	public CustomEditText(Context context) {
		super(context);
		init(context);
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
				Dialogues.Log("CustomTextView", "TYPEFACE: " + type, Log.ERROR);
				
				Typeface typeface = TypefaceFactory.setTypeface(context, type);
				setTypeface(typeface);
				
				break;
			}
		}
		typedArray.recycle();
	}
	
	public void setRegexType(int regexType) {
		this.regexType = regexType;

		addTextChangedListener(new MyCurrencyTextWatcher());
	}

	/**
	 * Show an error message in the {@link EditText}
	 * 
	 * @param errorMessage
	 *            (String) error message
	 * @param editText
	 *            (EditText) view
	 */
	public void setErrorMessage(String errorMessage) {
		Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
		startAnimation(shake);
			
		int errorColor = Color.WHITE;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);

		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorMessage);
		ssbuilder.setSpan(fgcspan, 0, errorMessage.length(), 0);
		setError(ssbuilder);
	}
	
	public static boolean isEditTextEmpty(EditText editText) {
		return editText.getText().toString().trim().length() == 0;
	}

	public class MyCurrencyTextWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}

		@Override
		public void afterTextChanged(Editable s) {
			String expression = s.toString();
			
			if (!expression.equals("")) {
				if (regexType == RegularExpressions.KEY_IS_STRING) {
					if (!RegularExpressions.isString(expression)) {
						setErrorMessage(getContext().getString(R.string.edittext_error_string));
					} else {
						setError(null);
					}
					
				} else if (regexType == RegularExpressions.KEY_IS_EMAIL) {
					if (!RegularExpressions.isEmail(expression)) {
						setErrorMessage(getContext().getString(R.string.edittext_error_email));
					} else {
						setError(null);
					}
					
				} else if (regexType == RegularExpressions.KEY_IS_NUMBER) {
					if (!RegularExpressions.isNumber(expression)) {
						setErrorMessage(getContext().getString(R.string.edittext_error_number));
					} else {
						setError(null);
					}
				}
			}
		}
	}
}