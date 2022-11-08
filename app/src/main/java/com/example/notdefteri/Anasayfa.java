package com.example.notdefteri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.os.Bundle;

public class Anasayfa extends AppCompatActivity implements View.OnClickListener {


    ImageButton btnmuzik, btnvideo, btngunluk, btnalarm, btnmuzcal, btnmuzdur;
    Button btnproffoto, btnbildirim;
    final int REQUEST_CODE_GALLERY = 999;
    ImageView proffoto;
    MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        proffoto = (ImageView) findViewById(R.id.proffoto);
        proffoto.setOnClickListener(this);
        btnproffoto = (Button) findViewById(R.id.btnProfFoto);
        btnproffoto.setOnClickListener(this);

        btnmuzcal = (ImageButton) findViewById(R.id.btnCal);
        btnmuzcal.setOnClickListener(this);
        btnmuzdur = (ImageButton) findViewById(R.id.btnDuraklat);
        btnmuzdur.setOnClickListener(this);

        btngunluk = (ImageButton) findViewById(R.id.btnGunluk);
        btngunluk.setOnClickListener(this);
        btnalarm = (ImageButton) findViewById(R.id.btnalarmm);
        btnalarm.setOnClickListener(this);
        btnbildirim = (Button) findViewById(R.id.btnbildirim);
        btnbildirim.setOnClickListener(this);


        btnproffoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        Anasayfa.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        ActivityResultLauncher<String> diyalogPenceresi = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        player = MediaPlayer.create(getApplicationContext(), result);
                    }
                }

        );

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btngunluk.getId()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            Toast msg = Toast.makeText(getApplicationContext(), "Gunluk sayfasına yonlendiriliyorsunuz", Toast.LENGTH_SHORT);
            msg.show();
            bildirim();
        } else if (view.getId() == btnalarm.getId()) {
            Intent i = new Intent(this, AlarmSayfasi.class);
            startActivity(i);
            Toast msg = Toast.makeText(getApplicationContext(), "Alarm sayfasına yonlendiriliyorsunuz", Toast.LENGTH_SHORT);
            msg.show();
        } else if (view.getId() == btnmuzcal.getId()) {
            player = MediaPlayer.create(this, R.raw.huzur);
            bildirim();
            Toast.makeText(this, "Ses Dosyası Seçildi", Toast.LENGTH_SHORT).show();
            if (player != null) {

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        sesiKapat();
                    }
                });
                player.start();
            }


        } else if (view.getId() == btnmuzdur.getId()) {
            if (player != null) {
                player.pause();
            }
            bildirim();
        } else if (view.getId() == btnbildirim.getId()) {
            bildirim();
        }
    }

    public void sesiKapat() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            Toast.makeText(this, "Ses kapatıldı", Toast.LENGTH_SHORT).show();
        }

    }

    private void bildirim() {
        final NotificationManager bildirimhizmeti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder bildirimolustur = new Notification.Builder(Anasayfa.this);
        bildirimolustur.setContentText("Hadi notları okuyalım :)");
        bildirimolustur.setSmallIcon(R.drawable.ic_launcher_background);
        bildirimolustur.setContentTitle("Not Defteri");
        bildirimolustur.setSound(Uri.parse(""));
        bildirimolustur.setWhen(System.currentTimeMillis());
        final Notification bildirim = bildirimolustur.build();
        final int mesajID = 1;

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                bildirimhizmeti.notify(mesajID, bildirim);
            }
        }, 50);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "dosayaya erişilemiyor", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                proffoto.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}