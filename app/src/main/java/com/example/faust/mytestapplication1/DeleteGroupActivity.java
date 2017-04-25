package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group);


        String name_group = getIntent().getExtras().getString("NAME_GROUP");


        String s = getString(R.string.message_deleteGroup);
        TextView tv = (TextView) findViewById(R.id.textview_eliminato);
        tv.setText(name_group +" "+s);


        Button b= (Button) findViewById(R.id.button_home);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DeleteGroupActivity.this,MainActivity.class);
                startActivity(intent);



            }
        });
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Toast.makeText(DeleteGroupActivity.this,R.string.impossible_to_come_back,Toast.LENGTH_LONG).show();
    }
}
