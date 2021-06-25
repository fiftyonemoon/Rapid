package com.fom.rapid.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.fom.rapid.resize.R;
import com.fom.rapid.assistant.HeyMoon;
import com.fom.rapid.app.Resize;

/**
 * Created on 28th May 2021.
 *
 * @author hardkgosai.
 * @since 1.0.0
 */
public class RapidView implements ViewTreeObserver.OnGlobalLayoutListener {

    private final Context context;
    private final View view;
    private int measureWith;
    private boolean measurePadding;
    private boolean measureMargin;
    private boolean withChildren;
    private boolean landscapeMode;

    public RapidView(Context context, View view, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        this.view = view;
        initialize(attrs, defStyleAttr);
    }

    /**
     * Initialize {@link RapidView}.
     */
    private void initialize(AttributeSet attrs, int defStyle) {

        if (attrs != null) {

            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RapidView, defStyle, 0);

            measureWith = array.getInt(R.styleable.RapidView_measureWith, Resize.Attrs.none);
            measureMargin = array.getBoolean(R.styleable.RapidView_measureMargin, true);
            measurePadding = array.getBoolean(R.styleable.RapidView_measurePadding, true);
            withChildren = array.getBoolean(R.styleable.RapidView_resizeChildren, false);
            landscapeMode = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

            array.recycle();
        }

        //add this view on global layout listener
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    /**
     * {@link RapidView} on successfully populated.
     */
    @Override
    public void onGlobalLayout() {

        // once view is populated remove from global layout listener
        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        resize(view); // resize parent view first

        if (withChildren) {
            resizeChildren(view); // resize parent view children
        }

    }

    /**
     * Resize all {@link RapidView} child.
     */
    private void resizeChildren(View parent) {

        for (int index = 0; index < ((ViewGroup) parent).getChildCount(); index++) {

            View view = ((ViewGroup) parent).getChildAt(index);

            if (isValid(view)) { //check validation of the view
                resize(view);
            }

            if (view instanceof ViewGroup) {
                resizeChildren(view); // check view contains any child
            }
        }
    }

    /**
     * Request assistant {@link HeyMoon} to resize view.
     *
     * @param view which we have to resize.
     */
    private void resize(View view) {
        HeyMoon.resize()
                .view(view)
                .with(measureWith, measureMargin, measurePadding, landscapeMode)
                .now(context);
    }

    /**
     * Check validation before resize the view.
     * Note: validation are only for the children view.
     */
    private boolean isValid(View view) {
        return !(view instanceof RapidImageView |
                view instanceof RapidRelativeLayout |
                view instanceof RapidLinearLayout |
                view instanceof RapidConstraintLayout |
                view instanceof RapidFrameLayout);
    }

}
