package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private int count_b=1;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private ImageView profile_image;
    String id_group, name_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("MAIN", "onCreate()");
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        id_group= intent.getExtras().getString("GROUP_ID");
        name_group= intent.getExtras().getString("GROUP_NAME");

        getSupportActionBar().setElevation(0);




       //setTheme(R.style.AppTheme);



        

       if (savedInstanceState != null) {
           id_group= savedInstanceState.getString("GROUP_ID");
           name_group= savedInstanceState.getString("GROUP_NAME");
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



        //image profile
        //getandSetImage();

        profile_image = (ImageView) findViewById(R.id.row1_image1);
        getandSetImage();



        /*

        Bitmap bip = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.appname);

         profile_image.setImageBitmap(getclip(bip));

        */




        final Button bGlobal = (Button) findViewById(R.id.bGlobal);
        final Button bGroups = (Button) findViewById(R.id.bGroups);
        final Button bActivities = (Button) findViewById(R.id.bActivities);
        /*

        bGlobal.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.home256x256pressed, 100, 100));

        bActivities.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.activities256x256, 100, 100));

        bGroups.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.groups900x900, 100, 100));
                */

        if (savedInstanceState == null) {
            //Nulla di attivo allora mi attivo il frammento 1 cioè la vista globale
            // QUA INIZIALIZZO IL FINTO DB CON I DATI CHE AVEVA SCRITTO ROBERTO



            //b1.setPressed(true);

            FragmentManager fm = getSupportFragmentManager();
           Fragment fragment = fm.findFragmentById(R.id.fragment1);
           // Fragment fragment= new GlobalListFragment();
            if (fragment == null) {
                fragment = new UsersGroupListFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID",id_group);
                // mBundle.putInt("GROUP_ID",item.getIdgroup());
                fragment.setArguments(mBundle);

                fm.beginTransaction()
                        .add(R.id.fragment1, fragment)
                        .commit();
            }
        }

        bGlobal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//commento


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f= new UsersGroupListFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID",id_group);
                // mBundle.putInt("GROUP_ID",item.getIdgroup());
                f.setArguments(mBundle);

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

            }
        });

        bActivities.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();
                Fragment f= new ActivityGroupListFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID",id_group);
                // mBundle.putInt("GROUP_ID",item.getIdgroup());
                f.setArguments(mBundle);

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

        b4add.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.addexpense593x593, 100, 100));





        b4add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Add expense

                Intent intent=new Intent(MainActivity.this,ActivityExpense.class);
                intent.putExtra("GROUP_ID",id_group);
                intent.putExtra("GROUP_NAME",name_group);
                startActivity(intent);


                return ;
            }
        });











    }


    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Image");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //if != null SET

                final String image = dataSnapshot.getValue(String.class);

                if (image != null) {

                    if (!image.contains("http")) {
                        try {


                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(image);
                            //Bitmap imageCirle = getclip(imageBitmaptaken);
                           // imageBitmaptaken.reconfigure(600,200, Bitmap.Config.ARGB_4444);

                            profile_image.setImageBitmap(imageBitmaptaken);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {



                       /* Glide
                                .with(getApplicationContext())
                                .load(image)
                                .override(600, 200)
                                .fitCenter()
                                .into(profile_image);
*/
                        Picasso.with(MainActivity.this)
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(profile_image);




                       // Bitmap imageBitmaptaken = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                       // Bitmap imageCirle = getclip(imageBitmaptaken);
                       // profile_image.setImageBitmap(imageCirle);



                    }


                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

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
            final Button b1 = (Button) findViewById(R.id.bGlobal);
            final Button b3 = (Button) findViewById(R.id.bActivities);
            final Button b2 = (Button) findViewById(R.id.bGroups);


            //      count_b = savedInstanceState.getInt("COUNT_B");

            if(count_b==1) {

                //count_b=1;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment1));
             //   ft.addToBackStack(null);
                ft.commit();
            }
            else if (count_b==2){

                //count_b = 2;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment2_groups));
              //  ft.addToBackStack(null);
                ft.commit();
            }
            else  if (count_b==3){

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

            //Toast.makeText(getApplicationContext(),R.string.toast_mess_settings,Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, SettingActivity.class));

        }

        if(res_id==R.id.action_contactus){

            //Toast.makeText(getApplicationContext(),R.string.toast_mess_contactus,Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, ContactActivity.class));

        }
        /*

        if(res_id==R.id.action_share){

            Toast.makeText(getApplicationContext(),R.string.toast_mess_share,Toast.LENGTH_LONG).show();

        }*/

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















    public static Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
          //      bitmap.getWidth() / 2, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,bitmap.getHeight() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }





    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }



    @Override
    public void onBackPressed() {

                        startActivity(new Intent(MainActivity.this,PrimaAttivitaGruppi.class));
                        finish();

    }



}

