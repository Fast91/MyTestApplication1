package com.example.faust.mytestapplication1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityExpense extends AppCompatActivity {
    private EditText date;
    int year=Calendar.YEAR,month=Calendar.MONTH,day=Calendar.DAY_OF_MONTH;
    private String id_group;



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
                String stringamount = amount.getText().toString();
                Double myamount=null;
                if(!stringamount.equals("")) {
                     myamount = Double.parseDouble(stringamount);
                }



                Spinner group= (Spinner) findViewById(R.id.Group_newexpense);
                String mygroup = group.getSelectedItem().toString();

                EditText description= (EditText) findViewById(R.id.Description_newexpense);
                String mydescription = description.getText().toString();


                Date mydata =null;

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date myDate;
                try {
                    myDate = df.parse(date.getText().toString());

                    String myText = myDate.getDate() + "-" + (myDate.getMonth() + 1) + "-" + (1900 + myDate.getYear());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                EditText category= (EditText) findViewById(R.id.Category_newexpense);
                String mycategory = category.getText().toString();

                if((!mytitle.equals(""))&&(!stringamount.equals(""))&&(!mygroup.equals(""))&&(!mydescription.equals(""))&&(!mycategory.equals(""))){
                    MyActivity myactivity=new MyActivity(mytitle,R.drawable.giftboxred,myamount,  mydata , mycategory);


                    try {
                       // DBManager.addActivity(myactivity);
                        DB.setActivity(myactivity);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }



                    Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                    startActivity(intent);



                    return true;}

                else{
                    Toast.makeText(getApplicationContext(),"Some Fields are empty",Toast.LENGTH_LONG).show();


                    return true;}


            }
        });


        //Spinner
        //Spinner

        Spinner dropdown = (Spinner)findViewById(R.id.Group_newexpense);
        ArrayList<String> items =new ArrayList<>();
        int i=0;
        try {
           // for (MyGroup g : DBManager.getGroups()) {
            for (MyGroup g : DB.getmGroups()) {
                items.add(g.getName());
                i++;

            }

        }
        catch(Exception e){

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdown.setAdapter(adapter);

        //Listener

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                id_group = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(getApplicationContext(),
                        "Selected Group : " + id_group, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        //Date


        date= (EditText) findViewById(R.id.Data_newexpense);
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };

        date.addTextChangedListener(tw);



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
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to delete this expense?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                        ActivityExpense.this.startActivity(intent);

                    }
                }).create().show();
    }





}
