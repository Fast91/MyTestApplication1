package com.example.faust.mytestapplication1;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by faust on 05/05/2017.
 */

public class DBShortKeys
{
    private Double global_balance,group_balance;
    private int group_mycount,group_totcount,global_mycount,global_totcount;
    private Double personal_balance;
    private int personal_mycount, personal_totcount;

    public static void aggiornaBilancioGlobale(final String id_user)
    {
        new DBShortKeys()._aggiornaBilancioGlobale(id_user);
    }

    public static void aggiornaBilancioGruppo(final String id_user, final String id_group)
    {
        new DBShortKeys()._aggiornaBilancioGruppo(id_user, id_group);
    }

    public static void aggiornaBilanciFraUtentiGruppo(final String id_user, final String id_other, final String id_group)
    {
        new DBShortKeys()._aggiornaBilanciFraUtentiGruppo(id_user, id_other, id_group);
    }

    public static void eliminaAttività(final String id_activity)
    {
        new DBShortKeys()._eliminaAttività(id_activity);
    }


    private void _eliminaAttività(final String id_activity)
    {
        // RICORDARSI ALLA FINE DI RICHIAMARE I 3 METODI PER RICALCOLARE I BILANCI
        final DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("Activities").child(id_activity);
        ref0.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // DEVO ELIMINARE L'ATTIVITA' DAL GRUPPO, DA TUTTI GLI USERS E DALL'OWNER
                // ottengo quindi una lista con tutti gli utenti (e owner) e ottengo l'id del gruppo
                Map<String, Object> map = new HashMap<String, Object>(); // qui metto le query
                map.put("/Activities/"+id_activity,null);

                final String group_id;
                final List<String> users_and_owner = new ArrayList<String>();

                group_id = dataSnapshot.child("Group").getValue(String.class);
                // ora group_id contiene l'id del gruppo dal quale eliminare l'attività
                map.put("/Groups/"+group_id+"/Activities/"+id_activity, null);

                users_and_owner.add(dataSnapshot.child("Owner").getValue(String.class));
                for(DataSnapshot data : dataSnapshot.child("Users").getChildren())
                {
                    users_and_owner.add(data.getValue(String.class));
                }
                // ora users_and_owner contiene gli id degli utenti dai quali eliminare l'attività

                for(String id : users_and_owner)
                {
                    map.put("/Users/"+id+"/Activities/"+id_activity,null);
                }

                DatabaseReference refRoot = FirebaseDatabase.getInstance().getReference();
                refRoot.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        for(String id : users_and_owner)
                        {
                            new DBShortKeys()._aggiornaBilancioGlobale(id);
                            new DBShortKeys()._aggiornaBilancioGruppo(id, group_id);
                            for(String id2 : users_and_owner)
                            {
                                new DBShortKeys()._aggiornaBilanciFraUtentiGruppoHALF(id, id2, group_id);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void _aggiornaBilancioGlobale(final String id_user) {

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

                                        Double amount = dataSnapshot.child("Total").getValue(Double.class);
                                        String s3x = String.format("%.2f", amount);
                                        s3x = s3x.replace(",", ".");
                                        amount = Double.parseDouble(s3x);


                                        total = dataSnapshot.child("Owner").child(id_user).child("Total").getValue(Double.class);



                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);
                                            s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);

                                            global_balance = global_balance  - total;

                                        }
                                        else{
                                            s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);
                                            global_balance = global_balance  + (amount-total);


                                        }




                                        if (global_mycount == global_totcount) {
                                            //ho finito

                                            s3x = String.format("%.2f", global_balance);
                                            s3x = s3x.replace(",", ".");
                                            global_balance = Double.parseDouble(s3x);

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


    private void _aggiornaBilancioGruppo(final String id_user, final String id_group)
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

                                        Double amount = dataSnapshot.child("Total").getValue(Double.class);
                                        String s3x = String.format("%.2f", amount);
                                        s3x = s3x.replace(",", ".");
                                        amount = Double.parseDouble(s3x);


                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(id_user).child("Total").getValue(Double.class);
                                            s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);

                                            group_balance = group_balance  - total;

                                        }
                                        else{

                                            s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);

                                            group_balance = group_balance  +( amount- total) ;
                                        }


                                        if (group_mycount == group_totcount) {

                                            //ho finito

                                             s3x = String.format("%.2f", group_balance);
                                            s3x = s3x.replace(",", ".");
                                            group_balance = Double.parseDouble(s3x);

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


    private void _aggiornaBilanciFraUtentiGruppo(final String id_user, final String id_other, final String id_group)
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

                                            String s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);


                                            //////////
                                            if(dataSnapshot.child("Owner").child(id_other).exists())
                                            {
                                                s3x = String.format("%.2f", total);
                                                s3x = s3x.replace(",", ".");
                                                total = Double.parseDouble(s3x);
                                                personal_balance = personal_balance  - total;
                                            }
                                            //////////
                                        }
                                        else{

                                            // io sono owner

                                            total = dataSnapshot.child("Users").child(id_other).child("Total").getValue(Double.class);
                                            String s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);
                                                personal_balance = personal_balance  + total;

                                            ////////

                                        }


                                        if (personal_mycount == personal_totcount) {

                                            //ho finito


                                            String s3x = String.format("%.2f", personal_balance);
                                            s3x = s3x.replace(",", ".");
                                            personal_balance = Double.parseDouble(s3x);

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


    private void _aggiornaBilanciFraUtentiGruppoHALF(final String id_user, final String id_other, final String id_group)
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

                                            String s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);


                                            //////////
                                            if(dataSnapshot.child("Owner").child(id_other).exists())
                                            {
                                                s3x = String.format("%.2f", total);
                                                s3x = s3x.replace(",", ".");
                                                total = Double.parseDouble(s3x);
                                                personal_balance = personal_balance  - total;
                                            }
                                            //////////
                                        }
                                        else{

                                            // io sono owner

                                            total = dataSnapshot.child("Users").child(id_other).child("Total").getValue(Double.class);
                                            String s3x = String.format("%.2f", total);
                                            s3x = s3x.replace(",", ".");
                                            total = Double.parseDouble(s3x);
                                            personal_balance = personal_balance  + total;

                                            ////////

                                        }


                                        if (personal_mycount == personal_totcount) {

                                            //ho finito


                                            String s3x = String.format("%.2f", personal_balance);
                                            s3x = s3x.replace(",", ".");
                                            personal_balance = Double.parseDouble(s3x);

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id_user).child("Groups").child(id_group).child("Users").child(id_other).child("Total").setValue(personal_balance);
                                            //FirebaseDatabase.getInstance().getReference().child("Users").child(id_other).child("Groups").child(id_group).child("Users").child(id_user).child("Total").setValue(-personal_balance);
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
