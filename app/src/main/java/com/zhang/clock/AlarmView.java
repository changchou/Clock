package com.zhang.clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Mr.Z on 2016/10/15 0015.
 */

public class AlarmView extends LinearLayout {

    public AlarmView(Context context) {
        super(context);

        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);

        readSavedAlarmList();

        btnAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });

        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{"删除"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        deleteAlarm(position);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }).setNegativeButton("取消", null).show();

                return true;
            }
        });
    }

    private void deleteAlarm(int position) {
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();

        alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getId(),
                new Intent(getContext(), AlarmReceiver.class), 0));
    }

    private void addAlarm() {
        Calendar c = Calendar.getInstance();

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar curTime = Calendar.getInstance();

                if (calendar.getTimeInMillis() <= curTime.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }

                AlarmData data = new AlarmData(calendar.getTimeInMillis());
                adapter.add(data);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        data.getTime(),
                        5 * 60 * 1000,
                        PendingIntent.getBroadcast(getContext(), data.getId(),
                                new Intent(getContext(), AlarmReceiver.class), 0));

                saveAlarmList();

            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }

        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }
        editor.commit();
    }

    private void readSavedAlarmList() {
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);

        if (content != null) {
            String[] times = content.split(",");
            for (String time : times) {
                adapter.add(new AlarmData(Long.parseLong(time)));
            }
        }
    }

    private Button btnAddAlarm;
    private ListView lvAlarmList;
    private ArrayAdapter<AlarmData> adapter;
    private static final String KEY_ALARM_LIST = "alarmlist";
    private AlarmManager alarmManager;

    private static class AlarmData {
        AlarmData(long time) {
            this.time = time;

            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            timeLabel = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));
        }

        String getTimeLabel() {
            return timeLabel;
        }

        long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }

        int getId() {
            return (int) (getTime() / 1000 / 60);
        }

        private String timeLabel;
        private long time;
        private Calendar date;
    }
}
