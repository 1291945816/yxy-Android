package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.shashank.sony.fancytoastlib.FancyToast;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterAcitivt";
    private CatLoadingView mView;
    private TextInputEditText rgstUsername;
    private TextInputEditText rgstPassword;
    private TextInputEditText check_rgstPassword;
    private TextInputEditText nickname;
    private TextInputEditText vf_code;
    private ImageView img_vfcode;
    private Button register;
    private Button backup;
    private Context context=RegisterActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);


        //控件初始化
        rgstUsername = findViewById(R.id.rgstUsername);
        rgstPassword = findViewById(R.id.rgstPassword);
        check_rgstPassword = findViewById(R.id.check_rgstPassword);
        nickname = findViewById(R.id.nickname);
        vf_code = findViewById(R.id.verification_code);
        register = findViewById(R.id.click_rgst);
        backup = findViewById(R.id.btn_backup);
        img_vfcode = findViewById(R.id.img_vfcode);

        //给控件绑定监听事件
        register.setOnClickListener(this);
        backup.setOnClickListener(this);
        img_vfcode.setOnClickListener(this);

        //加载验证码
        Glide.with(context).load(OkhttpUtils.BASE_URL+"captcha/?time="+ Calendar.getInstance()
                .getTimeInMillis()).into(img_vfcode);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.click_rgst:
                Map<String,String> registerInfo = new HashMap<>();
                registerInfo.put("username",rgstUsername.getText().toString());
                registerInfo.put("password",rgstPassword.getText().toString());
                registerInfo.put("nickname",nickname.getText().toString());
                registerInfo.put("vercode",vf_code.getText().toString());

                Gson gson = new Gson();
                String rginfo = gson.toJson(registerInfo);
                Log.d(TAG,rginfo);

                mView=new CatLoadingView();
                mView.show(getSupportFragmentManager(), "");
                mView.setCancelable(false);
                mView.setText("正在努力载入中...");
                mView.setBackgroundColor(Color.parseColor("#2CBEA9"));

                try {
                    OkhttpUtils.post("yxyUser/register", rginfo, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(()->{

                                new FancyAlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("网络开小差了，暂时无法注册")
                                        .setBackgroundColor(Color.parseColor("#2CBEA9"))
                                        .setMessage("要开启网络继续注册吗？")
                                        .setNegativeBtnText("不了")
                                        .setNegativeBtnBackground(Color.parseColor("#2CBEA9"))
                                        .setPositiveBtnText("好的")
                                        .setPositiveBtnBackground(Color.parseColor("#D1635F"))
                                        .setAnimation(Animation.POP)
                                        .setIcon(R.drawable.ic_baseline_sentiment_very_dissatisfied_24,
                                                Icon.Visible)
                                        .isCancellable(false)
                                        .OnNegativeClicked(new FancyAlertDialogListener() {
                                            @Override
                                            public void OnClick() {
                                                Toast.makeText(getApplicationContext(),
                                                        "不开启网络无法注册喔", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .OnPositiveClicked(new FancyAlertDialogListener() {
                                            @Override
                                            public void OnClick() {
                                                //跳转到系统设置页面打开网络
                                                startActivity(new Intent(android.provider.Settings
                                                        .ACTION_WIFI_SETTINGS));
                                            }
                                        })
                                        .build();
                            });

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            ResponseBody responseBody = response.body();
                            assert responseBody != null;
                            String s = responseBody.string();
                            Log.d(TAG, s);
                            ResponeObject object = gson.fromJson(s,ResponeObject.class);
                            //判断状态码 200 ok xxx 不ok
                            if(object.getCode() == null || !object.getCode().equals("200")){
                                runOnUiThread(()->{
                                    mView.onDestroyView();
                                    FancyToast.makeText(RegisterActivity.this,object.getMessage(),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR,
                                            false).show();
                                });
                            }else {
                                runOnUiThread(() -> {
                                    mView.onDestroyView();
                                    FancyToast.makeText(RegisterActivity.this, object.getMessage(),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false).show();
                                });

                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_backup:
                finish();
                break;
            case R.id.img_vfcode:
                //点击图片刷新验证码
                Glide.with(context).load(OkhttpUtils.BASE_URL+"captcha/?time="+
                        Calendar.getInstance()
                        .getTimeInMillis()).into(img_vfcode);
                break;
            default:
                break;
        }
    }
}