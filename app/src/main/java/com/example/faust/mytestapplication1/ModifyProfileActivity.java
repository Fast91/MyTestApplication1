package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by robertospaziani on 02/04/17.
 */

public class ModifyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private EditText editextName , editextSurname;
    private Button buttonSave;
    private String id_user;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);


        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }


        //id_user = savedInstanceState.getString("ID_USER");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editextName = (EditText) findViewById(R.id.name_profile);
        editextSurname = (EditText) findViewById(R.id.surname_profile);

        buttonSave = (Button) findViewById(R.id.save_profile_button);
        buttonSave.setOnClickListener(this);







    }

    @Override
    public void onClick(View v) {

        if(v == buttonSave){
            saveUserInformation();
        }

    }


    private void saveUserInformation(){

        String name= editextName.getText().toString().trim();
        String surname= editextSurname.getText().toString().trim();

        UserInformation user = new UserInformation(name,surname);

        //Now we have to store and we use the uniq ID

        FirebaseUser fireuser = firebaseAuth.getCurrentUser();


        databaseReference.child("Users").child(fireuser.getUid()).setValue(user);
        //databaseReference.child(id_user).setValue(user);

        Toast.makeText(this,R.string.toast_mess_save_infouser,Toast.LENGTH_LONG).show();

        Intent intent=new Intent(ModifyProfileActivity.this,MainActivity.class);
        ModifyProfileActivity.this.startActivity(intent);
        finish();



    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.backprofile_title)
                .setMessage(R.string.backprofile_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(ModifyProfileActivity.this,ReadProfileActivity.class);
                        ModifyProfileActivity.this.startActivity(intent);
                        finish();

                    }
                }).create().show();
    }



}
