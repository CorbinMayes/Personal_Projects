package com.example.corbi.dartevent.Activities;

import android.app.Dialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.corbi.dartevent.Adapters.AllUsersAdapter;
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

public class FriendsListActivity extends AppCompatActivity {

    private ArrayList<User>friends;
    private FriendsListAdapter mAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        listView = findViewById(R.id.friends_list_listview);
    }

    @Override
    public void onResume(){
        super.onResume();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
        }
        else {
            final String mUserId = mFirebaseUser.getUid();
            final String mUserEmail = mFirebaseUser.getEmail();
            //sets up the listview
            friends = new ArrayList<>();
            mAdapter = new FriendsListAdapter(this, friends);
            listView.setAdapter(mAdapter);

            //get the friends
            mDatabase.child("users").child(mUserId).child("friends").addChildEventListener(new ChildEventListener() {
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
                    finish();
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
                    final Dialog dialog = new Dialog(FriendsListActivity.this);
                    dialog.setTitle("Create account");
                    dialog.setContentView(R.layout.remove_friend_dialog);
                    dialog.show();
                    final User selected = (User)parent.getItemAtPosition(position);
                    TextView emailConfirm = dialog.findViewById(R.id.remove_friend_email_dialog);
                    emailConfirm.setText(selected.getUserEmail());
                    Button yesButton = dialog.findViewById(R.id.remove_friend_yes_button);
                    Button noButton = dialog.findViewById(R.id.remove_friend_no_button);
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child("users").child(mUserId).child("friends").orderByChild("userEmail").equalTo(selected.getUserEmail())
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
                            mDatabase.child("users").child(selected.getUserID()).child("friends").orderByChild("userEmail").equalTo(mUserEmail)
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
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }

        }
    }

