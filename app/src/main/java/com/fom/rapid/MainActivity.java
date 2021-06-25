package com.fom.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.fom.rapid.assistant.HeyMoon;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rapid_constraint_layout);
    }

    /**
     * Hide navigation and status/notification bar for better result.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * On back pressed show dialog example.
     */
    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    /**
     * Alert dialog example.
     */
    private void showAlertDialog() {
        HeyMoon.dialogs().alert().delete().cancelable(true).listener((dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                showProgressDialog();
            }
        }).show(this);
    }

    /**
     * Progress dialog example.
     */
    private void showProgressDialog() {
        HeyMoon.dialogs().progress().show(this);

        //dismiss and finish activity after 2 sec
        new Handler(Looper.myLooper()).postDelayed(() -> {
            HeyMoon.dialogs().progress().dismiss();
            finish();
        }, 2000);
    }
}