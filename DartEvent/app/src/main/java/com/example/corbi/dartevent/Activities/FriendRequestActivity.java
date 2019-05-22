package com.example.corbi.dartevent.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.corbi.dartevent.Adapters.FriendRequestAdapter;
import com.example.corbi.dartevent.Adapters.FriendsListAdapter;
import com.example.corbi.dartevent.R;
import com.example.corbi.dartevent.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRequestActivity extends AppCompatActivity {

    private ArrayList<User>friends;
    private FriendRequestAdapter mAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        listView = findViewById(R.id.friend_request_listview);
    }

    @Override
    public void onResume(){
        super.onResume();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
        }
        else {
            final String mUserId = mFirebaseUser.getUid();
            final String mUserEmail = mFirebaseUser.getEmail();
            //sets up the listview
            friends = new ArrayList<>();
            mAdapter = new FriendRequestAdapter(this, friends);
            listView.setAdapter(mAdapter);

            //get the friends
            mDatabase.child("users").child(mUserId).child("requests").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User friend = new User();
                    //set values for list view object
                    friend.setUserEmail((String) dataSnapshot.child("userEmail").getValue());
                    friend.setUserID((String) dataSnapshot.child("userID").getValue());
                    //add it to adapter
                    mAdapter.add(friend);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    User friend = new User();
                    //set values for list view object
                    friend.setUserEmail((String) dataSnapshot.child("userEmail").getValue());
                    friend.setUserID((String) dataSnapshot.child("userID").getValue());
                    //add it to adapter
                    mAdapter.remove(friend);
                    mAdapter.notifyDataSetChanged();
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
                    final Dialog dialog = new Dialog(FriendRequestActivity.this);
                    dialog.setTitle("Create account");
                    dialog.setContentView(R.layout.add_friend_request_dialog);
                    dialog.show();
                    final User selected = (User)parent.getItemAtPosition(position);
                    TextView emailConfirm = dialog.findViewById(R.id.add_friend_request_email_dialog);
                    emailConfirm.setText(selected.getUserEmail());
                    Button yesButton = dialog.findViewById(R.id.add_friend_request_yes_button);
                    Button noButton = dialog.findViewById(R.id.add_friend_request_no_button);
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child("users").child(mUserId).child("requests")
                                    .orderByChild("userEmail").equalTo(selected.getUserEmail())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                User temp = new User();
                                                temp.setUserEmail((String)snapshot.child("userEmail").getValue());
                                                temp.setUserID((String)snapshot.child("userID").getValue());
                                                User currTemp = new User();
                                                currTemp.setUserEmail(mFirebaseUser.getEmail());
                                                currTemp.setUserID(mFirebaseUser.getUid());
                                                mDatabase.child("users").child(mUserId).child("friends").push().setValue(temp);
                                                snapshot.getRef().removeValue();
                                                finish();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            mDatabase.child("users").child(selected.getUserID()).child("requests")
                                    .orderByChild("userEmail").equalTo(mUserEmail)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                User temp = new User();
                                                temp.setUserEmail((String)snapshot.child("userEmail").getValue());
                                                temp.setUserID((String)snapshot.child("userID").getValue());
                                                mDatabase.child("users").child(selected.getUserID()).child("friends").push().setValue(temp);
                                                snapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            dialog.dismiss();
                        }
                    });
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child("users").child(mUserId).child("requests")
                                    .orderByChild("userEmail").equalTo(selected.getUserEmail())
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
                            mDatabase.child("users").child(selected.getUserID()).child("requests")
                                    .orderByChild("userEmail").equalTo(mUserEmail)
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
                            dialog.dismiss();
                        }
                    });
                }
            });
        }

        }
    }

