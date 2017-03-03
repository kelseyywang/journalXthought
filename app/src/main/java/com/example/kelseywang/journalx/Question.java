/**
 * Created by kelseywang on 3/3/17.
 */
package com.example.kelseywang.journalx;

import java.io.Serializable;

public class Question implements Serializable {
    public String questionText;

    public Question() {
        // empty
    }

    public String getQuestionText() {
        return questionText;
    }
}

