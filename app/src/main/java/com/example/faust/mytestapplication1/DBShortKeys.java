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
    private int group_mycount,group_totcount,global_mycount,global_totcount;
    private Double personal_balance;
    private int personal_mycount, personal_totcount;


    private void AggiornaBilancioGlobale(final String id_user) {

        global_balance = 0.0;
        global_mycount=0;
        global_totcount=0;



        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id_user).child("Activities");

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {


                } else {


                    global_totcount = (int) dataSnapshot.getChildrenCount();


                    for (DataSnapshot databaseSnapshot1 : dataSnapshot.getChildren()) {


                        FirebaseDatabase.getInstance().getReference("Activities").child(databaseSnapshot1.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        global_mycount++;


                                        Double total = null;


                                        total = dataSnapshot.child("Owner").child(id_user).child("Total").getValue(Double.class);


                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);

                                            global_balance = global_balance  - total;

                                        }
                                        else{
                                            global_balance = global_balance  + total;
                                        }




                                        if (global_mycount == global_totcount) {
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





    private void AggiornaBilancioGruppo(final String id_user, final String id_group)
    {
        group_balance = 0.0;
        group_mycount=0;
        group_totcount=0;

        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group);

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {


                } else {


                    group_totcount = (int) dataSnapshot.child("Activities").getChildrenCount();


                    //Per ogni attivita
                    for (DataSnapshot postSnapshot : dataSnapshot.child("Activities").getChildren()) {


                        final String id = (String) postSnapshot.getKey();
                        final String nome = (String) postSnapshot.child("Name").getValue(String.class);
                        //category = (String) postSnapshot.child("Category").getValue(String.class); //todo inserire categoria nel DB groups




                        FirebaseDatabase.getInstance().getReference("Activities").child(postSnapshot.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        group_mycount++;

                                        Double total = null;


                                        total = dataSnapshot.child("Owner").child(id_user).child("Total").getValue(Double.class);


                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);

                                            group_balance = group_balance  - total;

                                        }
                                        else{
                                            group_balance = group_balance  + total;
                                        }


                                        if (group_mycount == group_totcount) {

                                            //ho finito

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id_user).child("Groups").child(id_group).child("Total").setValue(group_balance);
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




    private void AggiornaBilanciFraUtentiGruppo(final String id_user, final String id_other, final String id_group)
    {
        personal_balance = 0.0;
        personal_mycount=0;
        personal_totcount=0;

        final DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group);

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {


                } else {


                    personal_totcount = (int) dataSnapshot.child("Activities").getChildrenCount();


                    //Per ogni attivita
                    for (DataSnapshot postSnapshot : dataSnapshot.child("Activities").getChildren()) {


                        final String id = (String) postSnapshot.getKey();
                        final String nome = (String) postSnapshot.child("Name").getValue(String.class);
                        //category = (String) postSnapshot.child("Category").getValue(String.class); //todo inserire categoria nel DB groups




                        FirebaseDatabase.getInstance().getReference("Activities").child(postSnapshot.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        personal_mycount++;

                                        Double total = null;


                                        total = dataSnapshot.child("Owner").child(id_user).child("Total").getValue(Double.class);


                                        if (total == null) {

                                            // io sono utente
                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);


                                            //////////
                                            if(dataSnapshot.child("Owner").getKey().equals(id_other))
                                            {
                                                personal_balance = personal_balance  - total;
                                            }
                                            //////////
                                        }
                                        else{

                                            // io sono owner

                                            /////////
                                            //if(dataSnapshot.child("User").getKey().equals(id_other))
                                            //{
                                            total = dataSnapshot.child("Users").child(id_other).child("Total").getValue(Double.class);
                                                personal_balance = personal_balance  + total;
                                            //}
                                            ////////

                                        }


                                        if (personal_mycount == personal_totcount) {

                                            //ho finito

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id_user).child("Groups").child(id_group).child("Users").child(id_other).child("Total").setValue(personal_balance);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id_other).child("Groups").child(id_group).child("Users").child(id_user).child("Total").setValue(-personal_balance);
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
