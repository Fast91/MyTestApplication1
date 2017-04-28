package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mEmailView , mNameView, mSurnameView;
    private EditText mPasswordView;
    private ImageButton button;
    private View mLoginFormView;
    private ProgressDialog myprogressBar;
    private FirebaseAuth firebaseauth;
    private ImageView mLogoImage;
    String email, name, surname;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        firebaseauth=FirebaseAuth.getInstance();

        if(firebaseauth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity

            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            startActivity(new Intent(getApplicationContext(), PrimaAttivitaGruppi.class));
        }


        mLogoImage=(ImageView) findViewById(R.id.image_register_activity_logo);


        mLogoImage.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.appname, 100, 100));

         button=(ImageButton)findViewById(R.id.imageButtonLogin);


        button.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.register256x256, 100, 100));

         mEmailView=(EditText) findViewById(R.id.email_register);
         mPasswordView=(EditText) findViewById(R.id.password_register);
        mNameView=(EditText) findViewById(R.id.name_register);
        mSurnameView=(EditText) findViewById(R.id.surname_register);
        mLoginFormView=findViewById(R.id.textview_alreadyregistered);
        myprogressBar=new ProgressDialog(this);

        button.setOnClickListener(this);
        mLoginFormView.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        if(v == button){

            registerUser();



        }
        if(v==mLoginFormView){


            startActivity(new Intent(this, LoginActivity.class));


        }


    }

    private void registerUser() {

        email= mEmailView.getText().toString().trim();
        String password=mPasswordView.getText().toString().trim();
         name = mNameView.getText().toString().trim();
         surname = mSurnameView.getText().toString().trim();

        if(email.isEmpty()){

            Toast.makeText(this,R.string.enter_email,Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){

            Toast.makeText(this,R.string.enter_password,Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.isEmpty() || surname.isEmpty()){

            Toast.makeText(this,R.string.toast_emptyaddexpense,Toast.LENGTH_SHORT).show();
            return;

        }

        String s=(String)getString(R.string.progress_bar_register);
        myprogressBar.setMessage(s);
        myprogressBar.show();

        firebaseauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(RegisterActivity.this,R.string.successful_registration,Toast.LENGTH_SHORT).show();
                    FirebaseUser fireuser = firebaseauth.getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child(fireuser.getUid()).child("Email").setValue(email);

                    //setName
                    String Name = new String(name);
                    databaseReference.child("Users").child(fireuser.getUid()).child("Name").setValue(Name);

                    //setSurname
                    String Surname = new String(surname);
                    databaseReference.child("Users").child(fireuser.getUid()).child("Surname").setValue(Surname);


                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
                else{

                    Toast.makeText(RegisterActivity.this,R.string.unsuccessful_registration,Toast.LENGTH_SHORT).show();


                }
                myprogressBar.dismiss();

            }
        });

    }




    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.backmain_title)
                .setMessage(R.string.backmain_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ////intent.putExtra("ID_USER",firebaseAuth.getCurrentUser().getUid());
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }).create().show();
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
    
