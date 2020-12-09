package kilig.ink.yxy.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.vansz.glideimageloader.GlideImageLoader;
import com.vansz.universalimageloader.UniversalImageLoader;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.entity.InalbumPicture;
import kilig.ink.yxy.entity.PhotoItem;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.entity.SpacesItemDecoration;
import kilig.ink.yxy.source.AlbumsAdapter;
import kilig.ink.yxy.source.GlideEngine;
import kilig.ink.yxy.source.InAlbumAdapter;
import kilig.ink.yxy.utils.FileProgressRequestBody;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InAlbumActivity extends AppCompatActivity {

    private List<InalbumPicture> photosList ;
    private List<String> list;
    private TextView topTitle;
    private RecyclerView recyclerView;
    private Transferee transferee;
    private TransferConfig config;
    private AblumItem ablumItem;

    private InAlbumAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inalbum);
        transferee = Transferee.getDefault(this);
        photosList= new ArrayList<>();
        list=new ArrayList<>();
        Intent intent = getIntent();
        ablumItem = (AblumItem)intent.getSerializableExtra("album");
        TextView topTitle = findViewById(R.id.topTitle_in_album);
        topTitle.setText(ablumItem.getAblumName());
        InitData();
        config();

       recyclerView = (RecyclerView)findViewById(R.id.inalbum_pict);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

         adapter = new InAlbumAdapter(this, photosList);
        adapter.invokeListener(pos->{
            config.setNowThumbnailIndex(pos);
            Log.d("111", "onCreate: "+pos);
            transferee.apply(config).show();

        });
        adapter.invokeUListener(this::changePublish);
        adapter.invokeDListener(this::deleteItemPicture);
        recyclerView.setAdapter(adapter);
        Activity activity = this;
        // 添加图片按钮
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button_in_album);
        floatingActionButton.setOnClickListener(v -> {
                    Intent toPublish = new Intent(InAlbumActivity.this, PublishPictureActivity.class);
                    toPublish.putExtra("album",ablumItem);
                    startActivity(toPublish);



                }
//                PictureSelector.create(activity)
//                        .openGallery(PictureMimeType.ofImage())
//                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
//                        .forResult(PictureConfig.CHOOSE_REQUEST);

        );











        // 返回相册按钮
        ImageButton tv_return = findViewById(R.id.btn_backup);
        tv_return.setOnClickListener(e -> {
            finish();
        });
    }

    private void deleteItemPicture(int pos, InalbumPicture inalbumPicture) {
        try {
            OkhttpUtils.post("ablum/deletePicture/" + inalbumPicture.getId(), "", new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(()->{
                        FancyToast.makeText(getApplicationContext(),"出现异常，删除失败",FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String json = response.body().string();
                    ResponeObject responeObject = new Gson().fromJson(json, ResponeObject.class);
                    if (responeObject != null && responeObject.getCode().equals("200")){
                        runOnUiThread(()->{
                            adapter.removeItem(pos);
                            FancyToast.makeText(getApplicationContext(),responeObject.getMessage(),FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
                        });
                    }else {
                        runOnUiThread(()->{
                            FancyToast.makeText(getApplicationContext(),"出现异常，删除失败",FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
                        });
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // todo zyb 2 hps:写一下图片上传接口
                    // 获取到所有的选择结果
                    // 其中localMedia.getPath()是照片在设备中的绝对地址
                    // 下面有个imageToBase64方法，是把图片路径转化为byte[](应该能用，还没测试)
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia localMedia: selectList) {
                        Map<String, String> Image_info = new HashMap<String, String>();
                        Image_info.put("publishVisiable", "0");
                        Image_info.put("pictureInfo", localMedia.getFileName());
                        Image_info.put("ablumId", String.valueOf(ablumItem.getAblumId()));
                        Image_info.put("pictureName", localMedia.getFileName());
                    }
                        // 下面的是我测试的代码，可以删了
                         break;
                default:
                    break;
            }
        }
    }

    void InitData() {
        OkhttpUtils.get("ablum/pictures/"+ablumItem.getAblumId(), null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**
                 * //
                 */
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Type type = new TypeToken<ResponeObject<List<InalbumPicture>>>(){}.getType();
                ResponeObject<ArrayList<InalbumPicture>> responeObject = new Gson().fromJson(json, type);
                if(responeObject != null||responeObject.getCode().equals("200")){
                    list.clear();
                    photosList.clear();
                    photosList.addAll(responeObject.getData());
                    for (InalbumPicture inalbumPicture:photosList){
                        list.add(inalbumPicture.getImgUrl());
                    }
                    runOnUiThread(()->{
                        adapter.notifyDataSetChanged();

                    });

                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transferee.destroy();
    }
    private void config(){
         config=TransferConfig.build()
                .setBackgroundColor(R.color.black)
                .setSourceUrlList(list)
                 .enableScrollingWithPageChange(true)
                 .enableJustLoadHitPage(true)
                 .setImageLoader(GlideImageLoader.with(getApplicationContext()))
                .bindRecyclerView(recyclerView,R.id.item_photo);
    }


    private void changePublish(int pos,InalbumPicture picture){
        Map<String,String> map=new HashMap<>();
        map.put("pictureId",picture.getId());
        map.put("publish",String.valueOf(!picture.isPublish()));
        String toJson = new Gson().toJson(map);
        try {
            OkhttpUtils.post("ablum/changePublishStatus", toJson, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(()->{
                        FancyToast.makeText(getApplicationContext(),e.getMessage(),FancyToast.ERROR,FancyToast.LENGTH_SHORT,false).show();
                    });


                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseString = response.body().string();
                    ResponeObject responeObject = new Gson().fromJson(responseString, ResponeObject.class);
                    if (responeObject== null || !responeObject.getCode().equals("200")){
                        runOnUiThread(()->{
                            FancyToast.makeText(getApplicationContext(),"更新失败，请重试",FancyToast.ERROR,FancyToast.LENGTH_SHORT,false).show();
                        });
                    }else{
                        runOnUiThread(()->{
                            picture.setPublish(!picture.isPublish());

                            adapter.notifyItemChanged(pos);
                            FancyToast.makeText(getApplicationContext(),"更新发布状态成功",FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
                        });
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
