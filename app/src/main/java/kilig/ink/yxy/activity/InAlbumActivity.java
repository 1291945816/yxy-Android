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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.vansz.glideimageloader.GlideImageLoader;
import com.vansz.universalimageloader.UniversalImageLoader;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.entity.InalbumPicture;
import kilig.ink.yxy.entity.PhotoItem;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.entity.SpacesItemDecoration;
import kilig.ink.yxy.source.AlbumsAdapter;
import kilig.ink.yxy.source.InAlbumAdapter;
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
        adapter.invokeListenr(pos->{
            config.setNowThumbnailIndex(pos);
            Log.d("111", "onCreate: "+pos);
            transferee.apply(config).show();

        });

        recyclerView.setAdapter(adapter);









        // 返回相册按钮
        ImageButton tv_return = findViewById(R.id.btn_backup);
        tv_return.setOnClickListener(e -> {
            finish();
        });
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
}
