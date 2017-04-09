package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.android.gms.appinvite.*;
import com.google.android.gms.common.*;


import java.util.HashMap;

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


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);


        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addApi(AppInvite.API)
                                .enableAutoManage(this,this)
                                .build();
        */

        /*

        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient,this,autoLaunchDeepLink)
                .setResultCallback( new ResultCallback<AppInviteInvitationResult>() {
                    @Override
                    public void onResult(AppInviteInvitationResult result) {
                        Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                        if (result.getStatus().isSuccess()) {
                            // Extract information from the intent
                            Intent intent = result.getInvitationIntent();
                            String deepLink = AppInviteReferral.getDeepLink(intent);
                            String invitationId = AppInviteReferral.getInvitationId(intent);

                            // Because autoLaunchDeepLink = true we don't have to do anything
                            // here, but we could set that to false and manually choose
                            // an Activity to launch to handle the deep link here.
                            // ...
                        }
                    }
                });

        */






        Log.d("EXISTS", "Provo AUTH" );
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("EXISTS", "Provo a prendere id e name group" );
        id_group = getIntent().getExtras().getString("ID_GROUP");
        name_group = getIntent().getExtras().getString("NAME_GROUP");
        Log.d("EXISTS", "finito di prenderli" );
        key_nameuser= new HashMap<>();


        TextView text_name =(TextView) findViewById(R.id.textGroup_activity_group_members);
        text_name.setText(name_group);





        if (savedInstanceState != null) {

        }

        //ButtonADD per aggiungere il gruppo
        ImageButton addGroupUser = (ImageButton)  findViewById(R.id.activity_group_members_addmember);

        addGroupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                EditText editTextUserMail = (EditText) findViewById(R.id.memberMail_activity_group_members);
                 userMail = editTextUserMail.getText().toString();

                if(!userMail.equals("")){





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

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Per tutti gli utenti aggiungerli
                            for(DataSnapshot users : dataSnapshot.getChildren()){

                                key_nameuser.remove(users.getKey());
                                key_nameuser.put(users.getKey(),users.child("Name").getValue(String.class));

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
                                    for(DataSnapshot user : snapshot.getChildren()){

                                        if(user.child("Email").getValue(String.class).equals(userMail)){
                                            //esiste
                                            exists=true;
                                            id_nuovoutentedaaggiungere = user.getKey();
                                            name_nuovoutentedaaggiungere = user.child("Name").getValue(String.class);
                                        }
                                    }//fine FOR

                                    Log.d("EXISTS", "usermail : "+userMail + " GROUPNAME : " + name_group +"  GROUPID : " +id_group );
                                    Log.d("EXISTS", "esiste : "+exists );


                                    if(exists==true){


                                        //controllare se già è stato aggiunto
                                        if(key_nameuser.containsKey(id_nuovoutentedaaggiungere)){

                                            Toast.makeText(getApplicationContext(),R.string.toast_addedusergroupAlreadyAdded,Toast.LENGTH_LONG).show();

                                        }
                                        else {
                                            addUserToGroup();
                                            Log.d("EXISTS", "Sono uscito dal metodo");

                                            Toast.makeText(getApplicationContext(),R.string.toast_addedusergroup,Toast.LENGTH_LONG).show();
                                            Log.d("EXISTS", "Fine toast");
                                        }
                                    }
                                    else{

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






                    return ;}

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                    return ;}


            }

        });


        Log.d("EXISTS", "Continuazione");


        //ButtonSubmit per finire il gruppo ed andare alla main activity
        ImageButton submitGroupUser = (ImageButton)  findViewById(R.id.buttonSubmit_activity_group_member);

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
                    startActivity(intent);



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







}
