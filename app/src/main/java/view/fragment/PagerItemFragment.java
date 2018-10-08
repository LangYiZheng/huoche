package view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell004.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import app.BaseApplication;
import app.GlobalConstants;
import bean.QueryDataBean;
import bean.ReleaseBean;
import bean.UserBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.DateUtils;
import utils.NetWorkCallback;
import utils.OkHttpUtils;
import view.wiget.CustomDialog;

/**
 * Created by dell003 on 2018/5/11.
 */

@SuppressLint("ValidFragment")
public class PagerItemFragment extends BaseFragment implements View.OnClickListener {
    public static final String REGEX_HOURS = "HH:mm";
    QueryDataBean.DataBean dataBean;
    private TextView toOpenTime;
    TextView attendantState, attendantTime, waitingState, waitingTime, teahouseState, teahouseTime;
    TextView vipState, vipTime, addWaterState, addWaterTime, packeageState, packeageTime;
    TextView tvRelease, tvPostAttendant;
    UserBean userBean;
    boolean isDone, isRelease;
    Calendar calendar;
    int position;
    private TextView text;
    String trainNumber;
    SharedPreferences mPreferences;
    boolean isRun;

    public void setDataBean(QueryDataBean.DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public PagerItemFragment(QueryDataBean.DataBean dataBean) {
        super();
        this.dataBean = dataBean;
    }

    public PagerItemFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRun) {
            isRun = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long leaveTime = Long.parseLong(dataBean.getLate()) * 1000;
                    long currTime = 0;
                    while ((leaveTime - currTime) > (1000 * 60)) {
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        currTime = Calendar.getInstance().getTimeInMillis();
                    }
                    handler.sendEmptyMessage(100);
                }
            }).start();
        }
    }

    @Override
    protected void initView() {
        BaseApplication app = (BaseApplication) getActivity().getApplication();
        userBean = app.getUser();
        mPreferences = getActivity().getSharedPreferences("tips", Context.MODE_PRIVATE);
        //倒开时间
        toOpenTime = viewlayout.findViewById(R.id.tv_job_train_time);
        String fromToTime = DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/"
                + DateUtils.parseTime(REGEX_HOURS, dataBean.getLate());
        toOpenTime.setText(fromToTime);
        //停车位置
        TextView parkingLocation = viewlayout.findViewById(R.id.tv_job_platform_num);
        parkingLocation.setText(dataBean.getMarket_name());
        //候车位置
        TextView waiotingArea = viewlayout.findViewById(R.id.tv_job_waiteroom_num);
        waiotingArea.setText(dataBean.getCar_name());
        //客运安全员状态 //客运确认时间
        tvPostAttendant = viewlayout.findViewById(R.id.tv_job_post_duty);
//        tvRelease = viewlayout.findViewById(R.id.tv_job_release);
//        tvRelease.setOnClickListener(this);
        attendantState = viewlayout.findViewById(R.id.tv_job_duty_state);
        attendantTime = viewlayout.findViewById(R.id.tv_job_duty_time);
        attendantState.setText(null != dataBean.getPassenger_number() ? dataBean.getPassenger_number() : "--");
        if ("2".equals(dataBean.getPassenger_status())) {
            attendantState.setBackground(getResources().getDrawable(R.drawable.shape_point_gray));
        } else if ("1".equals(dataBean.getPassenger_status())) {
            attendantState.setBackground(getResources().getDrawable(R.drawable.shape_point_green));
        } else {
            attendantState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
            attendantState.setText(dataBean.getPassenger_number() + "\n放客");
        }
        if ("1".equals(dataBean.getCar_status()) && "1".equals(dataBean.getTeahouse_status())
                && "1".equals(dataBean.getVip_status())) {
            attendantState.setText(dataBean.getPassenger_number() + "\n发车");
        }
        attendantTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/"
                + (null == dataBean.getPassenger_time() ? "---:---" : DateUtils.parseTime(REGEX_HOURS, dataBean.getPassenger_time())));
        attendantState.setOnClickListener(this);

        //候车室
        waitingState = viewlayout.findViewById(R.id.tv_job_waiter_state);
        waitingTime = viewlayout.findViewById(R.id.tv_job_waiter_time);
        waitingState.setText(null != dataBean.getCar_number() ? dataBean.getCar_number() : "--");
        if ("2".equals(dataBean.getCar_status())) {
            waitingState.setBackground(getResources().getDrawable(R.drawable.shape_point_gray));
        } else if ("1".equals(dataBean.getCar_status())) {
            waitingState.setBackground(getResources().getDrawable(R.drawable.shape_point_green));
        } else if ("0".equals(dataBean.getCar_status())) {
            waitingState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
        } else {
            waitingState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
            trainNumber = mPreferences.getString("trainNumber", "");
            if (userBean.getData().getNumber().equals(dataBean.getCar_number())) {
                if (!userBean.getData().getNumber().equals(mPreferences.getString("userNumber", ""))
                        || !trainNumber.equals(dataBean.getNumber())) {
                    showReleaseWaiteDialog();
                }
            }
        }

        waitingTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/"
                + (null == dataBean.getCar_time() ? "---:---" : DateUtils.parseTime(REGEX_HOURS, dataBean.getCar_time())));
        waitingState.setOnClickListener(this);

