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
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.InalbumPicture;
import kilig.ink.yxy.entity.PhotoItem;
import kilig.ink.yxy.entity.SpacesItemDecoration;
import kilig.ink.yxy.source.InAlbumAdapter;

public class InAlbumActivity extends AppCompatActivity {

    private List<InalbumPicture> photosList ;
    private TextView topTitle;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inalbum);
        photosList= new ArrayList<>();
        Intent intent = getIntent();
        AblumItem album = (AblumItem)intent.getSerializableExtra("album");
        TextView topTitle = findViewById(R.id.topTitle_in_album);
        topTitle.setText(album.getAblumName());
        InitData();
       recyclerView = (RecyclerView)findViewById(R.id.inalbum_pict);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        InAlbumAdapter adapter = new InAlbumAdapter(this, photosList);
        recyclerView.setAdapter(adapter);

        // 返回相册按钮
        ImageButton tv_return = findViewById(R.id.btn_backup);
        tv_return.setOnClickListener(e -> {
            finish();
        });
    }

    void InitData() {
        for (int i = 0; i < 10; i++) {
            InalbumPicture photo = new InalbumPicture();
           photo.setPublish(i > 3);
           photo.setImgUrl("http://120.25.213.148:9000/yxy-pictures/3/ac1bd18ff289410f3e46978be15fa6b6.jpg");
           photo.setThumbnailUrl("http://120.25.213.148:9000/yxy-pictures/thumbnail/ac1bd18ff289410f3e46978be15fa6b6.jpg");
           photosList.add(photo);
        }
    }

}
