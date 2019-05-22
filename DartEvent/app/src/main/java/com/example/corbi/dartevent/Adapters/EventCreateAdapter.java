package com.example.corbi.dartevent.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.corbi.dartevent.Event;
import com.example.corbi.dartevent.EventInput;
import com.example.corbi.dartevent.R;

import java.util.ArrayList;

public class EventCreateAdapter extends ArrayAdapter<EventInput>{
    private static class ViewHolder {
        TextView title;
        TextView value;
    }

    public EventCreateAdapter(@NonNull Context context, ArrayList<EventInput> resource) {
        super(context, R.layout.item_event, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventInput eventInput = getItem(position);
        EventCreateAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new EventCreateAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_event, parent, false);
            viewHolder.title = convertView.findViewById(R.id.item_title);
            viewHolder.value = convertView.findViewById(R.id.item_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EventCreateAdapter.ViewHolder) convertView.getTag();
        }
        assert eventInput != null;
        viewHolder.title.setText(eventInput.title);
        viewHolder.value.setText(eventInput.value);
        return convertView;
    }
}
