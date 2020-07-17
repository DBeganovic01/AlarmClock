package com.example.alarmclock;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    ToggleButton onOff;
    Button snooze5;
    Button snooze10;
    Button snooze15;
    Button snooze20;
    Button snooze25;
    Button snooze30;
    Button snooze35;
    Button snooze40;
    Button snooze45;
    Button test;
    TextView text;

    Uri alarmTone;
    Ringtone ringtoneAlarm;

    final long minutes = 60 * 1000;//Used for snoozing, default delay = 1 minute
    final Handler handler = new Handler();
    CountDownTimer countDownTimer;

    /*
    Snoozes the alarm. If a previous alarm or snooze was scheduled, it is replaced.
    Counts the time remaining in the snooze until the alarm goes off again.
     */
    public void snooze(int n){
        ringtoneAlarm.stop();
        handler.removeCallbacksAndMessages(null);
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        Log.d(null, "onTick: " + (minutes*n));
        countDownTimer = new CountDownTimer((minutes * n), 1000) {
                @Override
                public void onTick(long l) {
                    text.setText(String.format("Snoozing for %d min, %02d sec",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                }
                @Override
                public void onFinish() {
                    text.setText("WAKE UP!!!");
                }
            };
        countDownTimer.start();
        Toast toast = Toast.makeText(getApplicationContext(), "Snoozing for " + n + " minutes", Toast.LENGTH_SHORT);
        toast.show();
        onOff.setChecked(true);
        onOff.setText("SNOOZING");
        onOff.setBackgroundColor(Color.GREEN);
        handler.postDelayed(new Runnable() {
            public void run() {
                if (onOff.isChecked()){
                    ringtoneAlarm.play();
                }
            }
        }, minutes * n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.simpleTimePicker);
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);

        onOff = (ToggleButton) findViewById((R.id.onOff));
        onOff.setBackgroundColor(Color.RED);

        text = (TextView) findViewById(R.id.text);

        snooze5 = (Button) findViewById(R.id.snooze5);
        snooze10 = (Button) findViewById(R.id.snooze10);
        snooze15 = (Button) findViewById(R.id.snooze15);
        snooze20 = (Button) findViewById(R.id.snooze20);
        snooze25 = (Button) findViewById(R.id.snooze25);
        snooze30 = (Button) findViewById(R.id.snooze30);
        snooze35 = (Button) findViewById(R.id.snooze35);
        snooze40 = (Button) findViewById(R.id.snooze40);
        snooze45 = (Button) findViewById(R.id.snooze45);
        test = (Button) findViewById(R.id.test);//For testing purposes. Plays the default alarm sound

        /*
        Turns the alarm on/off. The time to wait before the alarm rings is derived from
        a Calendar object using the selected time in the timePicker in the UI.
         */
        //TODO: Refactor toast/textView code into its own method
        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onOff.isChecked()){
                    if (countDownTimer != null){
                        countDownTimer.cancel();
                    }
                    ringtoneAlarm.stop();
                    onOff.setBackgroundColor(Color.RED);
                    handler.removeCallbacksAndMessages(null);
                    Toast toast = Toast.makeText(getApplicationContext(), "Alarm OFF. Snooze ENDED", Toast.LENGTH_SHORT);
                    toast.show();
                    text.setText("No alarm");
                }
                else{
                    onOff.setText("ALARM SET");
                    onOff.setBackgroundColor(Color.GREEN);
                    if (countDownTimer != null){
                        countDownTimer.cancel();
                    }
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    c.set(Calendar.MINUTE, timePicker.getMinute());
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    //How long to wait until alarm sounds
                    long delay = (Long)(c.getTimeInMillis() - System.currentTimeMillis());
                    //If alarm is set for the next day (ie the time in the timePicker is earlier than currrent time)
                    if (delay <= 0){
                        delay = 1000 * 60 * 60 * 24 + c.getTimeInMillis() - System.currentTimeMillis();
                    }
                    Log.d(null, "Delay = " + delay);
                    if (timePicker.getHour() == 0){
                        if (timePicker.getMinute() < 10){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + (timePicker.getHour() + 12) + ":0" + timePicker.getMinute() + " AM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + (timePicker.getHour() + 12) + ":0" + timePicker.getMinute() + " AM");
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + (timePicker.getHour() + 12) + ":" + timePicker.getMinute() + " AM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + (timePicker.getHour() + 12) +":" + timePicker.getMinute() + " AM");
                        }
                    }
                    else if (timePicker.getHour() < 12){
                        if (timePicker.getMinute() < 10){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + timePicker.getHour() + ":0" + timePicker.getMinute() + " AM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + timePicker.getHour() + ":0" + timePicker.getMinute() + " AM");
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + timePicker.getHour()+ ":" + timePicker.getMinute() + " AM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + timePicker.getHour() + ":" + timePicker.getMinute() + " AM");
                        }
                    }
                    else if (timePicker.getHour() == 12){
                        if (timePicker.getMinute() < 10){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + timePicker.getHour() + ":0" + timePicker.getMinute() + " PM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + timePicker.getHour() + ":0" + timePicker.getMinute() + " PM");
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + timePicker.getHour()+ ":" + timePicker.getMinute() + " PM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + timePicker.getHour() + ":" + timePicker.getMinute() + " PM");
                        }
                    }
                    else{
                        if (timePicker.getMinute() < 10){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + (timePicker.getHour()-12) + ":0" + timePicker.getMinute() + " PM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + (timePicker.getHour()-12) + ":0" + timePicker.getMinute() + " PM");
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Alarm set for " + (timePicker.getHour()-12)+ ":" + timePicker.getMinute() + " PM", Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("Alarm set for " + (timePicker.getHour()-12) + ":" + timePicker.getMinute() + " PM");
                        }
                    }
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (onOff.isChecked()){
                                ringtoneAlarm.play();
                                text.setText("WAKE UP!!!");
                            }
                        }
                    }, delay);
                }
            }
        });

        snooze5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((5));
            }
        });

        snooze10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((10));
            }
        });

        snooze15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((15));
            }
        });

        snooze20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((20));
            }
        });

        snooze25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((25));
            }
        });

        snooze30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((30));
            }
        });

        snooze35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((35));
            }
        });

        snooze40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((40));
            }
        });

        snooze45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze((45));
            }
        });

        test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                snooze(1);
            }
        });
    }
}