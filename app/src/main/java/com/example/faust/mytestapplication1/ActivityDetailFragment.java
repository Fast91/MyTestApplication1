package com.example.faust.mytestapplication1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
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
        DetailLab detailLab = DetailLab.get(getActivity());
        List<Detail> details = detailLab.getDetails();

        mAdapter = new ActivityDetailAdapter(details);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class ActivityDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Detail mDetail;
        private TextView mUserNameTextView;
        private TextView mAmountTextView;

        public ActivityDetailHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_activity_detail, parent, false));
            itemView.setOnClickListener(this);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.detail_user_name);
            mAmountTextView = (TextView) itemView.findViewById(R.id.detail_amount);
        }

        public void bind(Detail detail)
        {
            mDetail = detail;
            mUserNameTextView.setText(mDetail.getUserName());
            mAmountTextView.setText(mDetail.getAmount());
            try {
                if (mDetail.getAmount().charAt(0) == '-') {
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
                    mDetail.getUserName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class ActivityDetailAdapter extends RecyclerView.Adapter<ActivityDetailHolder>
    {
        private List<Detail> mDetails;

        public ActivityDetailAdapter(List<Detail> details)
        {
            mDetails = details;
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
            Detail detail = mDetails.get(position);
            holder.bind(detail);
        }

        @Override
        public int getItemCount()
        {
            return mDetails.size();
        }
    }
}
