package view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell004.myapplication.R;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import adapter.HistoryInfoAdapter;
import app.BaseApplication;
import app.GlobalConstants;
import bean.HistoryInfoJobBean;
import okhttp3.Request;
import okhttp3.Response;
import utils.NetWorkCallback;
import utils.OkHttpUtils;

/**
 * Created by dell003 on 2018/5/13.
 */

public class HistoryInfoFragment extends BaseFragment implements View.OnClickListener{

    public static final String REGEX = "yyyy-MM-dd";
    public static final String REGEX1 = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat SDF = new SimpleDateFormat(REGEX);
    public static final SimpleDateFormat SDF1 = new SimpleDateFormat(REGEX1);
    EditText etQueryNumber;
    TextView tvFromDate,tvToDate;
    Button btnQuerySubmmit;
    View header;
    ListView lvHistoryTask;
    String trainNumber, fromDate = null,toDate = null;
    Calendar fromCalendar,toCalendar;
    Date fd,td;

    Context mContext;
    HistoryInfoAdapter mAdapter;
    ArrayList<HistoryInfoJobBean.DataBean> mDataList;
    Gson gson;


    @Override
    protected void initView() {
        Log.e("HistoryInfoFragment","initView");
        etQueryNumber = viewlayout.findViewById(R.id.et_number_query_frg);
        tvFromDate = viewlayout.findViewById(R.id.tv_fromdate_query_frg);
        tvToDate = viewlayout.findViewById(R.id.tv_todate_query_frg);
        toCalendar = Calendar.getInstance();
        fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DAY_OF_MONTH,-1);
        tvFromDate.setText(SDF.format(fromCalendar.getTime()));
        fromDate = tvFromDate.getText().toString();
        tvToDate.setText(SDF.format(toCalendar.getTime()));
        toDate = tvToDate.getText().toString()+" 23:59:59";
        btnQuerySubmmit = viewlayout.findViewById(R.id.btn_find_query_frg);
        header = LayoutInflater.from(getActivity()).inflate(R.layout.lv_header_historyinfo,null);
        lvHistoryTask = viewlayout.findViewById(R.id.lv_query_frg);

        etQueryNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trainNumber = etQueryNumber.getText().toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
        btnQuerySubmmit.setOnClickListener(this);

        mContext = getActivity();
        mDataList = new ArrayList<>();
        mAdapter = new HistoryInfoAdapter(mContext,mDataList);
        lvHistoryTask.addHeaderView(header);
        lvHistoryTask.setAdapter(mAdapter);
        gson = new Gson();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public int loadView() {
        return R.layout.query_fgr;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_fromdate_query_frg:
                showFromDateDialog();
                break;
            case R.id.tv_todate_query_frg:
                showToDateDialog();
                break;
            case R.id.btn_find_query_frg:
                if(!checkDate(fromDate,toDate)){
                    Toast.makeText(getActivity(),"起始时间要早于结束时间",Toast.LENGTH_LONG).show();
                    return;
                }
                if(((BaseApplication)getActivity().getApplication()).isNetworkAvailable()){
                    QueryInfo();
                }else {
                    Toast.makeText(getActivity(),"网络未连接，请检查网络！",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private boolean checkDate(String fromDate, String toDate) {
        try {
            fd = SDF.parse(fromDate);
            td = SDF1.parse(toDate);

            return fd.before(td);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void QueryInfo() {
        try {
            fd = SDF.parse(fromDate);
            td = SDF1.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String url = GlobalConstants.HISTORY_QUERY_URL;
        Map<String, String> params = new HashMap<>();
        params.put("point", (fd.getTime() / 1000) + "");
        params.put("late", (td.getTime() / 1000) + "");
        OkHttpUtils.getinstance().postWithFormData(url, params, new NetWorkCallback<HistoryInfoJobBean>() {
            @Override
            public void onLoadingBefore(Request request) {}

            @Override
            public void onSuccess(Response response, HistoryInfoJobBean result) {
                if (result.getData().size() > 0) {
                    mDataList.clear();
                    ArrayList<HistoryInfoJobBean.DataBean> list = result.getData();
                    if (!TextUtils.isEmpty(trainNumber)) {
                        for (HistoryInfoJobBean.DataBean dataBean : list) {
                            if (dataBean.getNumber().contains(trainNumber)) {
                                mDataList.add(dataBean);
                            }
                        }
                    }else {
                        mDataList = result.getData();
                    }
                    mAdapter.setmHistoryDataList(mDataList);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"查询到"+mDataList.size()+"条匹配数据！",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),"查询到0条匹配数据！",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Response response) {}

            @Override
            public void onFailure(Request request, Exception e) {
                Toast.makeText(getActivity(), "网络异常，请求失败！", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showToDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvToDate.setText(new StringBuffer().append(year).append("-")
                        .append((month+1) < 10 ? "0"+(month+1) : ""+(month+1))
                        .append("-").append((dayOfMonth+1) < 10 ? "0"+dayOfMonth : ""+dayOfMonth));
                toDate = tvToDate.getText().toString()+" 23:59:59";
            }
        }, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showFromDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvFromDate.setText(new StringBuffer().append(year).append("-")
                .append((month+1) < 10 ? "0"+(month+1) : ""+(month+1))
                .append("-").append((dayOfMonth+1) < 10 ? "0"+dayOfMonth : ""+dayOfMonth));
                fromDate = tvFromDate.getText().toString();
            }
        }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
