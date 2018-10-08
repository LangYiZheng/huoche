package adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import java.util.List;

import bean.QueryDataBean;
import view.fragment.PagerItemFragment;

/**
 * Created by Administrator on 2018/5/9/009.
 */

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private QueryDataBean bean;
    private PagerItemFragment fragment;

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        QueryDataBean.DataBean dataBean = bean.getData().get(position);
        fragment = new PagerItemFragment(dataBean);
        return fragment;
    }

    @Override
    public int getCount() {
        if (bean != null) {
            return bean.getData().size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (bean != null) {
            return bean.getData().get(position).getNumber();
        }
        return null;
    }

    public void setData(QueryDataBean bean) {
        this.bean = bean;
//            notifyDataSetChanged();
    }


    public QueryDataBean getBean() {
        return this.bean;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        fragment = ((PagerItemFragment) super.instantiateItem(container, position));
        if (bean != null) {
            fragment.setDataBean(bean.getData().get(position));
        }
        return fragment;
    }
}
