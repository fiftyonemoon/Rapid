package com.fom.rapid.app;

import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.fom.rapid.assistant.HeyMoon;
import com.fom.rapid.resize.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 26th June 2021.
 * A class to handle file related functions.
 *
 * @author <a href="https://github.com/fiftyonemoon">hardkgosai</a>.
 * @since 1.0.3
 */
public class Files {

    private static final String TAG = HeyMoon.class.getName();

    /**
     * {@link Editor} class constructor.
     */
    public Editor editor() {
        return new Editor();
    }

    /**
     * A class to modified a file.
     */
    public static class Editor {

        private Listener listener;

        /**
         * {@link Editor} listener interface.
         */
        public interface Listener {
            void onComplete(String path);

            void onError(String message);
        }

        /**
         * Set listener to listen process.
         */
        public Editor setListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * File copy.
         * Copy file object to anywhere in external storage.
         *
         * @param input - A file to copy.
         * @param dest  - A destination path where you want to copy.
         */
        public void copy(Context context, File input, String dest) {

            Utils utils = new Utils();

            //final destination path
            String finalDest = dest.isEmpty() ? input.getPath() : dest;

            //final path with unique name
            String finalPath = utils.getUniqueFileName(finalDest);

            Log.d(TAG, "copy: real path: " + input.getPath());
            Log.d(TAG, "copy: final path: " + finalPath);

            //create file object of final path
            File output = new File(finalPath);

            boolean success = utils.writeFile(input, output); //write input data to output

            if (success) { //write success

                //add new output file to media store
                utils.addToMediaStore(context, output);

                if (listener != null) listener.onComplete(finalPath); //complete callback

            } else {
                if (listener != null)
                    listener.onError("Failed to copy"
                            + ", Input file is valid?"
                            + ", Destination is valid?"); //error callback
            }
        }

        /**
         * File delete.
         *
         * @param file - A file to delete.
         * @deprecated - use {{@link #delete(Context, ActivityResultLauncher, Uri)}} instead.
         */
        @Deprecated
        public void delete(Context context, File file) {

            //check file existence
            if (file.exists()) {

                if (file.delete()) { // if successfully deleted

                    int i = HeyMoon.file().utils().removeFromMediaStore(context, file);

                    Log.d(TAG, "delete: " + (i > 0
                            ? "File deleted from media store"
                            : "Failed to delete file from media store"));

                    if (listener != null) listener.onComplete("");

                } else if (listener != null) listener.onError("Failed to delete, File is valid?");

            } else if (listener != null) listener.onError("File not exist");
        }

        /**
         * File delete.
         * Delete file using content resolver.
         *
         * @param uri - A file to delete.
         */
        public void delete(Context context, ActivityResultLauncher<IntentSenderRequest> launcher, Uri uri) {

            ContentResolver contentResolver = context.getContentResolver();

            try {

                contentResolver.delete(uri, null, null);

            } catch (SecurityException e) {

                PendingIntent pendingIntent = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    ArrayList<Uri> collection = new ArrayList<>();
                    collection.add(uri);
                    pendingIntent = MediaStore.createDeleteRequest(contentResolver, collection);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    if (e instanceof RecoverableSecurityException) {
                        RecoverableSecurityException exception = (RecoverableSecurityException) e;
                        pendingIntent = exception.getUserAction().getActionIntent();
                    }
                }

                if (pendingIntent != null) {
                    IntentSender sender = pendingIntent.getIntentSender();
                    IntentSenderRequest request = new IntentSenderRequest.Builder(sender).build();
                    launcher.launch(request);
                }
            }
        }

        /**
         * File duplicate.
         * Duplicate file object in same directory.
         *
         * @param file - A file to duplicate.
         */
        public void duplicate(Context context, File file) {
            copy(context, file, "");
        }

