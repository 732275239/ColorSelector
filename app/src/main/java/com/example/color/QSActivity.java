package com.example.color;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.color.view.Color1;
import com.example.color.view.Color2;

public class QSActivity extends AppCompatActivity {
    private Color2 color;
    private Button bt;
    private TextView tv;
    private SeekBar red;
    private SeekBar green;
    private SeekBar bule;
    private int r;
    private int g;
    private int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_s);
        color = (Color2) findViewById(R.id.color);
        tv = findViewById(R.id.tv);
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        bule = (SeekBar) findViewById(R.id.bule);

        color.setColorSeleListener(new Color2.onColorSeleListener() {
            @Override
            public void onColorSele(int alpha, int newred, int newgreen, int newblue) {
                r = newred;
                g = newgreen;
                b = newblue;
                red.setProgress(newred);
                green.setProgress(newgreen);
                bule.setProgress(newblue);
                tv.setTextColor(Color.argb(alpha, newred, newgreen, newblue));
            }
        });


        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b1) {
                r = seekBar.getProgress();
                boolean ok = color.findBitmapColor(Color.rgb(r, g, b));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b1) {

                g = seekBar.getProgress();
                boolean ok = color.findBitmapColor(Color.rgb(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        bule.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b1) {

                b = seekBar.getProgress();
                boolean ok = color.findBitmapColor(Color.rgb(r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}