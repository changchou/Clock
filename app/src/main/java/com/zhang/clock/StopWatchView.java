package com.zhang.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mr.Z on 2016/10/17 0017.
 */

public class StopWatchView extends LinearLayout {

    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvHour = (TextView) findViewById(R.id.timeHour);
        tvMin = (TextView) findViewById(R.id.timeMin);
        tvSec = (TextView) findViewById(R.id.timeSec);
        tvMSec = (TextView) findViewById(R.id.timeMSec);
        tvHour.setText("0");
        tvMin.setText("0");
        tvSec.setText("0");
        tvMSec.setText("0");

        btnStart = (Button) findViewById(R.id.btnSWStart);
        btnPause = (Button) findViewById(R.id.btnSWPause);
        btnResume = (Button) findViewById(R.id.btnSWResume);
        btnReset = (Button) findViewById(R.id.btnSWReset);
        btnLap = (Button) findViewById(R.id.btnSWLap);
        btnPause.setVisibility(GONE);
        btnResume.setVisibility(GONE);
        btnReset.setVisibility(GONE);
        btnLap.setVisibility(GONE);

        lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);

        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
//                btnResume.setVisibility(GONE);
//                btnReset.setVisibility(GONE);
                btnLap.setVisibility(VISIBLE);
            }
        });
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();

//                btnStart.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(VISIBLE);
                btnReset.setVisibility(VISIBLE);
                btnLap.setVisibility(GONE);
            }
        });
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

//                btnStart.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
                btnResume.setVisibility(GONE);
                btnReset.setVisibility(GONE);
                btnLap.setVisibility(VISIBLE);
            }
        });
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                tenMSec = 0;
                adapter.clear();

                btnStart.setVisibility(VISIBLE);
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(GONE);
                btnReset.setVisibility(GONE);
                btnLap.setVisibility(GONE);
            }
        });
        btnLap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.insert(String.format("%d:%d:%d.%d",
                        tenMSec / 100 / 60 / 60,
                        tenMSec / 100 / 60 % 60,
                        tenMSec / 100 % 60,
                        tenMSec % 100), 0);
            }
        });

        if (showTimerTask == null) {
            showTimerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
                }
            };
            timer.schedule(showTimerTask, 200, 200);
        }
    }

    private void startTimer() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    tenMSec++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void onDestroy() {
        timer.cancel();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSec / 100 / 60 / 60 + "");
                    tvMin.setText(tenMSec / 100 / 60 % 60 + "");
                    tvSec.setText(tenMSec / 100 % 60 + "");
                    tvMSec.setText(tenMSec % 100 + "");
                    break;
            }
        }
    };

    private TextView tvHour, tvMin, tvSec, tvMSec;
    private Button btnStart, btnPause, btnResume, btnLap, btnReset;
    private ListView lvTimeList;
    private ArrayAdapter<String> adapter;

    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private TimerTask showTimerTask = null;
    private int tenMSec = 0;

    private static final int MSG_WHAT_SHOW_TIME = 1;
}
