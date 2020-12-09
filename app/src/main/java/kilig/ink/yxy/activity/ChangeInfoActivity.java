package kilig.ink.yxy.activity;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView changeIntro;
    private ImageButton backup;
    private CatLoadingView mView;
    private CatLoadingView aView;
    private static final String TAG = "ChangeInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        changeNickname = findViewById(R.id.tvw_changenickname);
        changePsw = findViewById(R.id.tvw_changePsw);
        changeIntro = findViewById(R.id.tvw_changeintro);
        backup = findViewById(R.id.btn_backup_changemassage);

        //修改简介
        changeIntro.setOnClickListener(v->{
            LayoutInflater inflater = this.getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view_intro = inflater.inflate(R.layout.dialog_change_introduce, null);
            EditText edtNewIntro = view_intro.findViewById(R.id.edt_changeintro);
            TextView tv_restnum = view_intro.findViewById(R.id.tv_restnum);

            String newIntro = edtNewIntro.getText().toString();
            //动态监测EditText的输入字数
            edtNewIntro.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tv_restnum.setText(String.valueOf(s.length())+"/20");
                    //Log.d(TAG, "输入字符长度为："+String.valueOf(s.length()));
                    if(s.length()>=20){
                        Toast.makeText(ChangeInfoActivity.this, "字数已达上限了，亲", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setView(view_intro)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

//                            Log.d(TAG, tv_restnum.getText().toString());
//                            Log.d(TAG, newIntro);

                            Map<String,String> introInfo = new HashMap<>();
                            introInfo.put("userIntro",edtNewIntro.getText().toString());
                            Gson gson = new Gson();
                            String intro_info = gson.toJson(introInfo);
                            aView = new CatLoadingView();
                            aView.show(getSupportFragmentManager(), "");
                            aView.setCancelable(false);
                            aView.setText("加载中...");
                            aView.setBackgroundColor(Color.parseColor("#2CBEA9"));
                            try {
                                OkhttpUtils.post("yxyUser/updateUserIntro", intro_info, new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        try {
                                            Thread.sleep(1000);

                                        } catch (InterruptedException q) {
                                            q.printStackTrace();
                                        }
                                        runOnUiThread(() -> {

                                            FancyToast.makeText(ChangeInfoActivity.this, "Error",
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
                                                aView.onDestroyView();
                                                FancyToast.makeText(ChangeInfoActivity.this, object.getMessage(),
                                                        FancyToast.LENGTH_SHORT,
                                                        FancyToast.ERROR,
                                                        false).show();
                                            });
                                        } else {
                                            runOnUiThread(() -> {
                                                aView.onDestroyView();
                                                FancyToast.makeText(ChangeInfoActivity.this, object.getMessage(),
                                                        FancyToast.LENGTH_SHORT,
                                                        FancyToast.SUCCESS,
                                                        false).show();
                                            });
                                        }

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNegativeButton("取消",null);
            // 点击返回不能取消对话框
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#E58981"));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#A2A2AA"));
        });

        //修改昵称
        changeNickname.setOnClickListener(v->{
            LayoutInflater inflater = this.getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view=inflater.inflate(R.layout.dialog_change_nickname, null);
            EditText edtNewNickname = view.findViewById(R.id.edt_changenickname);
            TextView tvnewnicname_num = view.findViewById(R.id.tv_change_restnum);

            String newNickname  = edtNewNickname.getText().toString();
            //动态检测输入框输入字符的长度
            edtNewNickname.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tvnewnicname_num.setText(String.valueOf(s.length())+"/8");
                    //Log.d(TAG, "输入字符长度为："+String.valueOf(s.length()));
                    if(s.length()>=8){
                        Toast.makeText(ChangeInfoActivity.this, "字数已达上限", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setView(view);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Map<String,String> NicknameInfo = new HashMap<>();
                    NicknameInfo.put("nickname",edtNewNickname.getText().toString());
                    Gson gson = new Gson();
                    String nickname_info = gson.toJson(NicknameInfo);
                    mView = new CatLoadingView();
                    mView.show(getSupportFragmentManager(), "");
                    mView.setCancelable(false);
                    mView.setText("加载中...");
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

                                    FancyToast.makeText(ChangeInfoActivity.this, "Error",
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

            // 点击返回不能取消对话框
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            //builder.create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#E58981"));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#A2A2AA"));
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
