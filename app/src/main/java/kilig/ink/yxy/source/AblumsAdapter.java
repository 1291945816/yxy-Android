package kilig.ink.yxy.source;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;


public class AblumsAdapter extends RecyclerView.Adapter<AblumsAdapter.ViewHolder> {

    private List<AblumItem> albumDatas;
    private Context context;


    private static final String TAG = "AblumsAdapter";
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent,false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        AblumItem item = albumDatas.get(position);
        holder.albumName.setText(item.getmName());
        holder.pictureNum.setText(item.getmNum()+" 张");


        //设置删除响应事件
        holder.delete.setOnClickListener(v->{
            Log.d(TAG, "1");
        });

        //设置修改响应事件


    }

    public AblumsAdapter(Context context,List<AblumItem> albumDatas) {
        this.context=context;
        this.albumDatas=albumDatas;
    }

    @Override
    public int getItemCount() {
        return albumDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView dImage;
        TextView albumName;
        TextView pictureNum;
        MaterialButton delete;
       MaterialButton change;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dImage = itemView.findViewById(R.id.fist_picture);
            albumName = itemView.findViewById(R.id.ablum_name);
            pictureNum = itemView.findViewById(R.id.nums);
            change = itemView.findViewById(R.id.changeName);
            delete= itemView.findViewById(R.id.delete);

        }

    }



}
