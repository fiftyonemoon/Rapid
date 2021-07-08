package com.fom.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.fom.rapid.app.Files;
import com.fom.rapid.assistant.HeyMoon;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rapid_constraint_layout);

        File file = new File(Environment.getExternalStorageDirectory() + "/All About PDF/video1.mp4");
        String dest = Environment.getExternalStorageDirectory() + "/All About PDF/sample.mp4";

        HeyMoon.file().editor().setListener(new Files.Editor.Listener() {
            @Override
            public void onComplete(String path) {
                System.out.println(path);

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                String date = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
                String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                System.out.println(title);
                System.out.println(date);
            }

            @Override
            public void onError(String message) {
                System.out.println(message);
            }
        }).rename(this, file, "video");
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