package pistachio.com.biotap;

/*
 *  Tap.java                            @author(Luke Bro)
 *  @version(Original)  Data holder for an instance when a user touches
 *  the screen on a phone.
 *  @version(2015.12.21)                @editor(Cameron Dziurgot)
 *  Reformatting and commenting added.
 *  @version(2016.1.4)  No longer implements an interface.
 *  @version(2016.1.8)  Added an additional toString method that displays
 *  a user friendly version of a string, that can display a normal
 *  sequence as well as a sequence storing standard deviation variables.
 */

import android.util.Log;

public class Tap {

    // Timestamp of tap.
    private long time;

    // Duration of tap.
    private long duration;

    // X coordinate of tap.
    private int x;

    // Y coordinate of tap.
    private int y;

    /**
     * Create a new instance of a tap with an interval.
     *
     * @param time Initial touch down of tap
     * @param duration Time between touch down and lift off
     * @param x x coordinate of tap
     * @param y y coordinate of tap
     */
    public Tap(long time, long duration, int x, int y) {
        this.set(time, duration, x, y);
    }

    /**
     * Create a new instance of a tap without a duration.
     *
     * @param time Initial touch down of tap
     * @param x x coordinate of tap
     * @param y y coordinate of tap
     */
    public Tap(long time, int x, int y) {
        this.set(time, 0, x, y);
    }

    /**
     * Set properties on instance.
     *
     * @param time Initial touch down time of tap
     * @param duration Duration between touch down and lift off of tap
     * @param x x coordinate of tap
     * @param y y coordinate of tap
     */
    protected void set(long time, long duration, int x, int y) {
        this.time = time;
        this.duration = duration;
        this.x = x;
        this.y = y;
    }

    /**
     * Get string value of tap.
     * @return Each element of a Tap separated by "/" in a string
     */
    public String toString() {
        return this.getTime() + "/" + this.getDuration() + "/" + this.getX() + "/" + this.getY();
    }

    /**
     * Get a user friendly string value of tap.
     * @param sd determines if the sequence is a mean or standard
     *           deviation sequence
     * @return Tap encompassed in "[]", and elements separated by ", "
     */
    public String toUserString(boolean sd) {
        if (sd) {
            Log.d("Bio.Tap", "toUserString: sd");
            return "[" + Compare.longToDbl(this.getTime()) +
                    ", " + Compare.longToDbl(this.getDuration()) +
                    ", " + Compare.intToDbl(this.getX()) +
                    ", " + Compare.intToDbl(this.getY()) + "]";
        }
        Log.d("Bio.Tap", "toUserString: readable");
        return "[" + this.getTime() + ", " + this.getDuration() +
                ", " + this.getX() + ", " + this.getY() + "]";
    }

    /**
     * @return Time interval between touch down
     */
    public long getTime() { return this.time; }

    /**
     * @return Duration value of tap
     */
    public long getDuration() { return this.duration; }

    /**
     * Set the interval of the tap.
     * @param duration Value to be set as new duration
     * @return New duration value of tap
     */
    public long setDuration(long duration) { return this.duration = duration; }

    /**
     * @return X coordinate of tap
     */
    public int getX() { return this.x; }

    /**
     * @return Y coordinate of tap
     */
    public int getY() { return this.y; }

    /**
     * @return Relative touch down time of tap
     */
    public long getDownTime() { return this.time; }

    /**
     * Calculate the relative lift off timestamp of a tap by adding the
     * relative touchdown time and the duration of the tap.
     * @return Relative lift off time
     */
    public long getUpTime() { return this.time + this.duration; }
}
