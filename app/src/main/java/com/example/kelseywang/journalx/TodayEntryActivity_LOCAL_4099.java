//TODO: make questions not repeat, and go in order one by one
package com.example.kelseywang.journalx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import stanford.androidlib.*;

import java.io.PrintStream;
import java.util.*;
import java.text.*;


public class TodayEntryActivity extends SimpleActivity {

    private List<String> questions = new ArrayList<>();
    private Map<Integer, String> dailyQuestions = new HashMap<Integer, String>();
    private final int START_DAY_OF_YEAR = 35; //should be day which app is launched

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_entry);
        setDate();
        populateQuestionsArray();
        populateQuestionsMap();
        setQuestions();

    }

    //TODO: put this into the Strings file since this is predetermined
    private void populateQuestionsArray() {
        //List<String> questions = new ArrayList<>();
        questions.addAll(Arrays.asList("q1", "q2", "q3", "q4", "q5",
                "q6", "q7", "q8", "q9", "q10",
                "q11", "q12", "q13", "q14", "q15",
                "q16", "q17", "q18", "q19", "q20",
                "q21", "q22", "q23", "q24", "q25",
                "q26", "q27", "q28", "q29", "q30"));
    }

    private void populateQuestionsMap() {
        for(int i = 0; i < questions.size()-1; i+=2) {
            dailyQuestions.put(START_DAY_OF_YEAR + i/2, questions.get(i));
            dailyQuestions.put(START_DAY_OF_YEAR + i/2 + 1000, questions.get(i+1)); //adds 1000 to key for second question
        }
    }

    private void setQuestions() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        toast("THE DAY IS "+ day);
        String q1 = dailyQuestions.get(day);
        handleOpenedToday(q1);
        String q2 = dailyQuestions.get(day + 1000);
        $TV(R.id.question_1).setText(q1);
        $TV(R.id.question_2).setText(q2);
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
        Calendar calendar = Calendar.getInstance();
        String m = Integer.toString(calendar.get(Calendar.MONTH));
        String d = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String y = Integer.toString(calendar.get(Calendar.YEAR));
        $TV(R.id.date_view).setText("Today is: " + m + "/" + d + "/" + y);
    }

    public void saveClicked(View view) {
        PrintStream output = new PrintStream(openFileOutput("thoughtsList.txt", MODE_APPEND));
        String question1 = $TV(R.id.question_1).getText().toString();
        String answer1 = $ET(R.id.answer_1).getText().toString();
        String question2 = $TV(R.id.question_2).getText().toString();
        String answer2 = $ET(R.id.answer_2).getText().toString();

        Calendar calendar = Calendar.getInstance();
        String mc = Integer.toString(calendar.get(Calendar.MONTH));
        String dc = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String yc = Integer.toString(calendar.get(Calendar.YEAR));

        output.println(question1 + "\t" + answer1);
        output.println(question2 + "\t" + answer2);

        //attaching the month, day of month, and year entry
        //was created, followed immediately by date modified
        //(empty string are placeholders before modification
        output.println(mc + "\t" + dc + "\t" + yc + "\n" +
                "" + "\t" + "" + "\t" + "");
        output.close();

        Intent goToMenu = new Intent(this, AllEntriesActivity.class);
        startActivity(goToMenu);
    }
}
