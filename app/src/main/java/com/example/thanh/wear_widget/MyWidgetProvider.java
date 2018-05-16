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
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
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
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
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

    int arr[]={R.drawable.image0000001,R.drawable.image0000002,R.drawable.image0000003,R.drawable.image0000004
            ,R.drawable.image0000005,R.drawable.image0000006,R.drawable.image0000007,R.drawable.image0000008,R.drawable.image0000009,R.drawable.image0000010
            ,R.drawable.image0000011,R.drawable.image0000012,R.drawable.image0000013,R.drawable.image0000014,R.drawable.image0000015,R.drawable.image0000016,R.drawable.image0000017
            ,R.drawable.image0000018,R.drawable.image0000019,R.drawable.image0000020,R.drawable.image0000021,R.drawable.image0000022
            ,R.drawable.image0000023,R.drawable.image0000024
    };

    int arr1[]={R.drawable.frame00delay01s,R.drawable.frame01delay01s,R.drawable.frame02delay01s,R.drawable.frame03delay01s,R.drawable.frame04delay01s,R.drawable.frame05delay01s,R.drawable.frame06delay01s
            ,R.drawable.frame07delay01s,R.drawable.frame08delay01s,R.drawable.frame09delay01s,R.drawable.frame10delay01s,R.drawable.frame11delay01s,R.drawable.frame12delay01s,R.drawable.frame13delay01s,R.drawable.frame14delay01s
            ,R.drawable.frame15delay01s
    };

    int arr2[]={R.drawable.frame2delay007s1,R.drawable.frame2delay007s2,R.drawable.frame2delay007s3,R.drawable.frame2delay007s4
            ,R.drawable.frame2delay007s5,R.drawable.frame2delay007s6,R.drawable.frame2delay007s7,R.drawable.frame2delay007s8
    };
    int arr3[]={R.drawable.framedelay1,R.drawable.framedelay2,R.drawable.framedelay3,R.drawable.framedelay4,R.drawable.framedelay5,R.drawable.framedelay6,R.drawable.framedelay7
            ,R.drawable.framedelay8,R.drawable.framedelay9
    };

    int arr4[]={R.drawable.thanh1,R.drawable.thanh2,R.drawable.thanh3,R.drawable.thanh4,R.drawable.thanh5,R.drawable.thanh6,R.drawable.thanh7,R.drawable.thanh8,R.drawable.thanh9,R.drawable.thanh10
            ,R.drawable.thanh11,R.drawable.thanh12,R.drawable.thanh13,R.drawable.thanh14,R.drawable.thanh15,R.drawable.thanh16,R.drawable.thanh17,R.drawable.thanh18,R.drawable.thanh19,R.drawable.thanh20
            ,R.drawable.thanh21,R.drawable.thanh22,R.drawable.thanh23,R.drawable.thanh24,R.drawable.thanh25,R.drawable.thanh26,R.drawable.thanh27,R.drawable.thanh28,R.drawable.thanh29,R.drawable.thanh30
            ,R.drawable.thanh31};


    AudioManager audioManager;

    private static MediaPlayer mp;

    static int x=0;


    private static final long DOUBLE_CLICK_WINDOW = 400;
    private static volatile long firstClickTimeReference;

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

                    String timeda = String.format("%02d:%02ds",
                            TimeUnit.MILLISECONDS.toMinutes(max),
                            TimeUnit.MILLISECONDS.toSeconds(max) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                    );

                    String timeru = String.format("%02d:%02ds",
                            TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                    );

                    RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                    ClockViews.setTextViewText(R.id.run,timeru);
                    ClockViews.setTextViewText(R.id.dura,timeda);
                    ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    manager.updateAppWidget(myComponentName, ClockViews);


                    Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);

                }
                public void onFinish() {
                    ProgressPlayUpdateAppWidget(context,max,0);
                }
            };
            ctime.start();
            ctime.cancel();
            search="thanh";


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
//789
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

                    String timeda = String.format("%02d:%02ds",
                            TimeUnit.MILLISECONDS.toMinutes(max),
                            TimeUnit.MILLISECONDS.toSeconds(max) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                    );

                    String timeru = String.format("%02d:%02ds",
                            TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                    );

                    RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                    ClockViews.setTextViewText(R.id.run,timeru);
                    ClockViews.setTextViewText(R.id.dura,timeda);
                    ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    manager.updateAppWidget(myComponentName, ClockViews);


                    Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);
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
            mp.reset();

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

                   // Toast.makeText(context,"time :"+millisUntilFinished,Toast.LENGTH_SHORT).show();

                    String timeda = String.format("%02d:%02ds",
                            TimeUnit.MILLISECONDS.toMinutes(max),
                            TimeUnit.MILLISECONDS.toSeconds(max) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                    );

                    String timeru = String.format("%02d:%02ds",
                            TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                    );

                    RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                    ClockViews.setTextViewText(R.id.run,timeru);
                    ClockViews.setTextViewText(R.id.dura,timeda);
                    ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    manager.updateAppWidget(myComponentName, ClockViews);


                    Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);
                }

                public void onFinish() {
                    ProgressPlayUpdateAppWidget(context, max, 0);
                }
            };
            ctime.start();

            //updateWidget(context);


            //Toast.makeText(context,"bat",Toast.LENGTH_SHORT).show();
        }
        if(playSong1.equals(intent.getAction()))
        {
           // Toast.makeText(context,"tat",Toast.LENGTH_SHORT).show();

            SoundOffUpdateAppWidget(context);
            ImageplayUpdateAppWidget(context);

            position = mp.getCurrentPosition();
            run=position;

            mp.stop();
            //mp.reset();

            ctime.cancel();
            ItemUpdateAppWidget(context);

           // updateWidget(context);

            //Toast.makeText(context,"tat",Toast.LENGTH_SHORT).show();


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

                        String timeda = String.format("%02d:%02ds",
                                TimeUnit.MILLISECONDS.toMinutes(max),
                                TimeUnit.MILLISECONDS.toSeconds(max) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                        );

                        String timeru = String.format("%02d:%02ds",
                                TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                        );

                        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                        ClockViews.setTextViewText(R.id.run,timeru);
                        ClockViews.setTextViewText(R.id.dura,timeda);
                        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                        AppWidgetManager manager = AppWidgetManager.getInstance(context);
                        manager.updateAppWidget(myComponentName, ClockViews);


                        Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);
                    }
                    public void onFinish() {
                        ProgressPlayUpdateAppWidget(context,max,0);
                    }
                };
                ctime.start();
            }


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


                        String timeda = String.format("%02d:%02ds",
                                TimeUnit.MILLISECONDS.toMinutes(max),
                                TimeUnit.MILLISECONDS.toSeconds(max) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                        );

                        String timeru = String.format("%02d:%02ds",
                                TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                        );

                        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                        ClockViews.setTextViewText(R.id.run,timeru);
                        ClockViews.setTextViewText(R.id.dura,timeda);
                        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                        AppWidgetManager manager = AppWidgetManager.getInstance(context);
                        manager.updateAppWidget(myComponentName, ClockViews);


                        Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);
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

                                    String timeda = String.format("%02d:%02ds",
                                            TimeUnit.MILLISECONDS.toMinutes(max),
                                            TimeUnit.MILLISECONDS.toSeconds(max) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                                    );

                                    String timeru = String.format("%02d:%02ds",
                                            TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                                    );

                                    RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                                    ClockViews.setTextViewText(R.id.run,timeru);
                                    ClockViews.setTextViewText(R.id.dura,timeda);
                                    ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                                    manager.updateAppWidget(myComponentName, ClockViews);


                                    Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);
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

                                String timeda = String.format("%02d:%02ds",
                                        TimeUnit.MILLISECONDS.toMinutes(max),
                                        TimeUnit.MILLISECONDS.toSeconds(max) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max))
                                );

                                String timeru = String.format("%02d:%02ds",
                                        TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toSeconds(max-millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max-millisUntilFinished))
                                );

                                RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                                ClockViews.setTextViewText(R.id.run,timeru);
                                ClockViews.setTextViewText(R.id.dura,timeda);
                                ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                                manager.updateAppWidget(myComponentName, ClockViews);


                                Log.e("log","max :"+max+" run :"+(max-millisUntilFinished)+" time :"+timeda +"  time run :"+timeru);
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

                RemoteViews ImageView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                ImageView.removeAllViews(R.id.slidingimage);


            for(int i=0;i<ImageList.size();i++)
            {

                RemoteViews Image = new RemoteViews(context.getPackageName(), R.layout.image);
                Bitmap bmImg1 = BitmapFactory.decodeFile(ImageList.get(i).get("imagePath")).copy(Bitmap.Config.ARGB_8888, true);

                ImageView.setViewVisibility(R.id.slidingimage,View.VISIBLE);

                Image.setImageViewBitmap(R.id.imageitem,getCircularBitmap(darkenBitMap(bmImg1)));

                ImageView.addView(R.id.slidingimage,Image);

            }
                ImageView.setViewVisibility(R.id.bg,View.VISIBLE);
                ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(myComponentName, ImageView);


                RemoteViews ImageView1 = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                ImageView1.removeAllViews(R.id.soundwaves);
                ImageView1.removeAllViews(R.id.soundwaves1);

                for(int i=1;i<=20;i++)
                {

                    RemoteViews Image1 = new RemoteViews(context.getPackageName(), R.layout.image_sound_waves);
                    Image1.setImageViewResource(R.id.imageitem,arr[i]);
                    ImageView1.addView(R.id.soundwaves,Image1);
                    Image1.setImageViewResource(R.id.imageitem,arr[i]);
                    ImageView1.addView(R.id.soundwaves1,Image1);

                }

                ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
                AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
                manager1.updateAppWidget(myComponentName1, ImageView1);


            face="Face";

            //Toast.makeText(context,""+ImageList.size(),Toast.LENGTH_SHORT).show()
             //  Toast.makeText(context,""+face +"size :"+ImageList.size(),Toast.LENGTH_SHORT).show();
            }
            else if(face.equalsIgnoreCase("Face"))
            {
                RemoteViews ImageView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                ImageView.setViewVisibility(R.id.slidingimage,View.GONE);
                ImageView.setViewVisibility(R.id.bg,View.GONE);
                ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(myComponentName, ImageView);
                face="noFace";


               // Toast.makeText(context,""+face,Toast.LENGTH_SHORT).show();
            }

            //Toast.makeText(context,"cham menu !",Toast.LENGTH_SHORT).show();
        }
