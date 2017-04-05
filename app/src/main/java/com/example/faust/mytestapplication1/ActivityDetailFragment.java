package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by faust on 05/04/2017.
 */

public class ActivityDetailFragment extends android.support.v4.app.Fragment
{
    private RecyclerView mRecyclerView;
    private ActivityDetailAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_activity_detail_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.activity_detail_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI()
    {
        DetailEntryLab detailEntryLab = new DetailEntryLab();//DetailEntryLab.get(getActivity());
        List<DetailEntry> detailEntries = detailEntryLab.getDetailEntries();

        mAdapter = new ActivityDetailAdapter(detailEntries);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class ActivityDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private DetailEntry mDetailEntry;
        private TextView mUserNameTextView;
        private TextView mAmountTextView;

        public ActivityDetailHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_activity_detail, parent, false));
            itemView.setOnClickListener(this);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.detail_user_name);
            mAmountTextView = (TextView) itemView.findViewById(R.id.detail_amount);
        }

        public void bind(DetailEntry detailEntry)
        {
            mDetailEntry = detailEntry;
            mUserNameTextView.setText(mDetailEntry.getUserName());
            mAmountTextView.setText(mDetailEntry.getAmount() + mDetailEntry.getCurrency());
            try {
                if (mDetailEntry.getAmount().charAt(0) == '-') {
                    mAmountTextView.setTextColor(Color.parseColor("#d02020"));
                } else {
                    mAmountTextView.setTextColor(Color.parseColor("#08a008"));
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {}
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(getActivity(),
                    mDetailEntry.getUserName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class ActivityDetailAdapter extends RecyclerView.Adapter<ActivityDetailHolder>
    {
        private List<DetailEntry> mDetailEntries;

        public ActivityDetailAdapter(List<DetailEntry> detailEntries)
        {
            mDetailEntries = detailEntries;
        }


        @Override
        public ActivityDetailHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ActivityDetailHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ActivityDetailHolder holder, int position)
        {
            DetailEntry detailEntry = mDetailEntries.get(position);
            holder.bind(detailEntry);
        }

        @Override
        public int getItemCount()
        {
            return mDetailEntries.size();
        }
    }
}
