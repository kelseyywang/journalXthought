//Allows user to modify and save old entries
package com.journalxapp.kelseywang.journalx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import stanford.androidlib.*;

public class OldEntryActivity extends SimpleActivity {
    private String[] dateCreated;
    private String favorited;

    //Sets text for questions and answers using helper methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_entry);
        setAllText();
        if (savedInstanceState != null) {
            CharSequence myQ1Answer = savedInstanceState.getCharSequence("q1Answer");
            CharSequence myQ2Answer = savedInstanceState.getCharSequence("q2Answer");
            $ET(R.id.answer_1).setText(myQ1Answer.toString());
            $ET(R.id.answer_2).setText(myQ2Answer.toString());
        }
    }

    //Saves current text in answer EditTexts in case of orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("q1Answer", $ET(R.id.answer_1).getText().toString());
        outState.putCharSequence("q2Answer", $ET(R.id.answer_2).getText().toString());
    }

    //Handles setting text for questions and answers
    private void setAllText() {
        Intent intent = getIntent();
        String thisThought = intent.getStringExtra("thoughtClicked");
        setQuestions(thisThought);
        setAnswers(thisThought);
    }

    //Sets questions
    private void setQuestions(String questions) {
        String[] questionsArray = questions.split("@@@@");
        $TV(R.id.question_1).setText(questionsArray[0]);
        $TV(R.id.question_2).setText(questionsArray[1]);
    }

    //Sets answers by going through saved file
    private void setAnswers(String questions) {
        String answer1 = "";
        String answer2 = "";
        String mc, dc, yc, mm, dm, ym, favorited;
        String[] questionsArray = questions.split("@@@@");
        try {
            Scanner scanner = new Scanner(openFileInput("thoughtsList.txt")).useDelimiter("@@@@");
            while (scanner.hasNext()) {
                String checkQuestion = scanner.next();
                if(checkQuestion.equals(questionsArray[0])) {
                    answer1 = scanner.next();
                    scanner.next(); //question 2
                    answer2 = scanner.next();
                    mc = scanner.next();
                    dc = scanner.next();
                    yc = scanner.next();
                    mm = scanner.next();
                    dm = scanner.next();
                    ym = scanner.next();
                    favorited = scanner.next();
                    storeDateCreated(mc, dc, yc);
                    storeFavorited(favorited);
                    setDates(mc, dc, yc, mm, dm, ym);
                }
            }
            $ET(R.id.answer_1).setText(answer1);
            $ET(R.id.answer_2).setText(answer2);
        } catch (Exception e) {
        }
    }

    //Stores date, month, year created in a private class variable
    private void storeDateCreated(String mc, String dc, String yc) {
        dateCreated = new String[]{mc, dc, yc};
    }

    //Stores whether entry was favorited
    private void storeFavorited(String wasFavorited) {
        favorited = wasFavorited;
    }

    //Sets date TextViews on top of page
    private void setDates(String mc, String dc, String yc, String mm, String dm, String ym) {
        $TV(R.id.date_created).setText("Entry from " + Integer.toString(Integer.parseInt(mc) + 1) + "/" + dc + "/" + yc);
        if(!mm.isEmpty() && !(mm.equals(mc) && dm.equals(dc) && ym.equals(yc))) {
            $TV(R.id.date_modified).setText("Last modified on " + Integer.toString(Integer.parseInt(mm) + 1) + "/" + dm + "/" + ym);
        }
    }

    //Splits parameter string into two by new line
    private String[] splitLineHelper(String line) {
        String[] QAndA = line.split("@@@@");
        return QAndA;
    }

    //Replaces line from saved file to reflect modifications
    private void replaceLineFromFile(List<String> questionsArraylist, List<String> thoughtsArraylist,
                                     String question, String newAnswer, String question2, String newAnswer2,
                                     String mc, String dc, String yc, String favorited) {
        Calendar calendar = Calendar.getInstance();
        String mm = Integer.toString(calendar.get(Calendar.MONTH));
        String dm = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String ym = Integer.toString(calendar.get(Calendar.YEAR));
        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        for (int i = 0; i < questionsArraylist.size(); i++) {
            if (splitLineHelper(questionsArraylist.get(i))[0].equals(question)) {
                //Changing date modified to today
                thoughtsArraylist.set(i, question + "@@@@" + newAnswer + "@@@@" + question2 + "@@@@" + newAnswer2
                        + "@@@@" + mc + "@@@@" + dc + "@@@@" + yc + "@@@@"
                        + mm + "@@@@" + dm + "@@@@" + ym + "@@@@" + favorited);
                break;
            }
        }
        for (int i = 0; i < thoughtsArraylist.size(); i++) {
            writer.print(thoughtsArraylist.get(i) + "@@@@");
        }
        writer.close();
    }
    //TODO: make the last word of a line have a delimiter too

    //Returns list of entries
    private List<String> getList() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "@@@@" + a1 + "@@@@" + q2 + "@@@@" + a2 + "@@@@"
                        + mc + "@@@@" + dc + "@@@@" + yc + "@@@@"
                        + mm + "@@@@" + dm + "@@@@" + ym + "@@@@" + favorited);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    //Returns list of questions from entries
    private List<String> getQuestionsList() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "@@@@" + q2);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    //Saves modifications and transitions to AllEntriesActivity
    public void saveClicked(View view) {
        String question1 = $TV(R.id.question_1).getText().toString();
        String answer1 = $ET(R.id.answer_1).getText().toString();
        String question2 = $TV(R.id.question_2).getText().toString();
        String answer2 = $ET(R.id.answer_2).getText().toString();
        replaceLineFromFile(getQuestionsList(), getList(), question1, answer1,
                question2, answer2, dateCreated[0], dateCreated[1], dateCreated[2], favorited);
        Intent goToAll = new Intent(this, AllEntriesActivity.class);
        startActivity(goToAll);
    }
}