        /**
         * File rename.
         *
         * @param input  - file to rename.
         * @param rename - new file name.
         */
        public void rename(Context context, File input, String rename) {

            Utils utils = HeyMoon.file().utils();

            //get file extension
            String fileExt = utils.getFileExtension(input.getPath());

            //check new name has extension
            boolean isExt = rename.contains(fileExt);

            //temporary path with new name
            String tempPath = input.getPath().replace(input.getName(), rename) + (isExt ? "" : fileExt);

            //final path with unique name
            String finalPath = utils.getUniqueFileName(tempPath);

            Log.d(TAG, "rename: real path: " + input.getPath());
            Log.d(TAG, "rename: temp path: " + tempPath);
            Log.d(TAG, "rename: final path: " + finalPath);

            //create file object of final path
            File output = new File(finalPath);

            if (input.exists()) { //check file existence

                if (input.renameTo(output)) { //if rename success

                    //delete old file from media store
                    utils.removeFromMediaStore(context, input);

                    //add new renamed file to media store
                    utils.addToMediaStore(context, output);

                    if (listener != null) listener.onComplete(finalPath); //complete callback

                } else if (listener != null)
                    listener.onError("Failed to rename, Security manager denies write access. permissions are granted?"); // error callback

            } else if (listener != null) listener.onError("File not exist"); //error callback
        }
    }

    /**
     * {@link Utils} class constructor.
     */
    public Utils utils() {
        return new Utils();
    }

    /**
     * A class of collection of file related functions.
     */
    public static class Utils {

        /**
         * Emails the desired PDF using application of choice by user
         *
         * @param file      - the file to be shared
         * @param authority - path provide authority
         * @param mimetype  - file type
         */
        public void shareFile(Context context, File file, String authority, String mimetype) {
            Uri uri = FileProvider.getUriForFile(context, authority, file);
            ArrayList<Uri> uris = new ArrayList<>();
            uris.add(uri);
            shareFile(context, uris, mimetype);
        }

        /**
         * Share the desired PDFs using application of choice by user
         *
         * @param files     - the list of files to be shared
         * @param authority - path provide authority
         * @param mimetype  - file type
         */
        public void shareMultipleFiles(Context context, List<File> files, String authority, String mimetype) {
            ArrayList<Uri> uris = new ArrayList<>();
            for (File file : files) {
                Uri uri = FileProvider.getUriForFile(context, authority, file);
                uris.add(uri);
            }
            shareFile(context, uris, mimetype);
        }

        /**
         * Emails the desired PDF using application of choice by user
         *
         * @param uris     - list of uris to be shared
         * @param mimetype - file type
         */
        private void shareFile(Context context, ArrayList<Uri> uris, String mimetype) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType(mimetype);
            openIntent(context, Intent.createChooser(intent, context.getString(R.string.choose_app)));
        }

        /**
         * opens a file in appropriate application
         *
         * @param path - path of the file to be opened
         */
        public void openFile(Context context, String path, String authority, String mimetype) {
            if (path == null) {
                HeyMoon.ui().toast(context, context.getString(R.string.null_path));
                return;
            }

            File file = new File(path);
            openFile(context, file, authority, mimetype);
        }

        /**
         * This function is used to open the created file
         * applications on the device.
         *
         * @param file - file to be open
         */
        public void openFile(Context context, File file, String authority, String mimetype) {

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            try {
                Uri uri = FileProvider.getUriForFile(context, authority, file);

                target.setDataAndType(uri, mimetype);
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                openIntent(context, Intent.createChooser(target, context.getString(R.string.choose_app)));
            } catch (Exception e) {
                e.printStackTrace();
                HeyMoon.ui().toast(context, e.getMessage());
            }
        }

        /**
         * Opens the targeted intent (if possible), otherwise show a snackBar
         *
         * @param intent - input intent
         */
        public void openIntent(Context context, Intent intent) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                HeyMoon.ui().toast(context, e.getMessage());
            }
        }

        /**
         * Get unique name of file if original name already exist in directory.
         */
        public String getUniqueFileName(String filepath) {

            File file = new File(filepath);

            String parentPath = file.getParent();
            String extension = getFileExtension(filepath);

            if (!isFileExist(parentPath, file.getName()))
                return filepath;

            File parentFile = file.getParentFile();
            if (parentFile != null) {
                File[] listFiles = parentFile.listFiles();

                if (listFiles != null) {
                    int append = checkRepeat(filepath, Arrays.asList(listFiles), extension);
                    filepath = filepath.replace(extension, "(" + append + ")" + extension);
                }
            }

            return filepath;
        }

        /***
         * Check if file already exists in dir
         * @param filename - Name of the file
         * @return true if file exists else false
         */
        public boolean isFileExist(String parentPath, String filename) {
            String path = parentPath + "/" + filename;
            File file = new File(path);
            return file.exists();
        }

        /**
         * Checks if the new file already exists.
         *
         * @param finalOutputFile Path of pdf file to check
         * @param mFile           Files List of all PDFs
         * @return Number to be added finally in the name to avoid overwrite
         */
        private int checkRepeat(String finalOutputFile, final List<File> mFile, String extension) {
            boolean flag = true;
            int append = 0;
            while (flag) {
                append++;
                String name = finalOutputFile.replace(extension, "(" + append + ")" + extension);

                flag = mFile.contains(new File(name));
            }
            return append;
        }

        /**
         * @return final file output path.
         */
        public String getOutputPath(String parentPath, String filename, String afterFilename, String extension) {

            File file = new File(parentPath);

            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.i(TAG, "getOutputPath: Directory created");
                }
            }

            return parentPath + (filename.contains(extension)
                    ? filename.replace(extension, "_" + afterFilename + extension)
                    : afterFilename.isEmpty() ? filename + extension : filename + "_" + afterFilename + extension);
        }

        /**
         * Replace old file extension with new extension and return output path.
         *
         * @return final file output path.
         */
        public String getOutputPath(String parentPath, String filename, String afterFilename, String oldExtension, String newExtension) {

            File file = new File(parentPath);

            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.i(TAG, "getOutputPath: Directory created");
                }
            }

            return parentPath + (filename.contains(oldExtension)
                    ? filename.replace(oldExtension, "_" + afterFilename + newExtension)
                    : afterFilename.isEmpty() ? filename + newExtension : filename + "_" + afterFilename + newExtension);
        }

        /**
         * @return final file output path without extension.
         */
        public String getOutputPathWithoutExt(String parentPath, String filename, String category, String extension) {

            File file = new File(parentPath);

            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.i(TAG, "getOutputPathWithoutExt: Directory created");
                }
            }

            return parentPath + (filename.contains(extension)
                    ? filename.replace(extension, "_" + category)
                    : category.isEmpty() ? filename : filename + "_" + category);
        }

        /**
         * Extracts file name from the path
         *
         * @param path - file path
         * @return - extracted filename
         */
        public String getFileName(String path) {
            if (path == null)
                return null;

            int index = path.lastIndexOf("/");
            return index == -1
                    ? path
                    : index < path.length()
                    ? path.substring(index + 1)
                    : null;
        }

        /**
         * Extracts file name from the path
         *
         * @param path - file path.
         * @return - extracted filename without extension.
         */
        public String getFileNameWithoutExt(String path) {
            if (path == null) return null;

            String filename = getFileName(path);
            int index = filename.lastIndexOf(".");
            return index == -1
                    ? path
                    : index < filename.length()
                    ? filename.substring(0, index)
                    : null;
        }

        /**
         * Extracts extension from the path
         *
         * @param path - file path.
         * @return - extension of file.
         */
        public String getFileExtension(String path) {
            if (path == null) return null;

            int index = path.lastIndexOf(".");
            return index == -1
                    ? path
                    : index < path.length()
                    ? path.substring(index)
                    : null;
        }

        /**
         * Extracts parent path from the file path without filename.
         *
         * @param path - file path.
         * @return - file parent path.
         * @deprecated - use {@link File#getParent()}
         */
        @Deprecated
        public String getParentPath(String path) {
            if (path == null) return null;

            int index = path.lastIndexOf("/");
            return index == -1
                    ? path
                    : index < path.length()
                    ? path.substring(0, index + 1)
                    : null;
        }

        /**
         * Extracts extension from the path
         *
         * @param path - file path.
         * @return - extension of file.
         */
        public String getFileMimeType(String path) {
            if (path == null) return null;

            String extension = getFileExtension(path);

            if (extension != null) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.replace(".", ""));
            }
            return null;
        }

        /**
         * Convert file size into decimal format.
         * It may return wrong size.
         *
         * @return - file size with written MB or KB.
         * @deprecated - use {@link android.text.format.Formatter#formatFileSize(Context, long)}
         * instead.
         */
        @Deprecated
        public String getFileSizeInDecimalFormat(double size) {
            size = size / 1024.0;

            if (size > 1024.0) {
                return new DecimalFormat("##.##").format(size / 1024.0) + " MB";
            } else return new DecimalFormat("##.##").format(size) + " KB";
        }

        /**
         * @return - time in millis when the file was created.
         * More info:{@link BasicFileAttributes#creationTime()}
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public long getFileCreationTime(File file) throws IOException {
            BasicFileAttributes attr = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.creationTime().toMillis();
        }

        /**
         * @return - time in millis when the last time file was modified.
         * More info:{@link BasicFileAttributes#lastModifiedTime()}
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public long getFileLastModifiedTime(File file) throws IOException {
            BasicFileAttributes attr = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.lastModifiedTime().toMillis();
        }

        /**
         * @return - time in millis when the last time file was accessed.
         * more info:{@link BasicFileAttributes#lastAccessTime()}
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public long getFileLastAccessTime(File file) throws IOException {
            BasicFileAttributes attr = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.lastAccessTime().toMillis();
        }

        /**
         * @return - file size. The size may differ from the actual size on the file system.
         * more info:{@link BasicFileAttributes#size()}
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public long getFileSize(File file) throws IOException {
            BasicFileAttributes attr = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.size();
        }

        /**
         * Store file object into android media store.
         *
         * @param file - to be add.
         */
        private void addToMediaStore(Context context, File file) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);
        }

        /**
         * Remove file object from android media store.
         *
         * @param file - to be delete.
         */
        private int removeFromMediaStore(Context context, File file) {

            //check each media store and delete file from valid media store
            // if i = 1 means file will be deleted, if 0 check next media store.
            int i = deleteFromVideoMediaStore(context, file); //delete file from video media store
            if (i == 0)
                i = deleteFromAudioMediaStore(context, file); //delete file from audio media store
            if (i == 0)
                i = deleteFromImagesMediaStore(context, file); //delete file from images media store
            if (i == 0)
                i = deleteFromFileMediaStore(context, file); //delete file from files media store

            return i;
        }

        /**
         * Delete media from {@link MediaStore.Video}.
         *
         * @param file - to be delete.
         */
        public int deleteFromVideoMediaStore(Context context, File file) {
            ContentResolver resolver = context.getContentResolver();

            return resolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , MediaStore.Video.Media.DATA + "=?"
                    , new String[]{file.getAbsolutePath()});
        }

        /**
         * Delete media from {@link MediaStore.Audio}.
         *
         * @param file - to be delete.
         */
        public int deleteFromAudioMediaStore(Context context, File file) {
            ContentResolver resolver = context.getContentResolver();

            return resolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    , MediaStore.Video.Media.DATA + "=?"
                    , new String[]{file.getAbsolutePath()});
        }

        /**
         * Delete media from {@link MediaStore.Images}.
         *
         * @param file - to be delete.
         */
        public int deleteFromImagesMediaStore(Context context, File file) {
            ContentResolver resolver = context.getContentResolver();

            return resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , MediaStore.Video.Media.DATA + "=?"
                    , new String[]{file.getAbsolutePath()});
        }

        /**
         * Delete media from {@link MediaStore.Files}.
         *
         * @param file - to be delete.
         */
        public int deleteFromFileMediaStore(Context context, File file) {
            ContentResolver resolver = context.getContentResolver();

            return resolver.delete(MediaStore.Files.getContentUri("external")
                    , MediaStore.Video.Media.DATA + "=?"
                    , new String[]{file.getAbsolutePath()});
        }

        /**
         * Write input file data into output file.
         *
         * @param input  - source file.
         * @param output - where data will written.
         */
        public boolean writeFile(File input, File output) {

            try (InputStream in = new FileInputStream(input)) {  //input stream

                OutputStream out = new FileOutputStream(output); //output stream

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len); //write input file data to output file
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
