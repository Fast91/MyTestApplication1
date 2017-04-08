package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ActivityAddUserToGroup extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String id_group, name_group;
    private DatabaseReference databaseReference, databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);

        firebaseAuth = FirebaseAuth.getInstance();
        id_group = getIntent().getExtras().getString("ID_GROUP");
        name_group = getIntent().getExtras().getString("NAME_GROUP");




        if (savedInstanceState != null) {

        }

        //ButtonADD per aggiungere il gruppo
        ImageButton addGroupUser = (ImageButton)  findViewById(R.id.activity_group_members_addmember);

        addGroupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                EditText editTextUserMail = (EditText) findViewById(R.id.memberMail_activity_group_members);
                String userMail = editTextUserMail.getText().toString();

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




                    //todo cambiare l'if
                    if(id_group.equals(id_group)){
                        //Se e' registrato lo aggiungo


                    }

                    else{
                        //Se non è registrato



                    }







                    //else chiedo se vuole invitarlo o no







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









}
