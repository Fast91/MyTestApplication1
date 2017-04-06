package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by robertospaziani on 26/03/17.
 */

class MyUsersGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyUsersGroupRecyclerViewAdapter.UserHolder>{

    private final List<NomeDovuto> items;
    // private final OnListFragmentInteractionListener mListener;



    //public MyGlobalRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
    public MyUsersGroupRecyclerViewAdapter(List<NomeDovuto> items) {
        this.items = items;
        // mListener = listener;
    }


    @Override
    public MyUsersGroupRecyclerViewAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_group, parent, false);
        return new MyUsersGroupRecyclerViewAdapter.UserHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyUsersGroupRecyclerViewAdapter.UserHolder holder, int position) {
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



    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private NomeDovuto user;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;

        public UserHolder(View view) {
            super(view);
            itemView.findViewById(R.id.fragment_user_group_item_button).setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image_user_group);
            nameView = (TextView) view.findViewById(R.id.name_user_group);
            balanceView = (TextView) view.findViewById(R.id.money_user_group);

        }

        public void bindData(NomeDovuto u){
            user=u;
            imageView.setImageResource(R.drawable.giftgreen);
            nameView.setText(u.getName());
            balanceView.setText(""+(u.getDovuto()));

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(imageView.getContext(), "Hai cliccato \"Paga\"", Toast.LENGTH_SHORT).show();
            Log.d("PaymentActivity", "onClick");
            Intent i = PaymentActivity.newIntent(imageView.getContext(), "1");
            Log.d("PaymentActivity", "intent created");
            imageView.getContext().startActivity(i);
            Log.d("PaymentActivity", "activity started");
        }
    }



}
