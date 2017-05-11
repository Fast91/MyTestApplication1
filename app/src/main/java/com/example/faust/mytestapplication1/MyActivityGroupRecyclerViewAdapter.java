package com.example.faust.mytestapplication1;

import android.app.Activity;
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
import java.util.UUID;

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


    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private NomeDovuto activity;
        private View mParentView;
        private Context mContext;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView balanceView;
        public final TextView categoryView;
        public final TextView pagatoView;
        public final Resources myr;

        public ActivityHolder(View view) {
            super(view);
            mParentView = view;
            mContext = view.getContext();
            itemView.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image_activity_group);
            nameView = (TextView) view.findViewById(R.id.name_activity_group);
            balanceView = (TextView) view.findViewById(R.id.money_activity_group);
            categoryView = (TextView) view.findViewById(R.id.category_activity_global);
            pagatoView = (TextView) view.findViewById(R.id.pagatoda_activity);

myr=view.getResources();
        }

        public void bindData(NomeDovuto u){
            activity=u;
            //imageView.setImageResource(R.drawable.profilecircle);
            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(myr, R.drawable.giftboxred, 100, 100));
            //prendere il titolo della spesa
            if(u.getName().equals("Pagamento") || u.getName().equals("Payment") ){


                nameView.setText(myr.getString(R.string.textview_payment));
            }
            else{
                nameView.setText(u.getName());
            }



            balanceView.setText(""+(String.format("%.2f", u.getDovuto())+"€"));
            pagatoView.setText(u.pagatoda());


            categoryView.setText(u.getCategory());
        }

        @Override
        public void onClick(View v) {
            //Intent intent=new Intent(mContext , ActivityDetailActivity.class);
            //TODO PASSARE AL POSTO DI "1" L'UUID DELL'ATTIVITA' PRESO DAL DB
            Intent intent = ActivityDetailActivity.newIntent(mContext,activity.getId());
            mContext.startActivity(intent);

            //MainActivity.getInstance().finish(); todo brutto

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
