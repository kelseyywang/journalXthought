package com.example.kelseywang.journalx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import stanford.androidlib.*;

public class OldEntryActivity extends SimpleActivity {

    private String[] dateCreated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_entry);
        setAllText();
    }
    private void setAllText() {
        Intent intent = getIntent();
        String thisThought = intent.getStringExtra("thoughtClicked");
        setQuestions(thisThought);
        setAnswers(thisThought);
    }
    private void setQuestions(String questions) {
        String[] questionsArray = questions.split("\\r?\\n");
        $TV(R.id.question_1).setText(questionsArray[0]);
        $TV(R.id.question_2).setText(questionsArray[1]);
    }
    private void setAnswers(String questions) {
        String answer1 = "";
        String answer2 = "";
        String mc, dc, yc, mm, dm, ym;
        String[] questionsArray = questions.split("\\r?\\n");
        try {
            Scanner scanner = new Scanner(openFileInput("thoughtsList.txt")).useDelimiter("\\t|\\n");
            while (scanner.hasNext()) {
                String checkQuestion = scanner.next();
                if(checkQuestion.equals(questionsArray[0])) {
                    answer1 = scanner.next();
                    scanner.next(); //question 2
                    answer2 = scanner.next();
                    //TODO: see if this is ok
                    mc = scanner.next();
                    dc = scanner.next();
                    yc = scanner.next();
                    mm = scanner.next();
                    dm = scanner.next();
                    ym = scanner.next();
                    storeDateCreated(mc, dc, yc);
                    setDates(mc, dc, yc, mm, dm, ym);
                }
            }
            $ET(R.id.answer_1).setText(answer1);
            $ET(R.id.answer_2).setText(answer2);
        } catch (Exception e) {
            // do nothing
        }
    }

    private void storeDateCreated(String mc, String dc, String yc) {
        dateCreated = new String[]{mc, dc, yc};
    }

    private void setDates(String mc, String dc, String yc, String mm, String dm, String ym) {
        $TV(R.id.date_created).setText("Created: " + Integer.toString(Integer.parseInt(mc) + 1) + "/" + dc + "/" + yc);
        if(!mm.isEmpty()) {
            $TV(R.id.date_modified).setText("Modified: " + Integer.toString(Integer.parseInt(mm) + 1) + "/" + dm + "/" + ym);
        }
    }

    //Splits parameter string into two
    private String[] splitLineHelper(String line) {
        String[] QAndA = line.split("\\r?\\n");
        return QAndA;
    }

    private void replaceLineFromFile(List<String> questionsArraylist, List<String> thoughtsArraylist,
                                     String question, String newAnswer, String question2, String newAnswer2,
                                     String mc, String dc, String yc) {
        Calendar calendar = Calendar.getInstance();
        String mm = Integer.toString(calendar.get(Calendar.MONTH));
        String dm = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String ym = Integer.toString(calendar.get(Calendar.YEAR));

        //PrintStream tempOutput2 = new PrintStream(openFileOutput("temp_thoughts2.txt", MODE_APPEND));
        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        for (int i = 0; i < questionsArraylist.size(); i++) {
            if (splitLineHelper(questionsArraylist.get(i))[0].equals(question)) {
                //changing date modified to today
                thoughtsArraylist.set(i, question + "\t" + newAnswer + "\n" + question2 + "\t" + newAnswer2
                        + "\n" + mc + "\t" + dc + "\t" + yc + "\t"
                        + mm + "\t" + dm + "\t" + ym);
                break;
            }
        }
        for (int i = 0; i < thoughtsArraylist.size(); i++) {
            writer.println(thoughtsArraylist.get(i));
        }
        writer.close();
        /*File filesDirectory2 = getFilesDir();
        File tempFile2 = new File(filesDirectory2, "temp_thoughts2.txt");
        File oldFile2 = new File(filesDirectory2, "thoughtsList.txt");
        tempFile2.renameTo(oldFile2);*/
    }

    private List<String> getList() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "\t" + a1 + "\n" + q2 + "\t" + a2 + "\n"
                        + mc + "\t" + dc + "\t" + yc + "\t"
                        + mm + "\t" + dm + "\t" + ym);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    private List<String> getQuestionsList() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "\n" + q2);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    public void saveClicked(View view) {
        String question1 = $TV(R.id.question_1).getText().toString();
        String answer1 = $ET(R.id.answer_1).getText().toString();
        String question2 = $TV(R.id.question_2).getText().toString();
        String answer2 = $ET(R.id.answer_2).getText().toString();

        replaceLineFromFile(getQuestionsList(), getList(), question1, answer1,
                question2, answer2, dateCreated[0], dateCreated[1], dateCreated[2]);

        Intent goToMenu = new Intent(this, AllEntriesActivity.class);
        startActivity(goToMenu);
    }

}