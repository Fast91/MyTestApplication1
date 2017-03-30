package com.example.faust.mytestapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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


    public ActivityGroupListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            MyActivity u = new MyActivity(names[i], images[i], balances[i]);
            if(i==0){//Luce

            }
            else if(i==1){

            }
            else if(i==2){

            }
            else{//Pranzo

            }

            activity.add(u);
        }


        activity.clear();
        activity = (ArrayList<MyActivity>) DB.getmActivities();

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

            adapter = new MyActivityGroupRecyclerViewAdapter(activity);
            recyclerView.setAdapter(adapter);
        }
        else{

            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.activity_group_list);
            //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new MyActivityGroupRecyclerViewAdapter(activity);
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
