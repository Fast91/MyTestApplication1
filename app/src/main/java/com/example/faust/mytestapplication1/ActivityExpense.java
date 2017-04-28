package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class ActivityExpense extends AppCompatActivity implements View.OnClickListener{
    //private Date mydata;
    private Date myDate;
    private String mytitle;
    private Double myamount;
    private NomeDovuto mygroup_selected;
    private CurrencyDetail mycurrency_selected_from_spinner;
    private String mycurrency_selected = "EUR"; // IMPOSTO SEMPRE EURO AL MOMENTO, PER ORA NON SI PUò CAMBIARE SENNò SOTTO SBALLA TUTTO

    private Double Total2;
    Spinner dropdownC;
    Spinner category;
    private String[] listato_id;

    private boolean premuto_diviso=false;

    private EditText[] amountBox2;
    private Double[] amountBox3;

    Double my_amount=0.0;

    ProgressDialog mProgressDialog ;
    private Uri downloadUri;
    private String key=null;

    private String mycategory;
    private String  keyowner;
    private ImageButton buttonGallery, buttonCamera, buttonDelete;
    private static final int GALLERY_INTENT = 2, CAMERA_REQUEST_CODE = 1;
    private ImageView image_activity;
    Integer utente_selezionato=-1;

    private StorageReference mStorage;

    private EditText date;
    int year=Calendar.YEAR,month=Calendar.MONTH,day=Calendar.DAY_OF_MONTH;
    private NomeDovuto id_group;
    private String id_currency;
    Double  bilancioGlobale, bilanciodelgruppo, bilanciosingolo;
    String currencyBilancioGlobale, currencyBilancioDelGruppo, currencyBilancioSingolo;
    String id_owner=null;
    ArrayList<NomeDovuto> items_nomi_gruppi =new ArrayList<>();
    List<CurrencyDetail> items_nomi_valute =new ArrayList<>();

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4, databaseReference5, databaseReference6, databaseReference7;

    private   HashMap<String,NomeDovuto> utenti_gruppo =new HashMap<>();
    private   HashMap<String,NomeDovuto> utenti_gruppo2 =new HashMap<>();


    Button button_pagatoda;
    Button button_divide;

    private String[] listItems, list_id;
    private boolean[] checkedItems;

    private NomeDovuto utente_pagante;
    private HashMap<String,NomeDovuto> utentiGruppo_conDovuto=new HashMap<>();


    private String id_group_iniziale , name_group_iniziale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        Intent inte = getIntent();
        id_group_iniziale = inte.getExtras().getString("GROUP_ID");
        name_group_iniziale = inte.getExtras().getString("GROUP_NAME");




        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }


        if (savedInstanceState != null) {

        }


        category= (Spinner) findViewById(R.id.Category_newexpense);

        buttonDelete= (ImageButton)  findViewById(R.id.buttonDeleteImage);

        buttonDelete.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.delete480x385, 100, 100));

        ImageButton submitexpense = (ImageButton)  findViewById(R.id.buttonSubmitExpense);



        submitexpense.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.tick_icon256x256, 100, 100));

         buttonCamera= (ImageButton)  findViewById(R.id.buttonPhoto);

        buttonCamera.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.icon_camera128x128, 100, 100));

        buttonCamera.setOnClickListener(this);
         buttonGallery = (ImageButton)  findViewById(R.id.buttonGallery);
        buttonGallery.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.picture_attachment256x256, 100, 100));

        buttonGallery.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance().getReference();

        image_activity = (ImageView) findViewById(R.id.imagePicture);
        image_activity.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.gallery314x250, 100, 100));


        mProgressDialog = new ProgressDialog(this);


        button_pagatoda = (Button) findViewById(R.id.button_pagatoda_expense);

        button_divide = (Button) findViewById(R.id.button_diviso_expense);




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
                        mycurrency_selected_from_spinner = (CurrencyDetail) curr.getSelectedItem();
                        try {
                            myamount = CurrencyEditor.convertCurrency(myamount, mycurrency_selected_from_spinner.getSymbol(), "EUR");
                        } catch (IOException e) {
                            //e.printStackTrace();
                            //lascio myamount così com'è
                        }

                        //mydata =null;

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                myDate=null;
                try {
                    myDate = df.parse(date.getText().toString());

                    String myText = myDate.getDay() + "/" + (myDate.getMonth() + 1) + "/" + (1900 + myDate.getYear());

                } catch (Exception e) {
                    e.printStackTrace();
                }


                mycategory = (String ) category.getSelectedItem();

                if((!mytitle.equals(""))&&(!stringamount.equals(""))&&(!mygroup_selected.getName().equals("Select Group"))&&(!mycategory.equals(""))){
                  //  MyActivity myactivity=new MyActivity(mytitle,R.drawable.giftboxred,myamount,  mydata , mycategory);


                 //prendo id gruppo e prendo hashmap con chiave il nome(nickname dell'utente) e valore id utente

                    divideOrSplit();

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

                            if(key==null){
                                key = databaseReference.push().getKey();
                            }

                            String Name=mytitle;
                            Double Total=myamount;
                            final Double Amount=myamount;
                            String GroupId=mygroup_selected.getId();

                            String Category=mycategory;
                            String Date=""+myDate.getDate()+"/"+myDate.getMonth()+"/"+(myDate.getYear()+1900);


                            databaseReference.child(key).child("Name").setValue(Name);
                            databaseReference.child(key).child("Total").setValue(Total);
                            databaseReference.child(key).child("Currency").setValue(mycurrency_selected);

                            databaseReference.child(key).child("GroupId").setValue(GroupId);

                            databaseReference.child(key).child("Category").setValue(Category);
                            databaseReference.child(key).child("Date").setValue(Date);

                            int count_users=myusers.size();
                            //todo

                            // prendere il vero owner
                            //in questo caso prendiamo il primo che capita dalla lista
                             //keyowner= firebaseAuth.getCurrentUser().getUid();
                            keyowner = utente_pagante.getId();

                            Name=myusers.get(keyowner);
                            Total=myamount/count_users;

                            //Ricerca di quanto ha pagato l'owner

                            for(int i=0;i<listato_id.length;i++){

                                if(listato_id[i].equals(keyowner)){

                                    Total=(amountBox3[i]);
                                }


                            }


                            //lo settiamo come owner
                            databaseReference.child(key).child("Owner").child(keyowner).child("Name").setValue(Name);
                            databaseReference.child(key).child("Owner").child(keyowner).child("Total").setValue(Total);
                            databaseReference.child(key).child("Owner").child(keyowner).child("Currency").setValue(mycurrency_selected);
                            Log.d("EXPENSE", Name + " " + Total + " (DB)" );

                            //preso tutti i nomi degli utenti
                            for(String in : myusers.keySet()){

                                if(!in.equals(keyowner)){

                                    //Ricerca di quanto ha pagato l'owner

                                    for(int i=0;i<listato_id.length;i++){

                                        if(listato_id[i].equals(in)){

                                            Total=amountBox3[i];
                                        }


                                    }

                                    Name=in;
                                    //Setto la spesa a tutti gli utenti
                                    databaseReference.child(key).child("Users").child(in).child("Name").setValue(myusers.get(in));
                                    databaseReference.child(key).child("Users").child(in).child("Total").setValue(Total);

                                    databaseReference.child(key).child("Users").child(in).child("Currency").setValue(mycurrency_selected);



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
                    databaseReference3.child(key).child("Currency").setValue(mycurrency_selected);
                    databaseReference3.child(key).child("Name").setValue(Name);
                    databaseReference3.child(key).child("Date").setValue(Date);
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
                        databaseReference4.child(id_user).child("Activities").child(key).child("Currency").setValue(mycurrency_selected);
                        databaseReference4.child(id_user).child("Activities").child(key).child("Category").setValue(Category);

                        databaseReference4.child(id_user).child("Activities").child(key).child("Date").setValue(Date);
                        databaseReference4.child(id_user).child("Activities").child(key).child("Group").setValue(mygroup_selected.getName());


                    }



                    //3 USERS - BILANCIO Globale x te e x tutti
                    //per l'owner deve ricevere +
                    //per gli altri devono dare -

                     Total2= myamount/count_users; // per persona
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
                                currencyBilancioGlobale = (String) dataSnapshot.child("Currency").getValue(String.class);

                                if (bilancioGlobale == null) {
                                    bilancioGlobale = 0.0;
                                }
                                Log.d("EXPENSE", "bilancioGlobale: " + bilancioGlobale);
                                Log.d("EXPENSE", "Id: " + name_user);

                                if(!name_user.equals(keyowner)){
                                    //devo levare
                                    //Ricerca di quanto ha pagato l'owner

                                    for(int i=0;i<listato_id.length;i++){

                                        if(listato_id[i].equals(name_user)){

                                            Total2=amountBox3[i];
                                        }


                                    }



                                    Double tmp= bilancioGlobale-Total2; //todo sbagliato
                                  FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("GlobalBalance").setValue(tmp);

                                    //databaseReference5.removeEventListener(this);
                                    Log.d("EXPENSE", "bilancioGlobale-Total2: " + (tmp));
                                }
                                else{
                                    //sono chi ha pagato l'owner devo aggiungere

                                    //Ricerca di quanto ha pagato l'owner

                                    for(int i=0;i<listato_id.length;i++){

                                        if(listato_id[i].equals(name_user)){

                                            Total2=amountBox3[i];
                                        }


                                    }


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
                                currencyBilancioDelGruppo = (String) dataSnapshot.child("Currency").getValue(String.class);


                                if (bilanciodelgruppo == null) {
                                    bilanciodelgruppo = 0.0;
                                }



                                if(!name_user.equals(keyowner)){
                                    //devo levare

                                    //Ricerca di quanto ha pagato l'owner

                                    for(int i=0;i<listato_id.length;i++){

                                        if(listato_id[i].equals(name_user)){

                                            Total2=amountBox3[i];
                                        }


                                    }

                                    //aggiorno il bilancio del gruppo
                                    Double tmp= bilanciodelgruppo-Total2;//todo sbagliato
                                    FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId()).child("Total").setValue(tmp);
                                    // TODO finchè aggiorno il bilancio in questo modo non potrò mai salvare le expenses con la loro moneta originale ma solo in euro

                                    //databaseReference6.child("Total").setValue((bilanciodelgruppo-Total2));

                                    //adesso devo modificare a chi devo i soldi




                                }
                                else{
                                    //sono chi ha pagato l'owner devo aggiungere

                                    //Ricerca di quanto ha pagato l'owner

                                    for(int i=0;i<listato_id.length;i++){

                                        if(listato_id[i].equals(name_user)){

                                            Total2=amountBox3[i];
                                        }


                                    }

                                    //aggiorno il bilancio del gruppo
                                    Double tmp= bilanciodelgruppo-Total2+myamount;//todo sbagliato
                                    FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId()).child("Total").setValue(tmp);
                                    // TODO finchè aggiorno il bilancio in questo modo non potrò mai salvare le expenses con la loro moneta originale ma solo in euro

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
                                                currencyBilancioSingolo = (String) dataSnapshot.child(id_owner).child("Currency").getValue(String.class);

                                                if (bilanciosingolo == null) {
                                                    bilanciosingolo = 0.0;
                                                }

                                                //Ricerca di quanto ha pagato l'owner

                                                for(int i=0;i<listato_id.length;i++){

                                                    if(listato_id[i].equals(name_user)){

                                                        Total2=amountBox3[i];
                                                    }


                                                }


                                                Log.d("SINGOLO", " bilancio singolo : " + bilanciosingolo);

                                                //step 2 aggiornalo
                                                Double tmp = bilanciosingolo - Total2;//todo sbagliato
                                                FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId())
                                                        .child("Users").child(id_owner).child("Total").setValue(tmp);
                                                FirebaseDatabase.getInstance().getReference("Users").child(name_user).child("Groups").child(mygroup_selected.getId())
                                                        .child("Users").child(id_owner).child("Currency").setValue(mycurrency_selected);


                                                Log.d("SINGOLO", " bilancio aggiornato : " + tmp);


                                                //databaseReference6.child("Users").child(id_owner).child("Total").setValue(bilanciosingolo-Total);

                                                //DEVO FARE L'INVERSO
                                                //DEVO SETTARE A ROBERTO L'OPPOSTO bilanciosingolo+Total

                                                tmp = -tmp; //todo sbagliato
                                                FirebaseDatabase.getInstance().getReference("Users").child(id_owner).child("Groups")
                                                        .child(mygroup_selected.getId()).child("Users").child(name_user).child("Total").setValue(tmp);
                                                FirebaseDatabase.getInstance().getReference("Users").child(id_owner).child("Groups")
                                                        .child(mygroup_selected.getId()).child("Users").child(name_user).child("Currency").setValue(mycurrency_selected);


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
                    intent.putExtra("GROUP_ID",id_group_iniziale);
                    intent.putExtra("GROUP_NAME",name_group_iniziale);
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

        dropdownC = (Spinner)findViewById(R.id.new_expense_currency_spinner);

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

        //List<String> curry = CurrencyEditor.getCurrencySymbols();
        List<CurrencyDetail> currDet = CurrencyEditor.getCurrencyDetails();
        for(CurrencyDetail cD : currDet)
        {
            //TODO permettere solo certe valute
            if(cD.getSymbol().equals("EUR") || cD.getSymbol().equals("USD"))
            items_nomi_valute.add(cD);
        }


       // items_nomi_valute.add(new CurrencyDetail());

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
                id_group = (NomeDovuto) adapter.getItemAtPosition(position);

                // Showing selected spinner item


                //carica gli utenti e mette le cose per i nuovi bottoni

                    cercareUtentiDelGruppo(id_group,position);


                    chooseButton(id_group,position);



            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });



        ArrayAdapter<CurrencyDetail> adapterC = new ArrayAdapter<CurrencyDetail>(this, android.R.layout.simple_spinner_item,items_nomi_valute);
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
                //prova
                dropdownC.setSelection(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //Spiinner Category


        Categories cts = new Categories();
        //AGGIUNGO LE CATEGORIE
        String s = getString(R.string.category_generale);
        cts.setItem(s);
        s = getString(R.string.category_luce);
        cts.setItem(s);
        s = getString(R.string.category_gas);
        cts.setItem(s);
        s = getString(R.string.category_internet);
        cts.setItem(s);

        s = getString(R.string.category_cibo);
        cts.setItem(s);
        s = getString(R.string.category_regali);
        cts.setItem(s);







        ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,cts.getList());
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapterCat);

        //Listener

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterC, View v,
                                       int position, long id) {
                // On selecting a spinner item
                //id_currency = adapterC.getItemAtPosition(position).toString();
                // Showing selected spinner item
                //prova
                category.setSelection(position);

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

                        //Remove activity
                        if(key!=null) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("Activities").child(key).removeValue();
                        }


                        Intent intent=new Intent(ActivityExpense.this,MainActivity.class);
                        ActivityExpense.this.startActivity(intent);
                        finish();

                    }
                }).create().show();
    }



    @Override
    public void onClick(View v) {

        if(v == buttonGallery){


            Intent intent = new Intent ( Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent,GALLERY_INTENT);



        }


        else if (v == buttonCamera){

            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);


            startActivityForResult(intent,CAMERA_REQUEST_CODE);


        }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){


            String msg = getString(R.string.dialog_image_profile);
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();


            Uri uri = data.getData();


            StorageReference filepath  = mStorage.child("Photos").child(uri.getLastPathSegment());





            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUri = taskSnapshot.getDownloadUrl();

                    if(key==null){
                        databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                        key = databaseReference.push().getKey();
                    }


                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Activities")
                            .child(key)
                            .child("Image");
                    ref.setValue(downloadUri.toString());



                    // Picasso.with(ReadProfileActivity.this).load(downloadUri).fit().centerCrop().into(image_profile);

                    Toast.makeText(ActivityExpense.this, R.string.upload_ok ,Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();



                    if (!downloadUri.toString().contains("http")) {
                        try {
                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(downloadUri.toString());
                            image_activity.setImageBitmap(imageBitmaptaken);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // This block of code should already exist, we're just moving it to the 'else' statement:
                        Picasso.with(ActivityExpense.this)
                                .load(downloadUri.toString())
                                .fit()
                                .centerCrop()
                                .into(image_activity);
                    }



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ActivityExpense.this, R.string.upload_no ,Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();



                }
            });







        }


        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //image_profile.setImageBitmap(imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            if(key==null){
                databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                key = databaseReference.push().getKey();
            }

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Activities")
                    .child(key)
                    .child("Image");
            ref.setValue(imageEncoded);


            if (!imageEncoded.contains("http")) {
                try {
                    Bitmap imageBitmaptaken = decodeFromFirebaseBase64(imageEncoded);
                    image_activity.setImageBitmap(imageBitmaptaken);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // This block of code should already exist, we're just moving it to the 'else' statement:
                Picasso.with(this)
                        .load(imageEncoded)
                        .fit()
                        .centerCrop()
                        .into(image_activity);
            }




        }







    }


    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }




    private void cercareUtentiDelGruppo(NomeDovuto nm, int pos){

        if(pos==0) {

            button_pagatoda.setText("");


            button_pagatoda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }

            });
        }
        else {


            DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Groups").child(nm.getId()).child("Users");

            databaseReference10.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    utenti_gruppo.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                        NomeDovuto nm = new NomeDovuto(postSnapshot.getKey(), postSnapshot.child("Name").getValue(String.class));

                        utenti_gruppo.put(nm.getId(), nm);

                    }


                    //voglio mettere nel mio bottone il mio NOME
                    button_pagatoda.setText(utenti_gruppo.get(firebaseAuth.getCurrentUser().getUid().toString()).getName());
                    utente_pagante = utenti_gruppo.get(firebaseAuth.getCurrentUser().getUid().toString());

                    //quando il bottone viene premuto
                    // dobbiamo mostrare una lista di untenti e selezionare chi paga la spesa

                    button_pagatoda.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ///devo mostrare un dialogo con tutti gli utenti


                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityExpense.this);
                            mBuilder.setTitle(R.string.text_pagatoda_expense_string);

                            listItems = new String[utenti_gruppo.size()];
                            list_id =new String[utenti_gruppo.size()];
                            checkedItems = new boolean[utenti_gruppo.size()];

                            int i = 0;
                            for (NomeDovuto n : utenti_gruppo.values()) {

                                listItems[i] = n.getName();
                                list_id[i] = n.getId();
                                checkedItems[i] = false;
                                i++;
                            }

                            checkedItems[0] = true;

                            utente_selezionato = 0;


                            mBuilder.setSingleChoiceItems(listItems, 0, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    utente_selezionato = arg1;
                                }
                            }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    button_pagatoda.setText(listItems[utente_selezionato]);
                                    utente_pagante = utenti_gruppo.get(list_id[utente_selezionato]);


                                   // Toast.makeText(ActivityExpense.this,utente_pagante.getName(),Toast.LENGTH_LONG).show();


                                }
                            }).setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();

                        }




                           /* mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                                    if(isChecked){

                                        for(int i=0;i<checkedItems.length;i++){
                                            checkedItems[i]=false;
                                        }
                                        utente_selezionato = position;
                                        checkedItems[position]=true;
                                    }
                                    else {
                                        checkedItems[position]=false;
                                    }
                                }
                            });


                            mBuilder.setCancelable(false);
                            mBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    button_pagatoda.setText(listItems[utente_selezionato]);
                                }
                            });


                            mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });





                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                        */


                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }




    }









    private void chooseButton(NomeDovuto nm, int pos) {




        if(pos==0 ) {

            button_divide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }

            });
        }
        else {

            //selezionato un gruppo


            DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Groups").child(nm.getId()).child("Users");

            databaseReference10.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    utenti_gruppo2.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                        NomeDovuto nm2 = new NomeDovuto(postSnapshot.getKey(), postSnapshot.child("Name").getValue(String.class));
                        EditText amount_text= (EditText) findViewById(R.id.Total_newexpense);
                        String stringamount = amount_text.getText().toString();
                        my_amount=0.0;
                        Double x =-1.0;
                        if(!stringamount.equals("")) {
                            my_amount = Double.parseDouble(stringamount);
                            x= (Double) my_amount / dataSnapshot.getChildrenCount();
                        }

                        nm2.setDovuto(x);

                        utenti_gruppo2.put(nm2.getId(), nm2);

                    }


                    //quando il bottone viene premuto
                    // dobbiamo mostrare una lista di untenti con l'edittext per ogni utente

                    button_divide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //prendere il totale
                            EditText amount_text= (EditText) findViewById(R.id.Total_newexpense);
                            String stringamount = amount_text.getText().toString();
                            my_amount=0.0;
                            if(!stringamount.equals("")) {
                                my_amount = Double.parseDouble(stringamount);
                            }

                            if (my_amount <= 0.0) {
                                Toast.makeText(ActivityExpense.this,R.string.no_amount , Toast.LENGTH_LONG).show();
                            } else {

                                ///devo mostrare un dialogo con tutti gli utenti

                                Double total_amount = my_amount; // todo
                                Double personal_amount = (Double) (total_amount / utenti_gruppo2.size());


                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityExpense.this);
                                mBuilder.setTitle(R.string.text_diviso_expense_string);


                                LinearLayout layout = new LinearLayout(ActivityExpense.this);
                                layout.setOrientation(LinearLayout.VERTICAL);


                                amountBox2 = new EditText[utenti_gruppo2.size()];
                                listato_id = new String[utenti_gruppo2.size()];



                                int i=0;

                               for(NomeDovuto utente_corrente : utenti_gruppo2.values()) {

                                   listato_id[i]= utente_corrente.getId();

                                   ///inizio per ogni utente
                                   utenti_gruppo2.remove(utente_corrente);
                                   utente_corrente.setDovuto(personal_amount);
                                   utenti_gruppo2.put(utente_corrente.getId(),utente_corrente);



                                   LinearLayout layout2 = new LinearLayout(ActivityExpense.this);
                                   layout2.setOrientation(LinearLayout.HORIZONTAL);
                                   layout2.setGravity(Gravity.CENTER);


                                   final TextView user_name_text = new TextView(ActivityExpense.this);
                                   user_name_text.setText(utente_corrente.getName() + " : ");
                                   user_name_text.setGravity(Gravity.RIGHT);
                                   user_name_text.setPadding(5, 2, 10, 2);
                                   user_name_text.setTextAppearance(ActivityExpense.this, android.R.style.TextAppearance_Holo_Medium);
                                   layout2.addView(user_name_text);


                                   final EditText amountBox = new EditText(ActivityExpense.this);
                                   amountBox.setHint(String.format("%.2f",utente_corrente.getDovuto()));
                                   amountBox.setText(String.format("%.2f",utente_corrente.getDovuto()));
                                   amountBox.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                  // amountBox.setInputType(DecimalForm);
                                   //amountBox.setPadding(5, 2, 2, 2);
                                   layout2.addView(amountBox);
                                   amountBox2[i] = amountBox;


                                   final TextView currency_text = new TextView(ActivityExpense.this);
                                   currency_text.setText("€");
                                   currency_text.setGravity(Gravity.LEFT);
                                   currency_text.setPadding(1, 2, 0, 2);

                                   currency_text.setTextAppearance(ActivityExpense.this, android.R.style.TextAppearance_Holo_Medium);
                                   layout2.addView(currency_text);


                                   layout.addView(layout2);
                                   i++;


                                   /// per ogni utente fine

                               }









                                final TextView final_text = new TextView(ActivityExpense.this);
                                final_text.setText("Il totale deve essere : " + total_amount + "€");
                                final_text.setTextAppearance(ActivityExpense.this, android.R.style.TextAppearance_Holo_Medium);
                                final_text.setGravity(Gravity.CENTER);
                                final_text.setPadding(1, 20, 0, 2);

                                layout.addView(final_text);


                                mBuilder.setView(layout);


                                mBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                     //  String s3= amountBox2[0].getText().toString();
                                       // Toast.makeText(ActivityExpense.this,s3,Toast.LENGTH_LONG).show();

                                        amountBox3 = new Double[amountBox2.length];

                                        for(int i=0;i<amountBox2.length;i++)
                                        {


                                            /*
                                            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                                            Number number = null;
                                            try {
                                                number = format.parse(amountBox2[i].getText().toString());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            Double d = number.doubleValue();

                                            */

                                          String s3= amountBox2[i].getText().toString(); //problema virgola
                                            s3 = s3.replace(',', '.');
                                           amountBox3[i] = Double.parseDouble(s3);

                                        }


                                        premuto_diviso=true;

                                    }
                                }).setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        premuto_diviso=false;
                                        dialogInterface.dismiss();
                                    }
                                });


                                AlertDialog mDialog = mBuilder.create();
                                mDialog.show();


                            }

                        }


                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });






        }









    }


    private void divideOrSplit() {


        if(premuto_diviso!=true){

            amountBox3 = new Double[utenti_gruppo2.size()];
            listato_id = new String[utenti_gruppo2.size()];

            EditText amount= (EditText) findViewById(R.id.Total_newexpense);
            String stringamount = amount.getText().toString();
            Double myamount2=0.0;
            if(!stringamount.equals("")) {
                myamount2 = Double.parseDouble(stringamount);
            }




            Double tmp = myamount2 / utenti_gruppo2.size();
            int i=0;

            for(NomeDovuto nm2: utenti_gruppo2.values()){

                amountBox3[i]=tmp;
                listato_id[i]=nm2.getId();

                i++;
            }



        }



    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }




}
