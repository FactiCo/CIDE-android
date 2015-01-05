package mx.com.factico.cide.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MexicoMapView extends WebView {
	public static final String MEXICO_MAP = "http://www.carloscastellanos.com.mx/mapas/mexico.html";

	public MexicoMapView(Context context) {
		super(context);
	}

	public MexicoMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MexicoMapView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
