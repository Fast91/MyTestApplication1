package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        public final TextView categoryView;
        public final TextView groupView;
        private Context mContext;
        public final Resources myr;

        public ActivityHolder(View view) {
            super(view);
            mContext = view.getContext();
            itemView.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image_activity_global);
            nameView = (TextView) view.findViewById(R.id.name_activity_global);
            balanceView = (TextView) view.findViewById(R.id.money_activity_global);
            categoryView = (TextView) view.findViewById(R.id.category_activity_global);

            groupView = (TextView) view.findViewById(R.id.group_activity_global);
myr=view.getResources();
        }

        public void bindData(NomeDovuto u){
            activity=u;
           // imageView.setImageResource(R.drawable.profilecircle);
            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(myr, R.drawable.giftboxred, 100, 100));
            nameView.setText(u.getName());
            balanceView.setText(""+(String.format("%.2f", u.getDovuto())+"€"));
            categoryView.setText(u.getCategory());
            groupView.setText(u.getName_Group());



        }


        @Override
        public void onClick(View v) {
            //Intent intent=new Intent(mContext , ActivityDetailActivity.class);
            //TODO PASSARE AL POSTO DI "1" L'UUID DELL'ATTIVITA' PRESO DAL DB
            Intent intent = ActivityDetailActivity.newIntent(mContext,activity.getId());
            mContext.startActivity(intent);
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
