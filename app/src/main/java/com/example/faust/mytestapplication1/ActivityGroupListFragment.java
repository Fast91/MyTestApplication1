package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by robertospaziani on 26/03/17.
 */

public class ActivityGroupListFragment extends Fragment {


    private MyActivityGroupRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private ImageView profile_image;


    String id_group;
    private FirebaseAuth firebaseAuth;
    private HashMap<String,NomeDovuto> attivita_dovuto;
    private AppCompatActivity myactivity;


    public ActivityGroupListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();

        id_group= b.getString("GROUP_ID");

        firebaseAuth = FirebaseAuth.getInstance();

         attivita_dovuto= new HashMap<>();





    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_activity_group_list, container, false);


       myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();

        profile_image = (com.makeramen.roundedimageview.RoundedImageView) myactivity.findViewById(R.id.row1_image1);

        profile_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


            }
        });

        getandSetImage();




        ///////////////
        //database
        ///////////////

        ////Ricerca ID per quel Nome che sarebbe GROUP_ID il nome
        ///// Prendere Tutti gli utenti e settare id_namegroup il vero ID del gruppo utile per il prossimo frammento

        ///controllo iniziale se esiste il gruppo
        DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference10.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //controllo se deve fare qualcosa

                if (dataSnapshot.getValue() == null) {


                }

                else {

                    DatabaseReference databaseReference;


                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group);


                    //Read content data
                    databaseReference.addValueEventListener(new ValueEventListener() { //todo cambiare value event

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            attivita_dovuto.clear();

                            if(dataSnapshot.getChildrenCount()>0) {


                                //Per ogni attivita
                                for (DataSnapshot postSnapshot : dataSnapshot.child("Activities").getChildren()) {





                                    String id = (String) postSnapshot.getKey();
                                    String category = (String) postSnapshot.child("Category").getValue(String.class);
                                    String nome = (String) postSnapshot.child("Name").getValue(String.class);
                                    Double dovuto = (Double) postSnapshot.child("Total").getValue(Double.class);
                                    //todo inserire categoria nel DB groups

                                    String a= (String) postSnapshot.child("NamePagato").getValue(String.class);;




                                    if (category != null && isAdded()) {

                                        if (category.equals("Generale") || category.equals("General")) {
                                            category = getString(R.string.category_generale);
                                        }

                                        if (category.equals("Luce") || category.equals("Light")) {
                                            category = getString(R.string.category_luce);
                                        }


                                        if (category.equals("Payment") || category.equals("Pagamento")) {
                                            category = getString(R.string.payment_title);
                                        }

                                        if (category.equals("Alimentari") || category.equals("Food")) {
                                            category = getString(R.string.category_cibo);
                                        }

                                        if (category.equals("Gift") || category.equals("Regalo")) {
                                            category = getString(R.string.category_generale);
                                        }

                                    }

                                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = null;
                                    try {
                                        String s = postSnapshot.child("Date").getValue(String.class);
                                        date = format.parse(s);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    NomeDovuto iniziale = new NomeDovuto(nome, dovuto);
                                    iniziale.setId(id);
                                    iniziale.setCategory(category);
                                    iniziale.setDate(date);
                                    iniziale.setPagatoDa(a);
                                    attivita_dovuto.put(id, iniziale);


                                }


                                //Adapter

                                // Set the adapter
                                if (view instanceof RecyclerView) {
                                    Context context = view.getContext();
                                    RecyclerView recyclerView = (RecyclerView) view;


                                    if (mColumnCount <= 1) {
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    } else {
                                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                    }
                                    List list = new ArrayList(attivita_dovuto.values());
                                    //todo: ordinare la lista
                                    Collections.sort(list);

                                    adapter = new MyActivityGroupRecyclerViewAdapter(list);
                                    recyclerView.setAdapter(adapter);
                                } else {

                                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.activity_group_list);
                                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    List list = new ArrayList(attivita_dovuto.values());
                                    //todo: ordinare la lista
                                    Collections.sort(list);


                                    adapter = new MyActivityGroupRecyclerViewAdapter(list);
                                    recyclerView2.setAdapter(adapter);
                                }


                                //End adapter

                            }
                            else{

                                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.activity_group_list);
                                recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                                List list = new ArrayList(attivita_dovuto.values());
                                //todo: ordinare la lista
                                Collections.sort(list);


                                adapter = new MyActivityGroupRecyclerViewAdapter(list);
                                recyclerView2.setAdapter(adapter);

                            }


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





        //Settare il valore della riga li sopra con nome del gruppo ecc
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups").child(id_group);

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
                namegroup.setText(dataSnapshot.child("Name").getValue(String.class));
                TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);

                moneygroup.setText(String.format("%.2f", dataSnapshot.child("Total").getValue(Double.class))+"€");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






        final Button bGlobal = (Button) myactivity.findViewById(R.id.bGlobal);
        final Button bGroups = (Button) myactivity.findViewById(R.id.bGroups);
        final Button bActivities = (Button) myactivity.findViewById(R.id.bActivities);



        return view;
    }



    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id_group).child("Image");

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





}
