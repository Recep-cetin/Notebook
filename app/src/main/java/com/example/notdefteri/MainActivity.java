package com.example.notdefteri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.PKIXRevocationChecker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edtName, edtPrice;
    Button btnChoose, btnAdd, btnList,btnanasayfa;
    ImageView imageView;

    TextView txtad;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper sqLiteHelper;

    public static String girisad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnanasayfa=(Button) findViewById(R.id.btnanasayfa);
        btnanasayfa.setOnClickListener(this);
        txtad=(TextView) findViewById(R.id.txtad);

        init();

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, image BLOB)");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqLiteHelper.insertData(
                            edtName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                    Toast.makeText(getApplicationContext(), "ekleme başarılı!", Toast.LENGTH_SHORT).show();
                    edtName.setText("");
                    edtPrice.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(girisad!="") {
                    Intent intent = new Intent(MainActivity.this, GunlukListesi.class);
                    startActivity(intent);
                }else{
                    sorgu();
                    Toast.makeText(MainActivity.this, "Notalıma gidebilmeniz için giriş yapmanız gerek", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (girisad!=""){
            txtad.setText("Hoşgeldin :"+girisad.toString());

        }else{
            txtad.setText("Herhangi bir giriş yapılmamış");
        }



    }




    @Override
    public void onClick(View view) {
        if(view.getId()==btnanasayfa.getId()){
            Intent intent = new Intent(MainActivity.this, Anasayfa.class);
            startActivity(intent);


        }
    }

    private void bildirim() {
        final NotificationManager bildirimhizmeti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder bildirimolustur = new Notification.Builder(MainActivity.this);
        bildirimolustur.setContentText("Hadi notları okuyalı :)");
        bildirimolustur.setSmallIcon(R.drawable.ic_launcher_background);
        bildirimolustur.setContentTitle("Alarm Çalma Vakti");
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

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "dosayalara erişebilmem için izn ver!", Toast.LENGTH_SHORT).show();
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
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        edtName = (EditText) findViewById(R.id.edtName);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnList = (Button) findViewById(R.id.btnList);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void sorgu(){

        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("gunluk otomasyonu");
        alert.setMessage("Notları gorebilmeniz için giriş gerekli ?");
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_launcher_background);
        alert.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getApplicationContext(), "Giris sayfasına Yonlendiriliyorsunuz", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, GirisSayfasi.class);
                        startActivity(intent);
                    }
                }
        )
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "Hayıra basıldı", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .show();

    }


}