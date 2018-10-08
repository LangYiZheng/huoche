package view.wiget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by dell004 on 2018/5/31.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private float mInitialDownY;
    private int mTouchSlop;

    public MySwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //拿到触发移动最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = ev.getY();
//                Log.e("mInitialDownY",mInitialDownY+"");
                break;
            case MotionEvent.ACTION_MOVE:
                final float yDiff = ev.getY() - mInitialDownY;
//                Log.e("mInitialDownY+yDiff",yDiff+"___"+ev.getY()+"___"+mTouchSlop);
                if (yDiff < mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * @return 返回灵敏度数值
     */
    public int getTouchSlop() {
        return mTouchSlop;
    }

    /**
     * 设置下拉灵敏度
     *
     * @param mTouchSlop dip值
     */
    public void setTouchSlop(int mTouchSlop) {
        this.mTouchSlop = mTouchSlop;
    }
}
