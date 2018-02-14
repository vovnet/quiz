package com.vovnet.quiz.models;

import android.support.annotation.NonNull;

/**
 * Created by Vladimir on 03.02.2018.
 */

public class Quiz implements Comparable<Quiz> {

    private int mId;
    private String mQuestion;
    private String mAnswer;
    private int mViews;
    private int mCorrect;
    private int mIncorrect;
    private int mRating;

    public Quiz(int id, String question, String answer, int views, int correct, int incorrect, int rating) {
        mId = id;
        mQuestion = question;
        mAnswer = answer;
        mViews = views;
        mCorrect = correct;
        mIncorrect = incorrect;
        mRating = rating;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public int getViews() {
        return mViews;
    }

    public void setViews(int views) {
        mViews = views;
    }

    public int getCorrect() {
        return mCorrect;
    }

    public void setCorrect(int correct) {
        mCorrect = correct;
    }

    public int getIncorrect() {
        return mIncorrect;
    }

    public void setIncorrect(int incorrect) {
        mIncorrect = incorrect;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }


    @Override
    public int compareTo(@NonNull Quiz quiz) {
        return quiz.getRating() - this.getRating();
    }
}
