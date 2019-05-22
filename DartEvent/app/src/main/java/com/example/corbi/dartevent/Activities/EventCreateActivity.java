package com.example.corbi.dartevent.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.corbi.dartevent.Adapters.EventCreateAdapter;
import com.example.corbi.dartevent.Event;
import com.example.corbi.dartevent.EventInput;
import com.example.corbi.dartevent.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class EventCreateActivity extends AppCompatActivity {

    private Context context;
    private Event event;
    private ListView listView;
    private final String DETAILS_ID = "Details";
    private final String TITLE_ID = "Title";
    private final String DATE_ID = "Date";
    private final String TIME_ID = "Time";
    private final String PUBLIC_ID = "Public Event";
    private String from;

    public final static String Action = "EventCreate.Address";
    public final static String AddressGetKey = "getAddress";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(from.equals("MyEventFrag")) {
            menuInflater.inflate(R.menu.delete_event, menu);
        }
        //sfdsf
        menuInflater.inflate(R.menu.save_event, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_db:
                ExerciseDBASyncTask exerciseDBASyncTask = new ExerciseDBASyncTask(event, context, from);
                exerciseDBASyncTask.execute();
                //unregisterReceiver(addressReceiver);
                finish();
                return true;
            case R.id.remove_from_db:
                from = "remove";
                exerciseDBASyncTask = new ExerciseDBASyncTask(event, context, from);
                exerciseDBASyncTask.execute();
                //unregisterReceiver(addressReceiver);
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        event = new Event();
        from = Objects.requireNonNull(getIntent().getExtras()).getString("from");
        assert from != null;
        if(from.equals("MyEventFrag")) {
            event.setUserEmail(getIntent().getExtras().getString("userEmail"));
            event.setTitle(getIntent().getExtras().getString("title"));
            event.setDateTime(getIntent().getExtras().getString("dateTime"));
            event.setAddress(getIntent().getExtras().getString("address"));
            event.setDetails(getIntent().getExtras().getString("details"));
            event.setPublic(getIntent().getExtras().getString("public"));
            event.setId(getIntent().getExtras().getString("id"));
        }
        context = this;
        final ArrayList<EventInput> arrayOfEventDetails;
        arrayOfEventDetails = buildItems();
        EventCreateAdapter adapter = new EventCreateAdapter(this, arrayOfEventDetails);
        registerReceiver(addressReceiver,new IntentFilter(EventCreateActivity.Action));
        LocalBroadcastManager.getInstance(this).registerReceiver(addressReceiver, new IntentFilter(EventCreateActivity.Action));
        listView = findViewById(R.id.EventDetails);
        listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                    switch (position) {
                        case 4:
                            createDialog(DETAILS_ID);
                            break;
                        case 0:
                            createDialog(TITLE_ID);
                            break;
                        case 2:
                            final Calendar currentDate = event.getObjDateTime();
                            createDateDialog(TIME_ID, currentDate);
                            break;
                        case 1:
                            final Calendar mcurrentTime = event.getObjDateTime();
                            createDateDialog(DATE_ID, mcurrentTime);
                            break;
                        case 5:
                            createPublicDialog(PUBLIC_ID);
                            break;
                        case 3:
                            Intent intent = new Intent(EventCreateActivity.this,MapsActivity.class);
                            intent.putExtra("from", "EventCreateActivity");
                            startActivity(intent);
                    }
                }
            });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(addressReceiver!=null) {
            unregisterReceiver(addressReceiver);
        }
    }
    private ArrayList<EventInput> buildItems() {
        ArrayList<EventInput> arrayList = new ArrayList<>();
        arrayList.add(new EventInput("Title", event.getTitle()));
        arrayList.add(new EventInput("Date", event.getDateString()));
        arrayList.add(new EventInput("Time", event.getTimeString()));
        arrayList.add(new EventInput("Address", event.getAddress()));
        arrayList.add(new EventInput("Details", event.getDetails()));
        if (event.isPublic().equals("true")){
            arrayList.add(new EventInput("Pulic Event", "Yes"));
        }
        else{
            arrayList.add(new EventInput("Pulic Event", "No"));
        }
        return arrayList;
    }

    public void createDateDialog(String ID, final Calendar calendar) {
        switch(ID) {
            case DATE_ID:
                int month = calendar.get(Calendar.MONTH);
                int days = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(EventCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearToday, int monthToday, int dayOfMonth) {
                        calendar.set(Calendar.MONTH, monthToday);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.YEAR, yearToday);
                        event.setObjDateTime(calendar);
                        listView.setAdapter(new EventCreateAdapter(context, buildItems()));
                    }
                }, year, month, days);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
                break;

            case TIME_ID:
                int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        calendar.set(Calendar.HOUR, hourOfDay);
                        calendar.set(Calendar.MINUTE, minuteOfDay);
                        event.setObjDateTime(calendar);
                        listView.setAdapter(new EventCreateAdapter(context, buildItems()));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
        }
    }

    public void createDialog(final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        final EditText input = new EditText(context);
        if(title.equals(DETAILS_ID) || title.equals(TITLE_ID)){
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (title) {
                    case TITLE_ID:
                        event.setTitle(input.getText().toString());
                        break;
                    case DETAILS_ID:
                        event.setDetails(input.getText().toString());
                        break;
                }
                listView.setAdapter(new EventCreateAdapter(context, buildItems()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listView.setAdapter(new EventCreateAdapter(context, buildItems()));
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void createPublicDialog(final String title){
        if(title.equals(PUBLIC_ID)) {
            final Dialog dialog = new Dialog(context);
            dialog.setTitle("Public Event");
            dialog.setContentView(R.layout.public_dialog_layout);
            dialog.show();
            Button yesButton = dialog.findViewById(R.id.public_yes_button);
            Button noButton = dialog.findViewById(R.id.public_no_button);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.setPublic("true");
                    dialog.dismiss();
                    listView.setAdapter(new EventCreateAdapter(context, buildItems()));
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.setPublic("false");
                    dialog.dismiss();
                    listView.setAdapter(new EventCreateAdapter(context, buildItems()));
                }
            });
        }

    }

    private BroadcastReceiver addressReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String address = Objects.requireNonNull(intent.getExtras()).getString(EventCreateActivity.AddressGetKey);
            String latLng = intent.getExtras().getString("latlng");
            event.setAddress(address);
            event.setLocation(latLng);
            listView.setAdapter(new EventCreateAdapter(context,buildItems()));

        }
    };
}
