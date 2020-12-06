package kilig.ink.yxy.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.entity.SpacesItemDecoration;
import kilig.ink.yxy.source.AlbumsAdapter;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlbumFragment extends Fragment {

    private static final String TAG = "AlbumFragment";

    private List<AblumItem> itemsList= new ArrayList<>();
    private AlbumsAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container,false);

        RecyclerView recyclerView = this.view.findViewById(R.id.recycler_album);
        refreshLayout=view.findViewById(R.id.layout_swipe_refresh);
         adapter = new AlbumsAdapter(getContext(),itemsList);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(18));
        recyclerView.setAdapter(adapter);
        initData();
        refreshLayout.setOnRefreshListener(()->{
            initData();
            refreshLayout.setRefreshing(false);
        });


        // 添加相册
        com.google.android.material.floatingactionbutton.FloatingActionButton button = this.view.findViewById(R.id.floating_action_button_album);
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
                                Map<String,String> body=new HashMap<>();
                                body.put("ablumName",inputServer.getText().toString());
                                Gson gson = new Gson();
                                String json = gson.toJson(body);

                                //在这里发起一个请求
                                try {
                                    OkhttpUtils.post("ablum/add", json, new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            //**这里暂时不做处理

                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            String responData = response.body().string();
                                            ResponeObject responeObject = gson.fromJson(responData, ResponeObject.class);
                                            getActivity().runOnUiThread(()->{
                                                Toast.makeText(getActivity(),responeObject.getMessage(),Toast.LENGTH_SHORT).show();
                                            });

                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                builder.setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"成功取消",Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });

        initData();

        return this.view;
    }

    /**
     * 请求数据
     */
    public void initData()
    {
        OkhttpUtils.get("ablum/info", null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**
                 * 这里另外处理
                 */
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Type type = new TypeToken<ResponeObject<List<AblumItem>>>(){}.getType();
                ResponeObject<List<AblumItem>> responeObject = new Gson().fromJson(json, type);
                List<AblumItem> data = responeObject.getData();
                if(responeObject.getCode().equals("200")){
                    itemsList.clear();
                    itemsList.addAll(data);
                    getActivity().runOnUiThread(()->{
                        adapter.notifyDataSetChanged();
                    });
                }

            }
        });

    }


}

