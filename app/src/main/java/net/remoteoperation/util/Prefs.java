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

    public static String getCIK(int index) {
        return prefs.getString("cik" + index, "");
    }

    public static void putCIK(String cik, int index) {
        prefs.edit().putString("cik" + index, cik).commit();
    }

    public static String getCIKTitle(int index) {
        return prefs.getString("title" + index, "");
    }

    public static void putCIKTitle(String title, int index) {
        prefs.edit().putString("title" + index, title).commit();
    }

    private static String getString(String key, int index, int CIKIndex) {
        return prefs.getString(key + index + " " + CIKIndex, "");
    }

    private static void putString(String value, String key, int index, int CIKIndex) {
        prefs.edit().putString(key + index + " " + CIKIndex, value).commit();
    }

    public static String getType(int index, int CIKIndex) {
        return getString("type", index, CIKIndex);
    }

    public static String getPermissions(int index, int CIKIndex) {
        return getString("permissions", index, CIKIndex);
    }

    public static String getAlias(int index, int CIKIndex) {
        return getString("alias", index, CIKIndex);
    }

    public static String getName(int index, int CIKIndex) {
        return getString("name", index, CIKIndex);
    }

    public static String getValue(int index, int CIKIndex) {
        return getString("value", index, CIKIndex);
    }

    public static void putType(String value, int index, int CIKIndex) {
        putString(value, "type", index, CIKIndex);
    }

    public static void putPermissions(String value, int index, int CIKIndex) {
        putString(value, "permissions", index, CIKIndex);
    }

    public static void putAlias(String value, int index, int CIKIndex) {
        putString(value, "alias", index, CIKIndex);
    }

    public static void putName(String value, int index, int CIKIndex) {
        putString(value, "name", index, CIKIndex);
    }

    public static void putValue(String value, int index, int CIKIndex) {
        putString(value, "value", index, CIKIndex);
    }

    public static void deleteCIK(int CIKIndex) {
        putCIK("", CIKIndex);
        putCIKTitle("", CIKIndex);
        for(int index = 0; ! getAlias(index, CIKIndex).equals(""); index++) {
            putType("", index, CIKIndex);
            putPermissions("", index, CIKIndex);
            putAlias("", index, CIKIndex);
            putName("", index, CIKIndex);
            putValue("", index, CIKIndex);
        }
    }

    public static void deleteAlias(int index, int CIKIndex) {
        for(; ! getAlias(index, CIKIndex).equals(""); index++) {
            putType(getType(index + 1, CIKIndex), index, CIKIndex);
            putPermissions(getPermissions(index + 1, CIKIndex), index, CIKIndex);
            putAlias(getAlias(index + 1, CIKIndex), index, CIKIndex);
            putName(getName(index + 1, CIKIndex), index, CIKIndex);
            putValue(getValue(index + 1, CIKIndex), index, CIKIndex);
        }
    }

}
