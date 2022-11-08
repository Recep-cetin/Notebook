package com.example.notdefteri;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Not alma vakti geldi", Toast.LENGTH_SHORT).show();

        Uri melodi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (melodi == null) {
            melodi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, melodi);
        ringtone.play();
    }
}
