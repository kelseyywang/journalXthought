//This class allows user to write their entry
//for today

//TODO: all files: make sure every end of entry in list or file line has delimiter
//make sure delimiter and split line is set to appropriate delimiter (search \n or \\ or \\r or ?)
//make sure all delimiters are there
//TODO: DELETE TESTTHINGS!!! and add replacements for existing users
//TODO: IT IS NEWLINE-ING, MESSING UP THE UI
package com.journalxapp.kelseywang.journalx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import stanford.androidlib.*;
import java.io.PrintStream;
import java.util.*;

public class TodayEntryActivity extends SimpleActivity {

    //Sets layout with dates, questions, etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_entry);
        setDate();
        setQuestions();
        if (savedInstanceState != null) {
            CharSequence myQ1Answer = savedInstanceState.getCharSequence("q1Answer");
            CharSequence myQ2Answer = savedInstanceState.getCharSequence("q2Answer");
            $ET(R.id.answer_1).setText(myQ1Answer.toString());
            $ET(R.id.answer_2).setText(myQ2Answer.toString());
        }
    }

    //Saves current text in answer EditTexts in case of orientation change
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("q1Answer", $ET(R.id.answer_1).getText().toString());
        outState.putCharSequence("q2Answer", $ET(R.id.answer_2).getText().toString());
    }

    //Returns current month, day and year in String array
    private int[] calendarDates() {
        Calendar calendar = Calendar.getInstance();
        int[] dates = {calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR)};
        return dates;
    }

    //Gets questions from intent and sets question TextViews
    private void setQuestions() {
        Intent intent = getIntent();
        String todayQ1 = intent.getStringExtra("q1");
        String todayQ2 = intent.getStringExtra("q2");
        $TV(R.id.question_1).setText(todayQ1);
        $TV(R.id.question_2).setText(todayQ2);
    }

    //Sets date TextView
    private void setDate() {
        int[] date = calendarDates();
        String m = Integer.toString(date[0] + 1);
        String d = Integer.toString(date[1]);
        String y = Integer.toString(date[2]);
        $TV(R.id.date_view).setText("Today is: " + m + "/" + d + "/" + y);
    }

    //Adds entry to saved file
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

        //attaching the month, day of month, and year entry
        //was created, followed immediately by date modified
        //(empty string are placeholders before modification
        //and favorited
        output.print(question1 + "@@@@" + answer1 + "@@@@" + question2 + "@@@@" + answer2 + "@@@@"
                + mc + "@@@@" + dc + "@@@@" + yc + "@@@@" +
                "" + "@@@@" + "" + "@@@@" + "" + "@@@@" + "false" + "@@@@");
        output.close();
        Intent goToMenu = new Intent(this, AllEntriesActivity.class);
        startActivity(goToMenu);
    }
}
