package net.remoteoperation.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.remoteoperation.R;

public class StringEnterDialog extends AlertDialog implements OnClickListener {
    private static final String STRING = "string";

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnStringSetListener {

        /**
         * @param string The string that was set.
         */
        void onStringSet(String string);
    }

    private final EditText mEditText;
    private final OnStringSetListener mCallback;

    /**
     * @param context Parent.
     * @param callBack How parent string notified.
     * @param string The initial number.
     */
    public StringEnterDialog(Context context,
                             OnStringSetListener callBack,
                             String string,
                             String title) {
        this(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, callBack, string, title);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param string The initial string.
     */
    public StringEnterDialog(Context context,
                             int theme,
                             OnStringSetListener callBack,
                             String string,
                             String title) {
        super(context, theme);
        mCallback = callBack;

        setTitle(title);

        setButton(DialogInterface.BUTTON_POSITIVE, context.getText(R.string.ok), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getText(R.string.cancel),
                (OnClickListener) null);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.string_enter_dialog, null);
        setView(view);
        mEditText = (EditText) view.findViewById(R.id.edit_text);

        // initialize state
        mEditText.setText(string);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mEditText.clearFocus();
            mCallback.onStringSet(mEditText.getText().toString());
            dialog.dismiss();
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putString(STRING, mEditText.getText().toString());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String string = savedInstanceState.getString(STRING);
        mEditText.setText(string);
    }
}