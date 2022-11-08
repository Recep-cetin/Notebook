package com.example.notdefteri;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmSayfasi extends AppCompatActivity implements View.OnClickListener {

    TextView txtsaat;
    Handler handle = null;
    Runnable runnable = null;
    String zaman;
    TextClock txtalermsaati;
    Button btnalarm;
    private TimePickerDialog timepickerdialog;
    final static int islemkodu = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sayfasi);

        btnalarm = (Button) findViewById(R.id.btnalarm);
        btnalarm.setOnClickListener(this);

        txtsaat = (TextView) findViewById(R.id.txtsaat);

        final SimpleDateFormat bicim = new SimpleDateFormat("dd:MM:yyyy  HH:mm:ss");
        zaman = bicim.format(new Date());
        txtsaat.setText(zaman);

        handle = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                zaman = bicim.format(new Date());
                txtsaat.setText(zaman);

                handle.postDelayed(runnable, 1000);
            }
        };

        runnable.run();

    }

    @Override
    public void onClick(View view) {

        Alarmpenceresi(true);
    }

    private void Alarmpenceresi(Boolean vakit) {

        Calendar calendar = Calendar.getInstance();
        timepickerdialog = new TimePickerDialog(
                AlarmSayfasi.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                vakit
        );
        timepickerdialog.setTitle("alarm kur");
        timepickerdialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int saat, int dakika) {
            Calendar calnow = Calendar.getInstance();
            Calendar calset = (Calendar) calnow.clone();

            calset.set(Calendar.HOUR_OF_DAY, saat);
            calset.set(Calendar.MINUTE, dakika);
            calset.set(Calendar.SECOND, 0);
            calset.set(Calendar.MILLISECOND, 0);


            if (calset.compareTo(calnow) < 0) {
                calset.add(Calendar.DATE, 1);
            }
            setalarm(calset);
        }

    };





    private void setalarm(Calendar alarmcalender) {
        Toast.makeText(this, "alarm ayarlandı", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), islemkodu, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmcalender.getTimeInMillis(), pendingIntent);
    }



}