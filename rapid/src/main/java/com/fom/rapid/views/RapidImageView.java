package com.fom.rapid.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.fom.rapid.app.Resize;
import com.fom.rapid.resize.R;
import com.fom.rapid.assistant.HeyMoon;

/**
 * 28th May 2021.
 * Hybrid view of {@link android.widget.ImageView}.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.0
 */
public class RapidImageView extends androidx.appcompat.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener {

    private int measureWith;
    private boolean measureMargin;
    private boolean measurePadding;
    private boolean landscapeMode;

    public RapidImageView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public RapidImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public RapidImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    /**
     * Initialize {@link RapidImageView}.
     */
    private void initialize(Context context, AttributeSet attrs, int defStyle) {

        if (attrs != null) {

            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RapidImageView, defStyle, 0);

            measureWith = array.getInt(R.styleable.RapidImageView_measureWith, Resize.Attrs.none);
            measureMargin = array.getBoolean(R.styleable.RapidImageView_measureMargin, false);
            measurePadding = array.getBoolean(R.styleable.RapidImageView_measurePadding, false);
            landscapeMode = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

            //add this view on global layout listener
            getViewTreeObserver().addOnGlobalLayoutListener(this);

            array.recycle();
        }
    }

    /**
     * {@link RapidImageView} on successfully populated.
     */
    @Override
    public void onGlobalLayout() {

        // once view is populated remove from global layout listener
        getViewTreeObserver().removeOnGlobalLayoutListener(this);

        resize(this); // resize view
    }

    /**
     * Request assistant {@link HeyMoon} to resize view.
     *
     * @param view which we have to resize.
     */
    private void resize(View view) {
        HeyMoon.resize()
                .view(view)
                .measureWith(measureWith)
                .measureMargin(measureMargin)
                .measurePadding(measurePadding)
                .landscapeMode(landscapeMode)
                .now(getContext());
    }

}