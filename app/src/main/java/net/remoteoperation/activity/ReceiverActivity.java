package net.remoteoperation.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import net.remoteoperation.R;
import net.remoteoperation.util.ExoParser;
import net.remoteoperation.util.Prefs;

/**
 * Small activity that only serves to input exo files
 */
public class ReceiverActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs.init(this);

        setContentView(R.layout.activity_main);
        final Intent intent = getIntent();
        Uri uri = intent.getData();

        if(! ExoParser.parse(uri, this))
            Toast.makeText(this, "Error processing file", Toast.LENGTH_SHORT).show();

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

}
