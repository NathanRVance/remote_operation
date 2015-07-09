package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.util.ExositeController;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.view.adapter.MainListAdapter;
import net.remoteoperation.view.listener.DeleteListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 6/21/15.
 */
public class MainView extends LinearLayout implements DeleteListener.OnDelete {

    private ExositeController exositeController;
    private boolean isEmpty = false;

    private static final int TITLE_SIZE = 20;

    public MainView(Context context) {
        super(context);
        initView();
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        reload();
    }

    public void reload() {
        String cik = Prefs.getCIK();
        if (isEmpty = cik.isEmpty()) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(TITLE_SIZE);
            addView(textView);
            textView.setText("Import .exo files by opening them with this app!");
        } else {
            exositeController = new ExositeController(getContext(), this);
            exositeController.refresh();
        }
    }

    public void populate(ArrayList<String> aliases, HashMap<String, String> values) {
        MainListAdapter adapter = new MainListAdapter(getContext(), aliases, values, this);

        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(adapter);
    }

    public void commit() {
        if(! isEmpty)
            exositeController.commit();
    }

    public void setValue(String alias, String value) {
        exositeController.setValue(alias, value);
    }

    public void add() {
        exositeController.add();
    }

    @Override
    public void onDelete(ListItem item) {
        exositeController.delete(item.alias);
    }
}
