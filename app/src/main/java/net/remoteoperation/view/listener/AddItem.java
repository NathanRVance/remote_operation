package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.util.ExositeUtil;
import net.remoteoperation.view.ListItem;
import net.remoteoperation.view.StringEnterDialog;

/**
 * Created by nathav63 on 7/3/15.
 */
public class AddItem implements StringEnterDialog.OnStringSetListener, View.OnClickListener {

    ListItem item;
    ExositeUtil exositeUtil;

    public AddItem(ExositeUtil exositeUtil) {
        this.exositeUtil = exositeUtil;
    }

    @Override
    public void onClick(View v) {
        this.item = (ListItem) v;
        new StringEnterDialog(item.getContext(), AddItem.this,
                "", item.title).show();
    }

    @Override
    public void onStringSet(String string) {
        exositeUtil.createDataport(string);
    }
}
