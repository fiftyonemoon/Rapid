package com.fom.rapid.app;

import android.content.Context;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created on 12th June 2021.
 *
 * @author hardkgosai.
 * @since 1.0.2
 */
public class UI {

    public void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public String getSize(double size) {
        size = size / 1024.0;

        if (size > 1024.0) {
            return new DecimalFormat("##.##").format(size / 1024.0) + " MB";
        } else return new DecimalFormat("##.##").format(size) + " KB";
    }

}
