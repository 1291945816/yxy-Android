package kilig.ink.yxy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.SettingActivity;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.entity.YxyUser;
import kilig.ink.yxy.source.SettingItem;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MineFragment extends Fragment  {
    View view;
    private TextView nickName;
    private TextView userName;
    private TextView userIntro;
    private SettingItem changeInfo;
    private SettingItem settingButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mine,container,false);
        //Button exit = view.findViewById(R.id.btn_exit);
        ImageView imgProfile = view.findViewById(R.id.img_profile);
        nickName= view.findViewById(R.id.nickname);
        userName=view.findViewById(R.id.username);
        userIntro=view.findViewById(R.id.user_intro);
        changeInfo=view.findViewById(R.id.changeInfo);
        settingButton=view.findViewById(R.id.yxySetting);

        //跳转到设置界面
        settingButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            getActivity().startActivity(intent);
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
                String yxyUserIntro = responeObject.getData().getYxyUserIntro();
                String yxyUserName = responeObject.getData().getYxyUserName();
                String yxyNickName = responeObject.getData().getYxyNickName();
                userIntro.setText(yxyUserIntro==null?"暂无介绍":yxyUserIntro);
                userName.setText(yxyUserName);
                nickName.setText(yxyNickName);
                //这里应该存储起来个人的信息

                DrawableCrossFadeFactory factory =
                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                //只完成了远程头像的更新
                getActivity().runOnUiThread(()->{
                    //加载头像
                    Glide.with(getActivity())
                            .load(responeObject.getData().getYxyUserAvatar())
                            .transition(withCrossFade(factory))
                            .skipMemoryCache(true)
                            .apply(RequestOptions.bitmapTransform(new CropCircleTransformation())) //头像变圆
                            .into(imgProfile);
                });

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
}
