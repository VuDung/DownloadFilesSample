package asia.covisoft.downloadsample;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by USER on 7/15/2015.
 */
public class DownloadIntentService extends IntentService{
    public static final String KEY_ITEM = "key_item_download";
    Intent broadCastIntent;
    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null || intent.getExtras() == null){
            return;
        }
        broadCastIntent = new Intent();
        broadCastIntent.setAction(DownloadAction.ACTION_DOWNLOAD);
        broadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Bundle bundle = intent.getExtras();
        Item item = (Item)bundle.getSerializable(KEY_ITEM);
        if(item != null) {
            ItemDB.getInstance(this).updateDownloadStatus(Status.DOWNLOADING, item.getId());
            ItemDB.getInstance(this).updateDownloadProgress(0, item.getId());

            Bundle extras = new Bundle();
            extras.putString(DownloadAction.ACTION_STATUS, DownloadAction.ACTION_DOWNLOADING);
            //extras.putInt(DownloadAction.ACTION_ID, item.getId());
            broadCastIntent.putExtras(extras);
            sendBroadcast(broadCastIntent);
            download(item.getId(), item.getVideoUrl(), item.getFilePath());
        }

    }

    private void download(int id, String videoUrl, String fileOutput){
        int count;
        try {
            URL url = new URL(videoUrl);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(fileOutput);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                int percent = (int)((total*100)/lenghtOfFile);
                //ItemDB.getInstance(this).updateDownloadProgress(percent, id);
                Bundle extras = new Bundle();
                extras.putString(DownloadAction.ACTION_STATUS, DownloadAction.ACTION_PROGRESS);
                extras.putInt(DownloadAction.ACTION_PROGRESS, percent);
                extras.putInt(DownloadAction.ACTION_ID, id);
                broadCastIntent.putExtras(extras);
                sendBroadcast(broadCastIntent);
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            ItemDB.getInstance(this).updateDownloadStatus(Status.SUCCESS, id);
            ItemDB.getInstance(this).updateDownloadProgress(100, id);

            Bundle extras = new Bundle();
            extras.putString(DownloadAction.ACTION_STATUS, DownloadAction.ACTION_SUCCESS);
            //extras.putInt(DownloadAction.ACTION_ID, id);
            broadCastIntent.putExtras(extras);
            sendBroadcast(broadCastIntent);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            ItemDB.getInstance(this).updateDownloadStatus(Status.ERROR, id);
            ItemDB.getInstance(this).updateDownloadProgress(0, id);

            Bundle extras = new Bundle();
            extras.putString(DownloadAction.ACTION_STATUS, DownloadAction.ACTION_ERROR);
            //extras.putInt(DownloadAction.ACTION_ID, id);
            broadCastIntent.putExtras(extras);
            sendBroadcast(broadCastIntent);
        }
    }
}
