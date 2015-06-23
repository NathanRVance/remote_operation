package net.remoteoperation.viewbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nathav63 on 6/21/15.
 */
public class MainViewBuilder {

    private static Context context;
    private static LinearLayout linearLayout;
    private static int selection = 0;

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

    private static void populateForIndex(int index, SharedPreferences prefs) {
        System.out.println("Populating...");
        for(int i = 0; ! prefs.getString("type" + i + " " + index, "").equals(""); i++) {
            System.out.println("Got here...");
            String type = prefs.getString("type" + i + " " + index, "");
            String permissions = prefs.getString("permissions" + i + " " + index, "");
            String alias = prefs.getString("alias" + i + " " + index, "");

            TextView textView = new TextView(context);
            textView.setText("Type: " + type + ", Permissions: " + permissions + ", Alias: " + alias);
            linearLayout.addView(textView);
        }
    }

}
