package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    private String mIdGroup, nameGroup;
    // private final OnListFragmentInteractionListener mListener;



    //public MyGlobalRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
    public MyUsersGroupRecyclerViewAdapter(List<NomeDovuto> items, String id_group) {
        this.items = items;
        this.mIdGroup = id_group;
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
        public final TextView todoView;
        public final Resources myr;

        public UserHolder(final View view) {
            super(view);
            itemView.findViewById(R.id.fragment_user_group_item_button).setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image_user_group);
            nameView = (TextView) view.findViewById(R.id.name_user_group);
            balanceView = (TextView) view.findViewById(R.id.money_user_group);
            todoView=(TextView)view.findViewById(R.id.todo_user_group);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(),ReadProfileActivity.class);
                    i.putExtra("MY_PROFILE","NO");
                    i.putExtra("PROFILE_ID",user.getId());
                    v.getContext().startActivity(i);

                }
            });

            myr=view.getResources();
        }

        public void bindData(final NomeDovuto u){
            user=u;
           // imageView.setImageResource(R.drawable.giftgreen);
            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(myr, R.drawable.profilecircle, 100, 100));
            nameView.setText(u.getName());
            balanceView.setText(""+(String.format("%.2f", u.getDovuto())+"€"));


            if (u.getDovuto().toString().charAt(0) == '-') {
                balanceView.setTextColor(Color.RED);//parseColor("#d02020"));
            } else {
                balanceView.setTextColor(Color.parseColor("#08a008"));
            }

            if(u.getDovuto().toString().charAt(0) == '-'){
                todoView.setText(R.string.devi);
                todoView.setTextColor(Color.RED);

            }
            else{

                todoView.setText(R.string.tideve);
                todoView.setTextColor(Color.parseColor("#08a008"));

            }


        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(imageView.getContext(), "Hai cliccato \"Paga\"", Toast.LENGTH_SHORT).show();

            Intent i = PartPaymentActivity.newIntent(imageView.getContext(), nameView.getText().toString(), mIdGroup);
            i.putExtra("ID_GROUP",mIdGroup);
            i.putExtra("DOVUTO",balanceView.getText().toString());
            i.putExtra("ID_USER",user.getId());
            imageView.getContext().startActivity(i);
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
