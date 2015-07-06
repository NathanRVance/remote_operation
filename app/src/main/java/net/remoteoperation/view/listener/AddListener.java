package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.util.ExositeUtil;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.view.ListItem;
import net.remoteoperation.view.dialog.StringEnterDialog;

/**
 * Created by nathav63 on 7/3/15.
 */
public class AddListener implements StringEnterDialog.OnStringSetListener, View.OnClickListener {

    ListItem item;
    ExositeUtil exositeUtil;

    public AddListener(ExositeUtil exositeUtil) {
        this.exositeUtil = exositeUtil;
    }

    @Override
    public void onClick(View v) {
        this.item = (ListItem) v;
        new StringEnterDialog(item.getContext(), AddListener.this,
                "", item.title).show();
    }

    @Override
    public void onStringSet(String string) {
        exositeUtil.createDataport(string);
    }
}