//log123
        Log.e("log","max :"+max+" run :"+run);

       // Toast.makeText(context,"cham nhe !",Toast.LENGTH_SHORT).show();

        context.stopService(new Intent(context,MyBatteryReceiver.class));
        context.startService(new Intent(context,MyBatteryReceiver.class));



    }

    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    public void SoundOnUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.soundwaves,View.VISIBLE);
        ClockViews.setViewVisibility(R.id.soundwaves1,View.VISIBLE);

        ClockViews.setViewVisibility(R.id.imagesound,View.GONE);
        ClockViews.setViewVisibility(R.id.imagesound1,View.GONE);


        ComponentName myComponentName = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, ClockViews);
    }
    public void SoundOffUpdateAppWidget(final Context context){
        RemoteViews ClockViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        ClockViews.setViewVisibility(R.id.soundwaves,View.GONE);
        ClockViews.setViewVisibility(R.id.soundwaves1,View.GONE);

        ClockViews.setViewVisibility(R.id.imagesound,View.VISIBLE);
        ClockViews.setViewVisibility(R.id.imagesound1,View.VISIBLE);

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

        context.stopService(new Intent(context,MyBatteryReceiver.class));
        context.startService(new Intent(context,MyBatteryReceiver.class));

        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            remoteViews.setOnClickPendingIntent(R.id.next, getPendingSelfIntent(context, next));
            remoteViews.setOnClickPendingIntent(R.id.pre, getPendingSelfIntent(context, pre));

            remoteViews.setOnClickPendingIntent(R.id.btmusic,getPendingSelfIntent(context,music));
            remoteViews.setOnClickPendingIntent(R.id.btmenu,getPendingSelfIntent(context,menu));
            remoteViews.setOnClickPendingIntent(R.id.backhome,getPendingSelfIntent(context,backhome));
            remoteViews.setOnClickPendingIntent(R.id.btlistview,getPendingSelfIntent(context,listview));
            remoteViews.setOnClickPendingIntent(R.id.btbacklist,getPendingSelfIntent(context,backlist));

            remoteViews.setOnClickPendingIntent(R.id.nexx,getPendingSelfIntent(context,nextSong));
            remoteViews.setOnClickPendingIntent(R.id.pause,getPendingSelfIntent(context,playSong));
            remoteViews.setOnClickPendingIntent(R.id.pause1,getPendingSelfIntent(context,playSong1));
            remoteViews.setOnClickPendingIntent(R.id.pree,getPendingSelfIntent(context,preSong));

            remoteViews.setOnClickPendingIntent(R.id.ramdom,getPendingSelfIntent(context,random1));
            remoteViews.setOnClickPendingIntent(R.id.ramdom1,getPendingSelfIntent(context,random2));

            //remoteViews.setSelected(R.id.mywidget)

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
        }


        for (int widgetId : appWidgetIds) {


            long currentSystemTime = System.currentTimeMillis();
//789
            if (currentSystemTime - firstClickTimeReference <= DOUBLE_CLICK_WINDOW) {
                //double click happened in less than 400 miliseconds
                //so let's start our activity
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);

                //Toast.makeText(context,"double click !",Toast.LENGTH_SHORT).show();
                Log.e("chamnhe","x :"+x);

                RemoteViews ImageView1 = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                ImageView1.removeAllViews(R.id.slidingbackground);

                x++;

                if(x==6)
                {
                    x=0;
                }

                if(x==0)
                {

                    ImageView1.setViewVisibility(R.id.bgch,View.GONE);
                    ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
                    AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
                    manager1.updateAppWidget(myComponentName1, ImageView1);

                }
                if(x==1)
                {

                    ImageView1.setViewVisibility(R.id.bgch,View.VISIBLE);
                    for(int i=0;i<=8;i++)
                    {

                        RemoteViews Image1 = new RemoteViews(context.getPackageName(), R.layout.image3);

                        Image1.setImageViewResource(R.id.imageitem,arr3[i]);
                        ImageView1.addView(R.id.slidingbackground,Image1);

                        ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
                        AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
                        manager1.updateAppWidget(myComponentName1, ImageView1);
                    }

                }
                if(x==3)
                {
                    ImageView1.setViewVisibility(R.id.bgch,View.VISIBLE);
                    for(int i=0;i<=7;i++)
                    {

                        RemoteViews Image1 = new RemoteViews(context.getPackageName(), R.layout.image);

                        Image1.setImageViewResource(R.id.imageitem,arr2[i]);
                        ImageView1.addView(R.id.slidingbackground,Image1);

                        ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
                        AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
                        manager1.updateAppWidget(myComponentName1, ImageView1);
                    }
                }
                if(x==4)
                {
                    ImageView1.setViewVisibility(R.id.bgch,View.VISIBLE);
                    for(int i=0;i<=15;i++)
                    {
                        RemoteViews Image1 = new RemoteViews(context.getPackageName(), R.layout.image2);

                        Image1.setImageViewResource(R.id.imageitem,arr1[i]);
                        ImageView1.addView(R.id.slidingbackground,Image1);


                        ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
                        AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
                        manager1.updateAppWidget(myComponentName1, ImageView1);
                    }

                }
                if(x==5)
                {
                    ImageView1.setViewVisibility(R.id.bgch,View.VISIBLE);
                    for(int i=0;i<=30;i++)
                    {

                        RemoteViews Image1 = new RemoteViews(context.getPackageName(), R.layout.image3);

                        Image1.setImageViewResource(R.id.imageitem,arr4[i]);
                        ImageView1.addView(R.id.slidingbackground,Image1);

                        ComponentName myComponentName1 = new ComponentName(context, MyWidgetProvider.class);
                        AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
                        manager1.updateAppWidget(myComponentName1, ImageView1);
                    }
                }









            } else {
                firstClickTimeReference = currentSystemTime;


                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);

                Intent intent = new Intent(context, MyWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);


                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }

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

        //updateViews.setTextViewText(R.id.LC, "waiting!");

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
