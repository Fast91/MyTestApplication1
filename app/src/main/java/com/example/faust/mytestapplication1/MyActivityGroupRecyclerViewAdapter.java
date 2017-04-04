package com.example.faust.mytestapplication1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by robertospaziani on 26/03/17.
 */

public class MyActivityGroupRecyclerViewAdapter  extends RecyclerView.Adapter<MyActivityGroupRecyclerViewAdapter.ActivityHolder> {


    private final List<NomeDovuto> items;
    // private final OnListFragmentInteractionListener mListener;



    //public MyGroupsRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
    public MyActivityGroupRecyclerViewAdapter(List<NomeDovuto> items) {
        this.items = items;
        // mListener = listener;
    }


    @Override
    public MyActivityGroupRecyclerViewAdapter.ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activity_group, parent, false);
        return new MyActivityGroupRecyclerViewAdapter.ActivityHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyActivityGroupRecyclerViewAdapter.ActivityHolder holder, int position) {
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



    public class ActivityHolder extends RecyclerView.ViewHolder {
        private NomeDovuto activity;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;

        public ActivityHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_activity_group);
            nameView = (TextView) view.findViewById(R.id.name_activity_group);
            balanceView = (TextView) view.findViewById(R.id.money_activity_group);

        }

        public void bindData(NomeDovuto u){
            activity=u;
            imageView.setImageResource(R.drawable.profilecircle);
            nameView.setText(u.getName());
            balanceView.setText(""+(u.getDovuto()));

        }

    }

}
