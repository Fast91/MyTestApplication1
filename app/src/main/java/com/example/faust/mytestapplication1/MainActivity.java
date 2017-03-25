package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Button b1 = (Button) findViewById(R.id.b1);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Amatic-Bold.ttf");
        b1.setTypeface(font); */
        final Button b1 = (Button) findViewById(R.id.b1);
        final Button b2 = (Button) findViewById(R.id.b2);
        final Button b3 = (Button) findViewById(R.id.b3);

        b1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b1.setPressed(true);
                b2.setPressed(false);
                b3.setPressed(false);
                return true;
            }
        });


        b2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b1.setPressed(false);
                b3.setPressed(false);
                b2.setPressed(true);
                return true;
            }
        });

        b3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b3.setPressed(true);
                b1.setPressed(false);
                b2.setPressed(false);
                return true;
            }
        });


    }
}
