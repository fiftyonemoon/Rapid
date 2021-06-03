package com.fom.rapid.resize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.fom.rapid.resize.assistant.HeyMoon;

/**
 * @author hardkgosai. created on 28/05/2021.
 */
public class RapidImageView extends androidx.appcompat.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener {

    private int measureWith;
    private boolean measureMargin;
    private boolean measurePadding;

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

            @SuppressLint("CustomViewStyleable")
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RapidImageView, defStyle, 0);

            measureWith = array.getInt(R.styleable.RapidImageView_measureWith, 0);
            measureMargin = array.getBoolean(R.styleable.RapidImageView_measureMargin, true);
            measurePadding = array.getBoolean(R.styleable.RapidImageView_measurePadding, true);

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
     * Request assistant {@link HeyMoon} to resize all child.
     *
     * @param view which we have to resize.
     */
    private void resize(View view) {
        HeyMoon.resize()
                .view(view)
                .with(measureWith, measureMargin, measurePadding)
                .now(getContext());
    }

}