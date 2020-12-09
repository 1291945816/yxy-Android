package kilig.ink.yxy.utils;

import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kilig.ink.yxy.entity.CookiesManager;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者:kilig
 */

public  class OkhttpUtils {
    /**
     * 请求基地址
     */
    public static final String BASE_URL="http://yxyserver.kilig.ink/";

    public static OkHttpClient okHttpClient=new OkHttpClient.Builder().cookieJar(new CookiesManager()).build();

    private static String token="";

    private static final String TAG = "OkhttpUtils";
    /**
     * 设置token
     * @param newtoken 传进来的新的token
     */
    public static void setToken(String newtoken) {
        token = newtoken;
    }

    public static String getToken() {
        return token;
    }

    //普通的get请求
    public static void get(String url, Map<String,String> queryParams, Callback callback){
        Request.Builder builder = new Request.Builder()
                .url(OkhttpUtils.BASE_URL + url); //添加url
        Request request = builder.addHeader("token", OkhttpUtils.token).build();//携带token
        Log.d(TAG, "get: "+token);
        //构建一个参数的url
        final HttpUrl.Builder newBuilder = request.url().newBuilder();
        if (queryParams != null) {
            for (Map.Entry<String,String> entry:queryParams.entrySet()){
                newBuilder.addQueryParameter(entry.getKey(),entry.getValue());
            }
        }

        //构建完成
        Request last_request = builder.url(newBuilder.build()).build();
        okHttpClient.newCall(last_request).enqueue(callback);
    }

    /**
     * JSON数据格式请求
     * @param url
     *
     * @param json
     * @return
     */
    public static void post(String url, String json, Callback callback) throws IOException {
        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(BASE_URL+url).build();

        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();
        headerBuilder.add("token",token);
        // application/octet-stream
        RequestBody requestBody = FormBody.create(json, MediaType.parse("application/json"));

        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(requestBody);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    //上传头像
    public static  void postWithBody(String url, Map<String,String> fileInfo, FileProgressRequestBody.ProgressListener listener, Callback callback) throws IOException{
        String filePath = fileInfo.get("filePath");
        String fileName = fileInfo.get("fileName");

        File file = new File(filePath);
        //RequestBody requestBody = FormBody.create(file, );
        FileProgressRequestBody fileProgressRequestBody = new FileProgressRequestBody(file, "application/form-data;charset=utf-8", listener);
        MultipartBody file1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, fileProgressRequestBody ).build();
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(BASE_URL+url).build();
        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();
        headerBuilder.add("token",token);
        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(file1);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    public static void postPictureWithFile(String url ,Map<String,String> uploadInfo,FileProgressRequestBody.ProgressListener listener,Callback callback)throws IOException{
        String pictureInfo = uploadInfo.get("pictureInfo");
        String ablumId = uploadInfo.get("ablumId");
        String pictureName = uploadInfo.get("pictureName");
        String visiable = uploadInfo.get("publishVisiable");
        String filePath = uploadInfo.get("filePath");
        File file = new File(filePath);
        FileProgressRequestBody fileProgressRequestBody = new FileProgressRequestBody(file, "application/form-data;charset=utf-8", listener);
        MultipartBody build = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", Uri.decode(pictureName+file.getName().substring(file.getName().lastIndexOf("."))), fileProgressRequestBody)
                .addFormDataPart("pictureName", pictureName)
                .addFormDataPart("publishVisiable", visiable)
                .addFormDataPart("ablumId", ablumId)
                .addFormDataPart("pictureInfo", pictureInfo)
                .build();

        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(BASE_URL+url).build();
        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();
        headerBuilder.add("token",token);
        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(build);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

}
