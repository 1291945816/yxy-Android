package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import kilig.ink.yxy.R;

public class ChangeInfoActivity extends AppCompatActivity {
    private Button changeNickname;
    private Button changePsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        changeNickname = findViewById(R.id.btn_changenickname);
        changePsw = findViewById(R.id.btn_changePsw);

        //修改昵称
        changeNickname.setOnClickListener(v->{
            Toast.makeText(ChangeInfoActivity.this,"开发中",Toast.LENGTH_SHORT).show();
        });

        //跳转到修改密码界面
        changePsw.setOnClickListener(v->{
            Intent intent = new Intent(ChangeInfoActivity.this,ChangePswActivity.class);
            startActivity(intent);
        });

    }
}