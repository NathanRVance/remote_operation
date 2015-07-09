package net.remoteoperation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Static methods to access preferences
 */
public class Prefs {

    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getCIK() {
        return prefs.getString("cik", "");
    }

    public static void putCIK(String cik) {
        prefs.edit().putString("cik", cik).commit();
    }

}
