package com.fom.rapid.app;

import android.util.Log;
import android.view.View;

import com.fom.rapid.assistant.HeyMoon;

/**
 * Created on 13th June 2021.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.2
 */
public class Logs {

    public static final String TAG = HeyMoon.class.getName();
    private final String s;
    private final logs l;

    public Logs(View v, logs l) {
        this.s = getId(v);
        this.l = l;
    }

    public void show(int i1, int i2) {
        Log.d(TAG, s +
                ": " + l.getS1() + "=" + i1 +
                ", " + l.getS2() + "=" + i2
        );
    }

    public void show(int i1, int i2, int i3, int i4) {
        Log.d(TAG, s +
                ": " + l.getS1() + "=" + i1 +
                ", " + l.getS2() + "=" + i2 +
                ", " + l.getS3() + "=" + i3 +
                ", " + l.getS4() + "=" + i4
        );
    }

    private String getId(View view) {
        if (view.getId() == View.NO_ID) return "no-id";
        else return view.getResources()
                .getResourceName(view.getId())
                .replace("com.fom.rapid:id/", "");
    }

    public enum logs {

        wh("width", "height"),
        padding("pStart", "pTop", "pEnd", "pBottom"),
        margin("mStart", "mTop", "mEnd", "mBottom");

        private final String s1;
        private final String s2;
        private String s3, s4;

        logs(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        logs(String s1, String s2, String s3, String s4) {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
            this.s4 = s4;
        }

        public String getS1() {
            return s1;
        }

        public String getS2() {
            return s2;
        }

        public String getS3() {
            return s3;
        }

        public String getS4() {
            return s4;
        }
    }
}
