package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Omar on 04/04/2017.
 */

public class ActivityGroupUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);


        if (savedInstanceState != null) {

        }

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



                    Intent intent=new Intent(ActivityGroupUsers.this,MainActivity.class);
                    startActivity(intent);



                    return ;}

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                    return ;}


            }

        });

    }



    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onRestart(){
        super.onRestart();



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




    @Override
    public void onBackPressed() {
        /*
        new AlertDialog.Builder(this)
                .setTitle(R.string.backexpe_title)
                .setMessage(R.string.backexpe_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(ActivityGroupUsers.this,MainActivity.class);
                        ActivityGroupUsers.this.startActivity(intent);

                    }
                }).create().show();
                */
    }

}