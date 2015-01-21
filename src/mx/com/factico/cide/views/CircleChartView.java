package mx.com.factico.cide.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;

public class CircleChartView extends View {
	private Paint mPaint;
	private Paint mFramePaint;
	private RectF mBigOval;
	private float mStart;
	private float mSweep;
	private boolean use_centre = false;
	private static final float SWEEP_INC = 3.0f;
	private static final float START_INC = 15;

	public CircleChartView(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(0x880000FF);
		mPaint.setStrokeWidth(4);

		mBigOval = new RectF(40, 10, 280, 250);

		mFramePaint = new Paint();
		mFramePaint.setAntiAlias(true);
		mFramePaint.setStrokeWidth(4);
	}

	public void startDraw(Style style, boolean use_centre) {
		this.use_centre = use_centre;
		mPaint.setStyle(style);
		mFramePaint.setStyle(style);
	}

	private void drawArcs(Canvas canvas, RectF oval, boolean useCenter, Paint paint) {
		// canvas.drawRect(oval, mFramePaint);
		canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);

		drawArcs(canvas, mBigOval, use_centre, mPaint);

		mSweep += SWEEP_INC;
		if (mSweep > 360) {
			mSweep -= 360;
			mStart += START_INC;
			if (mStart >= 360) {
				mStart -= 360;
			}
		}
		invalidate();
	}
}