package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.remoteoperation.R;

/**
 * Created by nathav63 on 6/23/15.
 */
public class FloatAliasReadOnly extends AliasItem {

    public FloatAliasReadOnly(Context context) {
        super(context);
    }

    public FloatAliasReadOnly(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatAliasReadOnly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        TextView contents = (TextView) findViewById(R.id.contents);
        contents.setText(value);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        TextView titleView = (TextView) findViewById(R.id.title);
        if(titleView != null)
            titleView.setText(title);
    }

}
