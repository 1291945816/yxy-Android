package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import kilig.ink.yxy.R;
import kilig.ink.yxy.utils.FragmentEnum;
import kilig.ink.yxy.utils.FragmentSingleton;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView=null;
    private TextView topBar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topBar=findViewById(R.id.topTitle);
        replaceFragment(FragmentSingleton.getFragment(FragmentEnum.PUBLISH));
        topBar.setText(FragmentEnum.PUBLISH.getName());
        bottomNavigationView=findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.album:
                {
                    replaceFragment(FragmentSingleton.getFragment(FragmentEnum.ABLUM));
                    topBar.setText(FragmentEnum.ABLUM.getName());
                    break;
                }
                case R.id.publish:{
                    replaceFragment(FragmentSingleton.getFragment(FragmentEnum.PUBLISH));
                    topBar.setText(FragmentEnum.PUBLISH.getName());
                    break;
                }
                case R.id.my:{
                   replaceFragment(FragmentSingleton.getFragment(FragmentEnum.MY));
                   topBar.setText(FragmentEnum.MY.getName());
                   break;
                }


            }
            return true;
        });




    }
    private void replaceFragment(Fragment fragment){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }
}