package net.remoteoperation.util;

import android.content.Context;
import android.net.Uri;

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

        if(contents.contains(" ")) {
            return false;
        }

        Prefs.putCIK(contents.split("\n")[0]);
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
