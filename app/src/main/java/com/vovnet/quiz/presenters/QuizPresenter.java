package com.vovnet.quiz.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vovnet.quiz.R;
import com.vovnet.quiz.data.QuizDatabase;
import com.vovnet.quiz.models.Quiz;
import com.vovnet.quiz.views.QuizView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Vladimir on 05.02.2018.
 */

@InjectViewState
public class QuizPresenter extends MvpPresenter<QuizView> {

    private static final int CORRECT_RATE = 3;
    private static final int INCORRECT_RATE = 1;

    private List<Quiz> mQuizList = new ArrayList<>();
    private String mDbName;
    private String mTableName;
    private Context mContext;
    private Quiz mCurrentQuiz;

    public void onCreate(final Context context, String dbName, String tableName) {
        mContext = context;
        mDbName = dbName;
        mTableName = QuizDatabase.getTableByName(tableName);

        loadQuestions();
    }

    public void onClickTrue() {
        mCurrentQuiz.setViews(mCurrentQuiz.getViews() + 1);
        mCurrentQuiz.setCorrect(mCurrentQuiz.getCorrect() + 1);
        mCurrentQuiz.setRating(mCurrentQuiz.getRating() + CORRECT_RATE);
        saveQuiz(mCurrentQuiz);
        nextQuestion();
    }

    public void onClickFalse() {
        mCurrentQuiz.setViews(mCurrentQuiz.getViews() + 1);
        mCurrentQuiz.setIncorrect(mCurrentQuiz.getIncorrect() + 1);
        mCurrentQuiz.setRating(mCurrentQuiz.getRating() + INCORRECT_RATE);
        getViewState().showAnswer(mCurrentQuiz.getAnswer());
        saveQuiz(mCurrentQuiz);
    }

    public void onClickNext() {
        nextQuestion();
    }

    public String getTableName() {
        return mTableName;
    }

    private void nextQuestion() {
        showQuestion();
    }

    private Quiz getRandomQuiz() {
        Collections.sort(mQuizList);
        int totalWeight = getTotalWeight();

        for (Quiz q : mQuizList) {
            if (totalWeight <= q.getRating()) {
                return q;
            }
            totalWeight -= q.getRating();
        }
        return  mQuizList.get(new Random().nextInt(mQuizList.size()-1));
    }

    private void showQuestion() {
        mCurrentQuiz = getRandomQuiz();
        getViewState().showQuestion(mCurrentQuiz.getQuestion());
    }

    private void showError(String msg) {
        getViewState().showError(msg);
    }

    @SuppressLint("StaticFieldLeak")
    private void saveQuiz(final Quiz quiz) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                QuizDatabase db = new QuizDatabase(mContext, mDbName);
                db.saveQuiz(mTableName, quiz);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadQuestions() {
        new AsyncTask<Void, Void, Void>() {
            private String mError = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getViewState().showLoading(true);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!mError.isEmpty()) {
                    showError(mError);
                } else {
                    if (mQuizList == null && mQuizList.size() <= 0) {
                        showError(mContext.getString(R.string.table_ia_empty));
                    } else {
                        getViewState().showLoading(false);
                        showQuestion();
                    }
                }

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    QuizDatabase db = new QuizDatabase(mContext, mDbName);
                    mQuizList = db.getQuiz(mTableName);
                } catch (Exception e) {
                    mError = mContext.getString(R.string.db_error);
                }

                return null;
            }
        }.execute();
    }

    private int getTotalWeight() {
        int result = 1;
        for (Quiz q : mQuizList) {
            result += q.getRating();
        }
        return result;
    }

}
