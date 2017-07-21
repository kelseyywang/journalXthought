//This class loads new questions if it is the first time
//the user opened the app today. Otherwise, transitions
//to all entries.
package com.journalxapp.kelseywang.journalx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todddavies.components.progressbar.ProgressWheel;

import java.util.Calendar;
import java.util.Scanner;
import stanford.androidlib.SimpleActivity;
import stanford.androidlib.util.RandomGenerator;

public class OpenedActivity extends SimpleActivity {
    private final int START_DAY_OF_YEAR = 201; //should be day which app is launched - 1
    private String q1, q2;
    private int month, day, year;

    //Sets a quote, calls helper methods to handle transitions,
    //spins the ProgressWheel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened);
        RandomGenerator rand = new RandomGenerator();
        String[] OPENING_QUOTES = getResources().getStringArray(R.array.opening_quotes);
        $TV(R.id.quote).setText(OPENING_QUOTES[rand.nextInt(0, OPENING_QUOTES.length - 1)]);
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);
        ProgressWheel pw = (ProgressWheel) findViewById(R.id.pw_spinner);
        pw.startSpinning();
        if (wroteEntryToday(month, day, year)) {
            intentToAll();
        }
        setQuestions(dayOfYear);
    }

    //If loading takes too long and user clicks to open offline,
    //transitions to all
    public void offlineClicked(View view) {
        intentToAll();
    }

    //Handles intent to transition to AllEntriesActivity
    private void intentToAll() {
        Intent goToAll = new Intent(this, AllEntriesActivity.class);
        startActivity(goToAll);
        finish();
    }

    //Gets questions from Firebase and sets private class
    //variables to the questions
    private void setQuestions(int day) {
        final DatabaseReference fb = FirebaseDatabase
                .getInstance().getReference();
        DatabaseReference fbQuestionsToday = fb.child("questions").child(Integer.toString(day - START_DAY_OF_YEAR));
        final DatabaseReference today1 = fbQuestionsToday.child("Q1");
        final DatabaseReference today2 = fbQuestionsToday.child("Q2");
        today1.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(DataSnapshot data) {
                                             String todayQ1 = (String) data.getValue();
                                             if (todayQ1 != null) {
                                                 setQ1(todayQ1);
                                             }
                                         }

                                         @Override
                                         public void onCancelled(DatabaseError databaseError) {
                                             Log.d("onCancelled: ", "" + databaseError);
                                         }
                                     }
        );
        today2.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(DataSnapshot data) {
                                             String todayQ2 = (String) data.getValue();
                                             setQ2(todayQ2);
                                         }

                                         @Override
                                         public void onCancelled(DatabaseError databaseError) {
                                             Log.d("onCancelled: ", "" + databaseError);
                                         }
                                     }
        );
    }

    //Sets first question and transitions to TodayEntryActivity
    //if both Q1 and Q2 are set
    private void setQ1(String myQ1) {
        if (myQ1 != null) {
            q1 = myQ1;
            if (q2 != null && !wroteEntryToday(month, day, year)) {
                Intent goToToday = new Intent(this, TodayEntryActivity.class);
                goToToday.putExtra("q1", q1);
                goToToday.putExtra("q2", q2);
                startActivity(goToToday);
                finish();
            }
        }
    }

    //Sets second question and transitions to TodayEntryActivity
    //if both Q1 and Q2 are set
    private void setQ2(String myQ2) {
        if (myQ2 != null) {
            q2 = myQ2;
            if (q1 != null && !wroteEntryToday(month, day, year)) {
                Intent goToToday = new Intent(this, TodayEntryActivity.class);
                goToToday.putExtra("q1", q1);
                goToToday.putExtra("q2", q2);
                startActivity(goToToday);
                finish();
            }
        }
    }

    //If the app has already been opened today, there will already be
    //an entry for the daily questions and will return true
    private boolean wroteEntryToday(int month, int day, int year) {
        String q1, a1, q2, a2;
        String mc, dc, yc,
                mm, dm, ym, favorited;
        try {
            Scanner scanner = new Scanner(openFileInput("thoughtsList.txt")).useDelimiter("@@@@");
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
                if (mc.equals(Integer.toString(month)) && dc.equals(Integer.toString(day))
                        && yc.equals(Integer.toString(year))) {
                    return true;
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return false;
    }



//This method is for manually resetting the list
    /*public void testThings() {
        List<String> thoughtArraylist = new ArrayList<>();
        thoughtArraylist.clear();
        thoughtArraylist.add(
                "A place where you form your most creative ideas:" + "@@@@" +
                    "The shower!" + "@@@@" +
                        "What's different about this space that allows it to be like that?" + "@@@@" +
                        "I am forced to stop and think about nothing except what I want to" + "@@@@"
                        + "2" + "@@@@" + "9" + "@@@@" + "2017" + "@@@@"
                        + "2" + "@@@@" + "10" + "@@@@" + "2017" + "@@@@" +
                        "false"
        );
        thoughtArraylist.add(
                "A circumstance where you did better than you practiced:" + "@@@@" +
                        "While playing piano" + "@@@@" +
                        "How can you replicate that?" + "@@@@" +
                        "Having more pressure?" + "@@@@"
                        + "2" + "@@@@" + "10" + "@@@@" + "2017" + "@@@@"
                        + "2" + "@@@@" + "12" + "@@@@" + "2017" + "@@@@" +
                        "false"
        );
        thoughtArraylist.add(
                "A way to re-motivate yourself after failure:" + "@@@@" +
                        "remind yourself of your potential" + "@@@@" +
                        "How can you re-motivate a team after failure?" + "@@@@" +
                        "let all team members know their individual worth and contributions" + "@@@@"
                        + "2" + "@@@@" + "11" + "@@@@" + "2017" + "@@@@"
                        + "2" + "@@@@" + "11" + "@@@@" + "2017" + "@@@@" +
                        "false"
        );
        thoughtArraylist.add(
                "A personality trait you are currently trying to improve:" + "@@@@" +
                        "Impatience with myself" + "@@@@" +
                        "What would you invent to make that better?" + "@@@@" +
                        "An app that promotes productive self-reflection... lmao" + "@@@@"
                        + "2" + "@@@@" + "12" + "@@@@" + "2017" + "@@@@"
                        + "2" + "@@@@" + "12" + "@@@@" + "2017" + "@@@@" +
                        "false"
        );

        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        for (int i = 0; i < thoughtArraylist.size(); i++) {
            writer.println(thoughtArraylist.get(i) + "@@@@");
        }
        writer.close();
     }*/
}

