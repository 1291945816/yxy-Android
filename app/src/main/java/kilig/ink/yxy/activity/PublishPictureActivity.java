package kilig.ink.yxy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import kilig.ink.yxy.R;

public class PublishPictureActivity extends AppCompatActivity {

    private TextInputEditText pictureName;
    private TextInputEditText pictureIntro;
    private MaterialButton selectFile;
    private SwitchMaterial switchMaterial;
    private Button upload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_picture);
        pictureName=findViewById(R.id.picture_name);
        selectFile=findViewById(R.id.selectFile);
        pictureIntro=findViewById(R.id.picture_intro);
        switchMaterial=findViewById(R.id.is_publish);
        upload=findViewById(R.id.upload);








    }
}