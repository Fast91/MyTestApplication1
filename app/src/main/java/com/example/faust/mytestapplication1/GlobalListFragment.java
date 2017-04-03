package com.example.faust.mytestapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class GlobalListFragment extends Fragment {


    private MyGlobalRecyclerViewAdapter adapter;
    private FirebaseAuth firebaseAuth;

    private int mColumnCount=1;
    //private OnListFragmentInteractionListener mListener;

    /*private String[] names= {"Roberto", "Pasquale", "Fausto", "Omar", "Marco"};
    private int[] images= {R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle};
    private double[] balances = {25.00,20.00,25.00,-4.00,-3.00};*/
    //private ArrayList<User> users;
    private List<User> users;

    public GlobalListFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        try
        {
            //users = DBManager.getUsers();
            users= DB.getmUsers();
        }
        catch(Exception e) //sostituire con l'eccezione corretta
        {
            // dobbiamo gestire questa eccezione (lista vuota oppure problema col server)
        }

        /*users=new ArrayList<>();
        for(int i=0;i<names.length;i++){
            User u=new User(names[i],images[i],balances[i]);
            if(i==0 || i==1 || i==2){
                u.addGrouponUser(new MyGroup("G1"));
            }
            else{
                u.addGrouponUser(new MyGroup("G2"));
            }
            users.add(u);
        }*/



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_global_list, container, false);




        final AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();
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
                ((TextView) myactivity.findViewById(R.id.row1_text2)).setText(bilancioGlobale.toString()+"€");






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        /////////////////
        /// Lista di utenti per l'user autenticato con il rispettivo dovuto
        //////////////

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups");

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String,NomeDovuto> utenti_dovuto= new HashMap<>();

                //Prendo tutti i gruppi
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {



                    //prendo gli amici
                    DataSnapshot friends=postSnapshot.child("Users");




                    //prendo un amico
                    for (DataSnapshot friend : postSnapshot.child("Users").getChildren()){

                        String id= (String) friend.getKey();
                        String nome = (String) friend.child("Name").getValue(String.class);
                        Double dovuto = (Double) friend.child("Total").getValue(Double.class);





                        if(utenti_dovuto.get(id)==null || utenti_dovuto.containsKey(id)==false){
                            //add
                            NomeDovuto iniziale = new NomeDovuto(nome,dovuto);
                            utenti_dovuto.put(id,iniziale);


                        }
                        else{

                            //faccio il get e il replace
                            NomeDovuto iniziale = utenti_dovuto.get(id);
                            iniziale.setDovuto(iniziale.getDovuto() + dovuto);
                            utenti_dovuto.remove(id);
                            utenti_dovuto.put(id,iniziale);

                        }




                    }




                }


                ///Adesso che ho la mia cazzo di lista bella piena
                //Posso settare gli elementi nell'adapter porca puttana eva
                ///

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
                    adapter = new MyGlobalRecyclerViewAdapter(list);
                    recyclerView.setAdapter(adapter);
                }
                else{

                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.global_list);
                    //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    List list = new ArrayList(utenti_dovuto.values());
                    adapter = new MyGlobalRecyclerViewAdapter(list);
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
