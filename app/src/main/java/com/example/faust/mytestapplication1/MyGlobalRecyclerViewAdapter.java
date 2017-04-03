package com.example.faust.mytestapplication1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.faust.mytestapplication1.GlobalListFragment.OnListFragmentInteractionListener;


import java.util.List;


public class MyGlobalRecyclerViewAdapter extends RecyclerView.Adapter<MyGlobalRecyclerViewAdapter.UserHolder> {

    private final List<NomeDovuto> items;
   // private final OnListFragmentInteractionListener mListener;



    //public MyGlobalRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
         public MyGlobalRecyclerViewAdapter(List<NomeDovuto> items) {
        this.items = items;
       // mListener = listener;
    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_global, parent, false);
        return new UserHolder(view);
    }



    @Override
    public void onBindViewHolder(final UserHolder holder, int position) {
        NomeDovuto u = items.get(position);
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



    public class UserHolder extends RecyclerView.ViewHolder {
        private NomeDovuto user;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;

        public UserHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_user_global);
            nameView = (TextView) view.findViewById(R.id.name_user_global);
            balanceView = (TextView) view.findViewById(R.id.money_user_global);

        }

        public void bindData(NomeDovuto u){
            user=u;
            imageView.setImageResource(R.drawable.profilecircle);
            nameView.setText(u.getName());
            balanceView.setText(""+(u.getDovuto()));

        }

    }



}
