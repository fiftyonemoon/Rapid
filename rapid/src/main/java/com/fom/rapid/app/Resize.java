package com.fom.rapid.app;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;

import com.fom.rapid.assistant.HeyMoon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 28th May 2021.
 * A class to resize hybrid rapid views with applied attributes {@link Attrs}.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.0
 */
public class Resize {

    private View view;

    int w1080 = 1080;
    int h1920 = 1920;

    private int default_width;
    private int default_height;

    private int displayW;
    private int displayH;
    private int measureWith;
    private boolean measureMargin;
    private boolean measurePadding;
    private boolean landscapeMode;

    /**
     * The view which we have to resize.
     */
    public Resize view(View view) {
        this.view = view;
        return this;
    }

    /**
     * Measure view according to width and height.
     *
     * @param measureWith - {@link Attrs}.
     * @since 1.0.3.2 (Added).
     */
    public Resize measureWith(@Attrs int measureWith) {
        this.measureWith = measureWith;
        return this;
    }

    /**
     * Measure applied margin.
     *
     * @param measureMargin - if true applied margin will be measured, false to keep as it is.
     * @since 1.0.3.2 (Added).
     */
    public Resize measureMargin(boolean measureMargin) {
        this.measureMargin = measureMargin;
        return this;
    }

    /**
     * Measure applied padding.
     *
     * @param measurePadding - if true applied padding will be measured, false to keep as it is.
     * @since 1.0.3.2 (Added).
     */
    public Resize measurePadding(boolean measurePadding) {
        this.measurePadding = measurePadding;
        return this;
    }

    /**
     * Set layout orientation.
     *
     * @param landscapeMode - device orientation.
     * @since 1.0.3.2 (Added).
     */
    public Resize landscapeMode(boolean landscapeMode) {
        this.landscapeMode = landscapeMode;
        return this;
    }

    /**
     * Resize conditions.
     *
     * @param measureWith    - {@link Attrs}.
     * @param measureMargin  - if true applied margin will be measured, false to keep as it is.
     * @param measurePadding - if true applied padding will be measured, false to keep as it is.
     * @param landscapeMode  - device orientation.
     * @since 1.0.3.2 (Deprecated).
     * @deprecated - use individual instead.
     */
    public Resize with(@Attrs int measureWith, boolean measureMargin, boolean measurePadding, boolean landscapeMode) {
        this.measureWith = measureWith;
        this.measureMargin = measureMargin;
        this.measurePadding = measurePadding;
        this.landscapeMode = landscapeMode;
        return this;
    }

    /**
     * Start view resizing.
     *
     * @throws RuntimeException - view is missing {@link #view(View)}.
     */
    public void now(Context context) {

        preconditions(); // check null object

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        displayW = displayMetrics.widthPixels;
        displayH = displayMetrics.heightPixels;
        System.out.println(displayW + "-" + displayH);

        default_width = landscapeMode ? h1920 : w1080; // set width according to orientation
        default_height = landscapeMode ? w1080 : h1920; // set height according to orientation

        System.out.println(default_width + "-default-" + default_height);

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

        HeyMoon.log(view, Logs.logs.wh).show(params.width, params.height);
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

        HeyMoon.log(view, Logs.logs.padding).show(paddingStart, paddingTop, paddingEnd, paddingBottom);
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

            HeyMoon.log(view, Logs.logs.margin).show(marginStart, marginTop, marginEnd, marginBottom);
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
    @IntDef({Attrs.none, Attrs.width, Attrs.height})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Attrs {

        /**
         * It will measure width and height of view according device width and height.
         */
        int none = 0;

        /**
         * It will measure width and height of view according device width.
         * Use in portrait mode for best practice.
         */
        int width = -1;

        /**
         * It will measure width and height of view according device height.
         * Use in landscape mode for best practice.
         */
        int height = -2;

    }
}
