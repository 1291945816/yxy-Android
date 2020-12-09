package kilig.ink.yxy.source;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.InalbumPicture;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class InAlbumAdapter extends RecyclerView.Adapter<InAlbumAdapter.ViewHolder> {
    private Context context;
    private List<InalbumPicture> dataList;
    private Listener listener;

    @NonNull
    @Override
    public InAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InAlbumAdapter.ViewHolder holder, int position) {

        InalbumPicture picture = dataList.get(position);
        if (!picture.isPublish())
            holder.publishView.setImageResource(R.drawable.album_star_white);
        else
            holder.publishView.setImageResource(R.drawable.album_star_red);
        Glide.with(context)
                .load(picture.getThumbnailUrl())
                .skipMemoryCache(true)
                .into(holder.imageView);
        holder.detail.setOnClickListener(v->{
            PopupMenu popup = new PopupMenu(context,holder.detail);
            popup.getMenuInflater().inflate(R.menu.menu_pop,popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String info="";
                    switch (item.getItemId()){
                        case R.id.publish:
                            info="发布/取消";
                            break;
                        case R.id.detail:
                            info="图片详情";
                            break;
                        case R.id.star:
                            info="谁点赞了";
                            break;
                        case R.id.comment:
                            info="谁评论了";
                            break;
                        case R.id.delete:
                            info="删除";
                            break;
                    }
                    Toast.makeText(context,info,Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            popup.show();
        });
        holder.itemView.setOnClickListener(v->{
            if (listener != null){
                listener.setItemOnclick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView publishView;
        ImageButton detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_photo);
            publishView=itemView.findViewById(R.id.publish);
            detail=itemView.findViewById(R.id.dropdown_menu_photo);
        }

    }

    public InAlbumAdapter(Context context, List<InalbumPicture> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public interface  Listener{
        void setItemOnclick(int position);
    }

    public void invokeListenr(Listener listener){
        this.listener=listener;
    }

}
