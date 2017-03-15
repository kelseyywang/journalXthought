package com.example.kelseywang.journalx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import stanford.androidlib.*;
import java.io.PrintStream;
import java.util.*;

public class TodayEntryActivity extends SimpleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_entry);
        setDate();
        setQuestions();
    }

    private int[] calendarDates() {
        Calendar calendar = Calendar.getInstance();
        int[] dates = {calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR)};
        return dates;
    }

    private void setQuestions() {
        Intent intent = getIntent();
        String todayQ1 = intent.getStringExtra("q1");
        String todayQ2 = intent.getStringExtra("q2");
        $TV(R.id.question_1).setText(todayQ1);
        $TV(R.id.question_2).setText(todayQ2);
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
        //and favorited
        output.println(mc + "\t" + dc + "\t" + yc + "\t" +
                "" + "\t" + "" + "\t" + "" + "\t" + "false");
        output.close();
        Intent goToMenu = new Intent(this, AllEntriesActivity.class);
        startActivity(goToMenu);
    }
}
