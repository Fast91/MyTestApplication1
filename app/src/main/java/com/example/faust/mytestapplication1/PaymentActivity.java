package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PaymentActivity extends AppCompatActivity {
    private static final String EXTRA_PAYMENT_USER = ".extra_payment_user";
    private static final String EXTRA_PAYMENT_GROUP = ".extra_payment_group";
    private String mExtraPaymentReceiver;
    private String mExtraPaymentGroup;
    //TODO SOSTITUIRE IL PAYMENT ID (ED EXTRA_PAYMENT_UUID) CON LE ROBE QUA SOTTO
    private String mReceiverId;
    private String mSenderId;
    private String mGroupId;
    private String mActivityId;
    private String mDefaultAmount;
    private FirebaseAuth firebaseAuth;
    //TODO SOSTITUIRE IL PAYMENT ID CON QUALCOSA DI PIU' CONCRETO PER FAR CAPIRE AL DB A CHI DOBBIAMO DARE I SOLDI E PER QUALI GRUPPI/ATTIVITA'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mExtraPaymentReceiver = (String) getIntent().getStringExtra(EXTRA_PAYMENT_USER);
        mExtraPaymentGroup = (String) getIntent().getStringExtra(EXTRA_PAYMENT_GROUP);

        firebaseAuth = FirebaseAuth.getInstance();


        mSenderId = (String) firebaseAuth.getCurrentUser().getUid();
        mGroupId = (String) getIntent().getStringExtra("ID_GROUP");
        mDefaultAmount =(String) getIntent().getStringExtra("DOVUTO");
        mReceiverId = (String) getIntent().getStringExtra("ID_USER");

        String s = mSenderId + " " + mGroupId + " " + mDefaultAmount +" " + mReceiverId;

        Toast.makeText(this,s,Toast.LENGTH_LONG);




        //TODO cercare l'uuid nel db per prendere i dati

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.payment_fragment_container);

        if(fragment == null)
        {
            fragment = new PaymentFragment();
            Bundle args = new Bundle();
            args.putString("payment_receiver", mExtraPaymentReceiver);
            args.putString("payment_group", mExtraPaymentGroup);

            args.putString("ID_GROUP",mGroupId);
            args.putString("DOVUTO",mDefaultAmount);
            args.putString("ID_USER",mReceiverId);


            fragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.payment_fragment_container,fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, String user, String group)
    {
        Intent i = new Intent(packageContext, PaymentActivity.class);
        i.putExtra(EXTRA_PAYMENT_USER, user);
        i.putExtra(EXTRA_PAYMENT_GROUP, group);
        return i;
    }
}
