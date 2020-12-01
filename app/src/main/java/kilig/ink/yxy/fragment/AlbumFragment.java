package kilig.ink.yxy.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.source.AlbumAdapter;

public class AlbumFragment extends Fragment {

    private List<AblumItem> itemsList= new ArrayList<>();
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container,false);
        initData();

        AlbumAdapter albumAdapter = new AlbumAdapter(getContext(),itemsList);
        GridView gridView = view.findViewById(R.id.gridView_album);
        gridView.setAdapter(albumAdapter);

        // 添加相册
        com.google.android.material.floatingactionbutton.FloatingActionButton button = view.findViewById(R.id.floating_action_button_album);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(getContext());
                inputServer.setFocusable(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("输入相册名称")
                        .setIcon(R.drawable.androidicon)
                        .setView(inputServer)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputName = inputServer.getText().toString();
                                AblumItem ablumItem = new AblumItem();
                                ablumItem = new AblumItem();
                                ablumItem.setmImageId(R.drawable.cat);
                                ablumItem.setmName(inputName);
                                ablumItem.setmNum(0);
                                itemsList.add(ablumItem);
                            }
                        });
                builder.setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String inputName = inputServer.getText().toString();
                            }
                        });
                builder.show();
            }
        });

        initData();

        return view;
    }

    public void initData()
    {
        AblumItem ablumItem = new AblumItem();
        ablumItem.setmImageId(R.drawable.yishutishen);
        ablumItem.setmName("异鼠替身");
        ablumItem.setmNum(100);
        itemsList.add(ablumItem);

        ablumItem = new AblumItem();
        ablumItem.setmImageId(R.drawable.cat);
        ablumItem.setmName("猫");
        ablumItem.setmNum(3);
        itemsList.add(ablumItem);
    }

}

