package net.remoteoperation.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import net.remoteoperation.view.ListItem;

/**
 * Created by nathav63 on 6/28/15.
 */
public class MainListAdapter extends ArrayAdapter {
    private final ListItem[] items;

    public MainListAdapter(Context context, ListItem[] items) {
        super(context, -1, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem item = items[position];

        return item;
    }
}
