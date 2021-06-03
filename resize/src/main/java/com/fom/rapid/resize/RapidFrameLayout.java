package com.fom.rapid.resize;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author hardkgosai. created on 28/05/2021.
 */
public class RapidFrameLayout extends FrameLayout {

    public RapidFrameLayout(Context context) {
        super(context);
        new RapidView(context, this, null, 0);
    }

    public RapidFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        new RapidView(context, this, attrs, 0);
    }

    public RapidFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RapidView(context, this, attrs, defStyleAttr);
    }
}
