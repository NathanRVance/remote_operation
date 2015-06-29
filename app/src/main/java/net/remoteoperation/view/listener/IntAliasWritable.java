package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.view.AliasItem;
import net.remoteoperation.view.NumberPickerDialog;

/**
 * Created by nathav63 on 6/23/15.
 */
public class IntAliasWritable implements NumberPickerDialog.OnNumberSetListener, View.OnClickListener {

    AliasItem item;

    @Override
    public void onClick(View v) {
        this.item = (AliasItem) v;
        if (!item.value.equals(AliasItem.ERROR_MESSAGE))
            new NumberPickerDialog(item.getContext(),
                    IntAliasWritable.this, Integer.parseInt(item.value),
                    0, Integer.parseInt(item.value) + 1000,
                    item.title).show();
    }

    @Override
    public void onNumberSet(int number) {
        item.setValue(String.valueOf(number));
    }
}
