package asia.covisoft.downloadsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by USER on 7/15/2015.
 */
public class VideoActivity extends ActionBarActivity{
    public static final String KEY_ITEM_VIDEO = "key_item_video";
    private Item mItem;
    @Bind(R.id.vvItem)
    VideoView vvItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mItem = (Item)bundle.getSerializable(KEY_ITEM_VIDEO);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaController mediaController = new MediaController(this);
        vvItem.setMediaController(mediaController);
        vvItem.setVideoPath(mItem.getFilePath());
        vvItem.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vvItem.stopPlayback();
    }
}
