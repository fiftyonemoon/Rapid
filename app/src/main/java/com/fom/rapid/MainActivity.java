package com.fom.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

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
    protected void onResume() {
        super.onResume();
        HeyMoon.ui().hideSystemUI(getWindow());
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