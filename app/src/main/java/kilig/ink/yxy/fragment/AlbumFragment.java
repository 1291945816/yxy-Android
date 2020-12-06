package kilig.ink.yxy.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.SpacesItemDecoration;
import kilig.ink.yxy.source.AlbumsAdapter;

public class AlbumFragment extends Fragment {

    private static final String TAG = "AlbumFragment";

    private List<AblumItem> itemsList= new ArrayList<>();
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container,false);
        initData();
        Log.d(TAG, "onCreateView: "+itemsList.size());
        RecyclerView recyclerView = this.view.findViewById(R.id.recycler_album);
        AlbumsAdapter adapter = new AlbumsAdapter(getContext(),itemsList);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(18));
        recyclerView.setAdapter(adapter);

//        // 添加相册
//        com.google.android.material.floatingactionbutton.FloatingActionButton button = this.view.findViewById(R.id.floating_action_button_album);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final EditText inputServer = new EditText(getContext());
//                inputServer.setFocusable(true);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("输入相册名称")
//                        .setIcon(R.drawable.androidicon)
//                        .setView(inputServer)
//                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String inputName = inputServer.getText().toString();
//                                AblumItem ablumItem = new AblumItem();
//                                ablumItem = new AblumItem();
//                                ablumItem.setmImageId(R.drawable.cat);
//                                ablumItem.setmName(inputName);
//                                ablumItem.setmNum(0);
//                                itemsList.add(ablumItem);
//                            }
//                        });
//                builder.setPositiveButton("取消",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                String inputName = inputServer.getText().toString();
//                            }
//                        });
//                builder.show();
//            }
//        });

        initData();

        return this.view;
    }

    public void initData()
    {

            AblumItem ablumItem = new AblumItem();
            ablumItem.setmImageId(111);
            ablumItem.setmName("异鼠替身");
            ablumItem.setmNum(20);
            itemsList.add(ablumItem);

    }

}

