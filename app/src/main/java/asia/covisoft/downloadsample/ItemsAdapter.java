package asia.covisoft.downloadsample;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by USER on 7/14/2015.
 */
public class ItemsAdapter extends RecyclerTypedAdapter<Item, ItemsAdapter.ItemViewHolder>{

    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }
    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener){
        this.mItemLongClickListener = itemLongClickListener;
    }
    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        final Item item = getItem(i);
        itemViewHolder.bindItem(item, i, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @Bind(R.id.llItem) LinearLayout llItem;

        @Bind(R.id.llItemOk) LinearLayout llItemOk;

        @Bind(R.id.llItemDownloadWait) LinearLayout llDownloadWait;

        @Bind(R.id.llItemDownloading) LinearLayout llDownloading;

        @Bind(R.id.llItemDownloadError) LinearLayout llError;

        @Bind(R.id.numberPrgressBar)NumberProgressBar progress;

        @Bind(R.id.btnRetry) Button btnRetry;


        private Item item;
        private int position;
        private ItemClickListener itemClickListener;
        private ItemLongClickListener itemLongClickListener;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llItem.setOnClickListener(this);
            llItem.setOnLongClickListener(this);
            btnRetry.setOnClickListener(this);
        }

        public void bindItem(Item item, int position, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener){
            this.item = item;
            this.position = position;
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;
            progress.setTag("NumberProgressBar" + item.getId());
            File file = new File(item.getFilePath());
//            if(file.exists()){
//                showViewPlay();
//            }else{
                int status = item.getDownloadStatus();
                switch (status){
                    case Status.WAIT_DOWNLOAD:
                        showViewWait();
                        break;
                    case Status.DOWNLOADING:
                        showViewDownloading();
                        break;
                    case Status.ERROR:
                        showViewError();
                        break;
                    case Status.SUCCESS:
                        showViewPlay();
                }
//            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.llItem:
                    if(itemClickListener != null){
                        itemClickListener.onItemClickListener(position, item);
                    }
                    break;
                case R.id.btnRetry:
                    if(Utils.isConnected(view.getContext())) {
                        showViewDownloading();
                        itemClickListener.onItemClickListener(position, item);
                    }else{
                        Toast.makeText(view.getContext(), "Network connection invalid", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

        @Override
        public boolean onLongClick(final View view) {
            if(itemLongClickListener != null){
                itemLongClickListener.onItemLongClickListener(position, item);
            }

            return false;
        }

        private void showViewPlay(){
            llItemOk.setVisibility(View.VISIBLE);
            llDownloading.setVisibility(View.GONE);
            llDownloadWait.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
        }

        private void showViewWait(){
            llItemOk.setVisibility(View.GONE);
            llDownloading.setVisibility(View.GONE);
            llDownloadWait.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
        }

        private void showViewDownloading(){
            llItemOk.setVisibility(View.GONE);
            llDownloading.setVisibility(View.VISIBLE);
            llDownloadWait.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
        }

        private void showViewError(){
            llItemOk.setVisibility(View.GONE);
            llDownloading.setVisibility(View.GONE);
            llDownloadWait.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
        }


    }
}
