package com.example.estatehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeScreen extends AppCompatActivity {
    ImageView btn_img;
    TextView txt_m1;
    TextView txt_nhap;
    TextView txt_tao;
    TextView txt_hotro;
    TextView txt_m2;
    TextView txt_m5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_screen);
        txt_m2=findViewById(R.id.txt_m2);
        txt_m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,HomepageScreen.class);
                startActivity(intent);
            }
        });
        txt_m5=findViewById(R.id.txt_m5);
        txt_m5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,HomepageScreen.class);
                startActivity(intent);
            }
        });
        txt_m1=findViewById(R.id.txt_m1);
        txt_m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,HomepageScreen.class);
                startActivity(intent);
            }
        });
        txt_nhap=findViewById(R.id.txt_m3);
        txt_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,LoginScreen.class);
                startActivity(intent);
            }
        });
        txt_tao=findViewById(R.id.txt_m4);
        txt_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,RegisterScreen.class);
                startActivity(intent);
            }
        });
        txt_hotro=findViewById(R.id.txt_hotro);
        txt_hotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,HelpScreen.class);
                startActivity(intent);
            }
        });
        btn_img=findViewById(R.id.btn_home);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeScreen.this,HomepageScreen.class);
                startActivity(intent);
            }
        });
    }
}