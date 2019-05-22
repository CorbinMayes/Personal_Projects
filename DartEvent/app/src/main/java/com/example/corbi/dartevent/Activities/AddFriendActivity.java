package com.example.corbi.dartevent.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.corbi.dartevent.Adapters.AllUsersAdapter;
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
import java.util.HashMap;
import java.util.Objects;

public class AddFriendActivity extends AppCompatActivity {
    String emailToFind;
    ListView friendView;
    TextInputEditText emailSearch;
    private AllUsersAdapter allUsersAdapter;

    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        friendView = findViewById(R.id.friend_list);
        emailSearch = findViewById(R.id.email_search);
        emailToFind = "";
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        mUserId = mUser.getUid();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayList<User> list = new ArrayList<>();
        allUsersAdapter = new AllUsersAdapter(this, list);
        friendView.setAdapter(allUsersAdapter);

        if (mFirebaseUser == null) {

        } else {
            mDatabase.child("allUsers").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final User usr = new User();
                    HashMap<String, String> userInfo = (HashMap<String, String>) dataSnapshot.getValue();
                    assert userInfo != null;
                    usr.setUserEmail(userInfo.get("userEmail"));
                    usr.setUserID(userInfo.get("userID"));
                    if(!usr.getUserID().equals(mUserId)) {
                        mDatabase.child("users").child(mUserId).child("friends").orderByChild("userEmail").equalTo(usr.getUserEmail())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        boolean contains = false;
                                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            if(Objects.equals(snapshot.child("userEmail").getValue(), usr.getUserEmail())){
                                                contains = true;
                                            }
                                        }
                                        if(!contains){
                                            if (!emailToFind.equals("")) {
                                                if (emailToFind.equals(usr.getUserEmail())) {
                                                    allUsersAdapter.add(usr);
                                                }
                                            } else {
                                                allUsersAdapter.add(usr);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
            friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Dialog dialog = new Dialog(AddFriendActivity.this);
                    dialog.setTitle("Create account");
                    dialog.setContentView(R.layout.add_new_friend_dialog_layout);
                    dialog.show();
                    TextView friendConfirm = dialog.findViewById(R.id.thisFriend);
                    final User item = (User) parent.getItemAtPosition(position);
                    friendConfirm.setText("Add friend " + item.getUserEmail());
                    Button yesButton = dialog.findViewById(R.id.yes_button_2);
                    Button noButton = dialog.findViewById(R.id.no_button_2);
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            assert mFirebaseUser != null;
                            User ensure = new User(mFirebaseUser.getEmail(), mFirebaseUser.getUid());
                            String mUserId = mFirebaseUser.getUid();
                            mDatabase.child("users").child(mUserId).child("requests").push().setValue(item);
                            mDatabase.child("users").child(item.getUserID()).child("requests").push().setValue(ensure);
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

    public void Search(View view) {
        final String email = emailSearch.getText().toString();
        emailSearch.setText("");
        emailToFind = email;
        onResume();
    }
}
