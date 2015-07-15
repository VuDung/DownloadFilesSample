package asia.covisoft.downloadsample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 7/14/2015.
 */
public class ItemDB extends SQLiteOpenHelper{

    public static final String DB_NAME = "items_db";
    public static final int VERSION = 7;

    private static final String TABLE_ITEM = "item";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FILE_PATH = "file_path";
    private static final String KEY_VIDEO_URL = "video_url";
    private static final String KEY_DOWNLOAD_STATUS = "download_status";
    private static final String KEY_DOWNLOAD_PROGRESS = "download_progress";

    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            + TABLE_ITEM + "("
            + KEY_ID + " INTEGER primary key, "
            + KEY_NAME + " TEXT,"
            + KEY_FILE_PATH + " TEXT, "
            + KEY_VIDEO_URL + " TEXT, "
            + KEY_DOWNLOAD_STATUS + " INTEGER, "
            + KEY_DOWNLOAD_PROGRESS + " INTEGER);";

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().toString() + "/Demo/";
    private static final String VIDEO_URL = "http://www.ericmoyer.com/episode1.mp4";
    private static final List<Item> DEFAULT_ITEMS;
    static{
        DEFAULT_ITEMS = new ArrayList<>();
        DEFAULT_ITEMS.add(new Item(0, "Item 1", ROOT_PATH + "0.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(1, "Item 2", ROOT_PATH + "1.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(2, "Item 3", ROOT_PATH + "2.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(3, "Item 4", ROOT_PATH + "3.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(4, "Item 5", ROOT_PATH + "4.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(5, "Item 6", ROOT_PATH + "5.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(6, "Item 7", ROOT_PATH + "6.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(7, "Item 8", ROOT_PATH + "7.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(8, "Item 9", ROOT_PATH + "8.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(9, "Item 10", ROOT_PATH + "9.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(10, "Item 11", ROOT_PATH + "10.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
        DEFAULT_ITEMS.add(new Item(11, "Item 12", ROOT_PATH + "11.mp4", VIDEO_URL, Status.WAIT_DOWNLOAD, 0));
    }

    private static ItemDB mInstance = null;
    public static ItemDB getInstance(Context context){
        if(mInstance == null){
            mInstance = new ItemDB(context);
        }
        return mInstance;
    }

    public ItemDB(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
        insertDefaultItems(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(sqLiteDatabase);
    }

    private void insertDefaultItems(SQLiteDatabase db){

        for(int i = 0; i < DEFAULT_ITEMS.size(); i++){
            Item item = DEFAULT_ITEMS.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_ID, item.getId());
            values.put(KEY_NAME, item.getName());
            values.put(KEY_FILE_PATH, item.getFilePath());
            values.put(KEY_VIDEO_URL, item.getVideoUrl());
            values.put(KEY_DOWNLOAD_STATUS, item.getDownloadStatus());
            values.put(KEY_DOWNLOAD_PROGRESS, item.getDownloadProgress());
            long row = db.insert(TABLE_ITEM, null, values);
        }
    }

    public long addItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.getId());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_FILE_PATH, item.getFilePath());
        values.put(KEY_VIDEO_URL, item.getVideoUrl());
        values.put(KEY_DOWNLOAD_STATUS, item.getDownloadStatus());
        values.put(KEY_DOWNLOAD_PROGRESS, item.getDownloadProgress());

        long row = db.insert(TABLE_ITEM, null, values);
        close();
        return row;
    }

    public List<Item> getItems(){
        List<Item> mItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + TABLE_ITEM;
        Cursor cursor = db.rawQuery(sql, null);
        try{
            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                    String filePath = cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH));
                    String videoUrl = cursor.getString(cursor.getColumnIndex(KEY_VIDEO_URL));
                    int downloadStatus = cursor.getInt(cursor.getColumnIndex(KEY_DOWNLOAD_STATUS));
                    int downloadProgress = cursor.getInt(cursor.getColumnIndex(KEY_DOWNLOAD_PROGRESS));
                    mItems.add(new Item(id, name, filePath, videoUrl, downloadStatus, downloadProgress));
                }
            }
        }finally {
            cursor.close();
            close();
        }
        return mItems;
    }

    public int updateDownloadStatus(int status, int itemId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DOWNLOAD_STATUS, status);
        int row  = db.update(TABLE_ITEM, values, KEY_ID + " = ? ", new String[]{String.valueOf(itemId)});
        close();
        return row;

    }

    public int updateDownloadProgress(int progress, int itemId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DOWNLOAD_PROGRESS, progress);
        int row  = db.update(TABLE_ITEM, values, KEY_ID + " = ? ", new String[]{String.valueOf(itemId)});
        close();
        return row;

    }
}
