package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.*;

public class PartPaymentActivity extends AppCompatActivity {

    private String mSenderId;
    private String mGroupId;
    private FirebaseAuth firebaseAuth;
    private String mReceiverId;
    private String mDefaultAmount;
    private String mExtraPaymentReceiver;
    private String mExtraPaymentGroup;
    private DatabaseReference databaseReference;

    private String name_sender, name_receiver;
    private TextView tvsender, tvreceiver, tvamount;

    private static final String EXTRA_PAYMENT_USER = ".extra_payment_user";
    private static final String EXTRA_PAYMENT_GROUP = ".extra_payment_group";
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_payment);

        firebaseAuth = FirebaseAuth.getInstance();


        mExtraPaymentReceiver = (String) getIntent().getStringExtra(EXTRA_PAYMENT_USER); // questo è il nome
        mExtraPaymentGroup = (String) getIntent().getStringExtra(EXTRA_PAYMENT_GROUP);
        mSenderId = (String) firebaseAuth.getCurrentUser().getUid();
        mGroupId = (String) getIntent().getStringExtra("ID_GROUP");
        mDefaultAmount =(String) getIntent().getStringExtra("DOVUTO"); //dovuto con stringa
        mDefaultAmount = mDefaultAmount.replace(",",".");
        mDefaultAmount= mDefaultAmount.replace("€","");
        mReceiverId = (String) getIntent().getStringExtra("ID_USER");

        submit = (Button) findViewById(R.id.button_pagatoda_expense);





        tvsender = (TextView) findViewById(R.id.text_name_sender);
        tvamount = (TextView) findViewById(R.id.amount_deve);
        tvreceiver = (TextView) findViewById(R.id.text_name_receiver);


        name_receiver = mExtraPaymentReceiver;


        com.makeramen.roundedimageview.RoundedImageView b4add = (com.makeramen.roundedimageview.RoundedImageView ) findViewById(R.id.image_payment_user);

        b4add.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.exchange, 100, 100));


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Name");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_sender = dataSnapshot.getValue(String.class);


                //Devo settare i testi
                //name_sender sarei io

                //Controllare se il totale e' negativo
                //allora inverto tutto cioè sender e receiver
                //mettendo il totale positivo

                Double tmp = Double.parseDouble( mDefaultAmount );

                if(tmp>0 && tmp!=0){

                    String x_id = mSenderId;
                    String x_name = name_sender;
                    mSenderId = mReceiverId;
                    name_sender = name_receiver;
                    mReceiverId = x_id;
                    name_receiver = x_name;

                }
                else{
                    tmp=-tmp;
                    mDefaultAmount=mDefaultAmount.replace("-","");
                }

                //Adesso posso settare tutto

                tvsender.setText(name_sender);
                tvamount.setText(mDefaultAmount+"€");
                tvreceiver.setText(name_receiver);



              //Devo settare onClick qui

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(PartPaymentActivity.this,PaymentActivity.class);
                        i.putExtra("ID_GROUP",mGroupId);
                        i.putExtra("DOVUTO",mDefaultAmount);
                        i.putExtra("ID_USER_RECEIVER",mReceiverId);
                        i.putExtra("ID_USER_SENDER",mSenderId);
                        i.putExtra("NAME_USER_RECEIVER",name_receiver);
                        i.putExtra("NAME_USER_SENDER",name_sender);
                        startActivity(i);
                        finish();


                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    public static Intent newIntent(Context packageContext, String user, String group)
    {
        Intent i = new Intent(packageContext, PartPaymentActivity.class);
        i.putExtra(EXTRA_PAYMENT_USER, user);
        i.putExtra(EXTRA_PAYMENT_GROUP, group);
        return i;
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
