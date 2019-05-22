package com.example.corbi.dartevent;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Event {
    private String userEmail;
    private String title;
    private Calendar dateTime;
    private String address;
    private LatLng location;
    private String details;
    private boolean isPublic;
    private String id;

    public Event() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userEmail = mFirebaseUser.getEmail();
        isPublic = false;
        dateTime = Calendar.getInstance();
        title = "Event";
        details = "";
        address = "";
        location = new LatLng(0,0);
        id = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location.latitude + "," + location.longitude;
    }

    public void setLocation(String location) {
        String[] latLng = location.split(",");
        this.location = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String isPublic() {
        return ""+isPublic;
    }

    public void setPublic(String aPublic) {
        isPublic = Boolean.parseBoolean(aPublic);
    }

    public String getDateTime() {
        return getDateString() + getTimeString();
    }

    @Exclude
    public String getDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        return format1.format(dateTime.getTime());
    }

    @Exclude
    public String getTimeString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        return format2.format(dateTime.getTime());
    }

    public void setDateTime(String dateTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDateTime = new SimpleDateFormat("MM-dd-yyyyHH:mm", Locale.ENGLISH);
        try {
            this.dateTime.setTime(formatDateTime.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Exclude
    public Calendar getObjDateTime() {
        return dateTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setObjDateTime(Calendar calendar) {
        this.dateTime = calendar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
