package com.example.corbi.dartevent.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.corbi.dartevent.R;
import com.example.corbi.dartevent.User;

import java.util.ArrayList;

public class FriendsListAdapter extends ArrayAdapter<User> {
    public FriendsListAdapter(@NonNull Context context, ArrayList<User> resource) {
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
        FriendsListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new FriendsListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_users, parent, false);
            viewHolder.title = convertView.findViewById(R.id.item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FriendsListAdapter.ViewHolder) convertView.getTag();
        }
        assert user != null;
        viewHolder.title.setText(user.getUserEmail());
        return convertView;
    }
}

