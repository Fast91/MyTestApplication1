package com.example.faust.mytestapplication1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentTransaction;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GroupsListFragment extends Fragment {


    private MyGroupsRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MainActivity mainActivity;
    private FirebaseAuth firebaseAuth;
    /*private String[] names = {"G1", "G2"};
    private int[] images = {R.drawable.profilecircle, R.drawable.profilecircle};
    private double[] balances = {70.00, -7.00};*/
    //private ArrayList<MyGroup> groups;
    private List<MyGroup>groups;
    private HashMap<String,String> id_gruppo;


    public GroupsListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id_gruppo=new HashMap<>();

        try
        {
           // groups = DBManager.getGroups();
            groups = DB.getmGroups();
        }
        catch(Exception e) //sostituire con l'eccezione corretta
        {
            // dobbiamo gestire questa eccezione (lista vuota oppure problema col server)
        }

        //FIREBASE
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        //Set Name of the group

        final  AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();
        final TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
        String name= getString(R.string.global_string);
        namegroup.setText(name);

        final TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);
        moneygroup.setText("100€");


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
        /// Lista di gruppi per l'user autenticato con il rispettivo dovuto
        //////////////

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups");

        //Read content data

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String, NomeDovuto> gruppi_dovuto = new HashMap<>();

                //Prendo tutti i gruppi
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    id_gruppo.put(postSnapshot.child("Name").getValue(String.class),postSnapshot.getKey());

                    String id = (String) postSnapshot.getKey();

                    //prendo nome gruppo
                    String gruppo = postSnapshot.child("Name").getValue(String.class);
                    Double dovuto = (Double) postSnapshot.child("Total").getValue(Double.class);


                    //in teoria utente contiene solo una entry per un determinato gruppo

                    NomeDovuto iniziale = new NomeDovuto(gruppo, dovuto);
                    gruppi_dovuto.put(id, iniziale);


                }

                List list = new ArrayList(gruppi_dovuto.values());


                // Set the adapter forse il primo if non utile
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;


                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }

                    adapter = new MyGroupsRecyclerViewAdapter(list, new OnListFragmentInteractionListener() {//cambiato qui
                        @Override
                        public void onListFragmentInteraction(NomeDovuto item) {//cambiato qui
                            //TODO LISTENER IMPLEMENTARE
                            Toast.makeText(getContext(), R.string.toast_clickedgroup + item.getName(), Toast.LENGTH_LONG).show();

                            //You can change the fragment, something like this, not tested, please correct for your desired output:
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new UsersGroupListFragment();
                            //Create a bundle to pass data, add data, set the bundle to your fragment and:
                            Bundle mBundle;
                            mBundle = new Bundle();



                            mBundle.putString("GROUP_ID", id_gruppo.get(item.getName())); //Non gestito il fatto che il gruppo può avere nomi uguali
                            myFragment.setArguments(mBundle);

                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();


                            for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }


                        }
                    });
                    recyclerView.setAdapter(adapter);

                } else {

                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.groups_list);
                    //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new MyGroupsRecyclerViewAdapter(list, new OnListFragmentInteractionListener() {//cambiato qui
                        @Override
                        public void onListFragmentInteraction(NomeDovuto item) {//cambiato qui
                            //TODO LISTENER IMPLEMENTARE
                            Toast.makeText(getContext(), "Cliccato Gruppo: " + item.getName(), Toast.LENGTH_LONG).show();


                            //You can change the fragment, something like this, not tested, please correct for your desired output:
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new UsersGroupListFragment();
                            //Create a bundle to pass data, add data, set the bundle to your fragment and:
                            Bundle mBundle;
                            mBundle = new Bundle();
                            mBundle.putString("GROUP_ID", id_gruppo.get(item.getName()));

                            //set name
                    /*
                    final TextView namegroup = (TextView) view.findViewById(R.id.row1_text1);
                    String name= item.getName();
                    namegroup.setText(name);*/

                            myFragment.setArguments(mBundle);

                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();

                            for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }

                        }
                    });
                    recyclerView2.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(NomeDovuto item);
    }







}