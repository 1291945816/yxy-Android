package kilig.ink.yxy.frament;
//广场界面
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Entity;

import java.util.ArrayList;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pubulish_fragment, container,false);
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
        SquareViewEntity entity = new SquareViewEntity("1", "https://i.loli.net/2019/06/07/5cfa08739110842627.jpg", "动漫人物", 1, "宋淳", "https://gitforwindows.org/img/git_logo.png");
        SquareViewEntity entity2 = new SquareViewEntity("2", "https://i.loli.net/2020/10/21/UPfIW3MGgxOqsYS.png", "环境变量", 1, "宋淳", "https://gitforwindows.org/img/git_logo.png");
        SquareViewEntity entity3 = new SquareViewEntity("2", "https://i.loli.net/2020/10/21/pcLmKRCPx9zl2TO.png", "Debug", 1, "宋淳", "https://gitforwindows.org/img/git_logo.png");
        squareList.add(entity);
        squareList.add(entity2);
        squareList.add(entity3);
        squareList.add(entity);
        squareList.add(entity2);
        squareList.add(entity3);
        adapter = new SquareViewAdapter(getContext(), squareList);
        recyclerView.setAdapter(adapter);
    }

    private void addData()
    {

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
