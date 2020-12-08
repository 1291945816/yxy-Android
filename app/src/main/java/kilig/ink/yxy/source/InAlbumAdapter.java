package kilig.ink.yxy.source;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView publishView;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_photo);
            publishView=itemView.findViewById(R.id.publish);
            spinner=itemView.findViewById(R.id.dropdown_menu_photo);
        }

    }

    public InAlbumAdapter(Context context, List<InalbumPicture> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
}
