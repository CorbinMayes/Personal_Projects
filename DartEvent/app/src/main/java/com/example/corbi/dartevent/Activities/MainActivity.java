package com.example.corbi.dartevent.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.corbi.dartevent.Adapters.ActionTabsViewPagerAdapter;
import com.example.corbi.dartevent.Adapters.EventCreateAdapter;
import com.example.corbi.dartevent.CustomViewPager;
import com.example.corbi.dartevent.Fragments.FriendEventFragment;
import com.example.corbi.dartevent.Fragments.MyEventFragment;
import com.example.corbi.dartevent.Fragments.PublicEventFragment;
import com.example.corbi.dartevent.NotificationService;
import com.example.corbi.dartevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    private CustomViewPager cvp;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uID = firebaseUser.getUid();

        if(!NotificationService.isIsRunning()){
            Intent intent = new Intent(this,NotificationService.class);
            startService(intent);
        }

        bnv = findViewById(R.id.bottom_nav);
        cvp = findViewById(R.id.myViewPager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new MyEventFragment());
        fragments.add(new FriendEventFragment());
        fragments.add(new PublicEventFragment());

        myViewPageAdapter = new ActionTabsViewPagerAdapter(getSupportFragmentManager(),fragments);
        cvp.setAdapter(myViewPageAdapter);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_bar_my_event:
                        cvp.setCurrentItem(0);
                        return true;
                    case R.id.nav_bar_friend_event:
                        cvp.setCurrentItem(1);
                        return true;
                    case R.id.nav_bar_public_event:
                        cvp.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_event,m);
        inflater.inflate(R.menu.main,m);
        inflater.inflate(R.menu.sync_stuff, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(MainActivity.this,EventCreateActivity.class);
                intent.putExtra("from", "MainActivity");
                startActivity(intent);
                return true;
            case R.id.add_friend:
                Intent intent1 = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent1);
                return true;
            case R.id.sign_out:
                Intent intent2 = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent2);
                firebaseAuth.signOut();
                finish();
                return true;
            case R.id.friend_list:
                Intent intent3 = new Intent(MainActivity.this,FriendsListActivity.class);
                startActivity(intent3);
                return true;
            case R.id.settings:
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("Public Event");
                dialog.setContentView(R.layout.notification_settings_dialog_layout);
                dialog.show();
                Button yesButton = dialog.findViewById(R.id.notification_setting_yes_button);
                Button noButton = dialog.findViewById(R.id.notification_setting_no_button);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NotificationService.isIsRunning()){
                            stopService(new Intent(MainActivity.this,NotificationService.class));
                        }
                        dialog.dismiss();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!NotificationService.isIsRunning()){
                            Intent intent = new Intent(MainActivity.this,NotificationService.class);
                            startService(intent);
                        }
                        dialog.dismiss();
                    }
                });
                return true;
            case R.id.friend_request:
                Intent intent4 = new Intent(MainActivity.this,FriendRequestActivity.class);
                startActivity(intent4);
                return true;
            case R.id.sync_db:
                startActivity(getIntent());
                finish();
                return true;
        }
        return true;
    }
}
