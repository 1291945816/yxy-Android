package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kilig.ink.yxy.R;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterAcitivt";
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

                Gson gson = new Gson();
                String rginfo = gson.toJson(registerInfo);
                Log.d(TAG,rginfo);

                try {
                    OkhttpUtils.post("yxyUser/register", rginfo, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

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
                //点击图片加载验证码
                Glide.with(context).load(OkhttpUtils.BASE_URL+"captcha/?time="+
                        Calendar.getInstance()
                        .getTimeInMillis()).into(img_vfcode);
                break;
            default:
                break;
        }
    }
}