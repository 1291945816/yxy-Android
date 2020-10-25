package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 登录界面+逻辑处理
 */

public class LoginActivity extends AppCompatActivity {
    private CatLoadingView mView;
    private static final String TAG = "MainActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // SharedPreferences login_state = getSharedPreferences("login", MODE_PRIVATE);
     //   boolean isLogin = login_state.getBoolean("isLogin", false);
        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        if(isLogin)
//        {
////            //刷新一下token
////            OkhttpUtils.get("refreshToken", null, new Callback() {
////                @Override
////                public void onFailure(@NotNull Call call, @NotNull IOException e) {
////
////                }
////
////                @Override
////                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
////                    String string = response.body().string(); //获取的返回数据
////                    /**
////                     * 后续更新
////                     */
////
////                }
////
////            });
//            //跳转
//            startActivity(intent);
//            finish();
//        }

        //用户名
        TextInputEditText username = findViewById(R.id.yxyUsername);
        //密码
        TextInputEditText password = findViewById(R.id.yxyPassword);
        //登录
        Button login = findViewById(R.id.login);
        //注册
        Button rigister = findViewById(R.id.btn_rigister);
        //记住密码
        CheckBox rememberpsw= findViewById(R.id.ckb_rememberpsw);
        //获取SharedPreferences对象
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // 将账号和密码都设置到文本框中
            String tUsername = pref.getString("account", "");
            String tPassword = pref.getString("password", "");
            username.setText(tUsername);
            password.setText(tPassword);
            rememberpsw.setChecked(true);
        }

        Log.d(TAG, "onCreate: 999");

        //登录逻辑
        login.setOnClickListener(v->{

            Map<String,String> loginInfo=new HashMap<>();
            loginInfo.put("username",username.getText().toString());
            loginInfo.put("password",password.getText().toString());

            Gson gson=new Gson();
            String info = gson.toJson(loginInfo);
            Log.d(TAG, info);
            mView=new CatLoadingView();
            mView.show(getSupportFragmentManager(), "");
            mView.setCancelable(false);
            mView.setText("正在努力载入中...");
            mView.setBackgroundColor(Color.parseColor("#2CBEA9"));


            try {
                OkhttpUtils.post("yxyUser/login", info, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException q) {
                            q.printStackTrace();
                        }
                        runOnUiThread(()->{

                            FancyToast.makeText(LoginActivity.this,"由于未知的原因，登录失败，请检查一下您的网络是否有问题?",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.WARNING,
                                    false).show();
                        mView.onDestroyView();
                        });


                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        assert responseBody != null;
                        String s = responseBody.string();
                        ResponeObject object = gson.fromJson(s, ResponeObject.class);
                        //关闭窗口
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断状态码 200 ok xxx 不ok
                        if(object.getCode() == null || !object.getCode().equals("200")){
                            runOnUiThread(()->{
                                mView.onDestroyView();
                                FancyToast.makeText(LoginActivity.this,object.getMessage(),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false).show();
                            });
                        }else {
                            runOnUiThread(()->{
                                mView.onDestroyView();
                                FancyToast.makeText(LoginActivity.this,object.getMessage(),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false).show();
                            });
                            OkhttpUtils.setToken((String) object.getData()); //传递token


                            //记录登录状态
                            SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                            editor.putBoolean("isLogin",true);
                            editor.apply();
                            //禁止后退回到登录界面
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        }




                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        //跳转到注册页面
        rigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RigisterActivity.class);
                startActivity(intent);
            }
        });
    }
}