package com.example.faust.mytestapplication1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class ActivityDetailActivity extends AppCompatActivity
{
    private static final String EXTRA_EXPENSE_UUID = "com.example.faust.mytestapplication1.expense_uuid";

    private TextView mTitleTextView;
    private ImageView mImageView;
    private TextView mGroupTextView;
    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mCategoryTextView;
    private TextView mDescriptionTextView;

    public static Intent newIntent(Context packageContext, UUID uuid)
    {
        Intent i = new Intent(packageContext, ActivityDetailActivity.class);
        i.putExtra(EXTRA_EXPENSE_UUID, uuid);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_EXPENSE_UUID);
        //TODO cercare l'uuid nel db per prendere i dati

        mTitleTextView = (TextView) findViewById(R.id.title_expense);
        mImageView = (ImageView) findViewById(R.id.imageDetailPicture);
        mGroupTextView = (TextView) findViewById(R.id.Group_expense);
        mDateTextView = (TextView) findViewById(R.id.Date_expense);
        mAmountTextView = (TextView) findViewById(R.id.Total_expense);
        mCategoryTextView = (TextView) findViewById(R.id.Category_expense);
        mDescriptionTextView = (TextView) findViewById(R.id.Description_expense);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_detail_fragment);

        if(fragment == null)
        {
            fragment = new ActivityDetailFragment();
            fm.beginTransaction()
                    .add(R.id.activity_detail_fragment, fragment)
                    .commit();
        }
    }
}
