package com.fom.rapid.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created on 28th May 2021.
 *
 * @author hardkgosai.
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
