package com.fom.rapid.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.fom.rapid.resize.R;

/**
 * Created on 12th June 2021.
 * Purpose of this class is less coding, save time and work anywhere in the app.
 *
 * @author hardkgosai.
 * @since 1.0.2
 */
public class Dialogs {

    private static Alert alertInstance;
    private static Progress progressInstance;

    /**
     * {@link Alert} class constructor.
     */
    public Alert alert() {
        return alertInstance == null ? alertInstance = new Alert() : alertInstance;
    }

    /**
     * A class to show types of alert dialog.
     */
    public static class Alert {

        public Dialog<Object> delete() {
            return new Dialog<>(types.delete);
        }

        public Dialog<Object> exit() {
            return new Dialog<>(types.exit);
        }

        public Dialog<Object> save() {
            return new Dialog<>(types.save);
        }

        public static class Dialog<dialog> {

            private AlertDialog alertDialog;
            private DialogInterface.OnClickListener listener;
            private final types types;
            private boolean cancelable;

            public Dialog(types types) {
                this.types = types;
            }

            public Dialog<dialog> cancelable(boolean cancelable) {
                this.cancelable = cancelable;
                return this;
            }

            public Dialog<dialog> listener(DialogInterface.OnClickListener listener) {
                this.listener = listener ;
                return this;
            }

            public void show(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(cancelable);
                builder.setTitle(types.getTitle());
                builder.setMessage(types.getMessage());
                builder.setPositiveButton(types.getTitle(), listener);
                builder.setNegativeButton(types.getCancel(), listener);
                alertDialog = builder.create();
                alertDialog.show();
            }

            public void dismiss() {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    alertDialog = null;
                    alertInstance = null;
                }
            }
        }
    }

    /**
     * Enum class for types of alert dialog.
     */
    public enum types {

        delete(R.string.delete, R.string.are_you_sure_delete),
        exit(R.string.exit, R.string.are_you_sure_exit),
        save(R.string.save, R.string.are_you_sure_save);

        private final int title;
        private final int message;

        types(int title, int message) {
            this.title = title;
            this.message = message;
        }

        public int getTitle() {
            return title;
        }

        public int getMessage() {
            return message;
        }

        public int getCancel() {
            return R.string.cancel;
        }
    }

    /**
     * {@link Progress} class constructor.
     */
    public Progress progress() {
        return progressInstance == null ? progressInstance = new Progress() : progressInstance;
    }

    /**
     * A class to show progress dialog in runtime.
     */
    public static class Progress {

        private ProgressDialog progressDialog;

        public void show(Context context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.please_wait));
            progressDialog.show();
        }

        public void dismiss() {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
                progressInstance = null;
            }
        }
    }
}
