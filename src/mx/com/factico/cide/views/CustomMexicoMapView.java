package mx.com.factico.cide.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CustomMexicoMapView extends WebView {
	public static final String MEXICO_MAP = "http://justiciacotidiana.mx/es/JusticiaCotidiana/mapatestimonios";

	public CustomMexicoMapView(Context context) {
		super(context);
	}

	public CustomMexicoMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomMexicoMapView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
