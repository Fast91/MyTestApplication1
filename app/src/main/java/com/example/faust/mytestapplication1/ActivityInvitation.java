package com.example.faust.mytestapplication1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActivityInvitation extends AppCompatActivity {
    private EditText pintex;
    private ImageButton submit;
    private String pin;
    private DatabaseReference dbref;
    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseAuth firebaseAuth;
    private HashMap<String, String> key_nameuser;
    private String id_nuovoutentedaaggiungere, name_nuovoutentedaaggiungere, name_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        submit = (ImageButton) findViewById(R.id.imageButtonInvitation);
        submit.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.user512, 100, 100));

        pintex = (EditText) findViewById(R.id.invitation_pin);
        firebaseAuth = FirebaseAuth.getInstance();
        key_nameuser = new HashMap<>();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //controllo se ha inserito un pin
                pin = pintex.getText().toString().trim();

                if (pin.isEmpty()) {

                    Toast.makeText(getApplicationContext(), R.string.enterpinplease, Toast.LENGTH_LONG);


                } else {


                    checkandAddUser();

                }

            }
        });


    }


    private void checkandAddUser() {

        //Step 0 controllare se esiste quel gruppo con quel ID

        dbref = FirebaseDatabase.getInstance().getReference("Groups");
        id_nuovoutentedaaggiungere = firebaseAuth.getCurrentUser().getUid();


        //Read content data
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(pin)) {
                    //Se esiste aggiungo questo utente loggato al gruppo

                    //todo controllare se funziona
                    name_group = dataSnapshot.child(pin).child("Name").getValue(String.class);


                    ////////////////step 0
                    /// Prendere gli utenti di questo gruppo vecchio
                    //step 0 prendere gli utenti già registrati
                    // GROUPS - ID - USERS


                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(pin).child("Users");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Per tutti gli utenti aggiungerli
                            for (DataSnapshot users : dataSnapshot.getChildren()) {

                                key_nameuser.remove(users.getKey());
                                key_nameuser.put(users.getKey(), users.child("Name").getValue(String.class));

                            }


                            //controllare se già è stato aggiunto
                            if (key_nameuser.containsKey(id_nuovoutentedaaggiungere)) {

                                Toast.makeText(getApplicationContext(), R.string.toast_addedusergroupAlreadyAdded, Toast.LENGTH_LONG).show();

                            } else {


                                ///////////////step 3
                                /// Settarli nell utente loggato con totale 0
                                /// e fare l'inverso settare agli altri utenti con l'utente loggato 0

                                ///step 1 prendo il nome dell'utente loggato e lo setto nel gruppo
                                //setto l'utente loggato prima mi serve il suo NOME
                                databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

                                //Read content data
                                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        String name_user = dataSnapshot.child("Name").getValue(String.class);
                                        name_nuovoutentedaaggiungere = name_user;

                                        String s = new String(name_nuovoutentedaaggiungere );
                                        //setto il nome all'utente loggato
                                        FirebaseDatabase.getInstance().getReference("Groups").child(pin)
                                                .child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Name").setValue(s);

                                        addUserToGroup();


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                                Intent intent = new Intent(ActivityInvitation.this, PrimaAttivitaGruppi.class);
                                startActivity(intent);
                                finish();



                                Toast.makeText(getApplicationContext(), R.string.toast_addedusergroup, Toast.LENGTH_LONG).show();

                            }


                        }//fine onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });















                } else {
                    //Se non esiste TOAST
                    Toast.makeText(getApplicationContext(), R.string.pinnotregistred, Toast.LENGTH_LONG);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        return;
    }//fine metodo checkandadd


    private void addUserToGroup() {

        Log.d("EXISTS", "INIZIO ADDUSERTOGROUP");

        //step 1
        // aggiunge al gruppo l'utente
        // groups - idgruppo - users - idutente - name

        String s = new String(name_nuovoutentedaaggiungere );

        FirebaseDatabase.getInstance().getReference("Groups").child(pin)
                .child("Users").child(id_nuovoutentedaaggiungere).child("Name").setValue(s);


        //step 2
        // aggiungere l'utente al gruppo
        // users- id utente - groups - id group ---> namegroup + total 0

        String s2 = new String(name_group );

        FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                .child("Groups").child(pin).child("Name").setValue(s2);


        //setto il bilancio del gruppo a 0
        Double tmp = 0.0;
        FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                .child("Groups").child(pin).child("Total").setValue(tmp);


        // users- id utente - groups - id group ---> users settare tutti gli utente già inseriti e tutto a 0 + name

        for (String id_user : key_nameuser.keySet()) {

            if (!id_user.equals(id_nuovoutentedaaggiungere)) {


                String Name = key_nameuser.get(id_user);
                tmp = 0.0;

                FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                        .child("Groups").child(pin).child("Users").child(id_user)
                        .child("Name").setValue(Name);

                FirebaseDatabase.getInstance().getReference("Users").child(id_nuovoutentedaaggiungere)
                        .child("Groups").child(pin).child("Users").child(id_user)
                        .child("Total").setValue(tmp);

            }

        }


        //step 3
        // aggiungere agli utenti già inseriti questo nuovo utente --> name + con total 0

        for (String id_user : key_nameuser.keySet()) {


            if (!id_user.equals(id_nuovoutentedaaggiungere)) {


                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(id_user)
                        .child("Groups");


                dbref.child(pin).child("Users").child(id_nuovoutentedaaggiungere)
                        .child("Total").setValue(tmp);

                String nuovastringanome = new String(name_nuovoutentedaaggiungere);

                dbref.child(pin).child("Users").child(id_nuovoutentedaaggiungere)
                        .child("Name").setValue(nuovastringanome);


            }


        }


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