//        茶座
        teahouseState = viewlayout.findViewById(R.id.tv_job_tea_state);
        teahouseTime = viewlayout.findViewById(R.id.tv_job_tea_time);
        teahouseState.setText(null != dataBean.getTeahouse_number() ? dataBean.getTeahouse_number() : "--");
        teahouseTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                (null == dataBean.getTeahouse_time() ? "---:---" : DateUtils.parseTime(REGEX_HOURS, dataBean.getTeahouse_time())));
        if ("2".equals(dataBean.getTeahouse_status())) {
            teahouseState.setBackground(getResources().getDrawable(R.drawable.shape_point_gray));
        } else if ("1".equals(dataBean.getTeahouse_status())) {
            teahouseState.setBackground(getResources().getDrawable(R.drawable.shape_point_green));
            if (null == dataBean.getTeahouse_number()) {
                teahouseTime.setText(fromToTime);
            }
        } else if ("0".equals(dataBean.getTeahouse_status())) {
            teahouseState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
        } else {
            teahouseState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));

            trainNumber = mPreferences.getString("trainNumber", "");
            if (userBean.getData().getNumber().equals(dataBean.getTeahouse_number())) {
                if (!userBean.getData().getNumber().equals(mPreferences.getString("userNumber", ""))
                        || !trainNumber.equals(dataBean.getNumber())) {
                    showReleaseTeaHouseDialog();
                }
            }
        }
        teahouseState.setOnClickListener(this);
//        vip
        vipState = viewlayout.findViewById(R.id.tv_job_vip_state);
        vipTime = viewlayout.findViewById(R.id.tv_job_vip_tiem);
        vipState.setText(null != dataBean.getVip_number() ? dataBean.getVip_number() : "--");
        vipTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                (null == dataBean.getVip_time() ? "---:---" : DateUtils.parseTime(REGEX_HOURS, dataBean.getVip_time())));
        if ("2".equals(dataBean.getVip_status())) {
            vipState.setBackground(getResources().getDrawable(R.drawable.shape_point_gray));
        } else if ("1".equals(dataBean.getVip_status())) {
            vipState.setBackground(getResources().getDrawable(R.drawable.shape_point_green));
            if (null == dataBean.getVip_number()) {
                vipTime.setText(fromToTime);
            }
        } else if ("0".equals(dataBean.getVip_status())) {
            vipState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
        } else {
            vipState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));

            trainNumber = mPreferences.getString("trainNumber", "");
            if (userBean.getData().getNumber().equals(dataBean.getVip_number())) {
                if (!userBean.getData().getNumber().equals(mPreferences.getString("userNumber", ""))
                        || !trainNumber.equals(dataBean.getNumber())) {
                    showReleaseVipDialog();
                }
            }
        }
        vipState.setOnClickListener(this);
//       上水
        addWaterState = viewlayout.findViewById(R.id.tv_job_water_state);
        addWaterTime = viewlayout.findViewById(R.id.tv_job_water_time);
        addWaterState.setText(null != dataBean.getWater_number() ? dataBean.getWater_number() : "--");
        addWaterTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                (null == dataBean.getWater_time() ? "---:---" : DateUtils.parseTime(REGEX_HOURS, dataBean.getWater_time())));
        if ("2".equals(dataBean.getWater_status())) {
            addWaterState.setBackground(getResources().getDrawable(R.drawable.shape_point_gray));
        } else if ("1".equals(dataBean.getWater_status())) {
            addWaterState.setBackground(getResources().getDrawable(R.drawable.shape_point_green));
            if (null == dataBean.getWater_number()) {
                addWaterTime.setText(fromToTime);
            }
        } else {
            addWaterState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
        }
        addWaterState.setOnClickListener(this);
