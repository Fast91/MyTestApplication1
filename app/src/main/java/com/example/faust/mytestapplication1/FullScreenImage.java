package com.example.faust.mytestapplication1;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp ;
        Bitmap bmp2 = (Bitmap) extras.getParcelable("imagebitmap2");


        if(bmp2!=null) {
            bmp = bmp2;
        }

        else{
            bmp = (Bitmap) extras.getParcelable("imagebitmap");
        }



        ImageView imgDisplay;
        Button btnClose;


        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (Button) findViewById(R.id.btnClose);


        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });

    if(bmp!=null) {
        imgDisplay.setImageBitmap(bmp);
    }



    }
}
