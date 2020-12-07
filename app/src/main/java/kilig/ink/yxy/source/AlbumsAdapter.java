package kilig.ink.yxy.source;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import kilig.ink.yxy.R;
import kilig.ink.yxy.activity.InAlbumActivity;
import kilig.ink.yxy.entity.AblumItem;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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
                    .setMessage("你确定要删除这个相册吗?(会删除相册的照片!)")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String,Object> datas=new HashMap<>();
                            datas.put("ablumId",item.getAblumId());
                            Gson gson = new Gson();
                            String json = gson.toJson(datas);
                            try {
                                OkhttpUtils.post("ablum/delete",json, new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        /**
                                         * 先不做处理
                                         */
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        String string = response.body().string();
                                        ResponeObject object = gson.fromJson(string, ResponeObject.class);
                                        if (object != null){
                                            ((Activity)context).runOnUiThread(()->{
                                                Toast.makeText(context,object.getMessage(),Toast.LENGTH_SHORT).show();
                                            });

                                        }

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


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
                    .setNegativeButton("修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String,Object> json=new HashMap<>();
                            json.put("ablumId",item.getAblumId());
                            json.put("newAblumName",editText.getText().toString());
                            Gson gson = new Gson();
                            String s = gson.toJson(json);

                            try {
                                OkhttpUtils.post("ablum/change", s, new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        /**
                                         * 先不处理
                                         */

                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        String s1 = response.body().string();
                                        ResponeObject dataObject = gson.fromJson(s1, ResponeObject.class);
                                        if (dataObject != null){
                                            ((Activity)context).runOnUiThread(()->{
                                                Toast.makeText(context,dataObject.getMessage(),Toast.LENGTH_SHORT).show();
                                                item.setAblumName(editText.getText().toString());
                                                notifyDataSetChanged();
                                            });


                                        }


                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    })
                    .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"取消成功~",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });

        //整体添加监听器
        holder.layout.setOnClickListener(v->{

            Intent toInAlbum = new Intent(((Activity) context), InAlbumActivity.class);
            toInAlbum.putExtra("album",item);
            context.startActivity(toInAlbum);
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
