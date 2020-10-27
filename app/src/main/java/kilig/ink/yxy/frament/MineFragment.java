package kilig.ink.yxy.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;

public class MineFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mine,container,false);
        Button exit = view.findViewById(R.id.btn_exit);
        ImageView imgProfile = view.findViewById(R.id.img_profile);

        //加载头像
        Glide.with(getActivity()).
                load("https://hbimg.huabanimg.com/71290e6035eb9c9c71f395e42cb416c9a77b84cdc20a-IUlccn_fw658/format/webp")
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation())) //头像变圆
                .into(imgProfile);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getActivity().finish();
            }
        });

        return view;
    }
}
