package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button b2= (Button) findViewById(R.id.button_IT_sett);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("it");
                res2.updateConfiguration(conf2, dm2);


                Intent refresh = new Intent(SettingActivity.this, SettingActivity.class);
                startActivity(refresh);
                finish();




            }
        });


        Button b3= (Button) findViewById(R.id.button_EN_sett);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*

                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

               // rEditor.putString("language", languageToLoad);
                //rEditor.commit();

                */

                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("en");
                res2.updateConfiguration(conf2, dm2);


                Intent refresh = new Intent(SettingActivity.this, SettingActivity.class);
                startActivity(refresh);
                finish();





            }
        });




        Button b= (Button) findViewById(R.id.button_home_sett);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SettingActivity.this,PrimaAttivitaGruppi.class);

                startActivity(intent);
                finish();


            }
        });
    }
}
