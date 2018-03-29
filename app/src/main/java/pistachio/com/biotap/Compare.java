package pistachio.com.biotap;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
 * Compare.java                         @author(Cameron Dziurgot)
 * @version(Original)   Several methods in class used for getting
 * dissimilarity score between two lists of TapInterfaces. Contains
 * method for taking in several sequences and averaging them to return
 * a sequence. Also has methods that calculate the standard deviation
 * between a given number of sequences, which returns modified values
 * in a TapInterface object for convenience of passing data.
 *
 * @version(2015.12.21) Little reformatting, code/comment notes. Testing
 * is needed since some editing has been done on formula where
 * initialization of specific variables is done within or outside of
 * loops.
 *
 * @version(2015.12.31) Logs added. Now gives score for attempts that do
 * not match tap count for master, with an added penalty. Sets default
 * weights for duration and coordinates lower.
 *
 * @version(2016.1.4)   Changing TapInterface to Tap, since there will
 * not be multiple types of Taps in the application. Logs also edited.
 */

public class Compare {

    // Weights for the values of each element in a Tap
    public static double wtTime = 1.0;
    public static double wtDuration = 1.0;
    public static double wtCoord = 1.0;
    // Weights min/max and increment
    public static double min = 0;
    public static double max = 2.5;
    public static double inc = .05;
    // Penalty applied to sequences that do no match size of master
    public static double penalty = 100.0;

    /**
     * Compare two Tap sequences.
     *
     * @param master Original sequence others are compared against
     * @param attempt User entered sequence attempt to replicate master
     * @param standDev Sequence that holds standard deviations for each
     *                 tap's element in the master sequence
     * @return An double value representing a score that is larger the
     *         greater the difference between the master and attempt
     *         sequences
     */
    public static double dissimilarityScore(List<Tap> master,
                                            List<Tap> attempt,
                                            List<Tap> standDev) {
        // Return value
        double magSum = 0.0;
        Log.d("Bio.Compare", "disSco magSum@" + magSum);
        // Number of taps to be compared in two sequences
        int usable = master.size();

        //Adds penalty for every additional or missing tap in a sequence
        if (master.size() != attempt.size()) {
            // If attempt is smaller than master, usable set to smaller
            if (master.size() > attempt.size()) {
                usable = attempt.size();
            }
            // Gets difference, adds penalty taps to score
            int diff = Math.abs(master.size() - attempt.size());
            magSum += (penalty * diff);
            Log.d("Bio.Compare", "disSco sequences!= magSum@" + magSum);
        }

        /*
         * Cycles through each tap in the sequences and compares them,
         * returning a score for each tap and combining all scores into
         * one sum
         */
        for (int i = 0; i < usable; i++) {
            double tapScore = tapFormula(master.get(i), attempt.get(i), standDev.get(i));
            magSum += tapScore;
            Log.d("Bio.Compare", "disSco: for(" + i + ") tapScore@" + tapScore + " magSum@" + magSum);
        }

        Log.d("Bio.Compare", "disSco return@" + magSum);
        return magSum;
    }


    /**
     * Breaks down Taps by their elements and performs formula.
     *
     * @param master A specific tap from a master sequence
     * @param attempt A specific tap from an attempt sequence
     * @param sd A specific tap from the standard deviation sequence
     * @return A double value that represents the difference between the
     *  master tap and the attempt tap
     */
    private static double tapFormula(Tap master, Tap attempt, Tap sd) {
        // Return value
        double total = 0.0;
        double addOn;
        Log.d("Bio.Compare", "tapForm total@" + total);

        /*
         * Calculates a dissimilarity score for each tap element, multiples
         * each score by its respective weight before adding to total.
         */
        addOn = longTapForm(master.getTime(), attempt.getTime(), sd.getTime());
        total += (wtTime * addOn);
        Log.d("Bio.Compare", "tapForm wtTime@" + wtTime + ", addOn@" + addOn + ", total@" + total);

        addOn = longTapForm(master.getDuration(), attempt.getDuration(), sd.getDuration());
        total += (wtDuration * addOn);
        Log.d("Bio.Compare", "tapForm wtDuration@" + wtDuration + ", addOn@" + addOn + ", total@" + total);

        addOn = intTapForm(master.getX(), attempt.getX(), sd.getX());
        total += (wtCoord * addOn);
        Log.d("Bio.Compare", "tapForm wtCoord@" + wtCoord + ", addOn@" + addOn + ", total@" + total);

        addOn = intTapForm(master.getY(), attempt.getY(), sd.getY());
        total += (wtCoord * addOn);
        Log.d("Bio.Compare", "tapForm: wtCoord@" + wtCoord + ", addOn@" + addOn + ", total@" + total);

        Log.d("Bio.Compare", "tapForm return@" + total);
        return total;
    }

