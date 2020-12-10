package kilig.ink.yxy.fragment;
//广场界面
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.entity.ImageEntity;
import kilig.ink.yxy.source.SquareViewAdapter;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SquareFragment extends Fragment
{
    SquareViewAdapter adapter;
    RecyclerView recyclerView;
    RefreshLayout refreshLayout;
    ArrayList<ImageEntity> squareList;
    View view;
    String json;
    int pageNum = 0;
    private static final int pageSize = 7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_square, container,false);
        initView();
        initData();
        return view;
    }

    private void initView()
    {
        refreshLayout = view.findViewById(R.id.refreshLayout_square);
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                addDataToTop();
                refreshlayout.finishRefresh(400/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener()
        {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout)
            {
                addDataToBottom();
                refreshlayout.finishLoadMore(400);//传入false表示加载失败
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView_square);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    private void initData()
    {
        Map<String, String> map = new HashMap<>();
        pageNum = 0; //重新初始化数据，避免保存上次的浏览记录导致再次切换时会在上一次的基础上进行刷新7
        map.put("pageNum", String.valueOf(pageNum));
        ++pageNum;
        map.put("size", String.valueOf(pageSize));
        squareList = new ArrayList<>();
        OkhttpUtils.get("picture/pictures", map, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                 json = response.body().string();
//                 Log.e("json", json);
                 Type type = new TypeToken<ResponeObject<List<ImageEntity>>>(){}.getType();
                ResponeObject<ArrayList<ImageEntity>> responeObject = new Gson().fromJson(json, type);
                if (responeObject.getCode().equals("200")) {
                    squareList.addAll(responeObject.getData());
                    new Handler(Looper.getMainLooper()).post(() -> {
                        adapter.notifyDataSetChanged();
                    });
                }
            }
        });
        adapter = new SquareViewAdapter(getContext(), squareList);
        recyclerView.setAdapter(adapter);
    }

    private void addDataToTop()
    {
        adapter.notifyDataSetChanged();
    }

    private void addDataToBottom()
    {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(pageNum));
        ++pageNum;
        map.put("size", String.valueOf(pageSize));
        OkhttpUtils.get("picture/pictures", map, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                json = response.body().string();
                Log.e("json", json);
                Type type = new TypeToken<ResponeObject<List<ImageEntity>>>(){}.getType();
                ResponeObject<ArrayList<ImageEntity>> responeObject = new Gson().fromJson(json, type);
                squareList.addAll(responeObject.getData());
                new Handler(Looper.getMainLooper()).post(()->{
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }
}