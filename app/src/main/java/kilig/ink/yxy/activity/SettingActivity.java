package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
        SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
        //用户注销登录，跳转到登录界面
        exitSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                editor.remove("isLogin");
                editor.clear(); //清除用户的所有信息
                editor.apply();
                //销毁主界面 开启新的栈
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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