package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.util.Prefs;

/**
 * Created by nathav63 on 6/23/15.
 */
public class AliasItem extends LinearLayout {

    public String title;
    public String alias;
    public String value;
    public int index;

    public final static String ERROR_MESSAGE = "ERROR: Uninitialized on server side";

    public AliasItem(Context context) {
        super(context);
    }

    public AliasItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AliasItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(String title) {
        this.title = title;

        TextView titleView = (TextView) findViewById(R.id.title);
        if(titleView != null) titleView.setText(title);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public void setValue(String value) {
        if(value.equals(""))
            this.value = ERROR_MESSAGE;
        else
            this.value = value;

        TextView contents = (TextView) findViewById(R.id.contents);
        contents.setText(this.value);
        saveValue();
    }

    protected void saveValue() {
        if(! value.equals(ERROR_MESSAGE)) {
            int i;
            for (i = 0; !Prefs.getAlias(i, index).equals(""); i++) {
                if(Prefs.getAlias(i, index).equals(alias))
                    Prefs.putValue(value, i, index);
            }
        }
    }
}
