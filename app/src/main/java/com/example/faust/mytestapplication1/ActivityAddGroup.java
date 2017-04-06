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

public class ActivityAddGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);



        Button submitGroup = (Button) findViewById(R.id.buttonSubmit_activity_group);

        submitGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                EditText editTextName = (EditText) findViewById(R.id.nameGroup_actitivty_expense);
                String name = editTextName.getText().toString();

                if (!name.equals("")) {
                    MyGroup mygroup = new MyGroup(name);


                    try {
                        // DBManager.addActivity(myactivity);

                        DB.setGroup(mygroup);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(ActivityAddGroup.this, ActivityAddUserToGroup.class);
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
