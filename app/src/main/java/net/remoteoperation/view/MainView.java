package net.remoteoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.util.ExositeUtil;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.view.adapter.MainListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 6/21/15.
 */
public class MainView extends LinearLayout {

    private int selection = 0;
    private ArrayList<AliasItem> items;
    private int index;
    private ExositeUtil exositeUtil;

    private static final int TITLE_SIZE = 20;

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        removeAllViews();

        ArrayList<String> cik = new ArrayList<>();
        for(int i = 0; ! Prefs.getCIKTitle(i).equals(""); i++) {
            cik.add(Prefs.getCIKTitle(i));
        }

        if(cik.size() > 1) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cik);
            final Spinner spinner = new Spinner(getContext());
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    removeAllViews();
                    addView(spinner);
                    populateForIndex(selection = position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    removeAllViews();
                    addView(spinner);
                    System.out.println("Nothing selected.");
                }
            });

            removeAllViews();
            addView(spinner);
            populateForIndex((selection < cik.size())? selection : cik.size()-1);

        } else if(cik.size() == 1) {

            TextView textView = new TextView(getContext());
            textView.setTextSize(TITLE_SIZE);
            addView(textView);
            textView.setText(cik.get(0));
            populateForIndex(selection = 0);

        } else { //empty
            TextView textView = new TextView(getContext());
            textView.setTextSize(TITLE_SIZE);
            addView(textView);
            textView.setText("Import .exo files by opening them with this app!");
        }
    }

    private boolean populateForIndex(int index) {
        HashMap<String, String> aliasTypes = new HashMap<>();
        ArrayList<String> orderedKeys = new ArrayList<>();

        this.index = index;
        items = new ArrayList<>();

        for(int i = 0; ! Prefs.getType(i, index).equals(""); i++) {
            
            String type = Prefs.getType(i, index);
            String permissions = Prefs.getPermissions(i, index);
            String title = Prefs.getName(i, index);
            String alias = Prefs.getAlias(i, index);

            if(type.equals("") || permissions.equals("") || title.equals("") || alias.equals(""))
                return false;

            aliasTypes.put(alias, type);
            orderedKeys.add(alias);

            AliasItem item = null;

            if(type.equals("int")) {
                if(permissions.equals("r")) {
                    item = (AliasItem) LayoutInflater.from(getContext()).inflate(R.layout.int_alias_read_only, null, false);
                } else if(permissions.equals("w")) {
                    //item = new IntAliasReadOnly(getContext());
                    //item.addView(LayoutInflater.from(getContext()).inflate(R.layout.alias_item, item, true));
                    item = (AliasItem) LayoutInflater.from(getContext()).inflate(R.layout.int_alias_writable, null, false);
                }
            } else if(type.equals("float")) {
                if (permissions.equals("r")) {
                    item = (AliasItem) LayoutInflater.from(getContext()).inflate(R.layout.float_alias_read_only, null, false);
                } else if (permissions.equals("w")) {
                    item = (AliasItem) LayoutInflater.from(getContext()).inflate(R.layout.float_alias_writable, null, false);
                }
            }

            if(item == null)
                return false;

            item.setTitle(title);
            item.setAlias(alias);
            item.setIndex(index);

            //addView(item);
            items.add(item);
        }

        addView(getList());

        refreshViews();

        exositeUtil = new ExositeUtil(getContext(), index, aliasTypes, orderedKeys, this);
        exositeUtil.updateItems();
        return true;
    }

    private ListView getList() {
        ListView listView = new ListView(getContext());
        AliasItem[] aliases = new AliasItem[items.size()];
        MainListAdapter adapter = new MainListAdapter(getContext(), items.toArray(aliases));
        listView.setAdapter(adapter);

        return listView;
    }

    public void refreshViews() {
        for(int i = 0; i < items.size(); i++) {
            String value = Prefs.getValue(i, index);
            items.get(i).setValue(value);
        }
    }

    public int getIndex() {
        return index;
    }

    public ExositeUtil getExositeUtil() {
        return exositeUtil;
    }

}
