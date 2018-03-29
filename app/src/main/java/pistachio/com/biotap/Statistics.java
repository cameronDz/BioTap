package pistachio.com.biotap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

/*
 * Statistics.java                      @author(Cameron Dziurgot)
 * @version(Original)   Class used in displaying and storing statistics
 * from the application.
 *
 * @version(2015.12.21) Reformatting and commenting. Buttons and button
 * listeners added, as well as layout for future refactoring and updates.
 *
 * @version(2015.12.31) Buttons added to increase and decrease weights
 * of individual Tap elements on the dissimilarity score. Several parts
 * of the Statistics view now able to be changed.
 * ---POSSIBLE REFACTORING---
 * Reformat button listeners so they can be set up in their own class or
 * in own method. More comments, Logs, and AlertDialogs to be added.
 *
 * @version(2016.1.4)   Private Button myOnClick method created to send
 * all increase and decrease buttons through. Detailed Analysis button
 * pop-up added. Several debugging Logs edited. All previous version
 * refactoring suggestions implemented.
 *
 * @version(2016.1.8)   Moved popUp method to a utility class. Currently
 * set up to delete all files, 5 total, on delete all file listener.
 */

public class Statistics extends AppCompatActivity {

    public static int success = 0;
    public static int fail = 0;
    public static double score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * TextViews for statistics page
         * statView01 == attempts
         * statView02 == accepted
         * statView03 == score
         * statView04 == acceptance percentage
         * statView05 == coordinates weight
         * statView06 == duration weight
         * statView07 == timing weight
         */
        TextView statView01 = (TextView) findViewById(R.id.statText01);
        TextView statView02 = (TextView) findViewById(R.id.statText02);
        TextView statView03 = (TextView) findViewById(R.id.statText03);
        TextView statView04 = (TextView) findViewById(R.id.statText04);
        TextView statView05 = (TextView) findViewById(R.id.statText05);
        TextView statView06 = (TextView) findViewById(R.id.statText06);
        TextView statView07 = (TextView) findViewById(R.id.statText07);

        /*
         * Button sets/Buttons & Listeners for statistics page
         * dButt1/iButt1 == decrease/increase passable score
         * dButt2/iButt2 == decrease/increase coordinate weight
         * dButt3/iButt3 == decrease/increase duration weight
         * dButt4/iButt4 == decrease/increase timing weight
         * detailButt    == pop up explaining score and weight
         * deleteButt    == delete all stats and reset weights
         */

