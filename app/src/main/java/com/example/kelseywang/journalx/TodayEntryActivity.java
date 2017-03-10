package com.example.kelseywang.journalx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import stanford.androidlib.*;
import java.io.PrintStream;
import java.util.*;

public class TodayEntryActivity extends SimpleActivity {
    public static final String FIREBASE_USERNAME = "yeslek08@gmail.com";
    public static final String FIREBASE_PASSWORD = "yeslek";
    private FirebaseAuth mAuth;
    private String test = "a";
    private List<String> questions = new ArrayList<>();
    private Map<Integer, String> dailyQuestions = new HashMap<Integer, String>();
    private final int START_DAY_OF_YEAR = 67; //should be day which app is launched - 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_entry);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#418a8e")));
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(FIREBASE_USERNAME, FIREBASE_PASSWORD);

        setDate();
        populateQuestionsMap();
        setQuestions();
    }

    private int[] calendarDates() {
        Calendar calendar = Calendar.getInstance();
        int[] dates = {calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR)};
        return dates;
    }

    private void populateQuestionsMap() {
        for(int i = 0; i < questions.size()-1; i+=2) {
            dailyQuestions.put(START_DAY_OF_YEAR + i/2, questions.get(i));
            dailyQuestions.put(START_DAY_OF_YEAR + i/2 + 1000, questions.get(i+1)); //adds 1000 to key for second question
        }
    }

    private void setQuestions() {
        //get day of year
        int day = calendarDates()[3];

        final DatabaseReference fb = FirebaseDatabase
                .getInstance().getReference();
        DatabaseReference fbQuestionsToday = fb.child("questions").child(Integer.toString(day - START_DAY_OF_YEAR));
        final DatabaseReference today1 = fbQuestionsToday.child("Q1");
        final DatabaseReference today2 = fbQuestionsToday.child("Q2");
        today1.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange (DataSnapshot data) {
                 String todayQ1 = (String) data.getValue();
                 handleOpenedToday(todayQ1);
                 $TV(R.id.question_1).setText(todayQ1);
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
                     // No handleQuestionOpened
                     $TV(R.id.question_2).setText(todayQ2);
                 }
                 @Override
                 public void onCancelled (DatabaseError databaseError){
                     Log.d("onCancelled: ", "" + databaseError);
                 }
             }
        );
    }

    //If the app has already been opened today, there wil already be
    //an entry for the daily questions and the app will redirect
    //to the list of all entries
    private void handleOpenedToday(String todayQuestion1) {
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
                    Intent goToMenu = new Intent(this, AllEntriesActivity.class);
                    startActivity(goToMenu);
                    break;
                }
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    private void setDate() {
        int[] date = calendarDates();
        String m = Integer.toString(date[0] + 1);
        String d = Integer.toString(date[1]);
        String y = Integer.toString(date[2]);
        $TV(R.id.date_view).setText("Today is: " + m + "/" + d + "/" + y);
    }

    public void saveClicked(View view) {
        PrintStream output = new PrintStream(openFileOutput("thoughtsList.txt", MODE_APPEND));
        String question1 = $TV(R.id.question_1).getText().toString();
        String answer1 = $ET(R.id.answer_1).getText().toString();
        String question2 = $TV(R.id.question_2).getText().toString();
        String answer2 = $ET(R.id.answer_2).getText().toString();

        int[] date = calendarDates();
        String mc = Integer.toString(date[0]);
        String dc = Integer.toString(date[1]);
        String yc = Integer.toString(date[2]);

        output.println(question1 + "\t" + answer1);
        output.println(question2 + "\t" + answer2);

        //attaching the month, day of month, and year entry
        //was created, followed immediately by date modified
        //(empty string are placeholders before modification
        output.println(mc + "\t" + dc + "\t" + yc + "\t" +
                "" + "\t" + "" + "\t" + "");
        output.close();

        Intent goToMenu = new Intent(this, AllEntriesActivity.class);
        startActivity(goToMenu);
    }
}
