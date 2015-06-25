package net.remoteoperation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import net.remoteoperation.viewbuilder.MainViewBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 6/24/15.
 */
public class ExositeUtil {

    private Context context;
    private int index;
    private String mCIK;
    private String[] aliases;
    private HashMap<String, String> aliasTypes;
    private SharedPreferences prefs;


    public ExositeUtil(Context context, int index, HashMap<String, String> aliasTypes) {
        this.context = context;
        this.index = index;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mCIK = prefs.getString("cik" + index, "");
        if(mCIK.equals("")) {
            throw new RuntimeException("null cik");
        }

        this.aliasTypes = aliasTypes;
        this.aliases = aliasTypes.keySet().toArray(new String[aliasTypes.size()]);

    }

    public void updateItems() {
        new ReadTask().execute();
    }

    public void commitItems() {
        String[] values = new String[aliases.length * 2];
        for(int i = 0; i < aliases.length; i++) {
            values[i*2] = aliases[i];
            values[i*2+1] = prefs.getString("value" + i + " " + index, "");
        }
        new WriteTask().execute(values);
    }

    public void callback() {
        MainViewBuilder.refreshViews();
    }

    private void postErrorMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    class ReadTask extends AsyncTask<Void, Integer, ArrayList<Result>> {
        private static final String TAG = "ReadTask";
        private Exception exception;
        protected ArrayList<Result> doInBackground(Void... params) {
            exception = null;
            // call to OneP
            OnePlatformRPC rpc = new OnePlatformRPC();
            String responseBody = null;
            try {
                String requestBody = "{\"auth\":{\"cik\":\"" + mCIK
                        + "\"},\"calls\":[";
                for (String alias: aliases) {
                    requestBody += "{\"id\":\"" + alias + "\",\"procedure\":\"read\","
                            + "\"arguments\":[{\"alias\":\"" + alias + "\"},"
                            + "{\"limit\":1,\"sort\":\"desc\"}]}";
                    if (alias != aliases[aliases.length - 1]) {
                        requestBody += ',';
                    }
                }
                requestBody += "]}";
                Log.v(TAG, requestBody);
                // do this just to check for JSON parse errors on client side
                // while debugging. it can be removed for production.
                JSONObject jo = new JSONObject(requestBody);
                responseBody = rpc.callRPC(requestBody);

                Log.v(TAG, responseBody);
            } catch (JSONException e) {
                this.exception = e;
                Log.e(TAG, "Caught JSONException before sending request. Message:" + e.getMessage());
            } catch (HttpRPCRequestException e) {
                this.exception = e;
                Log.e(TAG, "Caught HttpRPCRequestException " + e.getMessage());
            } catch (HttpRPCResponseException e) {
                this.exception = e;
                Log.e(TAG, "Caught HttpRPCResponseException " + e.getMessage());
            }

            if (responseBody != null) {
                try {
                    ArrayList<Result> results = rpc.parseResponses(responseBody);
                    return results;
                } catch (OnePlatformException e) {
                    this.exception = e;
                    Log.e(TAG, "Caught OnePlatformException " + e.getMessage());
                } catch (JSONException e) {
                    this.exception = e;
                    Log.e(TAG, "Caught JSONException " + e.getMessage());
                }
            }
            return null;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(ArrayList<Result> results) {
            final SharedPreferences.Editor editor = prefs.edit();
            boolean hasError = false;
            if (results != null) {
                for(int i = 0; i < results.size(); i++) {
                    Result result = results.get(i);
                    String alias = aliases[i];
                    if (result.getResult() instanceof JSONArray) {
                        try {
                            JSONArray points = ((JSONArray)result.getResult());
                            if (points.length() > 0) {
                                JSONArray point = points.getJSONArray(0);
                                String value = null;
                                if(aliasTypes.get(alias).equals("int")) {
                                    value = String.valueOf(point.getInt(1));
                                } else if(aliasTypes.get(alias).equals("float")) {
                                    value = String.valueOf(point.getDouble(1));
                                } else {
                                    postErrorMessage("Error processing aliases: " + aliasTypes.get(alias));
                                }
                                if(value != null) {
                                    editor.putString("value" + i + " " + index, value);
                                    System.out.println("value" + i + " " + index + " : " + value);
                                }
                            } else {
                                hasError = true;
                                postErrorMessage("Error processing aliases");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException getting the result: " + e.getMessage());
                        }
                    } else {
                        Log.e(TAG, result.getStatus() + ' ' + result.getResult().toString());
                    }
                }
                editor.commit();
                callback();

            } else {
                Log.e(TAG, "null result in ReadTask.onPostExecute()");
                if (this.exception instanceof OnePlatformException) {
                    postErrorMessage("Received error from platform");
                } else {
                    postErrorMessage("Unable to connect to platform");
                }
            }
        }
    }

    class WriteTask extends AsyncTask<String[], Integer, ArrayList<Result>> {
        private static final String TAG = "WriteTask";

        protected ArrayList<Result> doInBackground(String[]... valueses) {
            assert(valueses.length == 1);
            String[] values = valueses[0];
            OnePlatformRPC rpc = new OnePlatformRPC();
            String responseBody = null;
            try {
                String requestBody = "{\"auth\":{\"cik\":\"" + mCIK
                        + "\"},\"calls\":[";
                for (int i = 0; i < values.length; i += 2) {
                    String alias = values[i];
                    requestBody += "{\"id\":\"" + alias + "\",\"procedure\":\"write\","
                            + "\"arguments\":[{\"alias\":\"" + alias + "\"},"
                            + "\"" + values[i + 1] + "\"]}";
                    // are we pointing to the last alias?
                    if (i != values.length - 2) {
                        requestBody += ',';
                    }
                }
                requestBody += "]}";
                Log.d(TAG, requestBody);
                // do this just to check for JSON parse errors on client side
                // while debugging. it can be removed for production.
                JSONObject jo = new JSONObject(requestBody);
                responseBody = rpc.callRPC(requestBody);

                Log.d(TAG, responseBody);
            } catch (JSONException e) {
                Log.e(TAG, "Caught JSONException before sending request. Message:" + e.getMessage());
            } catch (HttpRPCRequestException e) {
                Log.e(TAG, "Caught HttpRPCRequestException " + e.getMessage());
            } catch (HttpRPCResponseException e) {
                Log.e(TAG, "Caught HttpRPCResponseException " + e.getMessage());
            }

            if (responseBody != null) {
                try {
                    ArrayList<Result> results = rpc.parseResponses(responseBody);
                    return results;
                } catch (OnePlatformException e) {
                    Log.e(TAG, "Caught OnePlatformException " + e.getMessage());
                } catch (JSONException e) {
                    Log.e(TAG, "Caught JSONException " + e.getMessage());
                }
            }
            return null;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(ArrayList<Result> results) {
            //mDevice.setWriteInProgress(false);
        }
    }

}
