package kilig.ink.yxy.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

@RequiresApi(api = 30)
public class LoginActivity extends AppCompatActivity {

    //要申请的权限
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE};

    private CatLoadingView mView;
    private static final String TAG = "LoginActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //授权
        initPermissions();

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
        usernameLayout=findViewById(R.id.username);

        //密码
        TextInputEditText password = findViewById(R.id.yxyPassword);
        passwordLayout=findViewById(R.id.password);

        //登录
        Button login = findViewById(R.id.login);
        //注册
        Button rigister = findViewById(R.id.btn_rigister);
        //记住密码
        CheckBox rememberpsw= findViewById(R.id.ckb_rememberpsw);
        //获取SharedPreferences对象
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isRemember = pref.getBoolean("记住密码", false);
        if (isRemember) {
            // 将账号和密码都设置到文本框中
            String tUsername = pref.getString("username", "");
            String tPassword = pref.getString("password", "");
            username.setText(tUsername);
            password.setText(tPassword);
            rememberpsw.setChecked(true);
        }


        //登录逻辑
        login.setOnClickListener(v->{
            String mUsername = username.getText().toString();
            String mPassword = password.getText().toString();
            if (mUsername.isEmpty()){
                usernameLayout.setError("用户名不能够为空");
            }else if (mPassword.isEmpty()){
                usernameLayout.setError(null);
                passwordLayout.setError("密码不能够为空");
            }else {
                usernameLayout.setError(null);
                passwordLayout.setError(null);
                Map<String, String> loginInfo = new HashMap<>();
                loginInfo.put("username", mUsername);
                loginInfo.put("password", mPassword);

                Gson gson = new Gson();
                String info = gson.toJson(loginInfo);
                Log.d(TAG, info);
                mView = new CatLoadingView();
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
                            runOnUiThread(() -> {

                                FancyToast.makeText(LoginActivity.this, "由于未知的原因，登录失败，请检查一下您的网络是否有问题?",
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
                            Log.d(TAG, s);
                            ResponeObject object = gson.fromJson(s, ResponeObject.class);
                            //关闭窗口
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //判断状态码 200 ok xxx 不ok
                            if (object.getCode() == null || !object.getCode().equals("200")) {
                                runOnUiThread(() -> {
                                    mView.onDestroyView();
                                    FancyToast.makeText(LoginActivity.this, object.getMessage(),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR,
                                            false).show();
                                });
                            } else {
                                runOnUiThread(() -> {
                                    mView.onDestroyView();
                                    FancyToast.makeText(LoginActivity.this, object.getMessage(),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false).show();
                                });
                                OkhttpUtils.setToken((String) object.getData()); //传递token

                                //记住密码逻辑
                                editor = pref.edit();
                                if (rememberpsw.isChecked()) {//检查复选框是否被选中
                                    editor.putBoolean("记住密码", true);
                                    editor.putString("username", mUsername);
                                    editor.putString("password", mPassword);
                                } else {
                                    editor.clear();
                                }
                                editor.commit();

                                //记录登录状态
                                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                                editor.putBoolean("isLogin", true);
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

            }
        });


        //跳转到注册页面
        rigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            for (String permission : permissions)
            {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(LoginActivity.this, permission))
                {
                    ActivityCompat.requestPermissions(LoginActivity.this, permissions, 1);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    break;
                }
                else
                {
                    new android.app.AlertDialog.Builder(LoginActivity.this)
                            .setMessage("权限不足")
                            .setPositiveButton("重新授权", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    initPermissions();
                                }
                            })
                            .setNegativeButton("退出应用", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                break;
        }
    }
}