package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mEmailView;
    private EditText mPasswordView;
    private  Button button;
    private View mLoginFormView;
    private ProgressDialog myprogressBar;
    private FirebaseAuth firebaseauth;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        firebaseauth=FirebaseAuth.getInstance();

        if(firebaseauth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

         button=(Button)findViewById(R.id.email_sign_in_button);
         mEmailView=(EditText) findViewById(R.id.email);
         mPasswordView=(EditText) findViewById((R.id.password));
        mLoginFormView=findViewById(R.id.email_login_form);
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

       String email= mEmailView.getText().toString().trim();
        String password=mPasswordView.getText().toString().trim();

        if(email.isEmpty()){

            Toast.makeText(this,R.string.enter_email,Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){

            Toast.makeText(this,R.string.enter_password,Toast.LENGTH_SHORT).show();
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

}
    
