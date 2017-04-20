package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robertospaziani on 25/03/17.
 */

public class ActivityListFragment extends  Fragment  {



        private FirebaseAuth firebaseAuth;
        private MyActivityRecyclerViewAdapter adapter;
        private int mColumnCount = 1;
        View view;


        private List<MyActivity> activities;

        public ActivityListFragment() {
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //FIREBASE
            //initializing firebase authentication object
            firebaseAuth = FirebaseAuth.getInstance();




        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
            final  AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();
            final TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
            String name= getString(R.string.global_string);
            namegroup.setText(name);

            final TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);
            moneygroup.setText("100€");



            ////////////////
            //// DATABASE
            ///////////////


            //Bilancio Globale
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("GlobalBalance");

            Double  bilancioGlobale;

            //Read content data
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Double bilancioGlobale = (Double) dataSnapshot.getValue(Double.class);


                    if (bilancioGlobale == null) {
                        bilancioGlobale = 0.0;
                    }

                    //Bilancio
                    ((TextView) myactivity.findViewById(R.id.row1_text2)).setText((String.format("%.2f", bilancioGlobale)+"€"));






                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


            /////////////////
            /// Lista di activities per l'user autenticato con il rispettivo dovuto
            //////////////




            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Activities");

            //Read content data
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    HashMap<String, NomeDovuto> attività_dovuto = new HashMap<>();

                    //Prendo tutte le attività
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                      String id = (String) postSnapshot.getKey();
                        String nome = (String) postSnapshot.child("Name").getValue(String.class);
                        Double dovuto = (Double) postSnapshot.child("Total").getValue(Double.class);
                        String category = (String) postSnapshot.child("Category").getValue(String.class);
                        String name_group = (String) postSnapshot.child("Group").getValue(String.class);


                        //id per l'attività è univoco


                        //add
                        NomeDovuto iniziale = new NomeDovuto(nome, dovuto);
                        iniziale.setId(id);
                        iniziale.setCategory(category);
                        iniziale.setName_Group(name_group);
                        attività_dovuto.put(id, iniziale);


                    }


                    List list = new ArrayList(attività_dovuto.values());


                    // Set the adapter
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view;


                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }

                        adapter = new MyActivityRecyclerViewAdapter(list);//activity);
                        recyclerView.setAdapter(adapter);
                    } else {

                        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.activity_list);
                        //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
                        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new MyActivityRecyclerViewAdapter(list);//activity);
                        recyclerView2.setAdapter(adapter);

                    }

                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






            return view;
        }









    }



