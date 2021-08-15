package com.fom.rapid.assistant;

import android.view.View;

import com.fom.rapid.app.Dialogs;
import com.fom.rapid.app.Directory;
import com.fom.rapid.app.Logs;
import com.fom.rapid.app.Media;
import com.fom.rapid.app.Resize;
import com.fom.rapid.app.UI;
import com.fom.rapid.app.Files;

/**
 * 28th May 2021.
 * A virtual assistant of rapid library.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.0
 */
public class HeyMoon {

    /**
     * {@link Resize} class constructor.
     * A class that resize layout views.
     *
     * @since 1.0.0
     */
    public static Resize resize() {
        return new Resize();
    }

    /**
     * {@link UI} class constructor.
     * A class that handle ui views.
     *
     * @since 1.0.2
     */
    public static UI ui() {
        return new UI();
    }

    /**
     * {@link Dialogs} class constructor.
     * A class to show alert & progress dialog.
     *
     * @since 1.0.2
     */
    public static Dialogs dialogs() {
        return new Dialogs();
    }

    /**
     * {@link Logs} class constructor.
     * A class that showing log of every resized view.
     *
     * @since 1.0.2
     */
    public static Logs log(View v, Logs.logs l) {
        return new Logs(v, l);
    }

    /**
     * {@link Files} class constructor.
     * A util class for files.
     *
     * @since 1.0.3
     */
    public static Files file() {
        return new Files();
    }

    /**
     * {@link Directory} class constructor.
     * A class to create directory as well as file in android specified directory.
     *
     * @since 1.0.3.2
     */
    public static Directory directory() {
        return new Directory();
    }

    /**
     * {@link Media} class constructor.
     * A class to read all media objects from device.
     *
     * @since 1.0.3.2
     */
    public static Media media() {
        return new Media();
    }
}
