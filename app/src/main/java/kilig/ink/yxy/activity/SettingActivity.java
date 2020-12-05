package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import kilig.ink.yxy.R;
import kilig.ink.yxy.fragment.MineFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button exitSystem = findViewById(R.id.btn_exit);
        TextView setPath = findViewById(R.id.set_path);

        //用户注销登录，跳转到登录界面
        exitSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //设置图片下载路径
        setPath.setOnClickListener(v->{
            Toast.makeText(SettingActivity.this,"谢邀，不写了，先写作业",Toast.LENGTH_SHORT).show();
        });
    }
}