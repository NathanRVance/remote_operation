package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import net.remoteoperation.R;

/**
 * Created by nathav63 on 6/23/15.
 */
public class IntAliasWritable extends AliasItem implements NumberPickerDialog.OnNumberSetListener {

    public IntAliasWritable(Context context) {
        super(context);
    }

    public IntAliasWritable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntAliasWritable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initViews() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new NumberPickerDialog(getContext(),
                        IntAliasWritable.this, Integer.parseInt(value),
                        0, Integer.parseInt(value) + 1000,
                        title).show();
            }
        });
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        TextView contents = (TextView) findViewById(R.id.contents);
        contents.setText(value);
        saveValue();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        TextView titleView = (TextView) findViewById(R.id.title);
        if(titleView != null)
            titleView.setText(title);
    }

    @Override
    public void onNumberSet(int number) {
        setValue(String.valueOf(number));
    }
}
