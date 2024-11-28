package com.example.carbon_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> events;

    public EventsAdapter(Context context, ArrayList<String> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView eventText = convertView.findViewById(android.R.id.text1);
        eventText.setText(events.get(position));
        return convertView;
    }
}