package com.example.corbi.dartevent.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.CalendarContract;

import com.example.corbi.dartevent.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ExerciseDBASyncTask extends AsyncTask<Void, Void, Void> {
    private Event event;
    Context context;
    String from;

    ExerciseDBASyncTask(Event entry, Context context, String from) {
        this.event = entry;
        this.context = context;
        this.from = from;
    }

    @Override
    protected Void doInBackground(Void... unused) {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String mUserId = mFirebaseUser.getUid();
        if(from.equals("MainActivity")) {

            DatabaseReference key = mDatabase.child("users").child(mUserId).child("events").push();
            key.setValue(event);
            if(event.isPublic().equals("true")){
                mDatabase.child("public").child("events").child(key.getKey()).setValue(event);
            }
        }
        else if(from.equals("MyEventFrag")) {
            Map<String, Object> map = new HashMap<>();
            map.put(event.getId(), event);
            if(event.isPublic().equals("true")){
                mDatabase.child("public").child("events").updateChildren(map);
            }
            mDatabase.child("users").child(mUserId).child("events").updateChildren(map);
        }
        else if(from.equals("remove")) {
            if(event.isPublic().equals("true")){
                mDatabase.child("public").child("events").child(event.getId()).removeValue();
            }
            mDatabase.child("users").child(mUserId).child("events").child(event.getId()).removeValue();
        }
        return null;
    }
}
