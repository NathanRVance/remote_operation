package net.remoteoperation.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import net.remoteoperation.util.Onep.ClientOnep;
import net.remoteoperation.util.Onep.DataportDescription;
import net.remoteoperation.util.Onep.OneException;
import net.remoteoperation.util.Onep.Result;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nathav63 on 6/24/15.
 */
public class ExositeUtil {

    private Context context;
    private String CIK;
    private ArrayList<String> aliases;
    private ClientOnep onep;
    private ExositeCallback callback;
    private ProgressDialog progressDialog;

    public static final String RESULTS = "Results";


    public ExositeUtil(Context context) {
        this.context = context;
        this.CIK = Prefs.getCIK();
        if(CIK.equals("")) {
            throw new RuntimeException("null cik");
        }

        onep = new ClientOnep("http://m2.exosite.com/api:v1/rpc/process", 3, CIK);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Connecting to servers...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public enum Mode {
        READ,
        WRITE,
        CREATE,
        DELETE,
    }

    public interface ExositeCallback {
        void onComplete(Mode mode, Bundle results);
    }

    public void updateItems(ExositeCallback callback, ArrayList<String> aliases) {
        this.callback = callback;
        this.aliases = aliases;

        progressDialog.show();

        new ReadTask().execute();
    }

    public void commitItems(ExositeCallback callback, ArrayList<String> aliases, HashMap<String, String> values) {
        this.callback = callback;
        this.aliases = aliases;

        progressDialog.show();

        new WriteTask().execute(values);
    }

    public void createDataport(String alias, ExositeCallback callback) {
        this.callback = callback;
        new CreateDataport().execute("string", alias);
    }

    public void deleteDataport(String alias, ExositeCallback callback) {
        this.callback = callback;
        new DeleteDataport().execute(alias);
    }

    private void postErrorMessage(final String message) {
        System.out.println(message);
    }

    class ReadTask extends AsyncTask<Void, Void, ArrayList<String>> {
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> values = new ArrayList<>();
            boolean fail = false;
            for(int i = 0; i < aliases.size(); i++) {
                try {
                    net.remoteoperation.util.Onep.Result res = onep.read(aliases.get(i));
                    if (res.getStatus().equals(Result.OK)) {
                        String read = res.getMessage();
                        JSONArray dataarr = (JSONArray) JSONValue.parse(read);
                        JSONArray data1 = (JSONArray) dataarr.get(0);
                        values.add(i, "" + data1.get(1));
                    } else {
                        fail = true;
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if(fail) {
                postErrorMessage("Failed to read");
            }
            return values;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(ArrayList<String> results) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(RESULTS, results);
            callback.onComplete(Mode.READ, bundle);
            progressDialog.dismiss();
        }
    }

    class WriteTask extends AsyncTask<HashMap, Void, Void> {

        protected Void doInBackground(HashMap... valueses) {
            HashMap<String, String> values = valueses[0];
            boolean fail = false;
            for(int i = 0; i < aliases.size(); i++) {
                try {
                    if ( ! onep.write(aliases.get(i), values.get(aliases.get(i))).getStatus().equals(Result.OK)) {
                        fail = true;
                    }
                } catch (OneException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(fail) {
                postErrorMessage("Failed to write");
            }
            return null;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(Void v) {
            callback.onComplete(Mode.WRITE, new Bundle());
            progressDialog.dismiss();
        }
    }


    class CreateDataport extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... values) {
            String type = values[0];
            String alias = values[1];
            DataportDescription desc = new DataportDescription(type);
            desc.setName(alias);
            desc.retention.setCount(10);
            desc.retention.setDuration("infinity");
            //LinkedList<Object> p1= new LinkedList<>();
            //p1.add("div");
            //p1.add(2);
            //desc.preprocess.add(p1);
            try {
                if(! onep.create(alias, desc).getStatus().equals(Result.OK)) {
                    postErrorMessage("Failed to create for alias " + alias);
                }
            } catch (OneException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(Void v) {
            callback.onComplete(Mode.CREATE, new Bundle());
        }
    }

    class DeleteDataport extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... values) {
            String alias = values[0];
            try {
                net.remoteoperation.util.Onep.Result res = onep.lookup(CIK, "alias", alias);
                if ( ! (res.getStatus().equals(Result.OK) && onep.drop(CIK, res.getMessage()).getStatus().equals(Result.OK))) {
                    postErrorMessage("Failed to delete");
                }
            } catch (OneException e) {
                e.printStackTrace();
            }
            return null;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(Void v) {
            callback.onComplete(Mode.DELETE, new Bundle());
        }
    }
}
