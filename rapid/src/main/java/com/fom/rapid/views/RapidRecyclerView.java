package com.fom.rapid.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 30th Jan 2022.
 * Hybrid view of {@link RecyclerView}.
 * Currently under coding.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.3.3
 */
public class RapidRecyclerView extends RecyclerView {

    private Context context;

    public RapidRecyclerView(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public RapidRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public RapidRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
    }
}
