package view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell004.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import app.BaseApplication;
import app.GlobalConstants;
import bean.PushBean;
import bean.QueryDataBean;
import bean.TrainBean;
import bean.UserBean;
import evntBus.MsgEventBus;
import interfaces.HttpCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import receiver.IntnetReceiver;
import utils.NetWorkCallback;
import utils.OfflineResource;
import utils.OkHttpUtils;
import view.fragment.BaseFragment;
import view.fragment.HistoryInfoFragment;
import view.fragment.ItemFragment;
import view.wiget.CustomDialog;
import view.wiget.MySwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, HttpCallback {

    public static final String TAG = "MainActivity";
    TextView tvPost;
    UserBean user;
    Button btnSetup;
    private RadioGroup radioGroup;
    private RadioButton tabOne, tabTwo;
    private MySwipeRefreshLayout refreshLayout;
    private IntnetReceiver receiver;
    protected String appId = "11370362";
    protected String appKey = "cIGNCyrlgKnp6QYoua4AcYRY";
    protected String secretKey = "eIaRFA5VgULFkLP4lGtSrgs1mnRIZ9YC";
//    private static final String SAMPLE_DIR = Environment.getExternalStorageDirectory().getPath() + baidu;
    public boolean flag;
    String idFlag = "";
    ArrayList<ArrayList<String>> mPostList;
    List<PushBean.DataBean> data;
    Map<String,List<String>> pushUserMap = new HashMap<>();
    List<String> notifyList = new ArrayList<>();
    private QueryDataBean queryDataBean;
    private Timer timer;
    private Context context = this;
    private long firstTime;
    TrainBean tBean;

    private List<BaseFragment> list = new ArrayList<>();
    private static final int STOP_REFRESH = 1;
    private static final int STOP_REFRESH_ERROR = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP_REFRESH:
                    refreshLayout.setRefreshing(false);
                    break;
                case STOP_REFRESH_ERROR:
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(getApplication(), "请检测网络状态", Toast.LENGTH_SHORT).show();
                    break;
                case 300:
                    modifyQurryData();
                    MsgEventBus eventBus = new MsgEventBus();
                    eventBus.setBean(queryDataBean);
                    EventBus.getDefault().postSticky(eventBus);
                    break;
            }
        }
    };
    private String mSpeechFlag = "";
    private SharedPreferences sp;

    public void querySingleTrain(String trainId) {
        String url = GlobalConstants.SINGLE_TRAIN + "?id=" + trainId;
        Log.e("SingleTrain", url);
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<TrainBean>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, TrainBean result) {
                tBean = result;
            }

            @Override
            public void onError(Response response) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
    }

    private void modifyQurryData() {
        if (null != queryDataBean && queryDataBean.getData().size() > 0) {
            for (QueryDataBean.DataBean db : queryDataBean.getData()) {
                if (db.getId().equals(BaseApplication.pushTrainId)) {
                    if (user.getData().getNumber().startsWith("A")) {
                        db.setPassenger_status("0");
                        db.setPassenger_number(user.getData().getNumber());
                    } else if (user.getData().getNumber().startsWith("H")) {
                        db.setCar_status("0");
                        db.setCar_number(user.getData().getNumber());
                    } else if (user.getData().getNumber().startsWith("C")) {
                        db.setTeahouse_status("0");
                        db.setTeahouse_number(user.getData().getNumber());
                    } else if (user.getData().getNumber().startsWith("G")) {
                        db.setVip_status("0");
                        db.setVip_number(user.getData().getNumber());
                    } else if (user.getData().getNumber().startsWith("S")) {
                        db.setWater_status("0");
                        db.setWater_number(user.getData().getNumber());
                    } else if (user.getData().getNumber().startsWith("X")) {
                        db.setBag_status("0");
                        db.setBag_number(user.getData().getNumber());
                    }
                }
            }
        }
    }

    private CustomDialog dialog;

    public void showNotifyDialog(String text,String id) {
        dialog = new CustomDialog(MainActivity.this);
        dialog.setMessage(text + "\n确认是否接车?");
        dialog.setTitle("进站预告");
        dialog.setOnSureClickListener("确认接车", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                RecieveTrain(id);
                BaseApplication.pushTrainId = id;
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        BaseApplication.getInstance().addActivity(this);
        mPostList = BaseApplication.postList;
        receiver = new IntnetReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);

        user = BaseApplication.getInstance().getUser();
        initView();
        showRresh();
        loadData();
//        baiduInit();
    }

    private void showRresh() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
    }

    private void initFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        list.add(new ItemFragment());
        list.add(new HistoryInfoFragment());
        transaction.add(R.id.main_fragment, list.get(0)).show(list.get(0));
        transaction.add(R.id.main_fragment, list.get(1)).hide(list.get(1));
        transaction.commit();
    }

    private void initView() {
        initFragment();
        sp = getSharedPreferences("notify", MODE_PRIVATE);
        refreshLayout = findViewById(R.id.srfl);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setTouchSlop(150);
        refreshLayout.setColorSchemeResources(R.color.bady_bule);
        tvPost = findViewById(R.id.tv_post_top);
        tvPost.setText(user.getData().getTrue_name());
        tvPost.setOnClickListener(this);

        radioGroup = findViewById(R.id.main_tab_group);
        tabOne = findViewById(R.id.btn_monitor_top);
        tabTwo = findViewById(R.id.btn_find_top);
        btnSetup = findViewById(R.id.btn_setup_top);
        btnSetup.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                switch (checkedId) {
                    case R.id.btn_monitor_top:
//                        Toast.makeText(MainActivity.this, "作业监控被点击了", Toast.LENGTH_SHORT).show();
                        transaction.show(list.get(0)).hide(list.get(1)).commit();
                        if (refreshLayout.isEnabled() == false)
                            refreshLayout.setEnabled(true);
                        break;
                    case R.id.btn_find_top:
//                        Toast.makeText(MainActivity.this, "资料查询被点击了", Toast.LENGTH_SHORT).show();
                        transaction.show(list.get(1)).hide(list.get(0)).commit();
                        refreshLayout.setEnabled(false);
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        showRresh();
        loadData();
    }

    private void loadData() {
        new Thread(()->{
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(GlobalConstants.SERVER_IP)
                    .addPathSegments("Jiekou/Returndata/query")
                    .addQueryParameter("p", "1")
                    .addQueryParameter("pageSize", "10")
                    .build();
            Log.e(MainActivity.class.getSimpleName(), url.toString());
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
//                new Handler(getMainLooper()).sendEmptyMessage(STOP_REFRESH_ERROR);
                    initDateCallback(null, true);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        Log.e(MainActivity.class.getSimpleName(), json);
                        Gson gson = new Gson();
                        QueryDataBean queryDataBean = null;
                        try {
                            queryDataBean = gson.fromJson(json, QueryDataBean.class);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        initDateCallback(queryDataBean, false);
                    }
//                initDateCallback(null, false);
                }
            });
        }).start();
    }

    @Override
    public void initDateCallback(QueryDataBean bean, boolean isRefresh) {
        this.queryDataBean = bean;

        if (isRefresh) {
            mHandler.sendEmptyMessage(STOP_REFRESH_ERROR);
        } else {
            mHandler.sendEmptyMessage(STOP_REFRESH);
        }

        MsgEventBus msgEventBus = new MsgEventBus();
        msgEventBus.setBean(queryDataBean);
        EventBus.getDefault().postSticky(msgEventBus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        flag = true;
//        mSpeechSynthesizer.release();
//        EventBus.getDefault().unregister(this);
        BaseApplication.getInstance().removeActivity(this);
        unregisterReceiver(receiver);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimerRequst();//设置定时器
    }

    private void setTimerRequst() {
        if (timer == null)
            timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                push();
                loadData();
            }
        }, 15000, 1000 * 15);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            firstTime = System.currentTimeMillis();
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

