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
    ExositeUtil exositeUtil;

    public DeleteListener(ListItem item, ExositeUtil exositeUtil) {
        this.item = item;
        this.exositeUtil = exositeUtil;
    }

    @Override
    public void onClick(View v) {
        new ConfirmationDialog(item.getContext(), DeleteListener.this, "Delete " + item.title + "?").show();
    }

    @Override
    public void onConfirm() {
        exositeUtil.deleteDataport(item.alias);
        Prefs.deleteAlias(item.getIndex(), item.CIKIndex);
        item.mainView.reload(item.CIKIndex);
    }
}
