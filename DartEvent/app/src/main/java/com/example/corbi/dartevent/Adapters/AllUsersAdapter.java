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
import com.example.corbi.dartevent.R;
import com.example.corbi.dartevent.User;

import java.util.ArrayList;

public class AllUsersAdapter extends ArrayAdapter<User> {
    public AllUsersAdapter(@NonNull Context context, ArrayList<User> resource) {
        super(context, R.layout.item_users, resource);
    }
    private static class ViewHolder {
        TextView title;
    }

    @Override
    public void add(@Nullable User object) {
        super.add(object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        AllUsersAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new AllUsersAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_users, parent, false);
            viewHolder.title = convertView.findViewById(R.id.item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AllUsersAdapter.ViewHolder) convertView.getTag();
        }
        assert user != null;
        viewHolder.title.setText(user.getUserEmail());
        return convertView;
    }
}

