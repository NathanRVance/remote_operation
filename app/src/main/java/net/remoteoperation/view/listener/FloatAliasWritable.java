package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.view.AliasItem;
import net.remoteoperation.view.FloatEnterDialog;

/**
 * Created by nathav63 on 6/23/15.
 */
public class FloatAliasWritable implements FloatEnterDialog.OnNumberSetListener, View.OnClickListener {

    AliasItem item;

    @Override
    public void onClick(View v) {
        this.item = (AliasItem) v;
        if (!item.value.equals(AliasItem.ERROR_MESSAGE))
            new FloatEnterDialog(item.getContext(), FloatAliasWritable.this,
                    Float.parseFloat(item.value), item.title).show();
    }

    @Override
    public void onNumberSet(float number) {
        item.setValue(String.valueOf(number));
    }
}
