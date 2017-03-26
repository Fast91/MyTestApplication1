package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class MainActivity extends AppCompatActivity {
    private int count_b=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       if (savedInstanceState != null) {
                updateFragAndButton();
        }



        final Button b1 = (Button) findViewById(R.id.b1);
        final Button b2 = (Button) findViewById(R.id.b2);
        final Button b3 = (Button) findViewById(R.id.b3);

        if (savedInstanceState == null) {
            //Nulla di attivo allora mi attivo il frammento 1 cioè la vista globale

            b1.setPressed(true);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment1);
            if (fragment == null) {
                fragment = new GlobalListFragment();
                ;
                fm.beginTransaction()
                        .add(R.id.fragment1, fragment)
                        .commit();
            }
        }


        b1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                b2.setPressed(false);
                b3.setPressed(false);

                b1.setPressed(true);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f= new GlobalListFragment();
                if(count_b==1){
                     ft.replace(R.id.fragment1, f);}
                if(count_b==2){
                    ft.replace(R.id.fragment2_groups, f);}
                if(count_b==3){
                    ft.replace(R.id.fragment3_activity, f);}
               ft.addToBackStack("F1");
                   ft.commit();

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

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                GroupsListFragment f= new GroupsListFragment();

                if(count_b==1){
                    ft.replace(R.id.fragment1, f);

                    }
                if(count_b==2){
                    ft.replace(R.id.fragment2_groups, f);}
                if(count_b==3){
                   // ft.replace(R.id.fragment3_activity, f);} TODO: capire come implementare lo stack dei frammenti
                    ft.replace(R.id.fragment1, new GroupsListFragment());}
                ft.addToBackStack("F2");
                ft.commit();

                count_b=2;
                return true;
            }
        });

        b3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                b3.setPressed(true);
                b1.setPressed(false);
                b2.setPressed(false);

                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();
                Fragment f= new ActivityListFragment();

                if(count_b==1){
                    ft.replace(R.id.fragment1, f);}
             //   ft.remove(fm.findFragmentById(R.id.fragment1)).add(R.id.fragment3_activity,f);} sbagliato
                if(count_b==2){
                    //ft.replace(R.id.fragment2_groups, f);} TODO: capire come implementare lo stack dei frammenti
                    ft.replace(R.id.fragment1, f);}
                if(count_b==3){
                    ft.replace(R.id.fragment3_activity, f);}
                ft.addToBackStack("F3");
                ft.commit();

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

             updateFragAndButton();

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


        private void updateFragAndButton(){
            final Button b1 = (Button) findViewById(R.id.b1);
            final Button b2 = (Button) findViewById(R.id.b2);
            final Button b3 = (Button) findViewById(R.id.b3);

            //      count_b = savedInstanceState.getInt("COUNT_B");

            if(count_b==1) {
                b1.setPressed(true);
                b2.setPressed(false);
                b3.setPressed(false);
                count_b=1;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment1));
                ft.commit();
            }
            else if (count_b==2){
                b1.setPressed(false);
                b3.setPressed(false);
                b2.setPressed(true);
                count_b = 2;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment2_groups, fm.findFragmentById(R.id.fragment2_groups));
                ft.commit();
            }
            else{
                b3.setPressed(true);
                b1.setPressed(false);
                b2.setPressed(false);
                count_b=3;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment3_activity, fm.findFragmentById(R.id.fragment3_activity));
                ft.commit();
            }
        }


}

