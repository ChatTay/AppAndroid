package com.example.thanh.wear_widget;

/**
 * Created by thanh on 3/1/2018.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MyWidgetProvider extends AppWidgetProvider{


    public static final String ACTION_TOAST = ".ACTION_TOAST";
    public static final String EXTRA_STRING = ".EXTRA_STRING";
    public static final String EXTRA_ITEM = ".EXTRA_ITEM";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    private static final String next = "next";
    private static final String pre = "pre";
    private static String control = "clock";
    private static String search = "search";
    private static String random= "noRandom";
    private static String Completion= "noCompletion";


    private static String  face="noFace";
    private static String  path="";
    private static int  position=10;
    private static int item=0;
    private static int max=30000;
    private static int run=10;
    private static int volume=5;

    private static String time = "time";


    private final String playSong = "PlaySong";
    private final String playSong1 = "PlaySong1";
    private final String nextSong = "NetxtSong";
    private final String preSong = "PreSong";
    private final String music = "Music";
    private final String  backhome= "BackHome";
    private final String  listview= "ListView";
    private final String  backlist= "BackList";
    private final String  random1= "Random1";
    private final String  random2= "Random2";
    private final String menu = "Menu";


    SongsManager sm;
    private static CountDownTimer ctime;

    private static final ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private static final ArrayList<HashMap<String, String>> ImageList = new ArrayList<HashMap<String, String>>();
    private static String mp3Pattern = ".mp3";

    /*int arr[]={R.drawable.image0000001,R.drawable.image0000002,R.drawable.image0000003,R.drawable.image0000004
            ,R.drawable.image0000005,R.drawable.image0000006,R.drawable.image0000007,R.drawable.image0000008,R.drawable.image0000009,R.drawable.image0000010
            ,R.drawable.image0000011,R.drawable.image0000012,R.drawable.image0000013,R.drawable.image0000014,R.drawable.image0000015,R.drawable.image0000016,R.drawable.image0000017
            ,R.drawable.image0000018,R.drawable.image0000019,R.drawable.image0000020,R.drawable.image0000021,R.drawable.image0000022
            ,R.drawable.image0000023,R.drawable.image0000024,R.drawable.image0000025,R.drawable.image0000026,R.drawable.image0000027,R.drawable.image0000028,R.drawable.image0000029,R.drawable.image0000030,R.drawable.image0000031,R.drawable.image0000032,R.drawable.image0000033,R.drawable.image0000034,R.drawable.image0000035
            ,R.drawable.image0000036,R.drawable.image0000037,R.drawable.image0000038,R.drawable.image0000039,R.drawable.image0000040,R.drawable.image0000041,R.drawable.image0000042,R.drawable.image0000043,R.drawable.image0000044,R.drawable.image0000045,R.drawable.image0000046,R.drawable.image0000047,R.drawable.image0000048

    };*/

    int arr[]={R.drawable.image0000001,R.drawable.image0000002,R.drawable.image0000003,R.drawable.image0000004
            ,R.drawable.image0000005,R.drawable.image0000006,R.drawable.image0000007,R.drawable.image0000008,R.drawable.image0000009,R.drawable.image0000010
            ,R.drawable.image0000011,R.drawable.image0000012,R.drawable.image0000013,R.drawable.image0000014,R.drawable.image0000015,R.drawable.image0000016,R.drawable.image0000017
            ,R.drawable.image0000018,R.drawable.image0000019,R.drawable.image0000020,R.drawable.image0000021,R.drawable.image0000022
            ,R.drawable.image0000023,R.drawable.image0000024
    };



    AudioManager audioManager;

    private static MediaPlayer mp;

    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);




        songsList.clear();
        sm=new SongsManager();

        int x=sm.getPlayList().size();

        audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        String action = intent.getAction();

        ItemUpdateAppWidget(context);

        if(random.equalsIgnoreCase("Random"))
        {
            RandomstopUpdateAppWidget(context);
        }
        else
        {
            RandomplayUpdateAppWidget(context);
        }

        if(search.equalsIgnoreCase("search"))
        {
            ctime=new CountDownTimer(max-run, 1000)
            {
                public void onTick(long millisUntilFinished) {
                    ProgressPlayUpdateAppWidget(context,max,(max-(int)millisUntilFinished)+run);
                    //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                }
                public void onFinish() {
                    ProgressPlayUpdateAppWidget(context,max,0);
                }
            };
            ctime.start();
            ctime.cancel();

            search="thanh";

        }

        for(int i=1;i<=20;i++)
        {
            RemoteViews ImageView1 = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            RemoteViews Image1 = new RemoteViews(context.getPackageName(), R.layout.image_sound_waves);

            Image1.setImageViewResource(R.id.imageitem,arr[i]);
            ImageView1.addView(R.id.soundwaves,Image1);

            Image1.setImageViewResource(R.id.imageitem,arr[i]);
            ImageView1.addView(R.id.soundwaves1,Image1);

            ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
            AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
            manager1.updateAppWidget(myComponentName1, ImageView1);
        }

        if (intent.getAction().equals(ACTION_TOAST))
        {
            SoundOnUpdateAppWidget(context);
            path=intent.getExtras().getString(EXTRA_STRING);
            String i=intent.getExtras().getString(EXTRA_ITEM);
            item=Integer.parseInt(i);

            ItemUpdateAppWidget(context);
            ImagestopUpdateAppWidget(context);
            mp.stop();
            mp = MediaPlayer.create(context.getApplicationContext(),Uri.parse(path));
            mp.start();
            max=mp.getDuration();
            run=0;
            ctime.cancel();
            ctime=new CountDownTimer(max-run, 1000) {

                public void onTick(long millisUntilFinished) {
                    ProgressPlayUpdateAppWidget(context,max,(max-(int)millisUntilFinished)+run);
                    //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                }

                public void onFinish() {
                    ProgressPlayUpdateAppWidget(context,max,0);
                }
            };
            ctime.start();

            Log.e("thanh","thanh :"+songsList.size());

        }

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent AlarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.alarmclock", "com.android.alarmclock.AlarmClock"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, AlarmClockIntent, 0);
            AppWidgetManager.getInstance(context).updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
        }
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent AlarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(new ComponentName("com.android.alarmclock", "com.android.alarmclock.AlarmClock"));
            AppWidgetManager.getInstance(context).updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
        }
        if (next.equals(intent.getAction())) {
            if(control.equalsIgnoreCase("clock"))
            {
                //Toast.makeText(context, "next ", Toast.LENGTH_LONG).show();
                MenuUpdateAppWidget(context);
                control="menu";
            }
            else if(control.equalsIgnoreCase("menu"))
            {
                //Toast.makeText(context, "next ", Toast.LENGTH_LONG).show();
                MusicUpdateAppWidget(context);
                control="music";
            }
            else if(control.equalsIgnoreCase("playmusic"))
            {
                //Toast.makeText(context, "Tang", Toast.LENGTH_LONG).show();
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            }
        }
        if (pre.equals(intent.getAction())) {
            if(control.equalsIgnoreCase("clock"))
            {

            }
            else if(control.equalsIgnoreCase("menu"))
            {
                //Toast.makeText(context, "pre ", Toast.LENGTH_LONG).show();
                ClockUpdateAppWidget(context);
                control="clock";
            }
            else if(control.equalsIgnoreCase("music"))
            {
                // Toast.makeText(context, "pre ", Toast.LENGTH_LONG).show();
                MenuUpdateAppWidget(context);
                control="menu";
            }
            else if(control.equalsIgnoreCase("playmusic"))
            {
                //Toast.makeText(context,"Giam",Toast.LENGTH_LONG).show();
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

            }

        }

        if (mp == null)
            mp = MediaPlayer.create(context.getApplicationContext(),Uri.parse(songsList.get(0).get("songPath")));

        if(playSong.equals(intent.getAction()))
        {

            if(mp.isPlaying())
            {
                SoundOffUpdateAppWidget(context);
                ImageplayUpdateAppWidget(context);
                position = mp.getCurrentPosition();
                run=position;
                mp.stop();
                ctime.cancel();
                ItemUpdateAppWidget(context);

            }else
            {
                if(item<(songsList.size() - 1)) {
                    SoundOnUpdateAppWidget(context);
                    ImagestopUpdateAppWidget(context);
                    // mp.stop();
                    ItemUpdateAppWidget(context);
                    if (path.equalsIgnoreCase("")) {
                        mp = MediaPlayer.create(context.getApplicationContext(), Uri.parse(songsList.get(0).get("songPath")));
                    } else {
                        mp = MediaPlayer.create(context.getApplicationContext(), Uri.parse(path));
                    }
                    mp.seekTo(run);
                    mp.start();
                    max = mp.getDuration();
                    ctime.cancel();
                    ctime = new CountDownTimer(max - run, 1000) {

                        public void onTick(long millisUntilFinished) {
                            ProgressPlayUpdateAppWidget(context, max, (max - (int) millisUntilFinished)
                            );
                            //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                        }

                        public void onFinish() {
                            ProgressPlayUpdateAppWidget(context, max, 0);
                        }
                    };
                    ctime.start();
                }
            }
        }

        if(playSong1.equals(intent.getAction()))
        {
            //ImagestopUpdateAppWidget(context);
            if(mp.isPlaying())
            {
                SoundOffUpdateAppWidget(context);
                ImageplayUpdateAppWidget(context);
                position = mp.getCurrentPosition();
                run=position;
                mp.stop();
                ctime.cancel();
                ItemUpdateAppWidget(context);
            }else
            {
                if(item<(songsList.size() - 1)) {
                    SoundOnUpdateAppWidget(context);
                    ImagestopUpdateAppWidget(context);
                    ItemUpdateAppWidget(context);

                    if (path.equalsIgnoreCase("")) {
                        mp = MediaPlayer.create(context.getApplicationContext(), Uri.parse(songsList.get(0).get("songPath")));
                    } else {
                        mp = MediaPlayer.create(context.getApplicationContext(), Uri.parse(path));
                    }

                    mp.seekTo(run);
                    mp.start();
                    max = mp.getDuration();
                    ctime.cancel();
                    ctime = new CountDownTimer(max - run, 1000) {

                        public void onTick(long millisUntilFinished) {
                            ProgressPlayUpdateAppWidget(context, max, (max - (int) millisUntilFinished)
                            );
                            //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                        }

                        public void onFinish() {
                            ProgressPlayUpdateAppWidget(context, max, 0);
                        }
                    };
                    ctime.start();
                }
            }
        }
        if(preSong.equals(intent.getAction()))
        {
            if(item>0)
            {
                SoundOnUpdateAppWidget(context);
                path=songsList.get(item-1).get("songPath");
                item--;
                ImagestopUpdateAppWidget(context);
                mp.stop();
                mp = MediaPlayer.create(context.getApplicationContext(),Uri.parse(path));
                mp.start();
                ItemUpdateAppWidget(context);
                max=mp.getDuration();
                run=0;
                ctime.cancel();

                ctime=new CountDownTimer(max-run, 1000) {
                    public void onTick(long millisUntilFinished) {
                        ProgressPlayUpdateAppWidget(context,max,(max-(int)millisUntilFinished));
                        //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                    }
                    public void onFinish() {
                        ProgressPlayUpdateAppWidget(context,max,0);
                    }
                };
                ctime.start();
            }

            //Toast.makeText(context, ":pre:", Toast.LENGTH_LONG).show();
        }
        if(nextSong.equals(intent.getAction()))
        {
            if(item<(songsList.size()-1))
            {
                SoundOnUpdateAppWidget(context);
                path=songsList.get(item+1).get("songPath");
                item++;
                ImagestopUpdateAppWidget(context);

                mp.stop();
                mp = MediaPlayer.create(context.getApplicationContext(),Uri.parse(path));
                mp.start();
                ItemUpdateAppWidget(context);
                max=mp.getDuration();
                run=0;
                ctime.cancel();
                ctime=new CountDownTimer(max-run, 1000) {
                    public void onTick(long millisUntilFinished) {
                        ProgressPlayUpdateAppWidget(context,max,(max-(int)millisUntilFinished));
                        //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                    }
                    public void onFinish() {
                        ProgressPlayUpdateAppWidget(context,max,0);
                    }
                };
                ctime.start();

            }

        }

        if(music.equals(intent.getAction()))
        {
            // Toast.makeText(context, ":musci:", Toast.LENGTH_LONG).show();
            MusicPlayUpdateAppWidget(context);
            control="playmusic";
        }
        if(backhome.equals(intent.getAction()))
        {
            // Toast.makeText(context, ":musci:", Toast.LENGTH_LONG).show();
            MusicUpdateAppWidget(context);
            control="music";
        }
        if(listview.equals(intent.getAction()))
        {
            // Toast.makeText(context, ":musci:", Toast.LENGTH_LONG).show();
            ListMusicPlayUpdateAppWidget(context);
            control="playmusic";
        }
        if(backlist.equals(intent.getAction()))
        {
            // Toast.makeText(context, ":musci:", Toast.LENGTH_LONG).show();
            MusicPlayUpdateAppWidget(context);
            control="playmusic";
        }

        if(random1.equals(intent.getAction()))
        {
            RandomplayUpdateAppWidget(context);
            random="noRandom";

            //Toast.makeText(context,"randrom :"+random,Toast.LENGTH_SHORT).show();
        }

        if(random2.equals(intent.getAction()))
        {
            RandomstopUpdateAppWidget(context);
            random="Random";

            ////Toast.makeText(context,"randrom :"+random,Toast.LENGTH_SHORT).show();
        }

       if(mp!=null)
       {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    Random rand = new Random();
                    if (random.equalsIgnoreCase("noRandom")) {

                        if (item < (songsList.size()-1)) {
                            ImagestopUpdateAppWidget(context);
                            item++;
                            path = songsList.get(item).get("songPath");


                            mp.stop();

                            mp = MediaPlayer.create(context.getApplicationContext(), Uri.parse(path));
                            mp.start();
                            ItemUpdateAppWidget(context);
                            max = mp.getDuration();
                            run = 0;
                            ctime.cancel();
                            ctime = new CountDownTimer(max - run, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    ProgressPlayUpdateAppWidget(context, max, (max - (int) millisUntilFinished));
                                    //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                                }

                                public void onFinish() {
                                    ProgressPlayUpdateAppWidget(context, max, 0);
                                }
                            };
                            ctime.start();
                        }
                        else
                        {
                            mp.stop();
                            ctime.cancel();
                            ImageplayUpdateAppWidget(context);

                        }
                        SoundOnUpdateAppWidget(context);

                    }

                    if (random.equalsIgnoreCase("Random")) {

                        SoundOnUpdateAppWidget(context);

                        item = rand.nextInt(songsList.size() - 1) + 0;
                        path =songsList.get(item).get("songPath");
                        ImagestopUpdateAppWidget(context);
                        mp.stop();
                        mp = MediaPlayer.create(context.getApplicationContext(), Uri.parse(path));
                        mp.start();
                        ItemUpdateAppWidget(context);
                        max = mp.getDuration();
                        run = 0;
                        ctime.cancel();
                        ctime = new CountDownTimer(max - run, 1000) {
                            public void onTick(long millisUntilFinished) {
                                ProgressPlayUpdateAppWidget(context, max, (max - (int) millisUntilFinished));
                                //Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();
                            }

                            public void onFinish() {
                                ProgressPlayUpdateAppWidget(context, max, 0);
                            }
                        };
                        ctime.start();


                    }

                    onReceive(context,intent);
                }
            });
        }

        if(menu.equals(intent.getAction()))
        {
          if(face.equalsIgnoreCase("noFace"))
            {

            ImageList.clear();
            ImagesManager im=new ImagesManager();
            int tt=im.getPlayList().size();

            for(int i=0;i<ImageList.size();i++)
            {
                RemoteViews ImageView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                RemoteViews Image = new RemoteViews(context.getPackageName(), R.layout.image);Bitmap bmImg1 = BitmapFactory.decodeFile(ImageList.get(i).get("imagePath"));
                ImageView.setViewVisibility(R.id.slidingimage,View.VISIBLE);
                Image.setImageViewBitmap(R.id.imageitem,getCircularBitmap(bmImg1));
                ImageView.addView(R.id.slidingimage,Image);
                ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(myComponentName, ImageView);

            }
            face="Face";
            //Toast.makeText(context,""+ImageList.size(),Toast.LENGTH_SHORT).show();
            }
            else if(face.equalsIgnoreCase("Face"))
            {
                RemoteViews ImageView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                ImageView.setViewVisibility(R.id.slidingimage,View.GONE);
                ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(myComponentName, ImageView);
                face="noFace";
            }
        }

        Toast.makeText(context,"cham",Toast.LENGTH_SHORT).show();
    }

    public void SoundOnUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.soundwaves,View.VISIBLE);
        ClockViews.setViewVisibility(R.id.soundwaves1,View.VISIBLE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void SoundOffUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.soundwaves,View.GONE);
        ClockViews.setViewVisibility(R.id.soundwaves1,View.GONE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }


    public void RandomplayUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.ramdom,View.GONE);
        ClockViews.setViewVisibility(R.id.ramdom1,View.VISIBLE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }

    public void RandomstopUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.ramdom1,View.GONE);
        ClockViews.setViewVisibility(R.id.ramdom,View.VISIBLE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }

    public void ImageplayUpdateAppWidget(final Context context){
        RemoteViews ClockViews1 = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews1.setViewVisibility(R.id.pause1,View.GONE);
        ClockViews1.setViewVisibility(R.id.pause,View.VISIBLE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews1);
    }
    public void ImagestopUpdateAppWidget(final Context context){
        RemoteViews ClockViews1 = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews1.setViewVisibility(R.id.pause1,View.VISIBLE);
        ClockViews1.setViewVisibility(R.id.pause,View.GONE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews1);
    }

    public void ItemUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setTextViewText(R.id.namesong,""+songsList.get(item).get("songTitle"));
        ClockViews.setTextViewText(R.id.singer,""+songsList.get(item).get("songSinger"));

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void ProgressPlayUpdateAppWidget(final Context context,int max,int run){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setProgressBar(R.id.circular_progress_bar,max,run,false);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void ListMusicPlayUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.playmusic,View.GONE);
        ClockViews.setViewVisibility(R.id.clock_view, View.GONE);
        ClockViews.setViewVisibility(R.id.menu_view, View.GONE);
        ClockViews.setViewVisibility(R.id.music_view,View.GONE);
        ClockViews.setViewVisibility(R.id.listview,View.VISIBLE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void MusicPlayUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.playmusic,View.VISIBLE);
        ClockViews.setViewVisibility(R.id.clock_view, View.GONE);
        ClockViews.setViewVisibility(R.id.menu_view, View.GONE);
        ClockViews.setViewVisibility(R.id.music_view,View.GONE);
        ClockViews.setViewVisibility(R.id.listview,View.GONE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void ClockUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.clock_view, View.VISIBLE);
        ClockViews.setViewVisibility(R.id.menu_view, View.GONE);
        ClockViews.setViewVisibility(R.id.music_view,View.GONE);
        ClockViews.setViewVisibility(R.id.playmusic,View.GONE);
        ClockViews.setViewVisibility(R.id.listview,View.GONE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void MenuUpdateAppWidget(Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.clock_view, View.GONE);
        ClockViews.setViewVisibility(R.id.menu_view, View.VISIBLE);
        ClockViews.setViewVisibility(R.id.music_view,View.GONE);
        ClockViews.setViewVisibility(R.id.playmusic,View.GONE);
        ClockViews.setViewVisibility(R.id.listview,View.GONE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void MusicUpdateAppWidget(Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.clock_view, View.GONE);
        ClockViews.setViewVisibility(R.id.menu_view, View.GONE);
        ClockViews.setViewVisibility(R.id.music_view,View.VISIBLE);
        ClockViews.setViewVisibility(R.id.playmusic,View.GONE);
        ClockViews.setViewVisibility(R.id.listview,View.GONE);

        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        updateWidget(context);
        context.startService(new Intent(context,MyBatteryReceiver.class));

        /*for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            remoteViews.setOnClickPendingIntent(R.id.next, getPendingSelfIntent(context, next));
            remoteViews.setOnClickPendingIntent(R.id.pre, getPendingSelfIntent(context, pre));

            remoteViews.setOnClickPendingIntent(R.id.btmusic,getPendingSelfIntent(context,music));
            remoteViews.setOnClickPendingIntent(R.id.backhome,getPendingSelfIntent(context,backhome));
            remoteViews.setOnClickPendingIntent(R.id.btlistview,getPendingSelfIntent(context,listview));
            remoteViews.setOnClickPendingIntent(R.id.btbacklist,getPendingSelfIntent(context,backlist));

            remoteViews.setOnClickPendingIntent(R.id.nexx,getPendingSelfIntent(context,nextSong));
            remoteViews.setOnClickPendingIntent(R.id.pause,getPendingSelfIntent(context,playSong));
            remoteViews.setOnClickPendingIntent(R.id.pause1,getPendingSelfIntent(context,playSong));
            remoteViews.setOnClickPendingIntent(R.id.pree,getPendingSelfIntent(context,preSong));

            remoteViews.setOnClickPendingIntent(R.id.ramdom,getPendingSelfIntent(context,random1));
            remoteViews.setOnClickPendingIntent(R.id.ramdom1,getPendingSelfIntent(context,random2));

            remoteViews.setOnClickPendingIntent(R.id.btmenu,getPendingSelfIntent(context,menu));


            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

        for (int widgetId : appWidgetIds) {

            RemoteViews mView = initViews(context, appWidgetManager, widgetId);

            final Intent onItemClick = new Intent(context, MyWidgetProvider.class);
            onItemClick.setAction(ACTION_TOAST);
            onItemClick.setData(Uri.parse(onItemClick
                    .toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent onClickPendingIntent = PendingIntent
                    .getBroadcast(context, 0, onItemClick,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.list_view,
                    onClickPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, mView);
        }*/

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {
        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        Intent intent = new Intent(context, ListViewWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.list_view, intent);
        return mView;
    }
    public void updateWidget(Context context){

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        updateViews.setTextViewText(R.id.LC, "waiting!");
        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, updateViews);

    }


    public class SongsManager {

        public SongsManager() {

        }
        public ArrayList<HashMap<String, String>> getPlayList() {

            File home = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/NCT");
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {

                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
            return songsList;
        }

        private void scanDirectory(File directory) {
            if (directory != null) {
                File[] listFiles = directory.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file : listFiles) {
                        if (file.isDirectory()) {
                            scanDirectory(file);
                        } else {
                            addSongToList(file);
                        }

                    }
                }
            }
        }
        private void addSongToList(File song) {
            if (song.getName().endsWith(mp3Pattern)) {
                HashMap<String, String> songMap = new HashMap<String, String>();
                if (song.getName().indexOf('-') >= 0){
                    songMap.put("songTitle",song.getName().split("-")[0]);
                    songMap.put("songSinger",song.getName().split("-")[1]);

                } else {

                    songMap.put("songTitle", song.getName().substring(0, (song.getName().length() - 4)));
                }

                songMap.put("songPath", song.getPath());

                songsList.add(songMap);

            }
            Log.d("search",""+songsList.size());
        }
    }
//////////// search image !

public class ImagesManager {

    public  ImagesManager() {

    }
    public ArrayList<HashMap<String, String>> getPlayList() {

        File home = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/IMG");
        File[] listFiles = home.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {

                if (file.isDirectory()) {
                    scanDirectory(file);
                } else {
                    addSongToList(file);
                }
            }
        }
        return ImageList;
    }
    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }
    private void addSongToList(File image) {
        if (image.getName().endsWith(".jpeg")||image.getName().endsWith(".jpg")||image.getName().endsWith(".png")) {
            HashMap<String, String> imageMap = new HashMap<String, String>();
            imageMap.put("imagePath",image.getPath());
            ImageList.add(imageMap);

        }

    }
}
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }



}
