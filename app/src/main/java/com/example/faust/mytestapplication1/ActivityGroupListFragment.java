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
    //private OnListFragmentInteractionListener mListener;

    private String[] names = {"Luce", "Gas" , "Cena", "Pranzo"};
    private int[] images = {R.drawable.energia, R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle};
    private double[] balances = {100.00, 25.00 , 12.00, 6.00};
    private ArrayList<MyActivity> activity;

    String id_group;
    private FirebaseAuth firebaseAuth;
    private HashMap<String,NomeDovuto> attivita_dovuto;


    public ActivityGroupListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = new ArrayList<>();

        id_group= savedInstanceState.getString("GROUP_ID");

        firebaseAuth = FirebaseAuth.getInstance();

         attivita_dovuto= new HashMap<>();


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


                            if (attivita_dovuto.get(id) == null || attivita_dovuto.containsKey(id) == false) {
                                //add
                                NomeDovuto iniziale = new NomeDovuto(nome, dovuto);
                                attivita_dovuto.put(id, iniziale);


                            } else {

                                //faccio il get e il replace
                                NomeDovuto iniziale = attivita_dovuto.get(id);
                                iniziale.setDovuto(iniziale.getDovuto() + dovuto);
                                attivita_dovuto.remove(id);
                                attivita_dovuto.put(id, iniziale);

                            }


                        }


                    }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_group_list, container, false);

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
        else{

            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.activity_group_list);
            //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            List list = new ArrayList(attivita_dovuto.values());
            adapter = new MyActivityGroupRecyclerViewAdapter(list);
            recyclerView2.setAdapter(adapter);

        }


        final  AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();
        /*
        final TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
        String name= id_namegroup;
        namegroup.setText(name);


        final TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);
        moneygroup.setText("100€");
        */

        //Settare il valore della riga li sopra
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
                namegroup.setText(dataSnapshot.child("Name").getValue(String.class));
                TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);
                moneygroup.setText(dataSnapshot.child("Total").getValue(String.class));
                // or double
                //moneygroup.setText(dataSnapshot.child("Total").getValue(Double.class));
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


//CLASSI NON UTILIZZATE

/*
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GlobalListFragment newInstance(int columnCount) {
        GlobalListFragment fragment = new GlobalListFragment();
        Bundle args = new Bundle();
        args.putInt("ARG_COLUMN_COUNT", columnCount);
        fragment.setArguments(args);
        return fragment;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(User item);
    }
    */


}
