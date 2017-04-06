package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by robertospaziani on 25/03/17.
 */

class MyActivityRecyclerViewAdapter extends RecyclerView.Adapter<MyActivityRecyclerViewAdapter.ActivityHolder>{

    private final List<NomeDovuto> items;



    //public MyActivityRecyclerViewAdapter(List<NomeDovuto> items, OnListFragmentInteractionListener listener) {
    public MyActivityRecyclerViewAdapter(List<NomeDovuto> items) {
        this.items = items;
        // mListener = listener;
    }


    @Override
    public MyActivityRecyclerViewAdapter.ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activity, parent, false);
        return new MyActivityRecyclerViewAdapter.ActivityHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyActivityRecyclerViewAdapter.ActivityHolder holder, int position) {
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



    public class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NomeDovuto activity;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;
        private Context mContext;

        public ActivityHolder(View view) {
            super(view);
            mContext = view.getContext();
            imageView = (ImageView) view.findViewById(R.id.image_activity_global);
            nameView = (TextView) view.findViewById(R.id.name_activity_global);
            balanceView = (TextView) view.findViewById(R.id.money_activity_global);

        }

        public void bindData(NomeDovuto u){
            activity=u;
            imageView.setImageResource(R.drawable.profilecircle);
            nameView.setText(u.getName());
            balanceView.setText(""+(u.getDovuto()));



        }


        @Override
        public void onClick(View v) {
            //Intent intent=new Intent(mContext , ActivityDetailActivity.class);
            //TODO PASSARE AL POSTO DI "1" L'UUID DELL'ATTIVITA' PRESO DAL DB
            Intent intent = ActivityDetailActivity.newIntent(mContext,"1");
            mContext.startActivity(intent);
        }

    }



}
