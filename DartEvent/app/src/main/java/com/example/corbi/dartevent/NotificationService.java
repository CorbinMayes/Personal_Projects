package com.example.corbi.dartevent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.corbi.dartevent.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String mUserId;
    private DatabaseReference mDatabase;

    private static boolean isRunning = false;

    @Override
    public void onCreate(){
        super.onCreate();
        isRunning = true;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mDatabase.child("users").child(mUserId).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDatabase.child("users").child((String)snapshot.child("userID").getValue()).child("events")
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    //creating the intent that happens when the notification is clicked
                                    Context context = getApplicationContext();
                                    String notificationTitle = "Event Added";
                                    String notificationText = (String)snapshot.child("userEmail").getValue()+" has added a new event!";
                                    Intent notificationIntent = new Intent(context,MainActivity.class);

                                    //the pending intent to run
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_ONE_SHOT);
                                    NotificationChannel notificationChannel = new NotificationChannel("notification channel","add notification", NotificationManager.IMPORTANCE_DEFAULT);
                                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"notification channel")
                                            .setContentTitle(notificationTitle)
                                            .setContentText(notificationText)
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setContentIntent(pendingIntent);
                                    Notification notification = notificationBuilder.build();

                                    //getting the Notification Manager
                                    NotificationManager notificationManager = (NotificationManager)
                                            getSystemService(NOTIFICATION_SERVICE);
                                    if (notificationManager != null) {
                                        notificationManager.createNotificationChannel(notificationChannel);
                                        // 0 - can be any number. An identifier for this notification to be unique within
                                        // your application.
                                        notificationManager.notify(0, notification);
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    //creating the intent that happens when the notification is clicked
                                    Context context = getApplicationContext();
                                    String notificationTitle = "Event Changed";
                                    String notificationText = (String)snapshot.child("userEmail").getValue()+" has changed an event!";
                                    Intent notificationIntent = new Intent(context,MainActivity.class);

                                    //the pending intent to run
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_ONE_SHOT);
                                    NotificationChannel notificationChannel = new NotificationChannel("notification channel","change notification", NotificationManager.IMPORTANCE_DEFAULT);
                                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"notification channel")
                                            .setContentTitle(notificationTitle)
                                            .setContentText(notificationText)
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setContentIntent(pendingIntent);
                                    Notification notification = notificationBuilder.build();

                                    //getting the Notification Manager
                                    NotificationManager notificationManager = (NotificationManager)
                                            getSystemService(NOTIFICATION_SERVICE);
                                    if (notificationManager != null) {
                                        notificationManager.createNotificationChannel(notificationChannel);
                                        // 0 - can be any number. An identifier for this notification to be unique within
                                        // your application.
                                        notificationManager.notify(0, notification);
                                    }
                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isRunning = false;

        NotificationManager notificationManager;
        notificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        if (notificationManager != null)
            notificationManager.cancelAll();
    }

    public static boolean isIsRunning() {
        return isRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
