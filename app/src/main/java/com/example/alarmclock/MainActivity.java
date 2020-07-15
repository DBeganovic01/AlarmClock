package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    ToggleButton onOff;
    Button snooze1;
    Button snooze10;
    Button play;

    Uri alarmTone;
    Ringtone ringtoneAlarm;

    long delay = 60 * 1000;//Used for snoozing, default delay = 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.simpleTimePicker);
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);

        onOff = (ToggleButton) findViewById((R.id.onOff));
        onOff.setBackgroundColor(Color.RED);

        snooze1 = (Button) findViewById(R.id.snooze1);
        snooze10 = (Button) findViewById(R.id.snooze10);
        play = (Button) findViewById(R.id.playTone);//For testing purposes. Plays the default alarm sound

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                ringtoneAlarm.play();
            }
        });

        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onOff.isChecked()){
                    ringtoneAlarm.stop();
                    onOff.setBackgroundColor(Color.RED);
                    Toast toast = Toast.makeText(getApplicationContext(), "Alarm OFF. Snooze ENDED", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    onOff.setBackgroundColor(Color.GREEN);
                }
            }
        });

        snooze10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtoneAlarm.stop();
                Toast toast = Toast.makeText(getApplicationContext(), "Snoozing for 10", Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (onOff.isChecked()){
                            ringtoneAlarm.play();
                        }
                    }
                }, 10000);
            }
        });

    }

}