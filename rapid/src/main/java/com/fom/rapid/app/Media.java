package com.fom.rapid.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.fom.rapid.model.MediaObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 16th July 2021.
 * <p>
 * A class to read media available in the device.
 * Allow to read audio, video and images for now.
 * support SDK 16 to 30.
 *
 * @author <a href="https://github.com/fiftyonemoon/">hardkgosai</a>.
 * @since 1.0.3.2
 */
public class Media {

    private Context context;
    private MediaAction action;
    private MediaObserver observer;

    public static final HashMap<String, ArrayList<MediaObject>> audioMap = new HashMap<>();
    public static final HashMap<String, ArrayList<MediaObject>> videoMap = new HashMap<>();
    public static final HashMap<String, ArrayList<MediaObject>> imagesMap = new HashMap<>();
    public static final ArrayList<MediaObject> audioList = new ArrayList<>();
    public static final ArrayList<MediaObject> selectedAudioList = new ArrayList<>();
    public static final ArrayList<MediaObject> videoList = new ArrayList<>();
    public static final ArrayList<MediaObject> selectedVideoList = new ArrayList<>();
    public static final ArrayList<MediaObject> imagesList = new ArrayList<>();
    public static final ArrayList<MediaObject> selectedImagesList = new ArrayList<>();

    /**
     * Pass context.
     */
    public Media with(Context context) {
        this.context = context;
        return this;
    }

    /**
     * Set {@link MediaAction} according to need.
     */
    public Media action(MediaAction action) {
        this.action = action;
        return this;
    }

