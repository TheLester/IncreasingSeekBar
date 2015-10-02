package com.dogar.increasingseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class IncreasingSeekBar extends View {
    private static final String TAG                 = IncreasingSeekBar.class.getSimpleName();
    private static final int    DEFAULT_MAX_RANG    = 100;
    private static final int    DEFAULT_START_COLOR = Color.RED;
    private static final int    DEFAULT_END_COLOR   = Color.GREEN;


    private int      maxRang    = DEFAULT_MAX_RANG;
    private Position position   = Position.HORIZONTAL;
    private int      startColor = DEFAULT_START_COLOR;
    private int      endColor   = DEFAULT_END_COLOR;

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
            maxRang = typedArray.getInt(R.styleable.IncreasingSeekBar_isb_max, DEFAULT_MAX_RANG);
            startColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color_start,DEFAULT_START_COLOR);
            endColor = typedArray.getColor(R.styleable.IncreasingSeekBar_isb_color_end,DEFAULT_END_COLOR);

        } finally {
            typedArray.recycle();
        }

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
        public void onProgressChanged(IncreasingSeekBar seekBar, int value, boolean fromUser);

        public void onStartTrackingTouch(IncreasingSeekBar seekBar);

        public void onStopTrackingTouch(IncreasingSeekBar seekBar);
    }

}
