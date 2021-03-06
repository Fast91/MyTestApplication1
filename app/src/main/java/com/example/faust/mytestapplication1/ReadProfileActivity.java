package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.*;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReadProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private Uri downloadUri;

    private StorageReference mStorage;

    private TextView textName , textSurname, textEmail;
    private ImageButton buttonModify, buttonCamera;

    private static final int GALLERY_INTENT = 2, CAMERA_REQUEST_CODE = 1;

    private String id ;

    ProgressDialog mProgressDialog, mProgressDialog2 ,mProgressDialog3;
     private com.makeramen.roundedimageview.RoundedImageView image_profile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_profile);


        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog2 = new ProgressDialog(this);
        mProgressDialog3 = new ProgressDialog(this);
        image_profile = (com.makeramen.roundedimageview.RoundedImageView ) findViewById(R.id.image_profile_show);
        image_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);


        String msg = getString(R.string.dialog_image_profile_loading);
        mProgressDialog2.setMessage(msg);
        mProgressDialog2.show();

        mProgressDialog3.setMessage(msg);




        //if the user is not logged in
        //that means current user will return null

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
        else{
            id=firebaseAuth.getCurrentUser().getUid();
        }

        String id_profile=null;
        Intent intent = getIntent();

        if( intent.getExtras().getString("MY_PROFILE").equals("NO") ){
             id_profile = intent.getExtras().getString("PROFILE_ID");


        }

        if(id_profile!=null){
            id=id_profile;
        }




        getandSetImage();



        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id);



        textName = (TextView) findViewById(R.id.name_real_profile);
        textSurname = (TextView) findViewById(R.id.surname_real_profile);
        textEmail = (TextView) findViewById(R.id.email_real_profile);

        if(id_profile==null) {
        buttonModify = (ImageButton) findViewById(R.id.edit_profile_button);
        buttonModify.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.picture_attachment256x256, 100, 100));

            buttonModify.setOnClickListener(this);
        }

        if(id_profile==null) {
        buttonCamera = (ImageButton) findViewById(R.id.edit_profile_button_camera);
        buttonCamera.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.icon_camera128x128, 100, 100));

            buttonCamera.setOnClickListener(this);
        }


        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                UserInformation userInformation = ( UserInformation) dataSnapshot.getValue(UserInformation.class);

            String Name=(String)dataSnapshot.child("Name").getValue(String.class);
                String Surname=(String)dataSnapshot.child("Surname").getValue(String.class);




                if(Name==null&&Surname==null) {
                    textName.setText(R.string.prompt_name_profile);
                    textSurname.setText(R.string.prompt_surname_profile);
                    textEmail.setText("Email");
                }
                else{
                    textName.setText(Name);
                    textSurname.setText(Surname);
                    textEmail.setText(dataSnapshot.child("Email").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }



    @Override
    public void onClick(View v) {

        if(v == buttonModify){


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

         Uri    uri = data.getData();


            StorageReference filepath  = mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                @SuppressWarnings("VisibleForTests")
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                   downloadUri = taskSnapshot.getDownloadUrl();


                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Image");
                    ref.setValue(downloadUri.toString());



                   // Picasso.with(ReadProfileActivity.this).load(downloadUri).fit().centerCrop().into(image_profile);

                    Toast.makeText(ReadProfileActivity.this, R.string.upload_ok ,Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();



                    if (!downloadUri.toString().contains("http")) {
                        try {
                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(downloadUri.toString());
                            image_profile.setImageBitmap(imageBitmaptaken);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // This block of code should already exist, we're just moving it to the 'else' statement:
                        Picasso.with(ReadProfileActivity.this)
                                .load(downloadUri.toString())
                                .fit()
                                .centerCrop()
                                .into(image_profile);
                    }



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ReadProfileActivity.this, R.string.upload_no ,Toast.LENGTH_LONG).show();
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
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Image");
            ref.setValue(imageEncoded);


            if (!imageEncoded.contains("http")) {
                try {
                    Bitmap imageBitmaptaken = decodeFromFirebaseBase64(imageEncoded);
                    image_profile.setImageBitmap(imageBitmaptaken);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // This block of code should already exist, we're just moving it to the 'else' statement:
                Picasso.with(this)
                        .load(imageEncoded)
                        .fit()
                        .centerCrop()
                        .into(image_profile);
            }




        }







    }



/*
    @Override
    public void onBackPressed() {

                        Intent intent=new Intent(ReadProfileActivity.this,MainActivity.class);
                        ReadProfileActivity.this.startActivity(intent);
                        finish();



    }*/




    private void getandSetImage() {

        //getImage of user

        mProgressDialog3.show();

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Image");

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
                            image_profile.setImageBitmap(imageBitmaptaken);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {


                        Picasso.with(ReadProfileActivity.this)
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(image_profile);






                        // Bitmap imageBitmaptaken = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                        // Bitmap imageCirle = getclip(imageBitmaptaken);
                        // profile_image.setImageBitmap(imageCirle);



                    }



                }
                else
                {
                    image_profile.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.realphoto,100,100));
                }


                mProgressDialog2.dismiss();
                mProgressDialog3.dismiss();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                image_profile.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.realphoto,100,100));

            }
        });






    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

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
