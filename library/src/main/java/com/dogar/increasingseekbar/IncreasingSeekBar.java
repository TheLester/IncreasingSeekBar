package com.dogar.increasingseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class IncreasingSeekBar extends View {
	private static final String TAG              = IncreasingSeekBar.class.getSimpleName();
	private static final int    DEFAULT_MAX_RANG = 100;

	private static final int DEFAULT_COLOR = Color.GREEN;

	private Position position = Position.HORIZONTAL;
	private int      maxRang  = DEFAULT_MAX_RANG;
	private int     currValue;
	private float   gapWidthPercent;
	private float   gapWidth;
	private boolean allowUserTouch;
	private int     mainColor;
	private int     startColor;
	private int     endColor;

	private Paint barItemPaint;

	public IncreasingSeekBar(Context context) {
		super(context);
		init(context, null, 0);
	}


	public IncreasingSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, R.attr.increasingSeekBar);
	}

	public IncreasingSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//		int height = mThumb.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();
//		height += (mAddedTouchBounds * 2);
		setMeasuredDimension(widthSize, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {


		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IncreasingSeekBar,
				defStyle, 0);
		try {
			position = Position.fromId(typedArray.getInt(R.styleable.IncreasingSeekBar_isb_position, Position.HORIZONTAL.id));
			maxRang = typedArray.getInt(R.styleable.IncreasingSeekBar_isb_max_count, DEFAULT_MAX_RANG);
			currValue = typedArray.getInt(R.styleable.IncreasingSeekBar_isb_curr_value, 0);
			gapWidthPercent = typedArray.getFraction(R.styleable.IncreasingSeekBar_isb_gap_percent, 1, 1, 0.5f);
			allowUserTouch = typedArray.getBoolean(R.styleable.IncreasingSeekBar_isb_allow_user_touch, true);
			mainColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color, DEFAULT_COLOR);
			startColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color_start, -1);
			endColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color_end, -1);

		} finally {
			typedArray.recycle();
		}

		barItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barItemPaint.setColor(mainColor);
	}

	private enum Position {
		HORIZONTAL(0), VERTICAL(1);
		int id;

		Position(int id) {
			this.id = id;
		}

		static Position fromId(int id) {
			for (Position position : values()) {
				if (position.id == id) return position;
			}
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Interface to propagate seekbar change event
	 */
	public interface OnProgressChangeListener {
		/**
		 * When the {@link IncreasingSeekBar} value changes
		 *
		 * @param seekBar  The IncreasingSeekBar
		 * @param value    the new value
		 * @param fromUser True if the progress change was initiated by the user.
		 */
		void onProgressChanged(IncreasingSeekBar seekBar, int value, boolean fromUser);

		void onStartTrackingTouch(IncreasingSeekBar seekBar);

		void onStopTrackingTouch(IncreasingSeekBar seekBar);
	}

}
