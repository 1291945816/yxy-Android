package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;



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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //用户名
        TextInputEditText username = findViewById(R.id.yxyUsername);
        //密码
        TextInputEditText password = findViewById(R.id.yxyPassword);
        Button login = findViewById(R.id.login);
        Button rigister = findViewById(R.id.btn_rigister);

        Log.d(TAG, "onCreate: 999");
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
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        mView.onDestroyView();
                        Log.d(TAG, "onFailure: 666");

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        assert responseBody != null;
                        String s = responseBody.string();
                        ResponeObject object = gson.fromJson(s, ResponeObject.class);
                        //关闭窗口
                        mView.onDestroyView();

                        //判断状态码 200 ok xxx 不ok
                        if(object.getCode() == null || !object.getCode().equals("200")){
                            runOnUiThread(()->{
                                FancyToast.makeText(LoginActivity.this,object.getMessage(),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false).show();
                            });
                        }else {
                            runOnUiThread(()->{
                                FancyToast.makeText(LoginActivity.this,object.getMessage(),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false).show();
                            });
                            OkhttpUtils.setToken((String) object.getData()); //传递token
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);


                        }




                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        rigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RigisterActivity.class);
                startActivity(intent);
            }
        });
    }
}