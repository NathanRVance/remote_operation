package net.remoteoperation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Static methods used for parsing input files
 */
public class ExoParser {

    private static Context context;

    public static boolean parse(Uri uri, Context context) {
        ExoParser.context = context;
        String contents;
        try {
            contents = getStringFromFile(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String[] lines = contents.split("\n");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        String[] words = lines[0].split(" ");

        if(words[0].length() != 40) {
            return false;
        }

        int index;
        for(index = 0; ! (prefs.getString("cik" + index, "").equals("") || prefs.getString("cik" + index, "").equals(words[0])); index++);

        editor.putString("cik" + index, words[0]);
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < words.length; i++) {
            sb.append(words[i]).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        editor.putString("title" + index, sb.toString());

        for(int i = 1; i < lines.length; i++)
        {
            words = lines[i].split(" ");
            editor.putString("type" + (i-1) + " " + index, words[0]);
            editor.putString("permissions" + (i-1) + " " + index, words[1]);
            editor.putString("alias" + (i-1) + " " + index, words[2]);
            sb = new StringBuilder();
            for(int j = 3; j < words.length; j++) {
                sb.append(words[j]).append(" ");
            }
            sb.deleteCharAt(sb.length()-1);
            editor.putString("name" + (i-1) + " " + index, sb.toString());
        }

        editor.commit();
        return true;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
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
