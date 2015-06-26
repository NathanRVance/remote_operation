package net.remoteoperation.viewbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.util.ExositeUtil;
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

    public static void inflateLayout(final LinearLayout linearLayout, Context context) {
        MainViewBuilder.context = context;
        MainViewBuilder.linearLayout = linearLayout;
        linearLayout.removeAllViews();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> cik = new ArrayList<>();
        for(int i = 0; ! prefs.getString("title" + i, "").equals(""); i++) {
            cik.add(prefs.getString("title" + i, ""));
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
                    populateForIndex(selection = position, prefs);
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
            populateForIndex((selection < cik.size())? selection : cik.size()-1, prefs);

        } else if(cik.size() == 1) {

            TextView textView = new TextView(context);
            linearLayout.addView(textView);
            textView.setText(cik.get(0));
            populateForIndex(selection = 0, prefs);

        } else { //empty
            TextView textView = new TextView(context);
            linearLayout.addView(textView);
            textView.setText("Import .exo files by opening them with this app!");
        }
    }

    private static boolean populateForIndex(int index, SharedPreferences prefs) {
        HashMap<String, String> aliasTypes = new HashMap<>();
        ArrayList<String> orderedKeys = new ArrayList<>();

        MainViewBuilder.index = index;
        items = new ArrayList<>();

        for(int i = 0; ! prefs.getString("type" + i + " " + index, "").equals(""); i++) {
            
            String type = prefs.getString("type" + i + " " + index, "");
            String permissions = prefs.getString("permissions" + i + " " + index, "");
            String title = prefs.getString("name" + i + " " + index, "");
            String alias = prefs.getString("alias" + i + " " + index, "");
            String value = prefs.getString("value" + i + " " + index, "");

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
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        for(int i = 0; i < items.size(); i++) {
            String value = prefs.getString("value" + i + " " + index, "");
            items.get(i).setValue(value);
        }
    }

    public static ExositeUtil getExositeUtil() {
        return exositeUtil;
    }

}
