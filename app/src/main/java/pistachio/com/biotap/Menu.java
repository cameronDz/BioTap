package pistachio.com.biotap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/*
 * Menu.java                                @author(Travis Moretz)
 * @version(Original)   Set of buttons that direct user to different
 * areas of application.
 * @version(2015.12.21)                     @editor(Cameron Dziurgot)
 * Reformatting and comments added.
 * @version(2016.1.5)   Logs and comments edited for readability. Added
 * a database button listener that Logs the press, does not redirect
 * to another page yet.
 */

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Buttons to direct user to different part of application
        Button regBtn = (Button) findViewById(R.id.register_btn);
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        Button dataBtn = (Button) findViewById(R.id.data_btn);
        Button statBtn = (Button) findViewById(R.id.stat_btn);

        // Register button onClick redirection
        regBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Sends user to register screen
                Intent registerScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                Log.d("Bio.Menu", "regBtn.setOnClickListener: Pressed");
                startActivity(registerScreen);
            }
        });

        // Login button onClick redirection
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TESTING
                // Testing trying to fix the popUp method causing app
                // failure, mostlikely because of Context or View issues
                // TESTING
                String[] test = TapUtilities.meanAndSd(Menu.this);
                if (!("").equals(test[0])) {
                    // Sends user to login screen
                    Intent loginScreen = new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("Bio.Menu", "loginBtn.setOnClickListener: Pressed");
                    startActivity(loginScreen);
                } else {
                    TapUtilities.toPopUp("No recorded Sequences.",
                            "Stay on Main Menu.", true, Menu.this);
                }
            }
        });

        // Database button onCluck redirection
        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send to database screen
                Intent databaseScreen = new Intent(getApplicationContext(), AttemptDatabase.class);
                Log.d("Bio.Menu", "dataBtn.setOnClickListener: Pressed");
                startActivity(databaseScreen);
            }
        });

        // Statistics button onClick redirection
        statBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Sends user to statistics screen
                Intent statScreen = new Intent(getApplicationContext(), Statistics.class);
                Log.d("Bio.Menu", "statBtn.setOnClickListener: Pressed");
                startActivity(statScreen);
            }
        });
    }
}
