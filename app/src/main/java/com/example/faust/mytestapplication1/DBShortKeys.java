package com.example.faust.mytestapplication1;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by faust on 05/05/2017.
 */

public class DBShortKeys
{
    public static void updateUserBalance(String userID)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        Map<String, Object> activitiesMap = new HashMap<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Activities").exists())
                {
                    for(DataSnapshot activity : dataSnapshot.getChildren())
                    {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
