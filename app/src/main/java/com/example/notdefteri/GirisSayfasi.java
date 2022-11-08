package com.example.notdefteri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class GirisSayfasi extends AppCompatActivity implements View.OnClickListener {

    EditText txtEmail,txtparola;
    Button btngiris;

    MainActivity yeni=new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_sayfasi);

        btngiris=(Button) findViewById(R.id.btngiris);
        btngiris.setOnClickListener(this);

        txtEmail=(EditText) findViewById(R.id.txtemail);
        txtparola=(EditText) findViewById(R.id.txtparola);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btngiris.getId()) {
            if ((txtEmail.getText().toString().equals("recep@gmail.com")) && (txtparola.getText().toString().equals("123456"))) {
                Toast msg = Toast.makeText(getApplicationContext(), "bigiler dogru", Toast.LENGTH_LONG);
                msg.show();
                MainActivity.girisad="recep@gmail.com";
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("isim", txtEmail.getText().toString());
                startActivity(i);
            } else {
                Toast msg = Toast.makeText(getApplicationContext(), "bigiler yanlış", Toast.LENGTH_LONG);
                msg.show();
            }
        }
    }
}