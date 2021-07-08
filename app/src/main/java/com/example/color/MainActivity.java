package com.example.color;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ssw(View view) {
        startActivity(new Intent(this,SSWActivity.class));
    }

    public void qs(View view) {
        startActivity(new Intent(this,QSActivity.class));
    }
}