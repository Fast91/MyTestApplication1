package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReadProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    private Uri downloadUri;

    private StorageReference mStorage;

    private TextView textName , textSurname;
    private ImageButton buttonModify, buttonCamera;

    private static final int GALLERY_INTENT = 2, CAMERA_REQUEST_CODE = 1;

    ProgressDialog mProgressDialog, mProgressDialog2 ;
    ImageView image_profile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_profile);


        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog2 = new ProgressDialog(this);
        image_profile = (ImageView) findViewById(R.id.image_profile_show);


        String msg = getString(R.string.dialog_image_profile_loading);
        mProgressDialog2.setMessage(msg);
        mProgressDialog2.show();

        getandSetImage();

        //if the user is not logged in
        //that means current user will return null

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }



        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());



        textName = (TextView) findViewById(R.id.name_real_profile);
        textSurname = (TextView) findViewById(R.id.surname_real_profile);


        buttonModify = (ImageButton) findViewById(R.id.edit_profile_button);
        buttonModify.setOnClickListener(this);

        buttonCamera = (ImageButton) findViewById(R.id.edit_profile_button_camera);
        buttonCamera.setOnClickListener(this);



        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInformation userInformation = ( UserInformation) dataSnapshot.getValue(UserInformation.class);



                if(userInformation==null) {
                    textName.setText(R.string.prompt_name_profile);
                    textSurname.setText(R.string.prompt_surname_profile);
                }
                else{
                    textName.setText(userInformation.Name);
                    textSurname.setText(userInformation.Surname);
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

        Uri uri = data.getData();




        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            String msg = getString(R.string.dialog_image_profile);
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();

             uri = data.getData();


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




    @Override
    public void onBackPressed() {

                        Intent intent=new Intent(ReadProfileActivity.this,MainActivity.class);
                        ReadProfileActivity.this.startActivity(intent);
                        finish();



    }




    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Image");

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



                    mProgressDialog2.dismiss();
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
