package kilig.ink.yxy.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.PhotoItem;
import kilig.ink.yxy.source.InAlbumAdapter;

public class InAlbumActivity extends AppCompatActivity {

    private List<PhotoItem> photosList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inalbum);

        InitData();

        InAlbumAdapter photoAdapter = new InAlbumAdapter(photosList, this.getApplicationContext());
        GridView gridView = findViewById(R.id.gridView_in_album);
        gridView.setAdapter(photoAdapter);

        // 添加图片按钮
        com.google.android.material.floatingactionbutton.FloatingActionButton button = findViewById(R.id.floating_action_button_in_album);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });

        // 返回相册按钮
        TextView tv_return = findViewById(R.id.return_in_album);
        tv_return.setOnClickListener(e -> {
            finish();
        });
    }

    void InitData() {
        for (int i = 0; i < 10; i++) {
            PhotoItem photo = new PhotoItem();
            photo.setResourceId(R.drawable.cat);
            photosList.add(photo);
        }
    }

    // todo 不知道怎么用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                PhotoItem photo = new PhotoItem();
                ImageView imageView = (ImageView) findViewById(photo.getResourceId());
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
                photosList.add(photo);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
