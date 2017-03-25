package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = (Button) findViewById(R.id.b1);
        Typeface font = Typeface.createFromAsset(getAssets(), "GoodDog.otf");
        b1.setTypeface(font);

    }
}
