package com.example.faust.mytestapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnKeyListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by robertospaziani on 26/03/17.
 */

public class UsersGroupListFragment extends Fragment{


    private MyUsersGroupRecyclerViewAdapter adapter;

    private int mColumnCount=1;

    String id_group;
    private FirebaseAuth firebaseAuth;
    private HashMap<String,NomeDovuto> utenti_dovuto;

    public UsersGroupListFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();

        id_group= b.getString("GROUP_ID");

        firebaseAuth = FirebaseAuth.getInstance();



        utenti_dovuto= new HashMap<>();



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





      final  View view = inflater.inflate(R.layout.fragment_user_group_list, container, false);





        final  AppCompatActivity activity = (android.support.v7.app.AppCompatActivity) view.getContext();
        DatabaseReference databaseReference;



        ////////////////
        //database
        ///////////////

        ////Ricerca ID per quel Nome che sarebbe GROUP_ID il nome
        ///// Prendere Tutti gli utenti e settare id_namegroup il vero ID del gruppo utile per il prossimo frammento



        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                        //prendo gli amici
                        DataSnapshot friends = dataSnapshot.child("Users");


                        //prendo un amico
                        for (DataSnapshot friend : friends.getChildren()) {

                            String id = (String) friend.getKey();
                            String nome = (String) friend.child("Name").getValue(String.class);
                            Double dovuto = (Double) friend.child("Total").getValue(Double.class);


                            if (utenti_dovuto.get(id) == null || utenti_dovuto.containsKey(id) == false) {
                                //add
                                NomeDovuto iniziale = new NomeDovuto(nome, dovuto);
                                utenti_dovuto.put(id, iniziale);


                            } else {

                                //faccio il get e il replace
                                NomeDovuto iniziale = utenti_dovuto.get(id);
                                iniziale.setDovuto(iniziale.getDovuto() + dovuto);
                                utenti_dovuto.remove(id);
                                utenti_dovuto.put(id, iniziale);

                            }


                        }

                ///Adesso che ho la mia cazzo di lista bella piena
                //Posso settare gli elementi nell'adapter porca puttana eva
                ///

                // Set the adapter



                // Set the adapter

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;


                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }


                    List list = new ArrayList(utenti_dovuto.values());
                    adapter = new MyUsersGroupRecyclerViewAdapter(list);
                    recyclerView.setAdapter(adapter);
                }
                else{

                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.user_group_list);
                    //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    List list = new ArrayList(utenti_dovuto.values());
                    adapter = new MyUsersGroupRecyclerViewAdapter(list);
                    recyclerView2.setAdapter(adapter);

                    Button b5_showactivity = (Button) view.findViewById(R.id.b5_show_group_activity);
                    //Listener Button5 show group activity
                    b5_showactivity.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {



                            final  AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new ActivityGroupListFragment();
                            //Create a bundle to pass data, add data, set the bundle to your fragment and:
                            Bundle mBundle;
                            mBundle = new Bundle();
                            mBundle.putString("GROUP_ID",id_group);
                            // mBundle.putInt("GROUP_ID",item.getIdgroup());
                            myFragment.setArguments(mBundle);


                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();


                            for(int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }


                            return ;
                        }
                    });







                }
                 /////// END ADAPTER







                    }






            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });





        //Settare il valore della riga li sopra con nome del gruppo ecc
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                TextView namegroup = (TextView) activity.findViewById(R.id.row1_text1);
                namegroup.setText(dataSnapshot.child("Name").getValue(String.class));
                TextView moneygroup = (TextView) activity.findViewById(R.id.row1_text2);

                moneygroup.setText(dataSnapshot.child("Total").getValue(Double.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






        final  AppCompatActivity myactivity = (AppCompatActivity) view.getContext();
        final ImageButton bGlobal = (ImageButton) myactivity.findViewById(R.id.bGlobal);
        final ImageButton bGroups = (ImageButton) myactivity.findViewById(R.id.bGroups);
        final ImageButton bActivities = (ImageButton) myactivity.findViewById(R.id.bActivities);

        bGroups.setImageResource(R.drawable.groups900x900);

        bGlobal.setBackgroundResource(R.drawable.buttonshape);
        bGroups.setBackgroundResource(R.drawable.buttonshape);
        bActivities.setBackgroundResource(R.drawable.buttonshape);


        return view;
    }


}

