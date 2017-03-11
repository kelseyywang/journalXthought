package com.example.kelseywang.journalx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todddavies.components.progressbar.ProgressWheel;

import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import stanford.androidlib.SimpleActivity;
//TODO: LOOK AT DEBUG LOGS TO SEE WHY THE MESSAGES AINT PRINTIN

public class OpenedActivity extends SimpleActivity {
    public static final String FIREBASE_USERNAME = "yeslek08@gmail.com";
    public static final String FIREBASE_PASSWORD = "yeslek";
    private FirebaseAuth mAuth;
    private final int START_DAY_OF_YEAR = 67; //should be day which app is launched - 1
    private String q1, q2;
    private String test = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened);

        ProgressWheel pw = (ProgressWheel) findViewById(R.id.pw_spinner);
        pw.startSpinning();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#418a8e")));
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(FIREBASE_USERNAME, FIREBASE_PASSWORD);
        setQuestions();
    }
    private void setQuestions() {
        Log.d("OPENED" , test);
        //get day of year
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        Log.d("DAY IS ", Integer.toString(day));

        final DatabaseReference fb = FirebaseDatabase
                .getInstance().getReference();
        DatabaseReference fbQuestionsToday = fb.child("questions").child(Integer.toString(day - START_DAY_OF_YEAR));
        Log.d("NUMBER QUESTION", Integer.toString(day - START_DAY_OF_YEAR));
        final DatabaseReference today1 = fbQuestionsToday.child("Q1");
        final DatabaseReference today2 = fbQuestionsToday.child("Q2");
        Log.d("OPENED2" , test);
        today1.addValueEventListener(new ValueEventListener() {
            @Override
             public void onDataChange (DataSnapshot data) {
                 String todayQ1 = (String) data.getValue();
                 if(todayQ1 != null) {
                     setQ1(todayQ1);
                 }
             }
             @Override
             public void onCancelled (DatabaseError databaseError){
                 Log.d("onCancelled: ", "" + databaseError);
             }
         }
        );
        today2.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange (DataSnapshot data) {
                 String todayQ2 = (String) data.getValue();
                 setQ2(todayQ2);
             }
             @Override
             public void onCancelled (DatabaseError databaseError){
                 Log.d("onCancelled: ", "" + databaseError);
             }
         }
        );
    }
    private void setQ1(String myQ1) {
        Log.d("Q1", test);
        if (myQ1 != null) {
            q1 = myQ1;
            if (q2 != null) {
                if (openedToday(q1)) {
                    Intent goToAll = new Intent(this, AllEntriesActivity.class);
                    startActivity(goToAll);
                }
                else {
                    Intent goToToday = new Intent(this, TodayEntryActivity.class);
                    goToToday.putExtra("q1", q1);
                    goToToday.putExtra("q2", q2);
                    startActivity(goToToday);
                }
            }
        }
    }

    private void setQ2(String myQ2) {
        Log.d("Q2", test);
        if (myQ2 != null) {
            q2 = myQ2;
            if (q1 != null) {
                setQ1(q1);
                //go back to setQ1 because need q1 info to call openedToday
                //occurs when q2 data is retrieved after q1's
            }
        }
    }

    //If the app has already been opened today, there wil already be
    //an entry for the daily questions and will return true
    private boolean openedToday(String todayQuestion1) {
        String q1, a1, q2, a2;
        String mc, dc, yc,
                mm, dm, ym;
        try {
            Scanner scanner = new Scanner(openFileInput("thoughtsList.txt")).useDelimiter("\\t|\\n");
            while (scanner.hasNext()) {
                q1 = scanner.next();
                a1 = scanner.next();
                q2 = scanner.next();
                a2 = scanner.next();
                mc = scanner.next();
                dc = scanner.next();
                yc = scanner.next();
                mm = scanner.next();
                dm = scanner.next();
                ym = scanner.next();
                if(q1.equals(todayQuestion1)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return false;
    }
}
