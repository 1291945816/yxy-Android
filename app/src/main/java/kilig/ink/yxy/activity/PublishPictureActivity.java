package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.roger.catloadinglibrary.CatLoadingView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.fragment.MineFragment;
import kilig.ink.yxy.source.GlideEngine;
import kilig.ink.yxy.utils.FileProgressRequestBody;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PublishPictureActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText pictureName;
    private TextInputEditText pictureIntro;
    private MaterialButton selectFile;
    private SwitchMaterial switchMaterial;
    private Button upload;
    private Map<String,String> map;
    private CatLoadingView mView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_picture);
        map=new HashMap<>();
        pictureName=findViewById(R.id.picture_name);
        selectFile=findViewById(R.id.selectFile);
        pictureIntro=findViewById(R.id.picture_intro);
        switchMaterial=findViewById(R.id.is_publish);
        upload=findViewById(R.id.upload);
        selectFile.setOnClickListener(this);
        upload.setOnClickListener(this);
        Intent info = getIntent();
        AblumItem album = (AblumItem)info.getSerializableExtra("album");
        map.put("ablumId",String.valueOf(album.getAblumId()));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectFile:
                selectFile();
                break;
            case R.id.upload:
                upload();
                break;
        }
    }

    private void selectFile(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .forResult(new OnResultCallbackListener() {
                    @Override
                    public void onResult(List result) {
                        if (result.size() != 0) {

                            LocalMedia localMedia = (LocalMedia) result.get(0);
                            int i = localMedia.getFileName().lastIndexOf(".");
                            map.put("filePath",localMedia.getRealPath()); // 获取到真实路径
                            Log.d("111", "onResult: "+localMedia.getRealPath());
                            map.put("pictureName",localMedia.getFileName().substring(0,i)); //获取文件名

                            runOnUiThread(()->{
                                FancyToast.makeText(getApplicationContext(),"已选 "+localMedia.getFileName().substring(0,i),FancyToast.INFO,FancyToast.LENGTH_SHORT,false).show();
                                pictureName.setText(localMedia.getFileName().substring(0,i));
                            });


                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
    }
    private void upload()  {

        if (pictureName.getText().toString().equals("")){
            FancyToast.makeText(getApplicationContext(),"亲，给你的图片起个好听的名字呗~",FancyToast.INFO,FancyToast.LENGTH_SHORT,false).show();
        }else {
            map.put("pictureName",pictureName.getText().toString());
            map.put("pictureInfo",pictureIntro.getText().toString());
            map.put("publishVisiable",switchMaterial.isChecked()?"1":"0");


            mView = new CatLoadingView();
            mView.show(getSupportFragmentManager(), "");
            mView.setCancelable(false);
            mView.setText("正在努力上传中...");
            mView.setBackgroundColor(Color.parseColor("#2CBEA9"));

            try {
                OkhttpUtils.postPictureWithFile("picture/uploadPicture", map, listener, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(()->{
                            mView.onDestroyView();
                            FancyToast.makeText(getApplicationContext(),"由于未知的原因，上传失败",FancyToast.ERROR,FancyToast.LENGTH_SHORT,false);
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        ResponeObject responeObject = new Gson().fromJson(response.body().string(), ResponeObject.class);
                        Log.d("111", "onResponse: "+responeObject);
                        if (responeObject.getCode().equals("200")){
                            runOnUiThread(()->{
                                mView.onDestroyView();
                                FancyToast.makeText(getApplicationContext(),responeObject.getMessage(),FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
                            });

                        }else {
                            runOnUiThread(()->{
                                mView.onDestroyView();
                                FancyToast.makeText(getApplicationContext(),"上传失败",FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
                            });
                        }

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private FileProgressRequestBody.ProgressListener listener=v->{
        int i = (int) (v * 100);
        //进度相关
        if (i == 100){
            runOnUiThread(()->{
                mView.onDestroyView();
                FancyToast.makeText(getApplicationContext(),"上传成功", FancyToast.SUCCESS,FancyToast.LENGTH_SHORT,false).show();
            });


        }
    };



}