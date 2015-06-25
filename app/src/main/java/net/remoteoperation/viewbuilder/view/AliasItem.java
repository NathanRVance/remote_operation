package net.remoteoperation.viewbuilder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by nathav63 on 6/23/15.
 */
public abstract class AliasItem extends LinearLayout {

    protected String title;
    protected String alias;
    protected String value;

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
    public void setValue(String value) {
        this.value = value;
    }

    protected abstract void initViews();

}
