package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.view.listener.DeleteListener;

/**
 * Created by nathav63 on 6/23/15.
 */
public class ListItem extends LinearLayout {

    public String title;
    public String alias;
    public String value;
    public MainView mainView;

    public final static String ERROR_MESSAGE = "ERROR: Uninitialized on server side";

    public ListItem(Context context) {
        super(context);
        LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.list_item, this, true);
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

    public void setAlias(String alias){
        this.alias = alias;
    }

    public void setOnDelete(DeleteListener.OnDelete delete) {
        setOnClickListener(new DeleteListener(this, delete));
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
        mainView.setValue(alias, value);
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
