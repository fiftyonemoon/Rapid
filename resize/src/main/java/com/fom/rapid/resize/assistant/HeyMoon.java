package com.fom.rapid.resize.assistant;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hardkgosai. created on 27/05/2021.
 * <p>
 * A virtual assistant of rapid library.
 */
public class HeyMoon<assistant> {

    private View view;

    final int default_width = 1080;
    final int default_height = 1920;

    private int displayW;
    private int displayH;
    private int measureWith;
    private boolean measureMargin;
    private boolean measurePadding;

    /**
     * Resize helper class.
     */
    public static class Resize extends HeyMoon<Resize> {

    }

    /**
     * Helper constructor.
     */
    public static Resize resize() {
        return new Resize();
    }

    /**
     * The view which we have to resize.
     *
     * @throws RuntimeException if view is missing on call {@link #now(Context)} method.
     */
    public HeyMoon<assistant> view(View view) {
        this.view = view;
        return this;
    }

    /**
     * Resize conditions.
     */
    public HeyMoon<assistant> with(int measureWith, boolean measureMargin, boolean measurePadding) {
        this.measureWith = measureWith;
        this.measureMargin = measureMargin;
        this.measurePadding = measurePadding;
        return this;
    }

    /**
     * Start view resizing.
     */
    public void now(Context context) {

        preconditions(); // check null object

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        displayW = displayMetrics.widthPixels;
        displayH = displayMetrics.heightPixels;

        ViewGroup.LayoutParams params = view.getLayoutParams();

        updateWidthHeight(params); // update width height of the view
        updatePadding(); // update padding of the view
        updateMargin(params); // update margin of the view
        view.setLayoutParams(params); // set new params in the view
    }

    /**
     * Update width and height of the view as per device width and height.
     */
    private void updateWidthHeight(ViewGroup.LayoutParams params) {
        int width = params.width;
        int height = params.height;

        boolean withWidth = measureWith == Attrs.width;
        boolean withHeight = measureWith == Attrs.height;

        if (width > 0)
            params.width = (withHeight ? displayH : displayW) * width / (withHeight ? default_height : default_width);
        if (height > 0)
            params.height = (withWidth ? displayW : displayH) * height / (withWidth ? default_width : default_height);

    }

    /**
     * Update padding of the view as per device width and height.
     */
    private void updatePadding() {
        int top = view.getPaddingTop();
        int bottom = view.getPaddingBottom();
        int start = getPaddingStart();
        int end = getPaddingEnd();

        int paddingTop = measurePadding ? displayH * top / default_height : top;
        int paddingBottom = measurePadding ? displayH * bottom / default_height : bottom;
        int paddingStart = measurePadding ? displayW * start / default_width : start;
        int paddingEnd = measurePadding ? displayW * end / default_width : end;

        view.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom);
    }

    /**
     * Update margin of the view as per device width and height.
     */
    private void updateMargin(ViewGroup.LayoutParams params) {

        if (params instanceof ViewGroup.MarginLayoutParams) {

            int top = ((ViewGroup.MarginLayoutParams) params).topMargin;
            int bottom = ((ViewGroup.MarginLayoutParams) params).bottomMargin;
            int start = getMarginStart(params);
            int end = getMarginEnd(params);

            int marginTop = measureMargin ? displayH * top / default_height : top;
            int marginBottom = measureMargin ? displayH * bottom / default_height : bottom;
            int marginStart = measureMargin ? displayW * start / default_width : start;
            int marginEnd = measureMargin ? displayW * end / default_width : end;

            ((ViewGroup.MarginLayoutParams) params).setMargins(marginStart, marginTop, marginEnd, marginBottom);
        }
    }

    /**
     * Get start margin according to SDK {@link android.os.Build}.
     */
    private int getMarginStart(ViewGroup.LayoutParams params) {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ?
                ((ViewGroup.MarginLayoutParams) params).getMarginStart() :
                ((ViewGroup.MarginLayoutParams) params).leftMargin;
    }

    /**
     * Get end margin according to SDK {@link android.os.Build}.
     */
    private int getMarginEnd(ViewGroup.LayoutParams params) {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ?
                ((ViewGroup.MarginLayoutParams) params).getMarginEnd() :
                ((ViewGroup.MarginLayoutParams) params).rightMargin;
    }

    /**
     * Get start padding according to SDK {@link android.os.Build}.
     */
    private int getPaddingStart() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ?
                view.getPaddingStart() : view.getPaddingLeft();
    }

    /**
     * Get end padding according to SDK {@link android.os.Build}.
     */
    private int getPaddingEnd() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ?
                view.getPaddingEnd() : view.getPaddingRight();
    }

    /**
     * Check preconditions before resize the view.
     */
    private void preconditions() {
        if (view == null) {
            throw new RuntimeException("View is missing. To fix add 'view()' method");
        }
    }

    /**
     * Attributes class.
     */
    public static class Attrs {

        /**
         * It will measure width and height of view according device width and height.
         */
        public static final int none = 0;

        /**
         * It will measure width and height of view according device width.
         */
        public static final int width = -1;
        /**
         * It will measure width and height of view according device height.
         */
        public static final int height = -2;
    }
}
