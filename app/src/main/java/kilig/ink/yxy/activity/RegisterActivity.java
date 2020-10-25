package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

import kilig.ink.yxy.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText rgstUsername;
    private TextInputEditText rgstPassword;
    private TextInputEditText check_rgstPassword;
    private TextInputEditText nickname;
    private TextInputEditText vf_code;
    private Button register;
    Button backup;

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

        //给控件绑定监听事件
        register.setOnClickListener(this);
        backup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.click_rgst:

                break;
            case R.id.btn_backup:
                finish();
            default:
                break;
        }
    }
}