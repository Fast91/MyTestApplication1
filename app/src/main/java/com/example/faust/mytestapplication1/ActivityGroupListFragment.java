package com.example.faust.mytestapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.StringTokenizer;

/**
 * Created by robertospaziani on 26/03/17.
 */

public class ActivityGroupListFragment extends Fragment {


    private MyActivityGroupRecyclerViewAdapter adapter;
    private int mColumnCount = 1;


    String id_group;
    private FirebaseAuth firebaseAuth;
    private HashMap<String,NomeDovuto> attivita_dovuto;


    public ActivityGroupListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();

        id_group= b.getString("GROUP_ID");

        firebaseAuth = FirebaseAuth.getInstance();

         attivita_dovuto= new HashMap<>();



    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_activity_group_list, container, false);




        ///////////////
        //database
        ///////////////

        ////Ricerca ID per quel Nome che sarebbe GROUP_ID il nome
        ///// Prendere Tutti gli utenti e settare id_namegroup il vero ID del gruppo utile per il prossimo frammento

        DatabaseReference databaseReference;


        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Activities");

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                //Per ogni attivita
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    String id = (String) postSnapshot.getKey();
                    String nome = (String) postSnapshot.child("Name").getValue(String.class);
                    Double dovuto = (Double) postSnapshot.child("Total").getValue(Double.class);


                        NomeDovuto iniziale = new NomeDovuto(nome, dovuto);
                        attivita_dovuto.put(id, iniziale);



                }


                //Adapter

                // Set the adapter
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;


                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }
                    List list = new ArrayList(attivita_dovuto.values());
                    adapter = new MyActivityGroupRecyclerViewAdapter(list);
                    recyclerView.setAdapter(adapter);
                }
                else {

                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.activity_group_list);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    List list = new ArrayList(attivita_dovuto.values());
                    adapter = new MyActivityGroupRecyclerViewAdapter(list);
                    recyclerView2.setAdapter(adapter);
                }




                    //End adapter



            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


















        final  AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();


        //Settare il valore della riga li sopra con nome del gruppo ecc
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
                namegroup.setText(dataSnapshot.child("Name").getValue(String.class));
                TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);

                moneygroup.setText(dataSnapshot.child("Total").getValue(Double.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






        final ImageButton bGlobal = (ImageButton) myactivity.findViewById(R.id.bGlobal);
        final ImageButton bGroups = (ImageButton) myactivity.findViewById(R.id.bGroups);
        final ImageButton bActivities = (ImageButton) myactivity.findViewById(R.id.bActivities);
        bGlobal.setBackgroundResource(R.drawable.buttonshape);
        bGroups.setBackgroundResource(R.drawable.buttonshape);
        bActivities.setBackgroundResource(R.drawable.buttonshape);


        return view;
    }





}
