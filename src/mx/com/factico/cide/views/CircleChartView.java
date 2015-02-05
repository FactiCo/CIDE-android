package mx.com.factico.cide.views;

import java.util.List;

import mx.com.factico.cide.R;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Propuesta.Items.Question.Answers;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.utils.ScreenUtils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleChartView extends View {
	private RectF mBigOval;
	private List<Answers> listAnswers;

	private int[] listGrades;
	private Paint[] listPaints;
	
	private int width;
	private int height;
	
	private String[] colorsAnswers = {
			"#FF4A8293",
			"#FF4FB7AD",
			"#FF58CA9C",
			"#FF6CDA84" };
	private Paint paint;
	
	public CircleChartView(Context context) {
		super(context);
		init();
	}

	public CircleChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(4);
		paint.setColor(Color.WHITE);
	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
	     super.onSizeChanged(xNew, yNew, xOld, yOld);

	     width = xNew;
	     height = yNew;
	}
	
	public void startDraw(List<Propuesta.Items.Question.Answers> listAnswers) {
		this.listAnswers = listAnswers;
		
		Point point = ScreenUtils.getScreenSize(getContext());
		int screenWidth = point.x / 3;
		
		if (width == 0)
			width = screenWidth;
		if (height == 0)
			height = screenWidth;
		
		if (width < height)
	    	 mBigOval = new RectF(0, 0, width, width);
	     if (width > height)
	    	 mBigOval = new RectF(0, 0, height, height);
	     else
	    	 mBigOval = new RectF(0, 0, width, height);
		
		if (listAnswers != null) {
			listGrades = new int[listAnswers.size()];
			listPaints = new Paint[listAnswers.size()];
		}
		
		fillGrades();
	}

	private int getTotalCount() {
		int totalCount = 0;
		if (listAnswers != null) {
			for (Answers answer : listAnswers) {
				totalCount += Integer.parseInt(answer.getCount());
			}
		}
		
		return totalCount;
	}
	
	private void fillGrades() {
		int i = 0;
		
		if (listAnswers != null) {
			int totalCount = getTotalCount();
			
			for (Answers answer : listAnswers) {
				listPaints[i] = new Paint();
				listPaints[i].setAntiAlias(true);
				listPaints[i].setStyle(Paint.Style.FILL);
				listPaints[i].setStrokeWidth(4);
				listPaints[i].setColor(Color.parseColor(colorsAnswers[i]));
				
				int grades = Integer.parseInt(answer.getCount()) * 360 / totalCount;
				
				listGrades[i] = grades;
				
				Dialogues.Log("CircleChart", "/*Answer: " + answer.getTitle() + ", Grades: "+ grades, Log.INFO);
				
				i++;
			}
		}
		
		invalidate();
	}
	
	private void drawArcs(Canvas canvas, RectF oval, int startAngle, int sweepAngle, boolean useCenter, Paint paint) {
		canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(getResources().getColor(R.color.hololigth));

		if (mBigOval != null) {
			int i = 0;
			int startAngle = 0;
			int angle = 0;
			if (listPaints != null) {
				for (Paint paint : listPaints) {
					if (i == (listPaints.length - 1)) {
						angle = 360 - startAngle;
					} else {
						angle = listGrades[i];
					}
					drawArcs(canvas, mBigOval, startAngle, angle, true, paint);
					startAngle += listGrades[i];
					i++;
				}
			}
		} else {
			drawArcs(canvas, mBigOval, 0, 360, true, paint);
		}
	}
}