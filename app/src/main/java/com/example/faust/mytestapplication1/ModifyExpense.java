package com.example.faust.mytestapplication1;

import android.app.DatePickerDialog;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ModifyExpense extends AppCompatActivity implements View.OnClickListener{

    //private Date mydata;
    private Date myDate;
    private String mytitle;    private Double myamount;

    private NomeDovuto mygroup_selected;
    private CurrencyDetail mycurrency_selected_from_spinner;
    private String mycurrency_selected = "EUR"; // IMPOSTO SEMPRE EURO AL MOMENTO, PER ORA NON SI PUò CAMBIARE SENNò SOTTO SBALLA TUTTO
    private String string_image=null;


    private Double Total2;
    Spinner dropdownC;
    Spinner category;
    private String[] listato_id;
    Double somma_totale;
    Integer mDay,mYear,mMonth;
    private boolean premuto_diviso = false;

    private EditText[] amountBox2;
    private Double[] amountBox3;

    Double my_amount = 0.0;

    ProgressDialog mProgressDialog;
    private Uri downloadUri;
    private String key = null;

    private String mycategory;
    private String keyowner;
    private ImageButton buttonGallery, buttonCamera, buttonDelete;
    private ImageButton setdate;
    private static final int GALLERY_INTENT = 2, CAMERA_REQUEST_CODE = 1;
    private ImageView image_activity;
    Integer utente_selezionato = -1;

    private String nameowner;
    private StorageReference mStorage;

    private EditText date;
    int year = Calendar.YEAR, month = Calendar.MONTH, day = Calendar.DAY_OF_MONTH;
    private NomeDovuto id_group;
    private String id_currency;
    Double bilancioGlobale, bilanciodelgruppo, bilanciosingolo;
    String currencyBilancioGlobale, currencyBilancioDelGruppo, currencyBilancioSingolo;
    String id_owner = null;
    ArrayList<NomeDovuto> items_nomi_gruppi = new ArrayList<>();
    List<CurrencyDetail> items_nomi_valute = new ArrayList<>();

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4, databaseReference5, databaseReference6, databaseReference7;

    private HashMap<String, NomeDovuto> utenti_gruppo = new HashMap<>();
    private HashMap<String, NomeDovuto> utenti_gruppo2 = new HashMap<>();


    Button button_pagatoda;
    Button button_divide;

    private String[] listItems, list_id;
    private boolean[] checkedItems;

    private NomeDovuto utente_pagante;
    private HashMap<String, NomeDovuto> utentiGruppo_conDovuto = new HashMap<>();
     private String id_group_iniziale, name_group_iniziale, id_spesa_iniziale;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_expense);

        Intent inte = getIntent();
        id_spesa_iniziale = inte.getExtras().getString("ID_EX");
        id_group_iniziale = inte.getExtras().getString("GROUP_ID");
        name_group_iniziale = inte.getExtras().getString("GROUP_NAME");


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }


        if (savedInstanceState != null) {

        }



        EditText amount2 = (EditText) findViewById(R.id.Total_newexpense);
        amount2.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});

        category = (Spinner) findViewById(R.id.Category_newexpense);
        date = (EditText) findViewById(R.id.Data_newexpense);

        Button submitexpense = (Button) findViewById(R.id.buttonSubmitExpense);
        setdate = (ImageButton) findViewById(R.id.set_date);
        setdate.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        if(mDay<10){

            if(mMonth<9){
                date.setText("0"+mDay + "/" + "0"+(mMonth + 1) + "/" + mYear);


            }
            else{
                date.setText("0"+mDay + "/" + (mMonth +1) + "/" + mYear);

            }

        }
        else{
            if(mMonth<9){
                date.setText(+mDay + "/" + "0"+(mMonth + 1) + "/" + mYear);

            }
            else{
                date.setText(+mDay + "/" + (mMonth +1) + "/" + mYear);

            }
        }


        buttonCamera = (ImageButton) findViewById(R.id.buttonPhoto);

        buttonCamera.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.icon_camera128x128, 100, 100));

        buttonCamera.setOnClickListener(this);
        buttonGallery = (ImageButton) findViewById(R.id.buttonGallery);
        buttonGallery.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.picture_attachment256x256, 100, 100));

        buttonGallery.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance().getReference();

        image_activity = (ImageView) findViewById(R.id.imagePicture);
        //image_activity.setImageBitmap(
        //       decodeSampledBitmapFromResource(getResources(), R.drawable.gallery314x250, 100, 100));


        mProgressDialog = new ProgressDialog(this);


        button_pagatoda = (Button) findViewById(R.id.button_pagatoda_expense);

        button_divide = (Button) findViewById(R.id.button_diviso_expense);

        TextView group = (TextView) findViewById(R.id.Group_newexpense);
        group.setText(name_group_iniziale);


        // Showing selected spinner item


        //carica gli utenti e mette le cose per i nuovi bottoni

        cercareUtentiDelGruppo();


        chooseButton();


        //////////////
        // SPINNER E DATE

        //Spinner
        //Spinner


        dropdownC = (Spinner) findViewById(R.id.new_expense_currency_spinner);

        int i = 0;


        ///////////////////////////////////////

        ////// DB
        ////////////////////////////////////////





        //List<String> curry = CurrencyEditor.getCurrencySymbols();
        List<CurrencyDetail> currDet = CurrencyEditor.getCurrencyDetails();
        for (CurrencyDetail cD : currDet) {
            //TODO permettere solo certe valute
            if (cD.getSymbol().equals("EUR") || cD.getSymbol().equals("USD") || cD.getSymbol().equals("GBP"))
                items_nomi_valute.add(cD);
        }


        // items_nomi_valute.add(new CurrencyDetail());





        ArrayAdapter<CurrencyDetail> adapterC = new ArrayAdapter<CurrencyDetail>(this, android.R.layout.simple_spinner_item, items_nomi_valute);
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


        ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cts.getList());
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        date.addTextChangedListener(tw);





        //////////////
        // SETTO TUTTO

        FirebaseDatabase.getInstance().getReference().child("Activities").child(id_spesa_iniziale).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = dataSnapshot.child("Name").getValue(String.class);
                String string_date = dataSnapshot.child("Date").getValue(String.class);
                String string_category = dataSnapshot.child("Category").getValue(String.class);
                Double totale_iniziale = dataSnapshot.child("Total").getValue(Double.class);
                String owner_iniziale="";
                String name_owner_iniziale="";
                Double totale_owner_iniziale=0.0;

                for(DataSnapshot owner : dataSnapshot.child("Owner").getChildren()){

                     owner_iniziale = owner.getKey();

                     name_owner_iniziale = owner.child("Name").getValue(String.class);
                     totale_owner_iniziale = owner.child("Total").getValue(Double.class);

                }



                HashMap<String,NomeDovuto> users_iniziale = new HashMap<String, NomeDovuto>();

                for(DataSnapshot user : dataSnapshot.child("Users").getChildren()){

                    String id_x = user.getKey();

                    String  name_user_iniziale = user.child("Name").getValue(String.class);
                    Double totale_user_iniziale = user.child("Total").getValue(Double.class);


                    NomeDovuto nm = new NomeDovuto(id_x,name_user_iniziale);
                    nm.setDovuto(totale_user_iniziale);
                    users_iniziale.put(id_x,nm);


                }


                /////////setto

                EditText title_newexpense = (EditText)findViewById(R.id.Title_newexpense);
                // edittext date
                //spinner category
                EditText amount = (EditText) findViewById(R.id.Total_newexpense);
                Button pagatoda = (Button) findViewById(R.id.button_pagatoda_expense);

                title_newexpense.setText(title);
                amount.setText(totale_iniziale.toString());
                pagatoda.setText(name_owner_iniziale);


                Integer d,m,y;

                String[] diviso =string_date.split("/");
                d= new Integer(diviso[0]);
                m= new Integer(diviso[1]);
                y= new Integer(diviso[2]);

                String clean = String.format("%02d%02d%02d", d, m, y);
                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));


                date.setText(clean);



                getandSetImage(dataSnapshot.child("Image").getValue(String.class));

                utente_pagante = utenti_gruppo.get(owner_iniziale);

                ///spinner

                if (string_category.equals("Generale") || string_category.equals("General")) {
                    category.setSelection(0);
                }

                if (string_category.equals("Luce") || string_category.equals("Light")) {
                    category.setSelection(1);
                }

                if (string_category.equals("Gas") ) {
                    category.setSelection(2);
                }
                if (string_category.equals("Internet") ) {
                    category.setSelection(3);
                }

                if (string_category.equals("Alimentari") || string_category.equals("Food")) {
                    category.setSelection(4);
                }

                if (string_category.equals("Gift") || string_category.equals("Regalo")) {
                    category.setSelection(5);
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //////////////
        // FINE SETTO TUTTO














        //////////////
        ///
        //// INIZIO SUBMIT
        //////////////


        submitexpense.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                EditText title = (EditText) findViewById(R.id.Title_newexpense);
                mytitle = title.getText().toString();
                boolean check = false;


                EditText amount = (EditText) findViewById(R.id.Total_newexpense);
                String stringamount = amount.getText().toString();
                myamount = null;
                if (!stringamount.equals("")) {
                    myamount = Double.parseDouble(stringamount);
                    String sx = String.format("%.2f", myamount);
                    sx = sx.replace(",", ".");
                    myamount = Double.parseDouble(sx);
                }


                initButton();

                if (myamount != null) {
                    check = checkTotal(myamount);
                }




                //mygroup_selected = (NomeDovuto) group.getSelectedItem();


                //mydata =null;

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                myDate = null;
                try {
                    myDate = df.parse(date.getText().toString());

                    String myText = myDate.getDay() + "/" + (myDate.getMonth() + 1) + "/" + (1900 + myDate.getYear());

                } catch (Exception e) {
                    e.printStackTrace();
                }


                mycategory = (String) category.getSelectedItem();

                if ((!mytitle.equals("")) && (!stringamount.equals("")) && (!mycategory.equals("")) && (myDate != null) && (check != false)) {
                    //  MyActivity myactivity=new MyActivity(mytitle,R.drawable.giftboxred,myamount,  mydata , mycategory);


                    //prendo id gruppo e prendo hashmap con chiave il nome(nickname dell'utente) e valore id utente

                    Spinner curr = (Spinner) findViewById(R.id.new_expense_currency_spinner);
                    mycurrency_selected_from_spinner = (CurrencyDetail) curr.getSelectedItem();
                    try {
                        myamount = CurrencyEditor.convertCurrency(myamount, mycurrency_selected_from_spinner.getSymbol(), "EUR");
                        String sx = String.format("%.2f", myamount);
                        sx = sx.replace(",", ".");
                        myamount = Double.parseDouble(sx);


                    } catch (IOException e) {
                        //e.printStackTrace();
                        //lascio myamount così com'è
                    }

                    divideOrSplit();

                    //io con somma_totale so la mia spesa

                    final HashMap<String, String> myusers = new HashMap<String, String>();

                    /////////////
                    //// DATABASE
                    /////////////


                    databaseReference2 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group_iniziale);

                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue()==null){


                            }

                            else{



                                for (DataSnapshot postSnapshot : dataSnapshot.child("Users").getChildren()) {


                                    myusers.put(postSnapshot.getKey(), postSnapshot.child("Name").getValue(String.class));
                                    //Toast.makeText(getApplicationContext(),postSnapshot.getKey()+" "+ postSnapshot.child("Name").getValue(String.class), Toast.LENGTH_SHORT);
                                    Log.d("EXPENSE", postSnapshot.getKey() + " " + postSnapshot.child("Name").getValue(String.class));

                                }

//ora ho gli utenti e l'id del gruppo

                                databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                                //genero nuovo id per l'attività


                                    key = id_spesa_iniziale;


                                String Name = mytitle;
                                Double Total = myamount;
                                String sx = String.format("%.2f", myamount);
                                sx = sx.replace(",", ".");
                                myamount = Double.parseDouble(sx);
                                final Double Amount = myamount;
                                String GroupId = id_group_iniziale;

                                String Category = mycategory;
                                String Date = "" + myDate.getDate() + "/" + (myDate.getMonth()+1) + "/" + (myDate.getYear() + 1900);



                               /* databaseReference.child(key).child("Date").setValue(Date);
                                databaseReference.child(key).child("Category").setValue(Category);


                                databaseReference.child(key).child("Name").setValue(Name);
                                databaseReference.child(key).child("Total").setValue(Total);
                                databaseReference.child(key).child("Currency").setValue(mycurrency_selected);

                                databaseReference.child(key).child("GroupId").setValue(GroupId);*/


                                HashMap<String,Object> mappa = new HashMap<>();
                                mappa.put("Date",Date);
                                mappa.put("Category",Category);
                                mappa.put("Name",Name);
                                mappa.put("Total",Total);
                                mappa.put("Currency",mycurrency_selected);
                                mappa.put("GroupId",GroupId);

                                if(string_image!=null){
                                    mappa.put("Image",string_image);
                                }

                                databaseReference.child(key).setValue(mappa);





                                int count_users = myusers.size();
                                //todo

                                // prendere il vero owner
                                //in questo caso prendiamo il primo che capita dalla lista
                                //keyowner= firebaseAuth.getCurrentUser().getUid();
                                keyowner = utente_pagante.getId();

                                Name = myusers.get(keyowner);
                                Total = myamount / count_users;
                                String s = String.format("%.2f", Total);
                                s = s.replace(",", ".");
                                Total = Double.parseDouble(s);

                                //Ricerca di quanto ha pagato l'owner

                                for (int i = 0; i < listato_id.length; i++) {

                                    if (listato_id[i].equals(keyowner)) {

                                        Total = (amountBox3[i]);
                                        s = String.format("%.2f", Total);
                                        s = s.replace(",", ".");
                                        Total = Double.parseDouble(s);
                                    }


                                }


                                nameowner = Name;
                                //lo settiamo come owner
                                databaseReference.child(key).child("Owner").child(keyowner).child("Name").setValue(Name);
                                databaseReference.child(key).child("Owner").child(keyowner).child("Total").setValue(Total);
                                databaseReference.child(key).child("Owner").child(keyowner).child("Currency").setValue(mycurrency_selected);
                                Log.d("EXPENSE", Name + " " + Total + " (DB)");

                                //preso tutti i nomi degli utenti
                                for (String in : myusers.keySet()) {

                                    if (!in.equals(keyowner)) {

                                        //Ricerca di quanto ha pagato l'owner

                                        for (int i = 0; i < listato_id.length; i++) {

                                            if (listato_id[i].equals(in)) {

                                                Total = amountBox3[i];
                                                s = String.format("%.2f", Total);
                                                s = s.replace(",", ".");
                                                Total = Double.parseDouble(s);
                                            }


                                        }

                                        Name = in;
                                        //Setto la spesa a tutti gli utenti
                                        databaseReference.child(key).child("Users").child(in).child("Name").setValue(myusers.get(in));
                                        databaseReference.child(key).child("Users").child(in).child("Total").setValue(Total);

                                        databaseReference.child(key).child("Users").child(in).child("Currency").setValue(mycurrency_selected);


                                    }

                                }//todo chiusa operazione


                                    //1 groups-ACTIVITIES
                                    //2 users- ACTIVITIES x te e x tutti
                                    //3 USERS - BILANCIO x te e x tutti
                                    // 4 USERS - GROUPS  x te e x tutti


                                    //1 groups-ACTIVITIES
                                    // key
                                    Name = mytitle;
                                    Total = myamount;
                                    s = String.format("%.2f", myamount);
                                    s = s.replace(",", ".");
                                    Total = Double.parseDouble(s);

                                    databaseReference3 = FirebaseDatabase.getInstance().getReference("Groups").child(GroupId).child("Activities");

                                    /*databaseReference3.child(key).child("Date").setValue(Date);
                                    databaseReference3.child(key).child("Category").setValue(Category);

                                    databaseReference3.child(key).child("Name").setValue(Name);
                                    databaseReference3.child(key).child("Total").setValue(Total);
                                    databaseReference3.child(key).child("Currency").setValue(mycurrency_selected);*/

                                HashMap<String,Object> mappa2 = new HashMap<>();
                                mappa2.put("Date",Date);
                                mappa2.put("Category",Category);
                                mappa2.put("Name",Name);
                                mappa2.put("Total",Total);
                                mappa2.put("NamePagato",nameowner);
                                mappa2.put("Currency",mycurrency_selected);

                                databaseReference3.child(key).setValue(mappa2);


                                    //2 users- ACTIVITIES

                                    //users - id - activities - key- -----> Total -----> Name

                                    Name = mytitle;
                                    Total = myamount;
                                    s = String.format("%.2f", myamount);
                                    s = s.replace(",", ".");
                                    Total = Double.parseDouble(s);
                                    databaseReference4 = FirebaseDatabase.getInstance().getReference("Users");
                                    //da fare per tutti gli utenti


                                    for (String id_user : myusers.keySet()) {


                                        /*databaseReference4.child(id_user).child("Activities").child(key).child("Date").setValue(Date);
                                        databaseReference4.child(id_user).child("Activities").child(key).child("Category").setValue(Category);
                                        databaseReference4.child(id_user).child("Activities").child(key).child("Name").setValue(Name);
                                        databaseReference4.child(id_user).child("Activities").child(key).child("Total").setValue(Total);
                                        databaseReference4.child(id_user).child("Activities").child(key).child("Currency").setValue(mycurrency_selected);


                                        databaseReference4.child(id_user).child("Activities").child(key).child("Group").setValue(name_group_iniziale);*/

                                        HashMap<String,Object> mappa4 = new HashMap<>();
                                        mappa4.put("Date",Date);
                                        mappa4.put("Category",Category);
                                        mappa4.put("Name",Name);
                                        mappa4.put("Total",Total);
                                        mappa4.put("Currency",mycurrency_selected);
                                        mappa4.put("Group",name_group_iniziale);

                                        databaseReference4.child(id_user).child("Activities").child(key).setValue(mappa4);


                                    }

                                    //Aggiornare i bilanci

                                //AGGIORNO
                                int i=0;

                                for(String id : myusers.keySet())
                                {

                                    new DBShortKeys()._aggiornaBilancioGlobale(id);

                                    new DBShortKeys()._aggiornaBilancioGruppo(id, GroupId);


                                    for(String id2 : myusers.keySet())
                                    {
                                        if(!id.equals(id2))
                                        {
                                            new DBShortKeys()._aggiornaBilanciFraUtentiGruppoHALF(id, id2, GroupId);
                                        }
                                    }
                                    i++;


                                    if(i==myusers.keySet().size()){

                                        Intent intent = new Intent(ModifyExpense.this, MainActivity.class);
                                        intent.putExtra("GROUP_ID", id_group_iniziale);
                                        intent.putExtra("GROUP_NAME", name_group_iniziale);
                                        startActivity(intent);
                                        finish();

                                    }

                                }










                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {




                        }
                    });




                    return;
                } else {

                    if (check == false) {
                        Toast.makeText(getApplicationContext(), R.string.toast_notequal, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(), R.string.toast_emptyaddexpense, Toast.LENGTH_LONG).show();
                    }


                    return;
                }


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
    protected void onRestart() {
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
                .setTitle(R.string.backmodify_title)
                .setMessage(R.string.backmodify_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {




                        Intent intent = new Intent(ModifyExpense.this, MainActivity.class);
                        intent.putExtra("GROUP_ID", id_group_iniziale);
                        intent.putExtra("GROUP_NAME", name_group_iniziale);
                        ModifyExpense.this.startActivity(intent);
                        finish();

                    }
                }).create().show();
    }


    @Override
    public void onClick(View v) {

        if (v == buttonGallery) {


            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent, GALLERY_INTENT);


        } else if (v == buttonCamera) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            startActivityForResult(intent, CAMERA_REQUEST_CODE);


        }
        else if(v==setdate){

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {


                            Log.d("DATE","giorno "+ dayOfMonth + " mese " + monthOfYear + " anno "+ year);
                            if(dayOfMonth<10){

                                if(monthOfYear<9){
                                    date.setText("0"+dayOfMonth + "/" + "0"+(monthOfYear + 1) + "/" + year);


                                }
                                else{
                                    date.setText("0"+dayOfMonth + "/" + (monthOfYear +1) + "/" + year);

                                }

                            }
                            else{
                                if(monthOfYear<9){
                                    date.setText(+dayOfMonth + "/" + "0"+(monthOfYear + 1) + "/" + year);

                                }
                                else{
                                    date.setText(+dayOfMonth + "/" + (monthOfYear +1) + "/" + year);

                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();}

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {


            String msg = getString(R.string.dialog_image_profile);
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();


            Uri uri = data.getData();


            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());


            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUri = taskSnapshot.getDownloadUrl();

                    key = id_spesa_iniziale;


                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Activities")
                            .child(key)
                            .child("Image");

                    string_image = downloadUri.toString();

                    ref.setValue(string_image);



                    // Picasso.with(ReadProfileActivity.this).load(downloadUri).fit().centerCrop().into(image_profile);

                    Toast.makeText(ModifyExpense.this, R.string.upload_ok, Toast.LENGTH_LONG).show();
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
                        Picasso.with(ModifyExpense.this)
                                .load(downloadUri.toString())
                                .fit()
                                .centerCrop()
                                .into(image_activity);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ModifyExpense.this, R.string.upload_no, Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();


                }
            });


        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //image_profile.setImageBitmap(imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);


                key = id_spesa_iniziale;


            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Activities")
                    .child(key)
                    .child("Image");
            string_image = imageEncoded;
            ref.setValue(string_image);


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

        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }


    private void cercareUtentiDelGruppo() {


        DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group_iniziale).child("Users");

        databaseReference10.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                utenti_gruppo.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    NomeDovuto nm = new NomeDovuto(postSnapshot.getKey(), postSnapshot.child("Name").getValue(String.class));

                    utenti_gruppo.put(nm.getId(), nm);

                }


                //voglio mettere nel mio bottone il mio NOME
            //   button_pagatoda.setText(utenti_gruppo.get(firebaseAuth.getCurrentUser().getUid().toString()).getName());
            //    utente_pagante = utenti_gruppo.get(firebaseAuth.getCurrentUser().getUid().toString());

                //quando il bottone viene premuto
                // dobbiamo mostrare una lista di untenti e selezionare chi paga la spesa

                button_pagatoda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ///devo mostrare un dialogo con tutti gli utenti


                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ModifyExpense.this);
                        mBuilder.setTitle(R.string.text_pagatoda_expense_string);

                        listItems = new String[utenti_gruppo.size()];
                        list_id = new String[utenti_gruppo.size()];
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


    private void chooseButton() {


        //selezionato un gruppo


        DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group_iniziale).child("Users");

        databaseReference10.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                utenti_gruppo2.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    NomeDovuto nm2 = new NomeDovuto(postSnapshot.getKey(), postSnapshot.child("Name").getValue(String.class));
                    EditText amount_text = (EditText) findViewById(R.id.Total_newexpense);
                    String stringamount = amount_text.getText().toString();
                    my_amount = 0.0;
                    Double x = -1.0;
                    if (!stringamount.equals("")) {
                        my_amount = Double.parseDouble(stringamount);
                        x = (Double) my_amount / dataSnapshot.getChildrenCount();
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
                        EditText amount_text = (EditText) findViewById(R.id.Total_newexpense);
                        String stringamount = amount_text.getText().toString();
                        my_amount = 0.0;
                        if (!stringamount.equals("")) {
                            my_amount = Double.parseDouble(stringamount);
                        }

                        if (my_amount <= 0.0) {
                            Toast.makeText(ModifyExpense.this, R.string.no_amount, Toast.LENGTH_LONG).show();
                        } else {

                            ///devo mostrare un dialogo con tutti gli utenti

                            Double total_amount = my_amount; // todo
                            Double personal_amount = (Double) (total_amount / utenti_gruppo2.size());
                            String s = String.format("%.2f", personal_amount);
                            s = s.replace(",", ".");
                            personal_amount = Double.parseDouble(s);


                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ModifyExpense.this);
                            mBuilder.setTitle(R.string.text_diviso_expense_string);


                            LinearLayout layout = new LinearLayout(ModifyExpense.this);
                            layout.setOrientation(LinearLayout.VERTICAL);


                            amountBox2 = new EditText[utenti_gruppo2.size()];
                            listato_id = new String[utenti_gruppo2.size()];


                            int i = 0;
                            somma_totale = 0.0;
                            for (NomeDovuto utente_corrente : utenti_gruppo2.values()) {
                                ///inizio per ogni utente
                                utenti_gruppo2.remove(utente_corrente);
                                utente_corrente.setDovuto(personal_amount);
                                utenti_gruppo2.put(utente_corrente.getId(), utente_corrente);
                                somma_totale += personal_amount;
                            }


                            if (somma_totale != total_amount) {

                                for (NomeDovuto utente_corrente : utenti_gruppo2.values()) {

                                    if (somma_totale > total_amount) {
                                        utenti_gruppo2.remove(utente_corrente);
                                        utente_corrente.setDovuto(utente_corrente.getDovuto() - (somma_totale - total_amount));
                                        utenti_gruppo2.put(utente_corrente.getId(), utente_corrente);
                                        break;


                                    } else {
                                        utenti_gruppo2.remove(utente_corrente);
                                        utente_corrente.setDovuto(utente_corrente.getDovuto() + (total_amount - somma_totale));
                                        utenti_gruppo2.put(utente_corrente.getId(), utente_corrente);
                                        break;


                                    }

                                }


                            }


                            for (NomeDovuto utente_corrente : utenti_gruppo2.values()) {

                                listato_id[i] = utente_corrente.getId();


                                LinearLayout layout2 = new LinearLayout(ModifyExpense.this);
                                layout2.setOrientation(LinearLayout.HORIZONTAL);
                                layout2.setGravity(Gravity.CENTER);


                                final TextView user_name_text = new TextView(ModifyExpense.this);
                                user_name_text.setText(utente_corrente.getName() + " : ");
                                user_name_text.setGravity(Gravity.RIGHT);
                                user_name_text.setPadding(5, 2, 10, 2);
                                user_name_text.setTextAppearance(ModifyExpense.this, android.R.style.TextAppearance_Holo_Medium);
                                layout2.addView(user_name_text);


                                final EditText amountBox = new EditText(ModifyExpense.this);
                                amountBox.setHint(String.format("%.2f", utente_corrente.getDovuto()));
                                amountBox.setText(String.format("%.2f", utente_corrente.getDovuto()));
                                amountBox.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                // amountBox.setInputType(DecimalForm);
                                //amountBox.setPadding(5, 2, 2, 2);
                                layout2.addView(amountBox);
                                amountBox2[i] = amountBox;


                                final TextView currency_text = new TextView(ModifyExpense.this);
                                currency_text.setText("€");
                                currency_text.setGravity(Gravity.LEFT);
                                currency_text.setPadding(1, 2, 0, 2);

                                currency_text.setTextAppearance(ModifyExpense.this, android.R.style.TextAppearance_Holo_Medium);
                                layout2.addView(currency_text);


                                layout.addView(layout2);
                                i++;


                                /// per ogni utente fine

                            }


                            final TextView final_text = new TextView(ModifyExpense.this);
                            final_text.setText("Il totale deve essere : " + total_amount + "€");
                            final_text.setTextAppearance(ModifyExpense.this, android.R.style.TextAppearance_Holo_Medium);
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

                                    for (int i = 0; i < amountBox2.length; i++) {


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

                                        String s3 = amountBox2[i].getText().toString(); //problema virgola
                                        s3 = s3.replace(',', '.');
                                        amountBox3[i] = Double.parseDouble(s3);

                                    }


                                    premuto_diviso = true;

                                }
                            }).setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    premuto_diviso = false;
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


    private void divideOrSplit() {


        if(premuto_diviso!=true) {

            amountBox3 = new Double[utenti_gruppo2.size()];
            listato_id = new String[utenti_gruppo2.size()];

            int i = 0;

            for (NomeDovuto nm2 : utenti_gruppo2.values()) {

                amountBox3[i] = nm2.getDovuto();
                listato_id[i] = nm2.getId();


                i++;
            }

        }




    }


    public boolean checkTotal(Double x) {


        Double tmp = 0.0;
        int i=0;
        if(premuto_diviso==false){
            return true;
        }

        if(amountBox3==null){ //maroooo
            return true;
        }



        for (NomeDovuto utente_corrente : utenti_gruppo2.values()) {
            ///inizio per ogni utente

            tmp += amountBox3[i];
            i++;
        }

        if (tmp.equals(x)) {
            return true;
        }


        return false;
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


    private void initButton() {


        //selezionato un gruppo
        if (premuto_diviso == false) {

            Double somma_totale2 =0.0;


            utenti_gruppo2.clear();

            for (NomeDovuto postSnapshot : utenti_gruppo.values()) {


                NomeDovuto nm2 = new NomeDovuto(postSnapshot.getId(), postSnapshot.getName());
                EditText amount_text = (EditText) findViewById(R.id.Total_newexpense);
                String stringamount = amount_text.getText().toString();
                my_amount = 0.0;
                Double x = -1.0;
                Double total=0.0;
                if (!stringamount.equals("")) {
                    my_amount = Double.parseDouble(stringamount);
                    x = (Double) my_amount / utenti_gruppo.size();
                    String s = String.format("%.2f", x);
                    s = s.replace(",", ".");
                    x = Double.parseDouble(s);

                    somma_totale2+=x;
                }





                nm2.setDovuto(x);

                utenti_gruppo2.put(nm2.getId(), nm2);

            }



            if (!somma_totale2.equals(my_amount)) {

                for (NomeDovuto utente_corrente : utenti_gruppo2.values()) {

                    if (somma_totale2 > my_amount) {
                        utenti_gruppo2.remove(utente_corrente);
                        utente_corrente.setDovuto(utente_corrente.getDovuto() - (somma_totale2 - my_amount));
                        utenti_gruppo2.put(utente_corrente.getId(), utente_corrente);
                        break;


                    } else {
                        utenti_gruppo2.remove(utente_corrente);
                        utente_corrente.setDovuto(utente_corrente.getDovuto() + (my_amount - somma_totale2));
                        utenti_gruppo2.put(utente_corrente.getId(), utente_corrente);
                        break;


                    }

                }


            }



        }

    }



    private void getandSetImage(String image) {

        //getImage of user



                if (image != null) {

                    if (!image.contains("http")) {
                        try {


                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(image);
                            //Bitmap imageCirle = getclip(imageBitmaptaken);
                            // imageBitmaptaken.reconfigure(600,200, Bitmap.Config.ARGB_4444);

                            image_activity.setImageBitmap(imageBitmaptaken);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {



                       /* Glide
                                .with(getApplicationContext())
                                .load(image)
                                .override(600, 200)
                                .fitCenter()
                                .into(profile_image);
*/
                        Picasso.with(ModifyExpense.this)
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(image_activity);


                        // Bitmap imageBitmaptaken = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                        // Bitmap imageCirle = getclip(imageBitmaptaken);
                        // profile_image.setImageBitmap(imageCirle);


                    }


                }


            }









}
