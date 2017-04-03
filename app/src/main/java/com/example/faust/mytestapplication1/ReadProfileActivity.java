package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private TextView textName , textSurname;
    private Button buttonModify;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_profile);


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

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

        /*
        if(databaseReference==null){

            Intent intent=new Intent(ReadProfileActivity.this,ModifyProfileActivity.class);
            ReadProfileActivity.this.startActivity(intent);
            finish();

        }
        */

        textName = (TextView) findViewById(R.id.name_real_profile);
        textSurname = (TextView) findViewById(R.id.surname_real_profile);


        buttonModify = (Button) findViewById(R.id.edit_profile_button);
        buttonModify.setOnClickListener(this);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInformation userInformation = ( UserInformation) dataSnapshot.getValue(UserInformation.class);

                /*
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                   UserInformation userInformation =  postSnapshot.getValue(UserInformation.class);
                    //adding artist to the list

                textName.setText(userInformation.name);
                textSurname.setText(userInformation.surname);
                }
                */

                if(userInformation==null) {
                    textName.setText(R.string.prompt_name_profile);
                    textSurname.setText(R.string.prompt_surname_profile);
                }
                else{
                    textName.setText(userInformation.Name);
                    textSurname.setText(userInformation.Surname);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    @Override
    public void onClick(View v) {

        if(v == buttonModify){
            Intent intent=new Intent(ReadProfileActivity.this,ModifyProfileActivity.class);
            ReadProfileActivity.this.startActivity(intent);
           finish();
        }

    }






    @Override
    public void onBackPressed() {

                        Intent intent=new Intent(ReadProfileActivity.this,MainActivity.class);
                        ReadProfileActivity.this.startActivity(intent);
                        finish();



    }

}
