package com.fom.rapid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created on 28th May 2021.
 *
 * @author hardkgosai.
 * @since 1.0.0
 */
public class RapidLinearLayout extends LinearLayout {

    public RapidLinearLayout(Context context) {
        super(context);
        new RapidView(context, this, null, 0);
    }

    public RapidLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        new RapidView(context, this, attrs, 0);
    }

    public RapidLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RapidView(context, this, attrs, defStyleAttr);
    }
}
