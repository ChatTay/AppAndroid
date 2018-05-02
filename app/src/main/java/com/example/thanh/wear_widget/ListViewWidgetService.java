package com.example.thanh.wear_widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by thanh on 3/1/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {

    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    private static String mp3Pattern = ".mp3";
    int max;
    private Context mContext;
    private ArrayList<String> records;
    private ArrayList<String> recordspath;
    public ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }
    public void onCreate() {

        records=new ArrayList<String>();
        recordspath=new ArrayList<String>();
        songsList.clear();

        SongsManager sm=new SongsManager();
        records.clear();
        recordspath.clear();
        max=sm.getPlayList().size();

        for(int i=0;i<max;i++)
        {
            records.add(""+sm.getPlayList().get(i).get("songTitle"));
            recordspath.add(""+sm.getPlayList().get(i).get("songPath"));

        }


    }


    @Override
    public void onDataSetChanged() {


    }

    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(),R.layout.widget_item);
        String data=records.get(position);

        rv.setTextViewText(R.id.item, data);
        rv.setImageViewResource(R.id.imageView,R.drawable.icon);

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(MyWidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();

        bundle.putString(MyWidgetProvider.EXTRA_STRING,(String)recordspath.get(position));
        bundle.putString(MyWidgetProvider.EXTRA_ITEM,""+position);
        fillInIntent.putExtras(bundle);
        rv.setOnClickFillInIntent(R.id.item, fillInIntent);

        return rv;
    }
    public int getCount(){
        Log.e("size=",records.size()+"songTitle");
        return records.size();
    }

    public int getViewTypeCount(){
        return 1;
    }
    public long getItemId(int position) {

        return position;

    }
    public void onDestroy(){
        records.clear();
    }
    public boolean hasStableIds() {

        return true;

    }
    public RemoteViews getLoadingView() {

        return null;

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
                   // records.add(song.getName().split("-")[0]);
                    songMap.put("songSinger",song.getName().split("-")[1]);
                } else {
                    songMap.put("songTitle", song.getName().substring(0, (song.getName().length() - 4)));
                    //recordspath.add(song.getName().substring(0, (song.getName().length() - 4)));
                }

                songMap.put("songPath", song.getPath());

                songsList.add(songMap);

            }
            Log.e("thanh","zize");
        }
    }



}