        // First decrease button listener (passable score)
        Button dButt1 = (Button) findViewById(R.id.statButt01);
        dButt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.score = myOnClick((int)MainActivity.score,
                        -(int)MainActivity.inc, (int)MainActivity.min,
                        (int)MainActivity.max, 1, (TextView) findViewById(R.id.statText04),
                        "decrease passing score");
            }
        });

        // First increase button listener (passable score)
        Button iButt1 = (Button) findViewById(R.id.statButt02);
        iButt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.score = myOnClick((int)MainActivity.score,
                        (int)MainActivity.inc, (int)MainActivity.min,
                        (int)MainActivity.max, 1, (TextView) findViewById(R.id.statText04),
                        "increase passing score");
            }
        });

        // Second decrease button listener (coordinate weight)
        Button dButt2 = (Button) findViewById(R.id.statButt03);
        dButt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Compare.wtCoord = myOnClick((int)(Compare.wtCoord*100),
                        -(int)(Compare.inc*100), (int)(Compare.min*100),
                        (int)(Compare.max*100), 100, (TextView)findViewById(R.id.statText05),
                        "decrease coordinate weight")/100;
            }
        } );

        // Second increase button listener (coordinate weight)
        Button iButt2 = (Button) findViewById(R.id.statButt04);
        iButt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Compare.wtCoord = myOnClick((int)(Compare.wtCoord*100),
                        (int)(Compare.inc*100), (int)(Compare.min*100),
                        (int)(Compare.max*100), 100, (TextView)findViewById(R.id.statText05),
                        "increase coordinate weight")/100;
            }
        });

        // Third decrease button listener (duration weight)
        Button dButt3 = (Button) findViewById(R.id.statButt05);
        dButt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Compare.wtDuration = myOnClick((int)(Compare.wtDuration*100),
                        -(int)(Compare.inc*100), (int)(Compare.min*100),
                        (int)(Compare.max*100), 100, (TextView)findViewById(R.id.statText06),
                        "decrease duration weight")/100;
            }
        });

        // Third increase button listener (duration weight)
        Button iButt3 = (Button) findViewById(R.id.statButt06);
        iButt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Compare.wtDuration = myOnClick((int)(Compare.wtDuration*100),
                        (int)(Compare.inc*100), (int)(Compare.min*100),
                        (int)(Compare.max*100), 100, (TextView)findViewById(R.id.statText06),
                        "increase duration weight")/100;
            }
        });

        // Fourth decrease button listener (timing weight)
        Button dButt4 = (Button) findViewById(R.id.statButt07);
        dButt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Compare.wtTime = myOnClick((int)(Compare.wtTime*100),
                        -(int)(Compare.inc*100), (int)(Compare.min*100),
                        (int)(Compare.max*100), 100, (TextView)findViewById(R.id.statText07),
                        "increase timing weight")/100;
            }
        });

        // Fourth increase button listener (timing weight)
        Button iButt4 = (Button) findViewById(R.id.statButt08);
        iButt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Compare.wtTime = myOnClick((int)(Compare.wtTime*100),
                        (int)(Compare.inc*100), (int)(Compare.min*100),
                        (int)(Compare.max*100), 100, (TextView)findViewById(R.id.statText07),
                        "increase timing weight")/100;
            }
        });

        // Detail button listener
        Button detailButt = (Button) findViewById(R.id.statButt09);
        detailButt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Bio.Statistics", "onCreate, detailButt.setOnClickListener: Detail button pressed.");

                // Message to user explaining score and weight
                String message = "SCORE: The difference in an attempted passkey compared " +
                        "to the saved passkey.\n" +
                        "WEIGHT: The amount each element in a Tap is weighed to \n" +
                        "---ELEMENTS--- \n" +
                        "COORDINATE: The X & Y location of each Tap on the screen.\n" +
                        "DURATION: The amount of time a finger is pressed down on the screen.\n" +
                        "TIMING: The relative time difference between each Tap in a sequence " +
                        "between on another.";
                // Confirmation by user message
                String confirmation = "Return to Statistics";
                // Message displayed when user presses Detailed Analysis
                TapUtilities.toPopUp(message, confirmation, false, Statistics.this);
            }
        });

        // Delete button listener
        Button deleteButt = (Button) findViewById(R.id.statButt10);
        deleteButt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("Bio.Statistics", "onCreate, deleteButt.setOnClickListener: Delete All button pressed.");
                // Current statistics numbers reset
                Statistics.success = 0;
                Statistics.fail = 0;
                Statistics.score = 0;
                // Erase files
                TapUtilities.writeFile("average", "", Statistics.this, Context.MODE_PRIVATE);
                TapUtilities.writeFile("coordWeight", "", Statistics.this, Context.MODE_PRIVATE);
                TapUtilities.writeFile("durationWeight", "", Statistics.this, Context.MODE_PRIVATE);
                TapUtilities.writeFile("timeWeight", "", Statistics.this, Context.MODE_PRIVATE);
                TapUtilities.writeFile("overScore", "", Statistics.this, Context.MODE_PRIVATE);
                // Current weights reset
                Compare.wtCoord = 1.0;
                Compare.wtDuration = 1.0;
                Compare.wtTime = 1.0;
                // Score reset
                MainActivity.score = 15.0;

                // Message displayed when user deletes all current stats
                String message = "All data has been deleted and and weights reset.";
                String confirmation = "Return to Main Menu";
                TapUtilities.toPopUp(message, confirmation, true, Statistics.this);

            }
        });

        /*
         * Current Statistics and weights are prepared from the Statistics
         * and Compare class and sent to the Statistics View.
         */
        // Stats variables
        int iAttempts = Statistics.success + Statistics.fail;
        int iAccepted = Statistics.success;
        double iPercent = 0.0;

        // Get percent from attempts/accepts, check for division by 0
        if(iAttempts != 0) {
            iPercent = (double)iAccepted/iAttempts;
        }

        // Set the text of each stat in view
        String x = Integer.toString(iAttempts);
        statView01.setText(x);
        String y = new DecimalFormat("###.##").format(iPercent*100) + "%";
        statView02.setText(y);
        statView03.setText(new DecimalFormat("###.##").format(Statistics.score));
        statView04.setText(new DecimalFormat("###.##").format(MainActivity.score));
        // Set the text of each weight in view
        statView05.setText(new DecimalFormat("#.##").format(Compare.wtCoord));
        statView06.setText(new DecimalFormat("#.##").format(Compare.wtDuration));
        statView07.setText(new DecimalFormat("#.##").format(Compare.wtTime));
    }

    /**
     * Button method used to modify weight and scores of different weights
     * and scores in evaluating a passkey.
     *
     * @param weight The weight/score of the passkey acceptance
     * @param increment Amount weight/score is to be changed.
     * @param min Minimum value of weight/score
     * @param max Maximum value of weight/score
     * @param fact Amount the weight/score is factored when converting to TextView
     * @param t TextView to be updated on stats page
     * @param desc String description of Button
     *
     * @return The modified weight/score being stored.
     */
    private double myOnClick(int weight, int increment, int min, int max, int fact, TextView t, String desc) {
        // Debugging Log
        Log.d("Bio.Statistics", "myOnClick: " + desc + " modification attempt@" + increment + ", current@" + weight);

        // Makes sure weight is going to be modified
        if ((weight > min && increment < 0) || (weight < max && increment > 0)) {
            // Makes sure weight doesn't go below min
            if (weight > min && (weight + increment) < min) {
                weight = min;
                // Makes sure weight doesn't go above max
            } else if (weight < max && (weight + increment > max)) {
                weight = max;
                // Increments weight.
            } else {
                weight += increment;
            }

            // Update the TextView
            String s = new DecimalFormat("#.##").format((double)weight/fact);
            t.setText(s);

            // Debugging Log
            Log.d("Bio.Statistics", "myOnClick: " + desc + " Success: modified by@" +
                    increment + ", to@" + weight);

            // Weight/score not changed
        } else {
            // Strings for pop-up
            String message = "Could not " + desc + " because value is already at limit.";
            String confirmation = "Ok.";
            // Pop-up explaining why a modification wasn't made
            TapUtilities.toPopUp(message, confirmation, false, this);
            // Debugging Log
            Log.d("Bio.Statistics", " myOnClick: " + desc + " Failure: weight already at limit@" +
                    weight + ". Can not be modified by@" + increment);
        }

        return weight;
    }
}