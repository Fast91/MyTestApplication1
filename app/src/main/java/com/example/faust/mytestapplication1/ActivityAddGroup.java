package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ActivityAddGroup extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth firebaseAuth;
    private String id_group, name_group;
    private DatabaseReference databaseReference, databaseReference2;
    private ImageButton buttonGallery, buttonCamera, buttonDelete;
    ProgressDialog mProgressDialog ;
    private StorageReference mStorage;
    private Uri downloadUri;
    private ImageView image_activity;

    private static final int GALLERY_INTENT = 2, CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        firebaseAuth = FirebaseAuth.getInstance();


        buttonDelete= (ImageButton)  findViewById(R.id.buttonDeleteImage_activity_group);

        buttonDelete.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.delete480x385, 100, 100));


        buttonCamera= (ImageButton)  findViewById(R.id.buttonPhoto_activity_group);

        buttonCamera.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.icon_camera128x128, 100, 100));

        buttonCamera.setOnClickListener(this);

        buttonGallery = (ImageButton)  findViewById(R.id.buttonGallery_activity_group);
        buttonGallery.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.picture_attachment256x256, 100, 100));


        buttonGallery.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        image_activity = (ImageView) findViewById(R.id.imagePicture_activity_group);

        Button submitGroup = (Button) findViewById(R.id.buttonSubmit_activity_group);
        mProgressDialog = new ProgressDialog(this);

        submitGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                EditText editTextName = (EditText) findViewById(R.id.nameGroup_actitivty_expense);
                name_group = editTextName.getText().toString();

                if (!name_group.equals("")) {





                    //////////////////
                    /////
                    ///// DB todo io metterei già il gruppo e come unico utente chi lo ha creato
                    /////
                    /////////////////


                    ///STEP 1
                    /// Group - ID - name - image - users

                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
                    //genero nuovo id per il gruppo
                    if(id_group==null){
                        id_group = databaseReference.push().getKey();
                    }



                    //setto il nome
                    databaseReference.child(id_group).child("Name").setValue(name_group);







                    //setto l'utente loggato prima mi serve il suo NOME
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

                      //Read content data
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            String name_user = dataSnapshot.child("Name").getValue(String.class);

                            //setto il nome all'utente loggato
                            databaseReference.child(id_group).child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Name").setValue(name_user);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                    //// STEP 2
                    // USERS - ID loggato - GROUPS - ID - NAME - TOTAL 0

                    databaseReference2.child("Groups").child(id_group).child("Name").setValue(name_group);

                    Double tot=0.0;
                    databaseReference2.child("Groups").child(id_group).child("Total").setValue(tot);

                    //////////////////
                    /////
                    ///// FINE DB
                    /////
                    /////////////////

                    //step 0 mandare all'altra attività id group e name group


                    Intent intent = new Intent(ActivityAddGroup.this, ActivityAddUserToGroup.class);
                    intent.putExtra("ID_GROUP",id_group);
                    intent.putExtra("NAME_GROUP",name_group);
                    startActivity(intent);
                    finish();


                    return;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.toast_emptyaddexpense, Toast.LENGTH_LONG).show();


                    return;
                }


            }

        });







    }







    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.backgroup_title)
                .setMessage(R.string.backgroup_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {


                        //Remove activity
                        if(id_group!=null) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("Groups").child(id_group).removeValue();
                        }




                        Intent intent=new Intent(ActivityAddGroup.this,MainActivity.class);
                        ActivityAddGroup.this.startActivity(intent);

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

                        if (id_group == null) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
                            id_group = databaseReference.push().getKey();
                        }


                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Groups")
                                .child(id_group)
                                .child("Image");
                        ref.setValue(downloadUri.toString());


                        // Picasso.with(ReadProfileActivity.this).load(downloadUri).fit().centerCrop().into(image_profile);

                        Toast.makeText(ActivityAddGroup.this, R.string.upload_ok, Toast.LENGTH_LONG).show();
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
                            Picasso.with(ActivityAddGroup.this)
                                    .load(downloadUri.toString())
                                    .fit()
                                    .centerCrop()
                                    .into(image_activity);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ActivityAddGroup.this, R.string.upload_no, Toast.LENGTH_LONG).show();
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

                if (id_group == null) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
                    id_group = databaseReference.push().getKey();
                }

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Groups")
                        .child(id_group)
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
