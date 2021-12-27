package com.example.unstoppable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    TextView secret;
    TextView textView;
    ImageView play,left,right;
    SeekBar seekBar;
    int pos;
    String songName;
    ArrayList<File> songList;
    MediaPlayer mediaPlayer;
    Thread updateSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        secret=findViewById(R.id.secret);
        textView=findViewById(R.id.textView2);
        play=findViewById(R.id.imageView15);
        left=findViewById(R.id.imageView14);
        right=findViewById(R.id.imageView16);
        seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        pos=intent.getIntExtra("position",0);
        songName=intent.getStringExtra("currentSong");
        textView.setText(songName);
        textView.setSelected(true);
        songList=(ArrayList)intent.getParcelableArrayListExtra("songList");
        Uri uri=Uri.parse(songList.get(pos).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secret.setText("This App is developed by Shivam Mehta");
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek=new Thread(){
            @Override
            public void run() {
                int currPos=0;
                try{
                    while(currPos<mediaPlayer.getDuration())
                    {
                        currPos=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currPos);
                        sleep(500);
                    }
                }
                catch(Exception e)
                {
                   e.printStackTrace();
                }
            }
        };
        updateSeek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.gggggg);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setImageResource(R.drawable.pauseeee);
                    mediaPlayer.start();
                }
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(pos==0)
                {
                    pos=songList.size()-1;
                }
                else
                {
                    pos=pos-1;
                }
                seekBar.setProgress(0);
                textView.setText(songList.get(pos).getName().replace(".mp3",""));
                textView.setSelected(true);
                play.setImageResource(R.drawable.pauseeee);
                Uri uri=Uri.parse(songList.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(pos==songList.size()-1)
                {
                    pos=0;
                }
                else
                {
                    pos=pos+1;
                }
                seekBar.setProgress(0);
                textView.setText(songList.get(pos).getName().replace(".mp3",""));
                textView.setSelected(true);
                play.setImageResource(R.drawable.pauseeee);
                Uri uri=Uri.parse(songList.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });

    }
}