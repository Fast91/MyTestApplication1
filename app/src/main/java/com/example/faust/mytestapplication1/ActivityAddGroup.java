package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityAddGroup extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private String id_group, name_group;
    private DatabaseReference databaseReference, databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        firebaseAuth = FirebaseAuth.getInstance();


        Button submitGroup = (Button) findViewById(R.id.buttonSubmit_activity_group);

        submitGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                EditText editTextName = (EditText) findViewById(R.id.nameGroup_actitivty_expense);
                name_group = editTextName.getText().toString();

                if (!name_group.equals("")) {





                    //////////////////
                    /////
                    ///// DB todo io metterei già il gruppo e come unico utente chi lo ha creato
                    /////
                    /////////////////


                    ///STEP 1
                    /// Group - ID - name - image - users

                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
                    //genero nuovo id per il gruppo
                   id_group = databaseReference.push().getKey();

                    //setto il nome
                    databaseReference.child(id_group).child("Name").setValue(name_group);







                    //setto l'utente loggato prima mi serve il suo NOME
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

                      //Read content data
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            String name_user = dataSnapshot.child("Name").getValue(String.class);

                            //setto il nome all'utente loggato
                            databaseReference.child(id_group).child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Name").setValue(name_user);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                    //// STEP 2
                    // USERS - ID loggato - GROUPS - ID - NAME - TOTAL 0

                    databaseReference2.child("Groups").child(id_group).child("Name").setValue(name_group);

                    Double tot=0.0;
                    databaseReference2.child("Groups").child(id_group).child("Total").setValue(tot);

                    //////////////////
                    /////
                    ///// FINE DB
                    /////
                    /////////////////

                    //step 0 mandare all'altra attività id group e name group


                    Intent intent = new Intent(ActivityAddGroup.this, ActivityAddUserToGroup.class);
                    intent.putExtra("ID_GROUP",id_group);
                    intent.putExtra("NAME_GROUP",name_group);
                    startActivity(intent);


                    return;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.toast_emptyaddexpense, Toast.LENGTH_LONG).show();


                    return;
                }


            }

        });







    }







    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.backgroup_title)
                .setMessage(R.string.backgroup_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(ActivityAddGroup.this,MainActivity.class);
                        ActivityAddGroup.this.startActivity(intent);

                    }
                }).create().show();
    }



}