//    public void baiduInit() {
//        if (mSpeechSynthesizer == null) {
//            mSpeechSynthesizer = SpeechSynthesizer.getInstance();
//            mSpeechSynthesizer.setContext(context);
//            mSpeechSynthesizer.setApiKey(appKey, secretKey);
//            mSpeechSynthesizer.setAppId(appId);
//            mSpeechSynthesizer.auth(ttsMode);//设置混合模式
//
//            OfflineResource offlineResource = createOfflineResource(OfflineResource.VOICE_FEMALE);
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
////          mSpeechSynthesizer.loadModel(speechModelPath,  textModelPath);
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
//            int i = mSpeechSynthesizer.initTts(ttsMode);
//            Log.e("result", "" + i);
//        }
//    }

    public void push() {
        OkHttpUtils.getinstance().get(GlobalConstants.CPUSH, new NetWorkCallback<PushBean>() {
            @Override
            public void onLoadingBefore(Request request) {
            }

            @Override
            public void onSuccess(Response response, PushBean result) {
                if (response.isSuccessful()) {
                    data = result.getData();

                    if (data != null) {
                        for (PushBean.DataBean datum : data) {
                            String notify = datum.getNumber() + "次列车即将进入" + datum.getMarket_name() + "股道,请工作人员做好接车准备!";
                            String track = datum.getMarket_name();
                            String hou = datum.getCar_name();
                            String id = datum.getId();
                            addPost(datum, track, hou);
                            mySpeech(notify,id);
                        }
//                        if (!data.isEmpty()) {
//                            for (int i = 0; i < data.size(); i++) {
//                                PushBean.DataBean pushDataBean = data.get(i);
//                                List<String> userGroup = pushUserList.get(i);
//                                if (null == pushDataBean.getPassenger_number() || null == pushDataBean.getCar_number()
//                                        || null == pushDataBean.getTeahouse_number() || null == pushDataBean.getVip_number()
//                                        || null == pushDataBean.getWater_number() || null == pushDataBean.getBag_number()) {
//                                    for (String s : userGroup) {
//                                        if (user.getData().getNumber().equals(s)) {
//                                            showNotifyDialog(i);
//                                        }
//                                    }
//                                }
//                            }
//
                    }
                }

            }

            @Override
            public void onError(Response response) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        Log.e("Main-Push", GlobalConstants.CPUSH);
//        client.newCall(new Request.Builder().url(GlobalConstants.CPUSH).build()).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("s", "s");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String info = response.body().string();
//                Log.e("Main-Push", info);
//                if (response.isSuccessful()) {
//                    Gson gson = new Gson();
//                    PushBean bean = gson.fromJson(info, PushBean.class);
//                    data = bean.getData();
//
//                    if (data != null) {
//                        pushUserList.clear();
//                        for (PushBean.DataBean datum : data) {
//                            String notify = datum.getNumber() + "次列车即将进入" + datum.getMarket_name() + "股道,请工作人员做好接车准备";
//                            mySpeech(notify);
//                            String track = datum.getMarket_name();
//                            String hou = datum.getCar_name();
//                            BaseApplication.pushTrainId = datum.getId();
//                            notifyList.add(notify);
//                            List<String> group = new ArrayList<>();
//                            //安全员
//                            group.add(getAnQYNumber(track));
//                            Log.e("mPostMap", track + "      " + getAnQYNumber(track));
//                            //候车室
//                            if ("1候".equals(hou)) {
//                                group.add("H1");
//                            } else if ("2候".equals(hou)) {
//                                group.add("H2");
//                            } else if ("3候".equals(hou)) {
//                                group.add("H3");
//                            }
//
//                            //茶座
//                            if ("1".equals(datum.getTeahouse_type())) {
//                                group.add("C1");
//                            }
//
//                            //贵宾
//                            if ("1".equals(datum.getVip_type())) {
//                                group.add("G1");
//                            }
//                            //行包
//                            if ("1".equals(datum.getBag_type())) {
//                                group.add("X1");
//                                group.add("X2");
//                                group.add("X3");
//                                group.add("X4");
//                            }
//
//                            //上水
//                            if ("1".equals(datum.getWater_type())) {
//                                group.add(getShangNumber(track));
//                            }
//
//                            pushUserList.add(group);
//                        }
//                    }
//                    mHandler.sendEmptyMessage(100);
//                }
//            }
//        });
    }

    public void addPost(PushBean.DataBean datum, String track, String hou) {
        List<String> group = new ArrayList<>();
        //安全员
//        String str = getAnQYNumber(track);
        group.add(getAnQYNumber(track));
        //候车室
        if ("1候".equals(hou)) {
            group.add("H1");
        } else if ("2候".equals(hou)) {
            group.add("H2");
        } else if ("3候".equals(hou)) {
            group.add("H3");
        }

        //茶座
        if ("1".equals(datum.getTeahouse_type())) {
            group.add("C1");
        }

        //贵宾
        if ("1".equals(datum.getVip_type())) {
            group.add("G1");
        }
        //行包
        if ("1".equals(datum.getBag_type())) {
            group.add("X1");
            group.add("X2");
            group.add("X3");
            group.add("X4");
        }

        //上水
        if ("1".equals(datum.getWater_type())) {
            group.add(getShangNumber(track));
        }

        pushUserMap.put(datum.getId(),group);
    }

    public String getAnQYNumber(String trace) {

        if(null != trace && !"".equals(trace)){
           if("1道".equals(trace)){
               return mPostList.get(0).get(0);
           }else if("2道".equals(trace)){
               return mPostList.get(1).get(0);
           }else if("3道".equals(trace)){
               return mPostList.get(2).get(0);
           }else if("4道".equals(trace)){
               return mPostList.get(3).get(0);
           }else {
               return mPostList.get(4).get(0);
           }
        }
        return "";
    }

    public String getShangNumber(String trace) {
        String number = "";
        if(null != trace && !"".equals(trace)){
            switch (trace){
                case "1道":
                    number = mPostList.get(0).get(1);
                    break;
                case "2道":
                    number = mPostList.get(1).get(1);
                    break;
                case "3道":
                    number = mPostList.get(2).get(1);
                    break;
                case "4道":
                    number = mPostList.get(3).get(1);
                    break;
                case "5道":
                    number = mPostList.get(4).get(1);
                    break;
            }
        }
        return number;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(MainActivity.this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
//            toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    public void mySpeech(final String text ,String id) {
//        new Thread() {
//            @Override
//            public void run() {
////                while (!flag) {
//                    mSpeechSynthesizer.speak(text);
//                    mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {
//                        @Override
//                        public void onSynthesizeStart(String s) {
//
//                        }
//
//                        @Override
//                        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
//
//                        }
//
//                        @Override
//                        public void onSynthesizeFinish(String s) {
//
//                        }
//
//                        @Override
//                        public void onSpeechStart(String s) {
//
//                        }
//
//                        @Override
//                        public void onSpeechProgressChanged(String s, int i) {
//
//                        }
//
//                        @Override
//                        public void onSpeechFinish(String s) {
////                            try {
////                                sleep(1000 * 5);
////                            } catch (InterruptedException e) {
////                                e.printStackTrace();
////                            }
//                        }
//
//                        @Override
//                        public void onError(String s, SpeechError speechError) {
//
//                        }
//                    });
////                    if (flag) {
////                        flag = false;
////                        mSpeechSynthesizer.stop();
////                        return;
////                    }
////                }
//            }
//        }.start();
//        if (!TextUtils.isEmpty(text))

        if (!user.getData().getNumber().equals(sp.getString("userNumber",""))
                || !sp.getString("notify", "").equals(text)) {
            mSpeechFlag = text;
//            mSpeechSynthesizer.speak(text);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
            sp.edit().putString("notify",text).putString("userNumber",user.getData().getNumber()).commit();
            if(pushUserMap.get(id).contains(user.getData().getNumber())){
                showNotifyDialog(text,id);
            }else {
                Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();
            }
        }
    }

    public void RecieveTrain(String id) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(GlobalConstants.SERVER_IP)
                .addPathSegments("jiekou/returndata/andiord_init")
                .addQueryParameter("id", id)
                .addQueryParameter("number", user.getData().getNumber())
                .build();
        Log.e("url", url.toString());
//        client.newWebSocket()
        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    mHandler.sendEmptyMessage(300);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setup_top:
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                break;
        }
    }
}
