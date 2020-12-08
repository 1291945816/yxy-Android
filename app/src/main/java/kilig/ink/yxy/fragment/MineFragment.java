package kilig.ink.yxy.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.ChangeInfoActivity;
import kilig.ink.yxy.activity.ChangePswActivity;
import kilig.ink.yxy.activity.SettingActivity;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.entity.YxyUser;
import kilig.ink.yxy.source.GlideEngine;
import kilig.ink.yxy.source.SettingItem;
import kilig.ink.yxy.utils.FileProgressRequestBody;
import kilig.ink.yxy.utils.MyFileUtils;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MineFragment extends Fragment  {
    View view;
    private TextView nickName;
    private TextView userName;
    private TextView userIntro;
    private TextView publishSumView;
    private TextView starNumsView;
    private TextView commentSumView;
    private SettingItem changeInfo;
    private SettingItem settingButton;
    private SettingItem helpButton;
    private SettingItem aboutButton;
    private ImageView imgProfile;
    private ImageView   backgroundImageView;
    private static final String TAG = "MineFragment";
   //private AnimatedCircleLoadingView progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_mine,container,false);
        imgProfile = view.findViewById(R.id.img_profile);
        backgroundImageView = view.findViewById(R.id.image_background);
        nickName= view.findViewById(R.id.nickname);
        userName=view.findViewById(R.id.username);
        userIntro=view.findViewById(R.id.user_intro);
        changeInfo=view.findViewById(R.id.changeInfo);
        settingButton=view.findViewById(R.id.yxySetting);
        helpButton=view.findViewById(R.id.yxyHelp);
        aboutButton=view.findViewById(R.id.yxyAbout);
        publishSumView=view.findViewById(R.id.publishSum);
        starNumsView=view.findViewById(R.id.starNums);
        commentSumView=view.findViewById(R.id.commentNum);
        //progressBar=view.findViewById(R.id.circle_loading_view);






        //点击头像，进行头像上传
        imgProfile.setOnClickListener(v->{
            /*
            LayoutInflater inflaterProfile = getActivity().getLayoutInflater();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            view = inflaterProfile.inflate(R.layout.dialog_profile, null);
            alertBuilder.setView(view);*/
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
                                Log.d(TAG, "onResult: "+localMedia.getRealPath());
                                Map<String,String> map=new HashMap<>();
                                map.put("fileName",localMedia.getFileName());
                                map.put("filePath",localMedia.getRealPath());
                                try {
                                    OkhttpUtils.postWithBody("yxyUser/uploadAvatar", map,listener, new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            Log.d(TAG, "onFailure: "+66);

                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            String string = response.body().string();
                                            Gson gson = new Gson();
                                            ResponeObject responeObject = gson.fromJson(string, ResponeObject.class);
                                            if (isAdded()){
                                                getActivity().runOnUiThread(()->{
                                                    Toast.makeText(getActivity(),responeObject.getMessage(),Toast.LENGTH_SHORT);
                                                });
                                            }

                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });




            Toast.makeText(getActivity(),"等一下，马上就能上传头像啦^_^",Toast.LENGTH_SHORT).show();

        });


        //跳转到设置界面
        settingButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            getActivity().startActivity(intent);
            //getActivity().finish();
        });

        //跳转到修改信息界面
        changeInfo.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ChangeInfoActivity.class);
            startActivity(intent);
        });

        //帮助提示
        helpButton.setOnClickListener(v->{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("帮助提示")
                    .setMessage("“首页”是图片广场，可进行图片的浏览和下载；“个人相册”用于管理您的图片；“我的”是用户中心" )
                    .setIcon(R.drawable.ic_help)
                    .setPositiveButton("确定", null).create();
            alertDialog.show();
        });

        //关于
        aboutButton.setOnClickListener(v->{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("关于我们")
                    .setMessage("您好，这里是伊享云开发团队，开发不易，历时一个多月，如果您对我们的软件有任何吐槽或建议，欢迎您联系我们，邮箱：1291945816@qq.com" )
                    .setIcon(R.drawable.ic_cloud_)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(),"感谢相遇~",Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            alertDialog.show();
        });

        OkhttpUtils.get("yxyUser/userInfo", null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("MineFragment", "onFailure: ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                Log.i("MineFragment", "onResponse: "+string);
                Gson gson = new Gson();
                ResponeObject<YxyUser> responeObject = gson.fromJson(string, new TypeToken<ResponeObject<YxyUser>>() {
                }.getType());

                Log.i("MineFragment", "onResponse: "+responeObject.getData());


                if (!responeObject.getCode().equals("200")){
                    Log.d(TAG, "onResponse: "+OkhttpUtils.getToken()+"1212");
                }else {
                    String yxyUserIntro = responeObject.getData().getYxyUserIntro();
                    String yxyUserName = responeObject.getData().getYxyUserName();
                    String yxyNickName = responeObject.getData().getYxyNickName();
                    long starNums = responeObject.getData().getStarNums();
                    long publishSum = responeObject.getData().getPublishSum();
                    long commentSum = responeObject.getData().getCommentSum();

                    DrawableCrossFadeFactory factory =
                            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

                    new Handler(Looper.getMainLooper()).post(()->{

                    //只完成了远程头像的更新

                        userIntro.setText(yxyUserIntro == null ? "暂无介绍" : yxyUserIntro);
                        userName.setText(yxyUserName);
                        nickName.setText(yxyNickName);
                        starNumsView.setText(String.valueOf(starNums));
                        publishSumView.setText(String.valueOf(publishSum));
                        commentSumView.setText(String.valueOf(commentSum));
                        //这里应该存储起来个人的信息
                        //加载头像
                        //避免碎片还未加载进活动就调用活动，导致空指针
                        if (isAdded()){
                            Glide.with(getActivity())
                                    .load(responeObject.getData().getYxyUserAvatar())
                                    .transition(withCrossFade(factory))
                                    .skipMemoryCache(true)
                                    .apply(bitmapTransform(new CropCircleTransformation())) //头像变圆
                                    .into(imgProfile);
                            //加载背景
                            Glide.with(getActivity())
                                    .load(responeObject.getData().getYxyUserAvatar())
                                    .apply(bitmapTransform(new BlurTransformation(50, 3)))
                                    .into(backgroundImageView);
                        }


                    });
                }

            }
        });


        /*exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().finish();
            }
        });*/

        return view;
    }

    FileProgressRequestBody.ProgressListener listener=(v)->{
        int i = (int) (v * 100);
        if (isAdded() ){
                //进度相关
        }
    };


}
