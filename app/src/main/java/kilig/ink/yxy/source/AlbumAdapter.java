package kilig.ink.yxy.source;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.InAlbumActivity;
import kilig.ink.yxy.entity.AblumItem;

public class AlbumAdapter extends BaseAdapter
{
    private List<AblumItem> mAblumData;
    private Context mContext;
    private int resourceId;

    public AlbumAdapter(Context context, List<AblumItem> Items)
    {
        mContext = context;
        mAblumData = Items;
    }

    @Override
    public int getCount() {
        int num=0;
        if(mAblumData!=null)
        {
            num = mAblumData.size();
        }
        return num;
    }

    @Override
    public Object getItem(int position) {
        AblumItem item = null;
        if(mAblumData!=null)
        {
            item=mAblumData.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderInAblum viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolderInAblum();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_album, null);

            viewHolder.itemButton = convertView.findViewById(R.id.album_item);
            viewHolder.tv_title = convertView.findViewById(R.id.album_title);
            viewHolder.tv_num = convertView.findViewById(R.id.album_num);
            viewHolder.itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), InAlbumActivity.class);
                    v.getContext().startActivity(intent);
                }
            });

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderInAblum) convertView.getTag();
        }

        AblumItem item= (AblumItem) getItem(position);
        viewHolder.tv_num.setText(Integer.toString(item.getmNum()));
        viewHolder.tv_title.setText(item.getmName());
        viewHolder.itemButton.setImageResource(item.getmImageId());

        return convertView;
    }


    static class ViewHolderInAblum
    {
        ImageButton itemButton;
        TextView tv_title;
        TextView tv_num;
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        AblumItem item=getItem(position);
//        tv_num.setText(Integer.toString(item.getmNum()));
//        tv_title.setText(item.getmName());
//        iv_item.setImageResource(item.getmImageId());
//        return view;
//    }
}
