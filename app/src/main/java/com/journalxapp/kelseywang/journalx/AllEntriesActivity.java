package com.journalxapp.kelseywang.journalx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import stanford.androidlib.*;
import java.io.PrintStream;
import java.util.*;

public class AllEntriesActivity extends SimpleActivity {
    private final List<String> MONTHS_ABBREVS = new ArrayList<>(Arrays.asList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
    private String test = "APPLICABLE";
    private int indexLongClicked;
    private String currentList;
    private int scrollIndex;
    int scrollTop;
    private final String HELP_TEXT =
            "Take a few minutes out of your day to think about yourself in a context " +
                    "that you define. journalx was created with simplicity in mind, " +
                    "prompting you with daily questions that promote both self-reflection and innovative thinking. \n\n" +
                    "Tap on a thought to edit it. Prompts are loaded once a day, requiring internet connection, " +
                    "but offline mode lets you browse and edit old thoughts. \n\n" +
                    "Hold down on a thought to favorite it, and hold down again to un-favorite. " +
                    "Favorited thoughts are colored in your list.\n\n" +
                    "Questions? Feedback? Email me at askjournalx@gmail.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_entries);
        if (savedInstanceState != null) {
            CharSequence currList = savedInstanceState.getCharSequence("currentList");
            setView(currList.toString());
        }
        else {
            setView("All");
        }
        showInstructions();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.all_entries_toolbar);
        setSupportActionBar(myToolbar);
        $LV(R.id.thought_list).setOnItemClickListener(this);
        $LV(R.id.thought_list).setOnItemLongClickListener(this);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("currentList", currentList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all:
                setView("All");
                return true;
            case R.id.action_favorite:
                setView("Favorited");
                return true;
            case R.id.action_help:
                setView("Help");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    public void setView(String type) {
        currentList = type;
        if(type.equals("All")) {
            $TV(R.id.help_textview).setVisibility(View.GONE);
            $TV(R.id.all_entries_header).setText("All your thoughts");
            setList(type);
        }
        else if (type.equals("Favorited")) {
            setList(type);
            $TV(R.id.help_textview).setVisibility(View.GONE);
            $TV(R.id.all_entries_header).setText("Favorites");
            setList(type);
        }
        else {
            $TV(R.id.help_textview).setVisibility(View.VISIBLE);
            $TV(R.id.all_entries_header).setText("Help");
            $TV(R.id.help_textview).setText(HELP_TEXT);
        }
    }
    private void showInstructions() {
        SharedPreferences prefs = getSharedPreferences("ratings.txt", Context.MODE_PRIVATE);
        String isFirstOpen = prefs.getString("first open", "true");
        if (isFirstOpen.equals("true") && $LV(R.id.thought_list).getAdapter().getCount() < 2) {
            $TV(R.id.first_instructions).setVisibility(View.VISIBLE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString("first open", "false");
            prefsEditor.commit();
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

    private List<String> getList(String whatList) {
        List<String> retArraylist = new ArrayList<>();
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
                if(whatList.equals("All")) {
                    retArraylist.add(q1 + "\t" + a1 + "\n" + q2 + "\t" + a2 + "\n"
                            + mc + "\t" + dc + "\t" + yc + "\t"
                            + mm + "\t" + dm + "\t" + ym + "\t" + favorited);
                }
                else if (whatList.equals("Favorited")) {
                    if(favorited.equals("true")) {
                        retArraylist.add(q1 + "\t" + a1 + "\n" + q2 + "\t" + a2 + "\n"
                                + mc + "\t" + dc + "\t" + yc + "\t"
                                + mm + "\t" + dm + "\t" + ym + "\t" + favorited);
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return retArraylist;
    }

    private List<String> getQuestionsListWithMonthsDates(String whatList) {
        List<String> retArraylist = new ArrayList<>();
        String q1, a1, q2, a2;
        String mc, dc, yc,
                mm, dm, ym, favorited;
        List<String> origList;
        if(whatList.equals("All")) {
            origList = getList("All");
        }
        else if (whatList.equals("Favorited")) {
            origList = getList("Favorited");
        }
        else {
            origList = getList("All");
        }
        for(String element : origList) {
            String[] split = splitOneEntryLine(element);
            retArraylist.add(split[0] + "\n" + split[2] + "\n" + MONTHS_ABBREVS.get(Integer.parseInt(split[4])) + "\n" + split[5] + "\n" + split[10]);
        }
        return retArraylist;
    }

    //Sets list
    private void setList(String whatList) {
        ArrayList<ListElement> objects = new ArrayList<>();
        List<String> questionsWithDatesMonths = getQuestionsListWithMonthsDates(whatList);
        for (int i = questionsWithDatesMonths.size() - 1; i >= 0; i--) {
            String questionWithDateMonth = questionsWithDatesMonths.get(i);
            String[] elements = questionWithDateMonth.split("\\r?\\n");
            int color = 0;
            if (whatList.equals("All")) {
                if (elements[4].equals("true")) {
                    color = 1;
                }
            }
            else {
                color = 1;
            }
            ListElement newElement = new ListElement(elements[0], elements[1], elements[2], elements[3], color);
            objects.add(newElement);
        }

        CustomAdapter customAdapter = new CustomAdapter(this, objects);
        $LV(R.id.thought_list).setAdapter(customAdapter);
    }

    @Override
    public boolean onItemLongClick(ListView list, int index) {
        scrollIndex = list.getFirstVisiblePosition();
        View v = list.getChildAt(0);
        scrollTop = (v == null) ? 0 : (v.getTop() - list.getPaddingTop());

        int backwardsIndex = list.getAdapter().getCount() - 1 - index;
        indexLongClicked = backwardsIndex;
        setFavorited();
        setList(currentList);
        list.setSelectionFromTop(scrollIndex, scrollTop);
        return true;
    }

    private void setFavorited() {
        List<String> thoughtsArraylist = getList("All");
        String indexFavorited = splitOneEntryLine(thoughtsArraylist.get(indexLongClicked))[10];
        if(indexFavorited.equals("true")) {
            replaceFavoritedInThoughtsList(thoughtsArraylist, indexLongClicked, "false");
        }
        else {
            replaceFavoritedInThoughtsList(thoughtsArraylist, indexLongClicked, "true");
        }

    }
    private String[] splitOneEntryLine(String line) {
        String[] retval = line.split("\\t|\\n");
        return retval;
    }

    private void replaceFavoritedInThoughtsList(List<String> thoughtsArraylist,
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

