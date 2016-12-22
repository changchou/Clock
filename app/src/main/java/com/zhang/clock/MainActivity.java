package com.zhang.clock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("时钟").setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("闹钟").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("计时器").setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("秒表").setContent(R.id.tabStopWatch));

        timerView = (TimerView) findViewById(R.id.tabTimer);
        stopWatchView = (StopWatchView) findViewById(R.id.tabStopWatch);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timerView.onDestroy();
        stopWatchView.onDestroy();
    }

    private TabHost tabHost;
    private TimerView timerView;
    private StopWatchView stopWatchView;
}
