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
import kilig.ink.yxy.entity.SquareViewEntity;

public class SquareViewAdapter extends RecyclerView.Adapter<SquareViewAdapter.ViewHolder>
{
    private List<SquareViewEntity> squareList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        ImageView squareDisplayImgView;
        TextView  squareImgNameTextView;
        TextView  squareAuthorNameTextView;
        ImageView squareAuthorProfileImgView;
        TextView  squareStarNumTextView;

        public ViewHolder(View v)
        {
            super(v);
            itemView = v;
            squareDisplayImgView = v.findViewById(R.id.imageView_square_img);
            squareImgNameTextView = v.findViewById(R.id.textView_square_imgName);
            squareAuthorNameTextView = v.findViewById(R.id.textView_square_authorName);
            squareAuthorProfileImgView = v.findViewById(R.id.imageView_square_author_profile);
            squareStarNumTextView = v.findViewById(R.id.textView_square_likeNum);
        }
    }

    public SquareViewAdapter(Context context, List<SquareViewEntity> squareList)
    {
        this.squareList = squareList;
        this.context = context;
    }

    @NonNull
    @Override
    public SquareViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_square, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        //布局的点击事件
//        holder.itemView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                int position = holder.getAdapterPosition();
//                Intent intent = new Intent(context, ShowArticleActivity.class);
//                intent.putExtra("url", articleList.get(position).getLocalUrl());
//                context.startActivity(intent);
//            }
//        });
//
//        holder.articleCoverImageView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                int position = holder.getAdapterPosition();
//                Intent intent = new Intent(context, ShowArticleActivity.class);
//                intent.putExtra("url", articleList.get(position).getArticleUrl());
//                context.startActivity(intent);
//            }
//        });
//
//        holder.authorImageView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                int position = holder.getAdapterPosition();
//                String authorName = articleList.get(position).getAuthorName();
//
//                setData(authorName);
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SquareViewAdapter.ViewHolder holder, int position)
    {
        SquareViewEntity entity = squareList.get(position);

        Glide.with(context)
                .load(entity.getDisplayImgUrl())
                .into(holder.squareDisplayImgView);
        Glide.with(context)
                .load(entity.getAuthorProfileImgUrl())
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(holder.squareAuthorProfileImgView);
        holder.squareImgNameTextView.setText(entity.getDisplayImgName());
        holder.squareAuthorNameTextView.setText(entity.getAuthorName());
        holder.squareStarNumTextView.setText(String.valueOf(entity.getStarNum()));
    }

    @Override
    public int getItemCount()
    {
        return squareList.size();
    }
}