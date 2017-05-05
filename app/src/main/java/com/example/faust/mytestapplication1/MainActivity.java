package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.content.Context;
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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.*;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private int count_b = 1;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private ImageView profile_image;
    String id_group, name_group;
    private boolean bilancio_0;
    private int count_fatti;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RoundedImageView imageprofile;
    private TextView nameprofile;
    private boolean isInSideClicked = false;
    private ImageButton b4add;



    private DBShortKeys dbShortKeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("MAIN", "onCreate()");
        setContentView(R.layout.activity_main);

        findViewById(R.id.drawer_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Intent intent = getIntent();
        id_group = intent.getExtras().getString("GROUP_ID");
        name_group = intent.getExtras().getString("GROUP_NAME");

        getSupportActionBar().setElevation(0);

        b4add = (ImageButton) findViewById(R.id.bAddNewExpense);



      // dbShortKeys = new DBShortKeys();
        /*
        new DBShortKeys().AggiornaBilancioGlobale("k0fWwgOMSmN77HBveT0QVnYpt802");
        new DBShortKeys().AggiornaBilancioGruppo("k0fWwgOMSmN77HBveT0QVnYpt802","2");
        new DBShortKeys().AggiornaBilanciFraUtentiGruppo("k0fWwgOMSmN77HBveT0QVnYpt802","hJqLTxmbV3YNRAM8VPy3HZki5ky2","2");
        new DBShortKeys().AggiornaBilanciFraUtentiGruppo("k0fWwgOMSmN77HBveT0QVnYpt802","OAQlEuOH8mZly2oKyVSZZC8KWLy2","2");

        new DBShortKeys().AggiornaBilancioGlobale("hJqLTxmbV3YNRAM8VPy3HZki5ky2");
        new DBShortKeys().AggiornaBilancioGruppo("hJqLTxmbV3YNRAM8VPy3HZki5ky2","2");
        new DBShortKeys().AggiornaBilanciFraUtentiGruppo("hJqLTxmbV3YNRAM8VPy3HZki5ky2","k0fWwgOMSmN77HBveT0QVnYpt802","2");
        new DBShortKeys().AggiornaBilanciFraUtentiGruppo("hJqLTxmbV3YNRAM8VPy3HZki5ky2","OAQlEuOH8mZly2oKyVSZZC8KWLy2","2");

        new DBShortKeys().AggiornaBilancioGlobale("OAQlEuOH8mZly2oKyVSZZC8KWLy2");
        new DBShortKeys().AggiornaBilancioGruppo("OAQlEuOH8mZly2oKyVSZZC8KWLy2","2");
        new DBShortKeys().AggiornaBilanciFraUtentiGruppo("OAQlEuOH8mZly2oKyVSZZC8KWLy2","k0fWwgOMSmN77HBveT0QVnYpt802","2");
        new DBShortKeys().AggiornaBilanciFraUtentiGruppo("OAQlEuOH8mZly2oKyVSZZC8KWLy2","hJqLTxmbV3YNRAM8VPy3HZki5ky2","2");
        */
/*

        dbShortKeys.AggiornaBilancioGlobale("hJqLTxmbV3YNRAM8VPy3HZki5ky2");
        dbShortKeys.AggiornaBilancioGruppo("hJqLTxmbV3YNRAM8VPy3HZki5ky2","2");
        dbShortKeys.AggiornaBilanciFraUtentiGruppo("hJqLTxmbV3YNRAM8VPy3HZki5ky2","k0fWwgOMSmN77HBveT0QVnYpt802","2");
*/


        //setTheme(R.style.AppTheme);


        if (savedInstanceState != null) {
            id_group = savedInstanceState.getString("GROUP_ID");
            name_group = savedInstanceState.getString("GROUP_NAME");
            count_b = savedInstanceState.getInt("COUNT_B");
            updateFragAndButton();
        }


        //FIREBASE
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        provoMenu();


        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        esisteGruppo();


        //image profile
        //getandSetImage();

        profile_image = (ImageView) findViewById(R.id.row1_image1);

        getandSetImage();
        getandSetImage2();



        /*

        Bitmap bip = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.appname);

         profile_image.setImageBitmap(getclip(bip));

        */


        final Button bGlobal = (Button) findViewById(R.id.bGlobal);
        final Button bGroups = (Button) findViewById(R.id.bGroups);
        final Button bActivities = (Button) findViewById(R.id.bActivities);


        if (savedInstanceState == null) {
            //Nulla di attivo allora mi attivo il frammento 1 cioè la vista globale


            // ((LinearLayout) findViewById(R.id.linear1)).setVisibility(LinearLayout.VISIBLE);
            // ((LinearLayout) findViewById(R.id.linear2)).setVisibility(LinearLayout.INVISIBLE);
            // ((LinearLayout) findViewById(R.id.linear3)).setVisibility(LinearLayout.INVISIBLE);

            bGlobal.setBackground(getResources().getDrawable(R.drawable.shape_toolbar_selected));
            bGroups.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
            bActivities.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
            //bGlobal.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADDarkGreen));
            //bGroups.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));
            //bActivities.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));


            //b1.setPressed(true);

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment1);
            // Fragment fragment= new GlobalListFragment();
            if (fragment == null) {
                fragment = new UsersGroupListFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID", id_group);
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
                bGlobal.setBackground(getResources().getDrawable(R.drawable.shape_toolbar_selected));
                bGroups.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
                bActivities.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
                //bGlobal.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADDarkGreen));
                //bGroups.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));
                //bActivities.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));

                b4add.setVisibility(View.VISIBLE);


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f = new UsersGroupListFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID", id_group);
                // mBundle.putInt("GROUP_ID",item.getIdgroup());
                f.setArguments(mBundle);

                if (count_b == 1) {
                    ft.replace(R.id.fragment1, f);
                }
                if (count_b == 2) {
                    ft.replace(R.id.fragment1, f);
                }
                if (count_b == 3) {
                    ft.replace(R.id.fragment1, f);
                }
                //   ft.addToBackStack(null);
                ft.commit();

                //
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStackImmediate();
                }


                count_b = 1;
                return;
            }
        });


        bGroups.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                bGlobal.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
                bGroups.setBackground(getResources().getDrawable(R.drawable.shape_toolbar_selected));
                bActivities.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
                //bGlobal.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));
                //bGroups.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADDarkGreen));
                //bActivities.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));

                b4add.setVisibility(View.GONE);


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f = new PlotFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID", id_group);
                // mBundle.putInt("GROUP_ID",item.getIdgroup());
                f.setArguments(mBundle);

                if (count_b == 1) {
                    ft.replace(R.id.fragment1, f);
                }
                if (count_b == 2) {
                    ft.replace(R.id.fragment1, f);
                }
                if (count_b == 3) {
                    ft.replace(R.id.fragment1, f);
                }
                //   ft.addToBackStack(null);
                ft.commit();

                //
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStackImmediate();
                }


                count_b = 2;
                return;

            }
        });

        bActivities.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //bGlobal.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));
                //bGroups.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADLightGreen));
                //bActivities.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BreakingMADDarkGreen));
                bGlobal.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
                bGroups.setBackground(getResources().getDrawable(R.drawable.shape_toolbar));
                bActivities.setBackground(getResources().getDrawable(R.drawable.shape_toolbar_selected));

                FragmentManager fm = getSupportFragmentManager();

                b4add.setVisibility(View.VISIBLE);

                FragmentTransaction ft = fm.beginTransaction();
                Fragment f = new ActivityGroupListFragment();
                Bundle mBundle;
                mBundle = new Bundle();
                mBundle.putString("GROUP_ID", id_group);
                // mBundle.putInt("GROUP_ID",item.getIdgroup());
                f.setArguments(mBundle);

                if (count_b == 1) {
                    ft.replace(R.id.fragment1, f);
                }
                //   ft.remove(fm.findFragmentById(R.id.fragment1)).add(R.id.fragment3_activity,f);} sbagliato
                if (count_b == 2) {
                    //ft.replace(R.id.fragment2_groups, f);} TODO: capire come implementare lo stack dei frammenti
                    ft.replace(R.id.fragment1, f);
                }
                if (count_b == 3) {
                    ft.replace(R.id.fragment1, f);
                }
                // ft.addToBackStack(null);
                ft.commit();

                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStackImmediate();
                }

                count_b = 3;
                return;
            }
        });

        //ADD EXPENSE


        b4add.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.addexpense593x593, 100, 100));


        b4add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Add expense

                Intent intent = new Intent(MainActivity.this, ActivityExpense.class);
                intent.putExtra("GROUP_ID", id_group);
                intent.putExtra("GROUP_NAME", name_group);
                startActivity(intent);


                return;
            }
        });


    }


    private void getandSetImage() {

        //getImage of user

        String url;

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
        savedInstanceState.putInt("COUNT_B", count_b);

    }

    //@Override
          /*protected void onRestoreInstanceState(Bundle savedInstanceState) {
             super.onRestoreInstanceState(savedInstanceState);
             count_b=savedInstanceState.getInt("COUNT_B");
         }*/

    @Override
    protected void onRestart() {
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


    private void updateFragAndButton() {
        final Button b1 = (Button) findViewById(R.id.bGlobal);
        final Button b3 = (Button) findViewById(R.id.bActivities);
        final Button b2 = (Button) findViewById(R.id.bGroups);


        //      count_b = savedInstanceState.getInt("COUNT_B");

        if (count_b == 1) {

            b4add.setVisibility(View.VISIBLE);

            //count_b=1;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment1));
            //   ft.addToBackStack(null);
            ft.commit();
        } else if (count_b == 2) {

            b4add.setVisibility(View.GONE);

            //count_b = 2;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment1, fm.findFragmentById(R.id.fragment_plot));
            //  ft.addToBackStack(null);
            ft.commit();
        } else if (count_b == 3) {

            b4add.setVisibility(View.VISIBLE);

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
        int res_id = item.getItemId();
        if (res_id == R.id.action_addUserToGroup) {

            Intent intent = new Intent(MainActivity.this, ActivityAddUserToGroup.class);
            intent.putExtra("ID_GROUP", id_group);
            intent.putExtra("NAME_GROUP", name_group);
            startActivity(intent);

        }

        if (res_id == R.id.action_modifyGroup) {

            Intent intent = new Intent(MainActivity.this, ManageGroupActivity.class);
            intent.putExtra("ID_GROUP", id_group);
            intent.putExtra("NAME_GROUP", name_group);
            startActivity(intent);


        }

        if (res_id == R.id.action_deleteGroup) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.deletegroup_title)
                    .setMessage(R.string.deletegroup_message)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonDeleteGroup();

                        }
                    }).create().show();

        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);


            return super.onOptionsItemSelected(item);
        } else {

            mDrawerToggle.setDrawerIndicatorEnabled(true);

            drawerLayout.closeDrawers();


        }

        // Handle your other action bar items...


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
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getHeight() / 2, paint);
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

        startActivity(new Intent(MainActivity.this, PrimaAttivitaGruppi.class));
        finish();

    }


    private void buttonDeleteGroup() {

        //per ogni utente controllare se tutti i bilanci sono 0
        // se ne trovo uno diverso da 0 non posso cancellare altrimenti
        // cancello il gruppo per ogni utente
        //cancello il gruppo nei gruppi
        //riporto nella global


        //step1 prendere tutti gli utenti di quel gruppo
        //user group


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");

        //databaseReference.addValueEventListener(new ValueEventListener() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bilancio_0 = true;
                final ArrayList<String> id_utenti = new ArrayList<String>();


                for (DataSnapshot take : dataSnapshot.getChildren()) {
                    //utenti
                    //
                    id_utenti.add(take.getKey());
                }


                //dopo aver preso gli utenti controllare
                //se per ogni di questo utente il bilancio di quel gruppo e' 0 cancello altrimenti TOAST

                count_fatti = 0;

                for (String id : id_utenti) {

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Groups").child(id_group).child("Total");

                    //databaseReference2.addValueEventListener(new ValueEventListener() {
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Double bilancio_singolo = dataSnapshot.getValue(Double.class);


                            if (bilancio_singolo != null) {
                                if (bilancio_singolo > 0.0) {
                                    bilancio_0 = false;
                                }
                            }

                            count_fatti++;


                            if (count_fatti == id_utenti.size()) {

                                if (bilancio_0 == true) {
                                    //posso eliminare
                                    //   Toast.makeText(getContext(),R.string.yes_delete_group,Toast.LENGTH_LONG).show();

                                    //dovrei eliminarlo dal gruppo e da tutti gli utenti


                                    //Remove


                                    String id_group2 = new String(id_group);


                                    finish();


                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    ref.child("Groups").child(id_group2).removeValue();


                                    //problema concorrenza se lo mando in una attivita che vuole usare questo listener sui gruppis
                                    for (String id : id_utenti) {

                                        String id2 = new String(id);
                                        ref.child("Users").child(id2).child("Groups").child(id_group2).removeValue();
                                    }


                                    Intent intent = new Intent(MainActivity.this, DeleteGroupActivity.class);
                                    intent.putExtra("ID_GROUP", id_group);
                                    intent.putExtra("NAME_GROUP", name_group);
                                    startActivity(intent);
                                    // activity.finish(); //todoprovo


                                } else {
                                    //qualcuno lo ha diverso da 0 mando il toast

                                    Toast.makeText(MainActivity.this, R.string.no_delete_group, Toast.LENGTH_LONG).show();

                                }


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    /*private void buttonDeleteGroup(){

        //per ogni utente controllare se tutti i bilanci sono 0
        // se ne trovo uno diverso da 0 non posso cancellare altrimenti
        // cancello il gruppo per ogni utente
        //cancello il gruppo nei gruppi
        //riporto nella global


        //step1 prendere tutti gli utenti di quel gruppo
        //user group



        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");
        DatabaseReference databaseReferenceT1 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group);
        databaseReferenceT1.runTransaction(new Transaction.Handler()
        {
            @Override
            public Transaction.Result doTransaction(final MutableData mutableData)
            {
                if(mutableData.getValue()==null) // significa che ho cancellato il gruppo (questo if probabilmente verrà eseguito come ultimo step del loop, dopo che avrò eliminato il gruppo)
                {
                    return Transaction.success(mutableData); // probabilmente dopo questa chiamata uscirò dal loop
                }

                // il gruppo ancora esiste

                bilancio_0 = true;
                final ArrayList<String> id_utenti = new ArrayList<String>();

                for(MutableData take: mutableData.child("Users").getChildren()){
                    //utenti
                    //
                    id_utenti.add(take.getKey());
                }

                //dopo aver preso gli utenti controllare
                //se per ogni di questo utente il bilancio di quel gruppo e' 0 cancello altrimenti TOAST

                count_fatti=0;

                for(String id : id_utenti){

                    count_fatti++;

                    //DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Groups").child(id_group).child("Total");
                    DatabaseReference databaseReferenceT2 = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Groups").child(id_group);

                    databaseReferenceT2.runTransaction(new Transaction.Handler()
                    {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData)
                        {
                            if(mutableData.getValue()==null)
                            {
                                return Transaction.success(mutableData); // fine della seconda transazione
                            }

                            Double bilancio_singolo = mutableData.child("Total").getValue(Double.class);

                            if (bilancio_singolo != null)
                            {
                                if (bilancio_singolo > 0.0)
                                {
                                    bilancio_0 = false;
                                }
                            }

                            if (count_fatti == id_utenti.size())
                            {

                                if (bilancio_0 == true)
                                {
                                    //posso eliminare
                                    //   Toast.makeText(getContext(),R.string.yes_delete_group,Toast.LENGTH_LONG).show();

                                    //dovrei eliminarlo dal gruppo e da tutti gli utenti

                                    //Remove
                                    String id_group2 = new String(id_group);

                                    //problema concorrenza se lo mando in una attivita che vuole usare questo listener sui gruppis
                                    for (String id : id_utenti)
                                    {

                                        String id2 = new String(id);
                                        ref.child("Users").child(id2).child("Groups").child(id_group2).removeValue();
                                    }

                                    Intent intent = new Intent(MainActivity.this, DeleteGroupActivity.class);
                                    intent.putExtra("ID_GROUP", id_group);
                                    intent.putExtra("NAME_GROUP", name_group);
                                    startActivity(intent);
                                    // activity.finish(); //todoprovo


                                } else
                                {
                                    //qualcuno lo ha diverso da 0 mando il toast

                                    Toast.makeText(MainActivity.this, R.string.no_delete_group, Toast.LENGTH_LONG).show();

                                }


                            }




                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot)
                        {
                            String id_group2 = new String(id_group);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("Groups").child(id_group2).removeValue();
                        }
                    });








                }
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot)
            {
                finish();
            }
        });

    }*/


    private void provoMenu() {


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // update the actionbar to show the up carat/affordance


        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.menulaterale);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerToggle.setDrawerIndicatorEnabled(true);
                drawerLayout.closeDrawers();


                //Checking if the item is in checked state or not, if not set it to checked state.
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);


                int res_id = menuItem.getItemId();


                //Check to see which item was clicked and perform the appropriate action.
                if (res_id == R.id.action_settings) {

                    //Toast.makeText(getApplicationContext(),R.string.toast_mess_settings,Toast.LENGTH_LONG).show();

                    startActivity(new Intent(MainActivity.this, SettingActivity.class));

                }

                if (res_id == R.id.action_contactus) {

                    //Toast.makeText(getApplicationContext(),R.string.toast_mess_contactus,Toast.LENGTH_LONG).show();

                    startActivity(new Intent(MainActivity.this, ContactActivity.class));

                }


                if (res_id == R.id.action_myprofile) {
                    //logging out the user
                    //starting login activity
                    Intent i = new Intent(MainActivity.this, ReadProfileActivity.class);
                    i.putExtra("MY_PROFILE", "YES");
                    startActivity(i);

                }

                if (res_id == R.id.action_invitation) {
                    //logging out the user
                    //starting login activity
                    startActivity(new Intent(MainActivity.this, ActivityInvitation.class));
                }

                if (res_id == R.id.action_logout) {
                    //logging out the user
                    firebaseAuth.signOut();
                    //closing activity
                    finish();
                    //starting login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

                return false;


            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);


                invalidateOptionsMenu();
            }
        };


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //mDrawerToggle.syncState();


    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void getandSetImage2() {

        //getImage of user

        NavigationView navigationView = (NavigationView) findViewById(R.id.menulaterale);
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_top);


        imageprofile = (com.makeramen.roundedimageview.RoundedImageView) headerView.findViewById(R.id.menu_profile_image);
        imageprofile.setScaleType(ImageView.ScaleType.CENTER_CROP);
        nameprofile = (TextView) headerView.findViewById(R.id.menu_profile_name);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //if != null SET

                nameprofile.setText(dataSnapshot.child("Name").getValue(String.class));
                final String image = dataSnapshot.child("Image").getValue(String.class);

                if (image != null) {

                    if (!image.contains("http")) {
                        try {
                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(image);
                            //Bitmap imageCirle = getclip(imageBitmaptaken);
                            imageprofile.setImageBitmap(imageBitmaptaken);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {


                        Picasso.with(MainActivity.this)
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(imageprofile);


                        // Bitmap imageBitmaptaken = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                        // Bitmap imageCirle = getclip(imageBitmaptaken);
                        // profile_image.setImageBitmap(imageCirle);


                    }


                    imageprofile.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, FullScreenImage.class);

                            imageprofile.buildDrawingCache();
                            Bitmap image2 = imageprofile.getDrawingCache();

                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image2);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                    });


                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void esisteGruppo() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() == null) {

                    Intent i = new Intent(MainActivity.this, PrimaAttivitaGruppi.class);

                    startActivity(i);
                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }


}

