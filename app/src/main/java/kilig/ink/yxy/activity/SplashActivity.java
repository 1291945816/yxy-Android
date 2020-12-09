package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);

        SharedPreferences login_state = getSharedPreferences("login", MODE_PRIVATE);
        boolean isLogin = login_state.getBoolean("isLogin", false);
        Intent intent;

        //这部分做登录判断 若用户登录了就进行直接跳转
        if(isLogin)
        {
            //避免被kill token为空
            OkhttpUtils.setToken(login_state.getString("token","131313"));
            //刷新一下token
            OkhttpUtils.get("yxyUser/refreshToken", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    //...不做处理
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String string = response.body().string(); //获取的返回数据
                    Type type = new TypeToken<ResponeObject<String>>(){}.getType();
                    ResponeObject<String> responeObject = new Gson().fromJson(string, type);
                    OkhttpUtils.setToken(responeObject.getData() == null ?"":responeObject.getData());
                }
            });

            //已经登录则跳转到主界面
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        else
        {
            //未登录则跳转到登录界面
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
