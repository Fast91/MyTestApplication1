package com.example.faust.mytestapplication1;

/**
 * Created by robertospaziani on 25/03/17.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.List;

import com.example.faust.mytestapplication1.GroupsListFragment.OnListFragmentInteractionListener;


import java.util.List;


public class MyGroupsRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupsRecyclerViewAdapter.GroupHolder> {

    private final List<NomeDovuto> items;
    private final OnListFragmentInteractionListener mListener;



    public MyGroupsRecyclerViewAdapter(List<NomeDovuto> items, OnListFragmentInteractionListener listener) {
    //public MyGroupsRecyclerViewAdapter(List<MyGroup> items) {
        this.items = items;
        mListener = listener;
    }


    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groups, parent, false);
        return new GroupHolder(view);
    }



    @Override
    public void onBindViewHolder(final GroupHolder holder, int position) {
        NomeDovuto u = items.get(position);
        holder.bindData(u);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class GroupHolder extends RecyclerView.ViewHolder {
        private NomeDovuto group;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;
        public final View itemView;
        public final Resources myr;

        public GroupHolder(View view) {
            super(view);
            itemView=view;
            imageView = (ImageView) view.findViewById(R.id.image_groups_group);
            nameView = (TextView) view.findViewById(R.id.name_groups_group);
            balanceView = (TextView) view.findViewById(R.id.money_groups_group);
myr=view.getResources();
        }

        public void bindData(final NomeDovuto u){
            group=u;
            //imageView.setImageResource(R.drawable.profilecircle);
            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(myr, R.drawable.group, 100, 100));
            nameView.setText(u.getName());
            balanceView.setText(""+(String.format("%.2f", u.getDovuto())+"€"));

            if (u.getDovuto().toString().charAt(0) == '-') {
                balanceView.setTextColor(Color.RED);//parseColor("#d02020"));
            } else {
                balanceView.setTextColor(Color.parseColor("#08a008"));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(u);
                    }
                }
            });


        }

    }

    /*
    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }*/
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}

