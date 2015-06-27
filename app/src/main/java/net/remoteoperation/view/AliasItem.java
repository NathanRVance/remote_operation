package net.remoteoperation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by nathav63 on 6/23/15.
 */
public abstract class AliasItem extends LinearLayout {

    protected String title;
    protected String alias;
    protected String value;
    protected int index;

    protected final static String ERROR_MESSAGE = "ERROR: Uninitialized on server side";

    public AliasItem(Context context) {
        super(context);
    }

    public AliasItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AliasItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        initViews();
    }

    public void setTitle(String title) {
        this.title = title;
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
    }

    protected abstract void initViews();

    protected void saveValue() {
        if(! value.equals(ERROR_MESSAGE)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int i;
            for (i = 0; !prefs.getString("alias" + i + " " + index, "").equals(alias); i++) ;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("value" + i + " " + index, value);
            editor.commit();
        }
    }
}
