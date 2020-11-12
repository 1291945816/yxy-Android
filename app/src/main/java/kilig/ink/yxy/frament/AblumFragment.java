package kilig.ink.yxy.frament;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.MainActivity;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.source.AblumAdapter;

public class AblumFragment extends Fragment {

    private List<AblumItem> itemsList= new ArrayList<>();
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ablum, container,false);
        initData();

        AblumAdapter ablumAdapter = new AblumAdapter(getContext(),itemsList);
        GridView gridView = view.findViewById(R.id.gridView_ablum);
        gridView.setAdapter(ablumAdapter);

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

