package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class ChangePswActivity extends AppCompatActivity {
    private TextInputEditText changeOldPsw;
    private TextInputEditText changeNewPsw;
    private TextInputEditText changecheckPsw;
    private Button changeinfoBtn;
    private TextInputLayout oldPswLayout;
    private TextInputLayout newPswLayout;
    private TextInputLayout checkPswLayout;
    private CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);

        changeOldPsw = findViewById(R.id.changeinfo_oldpassword);
        changeNewPsw = findViewById(R.id.changeinfo_newpassword);
        changeinfoBtn = findViewById(R.id.changeinfo_ok);
        oldPswLayout = findViewById(R.id.oldPsw_layout);
        newPswLayout = findViewById(R.id.newPsw_layout);
        changecheckPsw = findViewById(R.id.checkPsw);
        checkPswLayout = findViewById(R.id.checkPsw_layout);

        //用户修改密码，跳转到登录界面
        SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();

        changeinfoBtn.setOnClickListener(v->{
            //获取旧密码
            String oldPsw = changeOldPsw.getText().toString();
            //获取新密码
            String newPsw = changeNewPsw.getText().toString();
            //获取确认密码
            String checkPsw = changecheckPsw.getText().toString();
            boolean flag = true;
            if(!oldPsw.matches("^[A-Za-z0-9_@]{1,}$")){
                oldPswLayout.setError("旧密码不能为空");
                flag=false;
            }
            if (!newPsw.matches("^[A-Za-z0-9_@]{6,}$") || !checkPsw.matches("^[A-Za-z0-9_@]{6,}$")){
                checkPswLayout.setError("密码必须由至少6位以上的数字、英文、下划线、@组成");
                flag=false;
            }else if (!newPsw.equals(checkPsw)){
                checkPswLayout.setError("两次输入的密码不一致");
                flag=false;
            }

            if (flag){
                Map<String,String> changeInfo = new HashMap<>();
                changeInfo.put("oldpassword",oldPsw);
                changeInfo.put("newpassword",newPsw);

                Gson gson = new Gson();
                String chinfo = gson.toJson(changeInfo);

                mView = new CatLoadingView();
                mView.show(getSupportFragmentManager(), "");
                mView.setCancelable(false);
                mView.setText("正在努力载入中...");
                mView.setBackgroundColor(Color.parseColor("#2CBEA9"));

                try {
                    OkhttpUtils.post("yxyUser/updatePassword", chinfo, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            try {
                                Thread.sleep(1000);

                            } catch (InterruptedException q) {
                                q.printStackTrace();
                            }
                            runOnUiThread(() -> {

                                FancyToast.makeText(ChangePswActivity.this, "由于未知的原因，登录失败，请检查一下您的网络是否有问题",
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
                            //关闭窗口
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //判断状态码 为空不ok
                            if (object.getCode() == null || !object.getCode().equals("200")) {
                                runOnUiThread(() -> {
                                    mView.onDestroyView();
                                    FancyToast.makeText(ChangePswActivity.this, object.getMessage(),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR,
                                            false).show();
                                });
                            } else {
                                runOnUiThread(() -> {
                                    mView.onDestroyView();
                                    FancyToast.makeText(ChangePswActivity.this, object.getMessage(),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false).show();
                                });
                                Intent intent = new Intent(ChangePswActivity.this,LoginActivity.class);
                                //像登录界面传回已改密码的状态  用于取消记住密码
                                intent.putExtra("changedPsw",true);

                                editor.remove("isLogin");
                                editor.clear(); //清除用户的所有信息
                                editor.apply();
                                //销毁主界面 开启新的栈
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }

}