package kilig.ink.yxy.frament;
//广场界面
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.SquareViewEntity;
import kilig.ink.yxy.source.SquareViewAdapter;

public class SquareFragment extends Fragment
{
    SquareViewAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ArrayList<SquareViewEntity> squareList;
    View view;

    //测试数据
    SquareViewEntity entity1 = new SquareViewEntity("1", "1", "https://i.loli.net/2019/06/07/5cfa08739110842627.jpg", "动漫人物", 86, 92, "宋淳", "https://gitforwindows.org/img/git_logo.png", true);
    SquareViewEntity entity2 = new SquareViewEntity("2", "1", "https://hbimg.huabanimg.com/9f40b04f45a7f81409a0b08221f82c1cc4605edd4796d-5nIsQy_fw658/format/webp", "好看的图片", 521, 55, "cc", "https://hbimg.huabanimg.com/bac707919ff201dce07e8597a672047abdec4fed54523-9ddBTa_fw658/format/webp", false);
    SquareViewEntity entity3 = new SquareViewEntity("3", "1", "https://hbimg.huabanimg.com/7664c5a9021856518df37d5633ae6049da481db0d4119-3ahehJ_fw658/format/webp", "非常好看", 23, 231, "chunson", "https://hbimg.huabanimg.com/71290e6035eb9c9c71f395e42cb416c9a77b84cdc20a-IUlccn_fw658/format/webp", true);
    SquareViewEntity entity4 = new SquareViewEntity("4", "1", "https://hbimg.huabanimg.com/65d6e304e78aa9827fe1f76633ea9e66dd0169db77000-BhZb5g_fw658/format/webp", "爱了爱了", 53, 93, "songchun", "https://hbimg.huabanimg.com/9638b6bea1d64e5f0d06a067598fc6b0bcf935c92ab62-762GC0_fw658/format/webp", false);
    SquareViewEntity entity5 = new SquareViewEntity("5", "1", "https://hbimg.huabanimg.com/3a5a14118d10fb31e7a9319594e6ab14c4c1b80ad2e4a-YaIOja_fw658/format/webp", "电脑壁纸", 82, 77, "纯纯", "https://hbimg.huabanimg.com/5ac04527742698185f90faf8c1c06d0af47952535a969-5UvFRN_fw658/format/webp", true);
    SquareViewEntity[] entities = {entity1, entity2, entity3, entity4, entity5};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square, container,false);
        initView();
        initData();
        initEvent();
        return view;
    }

    private void initView()
    {
        refreshLayout = view.findViewById(R.id.refreshLayout_square);
        recyclerView = view.findViewById(R.id.recyclerView_square);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    private void initData()
    {
        squareList = new ArrayList<>();

        squareList.add(entities[new Random().nextInt(entities.length)]);
        squareList.add(entities[new Random().nextInt(entities.length)]);
        squareList.add(entities[new Random().nextInt(entities.length)]);
        squareList.add(entities[new Random().nextInt(entities.length)]);
        squareList.add(entities[new Random().nextInt(entities.length)]);

        adapter = new SquareViewAdapter(getContext(), squareList);
        recyclerView.setAdapter(adapter);
    }

    private void addData()
    {
        squareList.add(0, entities[new Random().nextInt(entities.length)]);
        squareList.add(0, entities[new Random().nextInt(entities.length)]);
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    public void initEvent()
    {
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
//        refreshLayout.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
//        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
//        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                addData();
            }
        });
        // 刷新渐变颜色
        refreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent
        );
    }
}
