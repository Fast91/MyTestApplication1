package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.DebugUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentFragment extends Fragment
{
    private FirebaseAuth firebaseAuth;
    private String mBundlePaymentReceiver;
    private String mBundlePaymentGroup;

    private String mReceiver;
    private String mReceiverId;
    private String mSenderId;
    private String mGroupId;
    private String mActivityId;
    private String mDefaultAmount;
    private String mGroup;
    private String mDefaultAmountValue;
    private String mCustomAmountValue;
    private String mAmountValue;
    private String sender_name , receiver_name;
    private Double bilancioGlobale,bilanciodelgruppo,bilanciosingolo;

    private boolean mDefaultAmountOptionSelected;
    private boolean mCustomAmountEditTextOptionSelected;
    // se true significa che ho selezionato il bottone con l'importo esatto
    // se è false significa che ho selezionato il bottone custom amount per inserire un importo personalizzato

    //private RelativeLayout mLayout;

    private TextView mAmountLabelTextView;
    private TextView mReceiverLabelTextView;

    private TextView mSenderDetailTextView;
    private TextView mReceiverDetailTextView;
    private TextView mGroupDetailTextView;
    private TextView mAmountDetailTextView;

    private Button mDefaultAmountButton;
    private Button mCustomAmountButton;

    private EditText mCustomAmountEditText;

    private Button mConfirmButton;

    private Drawable mDeselectedButtonBackground;
    private Drawable mSelectedButtonBackground;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();



        Log.d("CIAO","sto iniziadidiai");

       // mBundlePaymentGroup= b.getString("GROUP_ID");
        mBundlePaymentReceiver = getArguments().getString("expense_receiver", null);
        mBundlePaymentGroup = getArguments().getString("expense_group", null);
        mDefaultAmountOptionSelected = true;
        mCustomAmountEditTextOptionSelected = false;


        firebaseAuth = FirebaseAuth.getInstance();

        // TODO DB etc etc
        mReceiver = mBundlePaymentReceiver;
        mGroup = mBundlePaymentGroup;
        mDefaultAmountValue = "50€"; // da importare dal db
        mCustomAmountValue=mDefaultAmountValue;
        mAmountValue=mDefaultAmountValue;



        ////PROVO A PRENDERLI


        mSenderId = (String) firebaseAuth.getCurrentUser().getUid(); // ok
        mGroupId = (String) getArguments().getString("ID_GROUP"); // ok
        mDefaultAmount =(String) getArguments().getString("DOVUTO"); // ok
        mReceiverId = (String) getArguments().getString("ID_USER");

        mDefaultAmountValue = mDefaultAmount;
        mCustomAmountValue = mDefaultAmount;
        mAmountValue = mDefaultAmount;

    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        wireUpWidgets(view);
        retrieveDataFromDB(view);
        initWidgets();
        setListeners();

        return view;
    }









    private void wireUpWidgets(View view)
    {
        //mLayout = (RelativeLayout) view.findViewById(R.id.layout_fragment_payment);

        mAmountLabelTextView = (TextView) view.findViewById(R.id.amount_label_payment_tv);
        mReceiverLabelTextView = (TextView) view.findViewById(R.id.receiver_label_payment_tv);

        mSenderDetailTextView = (TextView) view.findViewById(R.id.sender_detail_payment_tv);
        mReceiverDetailTextView = (TextView) view.findViewById(R.id.receiver_detail_payment_tv);
        mGroupDetailTextView = (TextView) view.findViewById(R.id.group_detail_payment_tv);
        mAmountDetailTextView = (TextView) view.findViewById(R.id.amount_detail_payment_tv);

        mDefaultAmountButton = (Button) view.findViewById(R.id.default_amount_button_payment_tv);
        mCustomAmountButton = (Button) view.findViewById(R.id.custom_amount_button_payment_tv);

        mCustomAmountEditText = (EditText) view.findViewById(R.id.custom_amount_edittext_payent_tv);

        mConfirmButton = (Button) view.findViewById(R.id.confirm_button_payment_tv);

        mDeselectedButtonBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.buttonshape_faust, null);
        mSelectedButtonBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.button_pressed_faust, null);
        /*mDeselectedButtonBackground = ContextCompat.getDrawable(getContext(), R.drawable.buttonshape_faust);
        mSelectedButtonBackground = ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_faust);*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDeselectedButtonBackground = view.getResources().getDrawable(R.drawable.buttonshape_faust, getContext().getTheme());
            mSelectedButtonBackground = view.getResources().getDrawable(R.drawable.button_pressed_faust, getContext().getTheme());
        }
        else
        {
            mDeselectedButtonBackground = view.getResources().getDrawable(R.drawable.buttonshape_faust);
            mSelectedButtonBackground = view.getResources().getDrawable(R.drawable.button_pressed_faust);
        }*/

    }





    private void retrieveDataFromDB(View view)
    {
        final AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();


        DatabaseReference databaseReference,databaseReference2,databaseReference3;


        //setto nella text view il sender
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Name");



        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mReceiver=(String) dataSnapshot.getValue(String.class);
                sender_name = mReceiver;

                ((TextView) myactivity.findViewById(R.id.sender_detail_payment_tv)).setText(mReceiver);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });//fin qui tutto bene




        //setto nella text view il RECEIVER

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(mReceiverId).child("Name");



        //Read content data
        databaseReference2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String mx=(String) dataSnapshot.getValue(String.class);
                receiver_name = mx;

                ((TextView) myactivity.findViewById(R.id.receiver_detail_payment_tv)).setText(mx);
                ((TextView) myactivity.findViewById(R.id.receiver_label_payment_tv)).setText(mx);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });//funziona


        //setto nella text view il RECEIVER

        databaseReference3 = FirebaseDatabase.getInstance().getReference("Groups").child(mGroupId).child("Name");



        //Read content data
        databaseReference3.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String mx=(String) dataSnapshot.getValue(String.class);

                ((TextView) myactivity.findViewById(R.id.group_detail_payment_tv)).setText(mx);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });//funziona



    }

    private void initWidgets()
    {
        // INIZIALIZZO I BOTTONI, MI ASSICURO CHE IL CONFIRM SIA DISABILITATO,
        // CHE TUTTI I BOTTONI SIANO VISIBILI E CHE L'EDITTEXT DEL CUSTOM AMOUNT SIA
        // INVISIBILE E DISABILITATO.

        mAmountLabelTextView.setText(mDefaultAmountValue);
        mDefaultAmountButton.setText(mDefaultAmountValue);
        mCustomAmountEditText.setText(mDefaultAmountValue);
        mReceiverLabelTextView.setText(mReceiver);
        mReceiverDetailTextView.setText(mReceiver);
        mGroupDetailTextView.setText(mGroup);

        mDefaultAmountButton.setEnabled(true);
        mCustomAmountButton.setEnabled(true);
        mConfirmButton.setEnabled(true);
        mCustomAmountEditText.setEnabled(true);

        mDefaultAmountButton.setVisibility(View.VISIBLE);
        mCustomAmountButton.setVisibility(View.VISIBLE);
        mConfirmButton.setVisibility(View.VISIBLE);
        mCustomAmountEditText.setVisibility(View.INVISIBLE);

        updateButtonsBackground();
        swapCustomButtonToEditText(false);

        refreshValues();
        refreshButtonsValue();
        refreshDetails();
    }



    private void swapCustomButtonToEditText(boolean toEditText)
    {
        if(toEditText!=mCustomAmountEditTextOptionSelected)
        {
            if (toEditText) {
                mCustomAmountEditTextOptionSelected = true;

                mCustomAmountButton.setEnabled(false);
                mCustomAmountEditText.setVisibility(View.VISIBLE);
                mCustomAmountButton.setVisibility(View.INVISIBLE);
                mCustomAmountEditText.setEnabled(true);
            } else {
                mCustomAmountEditTextOptionSelected = false;

                mCustomAmountEditText.setEnabled(false);
                mCustomAmountButton.setVisibility(View.VISIBLE);
                mCustomAmountEditText.setVisibility(View.INVISIBLE);
                mCustomAmountButton.setEnabled(true);
            }
        }
    }

    private void refreshValues()
    {
        mAmountValue = mDefaultAmountOptionSelected?mDefaultAmountValue:mCustomAmountValue;
    }

    private void refreshButtonsValue()
    {
        //mCustomAmountButton.setText(mCustomAmountValue);
    }

    private void refreshDetails()
    {
        mAmountDetailTextView.setText(mAmountValue);
    }

    private void setListeners()
    {
        // azioni da eseguire:
        // devo gestire la comparsa e scomparsa del bottone e dell'edittext del custom amount
        // devo modificare il valore di mDefaultAmountSelected quando premo uno dei due bottoni di amount
        // e di conseguenza devo aggiornare i background.
        mDefaultAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultAmountOptionSelected = true;
                updateButtonsBackground();
                swapCustomButtonToEditText(false);
                refreshValues();
                refreshDetails();
            }
        });
        mCustomAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultAmountOptionSelected = false;
                updateButtonsBackground();
                swapCustomButtonToEditText(true);
                refreshValues();
                refreshDetails();
            }
        });
        mCustomAmountEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultAmountOptionSelected = false;
                updateButtonsBackground();
                swapCustomButtonToEditText(true);
            }
        });
        mCustomAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mCustomAmountValue=s.toString();
                refreshValues();
                refreshButtonsValue();
                refreshDetails();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO qui il DB deve fare la transazione
                // ...
                mAmountValue= mAmountValue.replace("€",""); //todo cazzoooo metti il simbolooo
                mCustomAmountValue = mCustomAmountValue.replace("€","");

                mAmountValue= mAmountValue.replace(",","."); //todo cazzoooo metti il simbolooo
                mCustomAmountValue = mCustomAmountValue.replace(",",".");



                if(Double.parseDouble(mAmountValue)!=0) {

                    updateButtonsBackground();
                    swapCustomButtonToEditText(false);

                    saveAsNewActivity();
                    //ritornare al main

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(),R.string.payment0,Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void updateButtonsBackground()
    {
        mDefaultAmountButton.setBackground(mDefaultAmountOptionSelected?mSelectedButtonBackground:mDeselectedButtonBackground);
        mCustomAmountButton.setBackground(!mDefaultAmountOptionSelected?mSelectedButtonBackground:mDeselectedButtonBackground);
        // non devo modificare il background degli altri due bottoni
    }




    private void saveAsNewActivity(){

        ///////////////////////////////////////////////////////
        //creare qualcosa come nuova attivita e gestire i conti
        ///////////////////////////////////////////////////////


        //Registrarla come attivita

        //andare su gruppi e inserirla li

        //andare su user e cambiare il bilancio globale

        //andare su user su quel gruppo e cambiare il bilancio del gruppo

        //andare su user su quel gruppo e prendere l'utente e cambiare il bilancio singolo


        /////////////
        //// DATABASE
        /////////////

        DatabaseReference databaseReference, databaseReference2, databaseReference3 ,databaseReference4 , databaseReference5, databaseReference6, databaseReference7;




                //ora ho gli utenti e l'id del gruppo

                databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                //genero nuovo id per l'attività
                String key = databaseReference.push().getKey();
                String Name = getString(R.string.payment_title);
                final Double Total = Double.parseDouble(mAmountValue);
                String GroupId = mGroupId;

                String Category=getString(R.string.payment_title);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String Date=""+dateFormat.format(date);


                databaseReference.child(key).child("Name").setValue(Name);
                databaseReference.child(key).child("Total").setValue(Total);

                databaseReference.child(key).child("GroupId").setValue(GroupId);

                databaseReference.child(key).child("Category").setValue(Category);
                databaseReference.child(key).child("Date").setValue(Date);


                //todo

                // prendere il vero owner
                //in questo caso prendiamo il primo che capita dalla lista
                String keyowner= firebaseAuth.getCurrentUser().getUid();

                Name=sender_name;



                //lo settiamo come owner
                databaseReference.child(key).child("Owner").child(keyowner).child("Name").setValue(Name);
                databaseReference.child(key).child("Owner").child(keyowner).child("Total").setValue(Total);
                Log.d("EXPENSE", Name + " " + Total + " (DB)" );



                        //setto al ricevente la spesa a 0
                          Name=receiver_name;
                        Double Total2= 0.0;
                        //Setto la spesa a tutti gli utenti
                        databaseReference.child(key).child("Users").child(mReceiverId).child("Name").setValue(Name);
                        databaseReference.child(key).child("Users").child(mReceiverId).child("Total").setValue(Total2);



                    //1 groups-ACTIVITIES
                    //2 users- ACTIVITIES x te e x tutti
                    //3 USERS - BILANCIO x te e x tutti
                    // 4 USERS - GROUPS  x te e x tutti


                    //1 groups-ACTIVITIES
                    // key
                    Name = getString(R.string.payment_title);;

                    databaseReference3 = FirebaseDatabase.getInstance().getReference("Groups").child(GroupId).child("Activities");
                    databaseReference3.child(key).child("Total").setValue(Total);
                    databaseReference3.child(key).child("Name").setValue(Name);



                    //2 users- ACTIVITIES

                    //users - id - activities - key- -----> Total -----> Name


                    databaseReference4 = FirebaseDatabase.getInstance().getReference("Users");
                    //da fare per tutti gli utenti





                        databaseReference4.child(mSenderId).child("Activities").child(key).child("Name").setValue(Name);
                        databaseReference4.child(mSenderId).child("Activities").child(key).child("Total").setValue(Total);

                         databaseReference4.child(mReceiverId).child("Activities").child(key).child("Name").setValue(Name);
                         databaseReference4.child(mReceiverId).child("Activities").child(key).child("Total").setValue(Total);






                    //3 USERS - BILANCIO Globale x te e x tutti
                    //per l'owner deve ricevere +
                    //per gli altri devono dare -


                    //First sarebbe in questo momento chi paga owner


                        //Prendermi il bilancio
                        ////////// SENDER + OWNER
                        ///////////////
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(mSenderId).child("GlobalBalance");

                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                bilancioGlobale = (Double) dataSnapshot.getValue(Double.class);


                                if (bilancioGlobale == null) {
                                    bilancioGlobale = 0.0;
                                }

                                Log.d("EXPENSE", "bilancioGlobale: " + bilancioGlobale);



                                    //devo levare
                                    Double tmp= bilancioGlobale+Total;
                                    FirebaseDatabase.getInstance().getReference("Users").child(mSenderId).child("GlobalBalance").setValue(tmp);

                                    //databaseReference5.removeEventListener(this);
                                    Log.d("EXPENSE", "bilancioGlobale-Total2: " + (tmp));


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                      //Prendermi il bilancio
                       ////////// RECEIVER -
                          ///////////////
                         databaseReference3 = FirebaseDatabase.getInstance().getReference("Users").child(mReceiverId).child("GlobalBalance");

                         databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {

                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             bilancioGlobale = (Double) dataSnapshot.getValue(Double.class);


                             if (bilancioGlobale == null) {
                                 bilancioGlobale = 0.0;
                             }

                             Log.d("EXPENSE", "bilancioGlobale: " + bilancioGlobale);



                             //devo levare
                             Double tmp= bilancioGlobale-Total;
                             FirebaseDatabase.getInstance().getReference("Users").child(mReceiverId).child("GlobalBalance").setValue(tmp);

                             //databaseReference5.removeEventListener(this);
                             Log.d("EXPENSE", "bilancioGlobale-Total2: " + (tmp));


                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                                      }

                            });


                        ///////////// FINE BILANCIO GLOBALE



                    ////////////////////////////////////////////
                    ////////////////////////////////////////////
                    // 4 USERS - GROUPS - per quel gruppo ID - Users   ---> Total ---> Name  ( x owner e x tutti )
                    ////////////////////////////////////////////
                    ////////////////////////////////////////////










        //Prendermi il bilancio del gruppo
        ////////// SENDER + OWNER
        ///////////////
        databaseReference4 = FirebaseDatabase.getInstance().getReference("Users").child(mSenderId).child("Groups").child(mGroupId);

        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ABBIAMO AGGIORNATO IL BILANCIO DEL GRUPPO


                bilanciodelgruppo= (Double) dataSnapshot.child("Total").getValue(Double.class);


                if (bilanciodelgruppo == null) {
                    bilanciodelgruppo = 0.0;
                }
                Log.d("EXPENSE", "bilancioGlobale: " + bilanciodelgruppo);



                //devo levare
                Double tmp= bilanciodelgruppo+Total;
                FirebaseDatabase.getInstance().getReference("Users").child(mSenderId).child("Groups").child(mGroupId).child("Total").setValue(tmp);

                //databaseReference5.removeEventListener(this);
                Log.d("EXPENSE", "bilanciodelgruppo+Total2: " + (tmp));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        ////////// RECEIVER -
        ///////////////
        databaseReference4 = FirebaseDatabase.getInstance().getReference("Users").child(mReceiverId).child("Groups").child(mGroupId);

        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ABBIAMO AGGIORNATO IL BILANCIO DEL GRUPPO


                bilanciodelgruppo= (Double) dataSnapshot.child("Total").getValue(Double.class);


                if (bilanciodelgruppo == null) {
                    bilanciodelgruppo = 0.0;
                }
                Log.d("EXPENSE", "bilancioGlobale: " + bilanciodelgruppo);



                //devo levare
                Double tmp= bilanciodelgruppo-Total;
                FirebaseDatabase.getInstance().getReference("Users").child(mReceiverId).child("Groups").child(mGroupId).child("Total").setValue(tmp);

                //databaseReference5.removeEventListener(this);
                Log.d("EXPENSE", "bilanciodelgruppo-Total2: " + (tmp));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



                        ///////////// FINE BILANCIO del gruppo













                    ////// punto 5 aggiornare i singoli bilanci all'interno del gruppo
                    // per quelle persone convolte



                        //step 1 prendere il totale per quella persona



                            //Read content i dati del singolo utente
                            databaseReference5 = FirebaseDatabase.getInstance().getReference("Users").child(mSenderId).child("Groups").child(mGroupId).child("Users");

                            databaseReference5.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    bilanciosingolo = (Double) dataSnapshot.child(mReceiverId).child("Total").getValue(Double.class);


                                    if (bilanciosingolo == null) {
                                        bilanciosingolo = 0.0;
                                    }


                                    Log.d("SINGOLO", " bilancio singolo : " + bilanciosingolo);

                                    //step 2 aggiornalo
                                    Double tmp = bilanciosingolo + Total;
                                    FirebaseDatabase.getInstance().getReference("Users").child(mSenderId).child("Groups").child(mGroupId)
                                            .child("Users").child(mReceiverId).child("Total").setValue(tmp);


                                    Log.d("SINGOLO", " bilancio aggiornato : " + tmp);


                                    //databaseReference6.child("Users").child(id_owner).child("Total").setValue(bilanciosingolo-Total);

                                    //DEVO FARE L'INVERSO
                                    //DEVO SETTARE A ROBERTO L'OPPOSTO bilanciosingolo+Total

                                    tmp = -tmp;
                                    FirebaseDatabase.getInstance().getReference("Users").child(mReceiverId).child("Groups")
                                            .child(mGroupId).child("Users").child(mSenderId).child("Total").setValue(tmp);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });


                            ///////////// FINE BILANCIO singolo




                    //////////
                    ///////////
                    //// FINE DB
                    //////////
                    //////////




    }


















}
