package pistachio.com.biotap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

/*
 * AttemptDatabase.java                     @author(Cameron Dziurgot)
 * @version(2016.1.6)   Skeleton of the view and class that will
 * create a viewable database of all the attempts at passkeys. Currently
 * takes in standard deviation and mean sequences without making the
 * easier for user.
 *
 * @version(2016.1.8)   Added TextView for the weights and score of
 * attempts, which are now saved in a file.
 */
public class AttemptDatabase extends Activity {

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (item.getItemId() == R.id.action_settings ||
                super.onOptionsItemSelected(item));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Bio.AttemptDatabase", "onCreate");
        setContentView(R.layout.activity_database);
        super.onCreate(savedInstanceState);

        // Sets up mean and standard deviation in view
        TextView meanView = (TextView) findViewById(R.id.dataMeanDisplay);
        TextView sdView = (TextView) findViewById(R.id.dataSdDisplay);
        TextView coordWt = (TextView) findViewById(R.id.dataAttempt01);
        TextView durationWt = (TextView) findViewById(R.id.dataAttempt02);
        TextView timeWt = (TextView) findViewById(R.id.dataAttempt03);
        TextView overScore = (TextView) findViewById(R.id.dataAttempt04);

        // Retrieving standard deviation and mean sequences
        String[] s = TapUtilities.meanAndSd(this);

        // Re-map strings to sequences
        List<Tap> avgSeq = TapUtilities.mapToSeq(s[0]);
        List<Tap> devSeq = TapUtilities.mapToSeq(s[1]);

        // Converts sequences to user readable strings
        String avg = TapUtilities.seqToString(avgSeq, true, false);
        String dev = TapUtilities.seqToString(devSeq, true, true);

        // Get weight/score from file


        // Set sequences into database view
        meanView.setText(avg);
        sdView.setText(dev);

        // Get string array of weights/score
        String[] str = TapUtilities.weightsAndScore(AttemptDatabase.this);
        // Set weight/score view
        coordWt.setText(str[0]);
        durationWt.setText(str[1]);
        timeWt.setText(str[2]);
        overScore.setText(str[3]);
    }

}
