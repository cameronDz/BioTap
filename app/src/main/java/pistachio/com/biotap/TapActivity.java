package pistachio.com.biotap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/*
 * TapActivity.java                 @author(Cameron Dziurgot)
 * @version(2016.1.5)   Activity of taking in a tap sequence. Creates
 * a grid and button in which other activities are to use and override
 * for their view. Grid listener takes in taps and creates sequence
 * whenever button listener is pressed and taking input.
 *
 * @version(2016.1.8)   Modify how x & y coordinates are saved so that
 * they are relative to the first tap in a sequence. Removed popUp
 * method and put into a utility class.
 */
public class TapActivity extends Activity {
    protected Button button;
    protected View grid;
    protected boolean listening;

    protected long absoluteTapTime = 0;
    protected Tap currentTap;
    protected ArrayList<Tap> taps;

    protected int initialX = 0;
    protected int initialY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Button listener
        this.button = (Button) findViewById(R.id.start_btn);
        this.button.setOnClickListener(buttonListener);
        // Grid listener
        this.grid = findViewById(R.id.grid);
        this.grid.setOnTouchListener(gridListener);
        // A tap sequence and listener boolean
        this.taps = new ArrayList<>();
        this.listening = false;
    }

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

    // Button listener for Start/Stop button.
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listening) {
                // Stop button pressed.
                Log.d("Bio.TapActivity", "buttonListener.onClick: Stopped Button Pressed");
                startButton();

                // Call unique action method which is over ridden in children
                uniqueButtAction();
                // Reset variables
                taps = new ArrayList<>();
                absoluteTapTime = 0;
                currentTap = null;
            } else {
                // Start button pressed.
                Log.d("Bio.TapActivity", "buttonListener.onClick: Start Button Pressed");
                stopButton();
            }
            // Invert listening.
            listening = ! listening;
        }
    };

    /*
     * When on, listens to where the grid is being touched and records
     * data in a Tap sequence.
     */
    View.OnTouchListener gridListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            Log.d("Bio.TapActivity", "gridListener.onTouch");
            // Checks to make sure listener is on
            if (listening) {
                // Decides if a touch down or lift off has occurred
                switch(e.getAction()) {
                    // When a touch down on the screen is heard by listener
                    case android.view.MotionEvent.ACTION_DOWN: {
                        //Creates an absolute time for the events to be
                        //based off. Used to create a relative time reference
                        if (absoluteTapTime == 0) {
                            absoluteTapTime = e.getEventTime();
                            initialX = (int) e.getX();
                            initialY = (int) e.getY();
                        }

                        // Creates a Tap based on touch down event
                        currentTap = new Tap(e.getEventTime() - absoluteTapTime,
                                (int) e.getX() - initialX, (int) e.getY() - initialY);
                        Log.d("Bio.TapActivity", "gridListener.onTouch, Touch Down: " + currentTap.toString());
                        break;
                    }
                    // When a lift off screen is heard by the listener
                    case android.view.MotionEvent.ACTION_UP:
                        // Sets tap duration based on the absolute time,
                        // current event time, and touch down time.
                        currentTap.setDuration((e.getEventTime() -
                                absoluteTapTime) -
                                currentTap.getTime());
                        // Adds the current tap, with duration, to sequence
                        taps.add(currentTap);
                        Log.d("Bio.TapActivity", "gridListener.onTouch, Lift Off: " + currentTap.toString());
                        break;
                    default:
                        Log.d("Bio.TapActivity", "gridListener.onTouch, Default");
                }
            }
            return listening;
        }
    };

    // Start button for starting a sequence
    protected void startButton() {
        Log.d("Bio.TapActivity", "startButton");
        String s = "Start";
        button.setText(s);
        button.setBackgroundColor(0xFF4CAF50);
    }

    // Stop button for stopping a sequence
    protected void stopButton() {
        Log.d("Bio.TapActivity", "stopButton");
        String s = "Stop";
        button.setText(s);
        button.setBackgroundColor(0xFFF44336);
    }

    // To be over ridden by inherited classes for button listener
    protected void uniqueButtAction() {
        // Empty.
    }
}
