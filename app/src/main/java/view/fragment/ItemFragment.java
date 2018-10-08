package view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import com.example.dell004.myapplication.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import adapter.TabViewPagerAdapter;
import app.BaseApplication;
import bean.QueryDataBean;
import bean.UserBean;
import evntBus.MsgEventBus;
import view.wiget.PagerSlidingTab;

/**
 * Created by Administrator on 2018/5/9/009.
 */

public class ItemFragment extends BaseFragment {
    private ViewPager pager;
    private PagerSlidingTab indicator;
    private QueryDataBean bean = null;
    private TabViewPagerAdapter adapter;
    private TextView jPushMSG;
    private Context context;
    private String url;
    private UserBean user;
    private int page;

    public ItemFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        context = getContext();
    }

    @Override
    protected void lazyLoad() {}

    protected void initView() {
        Log.e(ItemFragment.class.getSimpleName(), "控件初始化");
        UserBean userBean = ((BaseApplication) getActivity().getApplication()).getUser();
        Log.e("ItemFragment-+-initView", userBean.getData().getType() +"+++++"+ userBean.getData().getNumber());
        user = ((BaseApplication) getActivity().getApplication()).getUser();
//        jPushMSG = viewlayout.findViewById(R.id.jpush_msg_show);
        pager = viewlayout.findViewById(R.id.pager);
        adapter = new TabViewPagerAdapter(getFragmentManager());
        //先设置 后绑定
        pager.setAdapter(adapter);
        indicator = viewlayout.findViewById(R.id.indicator);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Toast.makeText(getContext(), bean.getData().get(position).getNumber(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {}
//RMB天下第一
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    @Override
    public int loadView() {
        return R.layout.fragment_tab;
    }

    @Override
    public void initData() {}

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventBus(MsgEventBus msg) {
        Log.e(ItemFragment.class.getSimpleName(), "接受消息");
        if (msg.getBean() != null) {
            adapter.setData(msg.getBean());
            adapter.notifyDataSetChanged();
            pager.setCurrentItem(0);
            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                @Override
                public void onPageSelected(int position) {
                    page = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {}
            });
            indicator.setViewPager(pager);
        }
    }

//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    Toast.makeText(getActivity(), "网络故障，检查网络后请重新添加作业！", Toast.LENGTH_LONG).show();
//                    if ("6".equals(user.getData().getType())) {
//                        showOptionPackageDialog();
//                    } else if ("3".equals(user.getData().getType())) {
//                        showAddWaterDialog();
//                    }
//                    break;
//                case 1:
//                    Toast.makeText(getActivity(), "操作失败，请重新添加作业！", Toast.LENGTH_LONG).show();
//                    if ("6".equals(user.getData().getType())) {
//                        showOptionPackageDialog();
//                    } else if ("3".equals(user.getData().getType())) {
//                        showAddWaterDialog();
//                    }
//                    break;
//                case 2:
//                    Toast.makeText(getActivity(), "行包作业添加成功", Toast.LENGTH_LONG).show();
//                    if ("6".equals(user.getData().getType())) {
//                        adapter.getBean().getData().get(page).setBag_type("1");
//                        adapter.getBean().getData().get(page).setBag_status("0");
//                    } else if ("3".equals(user.getData().getType())) {
//                        adapter.getBean().getData().get(page).setWater_status("0");
//                    }
//
//                    adapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    };
//
//    private void showPrevueNoticeDialog(String msg) {
//        Log.e("PrevueNotice", "msg");
//        final CustomDialog alarmDialog = new CustomDialog(getActivity());
//        alarmDialog.setTitle("确认预告信息");
//        alarmDialog.setMessage(msg);
//        alarmDialog.setCanceledOnTouchOutside(true);
//        alarmDialog.setOnSureClickListener("确定", new CustomDialog.OnSureClickListener() {
//            @Override
//            public void onCliclk() {
//                switch (user.getData().getType()) {
//                    //"茶座值班员"
//                    case "1":
//                        break;
//                    //"客运值班长"
//                    case "7":
//                        break;
//                    //"贵宾值班员"
//                    case "2":
//
//                        break;
//                    //"候车值班员"
//                    case "9":
//                        break;
//                    //"客运值班员"
//                    case "5":
//                        break;
//                    //"行包值班员"
//                    case "6":
//                        showOptionPackageDialog();
//                        break;
//                    //"上水工作人员"
//                    case "3":
//                        showAddWaterDialog();
//                        break;
//                }
//                alarmDialog.dismiss();
//            }
//        });
//
//        alarmDialog.show();
//    }
//
//    private void showAddWaterDialog() {
//        final CustomDialog alarmDialog = new CustomDialog(getActivity());
//        alarmDialog.setTitle("上水作业");
//        alarmDialog.setMessage("是否需要上水作业");
//        alarmDialog.setOnSureClickListener("是", new CustomDialog.OnSureClickListener() {
//            @Override
//            public void onCliclk() {
//                final String url = GlobalConstants.ADD_OPTION_URL + "/To_Water?id="
//                        + adapter.getBean().getData().get(page).getId() + "&status=1";
//                Log.e("adapter", url);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        asyncGetAddOption(url);
//                    }
//                }).start();
//                alarmDialog.dismiss();
//            }
//        });
//        alarmDialog.setOnCancelClickListener("否", new CustomDialog.OnCancelClickListener() {
//            @Override
//            public void onCliclk() {
//                alarmDialog.dismiss();
//            }
//        });
//        alarmDialog.show();
//    }
//
//    private void showOptionPackageDialog() {
//        final CustomDialog alarmDialog = new CustomDialog(getActivity());
//        alarmDialog.setTitle("增加行包作业");
//        alarmDialog.setMessage("是否增加行包作业");
//        alarmDialog.setOnSureClickListener("增加", new CustomDialog.OnSureClickListener() {
//            @Override
//            public void onCliclk() {
//                final String url = GlobalConstants.ADD_OPTION_URL + "/To_Bag?id="
//                        + adapter.getBean().getData().get(page).getId() + "&status=1";
//                Log.e("adapter", url);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        asyncGetAddOption(url);
//                    }
//                }).start();
//                alarmDialog.dismiss();
//            }
//        });
//        alarmDialog.setOnCancelClickListener("不增加", new CustomDialog.OnCancelClickListener() {
//            @Override
//            public void onCliclk() {
//                alarmDialog.dismiss();
//            }
//        });
//        alarmDialog.show();
//    }
//
//    public void asyncGetAddOption(String url) {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        OkHttpClient client = builder.build();
//        final Request request = new Request.Builder()
//                .url(url)
//                .build();
//        final Message message = Message.obtain();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                message.what = 0;
//                mHandler.sendMessage(message);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String msg = response.body().string();
//                Log.e("onResponse", msg);
//                try {
//                    JSONObject json = new JSONObject(msg);
//                    int code = json.optInt("code");
//                    if (code == 200) {
//                        message.what = 2;
//                        mHandler.sendMessage(message);
//                    } else {
//                        message.what = 1;
//                        mHandler.sendMessage(message);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    message.what = 1;
//                    mHandler.sendMessage(message);
//                }
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
