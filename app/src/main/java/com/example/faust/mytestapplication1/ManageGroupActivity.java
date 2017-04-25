package com.example.faust.mytestapplication1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ManageGroupActivity extends AppCompatActivity {

    private String id_group, name_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);




        id_group = getIntent().getExtras().getString("ID_GROUP");
        name_group = getIntent().getExtras().getString("NAME_GROUP");

        //Toast.makeText(ManageGroupActivity.this,""+id_group + " " + name_group,Toast.LENGTH_LONG).show(); Funziona
    }
}
