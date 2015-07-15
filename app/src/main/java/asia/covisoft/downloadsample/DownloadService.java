package asia.covisoft.downloadsample;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

/**
 * Created by USER on 7/14/2015.
 */
public class DownloadService extends Service{

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().toString() + "/Demo/";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        File folder = new File(ROOT_PATH);
        if(folder.exists() && folder.isDirectory()){
            //folder exist
        }else{
            //create folder
            folder.mkdir();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
