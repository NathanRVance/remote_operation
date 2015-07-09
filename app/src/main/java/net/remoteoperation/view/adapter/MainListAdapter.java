package net.remoteoperation.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import net.remoteoperation.view.ListItem;
import net.remoteoperation.view.MainView;
import net.remoteoperation.view.listener.AddListener;
import net.remoteoperation.view.listener.IntAliasWritable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 6/28/15.
 */
public class MainListAdapter extends ArrayAdapter<ListItem> {
    private final ArrayList<String> aliases;
    private final HashMap<String, String> values;
    private final MainView mainView;

    public MainListAdapter(Context context, ArrayList<String> aliases,
                           HashMap<String, String> values, MainView mainView) {
        super(context, -1);
        this.aliases = aliases;
        this.values = values;
        this.mainView = mainView;
    }

    @Override
    public int getCount() {
        return aliases.size()+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item;

        if(convertView != null && convertView instanceof ListItem) {
            item = (ListItem) convertView;
        } else {
            item = new ListItem(mainView.getContext());
        }

        if(position == aliases.size()) {
            item.setOnClickListener(new AddListener(mainView));
            item.setTitle("Add New Setting");
            item.setHideDelete(true);
            return item;
        }

        item.setHideDelete(false);
        item.setMainView(mainView);
        item.setOnClickListener(new IntAliasWritable());
        item.setTitle("Item " + position);
        item.setAlias(aliases.get(position));
        item.setValue(values.get(aliases.get(position)));
        item.setOnDelete(mainView);

        return item;
    }
}
