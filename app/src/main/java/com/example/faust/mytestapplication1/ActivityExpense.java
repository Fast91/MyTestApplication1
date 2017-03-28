package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityExpense extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);


        if (savedInstanceState != null) {

        }

        Button submitexpense = (Button)  findViewById(R.id.b5_SubmitExpense);

        submitexpense.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                EditText title= (EditText) findViewById(R.id.Title_newexpense);
                String mytitle = title.getText().toString();

                EditText amount= (EditText) findViewById(R.id.Total_newexpense);
                Double myamount = Double.parseDouble(amount.getText().toString());

                EditText group= (EditText) findViewById(R.id.Group_newexpense);
                String mygroup = group.getText().toString();

                EditText description= (EditText) findViewById(R.id.Description_newexpense);
                String mydescription = description.getText().toString();

                EditText data= (EditText) findViewById(R.id.Data_newexpense);
                Date mydata =null;

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date myDate;
                try {
                    myDate = df.parse(data.getText().toString());
                    String myText = myDate.getDate() + "-" + (myDate.getMonth() + 1) + "-" + (1900 + myDate.getYear());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                EditText category= (EditText) findViewById(R.id.Category_newexpense);
                String mycategory = category.getText().toString();


                MyActivity myactivity=new MyActivity(mytitle,R.drawable.giftboxred,myamount,  mydata , mycategory);


                try {
                    DBManager.addActivity(myactivity);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }



                Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                startActivity(intent);


                return true;
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









}
