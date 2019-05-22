package com.example.corbi.dartevent.Fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.corbi.dartevent.Activities.MapsActivity;
import com.example.corbi.dartevent.Adapters.MyEventAdapter;
import com.example.corbi.dartevent.Adapters.PublicEventAdapter;
import com.example.corbi.dartevent.Event;
import com.example.corbi.dartevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class PublicEventFragment extends Fragment {


    private ArrayList<Event> arrayList;
    private PublicEventAdapter PublicEventAdapter;
    private ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_public_event, container, false);
        listView = v.findViewById(R.id.publicevent_listview);

        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {

        } else {
            String mUserId = mFirebaseUser.getUid();

            // Set up ListView
            arrayList = new ArrayList<>();
            PublicEventAdapter = new PublicEventAdapter(Objects.requireNonNull(getContext()), arrayList);
            listView.setAdapter(PublicEventAdapter);

            // Use Firebase to populate the list.
            mDatabase.child("public").child("events").addChildEventListener(new ChildEventListener() {

                // When the app starts this callback will add all items to the listview
                // or if a new item is added the "add new item" button or if an item
                // has been added to via the console

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Event event = new Event();

                    event.setTitle((String) dataSnapshot.child("title").getValue());
                    event.setDateTime((String) dataSnapshot.child("dateTime").getValue());
                    event.setDetails((String) dataSnapshot.child("details").getValue());
                    event.setUserEmail((String) dataSnapshot.child("userEmail").getValue());
                    event.setAddress((String) dataSnapshot.child("address").getValue());
                    event.setLocation((String) dataSnapshot.child("location").getValue());
                    event.setPublic(((String) dataSnapshot.child("public").getValue()));
                    event.setId(dataSnapshot.getKey());

                    PublicEventAdapter.add(event);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                // The record has been removed from the db, now remove from the listview
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    Event event = (Event) parent.getItemAtPosition(position);
                    intent.putExtra("from", "FriendEventFrag");
                    intent.putExtra("title", event.getTitle());
                    intent.putExtra("detail", event.getDetails());
                    intent.putExtra("location", event.getLocation());
                    intent.putExtra("address", event.getAddress());
                    startActivity(intent);
                    
                }
            });
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        mDatabase.child("public").child("events").orderByChild("dateTime").endAt(format.format(c.getTime()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
