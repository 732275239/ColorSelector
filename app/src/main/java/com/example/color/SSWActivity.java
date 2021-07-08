package com.example.color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.color.view.Color1;

public class SSWActivity extends AppCompatActivity {

    private TextView tv;
    private Color1 color;
    private SeekBar sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_s_w);
        tv = findViewById(R.id.tv);
        color = findViewById(R.id.color);
        sw = (SeekBar) findViewById(R.id.sw);
        color.setColorSeleListener(new Color1.onColorSeleListener() {
            @Override
            public void onColorSele(int alpha, int red, int green, int blue, int progress) {
                tv.setTextColor(Color.argb(alpha, red, green, blue));
                sw.setProgress(progress);
            }
        });
        sw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    int p = seekBar.getProgress();
                    color.setColorProgress(p);
                }
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