    /**
     * Observe retrieving process.
     */
    public void observe(MediaObserver observer) {

        //added: since 1.0.3.5
        preconditions();

        this.observer = observer;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            clear();
            retrieve();

            handler.post(() -> {
                if (observer != null)
                    observer.onComplete();
            });
        });
    }

    /**
     * Check pre-conditions before start.
     *
     * @since 1.0.3.5 (Added).
     */
    void preconditions() {
        if (context == null) {
            throw new NullPointerException("Context should not be null. to fix add 'with()' method.");
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Allow read permission to access media.");
        }
    }

    /**
     * Start retrieving media object using {@link android.content.ContentResolver}.
     */
    private void retrieve() {

        Cursor cursor = context.getContentResolver().query(action.getUri()
                , null
                , null
                , null
                , null);

        cursor.moveToFirst();

        do {

            if (observer != null) {
                observer.onObserving(cursor.getPosition());
            }

            MediaObject object = new MediaObject();
            setCursorCommonObject(cursor, object);

            if (action == MediaAction.audio | action == MediaAction.video) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    setCursorObjectForAndroidXI(cursor, object);
                } else {
                    setMediaRetrieverObject(cursor, object);
                }
            }

            saveObject(object);

            if (observer != null) {
                int progress = cursor.getPosition() * 100 / cursor.getCount();
                observer.onProgress(cursor.getPosition(), progress);
            }

        } while (cursor.moveToNext());

        cursor.close();
    }

    /**
     * Set common local data.
     *
     * @since 1.0.3.4 (Separate column index to check column exist or not) (Removed since 1.0.3.5).
     * @since 1.0.3.5 (check cursor column condition).
     */
    private void setCursorCommonObject(Cursor cursor, MediaObject object) {

        int column_id = cursor.getColumnIndex(BaseColumns._ID);
        int column_bucket_id = cursor.getColumnIndex("bucket_id");
        int column_bucket_name = cursor.getColumnIndex("bucket_display_name");
        int column_uri = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        int column_name = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        int column_mime = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
        int column_size = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
        int column_date = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);

        String id = isColumnIndexValid(cursor, column_id) ? cursor.getString(column_id) : "";
        String bucket_id = isColumnIndexValid(cursor, column_bucket_id) ? cursor.getString(column_bucket_id) : "";
        String bucket_name = isColumnIndexValid(cursor, column_bucket_name) ? cursor.getString(column_bucket_name) : "";
        String uri = isColumnIndexValid(cursor, column_uri) ? cursor.getString(column_uri) : "";
        String name = isColumnIndexValid(cursor, column_name) ? cursor.getString(column_name) : "";
        String mime = isColumnIndexValid(cursor, column_mime) ? cursor.getString(column_mime) : "";
        long size = isColumnIndexValid(cursor, column_size) ? cursor.getLong(column_size) : 0;
        long date = isColumnIndexValid(cursor, column_date) ? cursor.getLong(column_date) : 0;

        object.setId(id);
        object.setBucketId(bucket_id);
        object.setBucketName(bucket_name);
        object.setUri(uri);
        object.setName(name);
        object.setMime(mime);
        object.setSize(size);
        object.setDate(date);

        if (action == MediaAction.audio) {
            object.setArt(getAlbumArt(cursor));
        }
    }

    /**
     * Set data using {@link Cursor} for sdk {@link Build.VERSION_CODES#R} and above.
     *
     * @since 1.0.3.4 (Separate column index to check column exist or not) (Removed since 1.0.3.5).
     * @since 1.0.3.5 (check cursor column condition).
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setCursorObjectForAndroidXI(Cursor cursor, MediaObject object) {

        int column_album = cursor.getColumnIndex(MediaStore.MediaColumns.ALBUM);
        int column_artist = cursor.getColumnIndex(MediaStore.MediaColumns.ARTIST);
        int column_composer = cursor.getColumnIndex(MediaStore.MediaColumns.COMPOSER);
        int column_genre = cursor.getColumnIndex(MediaStore.MediaColumns.GENRE);
        int column_year = cursor.getColumnIndex(MediaStore.MediaColumns.YEAR);
        int column_duration = cursor.getColumnIndex(MediaStore.MediaColumns.DURATION);
        int column_resolution = cursor.getColumnIndex(MediaStore.MediaColumns.RESOLUTION);

        String album = isColumnIndexValid(cursor, column_album) ? cursor.getString(column_album) : "";
        String artist = isColumnIndexValid(cursor, column_artist) ? cursor.getString(column_artist) : "";
        String composer = isColumnIndexValid(cursor, column_composer) ? cursor.getString(column_composer) : "";
        String genre = isColumnIndexValid(cursor, column_genre) ? cursor.getString(column_genre) : "";
        String year = isColumnIndexValid(cursor, column_year) ? cursor.getString(column_year) : "";
        String resolution = isColumnIndexValid(cursor, column_resolution) ? cursor.getString(column_resolution) : "";
        long duration = isColumnIndexValid(cursor, column_duration) ? cursor.getLong(column_duration) : 0;

        object.setAlbum(album);
        object.setArtist(artist);
        object.setComposer(composer);
        object.setGenre(genre);
        object.setYear(year);
        object.setResolution(resolution);
        object.setDuration(duration);
    }

    /**
     * Check and return cursor has column or not.
     *
     * @since 1.0.3.5 (Added).
     */
    private boolean isColumnIndexValid(Cursor cursor, int columnIndex) {
        return columnIndex >= 0 && cursor != null && !cursor.isNull(columnIndex);
    }

    /**
     * set data using {@link MediaMetadataRetriever} for sdk {@link Build.VERSION_CODES#P} and below.
     *
     * @since 1.0.3.4 (Separate column index to check column exist or not).
     * @since 1.0.3.5 (Added resolution width and height individually).
     */
    private void setMediaRetrieverObject(Cursor cursor, MediaObject object) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.parse(object.getUri()));

            object.setAlbum(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            object.setArtist(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            object.setComposer(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
            object.setGenre(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            object.setYear(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
            object.setResolution(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    + "x"
                    + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

            //added: since 1.0.3.5
            object.setResolutionWidth(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            //added: since 1.0.3.5
            object.setResolutionHeight(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                //change: since 1.0.3.4
                int column_duration = cursor.getColumnIndex(MediaStore.MediaColumns.DURATION);
                long duration = column_duration >= 0 ? cursor.getLong(column_duration) : 0;

                object.setDuration(duration);

            } else {

                //change: since 1.0.3.4
                String meta_duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long duration = meta_duration != null ? Long.parseLong(meta_duration) : 0;

                object.setDuration(duration);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (observer != null) {
                observer.onError(cursor.getPosition(), e.getMessage());
            }
        }
    }

    /**
     * Save object.
     */
    private void saveObject(MediaObject object) {

        save(object, action.getMap()); //save into folder wise map

        if (!action.getList().contains(object)) { //save into list
            action.getList().add(object);
        }
    }

    /**
     * Save object in particular folder.
     */
    private void save(MediaObject object, HashMap<String, ArrayList<MediaObject>> map) {

        String key = object.getBucketName();

        if (key == null || key.trim().isEmpty()) return;

        if (!map.containsKey(key)) {

            ArrayList<MediaObject> arrayFileList = new ArrayList<>();
            arrayFileList.add(object);
            map.put(key, arrayFileList);

        } else {

            ArrayList<MediaObject> list = map.get(key);

            if (list != null && !list.contains(object)) {
                list.add(object);
                sort(list); //first sort the list by name and put in the map
                map.put(key, list);
            }
        }
    }

    /**
     * This function is only for {@link MediaAction#audio} action.
     *
     * @return audio album thumbnail.
     * @since 1.0.3.4 (Separate column index to check column exist or not).
     */
    private String getAlbumArt(Cursor cursor) {
        int column_albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        int column_album_art = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

        long albumId = isColumnIndexValid(cursor, column_albumId) ? cursor.getLong(column_albumId) : -1;

        if (albumId == -1) return null;

        Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=" + albumId, null, null);

        String art = cursorAlbum.moveToFirst() && isColumnIndexValid(cursor, column_album_art)
                ? cursorAlbum.getString(column_album_art)
                : null;

        cursorAlbum.close();

        return art;
    }

    /**
     * Sort media list by name.
     */
    private void sort(ArrayList<MediaObject> list) {
        Collections.sort(list, (o1, o2) -> {

            //change: since 1.0.3.5 (added null condition)
            boolean isNull = o1 == null || o1.getName() == null
                    || o2 == null || o2.getName() == null;
            return isNull ? -1 : o1.getName().compareTo(o2.getName());
        });

        //not in use since 1.3.0.5
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.comparing(MediaObject::getName));
        } else {
            Collections.sort(list, (o1, o2) -> {
                return o1.getName().compareTo(o2.getName());
            });
        }*/
    }

    /**
     * Clear list and map.
     */
    private void clear() {
        action.getMap().clear();
        action.getList().clear();
        action.getSelectedList().clear();
    }

    /**
     * Media observer for observing each media object while reading.
     * Make sure use runOnUiThread while working with main UI component on result.
     */
    public interface MediaObserver {
        void onObserving(int position);

        void onProgress(int position, int progress);

        void onError(int position, String message);

        void onComplete();
    }

    /**
     * Media action to perform specific task.
     * each action containing particular uri address, a list which contains all media objects,
     * a selected list which contains selected media objects and a map which contains folder wise media objects.
     */
    public enum MediaAction {
        audio(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioList, selectedAudioList, audioMap),
        video(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoList, selectedVideoList, videoMap),
        images(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imagesList, selectedImagesList, imagesMap);

        private final Uri uri;
        private final ArrayList<MediaObject> list;
        private final ArrayList<MediaObject> selectedList;
        private final HashMap<String, ArrayList<MediaObject>> map;

        MediaAction(Uri uri, ArrayList<MediaObject> list, ArrayList<MediaObject> selectedList, HashMap<String, ArrayList<MediaObject>> map) {
            this.uri = uri;
            this.list = list;
            this.selectedList = selectedList;
            this.map = map;
        }

        /**
         * @return uri address.
         */
        @NonNull
        public Uri getUri() {
            return uri;
        }

        /**
         * @return media list.
         */
        @NonNull
        public ArrayList<MediaObject> getList() {
            return list;
        }

        /**
         * @return selected media list.
         */
        @NonNull
        public ArrayList<MediaObject> getSelectedList() {
            return selectedList;
        }

        /**
         * @return media map.
         */
        @NonNull
        public HashMap<String, ArrayList<MediaObject>> getMap() {
            return map;
        }
    }
}
