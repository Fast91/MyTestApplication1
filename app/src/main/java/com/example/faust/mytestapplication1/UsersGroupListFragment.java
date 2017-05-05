package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnKeyListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.*;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robertospaziani on 26/03/17.
 */

public class UsersGroupListFragment extends Fragment{


    private MyUsersGroupRecyclerViewAdapter adapter;
    private AppCompatActivity myactivity;

    private com.makeramen.roundedimageview.RoundedImageView profile_image;
    private int mColumnCount=1;

    String id_group, name_group;
    private FirebaseAuth firebaseAuth;
    private HashMap<String,NomeDovuto> utenti_dovuto;
    Boolean bilancio_0 = true;
    int count_fatti=0;
    private View view;
    private  AppCompatActivity activity;



    public UsersGroupListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();

        id_group= b.getString("GROUP_ID");

        firebaseAuth = FirebaseAuth.getInstance();



        utenti_dovuto= new HashMap<>();





    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





       view = inflater.inflate(R.layout.fragment_user_group_list, container, false);



       activity = (android.support.v7.app.AppCompatActivity) view.getContext();




        profile_image = (com.makeramen.roundedimageview.RoundedImageView) activity.findViewById(R.id.row1_image1);

        profile_image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });



        getandSetImage();

        DatabaseReference databaseReference;



        ////////////////
        //database
        ///////////////

        ////Ricerca ID per quel Nome che sarebbe GROUP_ID il nome
        ///// Prendere Tutti gli utenti e settare id_namegroup il vero ID del gruppo utile per il prossimo frammento



        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if(dataSnapshot.getValue()==null){

                    Intent i = new Intent(view.getContext(), PrimaAttivitaGruppi.class);

                    startActivity(i);
                }


                //aggiunto non testato
                        name_group = dataSnapshot.child("Name").getValue(String.class);

                        //prendo gli amici
                        DataSnapshot friends = dataSnapshot.child("Users");

                utenti_dovuto.clear();


                        //prendo un amico
                        for (DataSnapshot friend : friends.getChildren()) {

                            String id = (String) friend.getKey();
                            String nome = (String) friend.child("Name").getValue(String.class);
                            Double dovuto = (Double) friend.child("Total").getValue(Double.class);


                            if (utenti_dovuto.get(id) == null || utenti_dovuto.containsKey(id) == false) {
                                //add
                                NomeDovuto iniziale = new NomeDovuto(nome, dovuto);
                                iniziale.setId(id);
                                iniziale.setId_Group(id_group);
                                iniziale.setName_Group(name_group);
                                utenti_dovuto.put(id, iniziale);


                            } else {

                                //faccio il get e il replace
                                NomeDovuto iniziale = utenti_dovuto.get(id);
                                iniziale.setDovuto(iniziale.getDovuto() + dovuto);
                                utenti_dovuto.remove(id);
                                utenti_dovuto.put(id, iniziale);

                            }


                        }

                ///Adesso che ho la mia cazzo di lista bella piena
                //Posso settare gli elementi nell'adapter porca puttana eva
                ///

                // Set the adapter



                // Set the adapter

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;


                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }


                    List list = new ArrayList(utenti_dovuto.values());
                    adapter = new MyUsersGroupRecyclerViewAdapter(list, id_group);
                    recyclerView.setAdapter(adapter);
                }
                else{

                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.user_group_list);
                    //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    List list = new ArrayList(utenti_dovuto.values());
                    adapter = new MyUsersGroupRecyclerViewAdapter(list, id_group);
                    recyclerView2.setAdapter(adapter);




                }
                 /////// END ADAPTER

                TextView namegroup = (TextView) activity.findViewById(R.id.row1_text1);
                namegroup.setText(dataSnapshot.child("Name").getValue(String.class));
                TextView moneygroup = (TextView) activity.findViewById(R.id.row1_text2);
                TextView todogroup=(TextView)activity.findViewById(R.id.row1_todo);
                moneygroup.setText(String.format("%.2f", dataSnapshot.child("Total").getValue(Double.class))+"€");




                ///sta cosa da problemiiii
/*
                if (dataSnapshot.child("Total").getValue(Double.class)<0) {
                    TextView tv= (TextView) myactivity.findViewById(R.id.row1_text2);
                    tv.setTextColor(Color.RED);
                } else {
                    TextView tv= (TextView) myactivity.findViewById(R.id.row1_text2);
                    tv.setTextColor(Color.parseColor("#08a008"));
                }*/


                Double value = dataSnapshot.child("Total").getValue(Double.class);
                if(value!=null) {

                    if (value < 0) {
                        TextView tv = (TextView) activity.findViewById(R.id.row1_text2);
                        tv.setTextColor(Color.RED);
                        TextView tv2= (TextView) activity.findViewById(R.id.row1_todo);
                        tv2.setText(R.string.devi);
                        tv2.setTextColor(Color.RED);

                    } else {
                        TextView tv = (TextView) activity.findViewById(R.id.row1_text2);
                        tv.setTextColor(Color.parseColor("#08a008"));

                        TextView tv2= (TextView) activity.findViewById(R.id.row1_todo);
                        tv2.setText(R.string.tideve2);

                        tv2.setTextColor(Color.parseColor("#08a008"));


                    }

                }







                    }






            @Override
            public void onCancelled(DatabaseError databaseError) {

                Intent i = new Intent(view.getContext(), PrimaAttivitaGruppi.class);

                startActivity(i);

            }

        });





        //Settare il valore della riga li sopra con nome del gruppo ecc
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*
                TextView namegroup = (TextView) activity.findViewById(R.id.row1_text1);
                namegroup.setText(dataSnapshot.child("Name").getValue(String.class));
                TextView moneygroup = (TextView) activity.findViewById(R.id.row1_text2);

                moneygroup.setText(String.format("%.2f", dataSnapshot.child("Total").getValue(Double.class))+"€");





                Double value = dataSnapshot.child("Total").getValue(Double.class);
                if(value!=null) {

                    if (value < 0) {
                        TextView tv = (TextView) activity.findViewById(R.id.row1_text2);
                        tv.setTextColor(Color.RED);
                    } else {
                        TextView tv = (TextView) activity.findViewById(R.id.row1_text2);
                        tv.setTextColor(Color.parseColor("#08a008"));
                    }

                }
                */


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






        final  AppCompatActivity myactivity = (AppCompatActivity) view.getContext();
        final Button bGlobal = (Button) myactivity.findViewById(R.id.bGlobal);
        final Button bGroups = (Button) myactivity.findViewById(R.id.bGroups);
        final Button bActivities = (Button) myactivity.findViewById(R.id.bActivities);




        return view;
    }




    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Groups").child(id_group).child("Image");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //if != null SET

                final String image = dataSnapshot.getValue(String.class);

                if (image != null) {

                    if (!image.contains("http")) {
                        try {
                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(image);
                            //Bitmap imageCirle = getclip(imageBitmaptaken);
                            profile_image.setImageBitmap(imageBitmaptaken);




                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {


                        Picasso.with(getContext())
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(profile_image);






                        // Bitmap imageBitmaptaken = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                        // Bitmap imageCirle = getclip(imageBitmaptaken);
                        // profile_image.setImageBitmap(imageCirle);



                    }
/*
                    profile_image.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), FullScreenImage.class);

                            profile_image.buildDrawingCache();
                            Bitmap image2= profile_image.getDrawingCache();

                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap2", image2);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                    });
                    */








                }
                else{
                    profile_image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.group));
                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

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

