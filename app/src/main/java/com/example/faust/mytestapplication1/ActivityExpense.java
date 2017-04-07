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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ActivityExpense extends AppCompatActivity {
    private EditText date;
    int year=Calendar.YEAR,month=Calendar.MONTH,day=Calendar.DAY_OF_MONTH;
    private String id_group;
    Double  bilancioGlobale, bilanciodelgruppo, bilanciosingolo;
    String id_owner=null;
    ArrayList<String> items_nomi_gruppi =new ArrayList<>();


    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }


        if (savedInstanceState != null) {

        }

        ImageButton submitexpense = (ImageButton)  findViewById(R.id.buttonSubmitExpense);



                submitexpense.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {



                EditText title= (EditText) findViewById(R.id.Title_newexpense);
                String mytitle = title.getText().toString();



                EditText amount= (EditText) findViewById(R.id.Total_newexpense);
                String stringamount = amount.getText().toString();
                Double myamount=null;
                if(!stringamount.equals("")) {
                     myamount = Double.parseDouble(stringamount);
                }



                Spinner group= (Spinner) findViewById(R.id.Group_newexpense);
                final String mygroup = group.getSelectedItem().toString();

                EditText description= (EditText) findViewById(R.id.Description_newexpense);
                String mydescription = description.getText().toString();


                Date mydata =null;

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date myDate=null;
                try {
                    myDate = df.parse(date.getText().toString());

                    String myText = myDate.getDate() + "-" + (myDate.getMonth() + 1) + "-" + (1900 + myDate.getYear());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                EditText category= (EditText) findViewById(R.id.Category_newexpense);
                String mycategory = category.getText().toString();

                if((!mytitle.equals(""))&&(!stringamount.equals(""))&&(!mygroup.equals(""))&&(!mydescription.equals(""))&&(!mycategory.equals(""))){
                  //  MyActivity myactivity=new MyActivity(mytitle,R.drawable.giftboxred,myamount,  mydata , mycategory);


                 //prendo id gruppo e prendo hashmap con chiave il nome(nickname dell'utente) e valore id utente
                    final String[] myid_group = new String[1];
                    final HashMap<String,String> myusers=new HashMap<String, String>();

                    /////////////
                    //// DATABASE
                    /////////////


                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                //Trovato identification ID del gruppo
                                if(postSnapshot.child("Name").getValue(String.class).equals(mygroup)){

                                    myid_group[0] =postSnapshot.getKey();

                                        //prendiamo tutti gli utenti -- name key --- id value
                                    for (DataSnapshot user : postSnapshot.child("Users").getChildren()) {

                                       myusers.put(user.child("Name").getValue(String.class),user.getKey());


                                    }




                                }



                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    //ora ho gli utenti e l'id del gruppo

                        databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                    //genero nuovo id per l'attività
                    String key = databaseReference.push().getKey();
                    String Name=mytitle;
                    Double Total=myamount;
                    String GroupId=myid_group[0];
                    String Description=mydescription;
                    String Category=mycategory;
                    String Date=myDate.toString();


                    databaseReference.child(key).child("Name").setValue(Name);
                    databaseReference.child(key).child("Total").setValue(Total);
                    databaseReference.child(key).child("GroupId").setValue(GroupId);
                    databaseReference.child(key).child("Description").setValue(Description);
                    databaseReference.child(key).child("Category").setValue(Category);
                    databaseReference.child(key).child("Date").setValue(Date);


                    int count_users=myusers.values().size();
                    //todo

                    // prendere il vero owner
                    //in questo caso prendiamo il primo che capita dalla lista
                    String first=null;
                    for(String in : myusers.keySet()){

                    first=in;
                         break;

                    }

                    Name=first;
                    Total=myamount/count_users;


                    //lo settiamo come owner
                    databaseReference.child(key).child("Owner").child(myusers.get(first)).child("Name").setValue(Name);
                    databaseReference.child(key).child("Owner").child(myusers.get(first)).child("Total").setValue(Total);


                    //// TODO: 05/04/2017  devo aggiungere tutti gli utenti
                    //preso tutti i nomi degli utenti
                    for(String in : myusers.keySet()){

                        if(!in.equals(first)){

                            Name=in;
                            //Setto la spesa a tutti gli utenti
                            databaseReference.child(key).child("Users").child(myusers.get(Name)).child("Name").setValue(Name);
                            databaseReference.child(key).child("Users").child(myusers.get(Name)).child("Total").setValue(Total);



                        }
                        else{
                            id_owner = myusers.get(in);
                        }




                    }


                    /*


                    //1 groups-ACTIVITIES
                    //2 users- ACTIVITIES x te e x tutti
                    //3 USERS - BILANCIO x te e x tutti
                    // 4 USERS - GROUPS  x te e x tutti


                    //1 groups-ACTIVITIES
                    // key
                    Name = mytitle;
                    Total=myamount;

                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
                    databaseReference.child(myid_group[0]).child("Activities").child(key).child("Total").setValue(Total);
                    databaseReference.child(myid_group[0]).child("Activities").child(key).child("Name").setValue(Name);



                    //2 users- ACTIVITIES

                    //users - id - activities - key- -----> Total -----> Name

                    Name = mytitle;
                    Total=myamount;
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    //da fare per tutti gli utenti


                    for(String id_user : myusers.values()){


                        databaseReference.child(id_user).child("Activities").child(key).child("Name").setValue(Name);
                        databaseReference.child(id_user).child("Activities").child(key).child("Total").setValue(Total);


                    }





                    //3 USERS - BILANCIO Globale x te e x tutti
                    //per l'owner deve ricevere +
                    //per gli altri devono dare -

                    Total= myamount/count_users; // per persona
                    //First sarebbe in questo momento chi paga owner





                    for(String name_user : myusers.keySet()){

                        //Prendermi il bilancio
                        ////////// INIZIO
                        ///////////////
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myusers.get(name_user)).child("GlobalBalance");



                        //Read content data
                        databaseReference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                 bilancioGlobale = (Double) dataSnapshot.getValue(Double.class);


                                if (bilancioGlobale == null) {
                                    bilancioGlobale = 0.0;
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                          ///////////// FINE BILANCIO GLOBALE





                        if(!name_user.equals(first)){
                            //devo levare


                            databaseReference.setValue(bilancioGlobale-Total);

                         }
                         else{
                            //sono chi ha pagato l'owner devo aggiungere

                            databaseReference.setValue(bilancioGlobale+Total);

                        }




                    }



                    ////////////////////////////////////////////
                    ////////////////////////////////////////////
                    // 4 USERS - GROUPS - per quel gruppo ID - Users   ---> Total ---> Name  ( x owner e x tutti )
                    ////////////////////////////////////////////
                    ////////////////////////////////////////////

                    Total= myamount/count_users; // per persona
                    //First sarebbe in questo momento chi paga owner





                    for(String name_user : myusers.keySet()){

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myusers.get(name_user)).child("Groups")
                                                .child(myid_group[0]);



                        //ABBIAMO AGGIORNATO IL BILANCIO DEL GRUPPO

                        //Read content data
                        databaseReference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                bilanciodelgruppo= (Double) dataSnapshot.child("Total").getValue(Double.class);


                                if (bilanciodelgruppo == null) {
                                    bilanciodelgruppo = 0.0;
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                        ///////////// FINE BILANCIO del gruppo




                        if(!name_user.equals(first)){
                            //devo levare

                            //aggiorno il bilancio del gruppo
                            databaseReference.child("Total").setValue(bilanciodelgruppo-Total);

                            //adesso devo modificare a chi devo i soldi


                            //step 1 prendere il totale per quella persona




                            //Read content data
                            databaseReference.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {





                                    bilanciosingolo= (Double) dataSnapshot.child("Users").child(id_owner).child("Total").getValue(Double.class);


                                    if (bilanciosingolo == null) {
                                        bilanciosingolo = 0.0;
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });


                            ///////////// FINE BILANCIO singolo




                            //step 2 aggiornalo

                            databaseReference.child("Users").child(id_owner).child("Total").setValue(bilanciosingolo-Total);

                            //DEVO FARE L'INVERSO
                            //DEVO SETTARE A ROBERTO L'OPPOSTO bilanciosingolo+Total
                             FirebaseDatabase.getInstance().getReference("Users").child(id_owner).child("Groups")
                                    .child(myid_group[0]).child("Users").child(myusers.get(name_user)).child("Total").setValue(bilanciosingolo+Total);



                        }
                        else{
                            //sono chi ha pagato l'owner devo aggiungere

                            //aggiorno il bilancio del gruppo
                            databaseReference.child("Total").setValue(bilanciodelgruppo+myamount-Total);


                            //adesso devo modificare da chi devo ricevere i soldi


                            //per tutti gli utenti a cui ho prestato soldi


                            //step 1 prendere il totale per quella persona


                            //step 2 aggiornalo


                        }




                    }













                    //////////
                    ///////////
                    //// FINE DB
                    //////////
                    //////////




                    */

                    Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                    startActivity(intent);



                    return ;}

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                    return ;}


            }

    });


        //Spinner
        //Spinner

        Spinner dropdown = (Spinner)findViewById(R.id.Group_newexpense);

        int i=0;


        ///////////////////////////////////////

        ////// DB
        ////////////////////////////////////////

        /////////////////
        /// Lista di gruppi per l'user autenticato con il rispettivo dovuto
        //////////////

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups");

        //Read content data

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //Prendo tutti i gruppi
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    items_nomi_gruppi.add(postSnapshot.child("Name").getValue(String.class));
                    //todo trovare metodo per salvare id


                }
            }



        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });




                ///////////////////////////////////////

                ////// FINE DB
                ////////////////////////////////////////












        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items_nomi_gruppi);
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
                .setTitle(R.string.backexpe_title)
                .setMessage(R.string.backexpe_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                        ActivityExpense.this.startActivity(intent);

                    }
                }).create().show();
    }





}
