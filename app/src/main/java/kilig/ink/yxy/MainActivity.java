package kilig.ink.yxy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button rigister;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rigister =(Button) findViewById(R.id.btn_rigister);
        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        rigister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_rigister:
                Intent intent = new Intent(MainActivity.this,RigisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                Toast.makeText(MainActivity.this,"开发中",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}