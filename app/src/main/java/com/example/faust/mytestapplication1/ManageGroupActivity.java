package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageGroupActivity extends AppCompatActivity {

    private String id_group, name_group;

    private HashMap<String, NomeDovuto> utenti_dovuto;
    private FirebaseAuth firebaseAuth;

    private MyUsersGroupRecyclerViewModifyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);


        id_group = getIntent().getExtras().getString("ID_GROUP");
        name_group = getIntent().getExtras().getString("NAME_GROUP");
        utenti_dovuto = new HashMap<>();
        firebaseAuth = FirebaseAuth.getInstance();

        //Toast.makeText(ManageGroupActivity.this,""+id_group + " " + name_group,Toast.LENGTH_LONG).show(); Funziona


        TextView tv = (TextView) findViewById(R.id.group_name_text);
        tv.setText(name_group);




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Users");

        //Read content data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()==null){


                }

                //prendo un amico
                for (DataSnapshot friend : dataSnapshot.getChildren()) {

                    String id = (String) friend.getKey();
                    String nome = (String) friend.child("Name").getValue(String.class);


                    if (utenti_dovuto.get(id) == null || utenti_dovuto.containsKey(id) == false) {
                        //add
                        NomeDovuto iniziale = new NomeDovuto(id,nome);
                        iniziale.setId_Group(id_group);
                        iniziale.setName_Group(name_group);
                        utenti_dovuto.put(id, iniziale);


                    } else {

                        //faccio il get e il replace
                        NomeDovuto iniziale = utenti_dovuto.get(id);
                        utenti_dovuto.remove(id);
                        iniziale.setId_Group(id_group);
                        iniziale.setName_Group(name_group);
                        utenti_dovuto.put(id, iniziale);

                    }






                    //////////////
                    // SET ADAPTER



                        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.user_group_list_manage);
                        recyclerView2.setLayoutManager(new LinearLayoutManager(ManageGroupActivity.this));
                        List list = new ArrayList(utenti_dovuto.values());
                         adapter = new MyUsersGroupRecyclerViewModifyAdapter(list, id_group);
                        recyclerView2.setAdapter(adapter);



                    // END ADAPTER
                    //////////////



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}