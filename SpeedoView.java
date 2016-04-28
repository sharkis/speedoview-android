/*
 * SpeedoView.java
 * Albert Sharkis
 * sharkis@sharkis.org
 * 20140506
 *
 * Simple half-arc indicator for Android, no parameters yet
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

class SpeedoView extends View{
	private Paint mTextPaint;
	private Paint mDialPaint;
	private Paint needlePaint;
	private RectF mDialRect;
	private float maxspeed = 200.0f;
	private float cx, dimension;
	private double speed;

	//change later
	private int mTextColor = 0xffffffff;

	public SpeedoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		//TODO: make colors configurable
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(mTextColor);
		mTextPaint.setTextSize(40);
		mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mDialPaint.setColor(0xffffffff);
		mDialPaint.setStyle(Paint.Style.STROKE);
		needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		needlePaint.setColor(0xffff0000);
		needlePaint.setStrokeWidth(5.0f);
		needlePaint.setStyle(Paint.Style.STROKE);
		speed=0;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		float xpad = getPaddingLeft()+getPaddingRight();
		float ypad = getPaddingTop()+getPaddingBottom();
		dimension = Math.min(w-xpad,(h-ypad)*2);
		mDialRect = new RectF(getPaddingLeft(),getPaddingTop(),dimension-getPaddingRight(),dimension-getPaddingBottom());
		cx = (dimension - xpad)/2;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int minw = getPaddingLeft()+getPaddingRight()+getSuggestedMinimumWidth();
		int measuredWidth = resolveSize(minw,widthMeasureSpec);

		int minh = (MeasureSpec.getSize(measuredWidth)/2)+getPaddingTop()+getPaddingBottom();
		int measuredHeight = resolveSize(minh, heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//draw dial
		canvas.drawArc(mDialRect, 180, 180, false, mDialPaint);

		//draw needle
		double foo = speed*Math.PI/maxspeed;
		double needlex = dimension/2-(Math.cos(foo))*(dimension/2);
		double needley = dimension/2-(Math.sin(foo)*(dimension/2));
		canvas.drawLine(cx, dimension/2, (float)needlex, (float)needley, needlePaint);

		//draw numbers
		String speedstring = String.format("%.2f",speed);
		canvas.drawText(speedstring, cx-(mTextPaint.measureText(speedstring)/2), dimension/2, mTextPaint);
	}

	protected void setSpeed(double s){
		speed = s;
		invalidate();
		requestLayout();
	}
}
