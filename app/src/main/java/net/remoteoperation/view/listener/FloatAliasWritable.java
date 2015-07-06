package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.view.ListItem;
import net.remoteoperation.view.dialog.FloatEnterDialog;

/**
 * Created by nathav63 on 6/23/15.
 */
public class FloatAliasWritable implements FloatEnterDialog.OnNumberSetListener, View.OnClickListener {

    ListItem item;

    @Override
    public void onClick(View v) {
        this.item = (ListItem) v;
        if (!item.value.equals(ListItem.ERROR_MESSAGE))
            new FloatEnterDialog(item.getContext(), FloatAliasWritable.this,
                    Float.parseFloat(item.value), item.title).show();
    }

    @Override
    public void onNumberSet(float number) {
        item.setValue(String.valueOf(number));
    }
}
