package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

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
            onEmptyCIK();
        } else {
            exositeController = new ExositeController(getContext(), this);
            exositeController.refresh();
        }
    }

    public void populate(ArrayList<String> aliases, HashMap<String, String> values) {
        MainListAdapter adapter = new MainListAdapter(getContext(), aliases, values, this);

        ListView listView = (ListView) findViewById(R.id.main_list);
        if(listView == null) {
            removeAllViews();
            LayoutInflater  inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.list_view, this, true);
            listView = (ListView) findViewById(R.id.main_list);
        }

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

    private void onEmptyCIK() {
        removeAllViews();
        LayoutInflater  inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.empty_cik, this, true);
        final EditText et = (EditText) findViewById(R.id.edit_cik);
        findViewById(R.id.cik_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String cik = et.getText().toString();
                Prefs.putCIK(cik);
                reload();
            }
        });
    }
}
