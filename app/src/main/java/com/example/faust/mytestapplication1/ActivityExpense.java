package com.example.faust.mytestapplication1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.util.Log;
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
import java.util.List;

public class ActivityExpense extends AppCompatActivity {
    //private Date mydata;
    private Date myDate;
    private String mytitle;
    private Double myamount;
    private NomeDovuto mygroup_selected;
    private String mycurrency_selected;

    private String mycategory;
    private String  keyowner;


    private EditText date;
    int year=Calendar.YEAR,month=Calendar.MONTH,day=Calendar.DAY_OF_MONTH;
    private String id_group;
    private String id_currency;
    Double  bilancioGlobale, bilanciodelgruppo, bilanciosingolo;
    String id_owner=null;
    ArrayList<NomeDovuto> items_nomi_gruppi =new ArrayList<>();
    List<String> items_nomi_valute =new ArrayList<>();

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4, databaseReference5, databaseReference6, databaseReference7;


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
                mytitle = title.getText().toString();



                EditText amount= (EditText) findViewById(R.id.Total_newexpense);
                String stringamount = amount.getText().toString();
                myamount=null;
                if(!stringamount.equals("")) {
                     myamount = Double.parseDouble(stringamount);
                }



                Spinner group= (Spinner) findViewById(R.id.Group_newexpense);
                mygroup_selected = (NomeDovuto) group.getSelectedItem();

                Spinner curr= (Spinner) findViewById(R.id.new_expense_currency_spinner);
                mycurrency_selected = (String) curr.getSelectedItem();



