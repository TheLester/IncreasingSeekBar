package com.dogar.increasingseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

public class IncreasingSeekBar extends View {
	private static final int DEFAULT_MAX_RANG = 100;

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

	private int      maxRang  = DEFAULT_MAX_RANG;
	private Position position = Position.HORIZONTAL;

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
		public void onProgressChanged(IncreasingSeekBar seekBar, int value, boolean fromUser);

		public void onStartTrackingTouch(IncreasingSeekBar seekBar);

		public void onStopTrackingTouch(IncreasingSeekBar seekBar);
	}


	private static final String TAG = IncreasingSeekBar.class.getSimpleName();

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
			position = Position.fromId(typedArray.getInt(R.styleable.IncreasingSeekBar_isb_position, 0));
		} finally {
			typedArray.recycle();
		}

	}

}
