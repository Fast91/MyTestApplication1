package com.example.faust.mytestapplication1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
        public final Resources myr;


        public UserHolder(View view) {
            super(view);

            myr=view.getResources();
            imageView = (ImageView) view.findViewById(R.id.image_user_global);
            nameView = (TextView) view.findViewById(R.id.name_user_global);
            balanceView = (TextView) view.findViewById(R.id.money_user_global);

        }

        public void bindData(NomeDovuto u){
            user=u;



            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(myr, R.drawable.profilecircle, 100, 100));


            //imageView.setImageResource(R.drawable.profilecircle);
            nameView.setText(u.getName());
            balanceView.setText(""+(String.format("%.2f", u.getDovuto())+"€"));

            if (u.getDovuto().toString().charAt(0) == '-') {
                balanceView.setTextColor(Color.RED);//parseColor("#d02020"));
            } else {
                balanceView.setTextColor(Color.parseColor("#08a008"));
            }

        }

    }

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
