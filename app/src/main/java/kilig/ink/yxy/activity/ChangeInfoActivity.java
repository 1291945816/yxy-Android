package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChangeInfoActivity extends AppCompatActivity {
    private TextView changeNickname;
    private TextView changePsw;
    private ImageButton backup;
    private CatLoadingView mView;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        changeNickname = findViewById(R.id.tvw_changenickname);
        changePsw = findViewById(R.id.tvw_changePsw);
        backup = findViewById(R.id.btn_backup_changemassage);

        //修改昵称
        changeNickname.setOnClickListener(v->{
            LayoutInflater inflater = this.getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            view=inflater.inflate(R.layout.dialog_change_nickname, null);
            builder.setView(view);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText edtNewNickname = view.findViewById(R.id.edt_changenickname);
                    String newNickname  = edtNewNickname.getText().toString();
                    Map<String,String> NicknameInfo = new HashMap<>();
                    NicknameInfo.put("nickname",newNickname);
                    Gson gson = new Gson();
                    String nickname_info = gson.toJson(NicknameInfo);
                    mView = new CatLoadingView();
                    mView.show(getSupportFragmentManager(), "");
                    mView.setCancelable(false);
                    mView.setText("正在努力载入中...");
                    mView.setBackgroundColor(Color.parseColor("#2CBEA9"));
                    Log.d("121", "onClick: "+OkhttpUtils.getToken());
                    try {
                        OkhttpUtils.post("yxyUser/updateNickname", nickname_info, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                try {
                                    Thread.sleep(1000);

                                } catch (InterruptedException q) {
                                    q.printStackTrace();
                                }
                                runOnUiThread(() -> {

                                    FancyToast.makeText(ChangeInfoActivity.this, "由于未知的原因，登录失败，请检查一下您的网络是否有问题",
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
                                ResponeObject object = gson.fromJson(s, ResponeObject.class);
                                //判断状态码
                                if (object.getCode() == null || !object.getCode().equals("200")) {
                                    runOnUiThread(() -> {
                                        mView.onDestroyView();
                                        FancyToast.makeText(ChangeInfoActivity.this, object.getMessage(),
                                                FancyToast.LENGTH_SHORT,
                                                FancyToast.ERROR,
                                                false).show();
                                    });
                                } else {
                                    runOnUiThread(() -> {
                                        mView.onDestroyView();
                                        FancyToast.makeText(ChangeInfoActivity.this, object.getMessage(),
                                                FancyToast.LENGTH_SHORT,
                                                FancyToast.SUCCESS,
                                                false).show();
                                    });
                                }
                                /*MineFragment mineFragment = new MineFragment();
                                Intent intent = new Intent(ChangeInfoActivity.this, mineFragment.getClass());
                                startActivity(intent);*/
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("取消",null);

            // 点击返回不会取消对话框
            builder.setCancelable(false);

            builder.create();
            builder.show();
        });

        //跳转到修改密码界面
        changePsw.setOnClickListener(v->{
            Intent intent = new Intent(ChangeInfoActivity.this,ChangePswActivity.class);
            startActivity(intent);
        });

        //返回键
        backup.setOnClickListener(v->{finish();});

    }
}