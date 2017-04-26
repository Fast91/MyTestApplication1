package com.example.faust.mytestapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertospaziani on 26/03/17.
 */

class MyUsersGroupRecyclerViewModifyAdapter extends RecyclerView.Adapter<MyUsersGroupRecyclerViewModifyAdapter.UserHolder>{

    private final List<NomeDovuto> items;
    private String mIdGroup, nameGroup;



    public MyUsersGroupRecyclerViewModifyAdapter(List<NomeDovuto> items, String id_group) {
        this.items = items;
        this.mIdGroup = id_group;
    }


    @Override
    public MyUsersGroupRecyclerViewModifyAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_group_modify, parent, false);
        return new MyUsersGroupRecyclerViewModifyAdapter.UserHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyUsersGroupRecyclerViewModifyAdapter.UserHolder holder, int position) {
        NomeDovuto u = items.get(position);
        holder.bindData(u);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private NomeDovuto user;
        public final ImageView imageView;
        public final TextView nameView;
        public final Resources myr;

        public UserHolder(View view) {
            super(view);
            itemView.findViewById(R.id.fragment_user_group_item_button).setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image_user_group);
            nameView = (TextView) view.findViewById(R.id.name_user_group);
        myr=view.getResources();

        }

        public void bindData(final NomeDovuto u){
            user=u;

            nameView.setText(u.getName());
            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(myr, R.drawable.profilecircle, 100, 100));

        }

        @Override
        public void onClick(final View v) {
            // Toast.makeText(v.getContext(), ""+user.getName()+" "+user.getId_Group()+" "+user.getId(), Toast.LENGTH_SHORT).show();

            ///Devo controllare per l'utente selezionato e per quel gruppo
            //se il bilancio e' 0 -- se 0 posso eliminarlo dal gruppo altrimenti TOAST

            new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.deleteuser_title)
                    .setMessage(R.string.deleteuser_message)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("Groups").child(user.getId_Group());

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Double balance = dataSnapshot.child("Total").getValue(Double.class);

                                    if(balance!=0){
                                        Toast.makeText(v.getContext(), R.string.impossible_to_delete, Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // Toast.makeText(v.getContext(), "Eliminato", Toast.LENGTH_SHORT).show();
                                        //devo levare l'utente per quel gruppo
                                        //devo levare dall'utente quel gruppo
                                        //devo levare per gli altri utenti di quel gruppo questo user
                                        //se il gruppo non ha più utenti eliminarlo

                                        //step 0 prendo gli amici connessi con quella persona dentro quel gruppo
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).child("Groups").child(user.getId_Group()).child("Users");

                                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                //prendo gli amici step 0
                                                int count=0;
                                                ArrayList<String> id_friend = new ArrayList<String>();

                                                for(DataSnapshot friend : dataSnapshot.getChildren()){


                                                    id_friend.add(friend.getKey());
                                                    count++;

                                                }




                                                //devo levare l'utente per quel gruppo
                                                FirebaseDatabase.getInstance().getReference().child("Groups").child(user.getId_Group()).child("Users").child(user.getId()).removeValue();

                                                //count= 0 nessun utente rimasto

                                                if(count==0){
                                                    //devo eliminare il gruppo
                                                    FirebaseDatabase.getInstance().getReference().child("Groups").child(user.getId_Group()).removeValue();

                                                }

                                                //per tutti gli amici connessi in quel gruppo devo levarlo
                                                for(String id: id_friend){

                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Groups").child(user.getId_Group())
                                                            .child("Users").child(user.getId()).removeValue();

                                                }

                                                //devo levare dall'utente quel gruppo
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).child("Groups").child(user.getId_Group()).removeValue();






                                                Intent intent=new Intent(v.getContext(),DeleteGroupActivity.class);
                                                intent.putExtra("ID_GROUP","00");
                                                intent.putExtra("NAME_GROUP",user.getName());
                                                v.getContext().startActivity(intent);




                                                return ;

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });






                        }
                    }).create().show();

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
