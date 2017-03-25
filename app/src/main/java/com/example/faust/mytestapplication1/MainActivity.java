package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private int count_b=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            final Button b1 = (Button) findViewById(R.id.b1);
            final Button b2 = (Button) findViewById(R.id.b2);
            final Button b3 = (Button) findViewById(R.id.b3);

      //      count_b = savedInstanceState.getInt("COUNT_B");

            if(count_b==1) {
                b1.setPressed(true);
            }
            else if (count_b==2){
                b2.setPressed(true);
            }
            else{
                b3.setPressed(true);
            }


        }


        final Button b1 = (Button) findViewById(R.id.b1);
        final Button b2 = (Button) findViewById(R.id.b2);
        final Button b3 = (Button) findViewById(R.id.b3);


        b1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b1.setPressed(true);
                b2.setPressed(false);
                b3.setPressed(false);
                count_b=1;
                return true;
            }
        });


        b2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b1.setPressed(false);
                b3.setPressed(false);
                b2.setPressed(true);
                count_b = 2;
                /*
                Intent i = new Intent(MainActivity.this,GroupsActivity.class);
                startActivity(i);
                return true;
                */
                /*
                View MainView = findViewById(R.id.linearLayoutMain);
                ViewGroup parent = (ViewGroup) MainView.getParent();
                int index = parent.indexOfChild(MainView);
                parent.removeView(MainView);
                parent.addView(findViewById(R.id.linearLayoutGroups),index);
                */

                return true;
            }
        });

        b3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b3.setPressed(true);
                b1.setPressed(false);
                b2.setPressed(false);
                count_b=3;
                return true;
            }
        });

    }

        @Override
        protected void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putInt("COUNT_B",count_b);

        }

         @Override
          protected void onRestoreInstanceState(Bundle savedInstanceState) {
             super.onRestoreInstanceState(savedInstanceState);
             count_b=savedInstanceState.getInt("COUNT_B");
         }

          @Override
          protected void onRestart(){
              super.onRestart();

              final Button b1 = (Button) findViewById(R.id.b1);
              final Button b2 = (Button) findViewById(R.id.b2);
              final Button b3 = (Button) findViewById(R.id.b3);

              //      count_b = savedInstanceState.getInt("COUNT_B");

              if(count_b==1) {
                  b1.setPressed(true);
              }
              else if (count_b==2){
                  b2.setPressed(true);
              }
              else{
                  b3.setPressed(true);
              }

          }

         @Override
         protected void onResume() {
                super.onResume();

         }

         @Override
         protected void onPause() {
              super.onPause();
              }

         @Override
         protected void onStop() {
                super.onStop();
             }
}

