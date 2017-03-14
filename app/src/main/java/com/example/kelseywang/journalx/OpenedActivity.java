package com.example.kelseywang.journalx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todddavies.components.progressbar.ProgressWheel;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import stanford.androidlib.SimpleActivity;

public class OpenedActivity extends SimpleActivity {
    public static final String FIREBASE_USERNAME = "yeslek08@gmail.com";
    public static final String FIREBASE_PASSWORD = "yeslek";
    private FirebaseAuth mAuth;
    private final int START_DAY_OF_YEAR = 67; //should be day which app is launched - 1
    private String q1, q2;
    private String test = "a";
    private int month, day, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened);
        testThings();
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);


        ProgressWheel pw = (ProgressWheel) findViewById(R.id.pw_spinner);
        pw.startSpinning();
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#418a8e")));

        if (wroteEntryToday(month, day, year)) {
            Log.d(test, "1");
            Intent goToAll = new Intent(this, AllEntriesActivity.class);
            startActivity(goToAll);
        }
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(FIREBASE_USERNAME, FIREBASE_PASSWORD);
        setQuestions(dayOfYear);
    }

    public void offlineClicked(View view) {
        Intent goToAll = new Intent(this, AllEntriesActivity.class);
        startActivity(goToAll);
    }
    private void setQuestions(int day) {
        final DatabaseReference fb = FirebaseDatabase
                .getInstance().getReference();
        DatabaseReference fbQuestionsToday = fb.child("questions").child(Integer.toString(day - START_DAY_OF_YEAR));
        final DatabaseReference today1 = fbQuestionsToday.child("Q1");
        final DatabaseReference today2 = fbQuestionsToday.child("Q2");
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
        if (myQ1 != null) {
            q1 = myQ1;
            if (q2 != null && !wroteEntryToday(month, day, year)) {
                Intent goToToday = new Intent(this, TodayEntryActivity.class);
                goToToday.putExtra("q1", q1);
                goToToday.putExtra("q2", q2);
                startActivity(goToToday);
            }
        }
    }

    private void setQ2(String myQ2) {
        if (myQ2 != null) {
            q2 = myQ2;
            if (q1 != null && !wroteEntryToday(month, day, year)) {
                Intent goToToday = new Intent(this, TodayEntryActivity.class);
                goToToday.putExtra("q1", q1);
                goToToday.putExtra("q2", q2);
                startActivity(goToToday);
            }
        }
    }

    //If the app has already been opened today, there wil already be
    //an entry for the daily questions and will return true
    private boolean wroteEntryToday(int month, int day, int year) {

        String q1, a1, q2, a2;
        String mc, dc, yc,
                mm, dm, ym, favorited;
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
                favorited = scanner.next();
                if(mc.equals(Integer.toString(month)) && dc.equals(Integer.toString(day))
                        && yc.equals(Integer.toString(year))) {

                    return true;
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return false;
    }


//This method is for manually resetting the list RIP
    public void testThings() {

        Log.d(test, "ENTERED");

        List<String> thoughtArraylist = new ArrayList<>();
        thoughtArraylist.clear();
        thoughtArraylist.add(
                "1 place where you form your most creative ideas:" + "\t" +
                    "The shower!" + "\n" +
                        "What's unique about this space that allows it to be like that?" + "\t" +
                        "I am forced to stop and think about nothing except what I want to" + "\n"
                        + "2" + "\t" + "9" + "\t" + "2017" + "\t"
                        + "2" + "\t" + "10" + "\t" + "2017" + "\t" +
                        "false"
        );
        thoughtArraylist.add(
                "1 thing you wish you didn't give up:" + "\t" +
                        "Dance" + "\n" +
                        "If this was changed, you wouldn't have given that up:" + "\t" +
                        "If I watched Dance Moms back then..." + "\n"
                        + "2" + "\t" + "10" + "\t" + "2017" + "\t"
                        + "2" + "\t" + "12" + "\t" + "2017" + "\t" +
                        "false"
        );
        thoughtArraylist.add(
                "1 way to re-motivate yourself after failure:" + "\t" +
                        "remind yourself of your potential" + "\n" +
                        "Similarly, how can you re-motivate a team after failure?" + "\t" +
                        "let all team members know their individual worth and contributions" + "\n"
                        + "2" + "\t" + "11" + "\t" + "2017" + "\t"
                        + "2" + "\t" + "11" + "\t" + "2017" + "\t" +
                        "false"
        );
        thoughtArraylist.add(
                "1 personality trait you are currently trying to improve:" + "\t" +
                        "Impatience with myself" + "\n" +
                        "What would you invent to make that easier?" + "\t" +
                        "an app that promotes productive self-reflection... oh wait" + "\n"
                        + "2" + "\t" + "13" + "\t" + "2017" + "\t"
                        + "2" + "\t" + "13" + "\t" + "2017" + "\t" +
                        "false"
        );

        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        for (int i = 0; i < thoughtArraylist.size(); i++) {
            writer.println(thoughtArraylist.get(i));
            Log.d(test, thoughtArraylist.get(i));
        }
        writer.close();
     }
}

