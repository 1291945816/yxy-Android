package kilig.ink.yxy.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import kilig.ink.yxy.R;
import kilig.ink.yxy.fragment.MineFragment;

public class SettingActivity extends AppCompatActivity {
    private int checkedItem = 0;//默认选中的Item
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button exitSystem = findViewById(R.id.btn_exit);
        TextView setPath = findViewById(R.id.set_path);
        ImageButton backup = findViewById(R.id.btn_backup_setting);
        //获取SharedPreferences对象
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        //取出当前Item的状态
        checkedItem = pref.getInt("defaultItem",0);

        //设置图片下载路径
        setPath.setOnClickListener(v->{
            //Toast.makeText(SettingActivity.this,"谢邀，不写了，先写作业",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择存储位置");
            String[] path = {"手机内存卡","外置内存卡"};
            builder.setSingleChoiceItems(path, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    editor = pref.edit();
                    //存储当前选择的Item的状态
                    if(which != checkedItem){//状态改变了，则存储
                        editor.putInt("defaultItem",which);
                    }
                    else {//否则清空
                        editor.clear();
                    }
                    editor.commit();
                    checkedItem = which;
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(SettingActivity.this,"你点击了确定",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("取消",null);
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

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

        //返回键
        backup.setOnClickListener(v->{finish();});
    }
}