    /**
     * Takes an array of Tap sequences and comes up with a standard
     * deviation Tap sequence.
     *
     * @param sequences A list of tap sequences used in obtaining an average
     *  and standard deviation sequence
     * @return A tap sequence that contains a standard deviation for each
     *  tap's element, gotten from comparing all sequences from the list
     */
    static public List<Tap> standDeviation(List<List<Tap>> sequences) {
        // Get number of sequences and number of taps in each sequence
        int trials = sequences.size();
        int numTaps = sequences.get(0).size();
        ArrayList<Tap> sd = new ArrayList<>();

        /*
         * Checks that all sequences are of the same size and returns an
         * empty standard deviation sequence if all sequences are not equal
         */
        for (int i = 0; i < trials; i++) {
            if (numTaps != sequences.get(i).size()) {
                return sd;
            }
        }

        // Get the mean sequence from all sequences
        List<Tap> mean = Compare.meanSeq(sequences);

        // Laps through each Tap in the sequences
        for (int a = 0; a < numTaps; a++) {
            // Initialize sum of square of each element
            long timeSum = 0, durSum = 0;
            int xSum = 0, ySum = 0;
            // Loop through each trial and sums each element
            for (int b = 0; b < trials; b++) {
                // Sum each down time element squared minus its mean
                timeSum += ((sequences.get(b).get(a).getTime() - mean.get(a).getTime()) *
                        (sequences.get(b).get(a).getTime() - mean.get(a).getTime()));
                // Sum each duration element squared minus its mean
                durSum  += ((sequences.get(b).get(a).getDuration() - mean.get(a).getDuration()) *
                        (sequences.get(b).get(a).getDuration() - mean.get(a).getDuration()));
                // Sum each x coordinate squared minus its mean
                xSum    += ((sequences.get(b).get(a).getX() - mean.get(a).getX()) *
                        (sequences.get(b).get(a).getX() - mean.get(a).getX()));
                // Sum each y coordinate squared minus its mean
                ySum    += ((sequences.get(b).get(a).getY() - mean.get(a).getY()) *
                        (sequences.get(b).get(a).getY() - mean.get(a).getY()));
            }

            /*
             * Standard deviations for each element calculated by taking
             * the square root of the sum of the square of each element
             * minus the parallel mean sequence element, and dividing it
             * by the number of trials.
             */
            double time = (Math.sqrt(timeSum)) / trials;
            double dur = (Math.sqrt(durSum)) / trials;
            double x = (Math.sqrt(xSum)) / trials;
            double y = (Math.sqrt(ySum)) / trials;
            // Standard deviation tap, casting used to keep precision
            Tap castedTap = new Tap(dblToLong(time), dblToLong(dur), dblToInt(x), dblToInt(y));
            // Tap added to standard deviation sequence
            sd.add(castedTap);
        }

        return sd;
    }

    /**
     * Get the mean of a list of sequences.
     *
     * @param seq A list of tap sequences used to create a master sequence
     * @return A tap sequence that is the average of all sequences from seq
     */
    public static List<Tap> meanSeq(List<List<Tap>> seq) {
        /*
         * Initialize average tap sequence, determine number of
         * trials, and number of taps in each sequence.
         */
        ArrayList<Tap> average = new ArrayList<>();
        int trial = seq.size();
        int numTap = seq.get(0).size();

        // Cycle through each tap of the sequences
        for (int k = 0; k < numTap; k++) {
            long timeSum = 0;
            long durSum = 0;
            int xSum = 0;
            int ySum = 0;
            // Cycle through each sequence at a specific tap
            for (int j = 0; j < trial; j++) {
                // Sum each respective element in the from all trials
                timeSum += seq.get(j).get(k).getTime();
                durSum  += seq.get(j).get(k).getDuration();
                xSum    += seq.get(j).get(k).getX();
                ySum    += seq.get(j).get(k).getY();
            }
            Tap ave = new Tap(timeSum/trial, durSum/trial, xSum/trial, ySum/trial);
            average.add(ave);
        }

        return average;
    }

    /**
     * Returns a dissimilarity score for a long element in a Tap
     *
     * @param mast A long element from a master Tap
     * @param att A long element from an attempt Tap
     * @param sd The standard deviation of the element in the master Tap
     * @return A double representing the difference in the master
     *  Tap element and the attempt Tap element
     */
    private static double longTapForm(long mast, long att, long sd) {
        double score = 0.0;
        // Checks for division by 0, then calculates score
        if (sd == 0) {
            return score;
        }
        score = (double)(Math.abs(att-mast)/longToDbl(sd));
        Log.d("Bio.Compare", "longTapForm mast@" + mast + ", att@" + att + ", sd@" + longToDbl(sd));
        return score;
    }

    /**
     * Returns a dissimilarity score for an integer element in a Tap
     *
     * @param mast An int element from a master Tap
     * @param att An int element from an attempt Tap
     * @param sd The standard deviation of the element in the master Tap
     * @return A double representing the difference in the master Tap
     *  element and the attempt Tap element
     */
    private static double intTapForm(int mast, int att, int sd) {
        double score = 0.0;
        // Checks for division by 0, then calculates score
        if (sd == 0) {
            return score;
        }
        score = (double)(Math.abs(att-mast)/intToDbl(sd));
        Log.d("Bio.Compare", "intTapForm mast@" + mast + ", att@" + att + ", sd@" + intToDbl(sd));
        return score;
    }

    /**
     * Convert double to long.
     *
     * @param d Double to be converted to long in order to be stored in
     *          a long element contained within a Tap
     * @return A long value 1000 times greater than the double parameter
     */
    protected static long dblToLong(double d) {
        return (long)(d*1000);
    }

    /**
     * Convert double to integer.
     *
     * @param d Double to be converted to int in order to be stored in
     *          an int element contained within a Tap
     * @return An int value 1000 times greater than the double parameter
     */
    protected static int dblToInt(double d) {
        return (int)(d*1000);
    }

    /**
     * Convert long to double.
     *
     * @param l Long value stored in a Tap to be converted back to a double
     * @return Double value, 1000th the value of the long parameter
     */
    protected static double longToDbl(long l) {
        return (double)l/1000.0;
    }

    /**
     * Convert integer to double.
     *
     * @param i Int value stored in a Tap to be converted back to a double
     * @return Double value, 1000th the value of the int parameter
     */
    protected static double intToDbl(int i) {
        return (double)i/1000.0;
    }
}
