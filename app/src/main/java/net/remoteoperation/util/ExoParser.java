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

        String[] lines = contents.split("\n");

        String[] words = lines[0].split(" ");

        if(words[0].length() != 40) {
            return false;
        }

        int index;
        for(index = 0; ! (Prefs.getCIK(index).equals("") || Prefs.getCIK(index).equals(words[0])); index++);

        Prefs.deleteCIK(index); //in case it's a matching CIK and this one will be shorter
        Prefs.putCIK(words[0], index);
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < words.length; i++) {
            sb.append(words[i]).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        Prefs.putCIKTitle(sb.toString(), index);

        for(int i = 1; i < lines.length; i++)
        {
            words = lines[i].split(" ");
            Prefs.putType(words[0], (i-1), index);
            Prefs.putPermissions(words[1], (i-1), index);
            Prefs.putAlias(words[2], (i-1), index);
            sb = new StringBuilder();
            for(int j = 3; j < words.length; j++) {
                sb.append(words[j]).append(" ");
            }
            sb.deleteCharAt(sb.length()-1);
            Prefs.putName(sb.toString(), (i-1), index);
        }

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