                //mydata =null;

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                myDate=null;
                try {
                    myDate = df.parse(date.getText().toString());

                    String myText = myDate.getDay() + "/" + (myDate.getMonth() + 1) + "/" + (1900 + myDate.getYear());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                EditText category= (EditText) findViewById(R.id.Category_newexpense);
                mycategory = category.getText().toString();

                if((!mytitle.equals(""))&&(!stringamount.equals(""))&&(!mygroup_selected.getName().equals("Select Group"))&&(!mycategory.equals(""))){
                  //  MyActivity myactivity=new MyActivity(mytitle,R.drawable.giftboxred,myamount,  mydata , mycategory);


                 //prendo id gruppo e prendo hashmap con chiave il nome(nickname dell'utente) e valore id utente

                    final HashMap<String,String> myusers=new HashMap<String, String>();

                    /////////////
                    //// DATABASE
                    /////////////


                    databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups").child(mygroup_selected.getId()).child("Users");

                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                                       myusers.put(postSnapshot.getKey(),postSnapshot.child("Name").getValue(String.class));
                                        //Toast.makeText(getApplicationContext(),postSnapshot.getKey()+" "+ postSnapshot.child("Name").getValue(String.class), Toast.LENGTH_SHORT);
                                        Log.d("EXPENSE", postSnapshot.getKey()+" "+ postSnapshot.child("Name").getValue(String.class));

                                    }

//ora ho gli utenti e l'id del gruppo

                            databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                            //genero nuovo id per l'attività
                            String key = databaseReference.push().getKey();
                            String Name=mytitle;
                            Double Total=myamount;
                            final Double Amount=myamount;
                            String GroupId=mygroup_selected.getId();

                            String Category=mycategory;
                            String Date=""+myDate.getDate()+"/"+myDate.getMonth()+"/"+(myDate.getYear()+1900);


                            databaseReference.child(key).child("Name").setValue(Name);
                            databaseReference.child(key).child("Total").setValue(Total);

                            databaseReference.child(key).child("GroupId").setValue(GroupId);

                            databaseReference.child(key).child("Category").setValue(Category);
                            databaseReference.child(key).child("Date").setValue(Date);

                            int count_users=myusers.size();
                            //todo

                            // prendere il vero owner
                            //in questo caso prendiamo il primo che capita dalla lista
                             keyowner= firebaseAuth.getCurrentUser().getUid();

                            Name=myusers.get(keyowner);
                            Total=myamount/count_users;


                            //lo settiamo come owner
                            databaseReference.child(key).child("Owner").child(keyowner).child("Name").setValue(Name);
                            databaseReference.child(key).child("Owner").child(keyowner).child("Total").setValue(Total);
                            Log.d("EXPENSE", Name + " " + Total + " (DB)" );

                            //preso tutti i nomi degli utenti
                            for(String in : myusers.keySet()){

                                if(!in.equals(keyowner)){

                                    Name=in;
                                    //Setto la spesa a tutti gli utenti
                                    databaseReference.child(key).child("Users").child(in).child("Name").setValue(myusers.get(in));
                                    databaseReference.child(key).child("Users").child(in).child("Total").setValue(Total);



                                }







                    //1 groups-ACTIVITIES
                    //2 users- ACTIVITIES x te e x tutti
                    //3 USERS - BILANCIO x te e x tutti
                    // 4 USERS - GROUPS  x te e x tutti


                    //1 groups-ACTIVITIES
                    // key
                    Name = mytitle;
                    Total=myamount;

                    databaseReference3 = FirebaseDatabase.getInstance().getReference("Groups").child(GroupId).child("Activities");
                    databaseReference3.child(key).child("Total").setValue(Total);
                    databaseReference3.child(key).child("Name").setValue(Name);
                    databaseReference3.child(key).child("Category").setValue(Category);


                    //2 users- ACTIVITIES

                    //users - id - activities - key- -----> Total -----> Name

                    Name = mytitle;
                    Total=myamount;
                    databaseReference4 = FirebaseDatabase.getInstance().getReference("Users");
                    //da fare per tutti gli utenti


                    for(String id_user : myusers.keySet()){


                        databaseReference4.child(id_user).child("Activities").child(key).child("Name").setValue(Name);
                        databaseReference4.child(id_user).child("Activities").child(key).child("Total").setValue(Total);
                        databaseReference4.child(id_user).child("Activities").child(key).child("Category").setValue(Category);

                    }



                    //3 USERS - BILANCIO Globale x te e x tutti
                    //per l'owner deve ricevere +
                    //per gli altri devono dare -

                    final Double Total2= myamount/count_users; // per persona
                    //First sarebbe in questo momento chi paga owner





                    for(final String  name_user : myusers.keySet()){

                        //Prendermi il bilancio
                        ////////// INIZIO
                        ///////////////
                        databaseReference5 = FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("GlobalBalance");



                        //Read content data
                        databaseReference5.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                 bilancioGlobale = (Double) dataSnapshot.getValue(Double.class);


                                if (bilancioGlobale == null) {
                                    bilancioGlobale = 0.0;
                                }
                                Log.d("EXPENSE", "bilancioGlobale: " + bilancioGlobale);
                                Log.d("EXPENSE", "Id: " + name_user);

                                if(!name_user.equals(keyowner)){
                                    //devo levare
                                    Double tmp= bilancioGlobale-Total2; //todo sbagliato
                                  FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("GlobalBalance").setValue(tmp);

                                    //databaseReference5.removeEventListener(this);
                                    Log.d("EXPENSE", "bilancioGlobale-Total2: " + (tmp));
                                }
                                else{
                                    //sono chi ha pagato l'owner devo aggiungere
                                    Double tmp= bilancioGlobale+(Amount-Total2); //todo sbagliato
                                    FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("GlobalBalance").setValue(tmp);

                                    //databaseReference5.removeEventListener(this);
                                    Log.d("EXPENSE", "bilancioGlobale+Total2: " + (tmp));
                                }
                                databaseReference5.removeEventListener(this);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                          ///////////// FINE BILANCIO GLOBALE










                    }



                    ////////////////////////////////////////////
                    ////////////////////////////////////////////
                    // 4 USERS - GROUPS - per quel gruppo ID - Users   ---> Total ---> Name  ( x owner e x tutti )
                    ////////////////////////////////////////////
                    ////////////////////////////////////////////

                    Total= myamount/count_users; // per persona
                    //First sarebbe in questo momento chi paga owner





                    for(final String name_user : myusers.keySet()){

                        databaseReference6 = FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups")
                                                .child(mygroup_selected.getId());



                        //ABBIAMO AGGIORNATO IL BILANCIO DEL GRUPPO

                        //Read  il bilancio del gruppo
                        databaseReference6.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                bilanciodelgruppo= (Double) dataSnapshot.child("Total").getValue(Double.class);


                                if (bilanciodelgruppo == null) {
                                    bilanciodelgruppo = 0.0;
                                }



                                if(!name_user.equals(keyowner)){
                                    //devo levare

                                    //aggiorno il bilancio del gruppo
                                    Double tmp= bilanciodelgruppo-Total2;//todo sbagliato
                                    FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId()).child("Total").setValue(tmp);

                                    //databaseReference6.child("Total").setValue((bilanciodelgruppo-Total2));

                                    //adesso devo modificare a chi devo i soldi




                                }
                                else{
                                    //sono chi ha pagato l'owner devo aggiungere

                                    //aggiorno il bilancio del gruppo
                                    Double tmp= bilanciodelgruppo-Total2+myamount;//todo sbagliato
                                    FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId()).child("Total").setValue(tmp);

                                  //  databaseReference6.child("Total").setValue(bilanciodelgruppo+myamount-Total);


                                    //adesso devo modificare da chi devo ricevere i soldi


                                    //per tutti gli utenti a cui ho prestato soldi


                                    //step 1 prendere il totale per quella persona


                                    //step 2 aggiornalo


                                }











                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                        ///////////// FINE BILANCIO del gruppo






                    }//fine for






