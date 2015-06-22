package net.remoteoperation.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.exoparser.ExoParser;

/**
 * Created by nathav63 on 6/20/15.
 */
public class ReceiverActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if(Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            TextView textView = (TextView) findViewById(R.id.textView);
            if(ExoParser.parse(uri, this))
                textView.setText("File imported successfully! You can close this window now!");
            else
                textView.setText("Failed to import file. Is it the wrong format?");
        }
    }

}
