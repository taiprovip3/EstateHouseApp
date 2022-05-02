package com.example.estatehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HelpScreen extends AppCompatActivity {

    ImageView goHomePageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        goHomePageView = findViewById(R.id.help_goHomePage);
        goHomePageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpScreen.this, HomepageScreen.class);
                startActivity(intent);
            }
        });
    }
}