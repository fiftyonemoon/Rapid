package com.fom.rapid.app;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.Toast;

/**
 * 12th June 2021.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.2
 */
public class UI {

    public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void toastL(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Hide system status bar.
     */
    public void hideStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getInsetsController().hide(WindowInsets.Type.statusBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /**
     * Show system status bar.
     */
    public void showStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getInsetsController().show(WindowInsets.Type.statusBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(~View.SYSTEM_UI_FLAG_FULLSCREEN & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            window.getDecorView().setSystemUiVisibility(~View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /**
     * Hide system navigation bar.
     */
    public void hideNavigationBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getInsetsController().hide(WindowInsets.Type.navigationBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * Show system navigation bar.
     */
    public void showNavigationBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getInsetsController().show(WindowInsets.Type.navigationBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.getDecorView().setSystemUiVisibility(~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            window.getDecorView().setSystemUiVisibility(~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * Hide all system bars.
     * Includes {@link WindowInsets.Type#statusBars()},{@link WindowInsets.Type#captionBar()}
     * as well as {@link WindowInsets.Type#navigationBars()}, but not {@link WindowInsets.Type#ime()}.
     */
    public void hideSystemUI(Window window) { //pass getWindow();

        //don't use for now
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getInsetsController().hide(WindowInsets.Type.systemBars());
        }*/

        View decorView = window.getDecorView();

        int uiVisibility = decorView.getSystemUiVisibility();

        uiVisibility |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiVisibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE;
            uiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        decorView.setSystemUiVisibility(uiVisibility);

    }

    /**
     * Show all system bars.
     * Includes {@link WindowInsets.Type#statusBars()},{@link WindowInsets.Type#captionBar()}
     * as well as {@link WindowInsets.Type#navigationBars()}, but not {@link WindowInsets.Type#ime()}.
     */
    public void showSystemUI(Window window) { //pass getWindow();

        //don't use for now
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getInsetsController().show(WindowInsets.Type.systemBars());
        }*/

        View decorView = window.getDecorView();

        int uiVisibility = decorView.getSystemUiVisibility();

        uiVisibility &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiVisibility &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiVisibility &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiVisibility &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
            uiVisibility &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        decorView.setSystemUiVisibility(uiVisibility);

    }
}
