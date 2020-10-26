package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;


import java.time.LocalDateTime;
import java.util.Calendar;

import kilig.ink.yxy.R;
import kilig.ink.yxy.utils.OkhttpUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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