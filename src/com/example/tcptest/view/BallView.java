package com.example.tcptest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BallView extends View {
	
	private static final String TAG = "BallView";
	
	private int xOffset;
	private int yOffset;
	
	private int radius;
	
	private int midX;
	private int midY;
	
	private Paint paint;

	public BallView(Context context){
		this(context, null);
	}
	
	public BallView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public BallView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFFFFFF);
	}
	
	public void setRadius(int radius){
		this.radius = radius;
	}
	
	public void setPaintColor(int color){
		paint.setColor(color);
	}
	
	public void offset(int xOffset, int yOffset){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		invalidate();
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		midX = getMeasuredWidth() / 2;
		midY = getMeasuredHeight() / 2;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		canvas.drawCircle(midX + xOffset, midY + yOffset, radius, paint);
	}
}
