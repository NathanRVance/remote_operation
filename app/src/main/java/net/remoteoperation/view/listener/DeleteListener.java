package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.util.ExositeUtil;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.view.ListItem;
import net.remoteoperation.view.dialog.ConfirmationDialog;

/**
 * Created by nathav63 on 7/5/15.
 */
public class DeleteListener implements ConfirmationDialog.OnConfirmListener, View.OnClickListener {

    ListItem item;
    OnDelete delete;

    public DeleteListener(ListItem item, OnDelete delete) {
        this.item = item;
        this.delete = delete;
    }

    @Override
    public void onClick(View v) {
        new ConfirmationDialog(item.getContext(), DeleteListener.this, "Delete " + item.title + "?").show();
    }

    @Override
    public void onConfirm() {
        delete.onDelete(item);
    }

    public interface OnDelete {
        void onDelete(ListItem item);
    }
}
