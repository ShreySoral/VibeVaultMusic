package com.example.vibevault;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Buttons
    Button forward_btn,back_btn,play_btn,stop_btn;
    TextView tvTextView,time_text,title_text;
    SeekBar seekBar;

    // Media Player
    MediaPlayer mediaPlayer;

    //Handlers
    Handler handler=new Handler();

    //Variables
    double startTime=0;
    double finalTime=0;
    int forwardTime=10000;
    int backwardTime=10000;
    static int oneTimeOnly=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_btn=findViewById(R.id.play_btn);
        stop_btn=findViewById(R.id.pause_btn); // Corrected the name
        forward_btn=findViewById(R.id.forward_btn);
        back_btn=findViewById(R.id.back_btn);

        title_text=findViewById(R.id.song_title);
        time_text=findViewById(R.id.time_left_text);

        seekBar=findViewById(R.id.seekBar);

        //media player
        mediaPlayer=MediaPlayer.create(this,R.raw.astronaut);
        title_text.setText(getResources().getIdentifier(
                "astronaut",
                "raw",
                getPackageName()
        ));
        seekBar.setClickable(false);

        //Adding Functionalities for the buttons
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;
                if((temp+forwardTime)<= finalTime){
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this,"Can't Jump Forward!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;

                if((temp - backwardTime)>0){
                    startTime=startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this,"Can't Go Back!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.start();
        finalTime=mediaPlayer.getDuration();
        startTime=mediaPlayer.getCurrentPosition();
        if(oneTimeOnly==0){
            seekBar.setMax((int) finalTime);
            oneTimeOnly=1;
        }

        time_text.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime,100);
    }
    //Creating the Runnable
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time_text.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

            seekBar.setProgress((int) startTime);

            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(this, 100);
            }
        }
    };
}