package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class ActivityDetailActivity extends AppCompatActivity
{
    private static final String EXTRA_EXPENSE_UUID = ".extra_expense_uuid"; //"com.example.faust.mytestapplication1.extra_expense_uuid";

    private TextView mTitleTextView;
    private ImageView mImageView;
    private TextView mGroupTextView;
    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mCategoryTextView;

    private FirebaseAuth firebaseAuth;

    private String mExpenseId;

    public static Intent newIntent(Context packageContext, String id)
    {
        Intent i = new Intent(packageContext, ActivityDetailActivity.class);
        i.putExtra(EXTRA_EXPENSE_UUID, id);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        firebaseAuth = FirebaseAuth.getInstance();
        mExpenseId = (String) getIntent().getStringExtra(EXTRA_EXPENSE_UUID);
        //TODO cercare l'uuid nel db per prendere i dati

        mTitleTextView = (TextView) findViewById(R.id.title_expense);
        mImageView = (ImageView) findViewById(R.id.imageDetailPicture);
        mGroupTextView = (TextView) findViewById(R.id.Group_expense);
        mDateTextView = (TextView) findViewById(R.id.Date_expense);
        mAmountTextView = (TextView) findViewById(R.id.Total_expense);
        mCategoryTextView = (TextView) findViewById(R.id.Category_expense);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_detail_fragment);

        if(fragment == null)
        {
            fragment = new ActivityDetailFragment();
            Bundle args = new Bundle();
            args.putString("expense_id", mExpenseId);
            fragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.activity_detail_fragment, fragment)
                    .commit();
        }

        mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.giftgreen));

        mImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        getandSetImage();


        /////////////////////////
        //////
        ////// DBB
        ///////////////////////////
        //////////////////////////


/*
        mImageView = (ImageView) findViewById(R.id.imageDetailPicture);
        mGroupTextView = (TextView) findViewById(R.id.Group_expense);
        mDateTextView = (TextView) findViewById(R.id.Date_expense);
        mAmountTextView = (TextView) findViewById(R.id.Total_expense);
        mCategoryTextView = (TextView) findViewById(R.id.Category_expense);
        mDescriptionTextView = (TextView) findViewById(R.id.Description_expense); */













        /////////////////////////
        //////
        ////// fine  DBB
        ///////////////////////////
        //////////////////////////



    }









    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Activities").child(mExpenseId).child("Image");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //if != null SET

                final String image = dataSnapshot.getValue(String.class);

                if (image != null) {

                    if (!image.contains("http")) {
                        try {
                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(image);
                            //Bitmap imageCirle = getclip(imageBitmaptaken);
                            mImageView.setImageBitmap(imageBitmaptaken);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {


                        Picasso.with(ActivityDetailActivity.this)
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(mImageView);




                        // Bitmap imageBitmaptaken = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                        // Bitmap imageCirle = getclip(imageBitmaptaken);
                        // profile_image.setImageBitmap(imageCirle);



                    }

                    mImageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ActivityDetailActivity.this, FullScreenImage.class);

                            mImageView.buildDrawingCache();
                            Bitmap image2= mImageView.getDrawingCache();

                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image2);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                    });


                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }









}
