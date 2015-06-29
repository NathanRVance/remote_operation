package net.remoteoperation.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import net.remoteoperation.view.AliasItem;

/**
 * Created by nathav63 on 6/28/15.
 */
public class MainListAdapter extends ArrayAdapter {
    private final AliasItem[] aliases;

    public MainListAdapter(Context context, AliasItem[] aliases) {
        super(context, -1, aliases);
        this.aliases = aliases;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AliasItem item = aliases[position];

        return item;
    }
}
