package com.fom.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.fom.rapid.app.Dialogs;
import com.fom.rapid.app.Directory;
import com.fom.rapid.app.Files;
import com.fom.rapid.app.Resize;
import com.fom.rapid.assistant.HeyMoon;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    final boolean goBack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rapid_constraint_layout);

        if (goBack) return;

        useOfResizeProgrammatically(); //resize programmatically example
        useOfDirectory(); //directory class example
        useOfFiles(); //files class example
        onBackPressed(); //dialogs class example
        onResume(); //ui class example
    }

    /**
     * Example use of {@link Resize} class programmatically.
     */
    private void useOfResizeProgrammatically() {
        HeyMoon.resize()
                .view(findViewById(R.id.ivMenu)) //pass the view which have to resize
                .measureWith(Resize.Attrs.width) //measure view with width or height, by default none
                .measureMargin(false) //true to measure margin, false to keep as it is, by default false
                .measurePadding(false) //true to measure padding, false to keep as it is, by default false
                .landscapeMode(false) //apply as per orientation, by default false
                .now(this);
    }

    /**
     * Example use of {@link Files} class.
     */
    private void useOfFiles() {

        /* ----------------- Use of File Editor ------------------- */

        File file = new File(Environment.getExternalStorageDirectory() + "/ExampleDirectory/example.jpg");
        String destination = Environment.getExternalStorageDirectory() + "/Destination/example.jpg";

        HeyMoon.file()
                .editor() //contain editing related functions for the file
                .setListener(new Files.Editor.Listener() { //add listener for callback on task completed
                    @Override
                    public void onComplete(String path) {

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(path);
                        String date = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
                        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

                    }

                    @Override
                    public void onError(String message) {
                        System.out.println(message);
                    }

                }).duplicate(this, file); //use to duplicate the file

        //.copy(this, file, destination); use to copy the file
        //.delete(this, file); use to delete the file
        //.rename(this, file, "rename.jpg") use to rename the file

        /* ----------------- Use of File Utils --------------------- */

        HeyMoon.file()
                .utils() // contain file related functions
                .shareFile(this, file, "Authority", "image/jpg"); //use to share the file

        //.openFile(this, file, "Authority", "image/jpg"); use to open the file
    }

    /**
     * Example use of {@link com.fom.rapid.app.Directory} class.
     */
    private void useOfDirectory() {

        //for multi purpose - defined globally
        Directory directory = HeyMoon.directory().with(this).type(Directory.DirectoryType.Images);//single time initialise

        boolean success = directory.createDirectory("Your directory path");
        Uri uri = directory.createFile("Example.jpg"); //create file
        Uri uri1 = directory.createFile("ExampleDirectory", "Example.jpg"); //create file with directory

        //use directly for single task
        HeyMoon.directory()
                .with(this)
                .type(Directory.DirectoryType.Images) //pass media type
                .createFile("example.jpg"); //this will create file in pictures folder
    }

    /**
     * Show runtime {@link com.fom.rapid.app.Dialogs.Alert} dialog example.
     * Types of dialog: {@link Dialogs.Alert#delete()}, {@link Dialogs.Alert#exit()}, {@link Dialogs.Alert#save()}.
     */
    private void showAlertDialog() {
        HeyMoon.dialogs().alert().delete().cancelable(true).listener((dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                showProgressDialog();
            }
        }).show(this);
    }

    /**
     * Show runtime {@link com.fom.rapid.app.Dialogs.Progress} dialog example
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

    /**
     * On back pressed show dialog example.
     */
    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    /**
     * Example of {@link com.fom.rapid.app.UI} class.
     * Hide navigation and status/notification bar for better result.
     */
    @Override
    protected void onResume() {
        super.onResume();
        HeyMoon.ui().hideSystemUI(getWindow()); //hide system bars
        //HeyMoon.ui().showSystemUI(getWindow()); //show system bars
    }
}