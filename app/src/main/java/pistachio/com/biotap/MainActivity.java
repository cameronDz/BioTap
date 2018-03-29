package pistachio.com.biotap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;

/*
 * MainActivity.java            @author(Luke Bro & Travis Moretz)
 * @version(Original)   A tap sequence is entered by a user, and
 * compared to an existing sequence.
 * @version(2015.12.21)             @editor(Cameron Dziurgot)
 * Added dissimilarity score to the end of each popup message. Some
 * comments and reformatting. Refactoring needed: Some code is copy/
 * pasted from RegisterActivity class. Require more comments
 * explaining code.
 *
 * @version(2016.1.4)   Global static variables added for passable
 * score for passkey. TapInterface removed and replaced with Tap class
 * alone since no reason for an interface in this situation.
 *
 * @version(2016.1.5)   Extends TapActivity class. Grid listener now
 * completely in separate class. Button listener in TapActivity has
 * method overridden in this class that deals with computing a comparison
 * score. onCreate overrides parent and gets data standard deviation
 * and mean sequences from file.
 *
 * @version(2016.1.8)   Moved the mapping method to utility class, and
 * moved section in onCreate method that pulled sequence from file, put
 * it into the utility class. Also moved popUp method to utility class.
 * Creates several files and stores weights into each one.
 */

public class MainActivity extends TapActivity {

    // Standard deviation and mean of a saved passkey
    protected List<Tap> master;
    protected List<Tap> stdDev;

    // Passing score
    protected static double score = 25.0;
    protected static double min = 0.0;
    protected static double max = 500.0;
    protected static double inc = 5.0;

    /*
     * Initial creation of view. Overrides TapActivity onCreate. Gets
     * a saved standard deviation and mean sequence used in comparing
     * user entered passkey.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Bio.MainActivity", "onCreate");
        // Set view, call TapActivity onCreate
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        // Retrieving standard deviation and mean sequences
        String s[] = TapUtilities.meanAndSd(this);

        // Parse strings into sequences
        this.master = TapUtilities.mapToSeq(s[0]);
        this.stdDev = TapUtilities.mapToSeq(s[1]);
    }

    /*
     * Button listener for Start/Stop button. Comes up with a score by
     * comparing the user passkey and the stored passkey. Messages user
     * the result.
     */
    @Override
    protected void uniqueButtAction() {
        Log.d("Bio.MainActivity", "uniqueButtActivity");
        // Does not analyze and empty sequence
        if (this.taps.size() == 0) {
            return;
        }

        // Determines the score, sets to current score in stats
        double score = Compare.dissimilarityScore(this.master, this.taps, this.stdDev);
        Statistics.score = score;

        // Strings used in messaging user in pop up
        String message, confirmation;
        // Determines if passkey is passable, adds values to stats, pop up
        // message sent to user informing them of validity of passkey
        if (score <= MainActivity.score) {
            // User has a passing passkey score
            message = "You've been successfully authenticated. \nScore: " +
                    new DecimalFormat("####.##").format(score);
            confirmation = "Success";
            Statistics.success++;
            Statistics.score = ((Statistics.score * (Statistics.success +
                    Statistics.fail - 1)) + score) /
                    (Statistics.success + Statistics.fail);
        } else if (score < MainActivity.score * 2) {
            // Passkey did not pass, but is less than double the max score
            message = "Error, familiar passkey but not passable. \nScore: " +
                    new DecimalFormat("####.##").format(score);
            confirmation = "Retry";
            Statistics.fail++;
        } else {
            // Passkey did not pass, and is double the max score
            message = "Error, unrecognized tap sequence. \nScore: " +
                    new DecimalFormat("####.##").format(score);
            confirmation = "Retry";
            Statistics.fail++;
        }

        // Pop up message to user displaying score.
        TapUtilities.toPopUp(message, confirmation, false, MainActivity.this);
        // Sending weights and score to files
        String s = (new DecimalFormat("#.##").format(Compare.wtCoord)) + "\n";
        TapUtilities.writeFile("coordWeight", s, MainActivity.this, Context.MODE_APPEND);
        s = (new DecimalFormat("#.##").format(Compare.wtDuration)) + "\n";
        TapUtilities.writeFile("durationWeight", s, MainActivity.this, Context.MODE_APPEND);
        s = (new DecimalFormat("#.##").format(Compare.wtTime)) + "\n";
        TapUtilities.writeFile("timeWeight", s, MainActivity.this, Context.MODE_APPEND);
        s =  (new DecimalFormat("#,###.##").format(Statistics.score)) + "\n";
        TapUtilities.writeFile("overScore", s, MainActivity.this, Context.MODE_APPEND);
    }
}
