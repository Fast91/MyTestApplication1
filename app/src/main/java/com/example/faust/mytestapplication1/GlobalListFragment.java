package com.example.faust.mytestapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;


public class GlobalListFragment extends Fragment {


    private MyGlobalRecyclerViewAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_global_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;


            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter = new MyGlobalRecyclerViewAdapter(users);
            recyclerView.setAdapter(adapter);
        }
        else{

            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.global_list);
         //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new MyGlobalRecyclerViewAdapter(users);
            recyclerView2.setAdapter(adapter);

        }



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
