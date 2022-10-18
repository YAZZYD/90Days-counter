package dz.iyb.recoverydaycount;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.DAYS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.nio.charset.IllegalCharsetNameException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton button;
    private ProgressBar Pbar;
    private AppCompatTextView text;         //Text where number of days appear
    private DataBaseOperator DB;           //DataBaseHelper
    private Animation ScaleUp, ScaleDown; // Scale for animation, XML resource in anim directory
    private CompletionFragment fragment;
    private String days = "0"; // contains number of days of appear
    public static final String DB_NAME = "days.db"; //database filename

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        text = findViewById(R.id.n_days);
        Pbar = findViewById(R.id.count_progress_bar);
        ScaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        ScaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        File DBFile = this.getDatabasePath(DB_NAME);    //get the database File
        if (DBFile.exists()) { //if there dataBase exists


            DB = new DataBaseOperator(this, DB_NAME);
            String StartDate = DB.getData();   // Get date of quit from database
            if (StartDate != null) { //if date is found

                days = DateOperator.CalculateDays(StartDate, DateOperator.getCurrentDate()); //calculate days between current date and quit date
                days="90";// test
                if (Integer.parseInt(days) == 90) {     //if challenge is completed
                    fragment = new CompletionFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                            .commit();  //Show the fragment
                } else {

                    text.setText(calculateValue(days) + "/ 90");  //appear days
                    Pbar.setProgress(Integer.parseInt(days)); //update progress bar
                    button.setText("STOP"); //Set next available option will be stop counting
                }
                button.setOnClickListener(v -> {
                    animateButton();        //animation when clicking button
                    stopCount();            //stop counting
                    text.setText("0 / 90"); //update shown days
                });
            }
        } else {
            button.setText("START");        // available option will be start counting
            text.setText("0 / 90");
            Pbar.setProgress(0);
            button.setOnClickListener(view -> {
                animateButton();
                startCount();           //start counting
            });
        }
    }

    private void animateButton() {
        button.startAnimation(ScaleUp);
        button.startAnimation(ScaleDown);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Integer.parseInt(days) == 90 && fragment != null) {     //if chalange is completed and the fragment is still openning
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit(); // keep showing fragment
            button.setText("AGAIN"); //set next available option 'restart challenge'
        }
        button.setOnClickListener(view -> {
            animateButton();
            switch (String.valueOf(button.getText())) { //decide what instruction to execute according to available option
                case "STOP":
                    stopCount();
                    break;
                case "START":
                    startCount();
                    break;
                case "AGAIN":
                    restartCount();
                default:
                    Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG);
            }
        });
    }

    private void startCount() {
        DB = new DataBaseOperator(this, DB_NAME);
        DB.addData(DateOperator.getCurrentDate()); //get qui date
        Pbar.setProgress(0);
        button.setText("STOP");
    }

    private void stopCount() {
        this.deleteDatabase(DB_NAME); //delete dataBase file
        text.setText("0 / 90");
        Pbar.setProgress(0);
        button.setText("START");
    }

    private void restartCount() {
        this.deleteDatabase(DB_NAME);
        DB = new DataBaseOperator(this, DB_NAME);
        DB.addData(DateOperator.getCurrentDate());
        Pbar.setProgress(0);
        text.setText("0/90");
        button.setText("STOP");
        getSupportFragmentManager().beginTransaction().detach(fragment).commit(); //detach fragment

    }

    private String calculateValue(String days) {
        return String.valueOf(Integer.parseInt(days) * 100 / 90); // progress = current days*100/90days
    }
}