package com.zhang.clock;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mr.Z on 2016/10/17 0017.
 */

public class TimerView extends LinearLayout {

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        etHour = (EditText) findViewById(R.id.etHour);
        etMin = (EditText) findViewById(R.id.etMin);
        etSec = (EditText) findViewById(R.id.etSec);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnResume = (Button) findViewById(R.id.btnResume);
        btnReset = (Button) findViewById(R.id.btnReset);

        etHour.setText("00");
        etMin.setText("00");
        etSec.setText("00");

        btnStart.setVisibility(VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(GONE);
        btnResume.setVisibility(GONE);
        btnReset.setVisibility(GONE);

        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        etMin.setText("59");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        etSec.setText("59");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
                btnReset.setVisibility(VISIBLE);
            }
        });

        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();

                btnPause.setVisibility(GONE);
                btnResume.setVisibility(VISIBLE);
                btnReset.setVisibility(VISIBLE);
            }
        });

        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnResume.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
                btnReset.setVisibility(VISIBLE);
            }
        });

        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();

                etHour.setText("00");
                etMin.setText("00");
                etSec.setText("00");

                btnStart.setVisibility(VISIBLE);
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(GONE);
                btnReset.setVisibility(GONE);
            }
        });
    }

    private void checkToEnableBtnStart() {
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
    }

    private void startTimer() {
        if (timerTask == null) {
            allTimerCount = Integer.parseInt(etHour.getText().toString()) * 60 * 60 +
                    Integer.parseInt(etMin.getText().toString()) * 60 +
                    Integer.parseInt(etSec.getText().toString());
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    allTimerCount--;

                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);

                    if (allTimerCount <= 0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }

                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void onDestroy(){
        timer.cancel();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_TIME_IS_UP:

                    new AlertDialog.Builder(getContext()).setMessage("Time is up").
                            setNegativeButton("Cancel", null).show();

                    btnStart.setVisibility(VISIBLE);
                    btnPause.setVisibility(GONE);
                    btnResume.setVisibility(GONE);
                    btnReset.setVisibility(GONE);

                    break;
                case MSG_WHAT_TIME_TICK:

                    int hour = allTimerCount / 60 / 60;
                    int min = (allTimerCount / 60) % 60;
                    int sec = allTimerCount % 60;

                    etHour.setText(hour + "");
                    etMin.setText(min + "");
                    etSec.setText(sec + "");

                    break;
            }
        }
    };

    private EditText etHour, etMin, etSec;
    private Button btnStart, btnPause, btnResume, btnReset;

    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private int allTimerCount = 0;

    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;

}
