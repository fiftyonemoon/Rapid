package com.fom.rapid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 28th May 2021.
 * Hybrid view of {@link ConstraintLayout}.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.0
 */
public class RapidConstraintLayout extends ConstraintLayout {

    public RapidConstraintLayout(Context context) {
        super(context);
        new RapidView(context, this, null, 0);
    }

    public RapidConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        new RapidView(context, this, attrs, 0);
    }

    public RapidConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RapidView(context, this, attrs, defStyleAttr);
    }
}
