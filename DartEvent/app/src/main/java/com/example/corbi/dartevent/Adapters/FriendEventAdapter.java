package com.example.corbi.dartevent.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.corbi.dartevent.Event;
import com.example.corbi.dartevent.R;

import java.util.ArrayList;

public class FriendEventAdapter extends ArrayAdapter<Event>{

    public FriendEventAdapter(@NonNull Context context, ArrayList<Event> resource) {
        super(context, R.layout.event_layout, resource);
    }

    private static class ViewHolder {
        TextView title;
        TextView detail;
        TextView date;
        TextView time;
        TextView address;
        TextView email;
        TextView public1;
    }
    @Override
    public void add(@Nullable Event object) {
        super.add(object);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        FriendEventAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new FriendEventAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_layout, parent, false);
            viewHolder.title = convertView.findViewById(R.id.event_title);
            viewHolder.detail = convertView.findViewById(R.id.event_details);
            viewHolder.date = convertView.findViewById(R.id.event_date);
            viewHolder.time = convertView.findViewById(R.id.event_time);
            viewHolder.address = convertView.findViewById(R.id.event_address);
            viewHolder.email = convertView.findViewById(R.id.event_email);
            viewHolder.public1 = convertView.findViewById(R.id.event_isPublic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FriendEventAdapter.ViewHolder) convertView.getTag();
        }
        assert event != null;
        viewHolder.title.setText(event.getTitle() + ": ");
        viewHolder.detail.setText(event.getDetails());
        viewHolder.date.setText(event.getDateString()+",");
        viewHolder.time.setText(event.getTimeString());
        viewHolder.address.setText(event.getAddress());
        viewHolder.email.setText(event.getUserEmail());
        viewHolder.public1.setText(event.isPublic());
        return convertView;
    }
}
