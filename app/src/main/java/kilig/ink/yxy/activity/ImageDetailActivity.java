package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.utils.MyFileUtils;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Map;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageDetailActivity extends AppCompatActivity
{
    ImageEntity entity = null;

    ImageView detailDisplayImgView;
    TextView detailImgNameTextView;
    TextView  detailAuthorNameTextView;
    ImageView detailAuthorProfileImgView;
    TextView  detailStarNumTextView;
    ImageView detailLikeImgView;
    TextView  detailDownloadNumTextView;
    ImageView detailDownloadImgView;

    DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        Intent intent = getIntent();
        if (intent != null)
        {
            entity = (ImageEntity) intent.getSerializableExtra("ImageEntity");
        }
        initView();
    }

    private void initView()
    {
        detailDisplayImgView = findViewById(R.id.imageView_detail_img);
        detailImgNameTextView = findViewById(R.id.textView_detail_imgName);
        detailAuthorNameTextView = findViewById(R.id.textView_detail_authorName);
        detailAuthorProfileImgView = findViewById(R.id.imageView_detail_author_profile);
        detailStarNumTextView = findViewById(R.id.textView_detail_likeNum);
        detailLikeImgView = findViewById(R.id.imageView_detail_like);
        detailDownloadNumTextView = findViewById(R.id.textView_detail_downloadNum);
        detailDownloadImgView = findViewById(R.id.imageView_detail_download);



        Glide.with(this)
                .load(entity.getDisplayImgUrl())
                .error(R.drawable.cloudlogo)
                .fitCenter()
                .transition(withCrossFade(factory))
                .into(detailDisplayImgView);
        Glide.with(this)
                .load(entity.getAuthorProfileImgUrl())
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(detailAuthorProfileImgView);
        detailImgNameTextView.setText(entity.getDisplayImgName());
        detailAuthorNameTextView.setText(entity.getAuthorName());
        detailStarNumTextView.setText(String.valueOf(entity.getStarNum()));
        if (entity.isStared())  //已经点赞
        {
            detailLikeImgView.setImageResource(R.drawable.ic_unlike);
        }
        else //未点赞
        {
            detailLikeImgView.setImageResource(R.drawable.ic_like);
        }
        detailDownloadNumTextView.setText(String.valueOf(entity.getDownloadNum()));

        detailLikeImgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (entity.isStared())  //已经点赞，则取消
                {
                    entity.setStared(false);
                    entity.setStarNum(entity.getStarNum() - 1);
                    detailStarNumTextView.setText(String.valueOf(entity.getStarNum()));
                    detailLikeImgView.setImageResource(R.drawable.ic_like);
                }
                else    //未点赞，则加上
                {
                    entity.setStared(true);
                    entity.setStarNum(entity.getStarNum() + 1);
                    detailStarNumTextView.setText(String.valueOf(entity.getStarNum()));
                    detailLikeImgView.setImageResource(R.drawable.ic_unlike);
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
                        public void onFailure(Call call, @NotNull IOException e)
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

        detailDownloadImgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                detailDownloadImgView.setClickable(false);
                entity.setDownloadNum(entity.getDownloadNum() + 1);
                detailDownloadNumTextView.setText(String.valueOf(entity.getDownloadNum()));

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
                        FutureTarget<File> target = Glide.with(ImageDetailActivity.this)
                                .asFile()
                                .load(entity.getDisplayImgUrl())
                                .submit();
                        final File imageFile = target.get();
                        String fileName = entity.getDisplayImgName() + entity.getDisplayImgUrl().substring(entity.getDisplayImgUrl().lastIndexOf('/')+1);
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
                Toast.makeText(ImageDetailActivity.this, "图片正在保存~", Toast.LENGTH_SHORT).show();
            }

        });
    }
}