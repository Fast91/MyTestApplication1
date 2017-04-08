package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActivityAddUserToGroup extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String id_group, name_group;
    private DatabaseReference databaseReference, databaseReference2;
    private HashMap<String,String> key_nameuser;
    String userMail=null;
    Boolean exists=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);

        firebaseAuth = FirebaseAuth.getInstance();
        id_group = getIntent().getExtras().getString("ID_GROUP");
        name_group = getIntent().getExtras().getString("NAME_GROUP");
        key_nameuser= new HashMap<>();





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

                    //step 0 prendere gli utenti già registrati

                    //if esiste lo aggiungo
                    //step 1
                    // aggiunge al gruppo l'utente
                    // groups - idgruppo - users - idutente - name

                    //step 2
                    // aggiungere l'utente al gruppo
                    // users- id utente - groups - id group ---> namegroup + total 0
                    // users- id utente - groups - id group ---> users settare tutti gli utente già inseriti e tutto a 0 + name


                    //step 3
                    // aggiungere agli utenti già inseriti questo nuovo utente --> name + con total 0


                    //DATI UTILI --> Utenti già presenti hashmap key, name
                    //nome del gruppo e id del gruppo
                    // id_group e name_group



                    //step 0 prendere gli utenti già registrati
                    // GROUPS - ID - USERS

                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Per tutti gli utenti aggiungerli
                            for(DataSnapshot users : dataSnapshot.getChildren()){

                                key_nameuser.put(users.getKey(),users.child("Name").getValue(String.class));

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




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
                                }
                             }//fine FOR


                            if(exists==true){

                                addUserToGroup();
                            }
                            else{

                                popUpInvitation();

                            }






                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });






                    //////////////////
                    /////
                    ///// FINE DB
                    /////
                    /////////////////


                    Toast.makeText(getApplicationContext(),R.string.toast_addedusergroup,Toast.LENGTH_LONG).show();



                    return ;}

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                    return ;}


            }

        });



        //ButtonSubmit per finire il gruppo ed andare alla main activity
        ImageButton submitGroupUser = (ImageButton)  findViewById(R.id.buttonSubmit_activity_group_member);

        submitGroupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView textViewGroupName = (TextView) findViewById(R.id.textGroup_activity_group_members);
                String groupName = textViewGroupName.getText().toString();

                EditText editTextUserMail = (EditText) findViewById(R.id.memberMail_activity_group_members);
                String userMail = editTextUserMail.getText().toString();

                if(!groupName.equals("") && !userMail.equals("")){

                    MyGroup mygroup=new MyGroup(groupName);
                    User myuser = new User(userMail,0);

                    try {
                        mygroup.addUserinGroup(myuser);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }



                    Intent intent=new Intent(ActivityAddUserToGroup.this,MainActivity.class);
                    startActivity(intent);



                    return ;}

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                    return ;}


            }

        });




    }



    private void addUserToGroup(){

        //step 1
        // aggiunge al gruppo l'utente
        // groups - idgruppo - users - idutente - name

        //step 2
        // aggiungere l'utente al gruppo
        // users- id utente - groups - id group ---> namegroup + total 0
        // users- id utente - groups - id group ---> users settare tutti gli utente già inseriti e tutto a 0 + name


        //step 3
        // aggiungere agli utenti già inseriti questo nuovo utente --> name + con total 0


        //DATI UTILI --> Utenti già presenti hashmap key, name
        //nome del gruppo e id del gruppo
        // id_group e name_group
        //userMail
        //key_nameuser hashmap





    }


    private void popUpInvitation(){


        new AlertDialog.Builder(getApplicationContext())
                .setTitle(R.string.user_not_registered_title)
                .setMessage(R.string.user_not_registered_msg)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Toast.makeText(getApplicationContext(),"INVITATO",Toast.LENGTH_SHORT).show();

                                        /*
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        //intent.putExtra("ID_USER",firebaseAuth.getCurrentUser().getUid());

                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();*/

                    }
                }).create().show();




    }









}
