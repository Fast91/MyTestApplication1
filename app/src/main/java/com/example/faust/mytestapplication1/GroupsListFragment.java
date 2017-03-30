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


import java.util.ArrayList;
import java.util.List;


public class GroupsListFragment extends Fragment {


    private MyGroupsRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MainActivity mainActivity;

    /*private String[] names = {"G1", "G2"};
    private int[] images = {R.drawable.profilecircle, R.drawable.profilecircle};
    private double[] balances = {70.00, -7.00};*/
    //private ArrayList<MyGroup> groups;
    private List<MyGroup>groups;


    public GroupsListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
           // groups = DBManager.getGroups();
            groups = DB.getmGroups();
        }
        catch(Exception e) //sostituire con l'eccezione corretta
        {
            // dobbiamo gestire questa eccezione (lista vuota oppure problema col server)
        }

        /*groups = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            MyGroup u = new MyGroup(names[i], images[i], balances[i]);
            u.setIdgroup(i+1);
            if(i==0){
                u.addUserinGroup(new User ("Roberto",R.drawable.profilecircle,25.00));
                u.addUserinGroup(new User ("Pasquale",R.drawable.profilecircle,20.00));
                u.addUserinGroup(new User ("Fausto",R.drawable.profilecircle,25.00));
                MyActivity a1 = new MyActivity("Luce",R.drawable.energia,100.00);
                a1.setOwner(new User("Possessore App",R.drawable.realphoto));
                u.addActivityinGroup(a1);
                MyActivity a2 = new MyActivity("Gas",R.drawable.logogas,25.00);
                a2.setOwner(new User("Pasquale",R.drawable.profilecircle));
                u.addActivityinGroup(a2);
            }
            else{
                u.addUserinGroup(new User ("Omar",R.drawable.profilecircle,-4.00));
                u.addUserinGroup(new User ("Marco",R.drawable.profilecircle,-3.00));
                MyActivity a1 = new MyActivity("Cena",R.drawable.cibo,12.00);
                a1.setOwner(new User("Omar",R.drawable.profilecircle));
                u.addActivityinGroup(a1);
                MyActivity a2 = new MyActivity("Pranzo",R.drawable.cibo,6.00);
                a2.setOwner(new User("Marco",R.drawable.profilecircle));
                u.addActivityinGroup(a2);
            }
            groups.add(u);
        }*/




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        // Set the adapter forse il primo if non utile
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;


            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter = new MyGroupsRecyclerViewAdapter(groups, new OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(MyGroup item) {
                    //TODO LISTENER IMPLEMENTARE
                    Toast.makeText(getContext(),"Cliccato Gruppo: "+item.getName(),Toast.LENGTH_LONG).show();
                    //You can change the fragment, something like this, not tested, please correct for your desired output:
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new UsersGroupListFragment();
                    //Create a bundle to pass data, add data, set the bundle to your fragment and:
                    Bundle mBundle;
                    mBundle = new Bundle();
                    mBundle.putString("GROUP_ID",item.getName());
                    myFragment.setArguments(mBundle);

                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();


                    for(int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        activity.getSupportFragmentManager().popBackStackImmediate();
                    }



                }
            });
            recyclerView.setAdapter(adapter);

        }
        else{

            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.groups_list);
            //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new MyGroupsRecyclerViewAdapter(groups, new OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(MyGroup item) {
                    //TODO LISTENER IMPLEMENTARE
                    Toast.makeText(getContext(),"Cliccato Gruppo: "+item.getName(),Toast.LENGTH_LONG).show();

                    //You can change the fragment, something like this, not tested, please correct for your desired output:
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new UsersGroupListFragment();
                    //Create a bundle to pass data, add data, set the bundle to your fragment and:
                    Bundle mBundle;
                    mBundle = new Bundle();
                    mBundle.putString("GROUP_ID",item.getName());

                    //set name
                    /*
                    final TextView namegroup = (TextView) view.findViewById(R.id.row1_text1);
                    String name= item.getName();
                    namegroup.setText(name);*/

                    myFragment.setArguments(mBundle);

                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();

                    for(int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        activity.getSupportFragmentManager().popBackStackImmediate();
                    }

                }
            });
            recyclerView2.setAdapter(adapter);

        }



        //Set Name of the group

        final  AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();
        final TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
        String name= "Global";
        namegroup.setText(name);

        final TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);
        moneygroup.setText("100€");



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

        void onListFragmentInteraction(MyGroup item);
    }







}