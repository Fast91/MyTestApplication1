package com.example.faust.mytestapplication1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentTransaction;


import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GroupsListFragment extends Fragment {


    private MyGroupsRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MainActivity mainActivity;
    private FirebaseAuth firebaseAuth;
    /*private String[] names = {"G1", "G2"};
    private int[] images = {R.drawable.profilecircle, R.drawable.profilecircle};
    private double[] balances = {70.00, -7.00};*/
    //private ArrayList<MyGroup> groups;
    private List<MyGroup>groups;
    private HashMap<String,String> id_gruppo;
    private ImageView profile_image;
    private ProgressDialog pd;


    public GroupsListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        id_gruppo=new HashMap<>();

        /*try
        {
           // groups = DBManager.getGroups();
            groups = DB.getmGroups();
        }
        catch(Exception e) //sostituire con l'eccezione corretta
        {
            // dobbiamo gestire questa eccezione (lista vuota oppure problema col server)
        }*/

        //FIREBASE
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();






    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        pd = new ProgressDialog(view.getContext());
        String msg_xd = getString(R.string.progress_bar_db);
        pd.setMessage(msg_xd);
        pd.show();

        //Set Name of the group

        final  AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();
        final TextView namegroup = (TextView) myactivity.findViewById(R.id.row1_text1);
        String name= getString(R.string.global_string);
        namegroup.setText(name);

        final TextView moneygroup = (TextView) myactivity.findViewById(R.id.row1_text2);


        profile_image = (ImageView) myactivity.findViewById(R.id.row1_image1);
        profile_image.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.realphoto, 100, 100));
        profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


            }
        });
        getandSetImage();



        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("GlobalBalance");

        Double  bilancioGlobale;

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Double bilancioGlobale = (Double) dataSnapshot.getValue(Double.class);


                if (bilancioGlobale == null) {
                    bilancioGlobale = 0.0;
                }

                //Bilancio
                ((TextView) myactivity.findViewById(R.id.row1_text2)).setText(String.format("%.2f", bilancioGlobale)+"€");

                if (bilancioGlobale<0) {
                    TextView tv= (TextView) myactivity.findViewById(R.id.row1_text2);
                    tv.setTextColor(Color.RED);
                } else {
                    TextView tv= (TextView) myactivity.findViewById(R.id.row1_text2);
                    tv.setTextColor(Color.parseColor("#08a008"));
                }






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        /////////////////
        /// Lista di gruppi per l'user autenticato con il rispettivo dovuto
        //////////////

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Groups");

        //Read content data

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String, NomeDovuto> gruppi_dovuto = new HashMap<>();

                //Prendo tutti i gruppi
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    id_gruppo.put(postSnapshot.getKey(),postSnapshot.child("Name").getValue(String.class));

                    String id = (String) postSnapshot.getKey();

                    //prendo nome gruppo
                    String gruppo = postSnapshot.child("Name").getValue(String.class);
                    Double dovuto = (Double) postSnapshot.child("Total").getValue(Double.class);


                    //in teoria utente contiene solo una entry per un determinato gruppo

                    NomeDovuto iniziale = new NomeDovuto(gruppo, dovuto);
                    iniziale.setId(postSnapshot.getKey());
                    gruppi_dovuto.put(id, iniziale);


                }

                List list = new ArrayList(gruppi_dovuto.values());


                // Set the adapter forse il primo if non utile
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;


                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }

                    adapter = new MyGroupsRecyclerViewAdapter(list, new OnListFragmentInteractionListener() {//cambiato qui
                        @Override
                        public void onListFragmentInteraction(NomeDovuto item) {//cambiato qui
                            //TODO LISTENER IMPLEMENTARE
                            Toast.makeText(getContext(), R.string.toast_clickedgroup + item.getName(), Toast.LENGTH_LONG).show();

                            //You can change the fragment, something like this, not tested, please correct for your desired output:
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new UsersGroupListFragment();
                            //Create a bundle to pass data, add data, set the bundle to your fragment and:
                            Bundle mBundle;
                            mBundle = new Bundle();



                            mBundle.putString("GROUP_ID", id_gruppo.get(item.getId())); //Non gestito il fatto che il gruppo può avere nomi uguali
                            myFragment.setArguments(mBundle);

                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment)/*.addToBackStack(null)*/.commit();


                            for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }


                        }
                    });
                    recyclerView.setAdapter(adapter);

                } else {

                    RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.groups_list);
                    //   recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new MyGroupsRecyclerViewAdapter(list, new OnListFragmentInteractionListener() {//cambiato qui
                        @Override
                        public void onListFragmentInteraction(NomeDovuto item) {//cambiato qui
                            //TODO LISTENER IMPLEMENTARE
                            Toast.makeText(getContext(), "Cliccato Gruppo: " + item.getName(), Toast.LENGTH_LONG).show();


                            //You can change the fragment, something like this, not tested, please correct for your desired output:
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new UsersGroupListFragment();
                            //Create a bundle to pass data, add data, set the bundle to your fragment and:
                            Bundle mBundle;
                            mBundle = new Bundle();
                            mBundle.putString("GROUP_ID", id_gruppo.get(item.getId()));

                            //set name
                    /*
                    final TextView namegroup = (TextView) view.findViewById(R.id.row1_text1);
                    String name= item.getName();
                    namegroup.setText(name);*/

                            myFragment.setArguments(mBundle);

                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).addToBackStack(null).commit();

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, myFragment).commit();

                            for (int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }

                        }
                    });
                    recyclerView2.setAdapter(adapter);

                }


                pd.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        ////////////////////
        //// BOTTONE PER L'ADD GRUPPO
        ///////////////////



        Button addgroup = (Button) view.findViewById(R.id.b5_addgroup);


        addgroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Add GROUP


                Intent intent=new Intent(getActivity(),ActivityAddGroup.class);
                startActivity(intent);




                return;
            }
        });









        return view;
    }




    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(NomeDovuto item);
    }




    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Image");

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


                    profile_image.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), FullScreenImage.class);

                            profile_image.buildDrawingCache();
                            Bitmap image2= profile_image.getDrawingCache();

                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image2);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                    });




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