package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private int count_b=1;
    //firebase auth object
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("MAIN", "onCreate()");
        setContentView(R.layout.activity_main);
        

       if (savedInstanceState != null) {
           count_b=savedInstanceState.getInt("COUNT_B");
                updateFragAndButton();
        }


        //FIREBASE
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        final ImageButton bGlobal = (ImageButton) findViewById(R.id.bGlobal);
        final ImageButton bGroups = (ImageButton) findViewById(R.id.bGroups);
        final ImageButton bActivities = (ImageButton) findViewById(R.id.bActivities);

        if (savedInstanceState == null) {
            //Nulla di attivo allora mi attivo il frammento 1 cioè la vista globale
            // QUA INIZIALIZZO IL FINTO DB CON I DATI CHE AVEVA SCRITTO ROBERTO

            // PROVO A FARE OPERAZIONI SUL DB

                if(DB.getmUsers().isEmpty()) {
                    DB.init();
                }

            /*
                try {



                    if(DBManager.getUsers().isEmpty()) {
                        DBManager.init();

                        // aggiungo un gruppo g3
                        MyGroup g3 = new MyGroup("3", "G3 muore", R.drawable.profilecircle, 40);
                        DBManager.addGroup(g3);
                        // aggiungo un gruppo g4
                        MyGroup g4 = new MyGroup("4", "G4 vive", R.drawable.profilecircle, 50);
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
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                */




            //b1.setPressed(true);
            bGlobal.setBackgroundResource(R.drawable.button_pressed);
            bGroups.setBackgroundResource(R.drawable.buttonshape);
            bActivities.setBackgroundResource(R.drawable.buttonshape);

            bGlobal.setPadding(10,10,10,10);
            bGroups.setPadding(10,10,10,10);
            bActivities.setPadding(10,10,10,10);

            FragmentManager fm = getSupportFragmentManager();
           Fragment fragment = fm.findFragmentById(R.id.fragment1);
           // Fragment fragment= new GlobalListFragment();
            if (fragment == null) {
                fragment = new GlobalListFragment();

                fm.beginTransaction()
                        .add(R.id.fragment1, fragment)
                        .commit();
            }
        }

        bGlobal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//commento

                /*b2.setPressed(false);
                b3.setPressed(false);

                b1.setPressed(true);*/

                bGlobal.setBackgroundResource(R.drawable.button_pressed);
                bGroups.setBackgroundResource(R.drawable.buttonshape);
                bActivities.setBackgroundResource(R.drawable.buttonshape);

                bGlobal.setImageResource(R.drawable.home256x256pressed);
                bActivities.setImageResource(R.drawable.activities256x256);
                bGroups.setImageResource(R.drawable.groups900x900);

                bGlobal.setPadding(10,10,10,10);
                bGroups.setPadding(10,10,10,10);
                bActivities.setPadding(10,10,10,10);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f= new GlobalListFragment();
                if(count_b==1){
                     ft.replace(R.id.fragment1, f);}
                if(count_b==2){
                    ft.replace(R.id.fragment1, f);}
                if(count_b==3){
                    ft.replace(R.id.fragment1, f);}
            //   ft.addToBackStack(null);
                   ft.commit();

                //
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStackImmediate();
                }


                count_b=1;
                return;
            }
        });


        bGroups.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*b1.setPressed(false);
                b3.setPressed(false);
                b2.setPressed(true);*/

                bGlobal.setBackgroundResource(R.drawable.buttonshape);
                bGroups.setBackgroundResource(R.drawable.button_pressed);
                bActivities.setBackgroundResource(R.drawable.buttonshape);

                bGlobal.setImageResource(R.drawable.home256x256);
                bActivities.setImageResource(R.drawable.activities256x256);
                bGroups.setImageResource(R.drawable.groups900x900pressed);

                bGlobal.setPadding(10,10,10,10);
                bGroups.setPadding(10,10,10,10);
                bActivities.setPadding(10,10,10,10);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                GroupsListFragment f= new GroupsListFragment();


                if(count_b==1){
                    ft.replace(R.id.fragment1, f);

                    }
                if(count_b==2){
                    ft.replace(R.id.fragment1, f);}
                if(count_b==3){
                   // ft.replace(R.id.fragment3_activity, f);} TODO: capire come implementare lo stack dei frammenti
                    ft.replace(R.id.fragment1, f);}
              //  ft.addToBackStack(null);
                ft.commit();

                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStackImmediate();
                }

                count_b=2;
                return;
            }
        });

        bActivities.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*b3.setPressed(true);
                b1.setPressed(false);
                b2.setPressed(false);*/

                bGlobal.setBackgroundResource(R.drawable.buttonshape);
                bGroups.setBackgroundResource(R.drawable.buttonshape);
                bActivities.setBackgroundResource(R.drawable.button_pressed);

                bGlobal.setImageResource(R.drawable.home256x256);
                bActivities.setImageResource(R.drawable.activities256x256pressed);
                bGroups.setImageResource(R.drawable.groups900x900);

                bGlobal.setPadding(10,10,10,10);
                bGroups.setPadding(10,10,10,10);
                bActivities.setPadding(10,10,10,10);

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
                    ft.replace(R.id.fragment1, f);}
               // ft.addToBackStack(null);
                ft.commit();

                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStackImmediate();
                }

                count_b=3;
                 return;
            }
        });

        //ADD EXPENSE

        final ImageButton b4add = (ImageButton) findViewById(R.id.bAddNewExpense);


        b4add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Add expense

                Intent intent=new Intent(MainActivity.this,ActivityExpense.class);
                startActivity(intent);


                return ;
            }
        });











    }


        @Override
        protected void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putInt("COUNT_B",count_b);

        }

         //@Override
          /*protected void onRestoreInstanceState(Bundle savedInstanceState) {
             super.onRestoreInstanceState(savedInstanceState);
             count_b=savedInstanceState.getInt("COUNT_B");
         }*/

          @Override
          protected void onRestart(){
              super.onRestart();

             //updateFragAndButton();

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
            final ImageButton b1 = (ImageButton) findViewById(R.id.bGlobal);
            final ImageButton b3 = (ImageButton) findViewById(R.id.bActivities);
            final ImageButton b2 = (ImageButton) findViewById(R.id.bGroups);

            b1.setBackgroundResource(R.drawable.buttonshape);
            b2.setBackgroundResource(R.drawable.buttonshape);
            b3.setBackgroundResource(R.drawable.buttonshape);

            b1.setPadding(10,10,10,10);
            b2.setPadding(10,10,10,10);
            b3.setPadding(10,10,10,10);

            //      count_b = savedInstanceState.getInt("COUNT_B");

            if(count_b==1) {
                b1.setBackgroundResource(R.drawable.button_pressed);
                //count_b=1;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment1));
             //   ft.addToBackStack(null);
                ft.commit();
            }
            else if (count_b==2){
                b2.setBackgroundResource(R.drawable.button_pressed);
                //count_b = 2;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment2_groups));
              //  ft.addToBackStack(null);
                ft.commit();
            }
            else  if (count_b==3){
                b3.setBackgroundResource(R.drawable.button_pressed);
                //count_b=3;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment3_activity));
               // ft.addToBackStack(null);
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

            Toast.makeText(getApplicationContext(),R.string.toast_mess_settings,Toast.LENGTH_LONG).show();

        }

        if(res_id==R.id.action_contactus){

            Toast.makeText(getApplicationContext(),R.string.toast_mess_contactus,Toast.LENGTH_LONG).show();

        }

        if(res_id==R.id.action_share){

            Toast.makeText(getApplicationContext(),R.string.toast_mess_share,Toast.LENGTH_LONG).show();

        }

        if(res_id==R.id.action_myprofile){
            //logging out the user
            //starting login activity
            startActivity(new Intent(this, ReadProfileActivity.class));
        }

        if(res_id==R.id.action_invitation){
            //logging out the user
            //starting login activity
            startActivity(new Intent(this, ActivityInvitation.class));
        }

        if(res_id==R.id.action_logout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        return true;
    }








    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.backmain_title)
                .setMessage(R.string.backmain_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        //intent.putExtra("ID_USER",firebaseAuth.getCurrentUser().getUid());
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                       finish();

                    }
                }).create().show();
    }








}

