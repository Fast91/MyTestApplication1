package com.example.faust.mytestapplication1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by robertospaziani on 06/05/17.
 */

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";
    private static final String EXTRA_EXPENSE_UUID = ".extra_expense_uuid";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification

        createNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
    }




    private void createNotification( String messageBody,final String id) {


        messageBody = messageBody.trim();

        FirebaseDatabase.getInstance().getReference().child("Groups").child(messageBody).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("Name").getValue(String.class);

               // Intent intent = new Intent( MyAndroidFirebaseMsgService.this , PrimaAttivitaGruppi.class );
                Intent intent = new Intent( MyAndroidFirebaseMsgService.this , ActivityDetailActivity.class );
                intent.putExtra(EXTRA_EXPENSE_UUID, id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent resultIntent = PendingIntent.getActivity( MyAndroidFirebaseMsgService.this , 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                String s = getString(R.string.newexpense_title2);
                String s3 = getString(R.string.newexpense_title3);

                Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( MyAndroidFirebaseMsgService.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(s)
                        .setContentText(s3 +" : "+name)
                        .setAutoCancel( true )
                        .setSound(notificationSoundURI)
                        .setContentIntent(resultIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, mNotificationBuilder.build());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


}
