package com.example.kelseywang.journalx;

/**
 * Created by kelseywang on 3/6/17.
 */

public class ListElement {
    private String q1;
    private String q2;
    private String drawableChar;

    public ListElement(String q1, String q2, String drawableChar) {
        this.q1 = q1;
        this.q2 = q2;
        this.drawableChar = drawableChar;
    }

    public String getQ1() {
        return q1;
    }

    public String getQ2() {
        return q2;
    }

    public String getDrawableChar() {
        return drawableChar;
    }
}
