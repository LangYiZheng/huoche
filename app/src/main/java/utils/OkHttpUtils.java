package utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dell004 on 2018/5/3.
 */

public class OkHttpUtils {
    /**
     * 采用单例模式使用OkHttpClient
     */
    private static OkHttpUtils mOkHttpUtilsInstance;
    private static OkHttpClient mClientInstance;
    private Handler mHandler;
    private Gson mGson;
    public static final MediaType JSON = MediaType
            .parse("appliction/json;charset=utf-8");

    /**
     * 单例模式，私有构造函数，构造函数里面进行一些初始化
     */
    private OkHttpUtils() {
        mClientInstance = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        mGson = new Gson();

        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static OkHttpUtils getinstance() {
        if (mOkHttpUtilsInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mOkHttpUtilsInstance == null) {
                    mOkHttpUtilsInstance = new OkHttpUtils();
                }
            }
        }
        return mOkHttpUtilsInstance;
    }

    public Request getRequset(){
        HttpUrl url = new HttpUrl.Builder()
                .scheme("")
                .addQueryParameter("","")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
//        mClientInstance.newCall(request)
        return  request;
    }

    public void get(String url){

        mClientInstance.newCall(getRequset()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void post() {
        Request request = getRequset();
        FormBody.Builder body = new FormBody.Builder();
        body.add("", "");

        mClientInstance.newCall(getRequset().newBuilder().post(body.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * Get异步请求
     * @authur dell003
     * Create at 2018/6/13 9:36
     * @param
     * @return
     * @exception/throws
     * @see
     */
    public void get(String url,NetWorkCallback callback){
        Request request = buildRequestForGet(url);
        requestNetWork(request,callback);
    }

    /**
     * Post表单数据异步请求
     * @authur dell003
     * Create at 2018/6/13 9:36
     * @param
     * @return
     * @exception/throws
     * @see
     */
    public void postWithFormData(String url, Map<String,String> params,NetWorkCallback callback){
        Request request = buildRequestForPostByForm(url,params);
        requestNetWork(request,callback);
    }

    /**
     * Post带Json数据异步请求
     * @authur dell003
     * Create at 2018/6/13 9:41
     * @param 
     * @return 
     * @exception/throws 
     * @see
     */
    public void postWithJson(String url,String json,NetWorkCallback callback){
        Request request = buildRequestForPostByJson(url,json);
        requestNetWork(request,callback);
    }

    /**
     *
     * @authur dell003
     * Create at 2018/6/13 9:41
     * @param
     * @return
     * @exception/throws
     * @see
     */
    private Request buildRequestForPostByJson(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder().url(url).post(body).build();
    }

    /**
     * 封装Post请求所需的Request
     * @authur dell003
     * Create at 2018/6/13 9:42
     * @param
     * @return
     * @exception/throws
     * @see
     */
    private Request buildRequestForPostByForm(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if(null != params){
            for (String key:params.keySet()) {
                builder.add(key,params.get(key));
            }
        }
        FormBody body = builder.build();

        return  new Request.Builder().url(url).post(body).build();
    }

    /**
     * 封装Get请求所需的Request
     * @authur dell003
     * Create at 2018/6/13 9:42
     * @param
     * @return
     * @exception/throws
     * @see
     */
    private Request buildRequestForGet(String url) {
        return new Request.Builder().url(url).get().build();
    }

    /**
     * 网络请求逻辑处理
     * @authur dell003
     * Create at 2018/6/13 9:42
     * @param
     * @return
     * @exception/throws
     * @see
     */
    private void requestNetWork(final Request request,final NetWorkCallback callback) {
        callback.onLoadingBefore(request);
        mClientInstance.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final Exception exception = e;
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        callback.onFailure(request,exception);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Response answer = response;
                String msg = answer.body().string();
                Log.e("onResponse",msg);
                if(response.isSuccessful()){
                    if(callback.mType == String.class){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(answer,msg);
                            }
                        });
                    }else{
                        try {
                            final Object object = mGson.fromJson(msg,callback.mType);
                            mHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    callback.onSuccess(answer,object);
                                }
                            });
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            mHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    callback.onError(answer);
                                }
                            });
                        }
                    }
                }else {
                    mHandler.post(new Runnable(){
                        @Override
                        public void run() {
                            callback.onError(answer);
                        }
                    });
                }
            }
        });
    }
}
