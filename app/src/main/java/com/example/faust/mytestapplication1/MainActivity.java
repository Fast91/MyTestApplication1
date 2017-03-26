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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private int count_b=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // QUA INIZIALIZZO IL FINTO DB CON I DATI CHE AVEVA SCRITTO ROBERTO
        DBManager.init();
        // PROVO A FARE OPERAZIONI SUL DB
        try {
            // aggiungo un gruppo g3
            MyGroup g3 = new MyGroup("3", "G3 muore",R.drawable.profilecircle,40);
            DBManager.addGroup(g3);
            // aggiungo un gruppo g4
            MyGroup g4 = new MyGroup("4", "G4 vive",R.drawable.profilecircle,50);
            DBManager.addGroup(g4);
            // elimino il gruppo g3
            DBManager.removeGroup(g3);
            // modifico il gruppo g4 in locale e aggiorno il db
            g4.setName("G4");
            DBManager.updateGroup(g4);
            // ancora bisogna sistemare un po' di cose
            // io eviterei la modifica dei dati in locale ma direttamente sul db e poi
            // la copia dal db al locale.
            // in questo modo i miei dati saranno sempre sincronizzati col db online.

        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        
        // QUA INIZIALIZZO IL FINTO DB CON I DATI CHE AVEVA SCRITTO ROBERTO
        DBManager.init();
        // PROVO A FARE OPERAZIONI SUL DB
        try {
            // aggiungo un gruppo g3
            MyGroup g3 = new MyGroup("3", "G3 muore",R.drawable.profilecircle,40);
            DBManager.addGroup(g3);
            // aggiungo un gruppo g4
            MyGroup g4 = new MyGroup("4", "G4 vive",R.drawable.profilecircle,50);
            DBManager.addGroup(g4);
            // elimino il gruppo g3
            DBManager.removeGroup(g3);
            // modifico il gruppo g4 in locale e aggiorno il db
            g4.setName("G4");
            DBManager.updateGroup(g4);
            // ancora bisogna sistemare un po' di cose
            // io eviterei la modifica dei dati in locale ma direttamente sul db e poi
            // la copia dal db al locale.
            // in questo modo i miei dati saranno sempre sincronizzati col db online.

        } catch (Exception e) {
            e.printStackTrace();
        }

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


               ft.addToBackStack(null);
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
                    ft.replace(R.id.fragment1, f);}
               ft.addToBackStack(null);
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
               ft.addToBackStack(null);
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
                ft.addToBackStack(null);
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
                ft.addToBackStack(null);
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
              ft.addToBackStack(null);
                ft.commit();
            }
        }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    //-- azioni per il menù della tool_bar--

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id=item.getItemId();
        if(res_id==R.id.action_settings){

            Toast.makeText(getApplicationContext(),"you selected Settings option",Toast.LENGTH_LONG).show();

        }

        if(res_id==R.id.action_contactus){

            Toast.makeText(getApplicationContext(),"you selected Contact Us option",Toast.LENGTH_LONG).show();

        }

        if(res_id==R.id.action_share){

            Toast.makeText(getApplicationContext(),"you selected Share option",Toast.LENGTH_LONG).show();

        }

        return true;
    }


}

