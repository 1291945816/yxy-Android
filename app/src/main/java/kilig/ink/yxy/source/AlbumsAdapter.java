package kilig.ink.yxy.source;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.AblumItem;


public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder>  {

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

        AblumItem item = albumDatas.get(position);
        Log.d(TAG, "onBindViewHolder: "+item);
        holder.albumName.setText(item.getAblumName());
        holder.pictureNum.setText(item.getNums()+" 张");

        //设置删除响应事件
        holder.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this.context)
                    .setTitle("删除提示")
                    .setMessage("你确定要删除这个相册吗?")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItem(position);
                        }
                    })
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"取消成功~",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setIcon(R.drawable.warn_album_delete)
                    .show();
        });

        //设置修改响应事件
        holder.change.setOnClickListener(v->{
            final TextInputEditText editText=new TextInputEditText(context);
            editText.setClipToOutline(true);
            new MaterialAlertDialogBuilder(context)
                    .setTitle("修改相册名称")
                    .setIcon(R.drawable.ic_change_img)
                    .setView(editText)
                    .setNegativeButton("修改",null)
                    .setPositiveButton("取消",null)
                    .show();
        });

        //整体添加监听器
        holder.layout.setOnClickListener(v->{
            Toast.makeText(context,"你点击了"+item.getAblumName(),Toast.LENGTH_SHORT).show();
        });


    }

    public AlbumsAdapter(Context context, List<AblumItem> albumDatas) {
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
       ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dImage = itemView.findViewById(R.id.fist_picture);
            albumName = itemView.findViewById(R.id.ablum_name);
            pictureNum = itemView.findViewById(R.id.nums);
            change = itemView.findViewById(R.id.changeName);
            delete= itemView.findViewById(R.id.delete);
            layout=itemView.findViewById(R.id.item_album_);
        }

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.delete:
//                new MaterialAlertDialogBuilder(this.context)
//                        .setTitle("删除提示")
//                        .setMessage("你确定要删除这个相册吗?")
//                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeItem(currentPosition);
//                            }
//                        })
//                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(context,"取消成功~",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setIcon(R.drawable.warn_album_delete)
//                        .show();
//                break;
//            case R.id.changeName:
//                final TextInputEditText editText=new TextInputEditText(context);
//                editText.setClipToOutline(true);
//                new MaterialAlertDialogBuilder(context)
//                        .setTitle("修改相册名称")
//                        .setIcon(R.drawable.ic_change_img)
//                        .setView(editText)
//                        .setNegativeButton("修改",null)
//                        .setPositiveButton("取消",null)
//                        .show();
//                break;
//            case R.id.item_album_:
//                Toast.makeText(context,"你点击了"+albumName,Toast.LENGTH_SHORT).show();
//        }
//
//    }

    /**
     * 删除子项
     * @param position 子项的位置
     */
    public void removeItem(int position){
        albumDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }







}
