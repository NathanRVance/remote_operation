package net.remoteoperation.util;

import android.content.Context;
import android.os.Bundle;

import net.remoteoperation.view.MainView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 7/6/15.
 */
public class ExositeController implements ExositeUtil.ExositeCallback {

    ExositeUtil exositeUtil;
    ArrayList<String> aliases;
    HashMap<String, String> values;
    int numItems;
    ArrayList<String> changeInAliasesAdd;
    ArrayList<String> changeInAliasesDelete;
    MainView mainView;

    public static final String NUMITEMS = "numItems";

    public ExositeController(Context context, MainView mainView) {
        aliases = new ArrayList<>();
        values = new HashMap<>();
        aliases.add(NUMITEMS);
        exositeUtil = new ExositeUtil(context);

        this.mainView = mainView;

        changeInAliasesAdd = new ArrayList<>();
        changeInAliasesDelete = new ArrayList<>();
    }

    public void refresh() {
        changeInAliasesAdd = new ArrayList<>();
        changeInAliasesDelete = new ArrayList<>();
        exositeUtil.updateItems(this, aliases);
    }

    private void onPostRefresh(ArrayList<String> results) {
        numItems = Integer.parseInt(results.get(0));
        if (numItems != aliases.size()) {
            aliases = new ArrayList<>();
            aliases.add(NUMITEMS);
            for (int i = 1; i < numItems; i++) {
                aliases.add("rule" + i);
            }
            exositeUtil.updateItems(this, aliases);
        } else {
            values = new HashMap<>();
            for (int i = 0; i < aliases.size(); i++) {
                try {
                    double result = Double.parseDouble(results.get(i));
                    if (result == (int) result)
                        values.put(aliases.get(i), String.valueOf((int) result));
                    else values.put(aliases.get(i), results.get(i));
                } catch(Exception e) {
                    //Improperly formatted number
                    values.put(aliases.get(i), "0");
                }
            }
            refreshMainView();
        }
    }

    public void commit() {
        for(String alias : changeInAliasesAdd) {
            exositeUtil.createDataport(alias, this);
        }
        for(String alias : changeInAliasesDelete) {
            exositeUtil.deleteDataport(alias, this);
        }

        changeInAliasesAdd = new ArrayList<>();
        changeInAliasesDelete = new ArrayList<>();

        exositeUtil.commitItems(this, aliases, values);
    }

    public void delete(String alias) {
        numItems--;
        changeInAliasesDelete.add(aliases.get(aliases.size()-1));
        for (int i = aliases.indexOf(alias); i < aliases.size() - 1; i++) {
            values.put(aliases.get(i), values.get(aliases.get(i + 1)));
        }
        values.remove(aliases.get(aliases.size() - 1));
        values.put(NUMITEMS, "" + numItems);
        aliases.remove(aliases.size()-1);
        refreshMainView();
    }

    public void add() {
        numItems++;
        String newAlias = "rule" + aliases.size();
        aliases.add(newAlias);
        values.put(newAlias, "0");
        values.put(NUMITEMS, "" + numItems);
        refreshMainView();
        changeInAliasesAdd.add(newAlias);
    }

    @Override
    public void onComplete(ExositeUtil.Mode mode, Bundle results) {
        switch (mode) {
            case READ:
                onPostRefresh(results.getStringArrayList(ExositeUtil.RESULTS));
                break;
            case WRITE:
                break;
            case CREATE:
                break;
            case DELETE:
                break;
            default:
                break;
        }
    }

    public void setValue(String alias, String value) {
        values.put(alias, value);
    }

    private void refreshMainView() {
        ArrayList<String> shortAliases = new ArrayList<>(aliases);
        shortAliases.remove(0);
        mainView.populate(shortAliases, values);
    }
}
