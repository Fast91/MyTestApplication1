package com.example.faust.mytestapplication1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import com.google.android.gms.appinvite.*;
import com.google.android.gms.common.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityAddUserToGroup extends AppCompatActivity {// implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "TAG";
    private static final int REQUEST_INVITE = 0;

    private FirebaseAuth firebaseAuth;
    private String id_group, name_group;
    private DatabaseReference databaseReference, databaseReference2, getDatabaseReference3;
    private HashMap<String,String> key_nameuser;
    String userMail=null;
    Boolean exists=false;
    String id_nuovoutentedaaggiungere, name_nuovoutentedaaggiungere;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ProgressDialog mProgressDialog;

    private Activity myactivity;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);

        myactivity=this;



        Log.d("EXISTS", "Provo AUTH" );
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("EXISTS", "Provo a prendere id e name group" );
        id_group = getIntent().getExtras().getString("ID_GROUP");
        name_group = getIntent().getExtras().getString("NAME_GROUP");
        Log.d("EXISTS", "finito di prenderli" );
        key_nameuser= new HashMap<>();

      mProgressDialog = new ProgressDialog(this);

        String msg = getString(R.string.dialog_image_profile_loading);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();


        TextView text_name =(TextView) findViewById(R.id.textGroup_activity_group_members);
        text_name.setText(name_group);

        ImageView iv = (ImageView) findViewById(R.id.groupImage_group_activity_members);


        iv.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.group, 100, 100));


        autocomplete();







        if (savedInstanceState != null) {

        }

        //ButtonADD per aggiungere il gruppo
        ImageButton addGroupUser = (ImageButton)  findViewById(R.id.activity_group_members_addmember);

        addGroupUser.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.plus_add_green, 100, 100));

        addGroupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



               final AutoCompleteTextView editTextUserMail = (AutoCompleteTextView) findViewById(R.id.memberMail_activity_group_members);
                 userMail = editTextUserMail.getText().toString();
                userMail=userMail.trim();
                if(!userMail.equals("")){




                    userMail=userMail.toLowerCase();
                    //////////////////
                    /////
                    ///// DB cercare l'utente e controllare se esiste
                    /////
                    /////////////////


                    //DATI UTILI --> Utenti già presenti hashmap key, name
                    //nome del gruppo e id del gruppo
                    // id_group e name_group



                    //step 0 prendere gli utenti già registrati
                    // GROUPS - ID - USERS

                    Log.d("EXISTS", "PROVO a prendere i vecchi utenti" );




                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue()==null){


                            }

                            else {

                                //Per tutti gli utenti aggiungerli
                                for (DataSnapshot users : dataSnapshot.getChildren()) {

                                    key_nameuser.remove(users.getKey());
                                    key_nameuser.put(users.getKey(), users.child("Name").getValue(String.class));

                                }


                                ////////////////////


                                //step 0.1
                                // controllare se l'utente è registrato userMail
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        //non esiste
                                        exists = false;

                                        //per tutti gli utenti controllare se esiste quello con quella mail
                                        for (DataSnapshot user : snapshot.getChildren()) {

                                            if (user.child("Email").getValue(String.class).equals(userMail)) {
                                                //esiste
                                                exists = true;
                                                id_nuovoutentedaaggiungere = user.getKey();
                                                name_nuovoutentedaaggiungere = user.child("Name").getValue(String.class);
                                            }
                                        }//fine FOR

                                        Log.d("EXISTS", "usermail : " + userMail + " GROUPNAME : " + name_group + "  GROUPID : " + id_group);
                                        Log.d("EXISTS", "esiste : " + exists);


                                        if (exists == true) {


                                            //controllare se già è stato aggiunto
                                            if (key_nameuser.containsKey(id_nuovoutentedaaggiungere)) {

                                                Toast.makeText(getApplicationContext(), R.string.toast_addedusergroupAlreadyAdded, Toast.LENGTH_LONG).show();

                                            } else {
                                                addUserToGroup();
                                                Log.d("EXISTS", "Sono uscito dal metodo");

                                                Toast.makeText(getApplicationContext(), R.string.toast_addedusergroup, Toast.LENGTH_LONG).show();
                                                Log.d("EXISTS", "Fine toast");
                                                editTextUserMail.setText("");

                                            }
                                        } else {

                                            popUpInvitation();

                                        }


                                        Log.d("EXISTS", "Fine  ondatachange");

                                        // return;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {


                                        // return ;
                                    }
                                });


                            }





                            /////////////////////




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    Log.d("EXISTS", "finito di prenderli" );











                    Log.d("EXISTS", "Uscito listener");

                    //////////////////
                    /////
                    ///// FINE DB
                    /////
                    /////////////////






                   }

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                   }







                //aggiorno recyclerview

                databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");

                //Read content data
                databaseReference2.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        HashMap<String, NomeDovuto> utenti_dovuto = new HashMap<>();

                        //Prendo tutti i gruppi
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                            //prendo gli amici
                            String name = postSnapshot.child("Name").getValue(String.class);
                            utenti_dovuto.put(postSnapshot.getKey(), new NomeDovuto(postSnapshot.getKey(), name));



                        }



                        ///Adesso che ho la mia cazzo di lista bella piena
                        //Posso settare gli elementi nell'adapter porca puttana eva
                        ///

                        // Set the adapter
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_add_user_to_group);

                                List list = new ArrayList(utenti_dovuto.values());





                              mAdapter = new ActivityDetailAdapter(list);
                              recyclerView.setAdapter(mAdapter);



                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