//        行包
        packeageState = viewlayout.findViewById(R.id.tv_job_package_state);
        packeageTime = viewlayout.findViewById(R.id.tv_job_package_time);
        packeageState.setText(null == dataBean.getBag_number() ? "--" : dataBean.getBag_number());
        packeageTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                (null == dataBean.getBag_time() ? "---:---" : DateUtils.parseTime(REGEX_HOURS, dataBean.getBag_time())));
        if ("2".equals(dataBean.getBag_status())) {
            packeageState.setBackground(getResources().getDrawable(R.drawable.shape_point_gray));
        } else if ("1".equals(dataBean.getBag_status())) {
            packeageState.setBackground(getResources().getDrawable(R.drawable.shape_point_green));
            if (null == dataBean.getBag_number()) {
                packeageTime.setText(fromToTime);
            }
        } else {
            packeageState.setBackground(getResources().getDrawable(R.drawable.shape_point_red));
        }
        packeageState.setOnClickListener(this);
//        推送显示文本
        text = viewlayout.findViewById(R.id.jpush_msg_show);

        if (dataBean.getArrive().equals("0")) {
            text.setText("预告信息");
        } else if (dataBean.getArrive().equals("1")) {
//            datum.getNumber()+"次列车即将进入"+datum.getMarket_name()+",请工作人员做好接车准备
//            Html.fromHtml(<font color='red' size='24'>你好</font>" )
//            text.setText(dataBean.getNumber() + "次预告,进" + dataBean.getMarket_name() + "股道");
            text.setText(Html.fromHtml("<font color='red'>" + dataBean.getNumber() + "</font>" + "次预告,进" + "<font color='red'>" + dataBean.getMarket_name() + "</font>" + "股道"));
        }
    }

    private void showReleaseTeaHouseDialog() {
        final CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setTitle("确认放客");
        dialog.setMessage(dataBean.getNumber() + "次列车可以放客了，是否放客？");
        dialog.setCancelable(false);
        dialog.setOnSureClickListener("确定放客", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                releaseTeaHouse();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void releaseTeaHouse() {
        String url = GlobalConstants.RELEASE_TEAHOUSE + "?id=" + dataBean.getId();
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<ReleaseBean>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, ReleaseBean result) {
                if (response.isSuccessful()) {

                    trainNumber = dataBean.getNumber();
                    mPreferences.edit().putString("userNumber", userBean.getData().getNumber())
                            .putString("trainNumber", trainNumber).commit();
                }
            }

            @Override
            public void onError(Response response) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
    }

    private void showReleaseWaiteDialog() {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setTitle("确认放客");
        dialog.setMessage(dataBean.getNumber() + "次列车可以放客了，是否放客？");
        dialog.setCancelable(false);
        dialog.setOnSureClickListener("确定放客", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                releaseWaite();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void releaseWaite() {
        String url = GlobalConstants.RELEASE_WAITE + "?id=" + dataBean.getId();
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<ReleaseBean>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, ReleaseBean result) {
                if (response.isSuccessful()) {
                    trainNumber = dataBean.getNumber();
                    Log.e("releaseWaite", "count" + trainNumber);
                    mPreferences.edit().putString("userNumber", userBean.getData().getNumber())
                            .putString("trainNumber", trainNumber).commit();
                }
            }

            @Override
            public void onError(Response response) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
    }

    private void showReleaseVipDialog() {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setTitle("确认放客");
        dialog.setMessage(dataBean.getNumber() + "次列车可以放客了，是否放客？");
        dialog.setCancelable(false);
        dialog.setOnSureClickListener("确定放客", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                releaseVip();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void releaseVip() {
        String url = GlobalConstants.RELEASE_VIP + "?id=" + dataBean.getId();
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<ReleaseBean>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, ReleaseBean result) {
                if (response.isSuccessful()) {
                    trainNumber = dataBean.getNumber();
                    mPreferences.edit().putString("userNumber", userBean.getData().getNumber())
                            .putString("trainNumber", trainNumber).commit();
                }
            }

            @Override
            public void onError(Response response) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    private void showSubmmitDialog(final String url) {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setTitle("5".equals(userBean.getData().getType()) ? "确认发车" : "确认作业完毕");
        String message = "5".equals(userBean.getData().getType()) ? "是否发车？" : "";
        dialog.setMessage(dataBean.getNumber() + "次列车作业完成！\n" + message);
        dialog.setCancelable(false);
        dialog.setOnSureClickListener("确定", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                isDone = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        asyncGetStatusRequest(url);
                    }
                }).start();
                dialog.dismiss();
            }
        });
        dialog.setOnCancelClickListener("取消", new CustomDialog.OnCancelClickListener() {
            @Override
            public void onCliclk() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int loadView() {
        return R.layout.train_job_info_fgr;
    }

    @Override
    public void initData() {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), "网络错误，请重新提交！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getActivity(), "提交失败，请重新提交！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();
                    calendar = Calendar.getInstance();
                    changeStatus(position, calendar);
                    break;
                case 100:
                    if (!isFinsh(userBean.getData().getNumber())) {
                        showAlertDialog();
                    }
                    break;
            }
        }
    };

    public boolean isFinsh(String workNumber) {
        if (null == workNumber || "".equals(workNumber)) {
            return false;
        } else {
            if (workNumber.equals(dataBean.getPassenger_number())
                    && "1".equals(dataBean.getPassenger_status())) {
                return true;
            }
            if (workNumber.equals(dataBean.getCar_number())
                    && "1".equals(dataBean.getCar_status())) {
                return true;
            }
            if (workNumber.equals(dataBean.getTeahouse_number())
                    && "1".equals(dataBean.getTeahouse_status())) {
                return true;
            }
            if (workNumber.equals(dataBean.getVip_number())
                    && "1".equals(dataBean.getVip_status())) {
                return true;
            }
            if (workNumber.equals(dataBean.getWater_number())
                    && "1".equals(dataBean.getWater_status())) {
                return true;
            }
            if (workNumber.equals(dataBean.getBag_number())
                    && "1".equals(dataBean.getBag_status())) {
                return true;
            }
            return false;
        }
    }

    private void changeStatus(int position, Calendar calendar) {
        switch (position) {
            case 1:
                dataBean.setPassenger_status("1");
                attendantState.setBackground(getResources().getDrawable("1".equals(dataBean.getPassenger_status()) ?
                        R.drawable.shape_point_green : R.drawable.shape_point_red));
                attendantTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                        DateUtils.parseTime(REGEX_HOURS, calendar.getTimeInMillis()));
                break;
            case 2:
                dataBean.setCar_status("1");
                waitingState.setBackground(getResources().getDrawable("1".equals(dataBean.getCar_status()) ?
                        R.drawable.shape_point_green : R.drawable.shape_point_red));
                waitingTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                        DateUtils.parseTime(REGEX_HOURS, calendar.getTimeInMillis()));
                break;
            case 3:
                dataBean.setTeahouse_status("1");
                teahouseState.setBackground(getResources().getDrawable("1".equals(dataBean.getTeahouse_status()) ?
                        R.drawable.shape_point_green : R.drawable.shape_point_red));
                teahouseTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                        DateUtils.parseTime(REGEX_HOURS, calendar.getTimeInMillis()));
                break;
            case 4:
                dataBean.setVip_status("1");
                vipState.setBackground(getResources().getDrawable("1".equals(dataBean.getVip_status()) ?
                        R.drawable.shape_point_green : R.drawable.shape_point_red));
                vipTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                        DateUtils.parseTime(REGEX_HOURS, calendar.getTimeInMillis()));
                break;
            case 5:
                dataBean.setWater_status("1");
                addWaterState.setBackground(getResources().getDrawable("1".equals(dataBean.getWater_status()) ?
                        R.drawable.shape_point_green : R.drawable.shape_point_red));
                addWaterTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                        DateUtils.parseTime(REGEX_HOURS, calendar.getTimeInMillis()));
                break;
            case 6:
                dataBean.setBag_status("1");
                packeageState.setBackground(getResources().getDrawable("1".equals(dataBean.getBag_status()) ?
                        R.drawable.shape_point_green : R.drawable.shape_point_red));
                packeageTime.setText(DateUtils.parseTime(REGEX_HOURS, dataBean.getPoint()) + "/" +
                        DateUtils.parseTime(REGEX_HOURS, calendar.getTimeInMillis()));
                break;
        }

    }

    @Override
    public void onClick(View v) {
        String url = null;
        calendar = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.tv_job_duty_state:
                securityClick();
                break;
            case R.id.tv_job_waiter_state:
                waiterClick();
                break;
            case R.id.tv_job_tea_state:
                teaHouseClick();
                break;
            case R.id.tv_job_vip_state:
                vipClick();
                break;
            case R.id.tv_job_water_state:
                addWaterCLick();
                break;
            case R.id.tv_job_package_state:
                packageClick();
                break;
            default:
                Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
        }
    }

    private void packageClick() {
        String url;
        if ("2".equals(dataBean.getBag_status())) {
            Toast.makeText(getActivity(), "该车次当前没有预告信息，不能操作！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userBean.getData().getNumber().equals(dataBean.getBag_number())) {
            Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
            return;
        }
        if ("1".equals(dataBean.getBag_status())) {
            Toast.makeText(getActivity(), "该作业已完成，请勿重新提交！", Toast.LENGTH_LONG).show();
            return;
        }
        url = GlobalConstants.DUTY_PUSH_URL + "/push?status=1&id=" + dataBean.getId()
                + "&where=bag&number=" + userBean.getData().getNumber();
        showSubmmitDialog(url);
        position = 6;
    }

    private void addWaterCLick() {
        String url;
        if ("2".equals(dataBean.getWater_status())) {
            Toast.makeText(getActivity(), "该车次当前没有预告信息，不能操作！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userBean.getData().getNumber().equals(dataBean.getWater_number())) {
            Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
            return;
        }

        url = GlobalConstants.DUTY_PUSH_URL + "/push?status=1&id=" + dataBean.getId()
                + "&where=water&number=" + userBean.getData().getNumber();
        showSubmmitDialog(url);
        position = 5;
    }

    private void vipClick() {
        String url;
        if ("2".equals(dataBean.getVip_status())) {
            Toast.makeText(getActivity(), "该车次当前没有预告信息，不能操作！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userBean.getData().getNumber().equals(dataBean.getVip_number())) {
            Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
            return;
        }
        url = GlobalConstants.DUTY_PUSH_URL + "/push?status=1&id=" + dataBean.getId()
                + "&where=vip&number=" + userBean.getData().getNumber();
        ;
        showSubmmitDialog(url);
        position = 4;
    }

    private void teaHouseClick() {
        String url;
        if ("2".equals(dataBean.getTeahouse_status())) {
            Toast.makeText(getActivity(), "该车次当前没有预告信息，不能操作！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userBean.getData().getNumber().equals(dataBean.getTeahouse_number())) {
            Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
            return;
        }

        url = GlobalConstants.DUTY_PUSH_URL + "/push?status=1&id=" + dataBean.getId()
                + "&where=teahouse&number=" + userBean.getData().getNumber();
        showSubmmitDialog(url);
        position = 3;
    }

    private void waiterClick() {
        String url;
        if ("2".equals(dataBean.getCar_status())) {
            Toast.makeText(getActivity(), "该车次当前没有预告信息，不能操作！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userBean.getData().getNumber().equals(dataBean.getCar_number())) {
            Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
            return;
        }

        url = GlobalConstants.DUTY_PUSH_URL + "/car_push?id=" + dataBean.getId() + "&status=1";
        showSubmmitDialog(url);
        position = 2;
    }

    private void securityClick() {
        String url;
        if ("2".equals(dataBean.getPassenger_status())) {
            Toast.makeText(getActivity(), "该车次当前没有预告信息，不能操作！", Toast.LENGTH_LONG).show();
            return;
        }
        if ("0".equals(dataBean.getTeahouse_status()) && attendantState.getText().toString().contains("放客")) {
            if (userBean.getData().getNumber().equals(dataBean.getPassenger_number()) &&
                    !"2".equals(dataBean.getCar_status()) && !"2".equals(dataBean.getTeahouse_status())
                    && !"2".equals(dataBean.getVip_number())) {
                showReleaseOderDialog();
            }
            return;
        }
        if (!userBean.getData().getNumber().equals(dataBean.getPassenger_number())) {
            Toast.makeText(getActivity(), "您没有权限操作此项作业！", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isReady()) {
            return;
        }

        url = GlobalConstants.DUTY_PUSH_URL + "/train_push?id=" + dataBean.getId() + "&status=" + "1";
        showSubmmitDialog(url);
        position = 1;
    }

    private void releaseOder() {
        String url = GlobalConstants.RELEASE + "?id=" + dataBean.getId();
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<ReleaseBean>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, ReleaseBean result) {
                if (response.isSuccessful()) {
                    isRelease = true;
                    attendantState.setText(dataBean.getPassenger_number() + "\n发车");
                }
            }

            @Override
            public void onError(Response response) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
    }

    public void asyncGetStatusRequest(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Message message = Message.obtain();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                Log.e("PagerItemFragment", msg);
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json.optInt("code") == 200) {
                        message.what = 2;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = 1;
                } finally {
                    handler.sendMessage(message);
                }
            }
        });
    }

    public boolean isReady() {
        if (!"1".equals(dataBean.getCar_status())) {
            if ("0".equals(dataBean.getCar_status())) {
                Toast.makeText(getActivity(), "候车值班员工作未完成！", Toast.LENGTH_LONG).show();
                return false;
            } else if ("2".equals(dataBean.getCar_status())) {
                Toast.makeText(getActivity(), "候车值班员未接车！", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Toast.makeText(getActivity(), "候车值班员未接收放客指令！", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!"1".equals(dataBean.getTeahouse_status())) {
            if ("0".equals(dataBean.getTeahouse_status())) {
                Toast.makeText(getActivity(), "茶座值班员工作未完成！", Toast.LENGTH_LONG).show();
                return false;
            } else if ("2".equals(dataBean.getTeahouse_status())) {
                Toast.makeText(getActivity(), "茶座值班员未接车！", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Toast.makeText(getActivity(), "茶座值班员未接受放客指令！", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!"1".equals(dataBean.getVip_status())) {
            if ("0".equals(dataBean.getVip_status())) {
                Toast.makeText(getActivity(), "贵宾席值班员工作未完成！", Toast.LENGTH_LONG).show();
                return false;
            } else if ("2".equals(dataBean.getVip_status())) {
                Toast.makeText(getActivity(), "贵宾席值班员未接车！", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Toast.makeText(getActivity(), "贵宾席值班员未接受放客指令！", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!"1".equals(dataBean.getWater_status())) {
            if ("0".equals(dataBean.getWater_status())) {
                Toast.makeText(getActivity(), "上水值班员工作未完成！", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Toast.makeText(getActivity(), "上水值班员工作未接车！", Toast.LENGTH_LONG).show();
                return false;
            }

        }
        if (!"1".equals(dataBean.getBag_status())) {
            if ("0".equals(dataBean.getBag_status())) {
                Toast.makeText(getActivity(), "行包值班员工作未完成！", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Toast.makeText(getActivity(), "行包值班员工作未接车！", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void showReleaseOderDialog() {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setTitle("发送放客指令");
        dialog.setMessage(dataBean.getNumber() + "次列车即将进站，是否发送放客指令？");
        dialog.setCancelable(false);
        dialog.setOnSureClickListener("确定", () -> {
            isRelease = true;
            releaseOder();
            dialog.dismiss();
        });
        dialog.show();
    }

    Ringtone ringtone;
    CustomDialog alarmDialog;

    public void showAlertDialog() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getActivity(), notification);
        ringtone.play();
        Vibrator vibrator = (Vibrator) (getActivity().getSystemService(Context.VIBRATOR_SERVICE));
        vibrator.vibrate(1000);

        alarmDialog = new CustomDialog(getActivity());
        alarmDialog.setTitle("警告");
        alarmDialog.setMessage(dataBean.getNumber() + "次列车即将离站，请尽快完成作业！");
        alarmDialog.setOnSureClickListener("确定", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                if (ringtone.isPlaying()) {
                    ringtone.stop();
                }

                alarmDialog.dismiss();
            }
        });
        alarmDialog.show();
    }
}
