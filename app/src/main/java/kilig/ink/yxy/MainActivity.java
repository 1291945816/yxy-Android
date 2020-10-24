package kilig.ink.yxy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            try {
                OkhttpUtils.post("yxyUser/login", info, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        assert responseBody != null;
                        String s = responseBody.string();
                        ResponeObject object = gson.fromJson(s, ResponeObject.class);

                        Log.d(TAG, s);
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
                Intent intent = new Intent(MainActivity.this,RigisterActivity.class);
                startActivity(intent);
            }
        });
        
    }
}