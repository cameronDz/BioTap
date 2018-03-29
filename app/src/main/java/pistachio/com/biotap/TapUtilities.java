package pistachio.com.biotap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 * TapUtilities.java                        @author(Cameron Dziurgot)
 * @version(2016.1.8)   Several methods taken out of various classes
 * and placed into this class as static methods. Specifically methods
 * involving converting sequences to and from strings. Also removed
 * code that pills sequence/string from a file. Changed sequence to
 * string method to also display user friendly strings. Method added
 * to extract weightAndScore into a string array, and be displayed in
 * a text view.
 * --- BROKEN---
 * toPopUp method currently broken, causing app failure.
 */
public class TapUtilities {

    /**
     * Map a string to sequence.
     * @param line A line from a file containing an un-parsed Tap sequence
     * @return A Tap sequence obtained from parsing out the line
     */
    protected static List<Tap> mapToSeq(String line) {
        Log.d("Bio.TapUtilities", "mapToSeq");
        ArrayList<Tap> sequence = new ArrayList<>();

        if(line == null || ("").equals(line)) {
            return sequence;
        }
        // Each Tap in a saved sequence is separated by a ","
        String[] taps = line.split(",");
        String[] unparsedTap;

        // Creates a tap string from each line element in the taps array
        for (String tap : taps) {
            // Each element in a Tap is separated by a "/"
            unparsedTap = tap.split("/");
            // Creates a Tap from values in the file and adds to a list
            sequence.add(new Tap(Long.parseLong(unparsedTap[0]),
                    Long.parseLong(unparsedTap[1]),
                    Integer.parseInt(unparsedTap[2]),
                    Integer.parseInt(unparsedTap[3])) );
        }
        // Return a sequence decrypted from a string
        return sequence;
    }

    /**
     * Converts a sequence to a string, separating each tap in a sequence
     * by "," and using the standard Tap toString format.
     * @param taps a List of Tap objects
     * @param readable tells whether the string is to be returned readable
     *                 for a user, or just parsed to a txt file
     * @param sd tells whether a readable string is a standard deviation
     * @return String representation of taps parameter
     */
    protected static String seqToString(List<Tap> taps, boolean readable, boolean sd) {
        Log.d("Bio.TapUtilities", "seqToString");
        String data = "";
        // Cycle through sequence, converting each Tap to string
        for (int i = 0; i < taps.size(); i++) {
            // user friendly string for a standard deviation
            if (readable) {
                data += taps.get(i).toUserString(sd);
                // standard compressed tap string
            } else {
                data += taps.get(i).toString();
            }

            // Each Tap separated by a ","
            if (i != taps.size() - 1) {
                data += ",";
                // Add an extra space between taps in readable sequences
                if (readable) {
                    data += "\n";
                }
            }
        }
        return data;
    }

