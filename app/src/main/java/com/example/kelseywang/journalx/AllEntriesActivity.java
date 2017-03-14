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

//TESTING MODE IS ON!
//TODO: MAKE FAVORITES DELETABLE
//TODO: MAKE VIEW FAVORITES OPTION ON HEADER BAR
//TODO: to use header, when user long clicks, set a private class boolean isThereaLongClick as well as the class variable index
//to true, and set list again with this index's elevation set higher. When header bar clicked, you set favorites
//and set isThereaLongClick to false and set list again. What about if you click elsewhere bc u clicked accidentally?
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

        List<String> thing = getList();
        for(String i : thing) {
            Log.d("THING", i);
        }
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
            case R.id.action_favorite:
                Log.d(test, "action favorite clicked");
                //setFavorite(indexLongClicked);
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

    private List<String> getFavoritedList() {
        List<String> favoritedList = new ArrayList<>();
        String q1, a1, q2, a2;
        String mc, dc, yc,
                mm, dm, ym, favorited;
        try {
            Scanner scanner = new Scanner(openFileInput("favoritedList.txt")).useDelimiter("\\t|\\n");
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
                favoritedList.add(q1 + "\t" + a1 + "\n" + q2 + "\t" + a2 + "\n"
                        + mc + "\t" + dc + "\t" + yc + "\t"
                        + mm + "\t" + dm + "\t" + ym + "\t" + favorited);
            }
        } catch (Exception e) {
            // do nothing
        }
        return favoritedList;
    }


    private void setFavorite(ListView list, int index) {
        ListElement itemsAtPosition = (ListElement) list.getItemAtPosition(index);
        String thoughtLongClicked = itemsAtPosition.getQ1() + "\n" + itemsAtPosition.getQ2();
        List<String> questionsArraylist = getQuestionsList();
        List<String> thoughtsArraylist = getList();
        switchToFavorited(questionsArraylist, thoughtsArraylist);
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
                thoughtArraylist.add(q1 + "\n" + q2 + "\n" + MONTHS_ABBREVS.get(Integer.parseInt(mc)) + "\n" + dc + "\n" + favorited);
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
            int elevation = 5;
            if(elements[4].equals("true")) {
                elevation = 30;
                Log.d(test, "RAISED ELEVATION");
            }
            ListElement newElement = new ListElement(elements[0], elements[1], elements[2], elements[3], elevation);
            objects.add(newElement);
        }

        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        $LV(R.id.thought_list).setAdapter(customAdapter);
    }

    //Deletes items when thoughts are long clicked
    //and updates the list with helper function removeLineFromFile
    @Override
    public boolean onItemLongClick(ListView list, int index) {
        indexLongClicked = index;
        setFavorite(list, index);
        setList();
        //list.getAdapter().getView(index, null, list).findViewById(R.id.month).getContext();
        //list.

        /*ListElement itemsAtPosition = (ListElement) list.getItemAtPosition(index);
        String thoughtLongClicked = itemsAtPosition.getQ1() + "\n" + itemsAtPosition.getQ2();
        List<String> questionsArraylist = getQuestionsList();
        List<String> thoughtsArraylist = getList();
        removeLineFromFile(questionsArraylist, thoughtsArraylist, thoughtLongClicked);
        setList();*/
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


    private void switchToFavorited(List<String> questionsArraylist, List<String> thoughtsArraylist) {
        //replacing line in thoughtsList
        replaceFavoritedInThoughtsList(questionsArraylist, thoughtsArraylist, indexLongClicked, "true");
        //adding line in favoritesList
        PrintStream writer = new PrintStream(openFileOutput("favoritedList.txt", MODE_APPEND));

        //TODO: CHECK IF ITS ALREADY FAVORITED, IF IT IS THEN REMOVE IT AS A FAVORITE
        writer.println(thoughtsArraylist.get(indexLongClicked));
        writer.close();
    }
    private String[] splitOneEntryLine(String line) {
        String[] retval = line.split("\\t|\\n");
        for(String i : retval) {
            Log.d("RETVAL", i);
        }
        return retval;
    }

    private void replaceFavoritedInThoughtsList(List<String> questionsArraylist, List<String> thoughtsArraylist,
                                     int index, String favorited) {
        PrintStream writer = new PrintStream(openFileOutput("thoughtsList.txt", MODE_PRIVATE));
        String[] line = splitOneEntryLine(thoughtsArraylist.get(index));
        thoughtsArraylist.set(index, line[0] + "\t" + line[1] + "\n" + line[2] + "\t" + line[3]
                + "\n" + line[4] + "\t" + line[5] + "\t" + line[6] + "\t"
                + line[7] + "\t" + line[8] + "\t" + line[9] + "\t" + favorited);

        for (int i = 0; i < thoughtsArraylist.size(); i++) {
            writer.println(thoughtsArraylist.get(i));
        }
        writer.close();
    }

}

