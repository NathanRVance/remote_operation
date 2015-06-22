package net.remoteoperation.exoparser;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nathav63 on 6/20/15.
 */
public class ExoParser {

    private static Context context;

    public static boolean parse(Uri uri, Context context) {
        ExoParser.context = context;
        String contents = "Blahs";
        try {
            contents = getStringFromFile(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("test", contents).commit();
        return true;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (Uri uri) throws Exception {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String ret = convertStreamToString(inputStream);
        //Make sure you close all streams.
        inputStream.close();
        return ret;
    }

}
