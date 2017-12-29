package com.example.ereminilya.customfancontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DialView myColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSwitchColors(View view) {

        myColor = (DialView)findViewById(R.id.dialView);
        if (myColor.isSetToAltColor()) {
            myColor.setAltColor(false);
        } else {
            myColor.setAltColor(true);
        }
    }
}
