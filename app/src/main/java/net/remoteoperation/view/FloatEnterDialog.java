package net.remoteoperation.view;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.remoteoperation.R;

public class FloatEnterDialog extends AlertDialog implements OnClickListener {
    private static final String NUMBER = "number";

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnNumberSetListener {

        /**
         * @param number The number that was set.
         */
        void onNumberSet(float number);
    }

    private final EditText mEditText;
    private final OnNumberSetListener mCallback;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param number The initial number.
     */
    public FloatEnterDialog(Context context,
                            OnNumberSetListener callBack,
                            float number,
                            String title) {
        this(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, callBack, number, title);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param number The initial number.
     */
    public FloatEnterDialog(Context context,
                            int theme,
                            OnNumberSetListener callBack,
                            float number,
                            String title) {
        super(context, theme);
        mCallback = callBack;

        setTitle(title);

        setButton(DialogInterface.BUTTON_POSITIVE, context.getText(R.string.ok), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getText(R.string.cancel),
                (OnClickListener) null);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.float_enter_dialog, null);
        setView(view);
        mEditText = (EditText) view.findViewById(R.id.edit_text);

        // initialize state
        mEditText.setText(String.valueOf(number));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mEditText.clearFocus();
            mCallback.onNumberSet(Float.parseFloat(mEditText.getText().toString()));
            dialog.dismiss();
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putFloat(NUMBER, Float.parseFloat(mEditText.getText().toString()));
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        float number = savedInstanceState.getFloat(NUMBER);
        mEditText.setText(String.valueOf(number));
    }
}