package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.CommentEntity;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.source.CommentAdapter;
import kilig.ink.yxy.utils.MyFileUtils;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    TextView  detailCommentNumTextView;
    ImageView detailAddCommentImageView;

    BottomSheetDialog dialog;

    CommentAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<CommentEntity> commentList;
    String json;

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
        initCommentData();
    }

    private void initView()
    {
        recyclerView = findViewById(R.id.recyclerView_detail_comments);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        detailDisplayImgView = findViewById(R.id.imageView_detail_img);
        detailImgNameTextView = findViewById(R.id.textView_detail_imgName);
        detailAuthorNameTextView = findViewById(R.id.textView_detail_authorName);
        detailAuthorProfileImgView = findViewById(R.id.imageView_detail_author_profile);
        detailStarNumTextView = findViewById(R.id.textView_detail_likeNum);
        detailLikeImgView = findViewById(R.id.imageView_detail_like);
        detailDownloadNumTextView = findViewById(R.id.textView_detail_downloadNum);
        detailDownloadImgView = findViewById(R.id.imageView_detail_download);
//        detailCommentNumTextView = findViewById(R.id.textView_detail_commentNument);
        detailAddCommentImageView = findViewById(R.id.imageView_detail_comment);

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
//        detailCommentNumTextView.setText(String.valueOf(entity.get));

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
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }).start();
                Toast.makeText(ImageDetailActivity.this, "图片正在保存~", Toast.LENGTH_SHORT).show();
            }
        });

        detailAddCommentImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog = new BottomSheetDialog(ImageDetailActivity.this);
                View commentView = LayoutInflater.from(ImageDetailActivity.this).inflate(R.layout.view_add_comment,null);
                EditText commentEdiText = commentView.findViewById(R.id.editText_add_comment);
                TextView titleTextView = commentView.findViewById(R.id.textView_comment_title);
                titleTextView.setText(entity.getDisplayImgName());
                Button publishButton = commentView.findViewById(R.id.button_add_comment);
                publishButton.setOnClickListener(v1 ->
                {
                    String comment = commentEdiText.getText().toString();
                    if (comment.isEmpty())
                    {
                        Toast.makeText(ImageDetailActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, String> commentInfo = new HashMap<>();
                    commentInfo.put("comment", comment);
                    commentInfo.put("pictureId", entity.getImgID());

                    String info = new Gson().toJson(commentInfo);
                    try
                    {
                        OkhttpUtils.post("picture/comment", info, new Callback()
                        {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e)
                            {
                                runOnUiThread(() -> Toast.makeText(ImageDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(ImageDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                        initCommentData();
                                        dialog.cancel();
                                    }
                                });
                            }
                        });
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                });
                dialog.setContentView(commentView);
                dialog.show();
            }
        });
    }

    private void initCommentData()
    {
        Map<String, String> map = new HashMap<>();
        map.put("pictureId", String.valueOf(entity.getImgID()));
        commentList = new ArrayList<>();
        OkhttpUtils.get("picture/comment", map, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                json = response.body().string();
                Type type = new TypeToken<ResponeObject<List<CommentEntity>>>(){}.getType();
                ResponeObject<ArrayList<CommentEntity>> responeObject = new Gson().fromJson(json, type);
                commentList.addAll(responeObject.getData());
                new Handler(Looper.getMainLooper()).post(()->{
                    adapter.notifyDataSetChanged();
                });
            }
        });
        adapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(adapter);
    }
}