    /**
     * Open the text file average and returns first two line in String array
     * @param context The view/context that is retrieving the file
     * @return A string array where [0] is the mean and [1] is the
     *         standard deviation sequence
     */
    protected static String[] meanAndSd(Context context) {
        Log.d("Bio.TapUtilities", "meanAndSd");
        // Retrieving standard deviation and mean sequences
        String s[] = new String[2];
        FileInputStream in;
        try {
            // Opens file for reading stored sequences
            in = context.openFileInput("average");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Throws exception if unable to read
            if (!bufferedReader.ready()) {
                in.close();
                throw new FileNotFoundException();
            }

            // Reads two lines, save as strings, close file
            s[0] = bufferedReader.readLine();
            s[1] = bufferedReader.readLine();
            in.close();
        } catch (FileNotFoundException e) {
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException");
            // Prompt user to create a registered sequence if one is not saved
            String m = "No tap sequence registered. Go back register a sequence.";
            String c = "Ok.";
            TapUtilities.toPopUp(m, c, true, context);
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException, postPopUp");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * Open the text file average and returns first two line in String array
     * @param context The view/context that is retrieving the file
     * @return A string array where [0] is the mean and [1] is the
     *         standard deviation sequence
     */
    protected static String[] weightsAndScore(Context context) {
        Log.d("Bio.TapUtilities", "weightsAndScore");
        // Retrieving standard deviation and mean sequences
        String s[] = new String[4];
        FileInputStream in;
        /*
         * Coordinate Weight
         */
        try {
            // Opens file for reading stored sequences
            in = context.openFileInput("coordWeight");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Throws exception if unable to read
            if (!bufferedReader.ready()) {
                in.close();
                throw new FileNotFoundException();
            }

            String line;
            // Reads and appends each line to the
            while ((line = bufferedReader.readLine()) != null) {
                if (s[0]==null) {
                    s[0] = line + "\n";
                } else {
                    s[0] += line + "\n";
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException");
            // Prompt user to create a registered sequence if one is not saved
            String m = "No tap sequence registered. Go back register a sequence.";
            String c = "Ok.";
            TapUtilities.toPopUp(m, c, true, context);
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException, postPopUp");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Duration Weight
         */
        try {
            // Opens file for reading stored sequences
            in = context.openFileInput("durationWeight");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Throws exception if unable to read
            if (!bufferedReader.ready()) {
                in.close();
                throw new FileNotFoundException();
            }

            String line;
            // Reads and appends each line to the
            while ((line = bufferedReader.readLine()) != null) {
                if (s[1] == null) {
                    s[1] = line + "\n";
                } else {
                    s[1] += line + "\n";
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException");
            // Prompt user to create a registered sequence if one is not saved
            String m = "No tap sequence registered. Go back register a sequence.";
            String c = "Ok.";
            TapUtilities.toPopUp(m, c, true, context);
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException, postPopUp");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Time Weight
         */
        try {
            // Opens file for reading stored sequences
            in = context.openFileInput("timeWeight");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Throws exception if unable to read
            if (!bufferedReader.ready()) {
                in.close();
                throw new FileNotFoundException();
            }

            String line;
            // Reads and appends each line to the
            while ((line = bufferedReader.readLine()) != null) {
                if (s[2] == null) {
                    s[2] = line + "\n";
                } else {
                    s[2] += line + "\n";
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException");
            // Prompt user to create a registered sequence if one is not saved
            String m = "No tap sequence registered. Go back register a sequence.";
            String c = "Ok.";
            TapUtilities.toPopUp(m, c, true, context);
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException, postPopUp");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Resulting Score
         */
        try {
            // Opens file for reading stored sequences
            in = context.openFileInput("overScore");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // Throws exception if unable to read
            if (!bufferedReader.ready()) {
                in.close();
                throw new FileNotFoundException();
            }

            String line;
            // Reads and appends each line to the
            while ((line = bufferedReader.readLine()) != null) {
                if (s[3] == null) {
                    s[3] = line + "\n";
                } else {
                    s[3] += line + "\n";
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException");
            // Prompt user to create a registered sequence if one is not saved
            String m = "No tap sequence registered. Go back register a sequence.";
            String c = "Ok.";
            TapUtilities.toPopUp(m, c, true, context);
            Log.d("Bio.TapUtilities", "meanAndSd: FileNotFoundException, postPopUp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Popup that sends user back to main menu
     * @param message Message to user in pop up
     * @param confirmation Wording on the confirmation button to close pop up
     * @param c context of the current view where the pop up will appear
     * @param bMenu boolean decides if confirmation sends user back to menu
     */
    protected static void toPopUp(String message, String confirmation,
                                  final boolean bMenu, Context c) {
        Log.d("Bio.TapUtilities", "toPopUp: preBuilder");
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        Log.d("Bio.TapUtilities", "toPopUp: postBuilder");
        builder.setMessage(message).setCancelable(false).setPositiveButton(confirmation,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Bio.TapUtilities", "toPopUp: preIfMenu");
                        if (bMenu) {
                            Log.d("Bio.TapUtilities", "toPopUp: inIfMenu");
                            Dialog d = (Dialog) dialog;
                            Intent myIntent = new Intent(d.getContext(), pistachio.com.biotap.Menu.class);
                            d.getContext().startActivity(myIntent);
                        }
                    }
                });
        // Display.
        builder.create().show();
    }

    /**
     * Write some data to a file in some context in a specified mode.
     * @param fileName file name data will be written to
     * @param data string representing the data
     * @param context context of origin
     * @param contextMode mode of writing
     */
    protected static void writeFile(String fileName, String data,
                                    Context context, int contextMode) {

        FileOutputStream file;
        // Opens the file to be written to
        try {
            file = context.openFileOutput(fileName, contextMode);
            // Write average and standard deviation sequences to fil
            try {
                file.write(data.getBytes());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
