package com.example.thanh.wear_widget;

/**
 * Created by thanh on 3/4/2018.
 */


import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyBatteryReceiver extends Service {

    private int batterylevel = 0;
    private String batteryStatus ="";


    private BroadcastReceiver myReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED))
            {
                RemoteViews mViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                batterylevel = intent.getIntExtra("level", 0);

                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                String strStatus;
                if (status == BatteryManager.BATTERY_STATUS_CHARGING){
                    batteryStatus = "Charging";
                } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                    batteryStatus = "Dis-charging";
                } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                    batteryStatus = "Not charging";
                } else if (status == BatteryManager.BATTERY_STATUS_FULL){
                    batteryStatus = "Full";
                } else {
                    batteryStatus = "";
                }
                updateAppWidget(context);
            }
        }

        public void updateAppWidget(Context context){

            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Calendar d = Calendar.getInstance();
            int minute = d.get(Calendar.MINUTE);
            int hour = d.get(Calendar.HOUR_OF_DAY);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            updateViews.setTextViewText(R.id.LC,""+formattedDate);
            updateViews.setTextViewText(R.id.time2,""+formattedDate);

            if(hour<10)
            {
                updateViews.setTextViewText(R.id.timedialogh,"0"+hour);

                updateViews.setTextViewText(R.id.timedialogh2,"0"+hour);
            }

            if(minute<10)
            {
                updateViews.setTextViewText(R.id.timedialogm,"   : 0"+minute);

                updateViews.setTextViewText(R.id.timedialogm2,": 0"+minute);
            }

            if(hour>=10)
            {
                updateViews.setTextViewText(R.id.timedialogh,""+hour);

                updateViews.setTextViewText(R.id.timedialogh2,""+hour);
            }
            if(minute>=10)
            {
                updateViews.setTextViewText(R.id.timedialogm,"   : "+minute);

                updateViews.setTextViewText(R.id.timedialogm2,": "+minute);
            }



           if( batteryStatus=="Full")
            {
                updateViews.setImageViewResource(R.id.pin,R.drawable.if_fully_charged_battery);

                ProgressPlayUpdateAppWidget(context, 360, 100);


            }
            if(batterylevel<25)
            {
                updateViews.setImageViewResource(R.id.pin,R.drawable.if_low_battery);

            }
            else if(batterylevel<50)
            {
                updateViews.setImageViewResource(R.id.pin,R.drawable.if_medium_battery);


            }
            else if(batterylevel<75)
            {
                updateViews.setImageViewResource(R.id.pin,R.drawable.if_high_battery);

            }
            else
            if(batterylevel<=100)
            {
                updateViews.setImageViewResource(R.id.pin,R.drawable.if_fully_charged_battery);

            }

            ProgressPlayUpdateAppWidget(context, 360, batterylevel);

            updateViews.setTextViewText(R.id.textpin,""+batterylevel+"%");
            updateViews.setTextViewText(R.id.powerpintext,""+batterylevel+"%");

            ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(myComponentName, updateViews);
        }
    };


    public void ProgressPlayUpdateAppWidget(final Context context,int max,int run){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setProgressBar(R.id.circular_progress_bar2,max,run,false);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

}