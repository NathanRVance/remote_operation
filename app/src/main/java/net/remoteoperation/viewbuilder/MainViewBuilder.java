package net.remoteoperation.viewbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.util.ExositeUtil;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.viewbuilder.view.AliasItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 6/21/15.
 */
public class MainViewBuilder {

    private static Context context;
    private static LinearLayout linearLayout;
    private static int selection = 0;
    private static ArrayList<AliasItem> items;
    private static int index;
    private static ExositeUtil exositeUtil;

    private static final int TITLE_SIZE = 20;

    public static void inflateLayout(final LinearLayout linearLayout, Context context) {
        MainViewBuilder.context = context;
        MainViewBuilder.linearLayout = linearLayout;
        linearLayout.removeAllViews();

        ArrayList<String> cik = new ArrayList<>();
        for(int i = 0; ! Prefs.getCIKTitle(i).equals(""); i++) {
            cik.add(Prefs.getCIKTitle(i));
        }

        if(cik.size() > 1) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cik);
            final Spinner spinner = new Spinner(context);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    linearLayout.removeAllViews();
                    linearLayout.addView(spinner);
                    populateForIndex(selection = position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    linearLayout.removeAllViews();
                    linearLayout.addView(spinner);
                    System.out.println("Nothing selected.");
                }
            });

            linearLayout.removeAllViews();
            linearLayout.addView(spinner);
            populateForIndex((selection < cik.size())? selection : cik.size()-1);

        } else if(cik.size() == 1) {

            TextView textView = new TextView(context);
            textView.setTextSize(TITLE_SIZE);
            linearLayout.addView(textView);
            textView.setText(cik.get(0));
            populateForIndex(selection = 0);

        } else { //empty
            TextView textView = new TextView(context);
            textView.setTextSize(TITLE_SIZE);
            linearLayout.addView(textView);
            textView.setText("Import .exo files by opening them with this app!");
        }
    }

    private static boolean populateForIndex(int index) {
        HashMap<String, String> aliasTypes = new HashMap<>();
        ArrayList<String> orderedKeys = new ArrayList<>();

        MainViewBuilder.index = index;
        items = new ArrayList<>();

        for(int i = 0; ! Prefs.getType(i, index).equals(""); i++) {
            
            String type = Prefs.getType(i, index);
            String permissions = Prefs.getPermissions(i, index);
            String title = Prefs.getName(i, index);
            String alias = Prefs.getAlias(i, index);
            String value = Prefs.getValue(i, index);

            if(type.equals("") || permissions.equals("") || title.equals("") || alias.equals(""))
                return false;

            aliasTypes.put(alias, type);
            orderedKeys.add(alias);

            AliasItem item = null;

            if(type.equals("int")) {
                if(permissions.equals("r")) {
                    item = (AliasItem) LayoutInflater.from(context).inflate(R.layout.int_alias_read_only, null, false);
                } else if(permissions.equals("w")) {
                    item = (AliasItem) LayoutInflater.from(context).inflate(R.layout.int_alias_writable, null, false);
                }
            } else if(type.equals("float")) {
                if (permissions.equals("r")) {
                    item = (AliasItem) LayoutInflater.from(context).inflate(R.layout.float_alias_read_only, null, false);
                } else if (permissions.equals("w")) {
                    item = (AliasItem) LayoutInflater.from(context).inflate(R.layout.float_alias_writable, null, false);
                }
            }

            if(item == null)
                return false;

            item.setTitle(title);
            item.setAlias(alias);
            item.setIndex(index);

            linearLayout.addView(item);
            items.add(item);
        }
        refreshViews();

        exositeUtil = new ExositeUtil(context, index, aliasTypes, orderedKeys);
        exositeUtil.updateItems();
        return true;
    }

    public static void refreshViews() {
        for(int i = 0; i < items.size(); i++) {
            String value = Prefs.getValue(i, index);
            items.get(i).setValue(value);
        }
    }

    public static int getIndex() {
        return index;
    }

    public static ExositeUtil getExositeUtil() {
        return exositeUtil;
    }

}
