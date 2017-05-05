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

    private Double global_balance,group_balance;
    private int mycount,totcount;



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








    private void AggiornaBilancioGlobale(final String id_user) {

        global_balance = 0.0;
        mycount=0;
        totcount=0;



        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id_user).child("Activities");

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {


                } else {


                    totcount = (int) dataSnapshot.getChildrenCount();


                    for (DataSnapshot databaseSnapshot1 : dataSnapshot.getChildren()) {


                        FirebaseDatabase.getInstance().getReference("Activities").child(databaseSnapshot1.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        mycount++;


                                        Double total = null;


                                        total = dataSnapshot.child("Owner").child(id_user).child("Total").getValue(Double.class);


                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);

                                            group_balance = global_balance  - total;

                                        }
                                        else{
                                            group_balance = global_balance  + total;
                                        }



                                        if (total == null) {


                                            total = 0.0;
                                        }



                                        if (mycount == totcount) {
                                            //ho finito

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id_user).child("GlobalBalance").setValue(global_balance);


                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }


                                });

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });







    }





    private void AggiornaBilancioGruppo(final String id_user, String id_group) {

        group_balance = 0.0;
        mycount=0;
        totcount=0;

        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group);

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {


                } else {


                    totcount = (int) dataSnapshot.child("Activities").getChildrenCount();


                    //Per ogni attivita
                    for (DataSnapshot postSnapshot : dataSnapshot.child("Activities").getChildren()) {


                        mycount++;


                        final String id = (String) postSnapshot.getKey();
                        final String nome = (String) postSnapshot.child("Name").getValue(String.class);
                        //category = (String) postSnapshot.child("Category").getValue(String.class); //todo inserire categoria nel DB groups




                        FirebaseDatabase.getInstance().getReference("Activities").child(postSnapshot.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        Double total = null;


                                        total = dataSnapshot.child("Owner").child(id_user).child("Total").getValue(Double.class);


                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);

                                            group_balance = global_balance  - total;

                                        }
                                        else{
                                            group_balance = global_balance  + total;
                                        }
                                        if (total == null) {
                                            total = 0.0;
                                        }




                                        if (mycount == totcount) {

                                            //ho finito

                                            databaseReference.child("Users").child(id_user).child("Total").setValue(group_balance);

                                        }



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }


                                });


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


             });


    }








}
