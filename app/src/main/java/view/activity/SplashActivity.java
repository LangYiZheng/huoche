package view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.dell004.myapplication.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.BaseApplication;
import app.GlobalConstants;
import bean.UpdateVersion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends Activity {

    private RelativeLayout rlRoot;
    private int localVersion;
    private AlertDialog dialog;
    private UpdateVersion updateVersion;
    private SharedPreferences sp;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        localVersion = BaseApplication.getVersionCode();
        ((BaseApplication) getApplication()).addActivity(this);
        initView();
        LoadAnimation();

        requsetServiceCode();

    }

    private void requsetServiceCode() {
        OkHttpClient client = new OkHttpClient();
        client.newCall(new Request.Builder().url(GlobalConstants.UPDATE + "/returndata/To_Update").build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = response.body().string();
                    updateVersion = gson.fromJson(json,UpdateVersion.class);
                    if (updateVersion.getData() != null) {
                        int serversCode = updateVersion.getData().get(0).getApkInfo().getVersionCode();
                        String packageName = updateVersion.getData().get(0).getProperties().getPackageId();
                        sp = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("updateCode", serversCode);
                        edit.putString("packageName", packageName);
                        edit.apply();
                    }
                }
            }
        });
    }

    private void showUpdateDialog() {
        dialog = new AlertDialog.Builder(SplashActivity.this)
                .setMessage("发现新版本,是否更新新版本?")
                .setTitle("提示:")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downloadApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 从服务器端下载最新apk
     */
    private void downloadApk() {
        //显示下载进度

        ProgressDialog dialog = new ProgressDialog(SplashActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();

        //访问网络下载apk
        new Thread(new DownloadApk(dialog)).start();
    }

    private void LoadAnimation() {
        // 旋转动画
        RotateAnimation animRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animRotate.setDuration(1000);// 动画时间
        animRotate.setFillAfter(true);// 保持动画结束状态

        // 缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);// 保持动画结束状态

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);// 动画时间
        animAlpha.setFillAfter(true);// 保持动画结束状态

        // 动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animRotate);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);

//		 启动动画
        rlRoot.startAnimation(set);

        set.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束,跳转页面
                // 如果是第一次进入, 跳新手引导
                // 否则跳主页面
//				boolean isFirstEnter = PrefUtils.getBoolean(
//						SplashActivity.this, "is_first_enter", true);
//
//				Intent intent;
//				if (isFirstEnter) {
//					// 新手引导
//					intent = new Intent(getApplicationContext(),
//							GuideActivity.class);
//				} else {
//					// 主页面
//					intent = new Intent(getApplicationContext(),
//							MainActivity.class);
//				}

//				startActivity(intent);
                if (sp == null) {
                    sp = getPreferences(MODE_PRIVATE);
                }
                int updateCode = sp.getInt("updateCode", 0);
                String packageName = sp.getString("packageName", "");
                if (updateCode > localVersion && packageName.equals(getPackageName())) {
                    showUpdateDialog();
                } else
                    startLoginActivity();
            }
        });
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//				startActivity(intent);
//				finish();// 结束当前页面
//			}
//		},3000);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        intent.setClass(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();// 结束当前页面
    }

    private void initView() {
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
    }

    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        ((BaseApplication) getApplication()).removeActivity(this);
        super.onDestroy();
    }

    private class DownloadApk implements Runnable {
        ProgressDialog dialog;
        private OutputStream os;
        private InputStream is;


        public DownloadApk(ProgressDialog dialog) {
            this.dialog = dialog;

        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            try {

                //http://192.168.199.122/release/app-release.apk
                File file = new File(Environment.getExternalStorageDirectory(), R.string.app_name + ".apk");

                Response response = client.newCall(new Request.Builder().url(GlobalConstants.UPDATE_APK).build()).execute();
                if (response.isSuccessful()) {
                    long l = response.body().contentLength();
                    dialog.setMax((int) l);//设值大小
                    is = response.body().byteStream();
                    os = new FileOutputStream(file);
                    int len = -1;
                    int progress = 0;
                    byte[] b = new byte[1024 * 1];
                    while ((len = is.read(b)) != -1) {
                        os.write(b, 0, len);
                        os.flush();
                        progress += len;
                        dialog.setProgress(progress);
                    }
                    installApk(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                is = null;
                os = null;
            }
            dialog.dismiss();

        }
    }

    private void installApk(File file) {
        //调用系统安装程序
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }
}
