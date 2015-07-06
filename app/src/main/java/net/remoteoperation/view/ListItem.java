package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.util.ExositeUtil;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.view.listener.DeleteListener;

/**
 * Created by nathav63 on 6/23/15.
 */
public class ListItem extends LinearLayout {

    public String title;
    public String alias;
    public String value;
    public int CIKIndex;
    public ExositeUtil exositeUtil;
    public MainView mainView;

    public final static String ERROR_MESSAGE = "ERROR: Uninitialized on server side";

    public ListItem(Context context) {
        super(context);
    }

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(String title) {
        this.title = title;

        TextView titleView = (TextView) findViewById(R.id.title);
        if (titleView != null) titleView.setText(title);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCIKIndex(int CIKIndex) {
        this.CIKIndex = CIKIndex;
    }

    public void setExositeUtil(ExositeUtil exositeUtil) {
        this.exositeUtil = exositeUtil;
        setOnClickListener(new DeleteListener(this, exositeUtil));
    }

    public void setValue(String value) {
        if (value.equals(""))
            this.value = ERROR_MESSAGE;
        else
            this.value = value;

        TextView contents = (TextView) findViewById(R.id.contents);
        contents.setText(this.value);
        saveValue();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    protected void saveValue() {
        int index = getIndex();
        if (index != -1 && !value.equals(ERROR_MESSAGE)) {
            Prefs.putValue(value, index, CIKIndex);
        }
    }

    public int getIndex() {
        for (int i = 0; !Prefs.getAlias(i, CIKIndex).equals(""); i++) {
            if (Prefs.getAlias(i, CIKIndex).equals(alias))
                return i;
        }
        return -1;
    }

    public void setHideDelete(boolean hide) {
        if(hide) {
            findViewById(R.id.delete).setVisibility(GONE);
        } else {
            findViewById(R.id.delete).setVisibility(VISIBLE);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        if (onClickListener instanceof DeleteListener) {
            findViewById(R.id.delete).setOnClickListener(onClickListener);
        } else {
            super.setOnClickListener(onClickListener);
        }
    }
}
