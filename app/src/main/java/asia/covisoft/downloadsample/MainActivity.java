package asia.covisoft.downloadsample;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements ItemClickListener, ItemLongClickListener{

    @Bind(R.id.rvItems)
    RecyclerView rvItems;

    private ItemsAdapter adapter;
    DownloadReceiver receiver;
    List<Item> mItems;

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);




    }

    @Override
    protected void onResume() {
        super.onResume();


        final GridLayoutManager manager = new GridLayoutManager(this, 2);
        adapter = new ItemsAdapter();
        adapter.setItemClickListener(this);
        adapter.setItemLongClickListener(this);
        rvItems.setLayoutManager(manager);
        rvItems.setAdapter(adapter);

        mItems = ItemDB.getInstance(this).getItems();
        adapter.changeDataSet(mItems);

        receiver = new DownloadReceiver();
        IntentFilter filter = new IntentFilter(DownloadAction.ACTION_DOWNLOAD);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onItemLongClickListener(int position, final Item item) {
        String filePath = item.getFilePath();
        final File file = new File(filePath);

        if(file.exists()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Delete");
            builder.setMessage("Do you want to delete?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean deleted = file.delete();
                    item.setDownloadStatus(Status.WAIT_DOWNLOAD);
                    ItemDB.getInstance(MainActivity.this).updateDownloadStatus(Status.WAIT_DOWNLOAD, item.getId());
                    adapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    @Override
    public void onItemClickListener(int position, Item item) {
        int status = item.getDownloadStatus();
        switch (status){
            case Status.DOWNLOADING:

                Log.i(TAG, "Click Downloading");
//                Intent i0 = new Intent(MainActivity.this, DownloadIntentService.class);
//                Bundle bundle0 = new Bundle();
//                bundle0.putSerializable(DownloadIntentService.KEY_ITEM, item);
//                i0.putExtras(bundle0);
//                startService(i0);
                break;
            case Status.SUCCESS:
                Log.i(TAG, "Click Success");
                Intent iVideo = new Intent(MainActivity.this, VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(VideoActivity.KEY_ITEM_VIDEO, item);
                iVideo.putExtras(bundle);
                startActivity(iVideo);
                break;
            case Status.WAIT_DOWNLOAD:

                Log.i(TAG, "Click Download");
                if(Utils.isConnected(this)) {
                    item.setDownloadStatus(Status.DOWNLOADING);
                    Intent i = new Intent(MainActivity.this, DownloadIntentService.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable(DownloadIntentService.KEY_ITEM, item);
                    i.putExtras(bundle1);
                    startService(i);
                }else{
                    showNetworkError();
                }
                break;
            case Status.ERROR:
                if(Utils.isConnected(this)) {
                    item.setDownloadStatus(Status.DOWNLOADING);
                    Log.i(TAG, "Click Error");
                    Intent i2 = new Intent(MainActivity.this, DownloadIntentService.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable(DownloadIntentService.KEY_ITEM, item);
                    i2.putExtras(bundle2);
                    startService(i2);
                }else{
                    showNetworkError();
                }
                break;
            default:
                break;
        }
    }

    public class DownloadReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String actionStatus = extras.getString(DownloadAction.ACTION_STATUS);
            int id;
            switch (actionStatus){
                case DownloadAction.ACTION_DOWNLOADING:
                    Log.i(TAG, "Receiver Status Downloading");
                    //id = extras.getInt(DownloadAction.ACTION_ID);
                    mItems = ItemDB.getInstance(MainActivity.this).getItems();
                    adapter.changeDataSet(mItems);
                    adapter.notifyDataSetChanged();


                    break;
                case DownloadAction.ACTION_ERROR:
                    Log.i(TAG, "Receiver Status Error");
                    //id = extras.getInt(DownloadAction.ACTION_ID);
                    mItems = ItemDB.getInstance(MainActivity.this).getItems();
                    adapter.changeDataSet(mItems);
                    adapter.notifyDataSetChanged();


                    break;
                case DownloadAction.ACTION_PROGRESS:
                    Log.i(TAG, "Receiver Status Progress");
                    id = extras.getInt(DownloadAction.ACTION_ID);
                    int progress = extras.getInt(DownloadAction.ACTION_PROGRESS);
                    NumberProgressBar numberProgressBar = (NumberProgressBar)rvItems.findViewWithTag("NumberProgressBar" + id);
                    if(numberProgressBar != null){
                        numberProgressBar.setProgress(progress);
                    }
                    break;
                case DownloadAction.ACTION_SUCCESS:
                    Log.i(TAG, "Receiver Status Success");
                    //id = extras.getInt(DownloadAction.ACTION_ID);
                    mItems = ItemDB.getInstance(MainActivity.this).getItems();
                    adapter.changeDataSet(mItems);
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    }

    private void showNetworkError(){
        Toast.makeText(this, "Network connection invalid", Toast.LENGTH_SHORT).show();
    }
}