                        ////// punto 5 aggiornare i singoli bilanci all'interno del gruppo
                                // per quelle persone convolte



                                id_owner=keyowner;
                                for(final String name_user : myusers.keySet()) {

                                    //step 1 prendere il totale per quella persona

                                    Log.d("SINGOLO"," inizio il primo id : "+ name_user);

                                    if(!name_user.equals(id_owner)) {

                                        Log.d("SINGOLO"," io sono : "+ name_user+" "+" quindi non sono owner "+id_owner);
                                        //Read content i dati del singolo utente
                                        databaseReference7 = FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups")
                                                .child(mygroup_selected.getId()).child("Users");
                                        databaseReference7.addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                for (DataSnapshot persone : dataSnapshot.getChildren()) {

                                                    Log.d("SINGOLO", "io sono   " + name_user + " trovo come amico : " + persone.getKey());

                                                }


                                                Log.d("SINGOLO", " Provo a prendere il bilancio per quella persona: ");


                                                bilanciosingolo = (Double) dataSnapshot.child(id_owner).child("Total").getValue(Double.class);


                                                if (bilanciosingolo == null) {
                                                    bilanciosingolo = 0.0;
                                                }


                                                Log.d("SINGOLO", " bilancio singolo : " + bilanciosingolo);

                                                //step 2 aggiornalo
                                                Double tmp = bilanciosingolo - Total2;//todo sbagliato
                                                FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId())
                                                        .child("Users").child(id_owner).child("Total").setValue(tmp);


                                                Log.d("SINGOLO", " bilancio aggiornato : " + tmp);


                                                //databaseReference6.child("Users").child(id_owner).child("Total").setValue(bilanciosingolo-Total);

                                                //DEVO FARE L'INVERSO
                                                //DEVO SETTARE A ROBERTO L'OPPOSTO bilanciosingolo+Total

                                                tmp = -tmp; //todo sbagliato
                                                FirebaseDatabase.getInstance().getReference("Users").child(id_owner).child("Groups")
                                                        .child(mygroup_selected.getId()).child("Users").child(name_user).child("Total").setValue(tmp);


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }

                                        });


                                        ///////////// FINE BILANCIO singolo


                                    }//fine if

                                }//fine for












                    //////////
                    ///////////
                    //// FINE DB
                    //////////
                    //////////








                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                    startActivity(intent);
                    finish();


                    return ;}

                else{
                    Toast.makeText(getApplicationContext(),R.string.toast_emptyaddexpense,Toast.LENGTH_LONG).show();



                    return ;}


            }

    });


        //Spinner
        //Spinner

        Spinner dropdown = (Spinner)findViewById(R.id.Group_newexpense);

        Spinner dropdownC = (Spinner)findViewById(R.id.new_expense_currency_spinner);

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
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {

                    String name= postSnapshot.child("Name").getValue(String.class);
                    items_nomi_gruppi.add(new NomeDovuto(postSnapshot.getKey(), name));
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






        items_nomi_gruppi.add(new NomeDovuto("0","Select Group"));

        List<String> curry = CurrencyEditor.getCurrencySymbols();
        for(String c : curry)
        {
            //TODO permettere solo certe valute
            if(c.startsWith("U") || c.startsWith("E"))
            items_nomi_valute.add(c);
        }

        //items_nomi_valute.add("Select Currency");

        //items_nomi_gruppi.add("Group1");

        ArrayAdapter<NomeDovuto> adapter = new ArrayAdapter<NomeDovuto>(this, android.R.layout.simple_spinner_item,items_nomi_gruppi);
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

        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items_nomi_valute);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownC.setAdapter(adapterC);

        //Listener

        dropdownC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterC, View v,
                                       int position, long id) {
                // On selecting a spinner item
                id_currency = adapterC.getItemAtPosition(position).toString();
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
                        finish();

                    }
                }).create().show();
    }





}
