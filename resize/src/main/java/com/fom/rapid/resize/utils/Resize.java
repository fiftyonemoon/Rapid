package com.fom.rapid.resize.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.fom.rapid.resize.assistant.HeyMoon;

public class Resize {

    public static int height, width;
    public static int SCALE_WIDTH = 1080; // scale width of ui
    public static int SCALE_HEIGHT = 1920; // scale height of ui

    public static void init(Context context) {
        getHeight(context);
        getWidth(context);
    }

    public static int getWidth(Context context) {
        width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static int getHeight(Context context) {
        height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static void getHeight(Context mContext, View view, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        view.getLayoutParams().height = dm.heightPixels * v_height / SCALE_HEIGHT;
    }

    public static void getWidth(Context mContext, View view, int v_Width) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        view.getLayoutParams().width = dm.widthPixels * v_Width / SCALE_WIDTH;
    }

    public static int getHeight(int h) {
        return (height * h) / 1920;
    }

    public static int getWidth(int w) {
        return (width * w) / 1080;
    }

    public static void setSize(View view, int width, int height) {
        view.getLayoutParams().height = getHeight(height);
        view.getLayoutParams().width = getWidth(width);
    }

    public static void setSize(View view, int width, int height, boolean sameHW) {

        if (sameHW) {
            view.getLayoutParams().height = getWidth(height);
            view.getLayoutParams().width = getWidth(width);
        } else {
            view.getLayoutParams().height = getHeight(height);
            view.getLayoutParams().width = getHeight(width);
        }
    }

    public static void setHeightByWidth(Context mContext, View view, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        view.getLayoutParams().height = dm.widthPixels * v_height / SCALE_WIDTH;
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMargins(getWidth(left), getHeight(top), getWidth(right), getHeight(bottom));
    }

    public static void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    public static void setHeightWidth(Context mContext, View view, int v_width, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels * v_width / SCALE_WIDTH;
        int height = dm.heightPixels * v_height / SCALE_HEIGHT;
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
    }

    public static void setHeightWidthAsWidth(Context mContext, View view, int v_width, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels * v_width / SCALE_WIDTH;
        int height = dm.widthPixels * v_height / SCALE_WIDTH;
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
    }

    public static void setMargins(Context mContext, View view, int m_left, int m_top, int m_right, int m_bottom) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int left = dm.widthPixels * m_left / SCALE_WIDTH;
        int top = dm.heightPixels * m_top / SCALE_HEIGHT;
        int right = dm.widthPixels * m_right / SCALE_WIDTH;
        int bottom = dm.heightPixels * m_bottom / SCALE_HEIGHT;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void setMarginLeft(Context mContext, View view, int m_left) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int left = dm.widthPixels * m_left / SCALE_WIDTH;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, 0, 0, 0);
            view.requestLayout();
        }
    }

    public static void setPadding(Context mContext, View view, int p_left, int p_top, int p_right, int p_bottom) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int left = dm.widthPixels * p_left / SCALE_WIDTH;
        int top = dm.heightPixels * p_top / SCALE_HEIGHT;
        int right = dm.widthPixels * p_right / SCALE_WIDTH;
        int bottom = dm.heightPixels * p_bottom / SCALE_HEIGHT;
        view.setPadding(left, top, right, bottom);
    }

    public static void setHeightWidth2(Context mContext, View view, int v_width, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels * v_width / SCALE_WIDTH;
        int height = dm.widthPixels * v_height / SCALE_WIDTH;
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
    }

    public static int getDimens(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }
}
