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
    private final int START_DAY_OF_YEAR = 67; //should be day which app is launched - 1
    private String q1, q2;
    private String test = "a";
    private int month, day, year;
    private String[] OPENING_QUOTES = {"\"In order to understand the world, one has to turn away from it on occasion.\"\n-Albert Camus",
            "\"Knowing yourself is the beginning of all wisdom.\"\n-Aristotle",
    "\"The only journey is the one within.\"\n-Rainer Maria Rilke",
    "\"A man wrapped up in himself makes a very small parcel.\"\n-John Ruskin",
    "\"Nothing is forever. Except atoms.\"\n-Dannika Dark",
    "\"Self is a sea boundless and measureless.\"\n-Kahlil Gibran",
    "\"Wherever you go, you take yourself with you. If you see what I mean.\"\n-Neil Gaiman",
    "\"The world should be a mirror that you reflect upon.\"\n-C. JoyBell C.",
    "\"When all think alike, then no one is thinking.\"\n-Walter Lippman",
    "\"To think creatively, we must be able to look afresh at what we normally take for granted.\"\n-George Kneller",
    "\"The best way to have a good idea is to have a lot of ideas.\"\n-Linus Pauling",
    "\"Discovery consists of seeing what everybody has seen and thinking what nobody has thought.\"\n-Albert von Szent-Gyorgy",
    "\"To regard old problems from a new angle, requires creative imagination and marks real advance in science.\"\n-Albert Einstein",
    "\"There's a way to do it better — find it.\"\n-Thomas Edison"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened);
        //testThings();
        RandomGenerator rand = new RandomGenerator();
        $TV(R.id.quote).setText(OPENING_QUOTES[rand.nextInt(0, OPENING_QUOTES.length - 1)]);
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);


        ProgressWheel pw = (ProgressWheel) findViewById(R.id.pw_spinner);
        pw.startSpinning();

        if (wroteEntryToday(month, day, year)) {
            Intent goToAll = new Intent(this, AllEntriesActivity.class);
            startActivity(goToAll);
            finish();
        }
        setQuestions(dayOfYear);
    }

    public void offlineClicked(View view) {
        Intent goToAll = new Intent(this, AllEntriesActivity.class);
        startActivity(goToAll);
        finish();
    }

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



//This method is for manually resetting the list RIP
    /*public void testThings() {
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
                "1 circumstance where you performed better than you practiced:" + "\t" +
                        "While playing piano" + "\n" +
                        "How can those circumstances be replicated?" + "\t" +
                        "Having more pressure?" + "\n"
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
                        "An app that promotes productive self-reflection... lmao" + "\n"
                        + "2" + "\t" + "12" + "\t" + "2017" + "\t"
                        + "2" + "\t" + "12" + "\t" + "2017" + "\t" +
                        "false"
        );

        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        for (int i = 0; i < thoughtArraylist.size(); i++) {
            writer.println(thoughtArraylist.get(i));
        }
        writer.close();
     }*/
}

