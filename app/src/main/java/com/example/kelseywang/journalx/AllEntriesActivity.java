package com.example.kelseywang.journalx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.amulyakhare.textdrawable.TextDrawable;

import stanford.androidlib.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;


public class AllEntriesActivity extends SimpleActivity {
    final List<String> MONTHS_ABBREVS = new ArrayList<>(Arrays.asList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
    private String test = "APPLICABLE";
    private int indexLongClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_entries);
        Log.d(test, "STARTED");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#418a8e")));
        Toolbar myToolbar = (Toolbar) findViewById(R.id.all_entries_toolbar);
        setSupportActionBar(myToolbar);


        setList();
        $LV(R.id.thought_list).setOnItemClickListener(this);
        $LV(R.id.thought_list).setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_entries_toolbar:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemClick(ListView list, int index) {
        ListElement itemsAtPosition = (ListElement) list.getItemAtPosition(index);
        String thoughtClicked = itemsAtPosition.getQ1() + "\n" + itemsAtPosition.getQ2();
        Intent goToEntry = new Intent(this, OldEntryActivity.class);
        goToEntry.putExtra("thoughtClicked", thoughtClicked);
        startActivity(goToEntry);
    }

    //This list has questions and answers--one question and answer
    //per line, separated by a tab. Two consecutive lines form
    //questions and answers for one single day
    private List<String> getList() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "\t" + a1 + "\n" + q2 + "\t" + a2 + "\n"
                        + mc + "\t" + dc + "\t" + yc + "\t"
                        + mm + "\t" + dm + "\t" + ym + "\t" + favorited);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    //Questions list has just questions--one per line, and two
    //consecutive ones forming the questions for one single day
    private List<String> getQuestionsList() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "\n" + q2);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    //Questions list has just questions--one per line, and two
    //consecutive ones forming the questions for one single day
    private List<String> getQuestionsListWithMonthsDates() {
        List<String> thoughtArraylist = new ArrayList<>();
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
                thoughtArraylist.add(q1 + "\n" + q2 + "\n" + MONTHS_ABBREVS.get(Integer.parseInt(mc)) + "\n" + dc);
            }
        } catch (Exception e) {
            // do nothing
        }
        return thoughtArraylist;
    }

    //Sets list
    private void setList() {
        ArrayList<ListElement> objects = new ArrayList<>();
        List<String> questionsWithDatesMonths = getQuestionsListWithMonthsDates();
        for (String questionWithDateMonth : questionsWithDatesMonths) {
            String[] elements = questionWithDateMonth.split("\\r?\\n");
            ListElement newElement = new ListElement(elements[0], elements[1], elements[2], elements[3]);
            objects.add(newElement);
        }

        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        $LV(R.id.thought_list).setAdapter(customAdapter);
    }

    //Deletes items when thoughts are long clicked
    //and updates the list with helper function removeLineFromFile
    @Override
    public boolean onItemLongClick(ListView list, int index) {
        ListElement itemsAtPosition = (ListElement) list.getItemAtPosition(index);
        String thoughtLongClicked = itemsAtPosition.getQ1() + "\n" + itemsAtPosition.getQ2();
        List<String> questionsArraylist = getQuestionsList();
        List<String> thoughtsArraylist = getList();
        removeLineFromFile(questionsArraylist, thoughtsArraylist, thoughtLongClicked);
        setList();
        return true;
    }

    //Recreates the remaining file
    //from scratch without deleted item
    private void removeLineFromFile(List<String> questionsArraylist, List<String> thoughtsArraylist, String thoughtToDelete) {
        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        for (int i = 0; i < questionsArraylist.size(); i++) {
            if (questionsArraylist.get(i).equals(thoughtToDelete)) {
                questionsArraylist.remove(i);
                thoughtsArraylist.remove(i);
                break;
            }
        }
        for (int i = 0; i < thoughtsArraylist.size(); i++) {
            writer.println(thoughtsArraylist.get(i));
        }
        writer.close();
    }
}
