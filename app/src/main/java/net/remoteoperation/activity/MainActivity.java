package net.remoteoperation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import net.remoteoperation.R;
import net.remoteoperation.util.Prefs;
import net.remoteoperation.viewbuilder.MainViewBuilder;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Prefs.init(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.alias_item_holder);
        MainViewBuilder.inflateLayout(linearLayout, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.refresh:
                MainViewBuilder.getExositeUtil().updateItems();
                return true;
            case R.id.commit:
                MainViewBuilder.getExositeUtil().commitItems();
                return true;
            case R.id.delete:
                Prefs.deleteCIK(MainViewBuilder.getIndex());
                MainViewBuilder.inflateLayout((LinearLayout) findViewById(R.id.alias_item_holder), this);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainViewBuilder.inflateLayout((LinearLayout) findViewById(R.id.alias_item_holder), this);
    }
}
