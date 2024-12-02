package com.example.carbon_project.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The EventsAdapter class is a custom adapter for displaying a list of events in a ListView.
 */
public class EventsAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> events;

    /**
     * Constructs a new EventsAdapter with the given context and list of events.
     * @param context
     * @param events
     */
    public EventsAdapter(Context context, ArrayList<String> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    /**
     * Returns the view for the specified position in the list.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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