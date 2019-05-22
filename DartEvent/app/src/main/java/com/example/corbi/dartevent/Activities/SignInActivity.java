package com.example.corbi.dartevent.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.corbi.dartevent.R;
import com.example.corbi.dartevent.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    private String Key_Email = "email_key";
    private String Key_Password = "password_key";
    TextInputEditText mEmailSignIn;
    TextInputEditText mPasswordSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmailSignIn = findViewById(R.id.email_signin);
        mPasswordSignIn = findViewById(R.id.password_signin);
        mAuth = FirebaseAuth.getInstance();

        if(savedInstanceState!=null){
            ((TextInputEditText)findViewById(R.id.email_signin)).setText(savedInstanceState.getString(Key_Email));
            ((TextInputEditText)findViewById(R.id.password_signin)).setText(savedInstanceState.getString(Key_Password));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(Key_Email, (String)((TextInputEditText)findViewById(R.id.email_signin)).getText().toString());
        outState.putString(Key_Password, (String)((TextInputEditText)findViewById(R.id.password_signin)).getText().toString());
    }

    public void onSignInClick(View v){
        mAuth.signInWithEmailAndPassword(((TextInputEditText)mEmailSignIn).getText().toString(),((TextInputEditText)mPasswordSignIn).getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                            final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            final ArrayList<String> arrayList = new ArrayList<>();
                            mDatabase.child("allUsers").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    User user = new User();
                                    user.setUserEmail((String) dataSnapshot.child("userEmail").getValue());
                                    user.setUserID((String) dataSnapshot.child("userID").getValue());
                                    arrayList.add(user.getUserEmail());
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
                            if(arrayList.contains(mEmailSignIn.getText())) {
                                final Dialog dialog = new Dialog(SignInActivity.this);
                                dialog.setTitle("Create account");
                                dialog.setContentView(R.layout.create_email_dialog_layout);
                                dialog.show();
                                TextView emailConfirm = dialog.findViewById(R.id.email_dialog);
                                emailConfirm.setText(((TextInputEditText)mEmailSignIn).getText().toString());
                                Button yesButton = dialog.findViewById(R.id.yes_button);
                                Button noButton = dialog.findViewById(R.id.no_button);
                                yesButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mAuth.createUserWithEmailAndPassword(mEmailSignIn.getText().toString(),mPasswordSignIn.getText().toString()).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isComplete()) {

                                                    String userEmail = mFirebaseUser.getEmail();
                                                    String userID = mFirebaseUser.getUid();
                                                    User user = new User(userEmail, userID);
                                                    mDatabase.child("allUsers").push().setValue(user);
                                                    Toast.makeText(SignInActivity.this, "Authentication Successful.\nTap Sign-in",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        dialog.dismiss();
                                    }
                                });
                                noButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mEmailSignIn.setText("");
                                        mPasswordSignIn.setText("");
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(SignInActivity.this, "Email already exists",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }
}
