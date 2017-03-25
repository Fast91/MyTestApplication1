package com.example.faust.mytestapplication1;

/**
 * Created by robertospaziani on 25/03/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//import com.example.faust.mytestapplication1.GlobalListFragment.OnListFragmentInteractionListener;


import java.util.List;


public class MyGroupsRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupsRecyclerViewAdapter.GroupHolder> {

    private final List<MyGroup> items;
    // private final OnListFragmentInteractionListener mListener;



    //public MyGroupsRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
    public MyGroupsRecyclerViewAdapter(List<MyGroup> items) {
        this.items = items;
        // mListener = listener;
    }


    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groups, parent, false);
        return new GroupHolder(view);
    }



    @Override
    public void onBindViewHolder(final GroupHolder holder, int position) {
        MyGroup u = items.get(position);
        holder.bindData(u);

       /*  holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class GroupHolder extends RecyclerView.ViewHolder {
        private MyGroup group;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;

        public GroupHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_groups_group);
            nameView = (TextView) view.findViewById(R.id.name_groups_group);
            balanceView = (TextView) view.findViewById(R.id.money_groups_group);

        }

        public void bindData(MyGroup u){
            group=u;
            imageView.setImageResource(u.getImageId());
            nameView.setText(u.getName());
            balanceView.setText(""+(u.getBalance()));

        }

    }



}

