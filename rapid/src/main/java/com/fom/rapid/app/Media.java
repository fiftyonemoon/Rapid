package com.fom.rapid.app;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

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

            if (observer != null) observer.onObserving(cursor.getPosition());

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
     * set common local data.
     */
    private void setCursorCommonObject(Cursor cursor, MediaObject object) {
        object.setId(cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
        object.setBucketId(cursor.getString(cursor.getColumnIndexOrThrow("bucket_id")));
        object.setBucketName(cursor.getString(cursor.getColumnIndexOrThrow("bucket_display_name")));
        object.setUri(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
        object.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
        object.setMime(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));
        object.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)));
        object.setDate(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)));

        if (action == MediaAction.audio)
            object.setArt(getAlbumArt(cursor));
    }

    /**
     * set data using {@link Cursor} for sdk {@link Build.VERSION_CODES#R} and above.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setCursorObjectForAndroidXI(Cursor cursor, MediaObject object) {
        object.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.ALBUM)));
        object.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.ARTIST)));
        object.setComposer(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.COMPOSER)));
        object.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.GENRE)));
        object.setYear(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.YEAR)));
        object.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)));
        object.setResolution(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RESOLUTION)));
    }

    /**
     * set data using {@link MediaMetadataRetriever} for sdk {@link Build.VERSION_CODES#P} and below.
     */
    private void setMediaRetrieverObject(Cursor cursor, MediaObject object) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.parse(object.getUri()));

            object.setAlbum(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            object.setArtist(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            object.setComposer(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            object.setGenre(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
            object.setYear(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
            object.setResolution(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    + "x"
                    + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                object.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)));
            } else
                object.setDuration(Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));

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
     */
    private String getAlbumArt(Cursor cursor) {
        long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

        Cursor cursorAlbum = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=" + albumId, null, null);

        String art = cursorAlbum.moveToFirst()
                ? cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                : null;

        cursorAlbum.close();

        return art;
    }

    /**
     * Sort media list by name.
     */
    private void sort(ArrayList<MediaObject> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        } else {
            Collections.sort(list, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
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
        public Uri getUri() {
            return uri;
        }

        /**
         * @return media list.
         */
        public ArrayList<MediaObject> getList() {
            return list;
        }

        /**
         * @return selected media list.
         */
        public ArrayList<MediaObject> getSelectedList() {
            return selectedList;
        }

        /**
         * @return media map.
         */
        public HashMap<String, ArrayList<MediaObject>> getMap() {
            return map;
        }
    }
}
