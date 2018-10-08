package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell004.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bean.HistoryInfoJobBean;

/**
 * Created by dell003 on 2018/5/15.
 */

public class HistoryInfoAdapter extends BaseAdapter {

    public static final String REGEX1 = "yyyy-MM-dd";
    public static final String REGEX2 = "hh:mm";
    Context mContext;
    LayoutInflater mInflater;
    List<HistoryInfoJobBean.DataBean> mHistoryDataList ;
    SimpleDateFormat sdf;

    public HistoryInfoAdapter(Context mContext, List<HistoryInfoJobBean.DataBean> mHistoryDataList) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mHistoryDataList = mHistoryDataList;
    }

    public void setmHistoryDataList(List<HistoryInfoJobBean.DataBean> mHistoryDataList){
        this.mHistoryDataList = mHistoryDataList;
    }

    @Override
    public int getCount() {
        return mHistoryDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistoryDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.lv_query_history_item, null);
            holder = new ViewHolder();
            holder.tvNumber = convertView.findViewById(R.id.tv_history_train_num);
            holder.tvStartAndTerminus = convertView.findViewById(R.id.tv_history_startandterminus);
            holder.tvDate = convertView.findViewById(R.id.tv_history_train_date);
            holder.tvArriveTime = convertView.findViewById(R.id.tv_history_train_arrivetime);
            holder.tvLeaveTime = convertView.findViewById(R.id.tv_history_train_leavetime);
            holder.tvRegion = convertView.findViewById(R.id.tv_history_region);
            holder.tvStock = convertView.findViewById(R.id.tv_history_stock);
            holder.tvDutyNumber = convertView.findViewById(R.id.tv_history_duty_number);
            holder.tvDytyTime = convertView.findViewById(R.id.tv_history_duty_time);
            holder.tvWaiterNumber = convertView.findViewById(R.id.tv_history_waiteroom_number);
            holder.tvWaiterTime = convertView.findViewById(R.id.tv_history_waiteroom_time);
            holder.tvTeaNumber = convertView.findViewById(R.id.tv_history_tea_number);
            holder.tvTeaTime = convertView.findViewById(R.id.tv_history_tea_time);
            holder.tvVipNumber = convertView.findViewById(R.id.tv_history_vip_number);
            holder.tvVipTime = convertView.findViewById(R.id.tv_history_vip_time);
            holder.tvWaterNumber = convertView.findViewById(R.id.tv_history_package_number);
            holder.tvWaterTime = convertView.findViewById(R.id.tv_history_package_time);
            holder.tvPackageNumber = convertView.findViewById(R.id.tv_history_water_number);
            holder.tvPackageTime = convertView.findViewById(R.id.tv_history_water_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mHistoryDataList.size() > 0) {
            HistoryInfoJobBean.DataBean bean = mHistoryDataList.get(position);
            holder.tvNumber.setText(bean.getNumber());
            holder.tvStartAndTerminus.setText(bean.getDeparture()+"-"+bean.getTerminus());
            holder.tvDate.setText(parseTime(REGEX1,bean.getPoint()));
            holder.tvArriveTime.setText(parseTime(REGEX2,bean.getPoint()));
            holder.tvLeaveTime.setText(parseTime(REGEX2,bean.getLate()));
            holder.tvRegion.setText(bean.getCar_name());
            holder.tvStock.setText(bean.getMarket_name());
            holder.tvDutyNumber.setText(bean.getPassenger_number());
            holder.tvDytyTime.setText("--:--");
            holder.tvWaiterNumber.setText(bean.getCar_name());
            holder.tvWaiterTime.setText("--:--");
            holder.tvTeaNumber.setText(bean.getTeahouse_number());
            holder.tvTeaTime.setText("--:--");
            holder.tvVipNumber.setText(bean.getVip_number());
            holder.tvVipTime.setText("--:--");
            holder.tvWaterNumber.setText(bean.getWater_number());
            holder.tvWaterTime.setText("--:--");
            holder.tvPackageNumber.setText(bean.getBag_number());
            holder.tvPackageTime.setText("--:--");
        }

        return convertView;
    }

    public String parseTime(String regex,String d){
        sdf = new SimpleDateFormat(regex);
        long t = Long.parseLong(d)*1000;
        Date date = new Date(t);
        return sdf.format(date);
    }

    class ViewHolder {
        TextView tvNumber;
        TextView tvStartAndTerminus;
        TextView tvDate;
        TextView tvArriveTime;
        TextView tvLeaveTime;
        TextView tvRegion;
        TextView tvStock;
        TextView tvDutyNumber;
        TextView tvDytyTime;
        TextView tvWaiterNumber;
        TextView tvWaiterTime;
        TextView tvTeaNumber;
        TextView tvTeaTime;
        TextView tvPackageNumber;
        TextView tvPackageTime;
        TextView tvWaterNumber;
        TextView tvWaterTime;
        TextView tvVipNumber;
        TextView tvVipTime;
    }
}
