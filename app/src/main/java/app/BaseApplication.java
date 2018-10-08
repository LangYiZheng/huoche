package app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import bean.UserBean;


/**
 * 自定义应用入口
 *
 * @author Ht
 */
public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    private static List<Activity> mActivityList = new LinkedList<>();
    private static UserBean user = new UserBean();
    private boolean isLogin = false;
    public static String loginTime = "";
    public static String pushTrainId;
    ConnectivityManager connMgr;
    boolean wifiConnected, mobileConnected;
    public static ArrayList<ArrayList<String>> postList = new ArrayList<>();

    public void initPostList(){
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("A1");
        list1.add("S1");
        postList.add(list1);
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("A2");
        list2.add("S1");
        postList.add(list2);
        ArrayList<String> list3 = new ArrayList<>();
        list3.add("A3");
        list3.add("S2");
        postList.add(list3);
        ArrayList<String> list4 = new ArrayList<>();
        list4.add("A4");
        list4.add("S2");
        postList.add(list4);
        ArrayList<String> list5 = new ArrayList<>();
        list5.add("A5");
        list5.add("S3");
        postList.add(list5);
    }

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        initScreenSize();
        initPostList();
    }

    public boolean isNetworkAvailable() {
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if (null != netInfo && netInfo.isConnected()) {
            wifiConnected = netInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = netInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }

        if (wifiConnected || mobileConnected) {
            return true;
        } else {
            Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * 添加Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    /**
     * 移除Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    /**
     * 退出所有Activity
     */
    public void exitAllActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    /**
     * 设置用户登录状态
     *
     * @param login
     */
    public void setLogin(boolean login) {
        isLogin = login;
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        BaseApplication.user = user;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode() {
        try {
            int version = mInstance.getPackageManager().getPackageInfo(mInstance.getPackageName(), 0).versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前系统语言
     *
     * @return 当前系统语言
     */
    public static String getLanguage() {
        Locale locale = mInstance.getResources().getConfiguration().locale;
        String language = locale.getDefault().toString();
        return language;
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }

    /**
     * 突破64K问题，MultiDex构建
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
