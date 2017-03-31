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

import java.util.ArrayList;

/**
 * Created by robertospaziani on 26/03/17.
 */

public class UsersGroupListFragment extends Fragment{


    private MyUsersGroupRecyclerViewAdapter adapter;

    private int mColumnCount=1;
    //private OnListFragmentInteractionListener mListener;

    private String[] names= {"Roberto", "Pasquale", "Fausto", "Omar", "Marco"};
    private int[] images= {R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle};
    private double[] balances = {25.00,20.00,25.00,-4.00,-3.00};
    private ArrayList<User> users;
    String id_namegroup;


    public UsersGroupListFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        if(b==null){
            id_namegroup="G1";
        }
        else{
            id_namegroup= b.getString("GROUP_ID");
        }




        users=new ArrayList<>();
        /*

        if(id_namegroup.equals("G1")) {
            for (int i = 0; i < 3; i++) {
                User u = new User(names[i], images[i], balances[i]);

                users.add(u);


            }
        }

        else {
            for (int i = 3; i < 5; i++) {
                User u = new User(names[i], images[i], balances[i]);

                users.add(u);
            }

        }
        */



        users.clear();
        users=(ArrayList<User>) DB.getUsersofGroup(id_namegroup);

        //Disabilitare il bottone







    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





      final  View view = inflater.inflate(R.layout.fragment_user_group_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;


            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }



            adapter = new MyUsersGroupRecyclerViewAdapter(users);
            recyclerView.setAdapter(adapter);
        }
        else{

            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.user_group_list);
            //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new MyUsersGroupRecyclerViewAdapter(users);
            recyclerView2.setAdapter(adapter);

            Button b5_showactivity = (Button) view.findViewById(R.id.b5_show_group_activity);
            //Listener Button5 show group activity
            b5_showactivity.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {



                    /*b2.setPressed(false);
                    b3.setPressed(false);

                    b1.setPressed(true);*/

                    final  AppCompatActivity activity = (AppCompatActivity) view.getContext();
                     Fragment myFragment = new ActivityGroupListFragment();
                      //Create a bundle to pass data, add data, set the bundle to your fragment and:
                         Bundle mBundle;
                     mBundle = new Bundle();
                    mBundle.putString("GROUP_ID",id_namegroup);
                   // mBundle.putInt("GROUP_ID",item.getIdgroup());
                    myFragment.setArguments(mBundle);







                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();


                    for(int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        activity.getSupportFragmentManager().popBackStackImmediate();
                    }


                    return ;
                }
            });

            final  AppCompatActivity activity = (android.support.v7.app.AppCompatActivity) view.getContext();
            final TextView namegroup = (TextView) activity.findViewById(R.id.row1_text1);
            String name= id_namegroup;
            namegroup.setText(name);

            final TextView moneygroup = (TextView) activity.findViewById(R.id.row1_text2);
            moneygroup.setText("100€");

        }



             /*
            final  AppCompatActivity activity = (AppCompatActivity) view.getContext();
            final TextView namegroup = (TextView) activity.findViewById(R.id.row1_text1);
            String name= id_namegroup;
            namegroup.setText(name);
            */

        final  AppCompatActivity myactivity = (AppCompatActivity) view.getContext();
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

