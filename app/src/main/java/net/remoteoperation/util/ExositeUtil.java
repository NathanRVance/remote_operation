package net.remoteoperation.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import net.remoteoperation.util.Onep.ClientOnep;
import net.remoteoperation.util.Onep.DataportDescription;
import net.remoteoperation.util.Onep.OneException;
import net.remoteoperation.util.Onep.Result;
import net.remoteoperation.view.MainView;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nathav63 on 6/24/15.
 */
public class ExositeUtil {

    private Context context;
    private int index;
    private String CIK;
    private ArrayList<String> aliases;
    private MainView mainView;
    private ClientOnep onep;


    public ExositeUtil(Context context, int index, ArrayList<String> orderedKeys, MainView mainView) {
        this.context = context;
        this.index = index;
        this.CIK = Prefs.getCIK(index);
        if(CIK.equals("")) {
            throw new RuntimeException("null cik");
        }

        this.aliases = orderedKeys;
        this.mainView = mainView;

        onep = new ClientOnep("http://m2.exosite.com/api:v1/rpc/process", 3, CIK);
    }

    public void updateItems() {
        new ReadTask().execute();
    }

    public void commitItems() {
        new WriteTask().execute();
    }

    public void createDataport(String alias) {
        new CreateDataport().execute("string", alias);
    }

    public void deleteDataport(String alias) {
        new DeleteDataport().execute(alias);
    }

    public void callback() {
        mainView.refreshViews();
    }

    private void postErrorMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    class ReadTask extends AsyncTask<Void, Integer, ArrayList<Result>> {
        private Exception exception;
        protected ArrayList<Result> doInBackground(Void... params) {
            for(int i = 0; i < aliases.size(); i++) {
                try {
                    net.remoteoperation.util.Onep.Result res = onep.read(aliases.get(i));
                    if (res.getStatus().equals(Result.OK)) {
                        String read = res.getMessage();
                        JSONArray dataarr = (JSONArray) JSONValue.parse(read);
                        JSONArray data1 = (JSONArray) dataarr.get(0);
                        Prefs.putValue("" + data1.get(1), i, index);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return null;
        }

        // this is executed on UI thread when doInBackground
        // returns a result
        protected void onPostExecute(ArrayList<Result> results) {
            callback();
        }
    }

    class WriteTask extends AsyncTask<String[], Integer, ArrayList<Result>> {

        protected ArrayList<Result> doInBackground(String[]... values) {
            boolean fail = false;
            for(int i = 0; i < aliases.size(); i++) {
                try {
                    if ( ! onep.write(aliases.get(i), Prefs.getValue(i, index)).getStatus().equals(Result.OK)) {
                        fail = true;
                    }
                } catch (OneException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(fail) {
                System.out.println("Something bad happened.");
            }
            return null;
        }
    }


    class CreateDataport extends AsyncTask<String, Integer, ArrayList<Result>> {

        protected ArrayList<Result> doInBackground(String... values) {
            String type = values[0];
            String alias = values[1];
            DataportDescription desc = new DataportDescription(type);
            desc.setName(alias);
            desc.retention.setCount(10);
            desc.retention.setDuration("infinity");
            LinkedList<Object> p1= new LinkedList<>();
            p1.add("div");
            p1.add(2);
            desc.preprocess.add(p1);
            try {
                onep.create(alias, desc);
            } catch (OneException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    class DeleteDataport extends AsyncTask<String, Integer, ArrayList<Result>> {

        protected ArrayList<Result> doInBackground(String... values) {
            String alias = values[0];
            try {
                net.remoteoperation.util.Onep.Result res = onep.lookup(CIK, "alias", alias);
                if (res.getStatus().equals(Result.OK)){
                    String rid = res.getMessage();
                    onep.drop(CIK, rid);
                }
            } catch (OneException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
