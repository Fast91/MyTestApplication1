package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Omar on 04/04/2017.
 */

public class ActivityGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        if (savedInstanceState != null) {

        }

        ImageButton submitGroup = (ImageButton)  findViewById(R.id.buttonSubmit_activity_group);

        submitGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText editTextName= (EditText) findViewById(R.id.nameGroup_actitivty_expense);
                String name = editTextName.getText().toString();

                if(!name.equals("")){
                    MyGroup mygroup=new MyGroup(name);


                    try {
                        // DBManager.addActivity(myactivity);

                        DB.setGroup(mygroup);


                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }



                    Intent intent=new Intent(ActivityGroup.this,MainActivity.class);
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
        new AlertDialog.Builder(this)
                .setTitle(R.string.backgroup_title)
                .setMessage(R.string.backgroup_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(ActivityGroup.this,MainActivity.class);
                        ActivityGroup.this.startActivity(intent);

                    }
                }).create().show();
    }

}
