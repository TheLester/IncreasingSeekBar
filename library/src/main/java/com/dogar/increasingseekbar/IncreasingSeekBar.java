package com.dogar.increasingseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class IncreasingSeekBar extends View {
	private static final String TAG              = IncreasingSeekBar.class.getSimpleName();
	private static final int    DEF_MAX_RANG     = 100;
	private static final int    DEF_COLOR        = Color.GREEN;
	private static final float  DEF_GAP_PERCENT  = 0.3f;//30%
	private static final float  DEF_BORDER_WIDTH = 5f;

	private int mWidth;
	private int mHeight;
	private int mBarWithGapWidth;
	private int mBarWidth;
	private int mGapWidth;

	private SparseArray<BarValue> mBarValues = new SparseArray<>();


	private Orientation mOrientation = Orientation.HORIZONTAL;
	private int         mMaxRang     = DEF_MAX_RANG;
	private int     mCurrValue;
	private float   mGapWidthPercent;
	private boolean mAllowUserTouch;
	private boolean mDrawBorder;
	private int     mBorderColor;
	private float   mBorderWidth;
	private int     mMainColor;
	private int     mStartColor;
	private int     mEndColor;

	private Paint mBarItemPaint;
	private Paint mBorderPaint;

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

	private void init(Context context, AttributeSet attrs, int defStyle) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IncreasingSeekBar,
				defStyle, 0);
		try {
			mOrientation = Orientation.fromId(typedArray.getInt(R.styleable.IncreasingSeekBar_isb_orientation, Orientation.HORIZONTAL.id));
			mMaxRang = typedArray.getInt(R.styleable.IncreasingSeekBar_isb_max, DEF_MAX_RANG);
			mCurrValue = typedArray.getInt(R.styleable.IncreasingSeekBar_isb_curr_value, 0);
			mGapWidthPercent = typedArray.getFraction(R.styleable.IncreasingSeekBar_isb_gap_percent, 1, 1, DEF_GAP_PERCENT);
			mAllowUserTouch = typedArray.getBoolean(R.styleable.IncreasingSeekBar_isb_allow_user_touch, true);
			mDrawBorder = typedArray.getBoolean(R.styleable.IncreasingSeekBar_isb_draw_border, false);
			mBorderColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_border_color, DEF_COLOR);
			mBorderWidth = typedArray.getDimension(R.styleable.IncreasingSeekBar_isb_border_width, DEF_BORDER_WIDTH);
			mMainColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color, DEF_COLOR);
			mStartColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color_start, -1);
			mEndColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color_end, -1);

		} finally {
			typedArray.recycle();
		}

		mBarItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBarItemPaint.setColor(mMainColor);
		mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		mBarItemPaint.setColor(mBorderColor);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = w;
		mHeight = h;
		initBarValues();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void initBarValues() {
		mBarWithGapWidth = mWidth / mMaxRang;
		mGapWidth = (int) (mBarWithGapWidth * mGapWidthPercent);
		mBarWidth = mBarWithGapWidth - mGapWidth;

		int heightDecreaseVal = mHeight / mMaxRang;
		int currentBarHeight = 0;
		for (int i = 0; i < mMaxRang; i++) {
			currentBarHeight += heightDecreaseVal;
			mBarValues.append(i, new BarValue(android.R.color.holo_green_light, currentBarHeight));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int curX = 0;
		for (int i = 0; i < mBarValues.size(); i++) {
			canvas.drawRect(curX, mHeight - mBarValues.get(i).height, curX + mBarWidth, mHeight, mBarItemPaint);
			curX += mBarWithGapWidth;
		}

		if (mDrawBorder) {
			canvas.drawRect(0, 0, getWidth(), getHeight(),
					mBorderPaint);
		}
		super.onDraw(canvas);
	}


	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (mAllowUserTouch) {

			float x = e.getX();
			float y = e.getY();

			switch (e.getAction()) {

				case MotionEvent.ACTION_DOWN:
					updateValue(x, y);
					invalidate();
				case MotionEvent.ACTION_MOVE:
					updateValue(x, y);
					invalidate();
					if (mSelectionListener != null)
						mSelectionListener.onSelectionUpdate(mValue, mMaxVal, mMinVal, this);
					break;
				case MotionEvent.ACTION_UP:
					updateValue(x, y);
					invalidate();
					if (mSelectionListener != null)
						mSelectionListener.onValueSelected(mValue, mMaxVal, mMinVal, this);
					break;
			}
			return true;
		} else
			return super.onTouchEvent(e);
	}

	private enum Orientation {
		HORIZONTAL(0), VERTICAL(1);
		int id;

		Orientation(int id) {
			this.id = id;
		}

		static Orientation fromId(int id) {
			for (Orientation orientation : values()) {
				if (orientation.id == id) return orientation;
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


	private static class BarValue {
		private int   color;
		private float height;

		public BarValue(int color, float height) {
			this.color = color;
			this.height = height;
		}
	}
}
