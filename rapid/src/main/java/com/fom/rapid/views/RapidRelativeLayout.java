package com.fom.rapid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created on 28th May 2021.
 *
 * @author hardkgosai.
 * @since 1.0.0
 */
public class RapidRelativeLayout extends RelativeLayout {

    public RapidRelativeLayout(Context context) {
        super(context);
        new RapidView(context, this, null, 0);
    }

    public RapidRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        new RapidView(context, this, attrs, 0);
    }

    public RapidRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RapidView(context, this, attrs, defStyleAttr);
    }
}
