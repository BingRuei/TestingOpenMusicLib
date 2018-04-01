package com.app.ray.testingopenmusiclib;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnStart, btnStop, btnFinish, btnRestart, bntLoop, btnMusic;
    private MediaPlayer mp;
    private boolean loop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.voice);
        btnStart = (Button) this.findViewById(R.id.btn_start);
        btnStop = (Button) this.findViewById(R.id.btn_stop);
        btnFinish = (Button) this.findViewById(R.id.btn_finish);
        btnRestart = (Button) this.findViewById(R.id.btn_restart);
        bntLoop = (Button) this.findViewById(R.id.btn_loop);
        btnMusic = (Button) this.findViewById(R.id.btn_music);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        bntLoop.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_music:
                chooseMusic();
                break;
            case R.id.btn_start:
                mp.start();
                break;
            case R.id.btn_stop:
                mp.pause();
                break;
            case R.id.btn_finish:
                mp.stop();
                break;
            case R.id.btn_restart:
                mp.seekTo(0);
                mp.start();
                break;
            case R.id.btn_loop:
                // You can loop it with timer, now show 10 times only
                loop = !loop;
                String s = loop ? "Looping Start" : "Looping Stop";
                bntLoop.setText(s);
                int temp = 0;
                while (loop){
                    mp.start();
                    if (temp == 9){
                        loop = !loop;
                    }
                    temp++;
                }
                s = loop ? "Looping Start" : "Looping Stop";
                bntLoop.setText(s);
                break;
        }
    }

    private void chooseMusic(){
        int totalCount;
        ArrayList<String> result = new ArrayList<String>();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();
        Cursor c = resolver.query(uri, null, null, null, null);
        String dispStr = "";
        c.moveToFirst();
        totalCount = c.getCount();

        for ( int i = 0 ; i < totalCount; i ++ ){
            int index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            String src = c.getString(index);
            result.add(src);
            dispStr = dispStr +"Title:   "+src + "\n";

            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            src = c.getString(index);
            dispStr = dispStr +"Artist:  "+ src + "\n";

            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            src = c.getString(index);
            dispStr = dispStr +"Album:  "+ src + "\n";

            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            src = c.getString(index);
            int size = Integer.parseInt(src);
            size = size/1024;
            dispStr = dispStr +"Size:  "+ size + " kB\n";

            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            src = c.getString(index);
            dispStr = dispStr +"Path:  "+ src + "\n";

            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int length = c.getInt(index);
            length = length/1000; // length in sec
            int sec = length%60;
            length = length-sec;
            int min = length/60;
            if (sec < 10)
                dispStr = dispStr +"Duration:  "+ min +":0"+ sec + "\n";
            else
                dispStr = dispStr +"Duration:  "+ min +":"+ sec + "\n";

            index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE);
            src = c.getString(index);
            dispStr = dispStr +"Data Type:  "+ src + "\n\n";
            c.moveToNext();
        }
        c.close();

        Log.i("1111", dispStr);

    }
}
