package kilig.ink.yxy.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.CommentEntity;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
{
    private List<CommentEntity> commentList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        
        TextView commentNameTextView;
        TextView commentContentTextView;
        TextView commentTimeTextView;
        ImageView commentProfileImageView;
        
        ViewHolder(View v)
        {
            super(v);
            itemView = v;

            commentNameTextView = v.findViewById(R.id.textView_comment_name);
            commentContentTextView = v.findViewById(R.id.textView_comment_content);
            commentTimeTextView = v.findViewById(R.id.textView_comment_time);
            commentProfileImageView = v.findViewById(R.id.imageView_comment_profile);
        }
    }

    public CommentAdapter(Context context, List<CommentEntity> commentList)
    {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position)
    {
        CommentEntity entity = commentList.get(position);

        Glide.with(context)
                .load(entity.getProfile())
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(holder.commentProfileImageView);
        holder.commentNameTextView.setText(entity.getUserName());
        holder.commentContentTextView.setText(entity.getContent());
        holder.commentTimeTextView.setText(String.valueOf(entity.getCreateTime()));
    }

    @Override
    public int getItemCount()
    {
        return commentList.size();
    }
}