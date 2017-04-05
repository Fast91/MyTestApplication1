package com.example.faust.mytestapplication1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityDetail extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
