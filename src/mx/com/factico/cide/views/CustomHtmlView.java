package mx.com.factico.cide.views;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomHtmlView extends LinearLayout {
	public static final String TAG_CLASS = CustomHtmlView.class.getName();
	
	private String TAG_IMAGE = "<img";
	
	public CustomHtmlView(Context context) {
		super(context);
	}

	public CustomHtmlView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomHtmlView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void createCustomView(String html) {
		String text = new String(html);
		
		TextView textView = new TextView(getContext());
		//Spanned sp = Html.fromHtml(text, new ImageGetter(), null);
		Spanned sp = Html.fromHtml(text);
		textView.setText(sp);
		
		addView(textView);
		
		/*while(hasImage(html)) {
			int indexOfStart = text.indexOf(TAG_IMAGE);
			
			String substring = text.substring(indexOfStart);
			
			int indexOfEnd = substring.indexOf("/>");
			
			// String imageSrc = 
		}*/
	}
	
	/*private class ImageGetter implements Html.ImageGetter {
		public Drawable getDrawable(String source) {
			int id;
			if (source.equals("hughjackman.jpg")) {
				id = R.drawable.hughjackman;
			} else {
				return null;
			}

			Drawable d = getResources().getDrawable(id);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			return d;
		}
	};*/
	
	protected boolean hasImage(String text) {
		if (text.contains(TAG_IMAGE))
			return true;
		else
			return false;
	}
}