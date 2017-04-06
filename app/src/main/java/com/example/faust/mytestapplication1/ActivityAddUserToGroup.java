package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAddUserToGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);




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
