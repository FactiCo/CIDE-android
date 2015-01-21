package mx.com.factico.cide.views;

import mx.com.factico.cide.interfaces.OnScrollViewListener;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
	private OnScrollViewListener mOnScrollViewListener;

	public CustomScrollView(Context context) {
		super(context);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnScrollViewListener(OnScrollViewListener l) {
		this.mOnScrollViewListener = l;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (mOnScrollViewListener != null)
			mOnScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
