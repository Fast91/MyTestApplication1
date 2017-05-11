package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String mExpenseId;

    private DatabaseReference databaseReference;
    private DatabaseReference dbref2;
    private DatabaseReference dbref3;

    private TextView mTitleTextView;
    private ImageView mImageView;
    private TextView mGroupTextView;
    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mAmountCurrencyTextView;
    private TextView mCategoryTextView;
    private TextView mPagatoDaTextView;

    private FirebaseAuth firebaseAuth;
    private Boolean inizio=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExpenseId = getArguments().getString("expense_id", null);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_activity_detail_list, container, false);



        mRecyclerView = (RecyclerView) view.findViewById(R.id.activity_detail_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mExpenseId = getArguments().getString("expense_id", null);
        updateUI();
        // INIZIO DATABASE
        final AppCompatActivity myActivity = (AppCompatActivity) view.getContext();

        mTitleTextView = (TextView) myActivity.findViewById(R.id.title_expense);
        mImageView = (ImageView) myActivity.findViewById(R.id.imageDetailPicture);
        mGroupTextView = (TextView) myActivity.findViewById(R.id.Group_expense);
        mDateTextView = (TextView) myActivity.findViewById(R.id.Date_expense);
        mAmountTextView = (TextView) myActivity.findViewById(R.id.Total_expense);
        mAmountCurrencyTextView = (TextView) myActivity.findViewById(R.id.Total_expense_currency);
        mCategoryTextView = (TextView) myActivity.findViewById(R.id.Category_expense);
        mPagatoDaTextView = (TextView) myActivity.findViewById(R.id.pagatoda_name_expense);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();



        //SECONDO DB
        databaseReference = FirebaseDatabase.getInstance().getReference("Activities").child(mExpenseId); //"1"
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getChildrenCount()>0) {

                    //prendere il titolo della spesa
                    if(dataSnapshot.child("Name").getValue(String.class).equals("Pagamento") || dataSnapshot.child("Name").getValue(String.class).equals("Payment") ){

                        mTitleTextView.setText(getString(R.string.textview_payment));
                    }
                    else{

                        mTitleTextView.setText(dataSnapshot.child("Name").getValue(String.class));
                    }





                    //prendere l'id del gruppi
                    String id_group = dataSnapshot.child("GroupId").getValue(String.class);
                    //prendere il nome del gruppo e settarlo

                    dbref2 = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Name");
                    dbref2.addValueEventListener(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                         mGroupTextView.setText(dataSnapshot.getValue(String.class));
                                                     }

                                                     @Override
                                                     public void onCancelled(DatabaseError databaseError) {

                                                     }
                                                 }
                    );

                    mDateTextView.setText(dataSnapshot.child("Date").getValue(String.class));
                    mAmountTextView.setText(String.format("%.2f", dataSnapshot.child("Total").getValue(Double.class)));
                    //mAmountCurrencyTextView.setText(CurrencyEditor.getShortSymbolFromSymbol(dataSnapshot.child("Currency").getValue(String.class), "€"));
                    mAmountCurrencyTextView.setText(CurrencyEditor.getShortSymbolFromSymbol(dataSnapshot.child("Currency").getValue(String.class), "€"));

                    String category = dataSnapshot.child("Category").getValue(String.class);
                    if(isAdded() ) {
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
                        mCategoryTextView.setText(category);
                    }

                }







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // ELIMINARE I LISTENER
    }

    /*private void updateUI()
    {
        DetailEntryLab detailEntryLab = new DetailEntryLab();//DetailEntryLab.get(getActivity());
        List<DetailEntry> detailEntries = detailEntryLab.getDetailEntries();

        mAdapter = new ActivityDetailAdapter(detailEntries);
        mRecyclerView.setAdapter(mAdapter);
    }*/

    private void updateUI()
    {

        final List<NomeDovuto> detailEntries =new ArrayList<>();

        ///////// DB

        dbref3 = FirebaseDatabase.getInstance().getReference("Activities").child(mExpenseId); //"1"
        dbref3.child("Owner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(inizio==true){
                    inizio=false;
                    detailEntries.clear();

                }
                else{
                    inizio=true;
                }

                for(DataSnapshot user : dataSnapshot.getChildren()){



                    NomeDovuto entry = new NomeDovuto(user.child("Name").getValue(String.class),user.child("Total").getValue(Double.class));
                    //entry.setCurrencySymbol(CurrencyEditor.getShortSymbolFromSymbol(entry.getCurrencySymbol(), "€")); //€
                    entry.setCurrencySymbol(user.child("Currency").getValue(String.class));
                    entry.setId(user.getKey());

                    //prendere OWNER DA tESTARE
                    //mPagatoDaTextView.setText(  dataSnapshot.child("Owner").child( dataSnapshot.child("Owner").getKey() ).child("Name").getValue(String.class));
                    mPagatoDaTextView.setText(  entry.getName());

                    //Toast.makeText(getContext(),entry.getName(), Toast.LENGTH_SHORT).show();

                    detailEntries.add(entry);

                }


                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        dbref3.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(inizio==true){
                    inizio=false;
                    detailEntries.clear();

                }else{
                    inizio=true;
                }




                for(DataSnapshot user : dataSnapshot.getChildren()){



                    NomeDovuto entry = new NomeDovuto(user.child("Name").getValue(String.class),user.child("Total").getValue(Double.class));
                    //entry.setCurrencySymbol(CurrencyEditor.getShortSymbolFromSymbol(entry.getCurrencySymbol(), "€")); //€
                    entry.setCurrencySymbol(user.child("Currency").getValue(String.class));
                    entry.setId(user.getKey());

                    //Toast.makeText(getContext(),entry.getName(), Toast.LENGTH_SHORT).show();

                    detailEntries.add(entry);

                }

                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });





        mAdapter = new ActivityDetailAdapter(detailEntries);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class ActivityDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private NomeDovuto mDetailEntry;
        private TextView mUserNameTextView;
        private TextView mAmountTextView;

        public ActivityDetailHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_activity_detail, parent, false));
            itemView.setOnClickListener(this);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.detail_user_name);
            mAmountTextView = (TextView) itemView.findViewById(R.id.detail_amount);
        }

        public void bind(NomeDovuto detailEntry)
        {
            mDetailEntry = detailEntry;
            mUserNameTextView.setText(mDetailEntry.getName());
            mAmountTextView.setText(String.format("%.2f",mDetailEntry.getDovuto()) + CurrencyEditor.getShortSymbolFromSymbol(mDetailEntry.getCurrencySymbol(),"€"));// CurrencyEditor.getShortSymbolFromSymbol(mDetailEntry.getCurrencySymbol(),"€"));
            try {
                if (mDetailEntry.getDovuto().toString().charAt(0) == '-') {
                    mAmountTextView.setTextColor(Color.RED);//parseColor("#d02020"));
                } else {
                    mAmountTextView.setTextColor(Color.RED);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {}
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(getActivity(),
                    mDetailEntry.getName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /*private class ActivityDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private DetailEntry mDetailEntry;
        private TextView mUserNameTextView;
        private TextView mAmountTextView;

        public ActivityDetailHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_activity_detail, parent, false));
            itemView.setOnClickListener(this);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.detail_user_name);
            mAmountTextView = (TextView) itemView.findViewById(R.id.detail_amount);
        }

        public void bind(DetailEntry detailEntry)
        {
            mDetailEntry = detailEntry;
            mUserNameTextView.setText(mDetailEntry.getUserName());
            mAmountTextView.setText(mDetailEntry.getAmount() + mDetailEntry.getCurrencySymbol());
            try {
                if (mDetailEntry.getAmount().charAt(0) == '-') {
                    mAmountTextView.setTextColor(Color.BLACK);//parseColor("#d02020"));
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
                    mDetailEntry.getUserName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

    /*private class ActivityDetailAdapter extends RecyclerView.Adapter<ActivityDetailHolder>
    {
        private List<DetailEntry> mDetailEntries;

        public ActivityDetailAdapter(List<DetailEntry> detailEntries)
        {
            mDetailEntries = detailEntries;
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
            DetailEntry detailEntry = mDetailEntries.get(position);
            holder.bind(detailEntry);
        }

        @Override
        public int getItemCount()
        {
            return mDetailEntries.size();
        }
    }*/

    private class ActivityDetailAdapter extends RecyclerView.Adapter<ActivityDetailHolder>
    {
        private List<NomeDovuto> mDetailEntries;

        public ActivityDetailAdapter(List<NomeDovuto> detailEntries)
        {
            mDetailEntries = detailEntries;
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
            NomeDovuto detailEntry = mDetailEntries.get(position);
            if(detailEntry!=null)
            holder.bind(detailEntry);
        }

        @Override
        public int getItemCount()
        {
            return mDetailEntries.size();
        }
    }
}
