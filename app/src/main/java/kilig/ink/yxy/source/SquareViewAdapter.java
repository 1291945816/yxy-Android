package kilig.ink.yxy.source;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.ImageDetailActivity;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.utils.MyFileUtils;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.service.controls.ControlsProviderService.TAG;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SquareViewAdapter extends RecyclerView.Adapter<SquareViewAdapter.ViewHolder>
{
    private List<ImageEntity> squareList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        ImageView squareDisplayImgView;
        TextView  squareImgNameTextView;
        TextView  squareAuthorNameTextView;
        ImageView squareAuthorProfileImgView;
        TextView  squareStarNumTextView;
        ImageView squareLikeImgView;
        TextView  squareDownloadNumTextView;
        ImageView squareDownloadImgView;

        public ViewHolder(View v)
        {
            super(v);
            itemView = v;
            squareDisplayImgView = v.findViewById(R.id.imageView_square_img);
            squareImgNameTextView = v.findViewById(R.id.textView_square_imgName);
            squareAuthorNameTextView = v.findViewById(R.id.textView_square_authorName);
            squareAuthorProfileImgView = v.findViewById(R.id.imageView_square_author_profile);
            squareStarNumTextView = v.findViewById(R.id.textView_square_likeNum);
            squareLikeImgView = v.findViewById(R.id.imageView_square_like);
            squareDownloadNumTextView = v.findViewById(R.id.textView_square_downloadNum);
            squareDownloadImgView = v.findViewById(R.id.imageView_square_download);
        }
    }

    public SquareViewAdapter(Context context, List<ImageEntity> squareList)
    {
        this.squareList = squareList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SquareViewAdapter.ViewHolder holder, int position)
    {
        ImageEntity entity = squareList.get(position);

        //展示的图片
        Glide.with(context)
                .load(entity.getThumbnailUrl())
//                .load(entity.getDisplayImgUrl())
                .error(R.drawable.cloudlogo)
                .fitCenter()
                .into(holder.squareDisplayImgView);

        //头像
        Glide.with(context)
                .load(entity.getAuthorProfileImgUrl())
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(holder.squareAuthorProfileImgView);
        holder.squareImgNameTextView.setText(entity.getDisplayImgName());
        holder.squareAuthorNameTextView.setText(entity.getAuthorName());
        holder.squareStarNumTextView.setText(String.valueOf(entity.getStarNum()));
        if (entity.isStared())  //已经点赞
        {
            holder.squareLikeImgView.setImageResource(R.drawable.ic_unlike);
        }
        else //未点赞
        {
            holder.squareLikeImgView.setImageResource(R.drawable.ic_like);
        }
        holder.squareDownloadNumTextView.setText(String.valueOf(entity.getDownloadNum()));
    }

    @NonNull
    @Override
    public SquareViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_square, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        //整个布局的点击事件
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

        holder.squareDisplayImgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ImageEntity entity = squareList.get(position);
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putExtra("ImageEntity", entity);
                context.startActivity(intent);
            }
        });

        holder.squareLikeImgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ImageEntity entity = squareList.get(position);
                if (entity.isStared())  //已经点赞，则取消
                {
                    entity.setStared(false);
                    entity.setStarNum(entity.getStarNum() - 1);
                    holder.squareStarNumTextView.setText(String.valueOf(entity.getStarNum()));
                    holder.squareLikeImgView.setImageResource(R.drawable.ic_like);
                }
                else    //未点赞，则加上
                {
                    entity.setStared(true);
                    entity.setStarNum(entity.getStarNum() + 1);
                    holder.squareStarNumTextView.setText(String.valueOf(entity.getStarNum()));
                    holder.squareLikeImgView.setImageResource(R.drawable.ic_unlike);
                }

                //改变云端点赞数量
                Map<String, String> map = new HashMap<>();
                map.put("pictureId", entity.getImgID());
                String json = new Gson().toJson(map);
                try
                {
                    OkhttpUtils.post("picture/star", json, new Callback()
                    {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e)
                        {
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                        {
                        }
                    });
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        holder.squareDownloadImgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ImageEntity entity = squareList.get(position);

                holder.squareDownloadImgView.setClickable(false);
                entity.setDownloadNum(entity.getDownloadNum() + 1);
                holder.squareDownloadNumTextView.setText(String.valueOf(entity.getDownloadNum()));

                //更新云端数据：此图片下载数加一
                Map<String, String> map = new HashMap<>();
                map.put("pictureId", entity.getImgID());
                String json = new Gson().toJson(map);
                try
                {
                    OkhttpUtils.post("picture/download_count", json, new Callback()
                    {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e)
                        {
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                        {
                        }
                    });
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                //下载
                new Thread(() -> {
                    try
                    {
                        FutureTarget<File> target = Glide.with(context)
                                .asFile()
                                .load(entity.getDisplayImgUrl())
                                .submit();
                        final File imageFile = target.get();
                        String fileName = entity.getDisplayImgName() + "_" + entity.getDisplayImgUrl().substring(entity.getDisplayImgUrl().lastIndexOf('/')+1);
                        boolean saveSuccess = MyFileUtils.saveFile(imageFile, fileName);
                        //不能在子线程用toast
//                        if (saveSuccess)
//                        {
//                            Log.e(TAG, "Save：图片保存成功");
//                            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            Log.e(TAG, "Save：图片保存失败");
//                            Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show();
//                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }).start();
                Toast.makeText(context, "图片正在保存~", Toast.LENGTH_SHORT).show();
            }

        });

        holder.squareAuthorProfileImgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                ImageEntity entity = squareList.get(position);
                //todo 跳转到用户主页
                Toast.makeText(context, entity.getAuthorName(), Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public int getItemCount()
    {
        return squareList.size();
    }
}