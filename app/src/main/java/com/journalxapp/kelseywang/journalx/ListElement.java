//This is the class representing an element in the ListView
package com.journalxapp.kelseywang.journalx;

public class ListElement {
    private String q1;
    private String q2;
    private String month;
    private String drawableChar;
    private int color;

    //Constructor setting all parameters
    public ListElement(String q1, String q2, String month, String drawableChar, int color) {
        this.q1 = q1;
        this.q2 = q2;
        this.month = month;
        this.drawableChar = drawableChar;
        this.color = color;
    }

    //Returns first question
    public String getQ1() {
        return q1;
    }

    //Returns second question
    public String getQ2() {
        return q2;
    }

    //Returns month abbreviation
    public String getMonth() {
        return month;
    }

    //Returns day of month to be drawn in TextDrawable
    public String getDrawableChar() {
        return drawableChar;
    }

    //Returns color int id
    public int getColor() {
        return color;
    }
}
