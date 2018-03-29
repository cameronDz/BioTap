package pistachio.com.biotap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * RegisterActivity.java                @author(Travis Moretz)
 * @version(Original)   Takes in 3 Tap sequences from the application,
 * averages them, saves the average and the original 3 sequences.
 *
 * @version(2015.12.21)                 @editor(Cameron Dziurgot)
 * Reformat and edit comments in code. Some refactoring. Future
 * refactoring needed: Possible class name change.
 *
 * @version(2016.1.4)   Removed TapInterface, replaced with just Tap
 * class since in was repetitive and unnecessary. Class name changed
 * from RegisterActivity to RegisterActivity.
 *
 * @version(2016.1.5)   Extends TapActivity class. Grid listener now
 * completely in separate class. Button listener in TapActivity has
 * method overridden in this class that deals with computing number of
 * sequences user entered and creating/storing the mean and standard
 * deviation from those sequences. Progress bar added.
 *
 * @version(2016.1.8)   Moved popUp, sequence mapping methods to
 * utility class where they are static methods. Turned file writing
 * portion into a method in utility class.
 */

public class RegisterActivity extends TapActivity {
    // Sequences variable
    protected List<List<Tap>> average;

    // Progress bar variables
    protected ProgressBar progressBar;
    protected int cnt = 0;
    protected int trials = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set view, call TapActivity onCreate
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);

        // List of sequences
        this.average = new ArrayList<>();

        // Progress bar set up
        progressBar = (ProgressBar) findViewById(R.id.my_bar);

        // Pop up to user telling them to enter 3 sequences
        String m = "Go ahead and tap out three tap sequences, " +
                "pressing start and stop between each one.";
        String c = "Ok";
        TapUtilities.toPopUp(m, c, false, RegisterActivity.this);
    }

    /*
     * Button listener for Start/Stop button. Keeps track of the number
     * of sequences being used to register a new passkey.
     */
    @Override
    protected void uniqueButtAction() {
        // Initial messages check for sequence errors
        if (average.size() >= 1 && average.get(0).size() != taps.size()) {
            String m = "That tap sequence did not match your first one. " +
                    "We'll throw it, try again.";
            String c = "Retry.";
            TapUtilities.toPopUp(m, c, false, RegisterActivity.this);
        } else if (taps.size() <= 0) {
            String m = "No taps detected...";
            String c = "Retry.";
            TapUtilities.toPopUp(m, c, false, RegisterActivity.this);
        } else {
            // Adds a successful sequence to a list to be averaged
            average.add(taps);
            cnt = average.size();
            // Update progress bar
            progressBar.setProgress(cnt);

            // After third sequence entered, writes mean and standard
            // deviation to file, resets resets variables, messages user
            if (average.size() == trials) {
                TapUtilities.writeFile("average", averageString(),
                        RegisterActivity.this, Context.MODE_PRIVATE);

                average = new ArrayList<>();
                cnt = 0;
                progressBar.setProgress(cnt);
                // Pop up sends user back to main menu
                String m = "You've successfully registered three tap sequences.";
                String c = "Back to Menu";
                TapUtilities.toPopUp(m, c, true, RegisterActivity.this);
            }
        }
    }

    // Creates the average and standard deviation string representations
    // of the sequences
    protected String averageString() {
        Log.d("Bio.Statistics", "averageString()");
        // Calls Compare class to get mean and standard deviation sequences
        List<Tap> mean = Compare.meanSeq(this.average);
        List<Tap> stdDev = Compare.standDeviation(this.average);
        // Put mean and standard deviation sequences into string form
        return TapUtilities.seqToString(mean, false, false) + "\n" +
                TapUtilities.seqToString(stdDev, false, false);

    }
}




