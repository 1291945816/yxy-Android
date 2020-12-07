package kilig.ink.yxy.source;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.InAlbumActivity;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.PhotoItem;

public class InAlbumAdapter extends BaseAdapter {
    private List<PhotoItem> photos;
    private Context mContext;
    private static final String TAG = "InAlbumAdapter";
    public InAlbumAdapter(List<PhotoItem> photos,Context context)
    {
        this.photos=photos;
        mContext=context;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        PhotoItem temp = null;
        if(photos != null)
        {
            temp = photos.get(position);
        }
        return temp;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPhoto viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolderPhoto();
            Log.d(TAG, "getView: "+mContext);
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_photo, null);


            viewHolder.photo = convertView.findViewById(R.id.item_photo);
            viewHolder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(v.getContext(), InAlbumActivity.class);
                    //v.getContext().startActivity(intent);
                }
            });

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderPhoto) convertView.getTag();
        }

        PhotoItem item= (PhotoItem) getItem(position);
        viewHolder.photo.setImageResource(item.getResourceId());


        return convertView;
    }

    static class ViewHolderPhoto
    {
        ImageView photo;
    }
}