//fine recycler



            }

        });


        Log.d("EXISTS", "Continuazione");

        //inserisco il recycler



        databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");

        //Read content data
        databaseReference2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String, NomeDovuto> utenti_dovuto = new HashMap<>();

                //Prendo tutti i gruppi
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    //prendo gli amici
                    String name = postSnapshot.child("Name").getValue(String.class);
                    utenti_dovuto.put(postSnapshot.getKey(), new NomeDovuto(postSnapshot.getKey(), name));



                }



                ///Adesso che ho la mia cazzo di lista bella piena
                //Posso settare gli elementi nell'adapter porca puttana eva
                ///

                // Set the adapter
                recyclerView = (RecyclerView) findViewById(R.id.recycler_add_user_to_group);

                List list = new ArrayList(utenti_dovuto.values());






                      mAdapter = new ActivityDetailAdapter(list);
                      recyclerView.setAdapter(mAdapter);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




        //termino recycler



        //ButtonSubmit per finire il gruppo ed andare alla main activity
        Button submitGroupUser = (Button)  findViewById(R.id.buttonSubmit_activity_group_member);




        submitGroupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*
                TextView textViewGroupName = (TextView) findViewById(R.id.textGroup_activity_group_members);
                String groupName = textViewGroupName.getText().toString();

                EditText editTextUserMail = (EditText) findViewById(R.id.memberMail_activity_group_members);
                String userMail = editTextUserMail.getText().toString();

               */



                    Intent intent=new Intent(ActivityAddUserToGroup.this,MainActivity.class);
                    intent.putExtra("GROUP_ID",id_group);
                intent.putExtra("GROUP_NAME",name_group);
                    startActivity(intent);
                    finish();


                    return ;


            }

        });



        Log.d("EXISTS", "End");


    }






    private void addUserToGroup(){

        Log.d("EXISTS", "INIZIO ADDUSERTOGROUP");

        //step 1
        // aggiunge al gruppo l'utente
        // groups - idgruppo - users - idutente - name

        FirebaseDatabase.getInstance().getReference("Groups").child(id_group)
                .child("Users").child(id_nuovoutentedaaggiungere).child("Name").setValue(name_nuovoutentedaaggiungere);





        //step 2
        // aggiungere l'utente al gruppo
        // users- id utente - groups - id group ---> namegroup + total 0

        FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                .child("Groups").child(id_group).child("Name").setValue(name_group);




        Double tmp=0.0;
        FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                .child("Groups").child(id_group).child("Total").setValue(tmp);




        // users- id utente - groups - id group ---> users settare tutti gli utente già inseriti e tutto a 0 + name

        for(String id_user : key_nameuser.keySet()){

            String Name = key_nameuser.get(id_user);
            tmp=0.0;

            FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                    .child("Groups").child(id_group).child("Users").child(id_user)
                    .child("Name").setValue(Name);

            FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                    .child("Groups").child(id_group).child("Users").child(id_user)
                    .child("Total").setValue(tmp);

        }



        //step 3
        // aggiungere agli utenti già inseriti questo nuovo utente --> name + con total 0

        for(String id_user : key_nameuser.keySet()){






            if(!id_user.equals(id_nuovoutentedaaggiungere)) {







                DatabaseReference dbref=  FirebaseDatabase.getInstance().getReference("Users").child(id_user)
                        .child("Groups");



                dbref.child(id_group).child("Users").child(id_nuovoutentedaaggiungere)
                        .child("Total").setValue(tmp);

                String nuovastringanome =new String(name_nuovoutentedaaggiungere);

                dbref.child(id_group).child("Users").child(id_nuovoutentedaaggiungere)
                        .child("Name").setValue(nuovastringanome);



            }


        }







        //aggiornare mappa hash map

        Log.d("EXISTS", "Tutte cose aggiunte  PROVO A LEVARE");
        key_nameuser.remove(id_nuovoutentedaaggiungere);
        key_nameuser.put(id_nuovoutentedaaggiungere,name_nuovoutentedaaggiungere);
        Log.d("EXISTS", "LEVATO");


    }




    private void popUpInvitation(){


        new AlertDialog.Builder(this)
                .setTitle(R.string.user_not_registered_title)
                .setMessage(R.string.user_not_registered_msg)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Set the right invitation_message
                    String msg = getString(R.string.invitation_message)+" "+id_group;

                        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                                .setMessage(msg)
                                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                                //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                                //.setCallToActionText(getString(R.string.invitation_cta))
                                .setEmailHtmlContent("&lthtml&gt&ltbody&gt"
                                        + "&lta href=\"%%APPINVITE_LINK_PLACEHOLDER%%\"&gtInvitation&lt/a&gt"
                                        + "&lt/body&gt&lt/html&gt")
                                .setEmailSubject("Breaking MAD invite")
                                .build();
                        startActivityForResult(intent, REQUEST_INVITE);

                       // Toast.makeText(getApplicationContext(),"INVITATO",Toast.LENGTH_SHORT).show();



                    }
                }).create().show();




    }


    /*
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult);


    }
    */

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);

                for (String id : ids) {
                    // Log.d(TAG, "onActivityResult: sent invitation " + id);

                }

                    ///////////////////
                    //////
                    ////// IMPLEMENTARE DB PER OGNI INVITATO
                    //////
                    ////////////////////







                    ///////////////////
                    //////
                    ////// FINE DB PER OGNI INVITATO
                    //////
                    ////////////////////



                Toast.makeText(getApplicationContext(),R.string.email_sent,Toast.LENGTH_SHORT).show();

            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
               // Log.d(TAG, "Sending invitation FAILED" );
                // [END_EXCLUDE]
                Toast.makeText(getApplicationContext(),R.string.email_notsent,Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END on_activity_result]




    private class ActivityDetailHolder extends RecyclerView.ViewHolder
    {
        private NomeDovuto mDetailEntry;
        private TextView mUserNameTextView;
        private ImageView imagev;


        public ActivityDetailHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.item_image_name, parent, false));
            mUserNameTextView = (TextView) itemView.findViewById(R.id.item_name);
            imagev = (ImageView) itemView.findViewById(R.id.item_image);

        }

        public void bind(NomeDovuto detailEntry)
        {
            mDetailEntry = detailEntry;
            mUserNameTextView.setText(mDetailEntry.getName());


            imagev.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.profilecircle, 100, 100));


        }

    }


    private class ActivityDetailAdapter extends RecyclerView.Adapter<ActivityAddUserToGroup.ActivityDetailHolder>
    {
        private List<NomeDovuto> mDetailEntries;

        public ActivityDetailAdapter(List<NomeDovuto> detailEntries)
        {
            mDetailEntries = detailEntries;
        }


        @Override
        public ActivityAddUserToGroup.ActivityDetailHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ActivityAddUserToGroup.ActivityDetailHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ActivityAddUserToGroup.ActivityDetailHolder holder, int position)
        {
            NomeDovuto detailEntry = mDetailEntries.get(position);
            holder.bind(detailEntry);
        }

        @Override
        public int getItemCount()
        {
            return mDetailEntries.size();
        }
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


    public void autocomplete(){




        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> strings = new ArrayList<String>();

                for(DataSnapshot dp : dataSnapshot.getChildren()){

                    String name = dp.child("Email").getValue(String.class);
                    strings.add(name);

                }

                String[] myarray = new String[strings.size()];
                strings.toArray(myarray);



                ///aggiungere adapter ecc

                //Creating the instance of ArrayAdapter containing list of language names
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (ActivityAddUserToGroup.this,android.R.layout.select_dialog_item,myarray);

                //Getting the instance of AutoCompleteTextView
                AutoCompleteTextView actv= (AutoCompleteTextView)findViewById(R.id.memberMail_activity_group_members);
                actv.setThreshold(1);//will start working from first character
                actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                actv.setTextColor(Color.RED);

                mProgressDialog.dismiss();







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }






}
