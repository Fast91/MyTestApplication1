package com.example.faust.mytestapplication1;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by robertospaziani on 06/05/17.
 */

public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService{
    private FirebaseAuth firebaseAuth;


    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        firebaseAuth = FirebaseAuth.getInstance();

        sendRegistrationToServer(refreshedToken);


    }


    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server

        if(firebaseAuth.getCurrentUser() != null){
      FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
              .child("Token").setValue(token);

             }
    }



}
