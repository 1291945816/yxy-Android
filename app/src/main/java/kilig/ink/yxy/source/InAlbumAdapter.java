package kilig.ink.yxy.source;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import javax.security.auth.login.LoginException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kilig.ink.yxy.R;
import kilig.ink.yxy.entity.InalbumPicture;
import kilig.ink.yxy.entity.ResponeObject;
import kilig.ink.yxy.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class InAlbumAdapter extends RecyclerView.Adapter<InAlbumAdapter.ViewHolder> {
    private Context context;
    private List<InalbumPicture> dataList;
    private Listener listener;
    private UpdateListener updateListener;
    private DeleteListener deleteListener;

    @NonNull
    @Override
    public InAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InAlbumAdapter.ViewHolder holder, int position) {

        InalbumPicture picture = dataList.get(position);
        if (!picture.isPublish())
            holder.publishView.setImageResource(R.drawable.album_star_white);
        else
            holder.publishView.setImageResource(R.drawable.album_star_red);
        Glide.with(context)
                .load(picture.getThumbnailUrl())
                .skipMemoryCache(true)
                .into(holder.imageView);
        holder.detail.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.detail);
            popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String info = "";
                    switch (item.getItemId()) {
                        case R.id.publish:
                            updateListener.updateStatus(position, picture);
                            break;
                        case R.id.detail:
                            info = "图片详情";
                            OkhttpUtils.get("picture/info/" + picture.getId(), null, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    FancyToast.makeText(context, "查看失败", FancyToast.INFO, FancyToast.LENGTH_SHORT, false);
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    //Log.e("response", response.body().string());
                                    Gson gson = new GsonBuilder().serializeNulls().create();
                                    ResponeObject responeObject = gson.fromJson(response.body().string(), ResponeObject.class);
                                    if (!responeObject.getCode().equals("200")) {
                                        Log.e("CheckFail", responeObject.getCode());
                                        return;
                                    }
                                    String data = gson.toJson(responeObject.getData());
                                    Map<String, Object> map = gson.fromJson(data, Map.class);
                                    ((Activity) context).runOnUiThread(() -> {
                                        new MaterialAlertDialogBuilder(context)
                                                .setTitle("图片详情")
                                                .setIcon(R.drawable.androidicon)
                                                .setMessage(
                                                        "图片名称：" + map.get("pictureName") + " \n" +
                                                                "图片描述：" + map.get("pictureInfo") + " \n" +
                                                                "点赞次数：" + Math.round((double)map.get("starNum")) + " \n" +
                                                                "下载次数：" + Math.round((double)map.get("downloadSum")) + " \n" +
                                                                "作者账号：" + map.get("yxyUserName") + " \n" +
                                                                "作者名称：" + map.get("yxyNickName") + " \n" +
                                                                "创建时间：" + stampToDate(map.get("pictureCreateTime").toString()) + " \n" +
                                                                "").show();
                                    });
                                }
                            });

                            break;
                        case R.id.star:
                            info = "谁点赞了";
                            OkhttpUtils.get("ablum/starInfo?pictureId=" + picture.getId(), null, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    FancyToast.makeText(context, "查看失败", FancyToast.INFO, FancyToast.LENGTH_SHORT, false);
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    Log.e("response", response.body().toString());
                                    Gson gson = new GsonBuilder().serializeNulls().create();
                                    ResponeObject responeObject = gson.fromJson(response.body().string(), ResponeObject.class);
                                    if (!responeObject.getCode().equals("200")) {
                                        Log.e("CheckFail", responeObject.getCode());
                                        return;
                                    }
                                    String data = gson.toJson(responeObject.getData());
                                    Map[] resultBean = new Gson().fromJson(data,Map[].class);
                                    String Names= "";
                                    for(Map user:resultBean)
                                    {
                                        Names+=user.get("yxyNickName")+"，";
                                    }
                                    Names=Names.substring(0, Names.length()-1);
                                    String finalNames = Names;
                                    ((Activity) context).runOnUiThread(() -> {
                                        new MaterialAlertDialogBuilder(context)
                                                .setMessage(finalNames +"点赞了这张图片").show();
                                    });
                                }
                            });
                            break;
                        case R.id.comment:
                            info = "谁评论了";
                            OkhttpUtils.get("picture/comment?pictureId=" + picture.getId(), null, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    FancyToast.makeText(context, "查看失败", FancyToast.INFO, FancyToast.LENGTH_SHORT, false);
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    Log.e("response", response.body().toString());
                                    Gson gson = new GsonBuilder().serializeNulls().create();
                                    ResponeObject responeObject = gson.fromJson(response.body().string(), ResponeObject.class);
                                    if (!responeObject.getCode().equals("200")) {
                                        Log.e("CheckFail", responeObject.getCode());
                                        return;
                                    }
                                    String data = gson.toJson(responeObject.getData());
                                    Map[] resultBean = new Gson().fromJson(data,Map[].class);
                                    Arrays.sort(resultBean, new Comparator<Map>() {
                                        @Override
                                        public int compare(Map o1, Map o2) {
                                            try {
                                                if( dateToStamp((String) o1.get("comment_time"),"yyyy-MM-dd HH:mm:ss")>dateToStamp((String) o2.get("comment_time"),"yyyy-MM-dd HH:mm:ss"))
                                                    return 1;
                                                else
                                                    return -1;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });

                                    String comments= "";
                                    for(Map user:resultBean)
                                    {
                                        comments+=user.get("yxyNickName")+"："+user.get("comment")+"\n";
                                    }
                                    comments=comments.substring(0, comments.length()-1);
                                    String finalNames = comments;
                                    ((Activity) context).runOnUiThread(() -> {
                                        new MaterialAlertDialogBuilder(context)
                                                .setMessage(finalNames).show();
                                    });
                                }
                            });
                            break;
                        case R.id.delete:
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("删除提示")
                                    .setIcon(R.drawable.warn_album_delete)
                                    .setMessage("你确定要删除这个图片吗?")
                                    .setPositiveButton("狠心删除", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.d("11", "onClick: " + deleteListener);
                                            deleteListener.deletePicture(position, picture); //删除相片
                                        }
                                    }).setNegativeButton("不了，误操作", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity) context).runOnUiThread(() -> {
                                        FancyToast.makeText(context, "取消成功~", FancyToast.INFO, FancyToast.LENGTH_SHORT, false);
                                    });

                                }
                            }).show();
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.setItemOnclick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView publishView;
        ImageButton detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_photo);
            publishView = itemView.findViewById(R.id.publish);
            detail = itemView.findViewById(R.id.dropdown_menu_photo);
        }

    }

    public InAlbumAdapter(Context context, List<InalbumPicture> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public interface Listener {
        void setItemOnclick(int position);
    }

    public interface UpdateListener {
        void updateStatus(int pos, InalbumPicture picture);
    }

    public interface DeleteListener {
        void deletePicture(int pos, InalbumPicture picture);
    }

    public void invokeListener(Listener listener) {
        this.listener = listener;
    }

    public void invokeUListener(UpdateListener listener) {
        this.updateListener = listener;
    }

    public void invokeDListener(DeleteListener listener) {
        this.deleteListener = listener;
    }

    /**
     * 删除子项
     *
     * @param position 子项的位置
     */
    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s,String pattern) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}
