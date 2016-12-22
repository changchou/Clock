package com.zhang.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Mr.Z on 2016/10/15 0015.
 */

public class TimeView extends LinearLayout {

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText("Hello");

        timeHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            timeHandler.sendEmptyMessage(0);
        } else {
            timeHandler.removeMessages(0);
        }
    }

    private void refreshTime() {

        Calendar c = Calendar.getInstance();

        tvTime.setText(String.format("%d:%d:%d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));

    }

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            refreshTime();

            if (getVisibility() == VISIBLE) {
                timeHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }


    };

    private TextView tvTime;
}
