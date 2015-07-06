package net.remoteoperation.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import net.remoteoperation.R;

public class ConfirmationDialog extends AlertDialog implements OnClickListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'OK' button).
     */
    public interface OnConfirmListener {
        void onConfirm();
    }

    private final OnConfirmListener mCallback;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     */
    public ConfirmationDialog(Context context,
                              OnConfirmListener callBack,
                              String title) {
        super(context);
        mCallback = callBack;

        setTitle(title);

        setButton(DialogInterface.BUTTON_POSITIVE, context.getText(R.string.ok), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getText(R.string.cancel),
                (OnClickListener) null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mCallback.onConfirm();
            dialog.dismiss();
        }
    }
}