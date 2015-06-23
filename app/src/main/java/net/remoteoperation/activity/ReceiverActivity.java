package net.remoteoperation.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.remoteoperation.R;
import net.remoteoperation.exoparser.ExoParser;

/**
 * Small activity that only serves to input exo files
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
            if(ExoParser.parse(uri, this)) {
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
            } else {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_layout);
                TextView textView = new TextView(this);
                linearLayout.addView(textView);
                textView.setText("Failed to import file. Is it the wrong format?");
            }
        }
    }

}
